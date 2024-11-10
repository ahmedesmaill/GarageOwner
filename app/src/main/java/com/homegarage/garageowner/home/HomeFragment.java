package com.homegarage.garageowner.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.homegarage.garageowner.adapter.ActiveOperAdapter;
import com.homegarage.garageowner.adapter.RequstOperAdapter;
import com.homegarage.garageowner.adapter.TabAdepter;
import com.homegarage.garageowner.databinding.FragmentHomeBinding;


public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    TabAdepter adapter;

    public HomeFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter=new TabAdepter(getParentFragmentManager());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());

        binding.tab.setupWithViewPager(binding.viewPager);
        binding.viewPager.setAdapter(adapter);


        return binding.getRoot();
    }
}