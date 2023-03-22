package edu.wust.durui.model.layers.t1;

import java.util.Comparator;
import java.util.PriorityQueue;

import edu.wust.durui.model.cornerstone.Scheduler;
import edu.wust.durui.model.cornerstone.Task;

public class JFCFS extends Scheduler {
    private final PriorityQueue<Task> queue = new PriorityQueue<>(
            Comparator.comparingInt(Task::getJobSubmitTime));

    @Override
    public void update(int currentTime, int elapseUnit) {
        boolean flag = true;
        while (queue.peek() != null && flag) {
            flag = reportToSuperior();
        }
    }

    @Override
    public boolean add(Task t) {
        if (queue.size() >= LIMIT)
            return false;

        t.toggleBackup();
        queue.offer(t);
        return true;
    }

    @Override
    public boolean reportToSuperior() {
        if (superior == null || queue.isEmpty())
            return false;

        if (!superior.add(queue.peek()))
            return false;

        return rebase();
    }

    @Override
    public boolean notifyToInferior() {
        if (inferior == null)
            return false;

        return false;
    }

    @Override
    public boolean rebase() {
        if (queue.isEmpty())
            return false;

        queue.poll();
        return true;
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public void shuffle() {
        if (queue.size() >= 2) {
            queue.offer(queue.poll());
        }
    }

    @Override
    public String toString() {
        return "收容队列：" + queue;
    }
}
