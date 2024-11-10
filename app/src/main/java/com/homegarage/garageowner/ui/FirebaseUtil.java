package com.homegarage.garageowner.ui;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.homegarage.garageowner.R;
import com.homegarage.garageowner.model.InfoUserGarageModel;
import com.homegarage.garageowner.model.Opreation;

import java.util.ArrayList;

public class FirebaseUtil {

    public static DatabaseReference mDatabaseReference;
    public static DatabaseReference referenceOperattion;
    public static DatabaseReference referenceCar;
    public static DatabaseReference referencePurchase;
    public static FirebaseDatabase mFirebaseDatabase;

    private static FirebaseUtil mFirebaseUtil;
    public static FirebaseAuth mFirebaseAuthl;
    public static FirebaseAuth.AuthStateListener mAuthStateListener;

    public static InfoUserGarageModel userGarageSign;
    public static StorageReference mStorageReference;
    public static FirebaseStorage mStorage;


    public static ArrayList<Opreation> reqstOperaionList;
    public static ArrayList<Opreation> activeOpreations;
    public static ArrayList<InfoUserGarageModel> userGarageInfo;

    public static ArrayList<Integer> stateList ;
    public static ArrayList<Integer> typeList ;
    public static ArrayList<Integer> paylist;
    public static ArrayList<Opreation> payOpreations;

    private FirebaseUtil(){}

    public static void openFbReference(String ref , String refOperattion ){
        if (mFirebaseUtil == null) {
            mFirebaseUtil = new FirebaseUtil();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mFirebaseAuthl = FirebaseAuth.getInstance();
            mAuthStateListener = firebaseAuth -> {
                if(firebaseAuth.getCurrentUser() != null){
                    Log.i("dsfdsfdsfsdf", "sign im");
                }else{
                    Log.i("dsfdsfdsfsdf", "sign out");
                }
            };
            connectStorage();
        }

        stateList = new ArrayList<>();
        stateList.add(R.string.waiting_requst);
        stateList.add(R.string.active_requst);
        stateList.add(R.string.finshed_requst);

        typeList = new ArrayList<>();
        typeList.add(R.string.requst_type);
        typeList.add(R.string.accpet_type);
        typeList.add(R.string.refusal_type);
        typeList.add(R.string.cancel);
        typeList.add(R.string.done);

        paylist = new ArrayList<>();
        paylist.add(R.string.pay_type);
        paylist.add(R.string.Purchase);
        paylist.add(R.string.deposit);



        userGarageSign = new InfoUserGarageModel();

        activeOpreations=new ArrayList<>();
        payOpreations=new ArrayList<>();
        reqstOperaionList = new ArrayList<>();
        userGarageInfo = new ArrayList<>();
        mDatabaseReference = mFirebaseDatabase.getReference().child(ref);
        referenceOperattion = mFirebaseDatabase.getReference().child(refOperattion);
        referenceCar = mFirebaseDatabase.getReference().child("CarInfo");
        referencePurchase = mFirebaseDatabase.getReference().child("Purchase");
    }

    public static void connectStorage(){
        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReference().child("garage_owner_pictures");

    }

    public static void attachListener() {
        mFirebaseAuthl.addAuthStateListener(mAuthStateListener);
    }

    public static void detachListener() { mFirebaseAuthl.removeAuthStateListener(mAuthStateListener); }

}
