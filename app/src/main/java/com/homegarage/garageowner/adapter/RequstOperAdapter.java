package com.homegarage.garageowner.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.homegarage.garageowner.ui.FirebaseUtil;
import com.homegarage.garageowner.R;
import com.homegarage.garageowner.model.Opreation;
import com.homegarage.garageowner.notifcation.NotificationActivity;
import com.homegarage.garageowner.service.FcmNotificationsSender;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequstOperAdapter extends RecyclerView.Adapter<RequstOperAdapter.RequstViewHolder> {

    final String TAG="opreation state";
   public ArrayList <Opreation> opreationslist;
    DatabaseReference reference;
    Query query;
    public RequstOperAdapter() {
        opreationslist = FirebaseUtil.reqstOperaionList;
        reference = FirebaseUtil.referenceOperattion;
        query = reference.orderByChild("to").equalTo(FirebaseUtil.mFirebaseAuthl.getUid());
        query.addChildEventListener(new ChildEventListener() {
            Opreation opreation;
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                opreationslist.clear();
                opreation = snapshot.getValue(Opreation.class);
                assert opreation != null;
                if(opreation.getState().equals("1") && opreation.getType().equals("1") ) {
                        opreationslist.add(opreation);
                        notifyItemChanged(opreationslist.size()-1);
                    }
                    notifyDataSetChanged();
            }
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                notifyDataSetChanged(); }
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { notifyDataSetChanged(); }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { notifyDataSetChanged(); }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        Log.d(TAG,opreationslist.size()+"R");
    }

    @NonNull
    @Override
    public RequstViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.opreation_wait_row,parent,false);
        return  new RequstViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull RequstViewHolder holder, int position) {
        holder.BulidUI(opreationslist.get(position));
    }

    @Override
    public int getItemCount() {
        return opreationslist.size();
    }

    public class RequstViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        SimpleDateFormat formatterLong =new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa" , new Locale("en"));
        TextView nameCar , dateOper ,time;
        Button btnAccpet , btnRefusal;
        CircleImageView imageView;
        DatabaseReference reference;
        public RequstViewHolder(@NonNull View itemView) {
            super(itemView);
            nameCar = itemView.findViewById(R.id.text_name_car_owner2);
            dateOper = itemView.findViewById(R.id.dateCalender2);
            time=itemView.findViewById(R.id.time2);
            btnAccpet = itemView.findViewById(R.id.btn_accpet_requst2);
            btnRefusal = itemView.findViewById(R.id.btn_reusal_req2);
            imageView=itemView.findViewById(R.id.circleImageView2);
            itemView.setOnClickListener(this);
            reference=FirebaseUtil.referenceCar;
        }

        @SuppressLint("NotifyDataSetChanged")
        public void BulidUI(Opreation opreation){
            StringBuilder dateTime=new StringBuilder(opreation.getDate());
            nameCar.setText(opreation.getFromName());
            dateOper.setText(dateTime.substring(0,10));
            time.setText(dateTime.substring(11,16)+" "+dateTime.substring(dateTime.length()-2));
            reference.child(opreation.getFrom()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String url=snapshot.child("imageUrl").getValue(String.class);

                    Picasso.get().load(url).placeholder(R.drawable.profile_icon).into(imageView);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            btnAccpet.setOnClickListener(v -> {
                Date date = new Date(System.currentTimeMillis());
                String dateOpreation = formatterLong.format(date);
                opreation.setState("2");
                opreation.setType("2");
                opreation.setDate(dateOpreation);
                FirebaseUtil.referenceOperattion.child(opreation.getId()).setValue(opreation);
                btnRefusal.setEnabled(false);
                btnAccpet.setEnabled(false);
                new Handler().postDelayed(() -> {
                    opreationslist.remove(opreation);
                    notifyDataSetChanged();
                    btnRefusal.setEnabled(true);
                    btnAccpet.setEnabled(true);

                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                            opreation.getFrom()
                            ,"Accpet"
                            ,"Accpet Reservion from Garage " + opreation.getToName()
                            ,opreation.getId(), itemView.getContext());
                    notificationsSender.SendNotifications();

                }, 2000);

                Toast.makeText(itemView.getContext(), FirebaseUtil.typeList.get(Integer.parseInt(opreation.getType())-1), Toast.LENGTH_SHORT).show();
            });
            btnRefusal.setOnClickListener(v -> {

                Date date = new Date(System.currentTimeMillis());
                String dateOpreation = formatterLong.format(date);
                opreation.setState("3");
                opreation.setType("3");
                opreation.setDate(dateOpreation);
                reference.child(opreation.getId()).setValue(opreation);
                btnRefusal.setEnabled(false);
                btnAccpet.setEnabled(false);

                new Handler().postDelayed(() -> {
                   opreationslist.remove(opreation);
                    notifyDataSetChanged();
                    btnRefusal.setEnabled(true);
                    btnAccpet.setEnabled(true);

                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                            opreation.getFrom()
                            ,"Refusal"
                            ,"sorry , Reservion cancel from Garage " + opreation.getToName()
                            ,opreation.getId(), itemView.getContext());
                    notificationsSender.SendNotifications();

                }, 2000);

                Toast.makeText(itemView.getContext(), FirebaseUtil.typeList.get(Integer.parseInt(opreation.getType())-1), Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), NotificationActivity.class);
            intent.putExtra("modelOper" , opreationslist.get(getAdapterPosition()));
            v.getContext().startActivity(intent);
        }
    }

}
