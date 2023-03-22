package edu.wust.durui.model.cornerstone;

public interface Observable {
    void attachObserver(Observer o);

    void detachObserver(Observer o);

    void notifyObservers();
}
