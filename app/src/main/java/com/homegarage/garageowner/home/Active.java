package com.homegarage.garageowner.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.homegarage.garageowner.ui.ActiveViewModel;
import com.homegarage.garageowner.ui.FirebaseUtil;
import com.homegarage.garageowner.R;
import com.homegarage.garageowner.adapter.ActiveOperAdapter;
import com.homegarage.garageowner.databinding.FragmentActiveBinding;
import com.homegarage.garageowner.model.CarInfo;
import com.homegarage.garageowner.model.Opreation;
import com.homegarage.garageowner.notifcation.CarProfileFragment;


public class Active extends Fragment {
    FragmentActiveBinding binding;
    ActiveOperAdapter adapter;
    CarInfo car;
    DatabaseReference reference;
    ActiveViewModel viewModel;

    public Active() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel= ViewModelProviders.of(getActivity()).get(ActiveViewModel.class);
        Log.i("opreations in active fragment",viewModel.getActiveOpreations().size()+"");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        adapter=new ActiveOperAdapter(new ActiveOperAdapter.ActiveListenr() {
            @Override
            public void onActiveListenr(Opreation opreation) {
                reference= FirebaseUtil.referenceCar;
                reference.child(opreation.getFrom()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        car =snapshot.getValue(CarInfo.class);
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragmentContainerView2 , new CarProfileFragment(car,opreation,true)).addToBackStack(null).commit();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
                binding = FragmentActiveBinding.inflate(getLayoutInflater());
        binding.recyclerActive.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        binding.recyclerActive.setAdapter(adapter);


        return binding.getRoot();
    }
}
