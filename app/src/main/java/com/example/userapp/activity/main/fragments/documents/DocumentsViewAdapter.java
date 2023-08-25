package com.example.userapp.activity.main.fragments.documents;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userapp.R;
import com.example.userapp.models.TicketRequestResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class DocumentsViewAdapter extends RecyclerView.Adapter< DocumentsViewAdapter.ViewHolder>{


    List<String> documentNames;
    DocumentsFragmentController documentsFragmentController;
    public DocumentsViewAdapter(Collection<String> documentNames, DocumentsFragmentController documentsFragmentController) {

        this.documentNames = new ArrayList<String>();
        this.documentNames.addAll(documentNames);
        this.documentsFragmentController = documentsFragmentController;

    }


    public void setData(Collection<String> responses) {

        this.documentNames.clear();
        this.documentNames.addAll(responses);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.document_item, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String documentName = documentNames.get(position);
            holder.documentNameTextview.setText(documentName);
            holder.documentName=documentName;
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    documentsFragmentController.deleteDocument(holder.documentName);
                }
            });

        holder.downloadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                documentsFragmentController.downloadDocument(holder.documentName);
            }
        });

    }



    @Override
    public int getItemCount() {

        return this.documentNames.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

       public String documentName;
        public TextView documentNameTextview;

        public Button downloadbtn;
        public  Button deleteBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            documentNameTextview = itemView.findViewById(R.id.DocumentName);
            downloadbtn = itemView.findViewById(R.id.downloadbtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);

        }




    }
}
