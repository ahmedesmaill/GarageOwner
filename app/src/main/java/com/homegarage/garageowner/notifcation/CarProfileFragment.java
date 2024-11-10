package com.homegarage.garageowner.notifcation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.homegarage.garageowner.ui.FirebaseUtil;
import com.homegarage.garageowner.R;
import com.homegarage.garageowner.databinding.FragmentCarProfileBinding;
import com.homegarage.garageowner.home.HomeFragment;
import com.homegarage.garageowner.model.CarInfo;
import com.homegarage.garageowner.model.InfoUserGarageModel;
import com.homegarage.garageowner.model.Opreation;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CarProfileFragment extends Fragment {

    FragmentCarProfileBinding binding;
    CarInfo carInfo;
    DatabaseReference Car,referenceOper;
    InfoUserGarageModel garageModel;
    Opreation opreation;
     boolean isActive;
    SimpleDateFormat formatterLong =new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa" , new Locale("en"));
    public CarProfileFragment(CarInfo carInfo,Opreation opreation,boolean isActive) {
        this.carInfo = carInfo;
        this.opreation=opreation;
        this.isActive=isActive;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Car = FirebaseUtil.referenceCar.child(carInfo.getId());
        referenceOper = FirebaseUtil.referenceOperattion.child(opreation.getId());
        garageModel = FirebaseUtil.userGarageInfo.get(FirebaseUtil.userGarageInfo.size()-1);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Car.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.get().load(snapshot.child("imageUrl").getValue(String.class)).placeholder(R.drawable.profile_icon).into(binding.profileImageCar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding = FragmentCarProfileBinding.inflate(getLayoutInflater());
        binding.txtEmailCar.setText(carInfo.getEmail());
        binding.txtNameCar.setText(carInfo.getName());
        binding.txtPhoneCar.setText(carInfo.getPhone());
        if(!isActive)
        {
            binding.finishBtn.setVisibility(View.GONE);
        }
        binding.finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date(System.currentTimeMillis());
                if (opreation.getDataEnd() == null) {
                    opreation.setDataEnd(formatterLong.format(date));
                }
                opreation.setPrice(-1 * calPriceExpect(garageModel.getPriceForHour(), opreation.getDate(), opreation.getDataEnd()));
                opreation.setState("3");
                opreation.setType("5");
                referenceOper.setValue(opreation);
                FirebaseUtil.activeOpreations.remove(opreation);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainerView2 , new HomeFragment()).addToBackStack(null).commit();
            }
        });

        binding.depositBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogDespost dialog=new DialogDespost(carInfo);
                dialog.show(getParentFragmentManager(),"deposit");
            }
        });

        binding.cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent call_intent = new Intent(Intent.ACTION_DIAL);
                    call_intent.setData(Uri.parse("tel:"+carInfo.getPhone()));
                    startActivity(call_intent);
            }
        });
        binding.cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"+carInfo.getEmail()));
                startActivity(intent);
            }
        });
        return binding.getRoot();
    }
    private float calPriceExpect(Float f , String s_time , String e_time){
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = formatterLong.parse(s_time);
            d2 = formatterLong.parse(e_time);
        } catch (ParseException e) { e.printStackTrace(); }
        Long diff = d2.getTime() - d1.getTime();
        Long diffMinets = diff / (60 * 1000) ;
        float total =   diffMinets * f / 60;
        if(total<10) return 10;
        else  return total ;
    }

}