package com.homegarage.garageowner.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.homegarage.garageowner.R;
import com.homegarage.garageowner.adapter.FinishAdepter;
import com.homegarage.garageowner.databinding.FragmentFinishBinding;


public class Finish extends Fragment {
    FragmentFinishBinding binding;

    public Finish() { }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentFinishBinding.inflate(getLayoutInflater());
        binding.finishedRV.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        binding.finishedRV.setAdapter(new FinishAdepter());

        return binding.getRoot();
    }
}