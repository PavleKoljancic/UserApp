package com.example.userapp.activity;

import android.os.HandlerThread;

import com.example.userapp.userdata.UserDataModel;

public abstract class ActivityController {
    protected HandlerThread handlerThread;
    protected UserDataModel userDataModel;

    protected  ActivityController(String HandlerName)
    {
        this.userDataModel = UserDataModel.getInstance();
        this.handlerThread = new HandlerThread(HandlerName);
        handlerThread.start();
    }

    public void quitHandlerThread()
    {
        this.handlerThread.quit();
    }
}
