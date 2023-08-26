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
        if (buyTicketFragment.viewCreated) {
            Handler handler = new Handler(handlerThread.getLooper());
            setLoadingUI();
            handler.post(() -> {

                try {
                    ticketDataModel.setTickets(ticketsApiDecorator.getTickets());
                    if(buyTicketFragment.getActivity()!=null)
                    buyTicketFragment.getActivity().runOnUiThread(() -> displayTicketsUi());

                } catch (IOException e) {
                    if(buyTicketFragment.getActivity()!=null)
                    buyTicketFragment.getActivity().runOnUiThread(() -> {
                        setInfoTextUi("Desila se greška pri dobijanju podataka");
                    });
                } finally {
                    if(buyTicketFragment.getActivity()!=null)
                    buyTicketFragment.getActivity().runOnUiThread(() -> {
                        setNotLoadingUi();
                    });

                }

            });
        }
    }


    private void sendRequestForTicket(TicketType ticketType) {
        if (buyTicketFragment.viewCreated) {

            setInfoTextUi("Slanje zahtjeva za kartu");
            setLoadingUI();
            Handler handler = new Handler(handlerThread.getLooper());

            handler.post(() -> {


                try {
                    String response = ticketsApiDecorator.sendTicketRequest(ticketType);
                    if(buyTicketFragment.getActivity()!=null)
                    buyTicketFragment.getActivity().runOnUiThread(() -> {
                        if ("Insufficient funds".equals(response))
                            setInfoTextUi("Nedovoljno kredita.");
                        else if ("Requested Ticket not inUse".equals(response))
                            setInfoTextUi("Zahtjevana karta nije dostupna.");
                        else if ("Successfully added request".equals(response))
                            setInfoTextUi("Zahtjev za kartu je poslat na obradu.");
                        else if ("Ticked Request Processed and Ticket bought".equals(response))
                            setInfoTextUi("Karta kupljena.");
                        else
                            setInfoTextUi("Desila se neočekivana greška!");

                    });
                } catch (JSONException | IOException e) {
                    setInfoTextUi("Desila se greška pri slanju zahtjeva.");
                } finally {
                    if (buyTicketFragment.getActivity() != null)
                        buyTicketFragment.getActivity().runOnUiThread(() ->
                                buyTicketFragment.loadData.setVisibility(View.INVISIBLE)


                        );
                }
            });
        }
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

    private void setInfoTextUi(String text) {
        if (buyTicketFragment.viewCreated) {
            buyTicketFragment.infoText.setText(text);
            buyTicketFragment.infoText.setVisibility(View.VISIBLE);
        }
    }

    private void setNotLoadingUi() {
        if (buyTicketFragment.viewCreated) {
            buyTicketFragment.loadData.setVisibility(View.INVISIBLE);
            buyTicketFragment.swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void setLoadingUI() {
        if (buyTicketFragment.viewCreated) {
            buyTicketFragment.loadData.setVisibility(View.VISIBLE);
            buyTicketFragment.infoText.setVisibility(View.INVISIBLE);
        }
    }

    void displayTicketsUi() {
        if (buyTicketFragment.viewCreated) {
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
    }
}
