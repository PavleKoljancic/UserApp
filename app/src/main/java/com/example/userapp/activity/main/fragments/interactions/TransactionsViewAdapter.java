package com.example.userapp.activity.main.fragments.interactions;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userapp.R;
import com.example.userapp.activity.main.fragments.buytickets.TicketsViewAdapter;
import com.example.userapp.models.Transaction;

import java.util.ArrayList;
import java.util.Collections;

public class TransactionsViewAdapter extends RecyclerView.Adapter< TransactionsViewAdapter.ViewHolder>{

    private ArrayList<Transaction> transactions;
    public TransactionsViewAdapter(ArrayList<Transaction> transactions) {
        this.transactions = new ArrayList<>(transactions);
        Collections.reverse(this.transactions);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_item, parent, false);

        return new TransactionsViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = this.transactions.get(position);
        holder.amount.setText(transaction.getAmount()+"");
        holder.datetime.setText(transaction.getTimestamp().toString());

        if(transaction.getSupervisorId()!=null) {
            holder.type.setText("Uplata kredita");
            holder.type.setTextColor(Color.parseColor("#00c853"));
        }
        if(transaction.getTicketRequestResponseId()!=null)
        {
            holder.type.setText("Kupovina karte");

            holder.type.setTextColor(Color.parseColor("#c62828"));
        }

        if(transaction.getTerminalId()!=null)
        {
            holder.type.setText("Jednokratna karta");

            holder.type.setTextColor(Color.parseColor("#c62828"));
        }
    }



    @Override
    public int getItemCount() {
        return this.transactions.size();
    }

    public void setData(ArrayList<Transaction> transactions) {

        this.transactions.clear();
        this.transactions.addAll(transactions);
        Collections.reverse(this.transactions);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView type;
        public  TextView amount;
        public   TextView datetime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.transactionType);
            amount = itemView.findViewById(R.id.transactionAmount);
            datetime = itemView.findViewById(R.id.transactionDateTime);
        }
    }
}
