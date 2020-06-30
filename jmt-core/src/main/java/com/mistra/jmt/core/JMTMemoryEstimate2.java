package com.mistra.jmt.core;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryVisitor;
import org.apache.lucene.util.Accountable;
import org.apache.lucene.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/7 18:41
 * @ Description:
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
@Component
public class JMTMemoryEstimate2 {

    @Autowired
    private JMTAutoConfiguration jmtAutoConfiguration;

    public static int ACCURACY_NUM = 100;
    public static final long ONE_KB = 1024;
    public static final long ONE_MB = ONE_KB * ONE_KB;
    public static final long ONE_GB = ONE_KB * ONE_MB;
    public final static boolean COMPRESSED_REFS_ENABLED;
    public final static int NUM_BYTES_OBJECT_REF;
    public final static int NUM_BYTES_OBJECT_HEADER;
    public final static int NUM_BYTES_ARRAY_HEADER;
    public final static int NUM_BYTES_OBJECT_ALIGNMENT;
    private final static Map<Class<?>, ClassCache> classCacheMap = new ConcurrentHashMap<>();
    public static final Map<Class<?>, Integer> primitiveSizes;
    static final long LONG_CACHE_MIN_VALUE, LONG_CACHE_MAX_VALUE;
    static final int LONG_SIZE, STRING_SIZE;
    static final boolean JVM_IS_HOTSPOT_64BIT;
    static final String MANAGEMENT_FACTORY_CLASS = "java.lang.management.ManagementFactory";
    static final String HOTSPOT_BEAN_CLASS = "com.sun.management.HotSpotDiagnosticMXBean";
    public static final int MAX_DEPTH = 1;

    static {
        Map<Class<?>, Integer> primitiveSizesMap = new IdentityHashMap<>();
        primitiveSizesMap.put(boolean.class, 1);
        primitiveSizesMap.put(byte.class, 1);
        primitiveSizesMap.put(char.class, Character.BYTES);
        primitiveSizesMap.put(short.class, Short.BYTES);
        primitiveSizesMap.put(int.class, Integer.BYTES);
        primitiveSizesMap.put(float.class, Float.BYTES);
        primitiveSizesMap.put(double.class, Double.BYTES);
        primitiveSizesMap.put(long.class, Long.BYTES);
        primitiveSizes = Collections.unmodifiableMap(primitiveSizesMap);
    }

    private JMTMemoryEstimate2() {
    }

    @PostConstruct
    private void init() {
        ACCURACY_NUM = jmtAutoConfiguration.getCapacity();
    }

    static {
        if (Constants.JRE_IS_64BIT) {
            boolean compressedOops = false;
            int objectAlignment = 8;
            boolean isHotspot = false;
            try {
                final Class<?> beanClazz = Class.forName(HOTSPOT_BEAN_CLASS);
                final Object hotSpotBean = Class.forName(MANAGEMENT_FACTORY_CLASS)
                        .getMethod("getPlatformMXBean", Class.class)
                        .invoke(null, beanClazz);
                if (hotSpotBean != null) {
                    isHotspot = true;
                    final Method getVMOptionMethod = beanClazz.getMethod("getVMOption", String.class);
                    try {
                        final Object vmOption = getVMOptionMethod.invoke(hotSpotBean, "UseCompressedOops");
                        compressedOops = Boolean.parseBoolean(vmOption.getClass().getMethod("getValue").invoke(vmOption).toString());
                    } catch (ReflectiveOperationException | RuntimeException e) {
                        isHotspot = false;
                    }
                    try {
                        final Object vmOption = getVMOptionMethod.invoke(hotSpotBean, "ObjectAlignmentInBytes");
                        objectAlignment = Integer.parseInt(vmOption.getClass().getMethod("getValue").invoke(vmOption).toString());
                    } catch (ReflectiveOperationException | RuntimeException e) {
                        isHotspot = false;
                    }
                }
            } catch (ReflectiveOperationException | RuntimeException e) {
                isHotspot = false;
            }
            JVM_IS_HOTSPOT_64BIT = isHotspot;
            COMPRESSED_REFS_ENABLED = compressedOops;
            NUM_BYTES_OBJECT_ALIGNMENT = objectAlignment;
            NUM_BYTES_OBJECT_REF = COMPRESSED_REFS_ENABLED ? 4 : 8;
            NUM_BYTES_OBJECT_HEADER = 8 + NUM_BYTES_OBJECT_REF;
            NUM_BYTES_ARRAY_HEADER = (int) alignObjectSize(NUM_BYTES_OBJECT_HEADER + Integer.BYTES);
        } else {
            JVM_IS_HOTSPOT_64BIT = false;
            COMPRESSED_REFS_ENABLED = false;
            NUM_BYTES_OBJECT_ALIGNMENT = 8;
            NUM_BYTES_OBJECT_REF = 4;
            NUM_BYTES_OBJECT_HEADER = 8;
            NUM_BYTES_ARRAY_HEADER = NUM_BYTES_OBJECT_HEADER + Integer.BYTES;
        }
        long longCacheMinValue = 0;
        while (longCacheMinValue > Long.MIN_VALUE && Long.valueOf(longCacheMinValue - 1) == Long.valueOf(longCacheMinValue - 1)) {
            longCacheMinValue -= 1;
        }
        long longCacheMaxValue = -1;
        while (longCacheMaxValue < Long.MAX_VALUE && Long.valueOf(longCacheMaxValue + 1) == Long.valueOf(longCacheMaxValue + 1)) {
            longCacheMaxValue += 1;
        }
        LONG_CACHE_MIN_VALUE = longCacheMinValue;
        LONG_CACHE_MAX_VALUE = longCacheMaxValue;
        LONG_SIZE = (int) shallowSizeOfInstance(Long.class);
        STRING_SIZE = (int) shallowSizeOfInstance(String.class);
    }

