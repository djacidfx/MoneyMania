package com.sellmycode.tns.moneymania.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sellmycode.tns.moneymania.AdMobAds.Admob;
import com.sellmycode.tns.moneymania.Models.UserModel;
import com.sellmycode.tns.moneymania.R;
import com.sellmycode.tns.moneymania.databinding.ActivitySpinWheelBinding;

import java.util.Random;

public class SpinWheelActivity extends AppCompatActivity {
    ActivitySpinWheelBinding binding;
    int rotation=0,rotationSpeed=10;
    int[] stopPosition = {720, 780, 840};
    int[] winPoints = {50, 10, 20};
    int randPosition=0;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    CountDownTimer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpinWheelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        loadCoins();
        loadSpins();
        resetTimer();
        binding.spinArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int spins = Integer.parseInt(binding.spinNumber.getText().toString());
                if (spins>0){
                    randPosition = new Random().nextInt(3-0)+0;
                    startSpin();
                }
                else {
                    binding.spinWheel.setVisibility(View.GONE);
                    binding.spinArrow.setVisibility(View.GONE);
                    Toast.makeText(SpinWheelActivity.this, "You have no spins left", Toast.LENGTH_SHORT).show();
                    binding.noSpinText.setVisibility(View.VISIBLE);
                }

            }
        });
        binding.back5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void startSpin() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.spinWheel.setRotation(rotation);
                if (rotation>=300){
                    rotationSpeed=8;

                }
                if (rotation>=400){
                    rotationSpeed=6;
                }
                if (rotation>=500){
                    rotationSpeed=4;
                }
                if (rotation>=600){
                    rotationSpeed=2;
                }
                rotation = rotation+rotationSpeed;
                if (rotation>=stopPosition[randPosition]){
                    addCoins(winPoints[randPosition]);
                }
                else {
                    startSpin();
                }
            }
        },3);
    }

    private void addCoins(int points) {
        firestore.collection("users").document(auth.getUid())
                .update("coins", FieldValue.increment(points));
        firestore.collection("users").document(auth.getUid())
                .update("spins",FieldValue.increment(-1));
        Toast.makeText(this, "Coins added", Toast.LENGTH_SHORT).show();
        loadCoins();
        loadSpins();
        binding.spinTimer.setVisibility(View.VISIBLE);
        timer.start();
        Admob.showVideoRewardedAd(SpinWheelActivity.this,true);
        binding.spinAnimation.playAnimation();
        binding.spinAnimation.setVisibility(View.VISIBLE);
    }

    private void loadCoins() {

        firestore.collection("users").document(auth.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        UserModel model = documentSnapshot.toObject(UserModel.class);

                        if (documentSnapshot.exists()){
                            binding.totalCoins5.setText(model.getCoins()+"");

                        }

                    }
                });

    }
    private void loadSpins(){
        firestore.collection("users").document(auth.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserModel model = documentSnapshot.toObject(UserModel.class);
                        if (documentSnapshot.exists()){
                            binding.spinNumber.setText(model.getSpins()+"");

                        }
                    }
                });
    }
    public void resetTimer(){
        timer = new CountDownTimer(6000,1000) {
            @Override
            public void onTick(long l) {
                binding.spinTimer.setText(String.valueOf(l/1000));
                binding.spinArrow.setEnabled(false);
            }

            @Override
            public void onFinish() {
                timer.cancel();
                rotation=0;
                rotationSpeed=10;
                randPosition=0;
                binding.spinArrow.setEnabled(true);
                binding.spinTimer.setVisibility(View.GONE);
                binding.spinAnimation.pauseAnimation();
                binding.spinAnimation.setVisibility(View.GONE);
            }
        };
    }

    @Override
    public void onBackPressed() {
        Admob.showInterstitialAd(SpinWheelActivity.this,true);
        super.onBackPressed();
    }
}