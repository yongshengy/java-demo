package local.test.java.countdownlatch;

import java.util.concurrent.CountDownLatch;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        /**
         * CountDownLatch允许一个或多个线程去等待其他线程完成操作
         *
         */

        CountDownLatch c = new CountDownLatch(3);

        WaitThread waitThread1 = new WaitThread("wait thread 1", c);
        WaitThread waitThread2 = new WaitThread("wait thread 2", c);

        Worker worker1 = new Worker("worker thread 1", c);
        Worker worker2 = new Worker("worker thread 2", c);
        Worker worker3 = new Worker("worker thread 3", c);

        // wait thread 中有CountDownLatch的await方法，去等待latch为0才会继续
        waitThread1.start();
        waitThread2.start();
        Thread.sleep(1000);
        // countdownlatch的latch值是3， 这里有3个线程去减去latch，这三个线程时阻塞着上面的线程
        worker1.start();
        worker2.start();
        worker3.start();
    }
}

class WaitThread extends Thread {
    private String name;
    private CountDownLatch c;

    public WaitThread(String name, CountDownLatch c) {
        this.name = name;
        this.c = c;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " wait...");
            c.await();
            System.out.println(this.name + " end...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Worker extends Thread {
    private String name;
    private CountDownLatch c;
    public Worker (String name, CountDownLatch c) {
        this.name = name;
        this.c = c;
    }

    @Override
    public void run() {
        try{
            System.out.println(this.name + " is running");
            Thread.sleep(2);
            System.out.println(this.name + " end");
            c.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
