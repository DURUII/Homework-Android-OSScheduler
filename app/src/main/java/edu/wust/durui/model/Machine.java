package edu.wust.durui.model;

import java.util.List;

import edu.wust.durui.model.cornerstone.Clock;
import edu.wust.durui.model.cornerstone.Scheduler;
import edu.wust.durui.model.cornerstone.Task;
import edu.wust.durui.model.layers.t2.PPRR;
import edu.wust.durui.model.layers.t3.T3;

public class Machine {
    private String TAG;
    private Clock clock;
    private Scheduler submitImitator;
    private Scheduler jobScheduler;
    private Scheduler processScheduler;
    private Scheduler landfilImitator;

    public void run() {
        build();
        while (!(submitImitator.isEmpty() && jobScheduler.isEmpty() && processScheduler.isEmpty())) {
            clock.incrementByUnit();
        }

        ((T3) landfilImitator).getFinalSnapShot();
    }

    // getters
    public float getAverageRoundTime() {
        return ((T3) landfilImitator).getAverageRoundTime();
    }

    public String getTAG() {
        return TAG;
    }

    public String getFinalSnapShot() {
        return ((T3) landfilImitator).getFinalSnapShot();
    }

    // setters
    public Machine setQuantum(int quantum) {
        if (processScheduler.getClass().equals(PPRR.class)) {
            ((PPRR) processScheduler).setQuantum(quantum);
        }
        return this;
    }

    public Machine setTrial(List<Task> tasks) {
        int starter = Integer.MAX_VALUE;
        for (Task task : tasks) {
            // FIXME prototype
            submitImitator.add((Task) task.clone());
            starter = Math.min(starter, task.getJobSubmitTime());
        }
        this.clock = new Clock(starter);
        return this;
    }

    public Machine setTAG(String TAG) {
        this.TAG = TAG;
        return this;
    }

    public Machine setClock(Clock clock) {
        this.clock = clock;
        return this;
    }

    public Machine setSubmitImitator(Scheduler submitImitator) {
        this.submitImitator = submitImitator;
        return this;
    }

    public Machine setJobScheduler(Scheduler jobScheduler) {
        this.jobScheduler = jobScheduler;
        return this;
    }

    public Machine setProcessScheduler(Scheduler processScheduler) {
        this.processScheduler = processScheduler;
        return this;
    }

    public Machine setLandfilImitator(Scheduler landfilImitator) {
        this.landfilImitator = landfilImitator;
        return this;
    }

    // Observer & Chain of Responsibility
    private void build() {
        submitImitator.observe(clock);
        jobScheduler.observe(clock);
        processScheduler.observe(clock);
        landfilImitator.observe(clock);
        submitImitator.setSuperior(jobScheduler);
        jobScheduler.setSuperior(processScheduler);
        processScheduler.setSuperior(landfilImitator);
        landfilImitator.setInferior(processScheduler);
        processScheduler.setInferior(jobScheduler);
        jobScheduler.setInferior(submitImitator);
    }

}
