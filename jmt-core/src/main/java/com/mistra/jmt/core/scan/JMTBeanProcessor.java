package com.mistra.jmt.core.scan;

import com.mistra.jmt.core.JMTWarden;
import com.mistra.jmt.core.anotation.*;
import com.mistra.jmt.model.ThreadPoolMemoryDump;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/21 21:53
 * @ Description:
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
@Component
public class JMTBeanProcessor implements BeanPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(JMTBeanProcessor.class);

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetCls = bean.getClass();
        JMTBean annotation = targetCls.getAnnotation(JMTBean.class);
        if (annotation != null) {
            try {
                Field[] declaredFields = targetCls.getDeclaredFields();
                for (Field field : declaredFields) {
                    JMTThreadPool threadPool = field.getAnnotation(JMTThreadPool.class);
                    if (threadPool != null) {
                        if (JMTWarden.threadPoolMemorySizeMap.containsKey(threadPool.threadPoolName())) {
                            throw new Exception("Scanner JMT bean Failure! JMTThreadPool name duplicated!");
                        }
                        JMTWarden.threadPoolMemorySizeMap.put(threadPool.threadPoolName(), new ThreadPoolMemoryDump());
                        JMTWarden.addExecutorService(threadPool.threadPoolName(), (ThreadPoolExecutor) field.get(targetCls));
                        continue;
                    }

                    JMTCollection collection = field.getAnnotation(JMTCollection.class);
                    if (collection != null) {
                        if (JMTWarden.objectMemorySizeMap.containsKey(collection.collectionName())) {
                            throw new IllegalArgumentException("Scanner JMT bean Failure! collectionName duplicated!");
                        }
                        JMTWarden.objectMemorySizeMap.put(collection.collectionName(), 0L);
                        JMTWarden.getCollectionKeeper().put(collection.collectionName(), (Collection<Object>) field.get(targetCls));
                        continue;
                    }

                    JMTMap map = field.getAnnotation(JMTMap.class);
                    if (map != null) {
                        if (JMTWarden.objectMemorySizeMap.containsKey(map.mapName())) {
                            throw new IllegalArgumentException("Scanner JMT bean Failure! mapName duplicated!");
                        }
                        JMTWarden.objectMemorySizeMap.put(map.mapName(), 0L);
                        JMTWarden.getMapKeeper().put(map.mapName(), (Map<Object, Object>) field.get(targetCls));
                        continue;
                    }

                    JMTObject object = field.getAnnotation(JMTObject.class);
                    if (object != null) {
                        if (JMTWarden.objectMemorySizeMap.containsKey(object.objectName())) {
                            throw new IllegalArgumentException("Scanner JMT bean Failure! objectName duplicated!");
                        }
                        JMTWarden.objectMemorySizeMap.put(object.objectName(), 0L);
                        JMTWarden.getObjectKeeper().put(object.objectName(), field.get(targetCls));
                    }
                }
            } catch (IllegalAccessException illegalAccessException) {
                log.error("Scanner JMT bean Failure!");
                illegalAccessException.printStackTrace();
            } catch (ClassCastException classCastException) {
                classCastException.printStackTrace();
                log.error("Annotation class cannot be cast to object class!");
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Scanner JMT bean Failure!");
            }
        }
        return bean;
    }

}
