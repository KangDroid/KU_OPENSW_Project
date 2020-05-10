package com.kangdroid.scheduling;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FCFSSimulator {
    private final long mTimeSliceLimited = 1000; // in ms
    private JSONArray arrayInfo = null; // Parsed Array Information
    private JobQueue mJobList;
    private int mJobArrivalOrder[];
    private int mJobCount;

    public FCFSSimulator() {
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
                mJobList.pushJob(new Job(t, bt, priority));
            }

            // Save order.
            mJobArrivalOrder[i] = Integer.parseInt(t);

            // Block till timer finishes.
            boolean whatever = false;
            while (timerInterrupt.isAlive())  {
                if (!whatever) {
                    //System.out.println("Waiting For: " + i);
                    whatever = true;
                }
            }
        }
    }

    public void parseJson() {
        String file_name = "testJob.json";
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
                e.printStackTrace();
            }
        }
    }

    /**
     * TODO: Calculate Gannt Chart Time Return
     */
    class ConsumerClass extends Thread {
        @Override
        public void run() {
            int actualCount = 0;
            boolean printed = false;
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
                    System.out.println("Running Job");
                    System.out.println(tmp + "\n");
                    try {
                        Thread.sleep(tmp.getmBurstTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    actualCount++;
                    printed = false;
                }

                if (actualCount == mJobCount) {
                    break;
                }
            }
        }
    }
}