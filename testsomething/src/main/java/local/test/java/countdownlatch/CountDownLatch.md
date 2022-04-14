# CountDownLatch

## 概念 

- CountDownLatch允许一个或者多个线程去等待其他线程完成操作
- 接收一个int参数，表示要等待的工作线程个数 初始的latch值

## 方法

- await() ： 当前线程进入同步队列进行等待，直到latch的值被减到0或者当前线程被中断，当前线程就会被唤醒。
- await(long timeout, TimeUnit unit); 带超时时间的等待
- countDown() : latch - 1; 如果减到了0，就唤醒等待latch的线程
- getCount(): latch的数值

