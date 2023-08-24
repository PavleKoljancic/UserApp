package com.example.userapp.activity.main.fragments.ticketrequests;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userapp.R;
import com.example.userapp.models.TicketRequest;
import com.example.userapp.models.TicketRequestResponse;
import com.example.userapp.models.TicketType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class ResponseViewAdapter extends RecyclerView.Adapter< ResponseViewAdapter.ViewHolder>{


    List<TicketRequestResponse> responses;
    public ResponseViewAdapter( Collection<TicketRequestResponse> responses) {

        this.responses = new ArrayList<TicketRequestResponse>();
        this.responses.addAll(responses);

    }

    public void setData(Collection<TicketRequestResponse> responses) {

        this.responses.clear();
        this.responses.addAll(responses);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.response_item, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            TicketRequestResponse response = responses.get(position);
            holder.comment.setText("Komentar:" + response.getComment());
            if(!response.getApproved())
            {
                holder.response.setBackgroundColor(Color.parseColor("#E53935"));
                holder.response.setText("Odbijen");
            }
    }



    @Override
    public int getItemCount() {

        return this.responses.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

       public TextView comment;
        public Button response;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.comment);
            response = itemView.findViewById(R.id.responseState);

        }




    }
}
