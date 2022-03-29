package com.example.hamzaos;

public class pMemory {
    static int size;
    static int frames[];
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        pMemory.size = size;
    }

    public int[] getFrames() {
        return frames;
    }

    public void setFrames(int[] frames) {
        pMemory.frames = frames;
    }

    public pMemory(int size) {
        this.size = size;
    }

    public pMemory() {
    }
}
