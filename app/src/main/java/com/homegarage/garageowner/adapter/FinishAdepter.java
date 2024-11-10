package com.homegarage.garageowner.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.homegarage.garageowner.ui.FirebaseUtil;
import com.homegarage.garageowner.R;
import com.homegarage.garageowner.model.Opreation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FinishAdepter extends RecyclerView.Adapter<FinishAdepter.ViewHolder> {
    ArrayList<Opreation> opreations;
    Query query;
    DatabaseReference reference;
    final String TAG="opreation state";
    public FinishAdepter() {
        opreations= FirebaseUtil.payOpreations;
        reference=FirebaseUtil.referenceOperattion;
        query=reference.orderByChild("to").equalTo(FirebaseUtil.mFirebaseAuthl.getUid());
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    opreations.clear();
                    for(DataSnapshot item : snapshot.getChildren())
                    {
                        Opreation opreation=item.getValue(Opreation.class);
                        if(opreation.getState().equals("3")&&opreation.getType().equals("5"))
                        {
                            opreations.add(opreation);
                            notifyItemChanged(opreations.size()-1);
                            Log.i("mmmm",opreations.size()+"");
                        }
                        notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        Log.d(TAG,opreations.size()+"f");
    }

    @NonNull
    @Override
    public FinishAdepter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.finished_itemview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FinishAdepter.ViewHolder holder, int position) {
        holder.bind(opreations.get(position));
    }

    @Override
    public int getItemCount() {
        return opreations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,dateFinish,dateStart,price,timeStart,timeFinish;
        CircleImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.text_name_car_owner3);
            dateFinish=itemView.findViewById(R.id.dateFinish);
            dateStart=itemView.findViewById(R.id.dateStart);
            price=itemView.findViewById(R.id.cost);
            timeStart=itemView.findViewById(R.id.time3);
            timeFinish=itemView.findViewById(R.id.timeEnd);
            imageView=itemView.findViewById(R.id.circleImageView3);
        }
        public void bind(Opreation opreation)
        {
            name.setText(opreation.getFromName());

            StringBuilder builder=new StringBuilder();
            builder.append(opreation.getDate());

            dateStart.setText(builder.substring(0,10));
            timeStart.setText(builder.substring(11,16)+" "+builder.substring(builder.length()-2));

            builder.delete(0,builder.length());
            builder.append(opreation.getDataEnd());

            dateFinish.setText(builder.substring(0,10));
            timeFinish.setText(builder.substring(11,16)+" "+builder.substring(builder.length()-2));

            price.setText(String.format("%.2f",opreation.getPrice())+" EL");

            FirebaseUtil.referenceCar.child(opreation.getFrom()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String url=snapshot.child("imageUrl").getValue(String.class);
                            Picasso.get().load(url).placeholder(R.drawable.profile_icon).into(imageView);
                        }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
