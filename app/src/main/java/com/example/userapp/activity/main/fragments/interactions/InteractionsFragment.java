package com.example.userapp.activity.main.fragments.interactions;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.userapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InteractionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InteractionsFragment extends Fragment {

    ScanInteractionViewAdapter interactionsViewAdapter;
    TransactionsViewAdapter transactionsViewAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerViewTransactions;
    RecyclerView recyclerViewScanInteractions;

    Button scanBtn;
    Button transactionsBtn;
    boolean viewCreated ;
    boolean dataFetched;
   private Parcelable transsctionState;
    private Parcelable scanInteractionSate;
     boolean uiPreLoaded;

    InteractionsFragmentController interactionsFragmentController;



    public InteractionsFragment() {
        interactionsFragmentController = new InteractionsFragmentController(this);
        viewCreated=false;
        dataFetched = this.interactionsFragmentController.checkData();
        transsctionState=null;
         scanInteractionSate=null;
         uiPreLoaded = false;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null)
        {
           transsctionState  = savedInstanceState.getParcelable("Transaction State");
            scanInteractionSate = savedInstanceState.getParcelable("Interaction State");
        }
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_interactions, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


    super.onViewCreated(view,savedInstanceState);
         swipeRefreshLayout = getView().findViewById(R.id.swipeRefreshInteractions);
         recyclerViewTransactions = getView().findViewById(R.id.transRVInteractions);
         recyclerViewScanInteractions = getView().findViewById(R.id.scanRVInteractions);


         recyclerViewTransactions.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));






        recyclerViewScanInteractions.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        if(this.scanInteractionSate!=null&&this.transsctionState!=null) {
            recyclerViewScanInteractions.getLayoutManager().onRestoreInstanceState(scanInteractionSate);
            recyclerViewTransactions.getLayoutManager().onRestoreInstanceState(this.transsctionState);
            uiPreLoaded=true;
        }
        else uiPreLoaded=false;

         scanBtn = getView().findViewById(R.id.scanBtnInteractions);
         transactionsBtn = getView().findViewById(R.id.transBtnInteractions);

        viewCreated =true;

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    recyclerViewTransactions.setVisibility(View.GONE);
                    recyclerViewScanInteractions.setVisibility(View.VISIBLE);

            }
        });


        transactionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    recyclerViewTransactions.setVisibility(View.VISIBLE);
                    recyclerViewScanInteractions.setVisibility(View.GONE);

            }
        });

        swipeRefreshLayout.setOnRefreshListener( () -> { interactionsFragmentController.refresh();});
    }

    public void onSaveState() {


        this.transsctionState = recyclerViewTransactions.getLayoutManager().onSaveInstanceState();


        this.scanInteractionSate  =recyclerViewScanInteractions.getLayoutManager().onSaveInstanceState();

    }






    @Override
    public void onStart() {

        if(!dataFetched) {
            interactionsFragmentController.loadData();
            dataFetched=true;
        }
        else interactionsFragmentController.displayUi();

        super.onStart();

    }

    public void quitHandlerThread()
    {
        this.interactionsFragmentController.quitHandlerThread();
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