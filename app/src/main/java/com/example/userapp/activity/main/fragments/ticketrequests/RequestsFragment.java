package com.example.userapp.activity.main.fragments.ticketrequests;

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
import android.widget.TextView;

import com.example.userapp.R;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.progressindicator.LinearProgressIndicator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestsFragment extends Fragment {


    TextView noResponseText;
    TextView noRequestsText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_requests, container, false);
    }
    boolean dataFetched;
    LinearProgressIndicator loadData;
    SwipeRefreshLayout swipeRefreshLayout;

    TextView infoText;
    RequestsFragmentController controller;

    RecyclerView responseRV;
    RecyclerView requestRV;

    ResponseViewAdapter responseViewAdapter = null;
    RequestViewAdapter requestViewAdapter = null;


    public RequestsFragment()
    {
        dataFetched=false;
        controller = new RequestsFragmentController(this);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

         noResponseText = view.findViewById(R.id.textNoResponse);
         noRequestsText=view.findViewById(R.id.textNoRequest);
         swipeRefreshLayout = view.findViewById(R.id.swiperefreshRequests);
         loadData = view.findViewById(R.id.loadingDataRequest);
        MaterialButtonToggleGroup toggleButton = view.findViewById(R.id.toggleButton);

        infoText = view.findViewById(R.id.infoTextRequests);
        responseRV = view.findViewById(R.id.rvResponse);
        responseRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        requestRV = view.findViewById(R.id.rvRequest);
        requestRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        toggleButton.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                                if(checkedId==R.id.requestBtn&&isChecked)
                                {
                                    controller.displayRequests();
                                }
                                if (checkedId==R.id.responseBtn&&isChecked)
                                {
                                    controller.displayResponse();
                                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                controller.fetchData();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        if(!dataFetched) {
            controller.fetchData();
            dataFetched=true;
        }
        else controller.displaySelected();
        super.onStart();
    }

    public void quitHandlerThread()
    {
        this.controller.quitHandlerThread();
    }
}