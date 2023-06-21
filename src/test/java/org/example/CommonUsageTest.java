package org.example;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Unit test for simple App.
 */
public class CommonUsageTest
{
    /**
     * Rigorous Test :-)
     */

    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    // 普通并发
    @Test
    public void ThreadExample() throws InterruptedException{
        int x = 10;
        final Result result = new Result();

        Thread t1 = new Thread(() -> {
            result.left = f(x);
        });

        Thread t2 = new Thread(() -> {
            result.right = g(x);
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();
        System.out.println(result.left + result.right);

    }

    // 线程池并发
    @Test
    public void ExecutorServiceExample() throws ExecutionException, InterruptedException {
        int x = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Integer> y = executorService.submit(() -> f(x));
        Future<Integer> z = executorService.submit(() -> g(x));
        System.out.println(y.get() + z.get());
        executorService.shutdown();
    }

    // 阻塞式睡眠
    @Test
    public void harmfulSleep() throws InterruptedException {
        work1();
        Thread.sleep(1000);
        work2();
    }

    // 非阻塞式睡眠
    @Test
    public void ScheduleExecutorServiceExample() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        work1();
        scheduledExecutorService.schedule(() -> work2(), 10, TimeUnit.SECONDS); // work1()完成后10秒，启动新任务执行work2()，期间会调度等待任务先执行
        scheduledExecutorService.shutdown();
    }

    //  组合器1 因调用get()而阻塞
    @Test
    public void CFComplete1() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        int x = 10;

        CompletableFuture<Integer> b = new CompletableFuture<>();
        executorService.submit(() -> b.complete(g(x)));
        int a = f(x);
        System.out.println(a + b.get());
    }

    // 组合器2 c使用thenCombine，它会在f(x)和g(x)都完成后产生线程，主线程不会阻塞
    @Test
    public void CFComplete2() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        int x = 10;
        CompletableFuture<Integer> a = new CompletableFuture<>();
        CompletableFuture<Integer> b = new CompletableFuture<>();
        CompletableFuture<Integer> c = a.thenCombine(b, (y, z) -> y + z);
        executorService.submit(() -> a.complete(f(x)));
        executorService.submit(() -> b.complete(g(x)));
        System.out.println(c.get());
        executorService.shutdown();
    }

    @Test
    public void callAsync() throws ExecutionException, InterruptedException {
        Future<Double> priceAsync = getPriceAsync();
        long start = System.nanoTime();
        Double aDouble = priceAsync.get();
        long dur = System.nanoTime() - start;
        System.out.println(aDouble + " " + dur);

        // 精简的异步方法
        Future<Integer> asyncf = asyncf(1);
        start = System.nanoTime();
        Integer integer = asyncf.get();
        dur = System.nanoTime() - start;
        System.out.println(integer + " " + dur);

        //  批量发起异步请求
        List<Integer> integers = asyncListF(10);
        System.out.println(integers);

        // 合并两个CompletableFuture
        CompletableFuture<Integer> c = CompletableFuture.supplyAsync(() -> f(1))
                .thenCombine(CompletableFuture.supplyAsync(() -> g(2)), (f, g) -> f * g);
        Integer integer1 = c.get();
        System.out.println(integer1);
    }

    // Thread 异步方法
    public Future<Double> getPriceAsync(){
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        new Thread( () -> {
            Double price = 1.0;
            futurePrice.complete(price);
        }).start();
        return futurePrice;
    }

    // CompletableFuture
    public Future<Integer> asyncf(int x){
        return CompletableFuture.supplyAsync(() -> f(x));
    }

    // CompletableFutureList
    public List<Integer> asyncListF(int x){
        List<CompletableFuture<Integer>> collect = IntStream.rangeClosed(1, x).boxed().map(a -> CompletableFuture.supplyAsync(() -> f(a))).collect(Collectors.toList());
        return collect.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    public static void work1(){
        System.out.println("work1...");
    }

    public static void work2() {
        System.out.println("work2...");
    }

    private static class Result {
        private int left;
        private int right;
    }

    public int f(int x){
        return x + 1;
    }

    public int g(int y){
        return y + 10;
    }


}
