package com.example.userapp.activity.main.fragments.ticketrequests;

import android.os.Handler;
import android.view.View;

import com.example.userapp.activity.ActivityController;
import com.example.userapp.datamodel.requests.RequestDataModel;
import com.example.userapp.models.TicketRequest;
import com.example.userapp.models.TicketRequestResponse;
import com.example.userapp.models.TicketType;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class RequestsFragmentController extends ActivityController {

    RequestsFragment requestsFragment;

    RequestsFragmentModel requestsFragmentModel;
    RequestDataModel requestDataModel;

    protected RequestsFragmentController(RequestsFragment requestsFragment) {
        super("HANDLER THREAD FOR API REQUEST FRAGMENT");
        this.requestsFragment = requestsFragment;
        this.requestDataModel = RequestDataModel.getInstance();
        this.requestsFragmentModel = new RequestsFragmentModel();
    }

    public void displayRequests() {

        requestsFragment.noResponseText.setVisibility(View.INVISIBLE);
        requestsFragment.responseRV.setVisibility(View.INVISIBLE);
        requestDataModel.setResponseSelected(false);
        if(requestDataModel.getUserTicketRequest().size()>0)
        {
            if (requestsFragment.requestViewAdapter == null) {
                requestsFragment.requestViewAdapter = new RequestViewAdapter(requestDataModel.getAllTickets(),requestDataModel.getUserTicketRequest());
                requestsFragment.requestRV.setAdapter(requestsFragment.requestViewAdapter);
            } else {
                if (requestDataModel.getUserTicketResponses().size() == requestsFragment.requestViewAdapter.getItemCount()) {
                    requestsFragment.requestViewAdapter.setData(requestDataModel.getAllTickets(),requestDataModel.getUserTicketRequest());
                    requestsFragment.requestViewAdapter.notifyDataSetChanged();
                }
                if (requestsFragment.requestRV.getAdapter() == null)
                    requestsFragment.requestRV.setAdapter(requestsFragment.requestViewAdapter);
            }

            requestsFragment.requestRV.setVisibility(View.VISIBLE);
        }
       else requestsFragment.noRequestsText.setVisibility(View.VISIBLE);
    }

    public void displayResponse() {
        requestsFragment.noRequestsText.setVisibility(View.INVISIBLE);
        requestDataModel.setResponseSelected(true);
        requestsFragment.requestRV.setVisibility(View.INVISIBLE);
        if (requestDataModel.getUserTicketResponses().size() > 0) {
            if (requestsFragment.responseViewAdapter == null) {
                requestsFragment.responseViewAdapter = new ResponseViewAdapter(requestDataModel.getUserTicketResponses());
                requestsFragment.responseRV.setAdapter(requestsFragment.responseViewAdapter);
            } else {
                if (requestDataModel.getUserTicketResponses().size() == requestsFragment.responseViewAdapter.getItemCount()) {
                    requestsFragment.responseViewAdapter.setData(requestDataModel.getUserTicketResponses());
                    requestsFragment.responseViewAdapter.notifyDataSetChanged();
                }
                if (requestsFragment.responseRV.getAdapter() == null)
                    requestsFragment.responseRV.setAdapter(requestsFragment.responseViewAdapter);
            }
            requestsFragment.responseRV.setVisibility(View.VISIBLE);
        } else requestsFragment.noResponseText.setVisibility(View.VISIBLE);
    }


    private void setUILoading() {

        requestsFragment.loadData.setVisibility(View.VISIBLE);
        requestsFragment.infoText.setText("Učitavnje podataka");
    }

    private void cleanUiLoading() {
        requestsFragment.loadData.setVisibility(View.INVISIBLE);
        requestsFragment.swipeRefreshLayout.setRefreshing(false);
        requestsFragment.infoText.setText("");
        displaySelected();
    }

     void displaySelected()
    {
        if(requestDataModel.isResponseSelected())
            displayResponse();
        else displayRequests();
    }

    public void fetchData() {

        Handler handler = new Handler(handlerThread.getLooper());
        setUILoading();
        handler.post(() -> {

            boolean dataFetchNoError = false;
            try {
                List<TicketRequest> request = requestsFragmentModel.getUserTicketRequests();
                List<TicketType> tickets = requestsFragmentModel.getAllTicketTypes();
                List<TicketRequestResponse> responses = requestsFragmentModel.getUserTicketResponses();
                this.requestDataModel.setUserTicketRequest(request);
                this.requestDataModel.setUserTicketResponses(responses);
                this.requestDataModel.setAllTickets(tickets);
                dataFetchNoError = true;
            } catch (JSONException | IOException e) {
                dataFetchNoError = false;
            } finally {
                final boolean noError = dataFetchNoError;
                requestsFragment.getActivity().runOnUiThread(() -> {
                    cleanUiLoading();
                    if (!noError)

                        requestsFragment.infoText.setText("Desila se greška pri učitavnja podataka");
                });
            }


        });

    }


}
