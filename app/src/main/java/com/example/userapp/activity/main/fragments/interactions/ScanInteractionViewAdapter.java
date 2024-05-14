package com.example.userapp.activity.main.fragments.interactions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userapp.R;
import com.example.userapp.models.Route;
import com.example.userapp.models.ScanInteraction;
import com.example.userapp.models.Transaction;
import com.example.userapp.models.UserTicket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

public class ScanInteractionViewAdapter  extends RecyclerView.Adapter< ScanInteractionViewAdapter.ViewHolder>{
    private  ArrayList<ScanInteraction> scanInteractions;
    private HashMap<Integer,Route> routesMap;
    private  ArrayList<UserTicket> tickets;
    public ScanInteractionViewAdapter(ArrayList<Route> routes, ArrayList<ScanInteraction> scanInteractions, Collection<UserTicket> tickets) {
     this.routesMap =  new HashMap<>();
        buildRouteMap(routes);
        this.scanInteractions = new ArrayList<>(scanInteractions);
        Collections.reverse(this.scanInteractions);
    this.tickets = new ArrayList<>(tickets);
    }

    private void buildRouteMap(ArrayList<Route> routes) {
        for(Route route : routes)
            this.routesMap.put(route.getId(),route);
    }

    @NonNull
    @Override
    public ScanInteractionViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scaninteractions_item, parent, false);

        return new ScanInteractionViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScanInteractionViewAdapter.ViewHolder holder, int position) {

        ScanInteraction scanInteraction = this.scanInteractions.get(position);

        holder.time.setText(scanInteraction.getId().getTime().toString());




        holder.routeName.setText(this.routesMap.get(scanInteraction.getId().getRouteHistoryRouteId()).getName());

        Integer transcationId = scanInteraction.getTransactionId();

        Optional<UserTicket> optionalTicket = this.tickets.stream().filter(userTicket -> userTicket.getTransaction_Id() == transcationId).findFirst();

        if(optionalTicket.isPresent())
        {
            holder.ticketName.setText(optionalTicket.get().getType().getName());
        }
        else
        {holder.ticketName.setText("Jednokratna karta");

        }


    }

    @Override
    public int getItemCount() {
        return this.scanInteractions.size();
    }

    public void setData(ArrayList<Route> routes, ArrayList<ScanInteraction> scanInteractions, Collection<UserTicket> tickets) {
     this.routesMap.clear();
     this.buildRouteMap(routes);
     this.scanInteractions.clear();
     this.scanInteractions.addAll(scanInteractions);
     Collections.reverse(this.scanInteractions);
     this.tickets.clear();
     this.tickets.addAll(tickets);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView ticketName;
        public TextView routeName;
        public TextView time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.scanInteractionTime);
            ticketName = itemView.findViewById(R.id.ticketNameInScanInteraction);
            routeName  = itemView.findViewById(R.id.RouteName);
        }
    }
}
