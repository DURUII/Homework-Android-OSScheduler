package edu.wust.durui.model.builder.trial;

import java.util.ArrayList;
import java.util.List;

import edu.wust.durui.model.cornerstone.Task;

public class ProcessTrialProvider implements TrialProvider {
    @Override
    public List<Task> provide() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(0, 8));
        tasks.add(new Task(1, 4));
        tasks.add(new Task(5, 1));
        return tasks;
    }
}
