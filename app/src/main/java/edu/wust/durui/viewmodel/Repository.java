package edu.wust.durui.viewmodel;

import android.graphics.Color;
import android.text.Editable;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.eazegraph.lib.models.PieModel;
import org.eazegraph.lib.models.ValueLinePoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import edu.wust.durui.model.Machine;
import edu.wust.durui.model.builder.machine.FCFSBuilder;
import edu.wust.durui.model.builder.machine.HRRNBuilder;
import edu.wust.durui.model.builder.machine.MFQBBuilder;
import edu.wust.durui.model.builder.machine.MFQBuilder;
import edu.wust.durui.model.builder.machine.PRRBuilder;
import edu.wust.durui.model.builder.machine.SJFBuilder;
import edu.wust.durui.model.cornerstone.Clock;
import edu.wust.durui.model.cornerstone.Task;

public class Repository extends ViewModel {
    // TODO Observable
    public MutableLiveData<List<Task>> tasks = new MutableLiveData<>();
    public MutableLiveData<List<ValueLinePoint>> rounds = new MutableLiveData<>();
    public MutableLiveData<List<PieModel>> histories = new MutableLiveData<>();
    public MutableLiveData<String> snap = new MutableLiveData<>();
    public MutableLiveData<Object[]> snapTasks = new MutableLiveData<>();
    public MutableLiveData<Integer> counter = new MutableLiveData<>();

    // TODO init
    public Repository() {
        tasks.setValue(new LinkedList<>());
        rounds.setValue(new ArrayList<>());
        histories.setValue(new ArrayList<>());
        snap.setValue("");
        snapTasks.setValue(new Object[]{});
        counter.setValue(0);

        tasks.getValue().add(new Task(
                Clock.parsePatternIntoMinutes("8:00"),
                120));
        tasks.getValue().add(new Task(
                Clock.parsePatternIntoMinutes("8:30"),
                30));
        tasks.getValue().add(new Task(
                Clock.parsePatternIntoMinutes("9:00"),
                6));
        tasks.getValue().add(new Task(
                Clock.parsePatternIntoMinutes("9:30"),
                12));
    }

    // TODO Add a task & Configure Round-Ribbon
    private Integer estimatedRequireTime = null;
    private Integer jobSubmitTime = null;
    private Integer priority = null;
    Integer quantum = null;
    Integer factor = null;

    public void setEstimatedRequireTime(int hour, int minute) {
        this.estimatedRequireTime = hour * 60 + minute;
    }

    public void setJobSubmitTime(int hour, int minute) {
        this.jobSubmitTime = hour * 60 + minute;
    }

    public void setPriority(Editable priority) {
        if (priority != null && priority.length() != 0) {
            this.priority = Integer.parseInt(priority + "");
        }
    }

    public void setQuantum(Editable quantum) {
        if (quantum != null && quantum.length() != 0) {
            this.quantum = Integer.parseInt(quantum + "");
        }
    }

    public void setFactor(Editable factor) {
        if (factor != null && factor.length() != 0) {
            this.factor = Integer.parseInt(factor + "");
        }
    }

    public String getEstimatedRequireTimeText() {
        return String.format(Locale.CHINA, "%02d:%02d", this.estimatedRequireTime / 60, this.estimatedRequireTime % 60);
    }

    public String getJobSubmitTimeText() {
        return String.format(Locale.CHINA, "%02d:%02d", this.jobSubmitTime / 60, this.jobSubmitTime % 60);
    }

    public boolean resetTask() {
        estimatedRequireTime = null;
        jobSubmitTime = null;
        priority = null;
        return true;
    }

    public boolean resetQuantumAndFactor() {
        quantum = null;
        factor = null;
        return true;
    }

    public boolean addTask() {
        if (priority == null || jobSubmitTime == null || estimatedRequireTime == null) {
            return false;
        }
        tasks.getValue().add(new Task(priority, jobSubmitTime, estimatedRequireTime));
        tasks.setValue(tasks.getValue());
        return resetTask();
    }

    public Task getTask(int pos) {
        if (pos >= tasks.getValue().size()) {
            return null;
        }
        return tasks.getValue().get(pos);
    }

    public boolean clearTask() {
        tasks.getValue().clear();
        tasks.setValue(tasks.getValue());
        return true;
    }

    public boolean deleteTask(int pos) {
        if (pos < tasks.getValue().size()) {
            tasks.getValue().remove(pos);
            tasks.setValue(tasks.getValue());
            return true;
        }
        return false;
    }

    // TODO algorithm analysis & snap shot
    private final HashMap<String, Float> accumulatedRoundTime = new HashMap<>();
    private final HashMap<String, String> snapShots = new HashMap<>();
    private final HashMap<String, Object[]> snapShotsTasks = new HashMap<>();
    private String tag = "";

    public void startSchedule() {
        List<Machine> machines = new ArrayList<>();
        machines.add(new FCFSBuilder().build());
        machines.add(new SJFBuilder().build());
        machines.add(new HRRNBuilder().build());
        machines.add(new PRRBuilder().build().setQuantum(quantum));
        machines.add(new MFQBuilder().build().setFactor(factor));
        machines.add(new MFQBBuilder().build().setFactor(factor));
        // machines.add(new SJFMQFBBuilder().build());

        // algorithm analysis(single)
        List<ValueLinePoint> points = new ArrayList<>();
        points.add(new ValueLinePoint("null", 0f));
        for (Machine machine : machines) {
            Log.d("time-line", machine.getTAG() + " is RUNNING");
            machine.setTrial(tasks.getValue()).run();
            points.add(new ValueLinePoint(machine.getTAG(), machine.getAverageRoundTime()));

            snapShots.put(machine.getTAG(), machine.getFinalSnapShot());
            snapShotsTasks.put(machine.getTAG(), machine.getFinalSnapShotTasks());

            Float history = accumulatedRoundTime.get(machine.getTAG());
            if (history != null) {
                accumulatedRoundTime.put(machine.getTAG(), history + machine.getAverageRoundTime());
            } else {
                accumulatedRoundTime.put(machine.getTAG(), machine.getAverageRoundTime());
            }
        }
        points.add(new ValueLinePoint("null", 0f));
        rounds.setValue(points);

        // algorithm analysis(history)
        List<PieModel> pies = new ArrayList<>();
        for (String key : accumulatedRoundTime.keySet()) {
            StringBuilder color = new StringBuilder().append("#");
            for (int i = 0; i < 6; i++) {
                String[] hex = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
                color.append(hex[new Random().nextInt(16)]);
            }
            pies.add(new PieModel(key, accumulatedRoundTime.get(key), Color.parseColor(color.toString())));
        }
        histories.setValue(pies);

        counter.setValue(counter.getValue() + 1);
        resetQuantumAndFactor();
    }

    public void setTag(Editable tag) {
        if (tag != null && tag.length() != 0) {
            this.tag = tag + "";
            snap.setValue(snapShots.getOrDefault(this.tag, "https://github.com/DURUII"));
            snapTasks.setValue(snapShotsTasks.getOrDefault(this.tag, new Object[]{}));
        } else {
            snap.setValue("https://github.com/DURUII");
        }
    }

    // trivial functions
    public Integer getEstimatedRequireTime() {
        return estimatedRequireTime;
    }

    public Integer getJobSubmitTime() {
        return jobSubmitTime;
    }

    public Integer getPriority() {
        return priority;
    }

    public Integer getQuantum() {
        return quantum;
    }

    public String getTag() {
        return tag;
    }
}
