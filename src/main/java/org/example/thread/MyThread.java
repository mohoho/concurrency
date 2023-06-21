package org.example.thread;

/**
 * @author qmruan
 * @date 2023/6/21 16:14
 */
public class MyThread extends Thread{

    public void run(){
        for (int i = 0; i < 100; i++) {
            System.out.println("子线程:" + Thread.currentThread() + " " + i);
        }
    }
}
