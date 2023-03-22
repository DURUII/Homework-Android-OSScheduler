package edu.wust.durui.model.layers.t1;

import java.util.PriorityQueue;

import edu.wust.durui.model.cornerstone.Clock;
import edu.wust.durui.model.cornerstone.Scheduler;
import edu.wust.durui.model.cornerstone.Task;

public class JHRRN extends Scheduler {
    private final PriorityQueue<Task> queue = new PriorityQueue<>(
            (o1, o2) -> (o2.getResponseRate(Clock.minutes) - o1.getResponseRate(Clock.minutes) > 0) ? (1) : (-1));

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

        shuffle();
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
        return "收容队列："+queue;
    }
}
