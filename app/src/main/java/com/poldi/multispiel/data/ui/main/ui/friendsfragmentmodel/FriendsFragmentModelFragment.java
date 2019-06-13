package com.poldi.multispiel.data.ui.main.ui.friendsfragmentmodel;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.poldi.multispiel.R;

import java.util.ArrayList;

public class FriendsFragmentModelFragment extends Fragment {

    private ArrayList<String> listItems = new ArrayList<String>();
    private ArrayAdapter<String> myListAdapter;
    private ListView listView;
    private FriendsFragmentModelFragment mViewModel;

    public static FriendsFragmentModelFragment newInstance() {
        return new FriendsFragmentModelFragment();
    }

    ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        myListAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                R.id.editText,
                listItems);

        View rootView = inflater.inflate(R.layout.game_fragment_model_fragment, container, false);

        listView = (ListView) rootView.findViewById(R.id.friendship_list);
        listView.setAdapter(myListAdapter);
        //adapter.setListAdapter(adapter);
        return inflater.inflate(R.layout.friends_fragment_model_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = ViewModelProviders.of(this).get(FriendsFragmentModelFragment.class);
        // TODO: Use the ViewModel
    }

}
