package com.kangdroid.scheduling;

public class Job implements Comparable<Job> {
    private String mJobIdentifier;
    private int mBurstTime;
    private int mPriority;
    private int mode; // 0 for FCFS, 1 for SJF, 2 for Priority

    public Job(String job, int bursttime, int priority, int md) {
        this.mJobIdentifier = job;
        this.mBurstTime = bursttime;
        this.mPriority = priority;
        this.mode = md;
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

    @Override
    public int compareTo(Job job) {
        int retVal;
        if (mode == 1) {
            retVal = this.mBurstTime - job.mBurstTime;
        } else if (mode == 2) {
            retVal = this.mPriority - job.mPriority;
        } else {
            retVal = this.mBurstTime - job.mBurstTime; // Default to SJF
        }
        return retVal;
    }
}
