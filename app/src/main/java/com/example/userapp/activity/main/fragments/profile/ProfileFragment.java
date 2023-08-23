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
    boolean viewCreated=false;
    ProfileFragmentController profileFragmentController;
    CircularProgressIndicator loadProfilePicture;
    LinearProgressIndicator loadData;
    SwipeRefreshLayout swipeRefreshLayout;
    boolean dataFetched;
    public ProfileFragment() {
        // Required empty public constructor
        dataFetched = false;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        profilePicture=getView().findViewById(R.id.profile_picture);

        nameAndLastname=getView().findViewById(R.id.name_and_lastname);
        credit=getView().findViewById(R.id.credit);
        informText=getView().findViewById(R.id.noTicketsText);
        ticketsRecyclerView=getView().findViewById(R.id.userTicketRV);
        ticketsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        userTicketsViewAdapter=null;
        loadProfilePicture = getView().findViewById(R.id.loadPictureProgress);
        loadData = getView().findViewById(R.id.loadingData);
        profileFragmentController= new ProfileFragmentController(this);
        viewCreated=true;
        swipeRefreshLayout = getView().findViewById(R.id.swiperefreshProfile);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                profileFragmentController.reloadData();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        if(!dataFetched) {
            profileFragmentController.loadInitUi();
        dataFetched=true;
        }
        else profileFragmentController.displayExistingData();
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}