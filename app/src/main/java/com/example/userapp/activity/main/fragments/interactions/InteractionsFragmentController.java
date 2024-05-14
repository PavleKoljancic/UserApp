package com.example.userapp.activity.main.fragments.interactions;

import android.os.Handler;
import android.view.View;

import com.example.userapp.activity.AbstractViewController;
import com.example.userapp.datamodel.interactions.InteractionsDataModel;

import org.json.JSONException;

import java.io.IOException;

public class InteractionsFragmentController extends AbstractViewController {

    private final InteractionsApiDecorator api;
    private final InteractionsDataModel interactionsDataModel;
    private  InteractionsFragment interactionsFragment;
    public InteractionsFragmentController(InteractionsFragment interactionsFragment) {
        super("HANDLER THREAD FOR INTERACTIONS  FRAGMENT");
        this.interactionsFragment = interactionsFragment;
        this.api = new InteractionsApiDecorator();
        this.interactionsDataModel = InteractionsDataModel.getInstance();
    }
    private void setNotLoadingUi() {
        if (interactionsFragment.viewCreated) {

            interactionsFragment.swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void setLoadingUI() {
        if (interactionsFragment.viewCreated) {

            interactionsFragment.swipeRefreshLayout.setRefreshing(true);
        }
    }
    public void refresh() {
        loadData();
    }

    public void loadData() {
        setLoadingUI();
        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(()-> {

            try {
                this.interactionsDataModel.setScanInteractions(api.getUserScanInteractions());
                this.interactionsDataModel.setRoutes(api.getRoutes());
                this.interactionsDataModel.setTransactions(api.getUserTransactions());
                this.interactionsFragment.getActivity().runOnUiThread(()->{

                    interactionsDataUpdate();
                    transactionDataUpdate();
                    displayUi();

                });
            } catch (JSONException e) {

            } catch (IOException e) {

            }
            finally {
                this.interactionsFragment.getActivity().runOnUiThread(()->{
                    setNotLoadingUi();
                });
            }

        });
    }

    public void displayUi() {
        if(interactionsFragment.viewCreated)
        {
            transactonsRVInit();
            scanInteractionRVInit();
            this.interactionsFragment.onSaveState();

        }

    }



    private void scanInteractionRVInit() {
        if (this.interactionsFragment.interactionsViewAdapter == null) {
            interactionsFragment.interactionsViewAdapter = new ScanInteractionViewAdapter(interactionsDataModel.getRoutes(),interactionsDataModel.getScanInteractions(),userDataModel.getUserTickets());
            interactionsFragment.recyclerViewScanInteractions.setAdapter(interactionsFragment.interactionsViewAdapter);
        }

        if (interactionsFragment.recyclerViewScanInteractions.getAdapter() == null)
            interactionsFragment.recyclerViewScanInteractions.setAdapter(interactionsFragment.interactionsViewAdapter);
    }

    private void interactionsDataUpdate() {
        if(interactionsFragment.interactionsViewAdapter==null)
            return;
        interactionsFragment.interactionsViewAdapter.setData(interactionsDataModel.getRoutes(),interactionsDataModel.getScanInteractions(),userDataModel.getUserTickets());
        interactionsFragment.interactionsViewAdapter.notifyDataSetChanged();
    }

    private void transactonsRVInit() {
        if (this.interactionsFragment.transactionsViewAdapter == null) {
            interactionsFragment.transactionsViewAdapter = new TransactionsViewAdapter(interactionsDataModel.getTransactions());

            interactionsFragment.recyclerViewTransactions.setAdapter(interactionsFragment.transactionsViewAdapter);
        }
        if (interactionsFragment.recyclerViewTransactions.getAdapter() == null)
            interactionsFragment.recyclerViewTransactions.setAdapter(interactionsFragment.transactionsViewAdapter);
    }

    private void transactionDataUpdate() {
        if(interactionsFragment.transactionsViewAdapter==null)
            return;
        interactionsFragment.transactionsViewAdapter.setData(interactionsDataModel.getTransactions());
        interactionsFragment.transactionsViewAdapter.notifyDataSetChanged();
    }

    boolean checkData()
    {
       return  this.interactionsDataModel.getScanInteractions()!=null &&this.interactionsDataModel.getScanInteractions().size()!=0 && this.interactionsDataModel.getTransactions()!=null&&this.interactionsDataModel.getTransactions().size()!=0 ;
    }
}
