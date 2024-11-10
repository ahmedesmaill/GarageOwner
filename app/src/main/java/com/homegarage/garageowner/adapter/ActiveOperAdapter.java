package com.homegarage.garageowner.adapter;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ActiveOperAdapter extends RecyclerView.Adapter<ActiveOperAdapter.ViewHolder> {
    final String TAG="opreation state";
    public ArrayList<Opreation> activeOpreations=FirebaseUtil.activeOpreations;
    ActiveListenr activeListenr;


    public ActiveOperAdapter(ActiveListenr activeListenr) {
        this.activeListenr = activeListenr;
        DatabaseReference opreationsRef=FirebaseUtil.referenceOperattion;
        Query query=opreationsRef.orderByChild("to").equalTo(FirebaseUtil.mFirebaseAuthl.getUid());
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
                            Log.i("opreations in view model", activeOpreations.size() + "");
                            notifyItemChanged(activeOpreations.size() - 1);
                        }
                        notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.active_item_view,parent,false);
        return new ViewHolder(view);
    }
    public void setActiveOpreations(ArrayList<Opreation> activeOpreations) {
        this.activeOpreations = activeOpreations;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(activeOpreations.get(position));
    }

    @Override
    public int getItemCount() {
        return activeOpreations.size();

    }
  /*  void observeViewModel(ActiveViewModel viewModel)
    {
        viewModel.getActiveOpreations().observeForever(new Observer<ArrayList<Opreation>>() {
            @Override
            public void onChanged(ArrayList<Opreation> opreations) {
                if(opreations!= null)
                {
                    opreationsActive=opreations;
                }
            }
        });
    }*/

    public  interface ActiveListenr{
        void onActiveListenr(Opreation opreation);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View activeOpreation;
        ImageView img;
        TextView carOnwer,day,time ,roundTime;
        ProgressBar progressBar;
        Chronometer chronometer;
        Date start = null;
        Date end = null;
        String roundTxt  ;
        volatile boolean con;
        int countProgress , round ;
        Long diff;
        DatabaseReference referenceOpreation;
        SimpleDateFormat formatterLong =new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa" , new Locale("en"));

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            carOnwer=itemView.findViewById(R.id.text_name_car_ownerActive);
            day=itemView.findViewById(R.id.dateCalenderActive);
            progressBar=itemView.findViewById(R.id.progressBarActive);
            chronometer=itemView.findViewById(R.id.chronometerActive);
            activeOpreation = itemView.findViewById(R.id.active_opreation);
            time = itemView.findViewById(R.id.timeActive);
            img=itemView.findViewById(R.id.circleImageViewActive);
            roundTime=itemView.findViewById(R.id.round_time_txtActive);
        }

        public void  bind(Opreation opreation) {
            StringBuilder dayClock=new StringBuilder(opreation.getDate());
            carOnwer.setText(opreation.getFromName());
            day.setText(dayClock.substring(0,10));
            time.setText(dayClock.substring(11,16)+" "+dayClock.substring(dayClock.length()-2));
            referenceOpreation=FirebaseUtil.referenceCar.child(opreation.getFrom());
            referenceOpreation.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Picasso.get().load(snapshot.child("imageUrl").getValue(String.class)).placeholder(R.drawable.profile_icon).into(img);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            setProgressBar(opreation);
            activeOpreation.setOnClickListener(v -> activeListenr.onActiveListenr(opreation));
        }

        private void setProgressBar(Opreation opreation){

            roundTxt = itemView.getContext().getString(R.string.rotating)+ " : ";;
            try { start = formatterLong.parse(opreation.getDate());
            } catch (ParseException e) { e.printStackTrace(); }

            if(opreation.getDataEnd()==null) {
                diff = System.currentTimeMillis() - start.getTime();
            }else {
                try { end = formatterLong.parse(opreation.getDataEnd());
                } catch (ParseException e) { e.printStackTrace(); }
                diff = end.getTime() - start.getTime();
            }

            if(diff<0){ con = false;countProgress = (int) (-1 * diff / 5000);
            }else { con=true;countProgress = (int) (diff / 5000);
                round = (countProgress/2160) + 1;
                roundTime.setText(roundTxt + round);}

            chronometer.setBase(SystemClock.elapsedRealtime() - diff);
            if(con && (opreation.getState().equals("1") || opreation.getState().equals("2"))){
                progressBar.setMax(2160);
                chronometer.start();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(countProgress==2160){
                            progressBar.setProgress(countProgress);
                            round++;
                            roundTime.setText(roundTxt + round);
                            countProgress=0;
                            handler.postDelayed(this,5000);
                        }else if(countProgress<2160){
                            progressBar.setProgress(countProgress);
                            countProgress++;
                            handler.postDelayed(this,5000);
                        }else{ handler.removeCallbacks(this); }}
                },5000);

            }else if(con == false) {
                progressBar.setMax(countProgress);
                chronometer.start();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(countProgress==0){
                            con=true;
                            progressBar.setProgress(countProgress);
                        }
                        if(countProgress>0){
                            progressBar.setProgress(countProgress);
                            countProgress--;
                            handler.postDelayed(this,5000);
                        }else{ handler.removeCallbacks(this); }}
                },5000);
            }
        }

    }
}
