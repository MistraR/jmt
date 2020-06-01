# jmt
### JVM monitoring and tuning
### JVM性能监控与调优

公司的Java程序中存在很多后台任务。线程池，队列，缓存也是遍布整个系统。平常测试的时候也因为一些OOM导致程序无法响应请求。还因为某些基础库的问题导致CPU错误，导致程序挂掉。
脑子里就冒出个想法，想设计一个基础库，能监控系统中的线程池，队列，缓存的内存使用情况，记录关键方法的执行时间，找出耗时的方法，才能精准的优化，提高系统整体性能。
所以我就想设计一个这样的性能监控系统。
要结合微服务的相关服务治理中间件。服务注册，统一配置中心，微服务管理中心等等。实现对系统服务的整体监控。
最近刚画好了设计图，慢慢完善。正在逐步码代码中。


![在这里插入图片描述](https://img-blog.csdnimg.cn/20200531212802813.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0F4ZWxhMzBX,size_16,color_FFFFFF,t_70#pic_center)