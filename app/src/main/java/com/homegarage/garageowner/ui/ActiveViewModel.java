package com.homegarage.garageowner.ui;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.homegarage.garageowner.model.Opreation;

import java.util.ArrayList;

public class ActiveViewModel extends AndroidViewModel {
    ArrayList<Opreation> activeOpreations;
    DatabaseReference opreationsRef;
    Query query;
    public ActiveViewModel(@NonNull Application application) {
        super(application);
        activeOpreations=new ArrayList<>();
        opreationsRef=FirebaseUtil.referenceOperattion;
        query=opreationsRef.orderByChild("to").equalTo(FirebaseUtil.mFirebaseAuthl.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot item : snapshot.getChildren()){
                        Opreation opreation= item.getValue(Opreation.class);
                        if ( (opreation.getState().equals("2") && opreation.getType().equals("2"))
                                || opreation.getPrice() < 0
                        ) {
                            activeOpreations.add(opreation);
                            Log.i("opreations in view model",activeOpreations.size()+"");
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
    public ArrayList<Opreation> getActiveOpreations()
    { return activeOpreations;
    }
}
