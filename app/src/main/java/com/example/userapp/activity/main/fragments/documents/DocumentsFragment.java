package com.example.userapp.activity.main.fragments.documents;

import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
     boolean viewCreated;
    ActivityResultLauncher<String> pdfPickerLauncher;

    public DocumentsFragment() {
        this.documentsFragmentController = new DocumentsFragmentController(this);
        this.dataFetched = this.documentsFragmentController.chechData();
        this.documentsViewAdapter = null;
        viewCreated = false;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pdfPickerLauncher= this.registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {

                        documentsFragmentController.createUploadDialog(uri);

                    }
                }
        );
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
        swiperefreshDocuments.setRefreshing(false);
        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    pdfPickerLauncher.launch("application/pdf");
            }
        });
        viewCreated=true;

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
            documentsFragmentController.displayDocumentsUi();}
        super.onStart();
    }
    public void quitHandlerThread()
    {
        this.documentsFragmentController.quitHandlerThread();
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }
    @Override
    public void onPause() {
        viewCreated=false;
        super.onPause();
    }

    @Override
    public void onResume() {
        viewCreated=true;
        super.onResume();
    }
}