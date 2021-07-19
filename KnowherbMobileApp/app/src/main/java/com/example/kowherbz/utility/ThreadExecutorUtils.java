package com.example.kowherbz.utility;

import android.os.Handler;
import android.os.HandlerThread;

import java.util.List;
import java.util.TreeMap;

public class ThreadExecutorUtils {
    private final Handler mainHandler;
    private TreeMap<String, Runnable> jobList;

    public ThreadExecutorUtils (String handlerThreadName){
        jobList = new TreeMap<>();
        HandlerThread handlerThread = new HandlerThread(handlerThreadName);
        handlerThread.start();
        mainHandler = new Handler(handlerThread.getLooper());
    }

    public void execute(){
        if(!jobList.isEmpty()){
            for (Runnable job : jobList.values())
                mainHandler.post(job);
            jobList = new TreeMap<>();
        }
    }

    public void addJob(String process_identifier, Runnable runnable){
        jobList.put(process_identifier, runnable);
    }

    public void removeJob(String key){
        if(!jobList.isEmpty())
            jobList.remove(key);
    }

    public Handler getMainHandler() {
        return mainHandler;
    }

}
