package edu.wust.durui.model.layers.t2;

import java.util.Comparator;
import java.util.PriorityQueue;

import edu.wust.durui.model.cornerstone.Clock;
import edu.wust.durui.model.cornerstone.Scheduler;
import edu.wust.durui.model.cornerstone.Task;

public class PFCFS extends Scheduler {
    private boolean leisure = true;
    private final PriorityQueue<Task> queue = new PriorityQueue<>(
            Comparator.comparingInt(Task::getProcessArriveTime));

    @Override
    public void update(int currentTime, int elapseUnit) {
        shuffle();

        // existing a running process
        if (!leisure) {
            queue.peek().decrementLeftTime(currentTime, elapseUnit);
            if (queue.peek().isTerminated()) {
                reportToSuperior();
                leisure = true;
            } else {
                leisure = false;
            }
        }

        // needy for a ready process
        if (leisure) {
            notifyToInferior();
            if (queue.peek() != null) {
                queue.peek().toggleRunning(currentTime);
                leisure = false;
            }
        }
    }

    @Override
    public boolean add(Task t) {
        if (queue.size() >= LIMIT)
            return false;

        t.toggleReady(Clock.minutes);
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

        return inferior.reportToSuperior();
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
        return "就绪队列：" + queue;
    }
}
