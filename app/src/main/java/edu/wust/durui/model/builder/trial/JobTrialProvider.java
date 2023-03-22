package edu.wust.durui.model.builder.trial;

import java.util.ArrayList;
import java.util.List;

import edu.wust.durui.model.cornerstone.Task;

public class JobTrialProvider implements TrialProvider {
    @Override
    public List<Task> provide() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(0, 7));
        tasks.add(new Task(5, 4));
        tasks.add(new Task(7, 13));
        tasks.add(new Task(12, 9));
        return tasks;
    }
}
