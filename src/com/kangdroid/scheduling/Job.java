package com.kangdroid.scheduling;

public class Job {
    private String mJobIdentifier;
    private int mBurstTime;
    private int mPriority;

    public Job(String job, int bursttime, int priority) {
        this.mJobIdentifier = job;
        this.mBurstTime = bursttime;
        this.mPriority = priority;
    }

    public int getmBurstTime() {
        return this.mBurstTime;
    }

    @Override
    public String toString() {
        return "Job Identifier: " + mJobIdentifier + "\n" +
                "Burst Time: " + mBurstTime + "\n" +
                "Priority: " + mPriority;
    }
}
