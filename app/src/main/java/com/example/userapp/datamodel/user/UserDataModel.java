package com.example.userapp.datamodel.user;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.userapp.datamodel.CacheLayer;
import com.example.userapp.models.Document;
import com.example.userapp.models.User;
import com.example.userapp.models.UserTicket;
import com.example.userapp.nfc.UserAppHceService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import lombok.Getter;

@Getter
public class UserDataModel {
    private User user;
    private String userKey;
    private HashSet<UserTicket> userTickets;
    private List<Document> userDocuments;
    private Bitmap userProfilePicture;


    private byte [] rawPictureBytes;


    private HashSet<UserDataChangeSubscriber> dataChangeSubscribers;
    private static UserDataModel userDataModel = null;

    private UserDataModel() {
        this.user = null;
        this.userTickets = new HashSet<UserTicket>();
        this.dataChangeSubscribers = new HashSet<UserDataChangeSubscriber>();
        this.userProfilePicture = null;
        this.userDocuments = new ArrayList<Document>();
        this.rawPictureBytes = null;
    }

    public static UserDataModel getInstance() {
        if (userDataModel == null)
            userDataModel = new UserDataModel();
        return userDataModel;
    }

    public boolean subscribeToDataChange(UserDataChangeSubscriber subscriber) {
        synchronized (this.dataChangeSubscribers) {
            return this.dataChangeSubscribers.add(subscriber);
        }
    }

    public boolean unsubscribeToDataChange(UserDataChangeSubscriber subscriber) {
        synchronized (this.dataChangeSubscribers) {
            return this.dataChangeSubscribers.remove(subscriber);
        }
    }

    private void informSubscribers(User user, HashSet<UserTicket> userTickets, Bitmap userProfilePicture, List<Document> userDocuments) {
        synchronized (dataChangeSubscribers) {
            for (UserDataChangeSubscriber subscriber : dataChangeSubscribers)
                if (subscriber != null)
                    subscriber.onUserDataChanged(user, userTickets, userProfilePicture, userDocuments);
        }
        final byte [] rawPictureBytesToSave = userProfilePicture ==null ? null:rawPictureBytes;

        new Thread(()->saveUserDataModel(user,userTickets,this.userKey,rawPictureBytesToSave,userDocuments)).start();

    }

    public void updateUser(User user,String userKey) {
        Boolean dataChanged = false;
        if (this.user == null) {
            if (user != null) {
                this.user = user;
                this.userKey = userKey;
                UserAppHceService.setAuthData(this.user.getId(),this.userKey);
                dataChanged = true;
            }
        } else
            synchronized (this.user) {
                if (!this.user.equals(user)&&user!=null) {
                    this.userKey = userKey;
                    UserAppHceService.setAuthData(user.getId(),userKey);
                    this.user = user;
                    dataChanged = true;
                }
            }
        if (dataChanged)
            informSubscribers(this.user, null, null, null);
    }

    public void updateUserTicket(Collection<UserTicket> userTickets) {
        boolean dataChanged = false;
        synchronized (this.userTickets) {
            if (userTickets != null) {
                if (!(this.userTickets.containsAll(userTickets) && userTickets.containsAll(this.userTickets))) {
                    this.userTickets.clear();
                    this.userTickets.addAll(userTickets);
                    dataChanged = true;
                }

            }
        }
        if (dataChanged)
            informSubscribers(null, this.userTickets, null, null);
    }

    public void updateUserDocuments(List<Document> userDocuments) {
        boolean dataChanged = false;
        synchronized (this.userDocuments) {
            if (userDocuments != null) {
                if (!(this.userDocuments.containsAll(userDocuments) && userDocuments.containsAll(this.userDocuments))) {
                    this.userDocuments.clear();
                    this.userDocuments.addAll(userDocuments);
                    dataChanged = true;
                }

            }
        }
        if (dataChanged)
            informSubscribers(null, null, null,this.userDocuments);
    }

    public void updateUserProfilePicture(byte[] pictureBytes) {
        if (pictureBytes != null) {
            this.userProfilePicture = BitmapFactory.decodeByteArray(pictureBytes, 0, pictureBytes.length, new BitmapFactory.Options());
            this.rawPictureBytes =pictureBytes;
            informSubscribers(null, null,  this.userProfilePicture,null);
        }
    }

    private void saveUserDataModel(User user, HashSet<UserTicket> userTickets, String userKey, byte[] rawPictureBytes, List<Document>userDocuments) {

        CacheLayer cacheLayer = CacheLayer.getInstance();
        cacheLayer.writeObject(user,"user.ser");
        cacheLayer.writeObject(userTickets,"userTickets.ser");
        cacheLayer.writeObject(userKey,"userKey.ser");
        cacheLayer.writeObject(rawPictureBytes,"userProfilePic.ser");
        cacheLayer.writeObject(userDocuments,"userDocuments.ser");

    }



    public void loadUserDataModelFromFile() {
        CacheLayer cacheLayer = CacheLayer.getInstance();
        user =  (User) cacheLayer.readObject("user.ser");
        HashSet<UserTicket> tempTickets = (HashSet<UserTicket>)cacheLayer.readObject("userTickets.ser");
        if(tempTickets!=null){
            userTickets.clear();
            userTickets.addAll(tempTickets);
        }
        userKey  = (String) cacheLayer.readObject("userKey.ser");
        rawPictureBytes = (byte []) cacheLayer.readObject("userProfilePic.ser");
        if(rawPictureBytes!=null && rawPictureBytes.length>0)
            userProfilePicture = BitmapFactory.decodeByteArray(rawPictureBytes, 0, rawPictureBytes.length, new BitmapFactory.Options());
        List<Document> tempDocs = (List<Document>) cacheLayer.readObject("userDocuments.ser");
        if(tempDocs!=null)
        {
            userDocuments.clear();
            userDocuments.addAll(tempDocs);
        }
    }

}
