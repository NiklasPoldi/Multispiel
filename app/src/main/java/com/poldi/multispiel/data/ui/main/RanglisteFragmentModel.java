package com.poldi.multispiel.data.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poldi.multispiel.R;

public class RanglisteFragmentModel extends Fragment {

    private RanglisteFragmentModelViewModel mViewModel;

    public static RanglisteFragmentModel newInstance() {
        return new RanglisteFragmentModel();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rangliste_fragment_model_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RanglisteFragmentModelViewModel.class);
        // TODO: Use the ViewModel
    }

}
