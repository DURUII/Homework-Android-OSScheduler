package edu.wust.durui.model.builder.trial;

import java.util.List;

import edu.wust.durui.model.cornerstone.Task;

public interface TrialProvider {
    List<Task> provide();
}
