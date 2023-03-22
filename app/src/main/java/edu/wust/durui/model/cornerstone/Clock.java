package edu.wust.durui.model.cornerstone;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Clock implements Observable {
    public static final int UNIT = 1;
    // main attribute
    public static int minutes = -1;
    // subscribers
    private final List<Observer> observers = new ArrayList<>();

    // TODO timeline logger

    public String getMilestones() {
        return null;
    }

    // increment by one minute
    public void incrementByUnit() {
        minutes += UNIT;
        notifyObservers();
        Log.e("time-line", this + "");
        for (Observer o : observers) {
            Log.e("time-line", o + "");
        }
    }

    // helpers
    public static int parsePatternIntoMinutes(String pattern) {
        String[] parts = pattern.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }

    public static String generatePatternFromMinutes(int minutes) {
        return String.format(Locale.CHINA, "%02d:%02d", minutes / 60, minutes % 60);
    }


    // constructors & setters
    public Clock(int minutes) {
        Clock.minutes = minutes - 1;
    }

    public Clock(String pattern) {
        minutes = parsePatternIntoMinutes(pattern) - 1;
    }

    // observable implementation
    @Override
    public void attachObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void detachObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(minutes, UNIT);
        }
    }

    // logger
    @Override
    public String toString() {
        return generatePatternFromMinutes(minutes);
    }
}
