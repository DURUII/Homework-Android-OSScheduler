package edu.wust.durui.model.builder.trial;

import java.util.ArrayList;
import java.util.List;

import edu.wust.durui.model.cornerstone.Clock;
import edu.wust.durui.model.cornerstone.Task;

public class JobTrialProvider implements TrialProvider {
    @Override
    public List<Task> provide() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(
                Clock.parsePatternIntoMinutes("8:00"),
                120));
        tasks.add(new Task(
                Clock.parsePatternIntoMinutes("8:30"),
                30));
        tasks.add(new Task(
                Clock.parsePatternIntoMinutes("9:00"),
                6));
        tasks.add(new Task(
                Clock.parsePatternIntoMinutes("9:30"),
                12));
        return tasks;
    }
}
