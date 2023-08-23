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
        if("periodic".equals(type))
            holder.ticketType.setText("Periodična");
        else holder.ticketType.setText("Količinska");
        if(userTickets.get(position).getUsage()!=null)
            holder.ticketState.setText("Iskorišteno vožnji "+userTickets.get(position).getUsage()+"/"+userTickets.get(position).getType().getAmount() );
        else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            holder.ticketState.setText("Karta važi do " + simpleDateFormat.format(userTickets.get(position).getValidUntilDate()));
        }
    }



    @Override
    public int getItemCount() {

        return this.userTickets.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

       public TextView ticketName;
        public TextView ticketType;
        public TextView ticketState;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ticketName = itemView.findViewById(R.id.ticketname);
            ticketType = itemView.findViewById(R.id.type);
            ticketState = itemView.findViewById(R.id.ticketState);


        }




    }
}
