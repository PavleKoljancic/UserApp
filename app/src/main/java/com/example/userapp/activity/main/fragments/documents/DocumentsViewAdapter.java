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
import com.example.userapp.models.Document;
import com.example.userapp.models.DocumentType;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class DocumentsViewAdapter extends RecyclerView.Adapter< DocumentsViewAdapter.ViewHolder>{


    List<Document> documents;
    DocumentsFragmentController documentsFragmentController;
    public DocumentsViewAdapter(Collection<Document> documentNames, DocumentsFragmentController documentsFragmentController) {

        this.documents = new ArrayList<Document>();
        this.documents.addAll(documentNames);
        Collections.reverse(this.documents);
        this.documentsFragmentController = documentsFragmentController;

    }


    public void setData(Collection<Document> responses) {

        this.documents.clear();
        this.documents.addAll(responses);
        Collections.reverse(this.documents);

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
            Document doc = documents.get(position);
            holder.documentNameTextview.setText(doc.getDocumentType().getName());
            if(doc.getComment()!=null)
        holder.documnetCommentTextView.setText("Komentar:"+doc.getComment());
        else holder.documnetCommentTextView.setText("Komentara još nema.");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");


        holder.documnetValidityTextView.setVisibility(View.GONE);
        if(doc.getSupervisorId()==null)

            holder.documentStateTextView.setText("Čeka odobrenje.");
        else if(doc.getApproved()) {
            holder.documentStateTextView.setText("Dokument je odobren.");
            holder.documnetValidityTextView.setVisibility(View.VISIBLE);
            holder.documnetValidityTextView.setText("Važi do:"+simpleDateFormat.format(doc.getDocumentType().getValidUntilDate()));
        }
            else
            holder.documentStateTextView.setText("Dokument je odbačen.");




        holder.downloadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                documentsFragmentController.downloadDocument(doc);
            }
        });

    }



    @Override
    public int getItemCount() {

        return this.documents.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {


        public TextView documentNameTextview;
        public TextView documnetCommentTextView;

        public TextView documentStateTextView;

        public TextView documnetValidityTextView;

        public Button downloadbtn;
        public Button showComment;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            documentNameTextview = itemView.findViewById(R.id.documentName);
            documnetCommentTextView = itemView.findViewById(R.id.documentComment);
            documentStateTextView = itemView.findViewById(R.id.documentState);
            documnetValidityTextView = itemView.findViewById(R.id.documentValidityDate);

            downloadbtn = itemView.findViewById(R.id.downloadBtn);
            showComment = itemView.findViewById(R.id.showCmnt);

            showComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(documnetCommentTextView.getVisibility()==View.GONE)
                        documnetCommentTextView.setVisibility(View.VISIBLE);
                    else documnetCommentTextView.setVisibility(View.GONE);
                }
            });


        }




    }
}
