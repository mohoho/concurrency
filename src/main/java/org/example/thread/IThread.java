package org.example.thread;

/**
 * @author qmruan
 * @date 2023/6/21 16:26
 */
public class IThread implements Runnable{


    @Override
    public void run() {
        for (int i = 0; i < 199; i++) {
            System.out.println("子线程: " + i);
        }
    }
}
