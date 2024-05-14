package com.example.userapp.datamodel.interactions;

import com.example.userapp.datamodel.CacheLayer;
import com.example.userapp.models.Route;
import com.example.userapp.models.ScanInteraction;
import com.example.userapp.models.Transaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class InteractionsDataModel {

     private static  InteractionsDataModel instance=null;

     public static InteractionsDataModel getInstance()
     {
         if(instance==null)
              instance = new InteractionsDataModel();
         return instance;
     }
      private ArrayList<Route> routes;
     private ArrayList<ScanInteraction> scanInteractions;
     private ArrayList<Transaction> transactions;

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(Collection<Route> routes) {
        if(routes!=null) {
            this.routes.clear();
            this.routes.addAll(routes);
            CacheLayer cacheLayer = CacheLayer.getInstance();
            cacheLayer.writeObject(this.routes,"routes");
        }
    }

    public ArrayList<ScanInteraction> getScanInteractions() {
        return scanInteractions;
    }

    public void setScanInteractions(Collection<ScanInteraction> scanInteractions) {
        if(scanInteractions!=null) {
            this.scanInteractions.clear();
            this.scanInteractions.addAll(scanInteractions);
            CacheLayer cacheLayer = CacheLayer.getInstance();
            cacheLayer.writeObject(this.scanInteractions,"interactions");
        }
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Collection<Transaction> transactions) {
        if(transactions!=null) {
            this.transactions.clear();
            this.transactions.addAll(transactions);
            CacheLayer cacheLayer = CacheLayer.getInstance();
            cacheLayer.writeObject(this.transactions,"transactions");
        }
    }

    private InteractionsDataModel()
     {
         routes = new ArrayList<>();
         scanInteractions = new ArrayList<>();
         transactions = new ArrayList<>();
     }

     public void loadFromFile()
     {

         CacheLayer cacheLayer = CacheLayer.getInstance();
         ArrayList<Transaction> temptransactions = (ArrayList<Transaction>) cacheLayer.readObject("transactions");

         ArrayList<ScanInteraction> tempscanInteractions = (ArrayList<ScanInteraction>) cacheLayer.readObject("interactions");
         ArrayList<Route> temproutes = (ArrayList<Route>) cacheLayer.readObject("routes");
        if(temproutes!=null)
        {
            this.routes.clear();
            this.routes.addAll(temproutes);

        }
        if(tempscanInteractions!=null)
        {
            this.scanInteractions.clear();
            this.scanInteractions.addAll(tempscanInteractions);
        }
        if(temptransactions!=null)
        {
            this.transactions.clear();
            this.transactions.addAll(temptransactions);
        }
     }
}
