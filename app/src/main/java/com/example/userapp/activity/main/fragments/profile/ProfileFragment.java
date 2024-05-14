package com.example.userapp.activity.main.fragments.profile;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.userapp.R;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ProfileFragment extends Fragment {


    ImageView profilePicture;

    TextView nameAndLastname;
    Button credit;
    TextView informText;
    RecyclerView ticketsRecyclerView;
    UserTicketsViewAdapter userTicketsViewAdapter;

    ProfileFragmentController profileFragmentController;
    CircularProgressIndicator loadProfilePicture;
    LinearProgressIndicator loadData;
    SwipeRefreshLayout swipeRefreshLayout;
    Button qrGen;
    ImageView qrDisplay;
    boolean dataFetched;


    boolean viewCreated;
    public ProfileFragment() {
        // Required empty public constructor
        profileFragmentController= new ProfileFragmentController(this);
        dataFetched = profileFragmentController.checkData();
        viewCreated = false;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profilePicture=getView().findViewById(R.id.profile_picture);

        nameAndLastname=getView().findViewById(R.id.name_and_lastname);
        credit=getView().findViewById(R.id.credit);
        informText=getView().findViewById(R.id.noTicketsText);
        ticketsRecyclerView=getView().findViewById(R.id.userTicketRV);
        qrGen = getView().findViewById(R.id.qrbutton);
        qrDisplay = getView().findViewById(R.id.qrdisplay);
        ticketsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        userTicketsViewAdapter=null;
        loadProfilePicture = getView().findViewById(R.id.loadPictureProgress);
        loadData = getView().findViewById(R.id.loadingData);


        swipeRefreshLayout = getView().findViewById(R.id.swiperefreshProfile);
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                profileFragmentController.reloadData();
            }
        });
        viewCreated=true;

        qrGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileFragmentController.qrClikced(qrDisplay);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        profileFragmentController.subscribeToUserDataModel();
        if(!dataFetched) {
            profileFragmentController.loadInit();
            dataFetched=true;
        }
        else profileFragmentController.displayExistingData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    public void quitHandlerThread()
    {
        this.profileFragmentController.quitHandlerThread();
    }

    @Override
    public void onDestroyView() {
        swipeRefreshLayout.setRefreshing(false);

        profileFragmentController.unsubscribeToUserDataModel();

        super.onDestroyView();
    }

    @Override
    public void onPause() {
        viewCreated=false;
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewCreated=true;

    }
}