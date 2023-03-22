package edu.wust.durui.model.layers.t3;

import java.util.Comparator;
import java.util.PriorityQueue;

import edu.wust.durui.model.cornerstone.Clock;
import edu.wust.durui.model.cornerstone.Scheduler;
import edu.wust.durui.model.cornerstone.Task;

public class T3 extends Scheduler {
    PriorityQueue<Task> queue = new PriorityQueue<>(new Comparator<Task>() {
        @Override
        public int compare(Task o1, Task o2) {
            // return o1.getProcessCompleteTime() - o2.getProcessCompleteTime();
            return o1.getId() - o2.getId();
        }
    });

    @Override
    public void update(int currentTime, int elapseUnit) {
    }

    @Override
    public boolean add(Task t) {
        queue.offer(t);
        return true;
    }

    @Override
    public boolean reportToSuperior() {
        return false;
    }

    @Override
    public boolean notifyToInferior() {
        return false;
    }

    @Override
    public boolean rebase() {
        return false;
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

    public float getAverageRoundTime() {
        float sum = 0.0f;
        int size = queue.size();

        PriorityQueue<Task> swap = new PriorityQueue<>(new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                // return o1.getProcessCompleteTime() - o2.getProcessCompleteTime();
                return o1.getId() - o2.getId();
            }
        });

        while (!queue.isEmpty()) {
            Task task = queue.poll();
            sum += task.getRoundTime();
            swap.offer(task);
        }

        this.queue = swap;
        return sum / size;
    }

    public String getFinalSnapShot() {
        PriorityQueue<Task> swap = new PriorityQueue<>(new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                // return o1.getProcessCompleteTime() - o2.getProcessCompleteTime();
                return o1.getId() - o2.getId();
            }
        });

        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            Task t = queue.poll();

            sb.append("ID:");
            sb.append(t.getId() + 1);
            sb.append("\n");
            sb.append("priority:");
            sb.append(t.getPriority());
            sb.append("\n");
            sb.append("submit:");
            sb.append(Clock.generatePatternFromMinutes(t.getJobSubmitTime()));
            sb.append("\n");
            sb.append("arrive:");
            sb.append(Clock.generatePatternFromMinutes(t.getProcessArriveTime()));
            sb.append("\n");
            sb.append("start:");
            sb.append(Clock.generatePatternFromMinutes(t.getProcessStartTime()));
            sb.append("\n");
            sb.append("complete:");
            sb.append(Clock.generatePatternFromMinutes(t.getProcessCompleteTime()));
            sb.append("\n");
            sb.append("round:");
            sb.append(Clock.generatePatternFromMinutes(t.getRoundTime()));
            sb.append("\n");
            sb.append("\n");
            swap.offer(t);
        }

        this.queue = swap;
        return sb.toString();
    }

    @Override
    public String toString() {
        return "销毁队列：" + queue;
    }
}
