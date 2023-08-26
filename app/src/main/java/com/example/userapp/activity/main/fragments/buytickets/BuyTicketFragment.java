package com.example.userapp.activity.main.fragments.buytickets;

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
import com.google.android.material.progressindicator.LinearProgressIndicator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BuyTicketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuyTicketFragment extends Fragment {
    LinearProgressIndicator loadData;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView infoText;
    RecyclerView recyclerView;
    TicketsViewAdapter ticketsViewAdapter;
    boolean dataFetched;


    BuyTicketFragmentController buyTicketFragmentController;
     boolean viewCreated;

    public BuyTicketFragment() {
        buyTicketFragmentController = new BuyTicketFragmentController(this);
        ticketsViewAdapter = null;
        dataFetched = false;
        viewCreated=false;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buy_ticket, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         loadData = getView().findViewById(R.id.loadingDataTickets);
         swipeRefreshLayout= getView().findViewById(R.id.swiperefreshTicket);
         infoText= getView().findViewById(R.id.infoTextTickets);
         recyclerView= getView().findViewById(R.id.rvtickets);
         recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                buyTicketFragmentController.loadTickets();
            }
        });
        viewCreated=true;

    }

    @Override
    public void onStart() {
        if(!dataFetched) {
            buyTicketFragmentController.loadTickets();
            dataFetched=true;
        }
        else buyTicketFragmentController.displayTicketsUi();
        super.onStart();
    }

    public void quitHandlerThread()
    {
        this.buyTicketFragmentController.quitHandlerThread();
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