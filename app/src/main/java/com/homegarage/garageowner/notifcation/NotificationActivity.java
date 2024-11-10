package com.homegarage.garageowner.notifcation;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.homegarage.garageowner.databinding.ActivityNotificationBinding;
import com.homegarage.garageowner.model.Opreation;
import com.homegarage.garageowner.service.MyFirebaseMessagingService;


public class NotificationActivity extends AppCompatActivity {

    public static String idOpreation = null;
    public static Opreation allOpreation = null;
    ActivityNotificationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        idOpreation = getIntent().getStringExtra(MyFirebaseMessagingService.ID_OPERATTON);
        allOpreation = (Opreation) getIntent().getSerializableExtra("modelOper");

    }
}