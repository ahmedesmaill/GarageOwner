package com.homegarage.garageowner.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.homegarage.garageowner.R;
import com.homegarage.garageowner.adapter.RequstOperAdapter;
import com.homegarage.garageowner.databinding.FragmentRequestesBinding;


public class Requestes extends Fragment {
    FragmentRequestesBinding binding;

    public Requestes() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentRequestesBinding.inflate(getLayoutInflater());
        binding.recyclerRequst.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        binding.recyclerRequst.setAdapter(new RequstOperAdapter());

        return binding.getRoot();
    }
}