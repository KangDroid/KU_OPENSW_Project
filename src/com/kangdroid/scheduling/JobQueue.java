package com.kangdroid.scheduling;

import java.util.Arrays;

public class JobQueue {
    private int head; // Location to pop
    private int tail; // Location to push
    private int max_size;
    private Job[] mJobQueue;

    public JobQueue() {
        this.max_size = 100;
        this.mJobQueue = new Job[this.max_size];
        this.head = 0;
        this.tail = 0;
    }

    public void pushJob(Job tmpJob) {
        if (tail < max_size) {
            this.mJobQueue[tail++] = tmpJob;
        } else {
            System.out.println("Nothing will pushed.");
        }
    }

    public Job popJob() {
        // Read Operation
        if (head >= tail) {
            return null;
        } else {
            return this.mJobQueue[head++]; // Head is NOT affected with PUSH
        }
    }

    public void clearQueue() {
        this.head = 0;
        this.tail = 0;
        for (int i = 0; i < max_size; i++) {
            mJobQueue[i] = null;
        }
    }

    /**
     * Sort array by SJF
     */
    public void sortSJF() {
        if (mJobQueue != null) {
            Arrays.sort(mJobQueue, head, tail);
        }
    }

    public int getMax_size() {
        return this.max_size;
    }
}
