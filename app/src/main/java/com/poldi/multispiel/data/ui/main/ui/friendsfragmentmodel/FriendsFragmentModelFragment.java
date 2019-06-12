package com.poldi.multispiel.data.ui.main.ui.friendsfragmentmodel;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poldi.multispiel.R;

public class FriendsFragmentModelFragment extends Fragment {

    private FriendsFragmentModelViewModel mViewModel;

    public static FriendsFragmentModelFragment newInstance() {
        return new FriendsFragmentModelFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.friends_fragment_model_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FriendsFragmentModelViewModel.class);
        // TODO: Use the ViewModel
    }

}
