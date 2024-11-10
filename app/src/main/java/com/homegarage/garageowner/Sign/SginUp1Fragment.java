package com.homegarage.garageowner.Sign;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.homegarage.garageowner.ui.FirebaseUtil;
import com.homegarage.garageowner.R;
import com.homegarage.garageowner.databinding.FragmentSginUpBinding;
import com.homegarage.garageowner.model.InfoUserGarageModel;

import java.util.Objects;


public class SginUp1Fragment extends Fragment {

    private FragmentSginUpBinding binding;
    private AwesomeValidation  mAwesomeValidation;
    private InfoUserGarageModel infoUserGarageModel;

    public SginUp1Fragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAwesomeValidation = new AwesomeValidation(BASIC);
        infoUserGarageModel = FirebaseUtil.userGarageSign;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSginUpBinding.inflate(getLayoutInflater());
        binding.terms.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        addValidationForEditText();
        binding.nextSign1.setOnClickListener(v -> {
            if (mAwesomeValidation.validate()) {
                binding.scrollSign1.fullScroll(View.FOCUS_DOWN);
                binding.layoutSuccess1.setVisibility(View.VISIBLE);

                infoUserGarageModel.setEmail(binding.etEmailSign.getEditText().getText().toString().trim());
                FirebaseAuth firebaseAuth = FirebaseUtil.mFirebaseAuthl;
                firebaseAuth.createUserWithEmailAndPassword(infoUserGarageModel.getEmail(), binding.etPasswordSign.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            SignUp2Fragment newFragment = new SignUp2Fragment();
                            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragmentContainerView, newFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                        else {
                            Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                binding.layoutSuccess1.setVisibility(View.GONE);
            }
        });

        return binding.getRoot();
    }

    private void addValidationForEditText() {
        mAwesomeValidation.addValidation(binding.etPasswordSign.getEditText(), "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}",getString(R.string.invalid_password));
        mAwesomeValidation.addValidation(binding.etConfirmPassword.getEditText(), binding.etPasswordSign.getEditText(),getString(R.string.password_confirmation));
        mAwesomeValidation.addValidation(binding.etEmailSign.getEditText(), Patterns.EMAIL_ADDRESS, getString(R.string.email_valid));

    }

}