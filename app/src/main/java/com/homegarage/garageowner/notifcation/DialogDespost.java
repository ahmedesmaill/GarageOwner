package com.homegarage.garageowner.notifcation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.homegarage.garageowner.ui.FirebaseUtil;
import com.homegarage.garageowner.R;
import com.homegarage.garageowner.model.CarInfo;
import com.homegarage.garageowner.model.InfoUserGarageModel;
import com.homegarage.garageowner.model.MoneyModel;
import com.homegarage.garageowner.model.PurchaseModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DialogDespost extends DialogFragment {

    TextView balanceTV;
    TextInputLayout balanceET;
    Button depositBTN;
    CarInfo carInfo;
    MoneyModel moneyModel;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("CarInfo");
    DatabaseReference refAppBalance = FirebaseDatabase.getInstance().getReference().child("App").child(FirebaseUtil.mFirebaseAuthl.getUid());
    DatabaseReference refPushase = FirebaseUtil.referencePurchase;
    InfoUserGarageModel garageModel;


    SimpleDateFormat formatterLong = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", new Locale("en"));


    public DialogDespost(CarInfo carInfo) {
        this.carInfo = carInfo;
        garageModel = FirebaseUtil.userGarageInfo.get(FirebaseUtil.userGarageInfo.size()-1);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog, container, false);
        initViews(view);


        balanceTV.setText(carInfo.getBalance()+" "+"E.G");

        getDepsit(new OnDepositGetCallback() {
            @Override
            public void onCarRecived(CarInfo carInfo) {
                balanceTV.setText(carInfo.getBalance()+"");
            }

            @Override
            public void OnDeositAdded(MoneyModel moneyModel) {
                depositBTN.setOnClickListener(view1 -> {
                    float depositNum = Float.parseFloat(balanceET.getEditText().getText().toString());
                    carInfo.setBalance(depositNum + carInfo.getBalance());
                    balanceTV.setText(carInfo.getBalance()+"");
                    reference.child(carInfo.getId()).child("balance").setValue(carInfo.getBalance());

                    moneyModel.setAppPercent(moneyModel.getAppPercent()+depositNum);
                    refAppBalance.child("appPercent").setValue(moneyModel.getAppPercent());
                    refAppBalance.child("totalBalance").setValue(moneyModel.getMoneyForGarage()-moneyModel.getAppPercent());

                    Date date = new Date(System.currentTimeMillis());
                    String dateOpreation = formatterLong.format(date);

                    PurchaseModel purchase = new PurchaseModel();
                    purchase.setDate(dateOpreation);
                    purchase.setType("3");
                    purchase.setFrom(carInfo.getId());
                    purchase.setTo(garageModel.getId());
                    purchase.setValue(depositNum);
                    purchase.setFromName(carInfo.getName());
                    purchase.setToName(garageModel.getNameEn());
                    purchase.setId(refPushase.push().getKey());
                    refPushase.child(purchase.getId()).setValue(purchase);

                    Toast.makeText(getContext(), "thanks", Toast.LENGTH_SHORT).show();
                    dismiss();
                });
            }
        });

        return view;
    }

    private void initViews(View view) {
        balanceTV = view.findViewById(R.id.balance);
        balanceET = view.findViewById(R.id.depositTL);
        depositBTN = view.findViewById(R.id.depoistBTN);
    }
    void getDepsit(OnDepositGetCallback callback) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CarInfo car= snapshot.child(carInfo.getId()).getValue(CarInfo.class);
                callback.onCarRecived(car);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        refAppBalance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    moneyModel = snapshot.getValue(MoneyModel.class);
                    callback.OnDeositAdded(moneyModel);
                }else  callback.OnDeositAdded(new MoneyModel(0,0,0));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }



    public interface OnDepositGetCallback {
         void onCarRecived(CarInfo carInfo);
         void OnDeositAdded(MoneyModel moneyModel);
    }
}
