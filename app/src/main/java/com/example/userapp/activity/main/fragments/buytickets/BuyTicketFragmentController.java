package com.example.userapp.activity.main.fragments.buytickets;

import android.content.DialogInterface;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.example.userapp.activity.AbstractViewController;
import com.example.userapp.datamodel.ticket.TicketDataModel;
import com.example.userapp.models.TicketType;


import org.json.JSONException;

import java.io.IOException;

public class BuyTicketFragmentController extends AbstractViewController {

    BuyTicketFragment buyTicketFragment;
    TicketsApiDecorator ticketsApiDecorator;

    TicketDataModel ticketDataModel;

    BuyTicketFragmentController(BuyTicketFragment buyTicketFragment) {
        super("HANDLER THREAD FOR TICKET FRAGMENT");
        this.buyTicketFragment = buyTicketFragment;
        this.ticketsApiDecorator = new TicketsApiDecorator();
        this.ticketDataModel = TicketDataModel.getInstance();
    }

    void loadTickets() {
        Handler handler = new Handler(handlerThread.getLooper());
        buyTicketFragment.loadData.setVisibility(View.VISIBLE);
        buyTicketFragment.infoText.setVisibility(View.INVISIBLE);
        handler.post(() -> {

            try {
                ticketDataModel.setTickets(ticketsApiDecorator.getTickets());
                buyTicketFragment.getActivity().runOnUiThread(() -> displayTickets());

            } catch (IOException e) {
                buyTicketFragment.getActivity().runOnUiThread(() -> {
                    buyTicketFragment.infoText.setText("Desila se greška pri dobijanju podataka");
                    buyTicketFragment.infoText.setVisibility(View.VISIBLE);
                });
            } finally {
                buyTicketFragment.getActivity().runOnUiThread(() -> {
                    buyTicketFragment.loadData.setVisibility(View.INVISIBLE);
                    buyTicketFragment.swipeRefreshLayout.setRefreshing(false);
                });

            }

        });

    }

    void displayTickets() {
        if (this.buyTicketFragment.ticketsViewAdapter == null) {
            buyTicketFragment.ticketsViewAdapter = new TicketsViewAdapter(ticketDataModel.getTickets());
            buyTicketFragment.ticketsViewAdapter.setOnClickListener(new TicketsViewAdapter.OnClickListener() {
                @Override
                public void onClick(TicketType ticketType) {

                    onClickInner(ticketType);


                }
            });
            buyTicketFragment.recyclerView.setAdapter(buyTicketFragment.ticketsViewAdapter);
        } else {
            buyTicketFragment.ticketsViewAdapter.setTickets(ticketDataModel.getTickets());
            buyTicketFragment.ticketsViewAdapter.notifyDataSetChanged();
            if (buyTicketFragment.recyclerView.getAdapter() == null)
                buyTicketFragment.recyclerView.setAdapter(buyTicketFragment.ticketsViewAdapter);
        }
    }

    private void sendRequestForTicket(TicketType ticketType) {
        buyTicketFragment.loadData.setVisibility(View.VISIBLE);
        buyTicketFragment.infoText.setText("Slanje zahtjeva za kartu");
        buyTicketFragment.infoText.setVisibility(View.VISIBLE);
        Handler handler = new Handler(handlerThread.getLooper());

        handler.post(() -> {


            try {
                String response = ticketsApiDecorator.sendTicketRequest(ticketType);
                buyTicketFragment.getActivity().runOnUiThread(() -> {
                    if("Insufficient funds".equals(response))
                    buyTicketFragment.infoText.setText("Nedovoljno kredita.");
                    else if("Requested Ticket not inUse".equals(response))
                        buyTicketFragment.infoText.setText("Zahtjevana karta nije dostupna.");
                    else if("Successfully added request".equals(response))
                        buyTicketFragment.infoText.setText("Zahtjev za kartu je poslat na obradu.");
                    else if("Ticked Request Processed and Ticket bought".equals(response))
                        buyTicketFragment.infoText.setText("Karta kupljena.");
                    else
                        buyTicketFragment.infoText.setText("Desila se neočekivana greška!");

                });
            } catch (JSONException | IOException e) {
                buyTicketFragment.infoText.setText("Desila se greška pri slanju zahtjeva.");
            } finally {
                buyTicketFragment.getActivity().runOnUiThread(() ->
                        buyTicketFragment.loadData.setVisibility(View.INVISIBLE)


                );
            }
        });

    }

    private void onClickInner(TicketType ticketType) {

        AlertDialog.Builder builder = new AlertDialog.Builder(buyTicketFragment.getActivity());


        builder.setMessage("Da li ste sigurni da želite da kupite kartu?")
                .setTitle(ticketType.getName());
        builder.setPositiveButton("Ne", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.setNegativeButton("Da", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                sendRequestForTicket(ticketType);
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
