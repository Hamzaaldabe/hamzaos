package com.example.hamzaos;

public class PageTracker {
    Pages pages;
    int isOnMemory[] ;

    public PageTracker(Pages pages) {
        this.pages = pages;
    }

    public int[] getIsOnMemory() {
        return isOnMemory;
    }

    public int getPID() {
        return pages.getP().getPID();
    }

    public Pages getPages() {
        return pages;
    }

    public void setIsOnMemory(int[] isOnMemory) {
        this.isOnMemory = isOnMemory;
    }
}
