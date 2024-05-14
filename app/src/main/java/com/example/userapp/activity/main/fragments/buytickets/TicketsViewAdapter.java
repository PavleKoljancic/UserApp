package com.example.userapp.activity.main.fragments.buytickets;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userapp.R;
import com.example.userapp.models.TicketType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


public class TicketsViewAdapter extends RecyclerView.Adapter< TicketsViewAdapter.ViewHolder>{

    List<TicketType> tickets;
    private OnClickListener onClickListener;
    public TicketsViewAdapter(Collection<TicketType> tickets) {
        this.tickets = new ArrayList<TicketType>();
        if(tickets!=null)
            this.tickets.addAll(tickets);

    }

    public void setTickets(Collection<TicketType> tickets) {
        this.tickets.clear();
        if(tickets!=null)
            this.tickets.addAll(tickets);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ticket_type_item, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setMyTicketType(tickets.get(position));
        holder.ticketName.setText(tickets.get(position).getName());
        String type = tickets.get(position).getType();
        StringBuilder stringBuilder = new StringBuilder();
        if("periodic".equals(type))
            stringBuilder.append("Periodična karata koja ");
        else stringBuilder.append("Količinska karata koja ");
        if(tickets.get(position).getAmount()!=null)
            stringBuilder.append("važi za " + tickets.get(position).getAmount() +" vožnji.");

        else {
            stringBuilder.append("važi "+tickets.get(position).getValidFor()+" dana.");
        }
        holder.ticketType.setText(stringBuilder.toString());
        holder.price.setText("Cijena: "+tickets.get(position).getCost()+" KM");

        if(tickets.get(position).getNeedsDocumentaion())
            holder.documents.setText("Dokument: "+tickets.get(position).getDocuments().stream().map(documentType -> documentType.getName()).collect(Collectors.joining(" ")));
        else holder.documents.setText("Dokument: Ne zahtijeva");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(tickets.get(position));
                }
            }
        });
    }



    @Override
    public int getItemCount() {

        return this.tickets.size();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(TicketType ticketType);
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

       public TextView ticketName;
        public TextView ticketType;

        public TextView price;

        public void setMyTicketType(TicketType myTicketType) {
            this.myTicketType = myTicketType;
        }

        private TicketType myTicketType;
        public TextView documents;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ticketName = itemView.findViewById(R.id.tickeTypetname);
            ticketType = itemView.findViewById(R.id.typeType);
            price = itemView.findViewById(R.id.price);
            documents = itemView.findViewById(R.id.documentation);

        }




    }
}