    public static final long HASHTABLE_RAM_BYTES_PER_ENTRY = 2 * NUM_BYTES_OBJECT_REF * 2;
    public static final long LINKED_HASHTABLE_RAM_BYTES_PER_ENTRY = HASHTABLE_RAM_BYTES_PER_ENTRY + 2 * NUM_BYTES_OBJECT_REF;

    public static long alignObjectSize(long size) {
        size += (long) NUM_BYTES_OBJECT_ALIGNMENT - 1L;
        return size - (size % NUM_BYTES_OBJECT_ALIGNMENT);
    }

    public static long sizeOf(Long value) {
        if (value >= LONG_CACHE_MIN_VALUE && value <= LONG_CACHE_MAX_VALUE) {
            return 0;
        }
        return LONG_SIZE;
    }

    public static long sizeOf(byte[] arr) {
        return alignObjectSize((long) NUM_BYTES_ARRAY_HEADER + arr.length);
    }

    public static long sizeOf(boolean[] arr) {
        return alignObjectSize((long) NUM_BYTES_ARRAY_HEADER + arr.length);
    }

    public static long sizeOf(char[] arr) {
        return alignObjectSize((long) NUM_BYTES_ARRAY_HEADER + (long) Character.BYTES * arr.length);
    }

    public static long sizeOf(short[] arr) {
        return alignObjectSize((long) NUM_BYTES_ARRAY_HEADER + (long) Short.BYTES * arr.length);
    }

    public static long sizeOf(int[] arr) {
        return alignObjectSize((long) NUM_BYTES_ARRAY_HEADER + (long) Integer.BYTES * arr.length);
    }

    public static long sizeOf(float[] arr) {
        return alignObjectSize((long) NUM_BYTES_ARRAY_HEADER + (long) Float.BYTES * arr.length);
    }

    public static long sizeOf(long[] arr) {
        return alignObjectSize((long) NUM_BYTES_ARRAY_HEADER + (long) Long.BYTES * arr.length);
    }

    public static long sizeOf(double[] arr) {
        return alignObjectSize((long) NUM_BYTES_ARRAY_HEADER + (long) Double.BYTES * arr.length);
    }

    public static long sizeOf(String[] arr) {
        long size = shallowSizeOf(arr);
        for (String s : arr) {
            if (s == null) {
                continue;
            }
            size += sizeOf(s);
        }
        return size;
    }

    public static long sizeOfMap(Map<?, ?> map, long defSize) {
        return sizeOfMap(map, 0, defSize);
    }

