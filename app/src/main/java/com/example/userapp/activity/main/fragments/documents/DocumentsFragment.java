package com.example.userapp.activity.main.fragments.documents;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.userapp.R;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class DocumentsFragment extends Fragment {


    RecyclerView documentsRv;

    Button uploadbtn;
    DocumentsFragmentController documentsFragmentController;
    SwipeRefreshLayout swiperefreshDocuments;

    LinearProgressIndicator progressIndicator;
    DocumentsViewAdapter documentsViewAdapter;
    boolean dataFetched;
    public DocumentsFragment() {
        this.documentsFragmentController = new DocumentsFragmentController(this);
        this.dataFetched = false;
        this.documentsViewAdapter = null;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.documentsRv = view.findViewById(R.id.documentsRV);
        this.documentsRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        this.uploadbtn = view.findViewById(R.id.addDocument);
        this.swiperefreshDocuments = view.findViewById(R.id.swiperefreshDocuments);
        this.progressIndicator = view.findViewById(R.id.loadingDataDocuments);
        swiperefreshDocuments.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                documentsFragmentController.fetchData();
            }
        });
        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    documentsFragmentController.launchPdfPicker();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_documents, container, false);
    }
    @Override
    public void onStart() {
        if(!dataFetched) {
            documentsFragmentController.fetchData();
            dataFetched=true;
        }
        else { documentsFragmentController.setDataFromDataModel();
            documentsFragmentController.displayDocuments();}
        super.onStart();
    }
    public void quitHandlerThread()
    {
        this.documentsFragmentController.quitHandlerThread();
    }
}