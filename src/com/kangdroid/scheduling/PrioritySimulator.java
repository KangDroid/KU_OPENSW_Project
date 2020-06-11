package com.kangdroid.scheduling;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class PrioritySimulator {
    private final long mTimeSliceLimited = 1000; // in ms
    private JSONArray arrayInfo = null; // Parsed Array Information
    private JobQueue mJobList;
    private int mJobArrivalOrder[];
    private int mJobCount;
    String file_name;

    public PrioritySimulator(String f) {
        mJobList = new JobQueue();
        mJobArrivalOrder = new int[mJobList.getMax_size()];
        this.file_name = f;
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
                mJobList.pushJob(new Job(t, bt, priority, 2));
                mJobList.sortSJF();
                mJobList.checkPriority();
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
        long returnTime[] = new long[mJobCount];
        int waitedTimeIter = 0, returnTimeIter = 0;
        @Override
        public void run() {
            int actualCount = 0;
            boolean printed = false;
            // Average return time
            int timeReturnNIDLE = 0;
            int timeReturnNIDLEFinal = 0;
            int last_value = 0;

            // Timer
            long timer_t = System.currentTimeMillis();
            long awt_timer = System.currentTimeMillis();

            // Averate Wait time
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
                    // AWT
                    waitedTime[waitedTimeIter++] = System.currentTimeMillis() - awt_timer;

                    // Run Job
                    System.out.println("Running Job");
                    System.out.println(tmp + "\n");
                    timeReturnNIDLE += tmp.getmBurstTime();
                    timeReturnNIDLEFinal += timeReturnNIDLE;
                    last_value = timeReturnNIDLE;
                    try {
                        Thread.sleep(tmp.getmBurstTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Record time [ART]
                    returnTime[returnTimeIter++] = System.currentTimeMillis() - timer_t;

                    actualCount++;
                    printed = false;
                }

                if (actualCount == mJobCount) {
                    long final_value = 0;
                    long awt_final = 0;
                    for (int i = 0; i < mJobCount; i++) {
                        final_value += returnTime[i];
                        awt_final += waitedTime[i];
                        System.out.println(waitedTime[i]);
                    }
                    //timeReturnEnd = System.currentTimeMillis();
                    System.out.println("Average Return Time(With IDLE TIME): " + ((final_value) / (double)mJobCount));
                    System.out.println("Average Return Time(WITHOUT IDLE TIME): " + (timeReturnNIDLEFinal / (double)mJobCount));

                    System.out.println("Average Waited Time(With IDLE TIME): " + (awt_final / (double)mJobCount));
                    System.out.println("Average Waited Tiem(WITHOUT IDLE TIME): " + (timeReturnNIDLEFinal - last_value) / (double)mJobCount);
                    break;
                }
            }
        }
    }
}
