package com.scheduler.TaskScheduler.Util;

public class Pair<T, U> {
    private T first;
    private U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getKey() {
        return first;
    }

    public void setKey(T first) {
        this.first = first;
    }

    public U getValue() {
        return second;
    }

    public void setValue(U second) {
        this.second = second;
    }
}
