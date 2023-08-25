package com.example.userapp.activity;

import android.os.HandlerThread;

import com.example.userapp.datamodel.user.UserDataModel;

public abstract class AbstractViewController {
    protected HandlerThread handlerThread;
    protected UserDataModel userDataModel;

    protected AbstractViewController(String HandlerName)
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
