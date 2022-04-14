package local.test.java.sdf;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

public class Test2 {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 使用Guava的ThreadFactoryBuilder创建一个线程池
    private static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build();
    private static ExecutorService pool = new ThreadPoolExecutor(5, 200,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

    // 定义一个CountDownLatch, 保证所有子线程执行完之后主线程再执行
    private static CountDownLatch countDownLatch = new CountDownLatch(100);

    public static void main(String[] args) throws InterruptedException {
        Set<String> dates = Collections.synchronizedSet(new HashSet<String>());
        for (int i = 0; i < 100; i++) {
            Calendar calendar = Calendar.getInstance();
            int finalI = i;
            pool.execute(() -> {
                System.out.println("Num:" + finalI);
                calendar.add(Calendar.DATE, finalI);
                String dateString = sdf.format(calendar.getTime());
                dates.add(dateString);
                countDownLatch.countDown();// latch -1， 如果减到了0， 会唤醒所有等待在这个latch上的线程
                    }

            );
        }
        System.out.println(countDownLatch.getCount());
        countDownLatch.await();// 当前线程进入同步等待，直到latch的值为0，当前线程才会被唤醒...
        System.out.println(countDownLatch.getCount());
        System.out.println(dates.size());
        pool.shutdown();

        // 程序使用了countdownLatch - 当主线程遇到await方法时，需要等countdownlatch中的latch值为0才能继续
        // countdownlatch 的latch值是100， 100个子线程，提交给线程池运行
        // 按基本流程是执行了100次线程，线程中是插入100个不同的日期，- add i day, 期望是100个日期
        // 但是结果是size<100

        /*
        原因是simpleDateFormat 是一个多线程共享对象，
        format执行的时候，把date给到局部变量，并format出结果，但是这个方法不是线程安全的，
        如果一个线程在format结果，另外的线程在设置值，当前线程的值就被修改成其他值了
         */
        /*
        fix: simpledateformat: 多例  -> 局部变量
        或者对共享变量加锁，当前线程在同步代码块中就独享这个变量，其他线程执行的时候只能等待，synchronized(sdf){sdf.format...}
        或者ThreadLocal<SimpleDateFormat> ... 每个线程都有自己的simpleDateFormat对象，
        或者Java8中得DateTimeFormatter替代SimpleDateFormat 这个是线程安全的


https://wenku.baidu.com/view/3d500b08e1bd960590c69ec3d5bbfd0a7956d5b0.html
         */
    }
}
