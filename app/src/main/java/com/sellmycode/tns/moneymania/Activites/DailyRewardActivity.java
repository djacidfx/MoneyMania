package com.sellmycode.tns.moneymania.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sellmycode.tns.moneymania.Models.UserModel;
import com.sellmycode.tns.moneymania.R;
import com.sellmycode.tns.moneymania.databinding.ActivityDailyRewardBinding;
import com.sellmycode.tns.moneymania.databinding.ActivityMainBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DailyRewardActivity extends AppCompatActivity {
    ActivityDailyRewardBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDailyRewardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        firestore  = FirebaseFirestore.getInstance();
        loadCoins();
        binding.btnReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SweetAlertDialog dialog = new SweetAlertDialog(v.getContext(),SweetAlertDialog.PROGRESS_TYPE);
                dialog.setTitleText("Loading");
                dialog.setCancelable(false);
                dialog.show();

                final Date currentDate = Calendar.getInstance().getTime();
                final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy", Locale.ENGLISH);


                firestore.collection("Daily Check").document(FirebaseAuth.getInstance().getUid())
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()){
                                    String dbDateString = documentSnapshot.get("date").toString();


                                    try{
                                        assert dbDateString!=null;
                                        Date dbDate = dateFormat.parse(dbDateString);
                                        String dates = dateFormat.format(currentDate);
                                        Date date = dateFormat.parse(dates);
                                        if (date.after(dbDate) && date.compareTo(dbDate) !=0){
                                            firestore.collection("users").document(FirebaseAuth.getInstance().getUid())
                                                    .update("coins", FieldValue.increment(20));
                                            firestore.collection("users").document(FirebaseAuth.getInstance().getUid())
                                                    .update("scratch", FieldValue.increment(5));
                                            firestore.collection("users").document(FirebaseAuth.getInstance().getUid())
                                                    .update("spins", FieldValue.increment(5));

                                            Date newDate = Calendar.getInstance().getTime();
                                            String NewDateString = dateFormat.format(newDate);

                                            firestore.collection("Daily Check").document(FirebaseAuth.getInstance().getUid())
                                                    .update("date",NewDateString).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                                            dialog.setTitleText("Success");
                                                            dialog.setContentText("20 Coins Added");
                                                            dialog.setConfirmButton("Dismiss", new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                    dialog.dismissWithAnimation();
                                                                    loadCoins();
                                                                }
                                                            }).show();
                                                        }
                                                    });
                                        }

                                        else {
                                            dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                            dialog.setTitleText("Failed");
                                            dialog.setContentText("You are already rewarded, come back tomorrow");
                                            dialog.setCancelButton("Dismiss",null);
                                            dialog.show();
                                        }

                                    }
                                    catch (ParseException e){
                                        throw new RuntimeException(e);
                                    }
                                }
                                else {
                                    Toast.makeText(DailyRewardActivity.this, "data not exist", Toast.LENGTH_SHORT).show();
                                    dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                                    dialog.setTitleText("System Busy");
                                    dialog.setContentText("System is busy,please try again later");
                                    dialog.setConfirmButton("Dismiss", new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            dialog.dismissWithAnimation();
                                        }
                                    });
                                    dialog.show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DailyRewardActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                dialog.dismissWithAnimation();
                            }
                        });
            }
        });
        binding.back4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void loadCoins() {

        firestore.collection("users").document(auth.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        UserModel model = documentSnapshot.toObject(UserModel.class);

                        if (documentSnapshot.exists()){
                            binding.totalCoins4.setText(model.getCoins()+"");

                        }

                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}