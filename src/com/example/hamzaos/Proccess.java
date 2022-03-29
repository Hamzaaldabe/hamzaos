package com.example.hamzaos;

public class Proccess {
    int PID;
    int start;
    int duration;
    int size;
    String trace;
    int elapsed=0;
    int turnAround=0;
    int wait=0;
    int rem=0;
    public Proccess(int PID, int start, int duration, int size, String trace) {
        this.PID = PID;
        this.start = start;
        this.duration = duration;
        this.size = size;
        this.trace = trace;
    }

    public Proccess() {

    }

    public int getElapsed() {return elapsed;}

    public void setTurnAround(int turnAround) {
        this.turnAround = turnAround;
    }

    public void setWait(int wait) {
        this.wait = wait;
    }

    public int getPID() {
        return PID;
    }

    public int getStart() {
        return start;
    }

    public int getDuration() {
        return duration;
    }

    public int getSize() {
        return size;
    }

    public String getTrace() {
        return trace;
    }



    public int getTurnAround() {
        return turnAround;
    }

    public int getWait() {
        return wait;
    }

    public int getRem() {
        return rem;
    }

    public void setRem(int rem) {
        this.rem = rem;
    }
}
