package org.example;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;

public class ComplexTask implements Runnable {
    private final int taskId;
    private final CyclicBarrier barrier;
    private final Random random = new Random();
    private int result;

    public ComplexTask(int taskId, CyclicBarrier barrier) {
        this.taskId = taskId;
        this.barrier = barrier;
    }

    public void execute() {
        result = random.nextInt() / 10;
        System.out.println(Thread.currentThread().getName() + " Task " + taskId +
                " computed value: " + result);
    }

    @Override
    public void run() {
        try {
            execute();
            barrier.await(); // Ожидание других задач
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getResult() {
        return result;
    }
}
