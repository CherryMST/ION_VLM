package com.nokia.vlm.manager;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * 程序中用到的线程池管理工具
 */
public class TemporaryThreadManager {

    static final int MAX_RUNNING_THREAD = 10;

    private static TemporaryThreadManager _instance;

    private ExecutorService executor;

    private TemporaryThreadManager(){
        try {
            executor = Executors.newFixedThreadPool(MAX_RUNNING_THREAD, new LowThreadFactory());
        } catch (Throwable t) {
            executor = Executors.newCachedThreadPool();
        }
    }




    public synchronized static TemporaryThreadManager get(){
        if(_instance == null){
            _instance = new TemporaryThreadManager();
        }
        return _instance;
    }

    public void start(Runnable runnable){
        executor.submit(runnable);
    }

    public <T> Future<T> start(Callable<T> callable){
        return executor.submit(callable);
    }


    /**
     * 低优先级的线程工厂
     */
    private static class LowThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        LowThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" +
                    poolNumber.getAndIncrement() +
                    "-temporary-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.MIN_PRIORITY)
                t.setPriority(Thread.MIN_PRIORITY);
            return t;
        }
    };
}
