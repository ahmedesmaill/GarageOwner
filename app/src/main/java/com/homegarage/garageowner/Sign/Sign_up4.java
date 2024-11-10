package com.homegarage.garageowner.Sign;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.homegarage.garageowner.databinding.FragmentSignUp4Binding;
import com.homegarage.garageowner.ui.FirebaseUtil;
import com.homegarage.garageowner.R;
import com.homegarage.garageowner.model.InfoUserGarageModel;
import com.homegarage.garageowner.ui.MainActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.UUID;

public class Sign_up4 extends Fragment {
    FragmentSignUp4Binding binding;
    InfoUserGarageModel garageModel;
    ActivityResultLauncher<Object> launcher;
    DatabaseReference reference;
    AwesomeValidation validation;
    FirebaseUser user;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reference=FirebaseUtil.mDatabaseReference;
        garageModel=FirebaseUtil.userGarageSign;
        validation=new AwesomeValidation(ValidationStyle.BASIC);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentSignUp4Binding.inflate(getLayoutInflater());

        ActivityResultContract<Object, Uri> contract=new ActivityResultContract<Object, Uri>() {
            @NonNull
            @Override
            public Intent createIntent(@NonNull Context context, Object input) {

                return CropImage.activity()
                        .setAspectRatio(1,1)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .getIntent(requireContext());
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
                }
            }
        });

        binding.addNationalIdIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launcher.launch(null);
            }
        });
        validat();
            binding.nextSign4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
            if (validation.validate()) {
                DatabaseReference databaseReference = FirebaseUtil.mDatabaseReference;
                FirebaseUser firebaseUser =FirebaseUtil.mFirebaseAuthl.getCurrentUser();
                assert firebaseUser != null;
                DatabaseReference newuser = databaseReference.child(firebaseUser.getUid());

                garageModel.setBankAcountName(binding.bankAccountName.getEditText().getText().toString());
                garageModel.setBankAcountNum(binding.bankAccountNum.getEditText().getText().toString());
                garageModel.setId(firebaseUser.getUid());
                newuser.setValue(garageModel);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            else
                Toast.makeText(getContext(), "invalid data", Toast.LENGTH_SHORT).show();
        }
            });


        return binding.getRoot();
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
                        garageModel.setNationalIDImg1(uri.toString());
                    }
                });
            }
        });
    }
    void validat(){
        validation.addValidation(binding.bankAccountName.getEditText(), RegexTemplate.NOT_EMPTY,getString(R.string.text_empt));
        validation.addValidation(binding.bankAccountNum.getEditText(),".*[0-9]{16}",getString(R.string.text_empt));

    }
}