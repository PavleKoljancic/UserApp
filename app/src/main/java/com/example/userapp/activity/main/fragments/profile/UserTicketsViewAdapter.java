package com.example.userapp.activity.main.fragments.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userapp.R;
import com.example.userapp.models.UserTicket;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;



public class UserTicketsViewAdapter extends RecyclerView.Adapter< UserTicketsViewAdapter.ViewHolder>{

    List<UserTicket> userTickets;

    public UserTicketsViewAdapter(HashSet<UserTicket> userTickets) {
        this.userTickets = new ArrayList<UserTicket>();
        this.userTickets.addAll(userTickets);

    }

    public void setUserTickets(HashSet<UserTicket> userTickets) {
        this.userTickets.clear();
        this.userTickets.addAll(userTickets);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_ticket_item, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ticketName.setText(userTickets.get(position).getType().getName());
        String type = userTickets.get(position).getType().getType();
        StringBuilder stringBuilder = new StringBuilder();
        if("periodic".equals(type))
            stringBuilder.append("Periodična karta");
        else stringBuilder.append("Količinska karta");


        if(userTickets.get(position).getUsage()!=null)
            stringBuilder.append(", broj preostalih vožnji:"+userTickets.get(position).getUsage());
        else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            stringBuilder.append("koja važi do " + simpleDateFormat.format(userTickets.get(position).getValidUntilDate()));
        }
        holder.ticketType.setText(stringBuilder.toString());
    }



    @Override
    public int getItemCount() {

        return this.userTickets.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

       public TextView ticketName;
        public TextView ticketType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ticketName = itemView.findViewById(R.id.ticketname);
            ticketType = itemView.findViewById(R.id.type);



        }




    }
}
