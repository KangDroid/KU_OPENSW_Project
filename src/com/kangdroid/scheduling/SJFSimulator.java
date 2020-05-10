package com.kangdroid.scheduling;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SJFSimulator {
    private final long mTimeSliceLimited = 1000; // in ms
    private JSONArray arrayInfo = null; // Parsed Array Information
    private JobQueue mJobList;
    private int mJobArrivalOrder[];
    private int mJobCount;

    public SJFSimulator() {
        mJobList = new JobQueue();
        mJobArrivalOrder = new int[mJobList.getMax_size()];
    }

    public void executor() {
        parseJson();
        ConsumerClass CThread = new ConsumerClass();
        CThread.start();
        pushToQueue();
        try {
            CThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Push job every mTimeSliceLimited
     */
    public void pushToQueue() {
        for (int i = 0; i < arrayInfo.size(); i++) {
            TimerThread timerInterrupt = new TimerThread();
            timerInterrupt.start();
            JSONObject tmp = (JSONObject) arrayInfo.get(i);

            // Get Value from JSON
            String t = (String) tmp.get("job_identifier");
            int bt = Integer.parseInt((String)tmp.get("burst_time"));
            int priority = Integer.parseInt((String)tmp.get("priority"));
            synchronized (mJobList) {
                mJobList.pushJob(new Job(t, bt, priority, 1));
                mJobList.sortSJF();
            }

            // Save order.
            mJobArrivalOrder[i] = Integer.parseInt(t);

            // In SJF, we need time Queue to be sorted.
            // Therefore, interrupt timer when pushing finished.
            timerInterrupt.interrupt();
            while (timerInterrupt.isAlive());
        }
    }

    public void parseJson() {
        String file_name = "testSmall.json";
        JSONParser jsonParser = new JSONParser();
        FileReader fr = null;
        JSONObject jObject = null;

        try {
            fr = new FileReader(file_name);
        } catch(FileNotFoundException fnfe) {
            System.out.println("File Not founded");
            return;
        }

        try {
            jObject = (JSONObject)jsonParser.parse(fr);
        } catch (ParseException | IOException pe) {
            System.out.println("Parse failed");
        }

        // in this state, parsing is done.
        arrayInfo = (JSONArray) jObject.get("job");
        mJobCount = arrayInfo.size();
    }

    class TimerThread extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(mTimeSliceLimited);
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
        }
    }

    /**
     * TODO: Calculate Gannt Chart Time Return
     */
    class ConsumerClass extends Thread {
        long waitedTime[] = new long[mJobCount];
        int waitedTimeIter = 0;
        @Override
        public void run() {
            int actualCount = 0;
            boolean printed = false;
            // Average return time
            long timeReturnStart = System.currentTimeMillis();
            long timeReturnEnd;
            int timeReturnNIDLE = 0;
            int last_value = 0;

            // Averate Wait time
            long timeWaitStart = System.currentTimeMillis();
            long timeWaitEnd;
            while (true) {
                Job tmp;
                synchronized (mJobList) {
                    tmp = mJobList.popJob();
                }
                if (tmp == null) {
                    if (!printed) {
                        System.out.println("No Job, IDLE");
                        printed = true;
                    }
                } else {
                    timeWaitEnd = System.currentTimeMillis();
                    waitedTime[waitedTimeIter++] = timeWaitEnd - timeWaitStart;
                    timeWaitStart = System.currentTimeMillis();
                    System.out.println("Running Job");
                    System.out.println(tmp + "\n");
                    timeReturnNIDLE += tmp.getmBurstTime();
                    last_value = tmp.getmBurstTime();
                    try {
                        Thread.sleep(tmp.getmBurstTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    actualCount++;
                    printed = false;
                }

                if (actualCount == mJobCount) {
                    timeReturnEnd = System.currentTimeMillis();
                    System.out.println("Average Return Time(With IDLE TIME): " + ((timeReturnEnd - timeReturnStart) / (double)mJobCount));
                    System.out.println("Average Return Time(WITHOUT IDLE TIME): " + (timeReturnNIDLE / (double)mJobCount));

                    long timeT = 0;
                    for (int i = 0; i < mJobCount; i++) {
                        timeT += waitedTime[i];
                    }

                    System.out.println("Average Waited Time(With IDLE TIME): " + (timeT / (double)mJobCount));
                    System.out.println("Average Waited Tiem(WITHOUT IDLE TIME): " + (timeReturnNIDLE - last_value) / (double)mJobCount);
                    break;
                }
            }
        }
    }
}
