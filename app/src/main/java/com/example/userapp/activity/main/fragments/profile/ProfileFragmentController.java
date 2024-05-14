package com.example.userapp.activity.main.fragments.profile;

import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.example.userapp.activity.AbstractViewController;
import com.example.userapp.models.Document;
import com.example.userapp.models.User;
import com.example.userapp.models.UserTicket;
import com.example.userapp.datamodel.user.UserDataChangeSubscriber;
import com.example.userapp.datamodel.user.UserDataModel;
import com.example.userapp.otp.TOTPGenerator;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TimerTask;

public class ProfileFragmentController extends AbstractViewController implements UserDataChangeSubscriber {

    ProfileFragment profileFragment;
    UserDataModel userDataModel;
    UserProfileApiDecorator userProfileApiDecorator;

    TOTPGenerator totpGenerator =null;

    ProfileFragmentController(ProfileFragment profileFragment) {


        super("Profile fragment HandlerThread");
        this.profileFragment = profileFragment;
        this.userDataModel = UserDataModel.getInstance();
        this.userProfileApiDecorator = new UserProfileApiDecorator();
        try {
            totpGenerator = new TOTPGenerator(this.userDataModel.getUserKey());
        } catch (NoSuchAlgorithmException e) {

        } catch (InvalidKeyException e) {

        }
    }

    void subscribeToUserDataModel() {
        this.userDataModel.subscribeToDataChange(this);
    }

    void unsubscribeToUserDataModel() {
        this.userDataModel.unsubscribeToDataChange(this);
    }

    @Override
    public void onUserDataChanged(User user, HashSet<UserTicket> userTickets, Bitmap userProfilePicture, List<Document> userDocuments) {
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

                this.userDataModel.updateUser(this.userProfileApiDecorator.loadUser(),this.userProfileApiDecorator.loadUserKey());
                if(this.userDataModel.getUserProfilePicture()==null)
                    this.userDataModel.updateUserProfilePicture(this.userProfileApiDecorator.getUserProfilePicture(userDataModel.getUser()));
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

    public void qrClikced(ImageView qrDisplay) {
        if(qrDisplay!=null)
        {
            if(qrDisplay.getVisibility()==View.GONE)
            {

                MultiFormatWriter mWriter = new MultiFormatWriter();
                try {
                    //BitMatrix class to encode entered text and set Width & Height

                    BitMatrix mMatrix = mWriter.encode(userDataModel.getUser().getId()+"."+totpGenerator.generate(), BarcodeFormat.QR_CODE, 300, 300);
                    BarcodeEncoder mEncoder = new BarcodeEncoder();
                    Bitmap mBitmap = mEncoder.createBitmap(mMatrix);//creating bitmap of code
                    qrDisplay.setImageBitmap(mBitmap);//Setting generated QR code to imageView

                    qrDisplay.setVisibility(View.VISIBLE);


                }
                catch (Exception e)
                {

                }


            }
            else
            {
                qrDisplay.setVisibility(View.GONE);
            }
        }
    }

    public boolean checkData() {
        return this.userDataModel.getUser()!=null && this.userDataModel.getUserDocuments()!=null && this.userDataModel.getUserTickets()!=null&& this.userDataModel.getUserKey() != null && this.userDataModel.getUserProfilePicture() != null;
    }
}
