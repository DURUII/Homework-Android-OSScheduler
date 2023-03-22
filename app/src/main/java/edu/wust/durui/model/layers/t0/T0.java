package edu.wust.durui.model.layers.t0;

import java.util.Comparator;
import java.util.PriorityQueue;

import edu.wust.durui.model.cornerstone.Scheduler;
import edu.wust.durui.model.cornerstone.Task;

public class T0 extends Scheduler {
    private final PriorityQueue<Task> queue = new PriorityQueue<>(new Comparator<Task>() {
        @Override
        public int compare(Task o1, Task o2) {
            return o1.getJobSubmitTime() - o2.getJobSubmitTime();
        }
    });


    @Override
    public void update(int currentTime, int elapseUnit) {
        while (queue.peek() != null && queue.peek().getJobSubmitTime() <= currentTime) {
            reportToSuperior();
        }
    }

    @Override
    public boolean add(Task t) {
        if (queue.size() >= LIMIT)
            return false;

        queue.offer(t);
        return true;
    }

    @Override
    public boolean reportToSuperior() {
        if (superior == null || queue.isEmpty())
            return false;

        boolean flag = superior.add(queue.peek());
        if (flag)
            return rebase();
        else
            return false;
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
        return "提交队列：" + queue;
    }
}
