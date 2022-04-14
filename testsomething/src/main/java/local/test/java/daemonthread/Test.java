package local.test.java.daemonthread;

public class Test {
    public static void main(String[] args) {
        Thread thread1  = new Thread("aa") {
            @Override
            public void run() {
                while(true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("running...");
                }
            }
        };
        thread1.setDaemon(true);
        // 守护进程和主线程一起，如果主线程停止，守护线程直接丢弃，比如上面sout没有执行到，如果非守护线程，还是会继续运行
        // 比如是一个长循环执行，守护线程会随着主进程的结束而结束，非守护线程会一直运行，直到JVM停止，守护进程不要做重要的操作
        // 守护和非守护并不控制执行频率，区别只在于停止是否依赖其他线程，守护线程是如果没有其他线程了，就停止，非守护线程没有这个依赖关系
        thread1.start();
    }
}
