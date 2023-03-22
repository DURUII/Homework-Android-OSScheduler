package edu.wust.durui.model.cornerstone;

public interface Observer {
    void observe(Observable target);

    void forsake(Observable target);

    void update(int currentTime, int elapseUnit);
}
