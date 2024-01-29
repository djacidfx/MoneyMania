package com.sellmycode.tns.moneymania.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sellmycode.tns.moneymania.databinding.ActivitySignUpBinding;
import com.sellmycode.tns.moneymania.Models.UserModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore firestore;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();

        dialog = new ProgressDialog(SignUpActivity.this);
        dialog.setTitle("Creating Account");

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.editEmail.getText().toString();
                String mobileNumber = binding.editMobileNumber.getText().toString();
                String name = binding.editName.getText().toString();
                String password = binding.editPassword.getText().toString();
                if (password == null) {
                    binding.editPassword.setError("Enter Password");
                } else if (name==null) {
                    binding.editName.setError("Enter Name");
                } else if (email==null) {
                    binding.editEmail.setError("Enter Email");
                } else if (mobileNumber==null) {
                    binding.editMobileNumber.setError("Enter Mobile Number");
                }else {
                    dialog.show();
                    auth.createUserWithEmailAndPassword(binding.editEmail.getText().toString(),binding.editPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    dialog.dismiss();
                                    if (task.isSuccessful()){
                                        String referCode = email.substring(0,email.lastIndexOf("@"));
                                        UserModel model = new UserModel(
                                                name,
                                                email,
                                                password,
                                                "https://firebasestorage.googleapis.com/v0/b/moneymania-80dd6.appspot.com/o/plac.png?alt=media&token=b1f3bee8-8043-470d-a402-a1bedd9d19a4",
                                                referCode,
                                                20,
                                                0,
                                                0);

                                        String id = task.getResult().getUser().getUid();
                                        Date date = Calendar.getInstance().getTime();
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy", Locale.ENGLISH);
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.setTime(date);

                                        calendar.add(Calendar.DAY_OF_MONTH,-1);
                                        Date previosDate = calendar.getTime();
                                        String dateString = dateFormat.format(previosDate);
                                        Map<String, Object> data = new HashMap<>();
                                        data.put("date", dateString);
                                        firestore.collection("Daily Check")
                                                        .document(id).set(data);


                                        firestore.collection("users").document(id).set(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                else {
                                                    Toast.makeText(SignUpActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }else {
                                        Toast.makeText(SignUpActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    binding.backToLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    });
    }
}