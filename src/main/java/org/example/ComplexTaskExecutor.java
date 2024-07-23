package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ComplexTaskExecutor {
    private final CyclicBarrier barrier;
    private final List<ComplexTask> tasks;

    public ComplexTaskExecutor(int numberOfTasks) {
        this.tasks = new ArrayList<>();
        this.barrier = new CyclicBarrier(numberOfTasks, () -> {
            System.out.println("All tasks completed. Merging results...");
            mergeResults();
        });
    }

    public void executeTasks(int numberOfTasks) {
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfTasks);
        for (int i = 0; i < numberOfTasks; i++) {
            tasks.add(new ComplexTask(i, barrier));
            executorService.submit(tasks.get(i));
        }
    }

    private void mergeResults() {
        int totalSum = tasks.stream().mapToInt(ComplexTask::getResult).sum();
        System.out.println("Total sum of results: " + totalSum);
    }
}