    private static long sizeOfMap(Map<?, ?> map, int depth, long defSize) {
        if (map == null) {
            return 0;
        }
        long size = shallowSizeOf(map);
        if (depth > MAX_DEPTH) {
            return size;
        }
        long sizeOfEntry = -1;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (sizeOfEntry == -1) {
                sizeOfEntry = shallowSizeOf(entry);
            }
            size += sizeOfEntry;
            size += sizeOfObject(entry.getKey(), depth, defSize);
            size += sizeOfObject(entry.getValue(), depth, defSize);
        }
        return alignObjectSize(size);
    }

    /**
     * Estimate Collection size
     *
     * @param collection
     * @return
     */
    public static long estimateCollection(Collection<?> collection) {
        if (collection.size() < ACCURACY_NUM) {
            return jmtSizeOfObject(collection);
        } else {
            List<Long> longList = new ArrayList<>(ACCURACY_NUM);
            for (int i = 0; i < ACCURACY_NUM; i++) {
                if (collection instanceof List<?>) {
                    longList.add(jmtSizeOfObject(((ArrayList<?>) collection).get(i)));
                } else if (collection instanceof Queue<?>) {
                    longList.add(jmtSizeOfObject(((Queue<?>) collection).peek()));
                } else if (collection instanceof Set<?>) {
                    Iterator<?> iterator = collection.iterator();
                    int count = 0;
                    while (count < ACCURACY_NUM) {
                        longList.add(jmtSizeOfObject(iterator.next()));
                        count++;
                    }
                } else {
                    return 0l;
                }
            }
            Double objectAvgSize = longList.stream().collect(Collectors.averagingDouble(Long::longValue));
            BigDecimal objectDeepSize = new BigDecimal(objectAvgSize).multiply(new BigDecimal(collection.size()));
            BigDecimal collectionShallowSize = new BigDecimal(shallowSizeOf(collection));
            return objectDeepSize.add(collectionShallowSize).longValue();
        }
    }

    public static long sizeOfCollection(Collection<?> collection, long defSize) {
        return sizeOfCollection(collection, 0, defSize);
    }

    private static long sizeOfCollection(Collection<?> collection, int depth, long defSize) {
        if (collection == null) {
            return 0;
        }
        long size = shallowSizeOf(collection);
        if (depth > MAX_DEPTH) {
            return size;
        }
        size += NUM_BYTES_ARRAY_HEADER + collection.size() * NUM_BYTES_OBJECT_REF;
        for (Object o : collection) {
            size += sizeOfObject(o, depth, defSize);
        }
        return alignObjectSize(size);
    }

    private static final class RamUsageQueryVisitor extends QueryVisitor {
        long total;
        long defSize;
        Query root;

        RamUsageQueryVisitor(Query root, long defSize) {
            this.root = root;
            this.defSize = defSize;
            if (defSize > 0) {
                total = defSize;
            } else {
                total = shallowSizeOf(root);
            }
        }

        @Override
        public void consumeTerms(Query query, Term... terms) {
            if (query != root) {
                if (defSize > 0) {
                    total += defSize;
                } else {
                    total += shallowSizeOf(query);
                }
            }
            if (terms != null) {
                total += sizeOf(terms);
            }
        }

        @Override
        public void visitLeaf(Query query) {
            if (query == root) {
                return;
            }
            if (query instanceof Accountable) {
                total += ((Accountable) query).ramBytesUsed();
            } else {
                if (defSize > 0) {
                    total += defSize;
                } else {
                    total += shallowSizeOf(query);
                }
            }
        }

        @Override
        public QueryVisitor getSubVisitor(BooleanClause.Occur occur, Query parent) {
            return this;
        }
    }

    public static long sizeOf(Query q, long defSize) {
        if (q instanceof Accountable) {
            return ((Accountable) q).ramBytesUsed();
        } else {
            RamUsageQueryVisitor visitor = new RamUsageQueryVisitor(q, defSize);
            q.visit(visitor);
            return alignObjectSize(visitor.total);
        }
    }

    /**
     * 精确计算某个对象的大小
     * 不过会占用内存，不适用于全量计算
     *
     * @param root 根对象
     * @return 所占用内存大小 bit
     */
    public static long jmtSizeOfObject(Object root) {
        final JMTMemoryEstimate.IdentityHashSet<Object> seen = new JMTMemoryEstimate.IdentityHashSet<Object>();
        final ArrayList<Object> stack = new ArrayList<Object>();
        stack.add(root);
        long totalSize = 0;
        while (!stack.isEmpty()) {
            final Object ob = stack.remove(stack.size() - 1);
            if (ob == null || seen.contains(ob)) {
                continue;
            }
            seen.add(ob);
            final Class<?> obClazz = ob.getClass();
            if (obClazz.isArray()) {
                long size = NUM_BYTES_ARRAY_HEADER;
                final int len = Array.getLength(ob);
                if (len > 0) {
                    Class<?> componentClazz = obClazz.getComponentType();
                    if (componentClazz.isPrimitive()) {
                        size += (long) len * primitiveSizes.get(componentClazz);
                    } else {
                        size += (long) NUM_BYTES_OBJECT_REF * len;
                        for (int i = len; --i >= 0; ) {
                            final Object o = Array.get(ob, i);
                            if (o != null && !seen.contains(o)) {
                                stack.add(o);
                            }
                        }
                    }
                }
                totalSize += alignObjectSize(size);
            } else {
                try {
                    ClassCache cachedInfo = classCacheMap.get(obClazz);
                    if (cachedInfo == null) {
                        classCacheMap.put(obClazz, cachedInfo = createCacheEntry(obClazz));
                    }
                    for (Field f : cachedInfo.referenceFields) {
                        final Object o = f.get(ob);
                        if (o != null && !seen.contains(o)) {
                            stack.add(o);
                        }
                    }
                    totalSize += cachedInfo.alignedShallowInstanceSize;
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Reflective field access failed?", e);
                }
            }
        }
        seen.clear();
        stack.clear();
        return totalSize;
    }

    private static ClassCache createCacheEntry(final Class<?> clazz) {
        ClassCache cachedInfo;
        long shallowInstanceSize = NUM_BYTES_OBJECT_HEADER;
        final ArrayList<Field> referenceFields = new ArrayList<Field>(32);
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            final Field[] fields = c.getDeclaredFields();
            for (final Field f : fields) {
                if (!Modifier.isStatic(f.getModifiers())) {
                    shallowInstanceSize = adjustForField(shallowInstanceSize, f);
                    if (!f.getType().isPrimitive()) {
                        f.setAccessible(true);
                        referenceFields.add(f);
                    }
                }
            }
        }
        cachedInfo = new ClassCache(alignObjectSize(shallowInstanceSize), referenceFields.toArray(new Field[referenceFields.size()]));
        referenceFields.clear();
        return cachedInfo;
    }

    public static long sizeOfObject(Object o, long defSize) {
        return sizeOfObject(o, 0, defSize);
    }

    private static long sizeOfObject(Object o, int depth, long defSize) {
        if (o == null) {
            return 0;
        }
        long size;
        if (o instanceof Accountable) {
            size = ((Accountable) o).ramBytesUsed();
        } else if (o instanceof String) {
            size = sizeOf((String) o);
        } else if (o instanceof boolean[]) {
            size = sizeOf((boolean[]) o);
        } else if (o instanceof byte[]) {
            size = sizeOf((byte[]) o);
        } else if (o instanceof char[]) {
            size = sizeOf((char[]) o);
        } else if (o instanceof double[]) {
            size = sizeOf((double[]) o);
        } else if (o instanceof float[]) {
            size = sizeOf((float[]) o);
        } else if (o instanceof int[]) {
            size = sizeOf((int[]) o);
        } else if (o instanceof Long) {
            size = sizeOf((Long) o);
        } else if (o instanceof long[]) {
            size = sizeOf((long[]) o);
        } else if (o instanceof short[]) {
            size = sizeOf((short[]) o);
        } else if (o instanceof String[]) {
            size = sizeOf((String[]) o);
        } else if (o instanceof Query) {
            size = sizeOf((Query) o, defSize);
        } else if (o instanceof Map) {
            size = sizeOfMap((Map) o, ++depth, defSize);
        } else if (o instanceof Collection) {
            size = sizeOfCollection((Collection) o, ++depth, defSize);
        } else {
            if (defSize > 0) {
                size = defSize;
            } else {
                size = shallowSizeOf(o);
            }
        }
        return size;
    }

    public static long sizeOf(String s) {
        if (s == null) {
            return 0;
        }
        long size = STRING_SIZE + (long) NUM_BYTES_ARRAY_HEADER + (long) Character.BYTES * s.length();
        return alignObjectSize(size);
    }

    public static long shallowSizeOf(Object[] arr) {
        return alignObjectSize((long) NUM_BYTES_ARRAY_HEADER + (long) NUM_BYTES_OBJECT_REF * arr.length);
    }

    public static long shallowSizeOf(Object obj) {
        if (obj == null) return 0;
        final Class<?> clz = obj.getClass();
        if (clz.isArray()) {
            return shallowSizeOfArray(obj);
        } else {
            return shallowSizeOfInstance(clz);
        }
    }

    public static long shallowSizeOfInstance(Class<?> clazz) {
        if (clazz.isArray())
            throw new IllegalArgumentException("This method does not work with array classes.");
        if (clazz.isPrimitive())
            return primitiveSizes.get(clazz);
        long size = NUM_BYTES_OBJECT_HEADER;
        for (; clazz != null; clazz = clazz.getSuperclass()) {
            final Class<?> target = clazz;
            final Field[] fields = AccessController.doPrivileged(new PrivilegedAction<Field[]>() {
                @Override
                public Field[] run() {
                    return target.getDeclaredFields();
                }
            });
            for (Field f : fields) {
                if (!Modifier.isStatic(f.getModifiers())) {
                    size = adjustForField(size, f);
                }
            }
        }
        return alignObjectSize(size);
    }

    private static long shallowSizeOfArray(Object array) {
        long size = NUM_BYTES_ARRAY_HEADER;
        final int len = Array.getLength(array);
        if (len > 0) {
            Class<?> arrayElementClazz = array.getClass().getComponentType();
            if (arrayElementClazz.isPrimitive()) {
                size += (long) len * primitiveSizes.get(arrayElementClazz);
            } else {
                size += (long) NUM_BYTES_OBJECT_REF * len;
            }
        }
        return alignObjectSize(size);
    }

    static long adjustForField(long sizeSoFar, final Field f) {
        final Class<?> type = f.getType();
        final int fsize = type.isPrimitive() ? primitiveSizes.get(type) : NUM_BYTES_OBJECT_REF;
        return sizeSoFar + fsize;
    }

    public static String unitsMB(long bytes) {
        return ((float) bytes / ONE_MB) + "";
    }

    public static String humanReadableUnits(long bytes) {
        return humanReadableUnits(bytes,
                new DecimalFormat("0.#", DecimalFormatSymbols.getInstance(Locale.ROOT)));
    }

    public static String humanReadableUnits(long bytes, DecimalFormat df) {
        if (bytes / ONE_GB > 0) {
            return df.format((float) bytes / ONE_GB) + " GB";
        } else if (bytes / ONE_MB > 0) {
            return df.format((float) bytes / ONE_MB) + " MB";
        } else if (bytes / ONE_KB > 0) {
            return df.format((float) bytes / ONE_KB) + " KB";
        } else {
            return bytes + " bytes";
        }
    }

    /**
     * Return the size of the provided array of {@link Accountable}s by summing
     * up the shallow size of the array and the
     * {@link Accountable#ramBytesUsed() memory usage} reported by each
     * {@link Accountable}.
     */
    public static long sizeOf(Accountable[] accountables) {
        long size = shallowSizeOf(accountables);
        for (Accountable accountable : accountables) {
            if (accountable != null) {
                size += accountable.ramBytesUsed();
            }
        }
        return size;
    }

    /**
     * An identity hash set implemented using open addressing. No null keys are allowed.
     * <p>
     * TODO: If this is useful outside this class, make it public - needs some work
     */
    static final class IdentityHashSet<KType> implements Iterable<KType> {
        /**
         * Default load factor.
         */
        public final static float DEFAULT_LOAD_FACTOR = 0.75f;

        /**
         * Minimum capacity for the set.
         */
        public final static int MIN_CAPACITY = 4;

        /**
         * All of set entries. Always of power of two length.
         */
        public Object[] keys;

        /**
         * Cached number of assigned slots.
         */
        public int assigned;

        /**
         * The load factor for this set (fraction of allocated or deleted slots before
         * the buffers must be rehashed or reallocated).
         */
        public final float loadFactor;

        /**
         * Cached capacity threshold at which we must resize the buffers.
         */
        private int resizeThreshold;

        /**
         * Creates a hash set with the default capacity of 16.
         * load factor of {@value #DEFAULT_LOAD_FACTOR}. `
         */
        public IdentityHashSet() {
            this(16, DEFAULT_LOAD_FACTOR);
        }

        /**
         * Creates a hash set with the given capacity, load factor of
         * {@value #DEFAULT_LOAD_FACTOR}.
         */
        public IdentityHashSet(int initialCapacity) {
            this(initialCapacity, DEFAULT_LOAD_FACTOR);
        }

        /**
         * Creates a hash set with the given capacity and load factor.
         */
        public IdentityHashSet(int initialCapacity, float loadFactor) {
            initialCapacity = Math.max(MIN_CAPACITY, initialCapacity);

            assert initialCapacity > 0 : "Initial capacity must be between (0, "
                    + Integer.MAX_VALUE + "].";
            assert loadFactor > 0 && loadFactor < 1 : "Load factor must be between (0, 1).";
            this.loadFactor = loadFactor;
            allocateBuffers(roundCapacity(initialCapacity));
        }

        /**
         * Adds a reference to the set. Null keys are not allowed.
         */
        public boolean add(KType e) {
            assert e != null : "Null keys not allowed.";

            if (assigned >= resizeThreshold) {
                expandAndRehash();
            }

            final int mask = keys.length - 1;
            int slot = rehash(e) & mask;
            Object existing;
            while ((existing = keys[slot]) != null) {
                if (e == existing) {
                    return false; // already found.
                }
                slot = (slot + 1) & mask;
            }
            assigned++;
            keys[slot] = e;
            return true;
        }

        /**
         * Checks if the set contains a given ref.
         */
        public boolean contains(KType e) {
            final int mask = keys.length - 1;
            int slot = rehash(e) & mask;
            Object existing;
            while ((existing = keys[slot]) != null) {
                if (e == existing) {
                    return true;
                }
                slot = (slot + 1) & mask;
            }
            return false;
        }

        /**
         * Rehash via MurmurHash.
         *
         * <p>The implementation is based on the
         * finalization step from Austin Appleby's
         * <code>MurmurHash3</code>.
         *
         * @see "http://sites.google.com/site/murmurhash/"
         */
        private static int rehash(Object o) {
            int k = System.identityHashCode(o);
            k ^= k >>> 16;
            k *= 0x85ebca6b;
            k ^= k >>> 13;
            k *= 0xc2b2ae35;
            k ^= k >>> 16;
            return k;
        }

        /**
         * Expand the internal storage buffers (capacity) or rehash current keys and
         * values if there are a lot of deleted slots.
         */
        private void expandAndRehash() {
            final Object[] oldKeys = this.keys;

            assert assigned >= resizeThreshold;
            allocateBuffers(nextCapacity(keys.length));

            /*
             * Rehash all assigned slots from the old hash table.
             */
            final int mask = keys.length - 1;
            for (int i = 0; i < oldKeys.length; i++) {
                final Object key = oldKeys[i];
                if (key != null) {
                    int slot = rehash(key) & mask;
                    while (keys[slot] != null) {
                        slot = (slot + 1) & mask;
                    }
                    keys[slot] = key;
                }
            }
            Arrays.fill(oldKeys, null);
        }

        /**
         * Allocate internal buffers for a given capacity.
         *
         * @param capacity New capacity (must be a power of two).
         */
        private void allocateBuffers(int capacity) {
            this.keys = new Object[capacity];
            this.resizeThreshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        }

        /**
         * Return the next possible capacity, counting from the current buffers' size.
         */
        protected int nextCapacity(int current) {
            assert current > 0 && Long.bitCount(current) == 1 : "Capacity must be a power of two.";
            assert ((current << 1) > 0) : "Maximum capacity exceeded ("
                    + (0x80000000 >>> 1) + ").";

            if (current < MIN_CAPACITY / 2) {
                current = MIN_CAPACITY / 2;
            }
            return current << 1;
        }

        /**
         * Round the capacity to the next allowed value.
         */
        protected int roundCapacity(int requestedCapacity) {
            // Maximum positive integer that is a power of two.
            if (requestedCapacity > (0x80000000 >>> 1)) {
                return (0x80000000 >>> 1);
            }

            int capacity = MIN_CAPACITY;
            while (capacity < requestedCapacity) {
                capacity <<= 1;
            }

            return capacity;
        }

        public void clear() {
            assigned = 0;
            Arrays.fill(keys, null);
        }

        public int size() {
            return assigned;
        }

        public boolean isEmpty() {
            return size() == 0;
        }

        @Override
        public Iterator<KType> iterator() {
            return new Iterator<KType>() {
                int pos = -1;
                Object nextElement = fetchNext();

                @Override
                public boolean hasNext() {
                    return nextElement != null;
                }

                @SuppressWarnings("unchecked")
                @Override
                public KType next() {
                    Object r = this.nextElement;
                    if (r == null) {
                        throw new NoSuchElementException();
                    }
                    this.nextElement = fetchNext();
                    return (KType) r;
                }

                private Object fetchNext() {
                    pos++;
                    while (pos < keys.length && keys[pos] == null) {
                        pos++;
                    }

                    return (pos >= keys.length ? null : keys[pos]);
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }

    private static final class ClassCache {
        public final long alignedShallowInstanceSize;
        public final Field[] referenceFields;

        public ClassCache(long alignedShallowInstanceSize, Field[] referenceFields) {
            this.alignedShallowInstanceSize = alignedShallowInstanceSize;
            this.referenceFields = referenceFields;
        }
    }
}
