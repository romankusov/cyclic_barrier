package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class ComplexTaskExecutor {
    private final CyclicBarrier barrier;
    private final List<ComplexTask> tasks;
    private final ReentrantLock lock = new ReentrantLock();

    public ComplexTaskExecutor(int numberOfTasks) {
        this.tasks = new ArrayList<>();
        this.barrier = new CyclicBarrier(numberOfTasks, () -> {
            System.out.println("All tasks completed. Merging results...");
            mergeResults();
        });
    }

    public void executeTasks(int numberOfTasks) {
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfTasks);
        try {
            for (int i = 0; i < numberOfTasks; i++) {
                lock.lock();
                try {
                    tasks.add(new ComplexTask(i, barrier));
                    executorService.submit(tasks.get(i));
                } finally {
                    lock.unlock();
                }
            }
            System.out.println("List size: " + tasks.size());
        } finally {
            executorService.shutdown();
        }
    }

    private synchronized void mergeResults() {
        int totalSum = tasks.stream().mapToInt(ComplexTask::getResult).sum();
        System.out.println("Total sum of results: " + totalSum);
    }
}
