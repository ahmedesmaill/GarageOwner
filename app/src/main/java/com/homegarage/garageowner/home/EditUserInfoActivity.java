package com.homegarage.garageowner.home;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.homegarage.garageowner.R;
import com.homegarage.garageowner.Sign.ChangePassword;
import com.homegarage.garageowner.model.InfoUserGarageModel;
import com.homegarage.garageowner.ui.FirebaseUtil;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditUserInfoActivity extends AppCompatActivity {
    CircleImageView profileImg;
    ImageView addIMG,editProfile;
    TextInputLayout nameAR,nameEn,price;
    LinearLayout editInfo,editPass;
    DatabaseReference garageRef;
    InfoUserGarageModel garageModel;
    Button saveBtn;
    ActivityResultLauncher<Object> launcher;
    AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        initViews();
        awesomeValidation=new AwesomeValidation(ValidationStyle.BASIC);
        addValidationForEditText();
        ActivityResultContract<Object, Uri> contract=new ActivityResultContract<Object, Uri>() {
            @NonNull
            @Override
            public Intent createIntent(@NonNull Context context, Object input) {
                return CropImage.activity()
                        .setAspectRatio(1,1)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .getIntent(getApplicationContext());
            }

            @Override
            public Uri parseResult(int resultCode, @Nullable Intent intent) {
                if(CropImage.getActivityResult(intent)!=null)
                {
                    return CropImage.getActivityResult(intent).getUri();
                }
                else
                    return null;
            }
        };
        launcher=registerForActivityResult(contract, new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if ((result!=null))
                {
                    uploadIMG(result);
                    Picasso.get().load(result).placeholder(R.drawable.profile_icon).into(profileImg);
                }
            }
        });
        addIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.launch(null);
            }
        });

        editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIMG.setVisibility(View.VISIBLE);
            }
        });
        garageRef= FirebaseUtil.mDatabaseReference.child(FirebaseUtil.mFirebaseAuthl.getUid());
        garageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                garageModel=snapshot.getValue(InfoUserGarageModel.class);
                Picasso.get().load(garageModel.getImageGarage()).placeholder(R.drawable.profile_icon).into(profileImg);
                nameAR.getEditText().setText(garageModel.getNameAr());
                nameEn.getEditText().setText(garageModel.getNameEn());
                price.getEditText().setText(garageModel.getPriceForHour()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        editPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), ChangePassword.class);
                startActivity(intent);
                finish();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(awesomeValidation.validate())
                {
                    saveingEdit();
                }
            }
        });
    }
    private void initViews()
    {
        profileImg=findViewById(R.id.profile_image_car);
        addIMG=findViewById(R.id.imageEdit2);
        editInfo=findViewById(R.id.layout_edit_info);
        nameAR=findViewById(R.id.nameAr);
        nameEn=findViewById(R.id.nameEN);
        price=findViewById(R.id.etPriceForHoure);
        editPass=findViewById(R.id.new_password);
        saveBtn=findViewById(R.id.save_edit);
    }
    private void saveingEdit()
    {
        garageRef.child("nameAr").setValue(nameAR.getEditText().getText().toString());
        garageRef.child("nameEn").setValue(nameEn.getEditText().getText().toString());
        garageRef.child("priceForHour").setValue(Float.parseFloat(price.getEditText().getText().toString()));
        garageRef.child("imageGarage").setValue(garageModel.getImageGarage());
    }
    void uploadIMG(Uri uri)
    {
        StorageReference ref= FirebaseUtil.mStorageReference.child(UUID.randomUUID().toString());
        ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        garageModel.setImageGarage(uri.toString());
                    }
                });
            }
        });
    }
    private void addValidationForEditText() {
        awesomeValidation.addValidation(price.getEditText(), RegexTemplate.NOT_EMPTY,getString(R.string.text_empt));
        awesomeValidation.addValidation(nameEn.getEditText(), RegexTemplate.NOT_EMPTY,getString(R.string.name_invalid));
        awesomeValidation.addValidation(nameAR.getEditText(), RegexTemplate.NOT_EMPTY,getString(R.string.name_invalid));
    }
}