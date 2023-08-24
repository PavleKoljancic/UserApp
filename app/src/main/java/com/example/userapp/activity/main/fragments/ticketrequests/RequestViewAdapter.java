package com.example.userapp.activity.main.fragments.ticketrequests;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userapp.R;
import com.example.userapp.models.TicketRequest;
import com.example.userapp.models.TicketType;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class RequestViewAdapter extends RecyclerView.Adapter< RequestViewAdapter.ViewHolder>{

    List<TicketType> tickets;
    List<TicketRequest> ticketRequests;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm dd/MM/yyyy");
    public RequestViewAdapter(Collection<TicketType> tickets, Collection<TicketRequest> ticketRequests) {
        this.tickets = new ArrayList<TicketType>();
        this.tickets.addAll(tickets);
        this.ticketRequests = new ArrayList<TicketRequest>();
        this.ticketRequests.addAll(ticketRequests);

    }

    public void setData(Collection<TicketType> tickets,Collection<TicketRequest> ticketRequests) {
        this.tickets.clear();
        this.tickets.addAll(tickets);
        this.ticketRequests.clear();
        this.ticketRequests.addAll(ticketRequests);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_item, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TicketRequest requset = ticketRequests.get(position);
        TicketType ticketType=null;
        for(TicketType t: tickets)
            if(t.getId()==requset.getTicketTypeId())
                ticketType=t;
        if(ticketType!=null)
            holder.ticketName.setText("Karta:"+ticketType.getName());
        holder.timeDate.setText(simpleDateFormat.format(requset.getDateTime()));
    }



    @Override
    public int getItemCount() {

        return this.ticketRequests.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

       public TextView ticketName;
        public TextView timeDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ticketName = itemView.findViewById(R.id.requesteTicketName);
            timeDate = itemView.findViewById(R.id.requesteDateTime);

        }




    }
}
