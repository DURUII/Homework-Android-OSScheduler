package edu.wust.durui.model.cornerstone;

public class Task implements Cloneable {
    // auto-increment
    private static int COUNTER = 0;

    // universal attribute
    private final int id;
    private int priority = 0;
    private State state = State.NULL;

    // exclusive for job scheduling
    private final int jobSubmitTime;
    private final int estimatedRequireTime;

    // exclusive for process scheduling
    private int processLeftTime;
    private int processArriveTime = -1;
    private int processStartTime = -1;
    private int processCompleteTime = -1;

    public Task(int id, int priority, int jobSubmitTime, int estimatedRequireTime) {
        this.id = id;
        this.priority = priority;
        this.jobSubmitTime = jobSubmitTime;
        this.estimatedRequireTime = estimatedRequireTime;
        this.processLeftTime = estimatedRequireTime;
    }

    public Task(int priority, int jobSubmitTime, int estimatedRequireTime) {
        this.id = COUNTER++;
        this.priority = priority;
        this.jobSubmitTime = jobSubmitTime;
        this.estimatedRequireTime = estimatedRequireTime;
        this.processLeftTime = estimatedRequireTime;
    }

    public Task(int jobSubmitTime, int estimatedRequireTime) {
        this.id = COUNTER++;
        this.jobSubmitTime = jobSubmitTime;
        this.estimatedRequireTime = estimatedRequireTime;
        this.processLeftTime = estimatedRequireTime;
    }

    public void decrementLeftTime(int currentTime, int elapseUnit) {
        if (isRunning()) {
            this.processLeftTime -= elapseUnit;
            if (processLeftTime <= 0) {
                toggleTerminated(currentTime);
            }
        }
    }

    public void incrementPriority(int diff) {
        this.priority += diff;
    }

    public void toggleBackup() {
        this.state = State.BACKUP;
    }

    public void toggleReady(int arriveTime) {
        this.state = State.READY;
        this.processArriveTime = arriveTime;
    }

    public void toggleReReady() {
        this.state = State.READY;
    }

    public void toggleTerminated(int completeTime) {
        this.state = State.TERMINATED;
        this.processCompleteTime = completeTime;
    }

    public void toggleRunning(int startTime) {
        this.state = State.RUNNING;
        if (processStartTime == -1) {
            this.processStartTime = startTime;
        }
    }

    public boolean isBackup() {
        return this.state == State.BACKUP;
    }

    public boolean isReady() {
        return this.state == State.READY;
    }

    public boolean isRunning() {
        return this.state == State.RUNNING;
    }

    public boolean isTerminated() {
        return this.state == State.TERMINATED;
    }

    public int getId() {
        int factor = (isRunning()) ? (-1) : (1);
        return id * factor;
    }

    public int getJobSubmitTime() {
        int factor = (isRunning()) ? (-1) : (1);
        return jobSubmitTime * factor;
    }

    public int getPriority() {
        int factor = (isRunning()) ? (-1) : (1);
        return priority * factor;
    }

    public int getProcessLeftTime() {
        int factor = (isRunning()) ? (-1) : (1);
        return processLeftTime * factor;
    }

    public int getProcessArriveTime() {
        int factor = (isRunning()) ? (-1) : (1);
        return processArriveTime * factor;
    }

    public int getProcessStartTime() {
        int factor = (isRunning()) ? (-1) : (1);
        return processStartTime * factor;
    }

    public int getProcessCompleteTime() {
        int factor = (isRunning()) ? (-1) : (1);
        return processCompleteTime * factor;
    }

    public int getEstimatedRequireTime() {
        return estimatedRequireTime;
    }

    public double getResponseRate(int currentTime) {
        int factor = (isRunning()) ? (-1) : (1);
        return (1 + ((1.0 * currentTime - jobSubmitTime) / estimatedRequireTime)) * factor;
    }

    public int getRoundTime() {
        int factor = (isRunning()) ? (-1) : (1);
        return (processCompleteTime - jobSubmitTime) * factor;
    }

    public String toString() {
        return String.format("{%d,%s,%s}", id, state, Clock.generatePatternFromMinutes(processLeftTime));
    }

    public Object clone() {
        return new Task(this.id, this.priority, this.jobSubmitTime, this.estimatedRequireTime);
    }
}
