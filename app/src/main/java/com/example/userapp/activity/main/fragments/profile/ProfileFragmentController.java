package com.example.userapp.activity.main.fragments.profile;

import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;

import com.example.userapp.activity.AbstractViewController;
import com.example.userapp.models.TicketRequest;
import com.example.userapp.models.TicketRequestResponse;
import com.example.userapp.models.User;
import com.example.userapp.models.UserTicket;
import com.example.userapp.datamodel.user.UserDataChangeSubscriber;
import com.example.userapp.datamodel.user.UserDataModel;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashSet;

public class ProfileFragmentController extends AbstractViewController implements UserDataChangeSubscriber {

    ProfileFragment profileFragment;
    UserDataModel userDataModel;
    UserProfileApiDecorator userProfileApiDecorator;


    ProfileFragmentController(ProfileFragment profileFragment)
    {   super("Profile fragment HandlerThread");
        this.profileFragment=profileFragment;
        this.userDataModel = UserDataModel.getInstance();
        this.userProfileApiDecorator = new UserProfileApiDecorator();


    }

    void subscribeToUserDataModel()
    {
        this.userDataModel.subscribeToDataChange(this);
    }
    void unsubscribeToUserDataModel()
    {
        this.userDataModel.unsubscribeToDataChange(this);
    }
    @Override
    public void onUserDataChanged(User user, HashSet<UserTicket> userTickets, HashSet<TicketRequestResponse> ticketRequestResponses, HashSet<TicketRequest> unprocessedTicketRequest, Bitmap userProfilePicture) {
        if(user!=null)
            profileFragment.getActivity().runOnUiThread(()->updateUserTextUi());
        if(userProfilePicture!=null)
            profileFragment.getActivity().runOnUiThread(()->updateProfilePicture(userProfilePicture));
        if(userTickets!=null)
            profileFragment.getActivity().runOnUiThread(()-> updateUserTickets(userTickets));
    }

    private void updateUserTickets(HashSet<UserTicket> userTickets) {
        if(userTickets!=null&&userTickets.size()>0)
            if(profileFragment.userTicketsViewAdapter==null)
            {
                profileFragment.userTicketsViewAdapter = new UserTicketsViewAdapter(userTickets);
                profileFragment.ticketsRecyclerView.setAdapter(profileFragment.userTicketsViewAdapter);
            }
            else
            {
                profileFragment.userTicketsViewAdapter.setUserTickets(userTickets);
                profileFragment.userTicketsViewAdapter.notifyDataSetChanged();
                if(profileFragment.ticketsRecyclerView.getAdapter()==null)
                    profileFragment.ticketsRecyclerView.setAdapter(profileFragment.userTicketsViewAdapter);
            }
            profileFragment.informText.setVisibility(View.INVISIBLE);
            profileFragment.ticketsRecyclerView.setVisibility(View.VISIBLE);
    }

    private void updateProfilePicture(Bitmap userProfilePicture) {

        profileFragment.profilePicture.setImageBitmap(userProfilePicture);
        profileFragment.loadProfilePicture.setVisibility(View.INVISIBLE);
        profileFragment.loadData.setVisibility(View.INVISIBLE);
        profileFragment.profilePicture.setVisibility(View.VISIBLE);


    }

    private void updateUserTextUi() {

            profileFragment.nameAndLastname.setText(userDataModel.getUser().getFirstName() + " " + userDataModel.getUser().getLastName());
            profileFragment.credit.setText("Kredit: " + userDataModel.getUser().getCredit() + "KM");
    }

    public void reloadData()
    {
        Handler handler = new Handler(super.handlerThread.getLooper());
        profileFragment.loadData.setVisibility(View.VISIBLE);
        handler.post(()-> {

            try {

                    this.userDataModel.updateUser(this.userProfileApiDecorator.loadUser());
            } catch (JSONException | IOException e) {

            }
            try {
                userDataModel.updateUserTicket(userProfileApiDecorator.getListUserTickets(userDataModel.getUser()));
            } catch (IOException e) {

            }
            finally {
                profileFragment.getActivity().runOnUiThread(()->{        profileFragment.loadData.setVisibility(View.INVISIBLE);
                profileFragment.swipeRefreshLayout.setRefreshing(false);
                });
            }
        });
    }

    public void loadInitUi() {
        updateUserTextUi();
        fetchUserProfilePicture();
        fetchUserTickets();
    }



    private void fetchUserProfilePicture()
    {   Handler handler = new Handler(super.handlerThread.getLooper());
        handler.post(()-> {
        try {

            userDataModel.updateUserProfilePicture(this.userProfileApiDecorator.getUserProfilePicture(userDataModel.getUser()));
        } catch (IOException e) {

        } });
    }

    private void fetchUserTickets()
    {   Handler handler = new Handler(super.handlerThread.getLooper());
        handler.post(()-> {
            try {

                userDataModel.updateUserTicket(userProfileApiDecorator.getListUserTickets(userDataModel.getUser()));
            } catch (IOException e) {

            } });
    }

    public void displayExistingData()
    {
           updateUserTextUi();
           updateProfilePicture(userDataModel.getUserProfilePicture());
            updateUserTickets(userDataModel.getUserTickets());

    }
}
