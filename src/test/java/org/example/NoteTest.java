package org.example;

import org.example.thread.IThread;
import org.example.thread.MyThread;
import org.junit.Test;
import org.example.thread.Ticket;

/**
 * @author qmruan
 * @date 2023/6/21 16:13
 */
public class NoteTest {

    // 继承方式实现线程
    @Test
    public void test1() {
        MyThread myThread = new MyThread();
        System.out.println(myThread.getName());
        myThread.start();
    }

    // 接口方式实现线程
    @Test
    public void test2() {
        Thread thread = new Thread(new IThread());
        thread.start();
    }

    // 同步
    @Test
    public void test3() {
        Ticket ticket = new Ticket();
        Thread thread1 = new Thread(ticket);
        Thread thread2 = new Thread(ticket);
        Thread thread3 = new Thread(ticket);
        thread1.start();
        thread2.start();
        thread3.start();
    }
}
