package edu.wust.durui.model.layers.t2;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import edu.wust.durui.model.cornerstone.Clock;
import edu.wust.durui.model.cornerstone.Scheduler;
import edu.wust.durui.model.cornerstone.Task;

/**
 * Created with IntelliJ IDEA.
 *
 * @author DURUII
 * @date 2023/03/19/20:20
 */
public class PMFQB extends Scheduler {
    private final List<Deque<Task>> queues = new ArrayList<>();
    private boolean leisure = true;
    private int busyIndex = 0;
    private int quantum = 1;
    private int dynamicTimeSlice = quantum;

    public PMFQB() {
        Deque<Task> top = new ArrayDeque<>();
        queues.add(top);
        Deque<Task> mid = new ArrayDeque<>();
        queues.add(mid);
        Deque<Task> btm = new ArrayDeque<>();
        queues.add(btm);
    }

    @Override
    public void update(int currentTime, int elapseUnit) {
        // existing a running process within the last minute
        if (!leisure) {
            Deque<Task> queue = queues.get(busyIndex);
            queue.peek().decrementLeftTime(currentTime, elapseUnit);
            dynamicTimeSlice -= elapseUnit;
            // terminated
            if (queue.peek().isTerminated()) {
                reportToSuperior();
                // reset time slice
                leisure = true;
            } else if (queue.peek().isRunning()) {
                // brute-force offline
                if (dynamicTimeSlice == 0) {
                    queue.peek().toggleReReady();
                    int nextIndex = Math.min(busyIndex + 1, 2);
                    // next level or bottom
                    queues.get(nextIndex).offer(queue.poll());
                    leisure = true;
                }
                // slice is still adequate for continuous running
                else {
                    // nobody can forcibly occupy
                    if (nextBusyIndex() == busyIndex) {
                        leisure = false;
                    } else {
                        // FIXME difference between preemptive and not
                        queue.peek().toggleReReady();
                        queue.offer(queue.poll());
                        leisure = true;
                    }
                }
            }
        }

        // needy for a ready process
        if (leisure) {
            notifyToInferior();
            // somebody ready
            if (nextBusyIndex() != -1) {
                busyIndex = nextBusyIndex();
                quantum = (int) Math.pow(2, busyIndex);
                dynamicTimeSlice = quantum;

                Deque<Task> queue = queues.get(busyIndex);
                queue.peek().toggleRunning(currentTime);
                leisure = false;
            }
        }
    }

    private int nextBusyIndex() {
        for (int i = 0; i <= 2; i++) {
            if (!queues.get(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean add(Task t) {
        if (this.size() >= LIMIT)
            return false;

        t.toggleReady(Clock.minutes);
        Deque<Task> queue = queues.get(0);
        queue.offer(t);
        return true;
    }

    @Override
    public boolean reportToSuperior() {
        Deque<Task> queue = queues.get(busyIndex);
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
        Deque<Task> queue = queues.get(busyIndex);
        if (queue.isEmpty())
            return false;

        queue.poll();
        return true;
    }

    @Override
    public int size() {
        int size = 0;
        for (Deque<Task> q : queues) {
            size += q.size();
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        for (Deque<Task> q : queues) {
            if (!q.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void shuffle() {

    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < queues.size(); i++) {
            str.append(i);
            str.append("：");
            str.append(queues.get(i));
            str.append("\n");
        }
        return str.toString();
    }
}
