package com.example.userapp.activity.main.fragments.ticketrequests;

import android.os.Handler;
import android.view.View;

import com.example.userapp.activity.AbstractViewController;
import com.example.userapp.datamodel.requests.RequestDataModel;
import com.example.userapp.models.TicketRequest;
import com.example.userapp.models.TicketRequestResponse;
import com.example.userapp.models.TicketType;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class RequestsFragmentController extends AbstractViewController {

    RequestsFragment requestsFragment;

    RequestsApiDecorator requestsApiDecorator;
    RequestDataModel requestDataModel;

    protected RequestsFragmentController(RequestsFragment requestsFragment) {
        super("HANDLER THREAD FOR API REQUEST FRAGMENT");
        this.requestsFragment = requestsFragment;
        this.requestDataModel = RequestDataModel.getInstance();
        this.requestsApiDecorator = new RequestsApiDecorator();
    }



     void displaySelected()
    {  if(requestsFragment.viewCreated){
        if(requestDataModel.isResponseSelected())
            displayResponseUi();
        else displayRequestsUi();}
    }

    public void fetchData() {

        Handler handler = new Handler(handlerThread.getLooper());
        setLoadingUi();
        handler.post(() -> {

            boolean dataFetchNoError = false;
            try {
                List<TicketRequest> request = requestsApiDecorator.getUserTicketRequests();
                List<TicketType> tickets = requestsApiDecorator.getAllTicketTypes();
                List<TicketRequestResponse> responses = requestsApiDecorator.getUserTicketResponses();
                this.requestDataModel.setUserTicketRequest(request);
                this.requestDataModel.setUserTicketResponses(responses);
                this.requestDataModel.setAllTickets(tickets);
                dataFetchNoError = true;
            } catch (JSONException | IOException e) {
                dataFetchNoError = false;
            } finally {
                final boolean noError = dataFetchNoError;
                if(requestsFragment.getActivity()!=null)
                requestsFragment.getActivity().runOnUiThread(() -> {
                    cleanLoadingUi();
                    if (!noError)
                        setInfoTextUi("Desila se greška pri učitavnja podataka");
                });
            }


        });

    }

    private void setInfoTextUi(String text) {
        requestsFragment.infoText.setText(text);
    }

    public void displayRequestsUi() {
        if(requestsFragment.viewCreated) {
            requestsFragment.noResponseText.setVisibility(View.INVISIBLE);
            requestsFragment.responseRV.setVisibility(View.INVISIBLE);
            requestDataModel.setResponseSelected(false);
            if(requestDataModel.getUserTicketRequest().size()>0)
            {
                requestsFragment.noRequestsText.setVisibility(View.INVISIBLE);
                if (requestsFragment.requestViewAdapter == null) {
                    requestsFragment.requestViewAdapter = new RequestViewAdapter(requestDataModel.getAllTickets(),requestDataModel.getUserTicketRequest());
                    requestsFragment.requestRV.setAdapter(requestsFragment.requestViewAdapter);
                } else {

                    requestsFragment.requestViewAdapter.setData(requestDataModel.getAllTickets(),requestDataModel.getUserTicketRequest());
                    requestsFragment.requestViewAdapter.notifyDataSetChanged();

                    if (requestsFragment.requestRV.getAdapter() == null)
                        requestsFragment.requestRV.setAdapter(requestsFragment.requestViewAdapter);
                }

                requestsFragment.requestRV.setVisibility(View.VISIBLE);
            }
            else { requestsFragment.noRequestsText.setVisibility(View.VISIBLE);
                requestsFragment.requestRV.setVisibility(View.INVISIBLE);
            }}
    }

    public void displayResponseUi() {
        if(requestsFragment.viewCreated){
            requestsFragment.noRequestsText.setVisibility(View.INVISIBLE);
            requestDataModel.setResponseSelected(true);
            requestsFragment.requestRV.setVisibility(View.INVISIBLE);
            if (requestDataModel.getUserTicketResponses().size() > 0) {
                requestsFragment.noResponseText.setVisibility(View.INVISIBLE);
                if (requestsFragment.responseViewAdapter == null) {
                    requestsFragment.responseViewAdapter = new ResponseViewAdapter(requestDataModel.getUserTicketResponses());
                    requestsFragment.responseRV.setAdapter(requestsFragment.responseViewAdapter);
                } else {

                    requestsFragment.responseViewAdapter.setData(requestDataModel.getUserTicketResponses());
                    requestsFragment.responseViewAdapter.notifyDataSetChanged();

                    if (requestsFragment.responseRV.getAdapter() == null)
                        requestsFragment.responseRV.setAdapter(requestsFragment.responseViewAdapter);
                }
                requestsFragment.responseRV.setVisibility(View.VISIBLE);
            } else {
                requestsFragment.noResponseText.setVisibility(View.VISIBLE);
                requestsFragment.responseRV.setVisibility(View.INVISIBLE);
            }}
    }


    private void setLoadingUi() {
        if(requestsFragment.viewCreated){
            requestsFragment.loadData.setVisibility(View.VISIBLE);
            setInfoTextUi("Učitavnje podataka");
        }
    }

    private void cleanLoadingUi() {
        if(requestsFragment.viewCreated){
            requestsFragment.loadData.setVisibility(View.INVISIBLE);
            requestsFragment.swipeRefreshLayout.setRefreshing(false);
            setInfoTextUi("");
            displaySelected();}
    }


}
