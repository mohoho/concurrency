package org.example.thread;

import java.util.concurrent.locks.ReentrantLock;

public class Ticket implements Runnable {
    private int tick = 100;

    private final ReentrantLock lock = new ReentrantLock();

    public void run() {

        while (true) {
            lock.lock();
            if (tick > 0) {
                System.out.println(Thread.currentThread
                            ().getName() + "售出车票，tick号为：" +
                            tick--);
            } else {
                break;
            }
            lock.unlock();
        }

    }
}