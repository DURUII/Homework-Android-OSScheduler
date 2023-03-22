package edu.wust.durui.model.layers.t2;

import java.util.PriorityQueue;

import edu.wust.durui.model.cornerstone.Clock;
import edu.wust.durui.model.cornerstone.Scheduler;
import edu.wust.durui.model.cornerstone.Task;

public class PPRR extends Scheduler {
    private boolean leisure = true;
    private int quantum = 2;
    private int dynamicTimeSlice = quantum;
    private final PriorityQueue<Task> queue = new PriorityQueue<>((o1, o2) ->
            (o1.getPriority() - o2.getPriority() != 0) ? (o1.getPriority() - o2.getPriority()) : (o1.getJobSubmitTime() - o2.getJobSubmitTime()));

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }

    public void update(int currentTime, int elapseUnit) {
        shuffle();

        // existing a running process within the last minute
        if (!leisure) {
            queue.peek().decrementLeftTime(currentTime, elapseUnit);
            dynamicTimeSlice -= elapseUnit;
            // terminated
            if (queue.peek().isTerminated()) {
                queue.peek().incrementPriority(2);
                reportToSuperior();
                // reset time slice
                leisure = true;
            } else if (queue.peek().isRunning()) {
                // brute-force offline
                if (dynamicTimeSlice == 0) {
                    queue.peek().incrementPriority(2);
                    queue.peek().toggleReReady();
                    shuffle();
                    leisure = true;
                }
                // slice is still adequate for continuous running
                else {
                    leisure = false;
                }
            }
        }

        // needy for a ready process
        if (leisure) {
            dynamicTimeSlice = quantum;
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
