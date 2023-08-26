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


    ProfileFragmentController(ProfileFragment profileFragment) {
        super("Profile fragment HandlerThread");
        this.profileFragment = profileFragment;
        this.userDataModel = UserDataModel.getInstance();
        this.userProfileApiDecorator = new UserProfileApiDecorator();


    }

    void subscribeToUserDataModel() {
        this.userDataModel.subscribeToDataChange(this);
    }

    void unsubscribeToUserDataModel() {
        this.userDataModel.unsubscribeToDataChange(this);
    }

    @Override
    public void onUserDataChanged(User user, HashSet<UserTicket> userTickets, HashSet<TicketRequestResponse> ticketRequestResponses, HashSet<TicketRequest> unprocessedTicketRequest, Bitmap userProfilePicture) {
        if (user != null&&profileFragment.getActivity()!=null)
            profileFragment.getActivity().runOnUiThread(() -> updateUserTextUi());
        if (userProfilePicture != null&&profileFragment.getActivity()!=null)
            profileFragment.getActivity().runOnUiThread(() -> updateProfilePictureUi(userProfilePicture));
        if (userTickets != null&&profileFragment.getActivity()!=null)
            profileFragment.getActivity().runOnUiThread(() -> updateUserTicketsUi(userTickets));
    }

    public void reloadData() {
        changeToNotLoadingUI();
        Handler handler = new Handler(super.handlerThread.getLooper());
        changeToNotLoadingUI();
        handler.post(() -> {

            try {

                this.userDataModel.updateUser(this.userProfileApiDecorator.loadUser());
            } catch (JSONException | IOException e) {

            }
            try {
                userDataModel.updateUserTicket(userProfileApiDecorator.getListUserTickets(userDataModel.getUser()));
            } catch (IOException e) {

            } finally {
                if(profileFragment.getActivity()!=null)
                profileFragment.getActivity().runOnUiThread(() -> changeToNotLoadingUI());
            }
        });
    }


    public void loadInit() {
        updateUserTextUi();
        fetchUserProfilePicture();
        fetchUserTickets();
    }


    private void fetchUserProfilePicture() {
        Handler handler = new Handler(super.handlerThread.getLooper());
        handler.post(() -> {
            try {

                userDataModel.updateUserProfilePicture(this.userProfileApiDecorator.getUserProfilePicture(userDataModel.getUser()));
            } catch (IOException e) {

            }
        });
    }

    private void fetchUserTickets() {
        Handler handler = new Handler(super.handlerThread.getLooper());
        handler.post(() -> {
            try {

                userDataModel.updateUserTicket(userProfileApiDecorator.getListUserTickets(userDataModel.getUser()));
            } catch (IOException e) {

            }
        });
    }

    public void displayExistingData() {

        updateUserTextUi();
        updateProfilePictureUi(userDataModel.getUserProfilePicture());
        updateUserTicketsUi(userDataModel.getUserTickets());

    }

    private void changeToNotLoadingUI() {
        if (profileFragment.viewCreated) {
            profileFragment.loadData.setVisibility(View.INVISIBLE);
            profileFragment.swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void changeToLoadingUI() {
        if (profileFragment.viewCreated)
            profileFragment.loadData.setVisibility(View.VISIBLE);
    }

    private void updateUserTicketsUi(HashSet<UserTicket> userTickets) {
        if (profileFragment.viewCreated) {
            if (userTickets != null && userTickets.size() > 0)
                if (profileFragment.userTicketsViewAdapter == null) {
                    profileFragment.userTicketsViewAdapter = new UserTicketsViewAdapter(userTickets);
                    profileFragment.ticketsRecyclerView.setAdapter(profileFragment.userTicketsViewAdapter);
                } else {
                    profileFragment.userTicketsViewAdapter.setUserTickets(userTickets);
                    profileFragment.userTicketsViewAdapter.notifyDataSetChanged();
                    if (profileFragment.ticketsRecyclerView.getAdapter() == null)
                        profileFragment.ticketsRecyclerView.setAdapter(profileFragment.userTicketsViewAdapter);
                }
            profileFragment.informText.setVisibility(View.INVISIBLE);
            profileFragment.ticketsRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void updateProfilePictureUi(Bitmap userProfilePicture) {
        if (profileFragment.viewCreated) {
            profileFragment.profilePicture.setImageBitmap(userProfilePicture);
            profileFragment.loadProfilePicture.setVisibility(View.INVISIBLE);
            changeToNotLoadingUI();
            profileFragment.profilePicture.setVisibility(View.VISIBLE);
        }


    }

    private void updateUserTextUi() {
        if (profileFragment.viewCreated) {
            profileFragment.nameAndLastname.setText(userDataModel.getUser().getFirstName() + " " + userDataModel.getUser().getLastName());
            profileFragment.credit.setText("Kredit: " + userDataModel.getUser().getCredit() + "KM");
        }
    }
}
