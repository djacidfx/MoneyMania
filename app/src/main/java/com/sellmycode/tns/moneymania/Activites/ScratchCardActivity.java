package com.sellmycode.tns.moneymania.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.anupkumarpanwar.scratchview.ScratchView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sellmycode.tns.moneymania.AdMobAds.Admob;
import com.sellmycode.tns.moneymania.Models.UserModel;
import com.sellmycode.tns.moneymania.R;
import com.sellmycode.tns.moneymania.databinding.ActivityScratchCardBinding;

import java.util.Random;

public class ScratchCardActivity extends AppCompatActivity {
    ActivityScratchCardBinding binding;
    CountDownTimer timer;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScratchCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        loadCoins();
        loadScratch();
        resetTimer();
        Admob.loadBannerAd(binding.bannerAd1,ScratchCardActivity.this);
        binding.scratchView.setRevealListener(new ScratchView.IRevealListener() {
            @Override
            public void onRevealed(ScratchView scratchView) {
                int spins = Integer.parseInt(binding.scratchNumber.getText().toString());
                if (spins>0){
                    Admob.showVideoRewardedAd(ScratchCardActivity.this,true);
                    Random randoms = new Random();
                    int val = randoms.nextInt(50+50)-50;
                    binding.scratchCoin.setText(val+"");
                    int scratchedCoin = Integer.parseInt(binding.scratchCoin.getText().toString());
                    firestore.collection("users").document(auth.getUid())
                            .update("coins", FieldValue.increment(scratchedCoin));
                    firestore.collection("users").document(auth.getUid())
                            .update("scratch",FieldValue.increment(-1));
                    Toast.makeText(ScratchCardActivity.this, val+" Coins Added Successfully", Toast.LENGTH_SHORT).show();
                    loadCoins();
                    loadScratch();
                    binding.scratchTimer.setVisibility(View.VISIBLE);
                    timer.start();
                    binding.winAnimation.playAnimation();
                    binding.winAnimation.setVisibility(View.VISIBLE);
                }
                else {
                    binding.ScratchVard.setVisibility(View.GONE);
                    Toast.makeText(ScratchCardActivity.this, "You have no scratch left", Toast.LENGTH_SHORT).show();
                    binding.noScratchText.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onRevealPercentChangedListener(ScratchView scratchView, float percent) {
                if (percent>=0.5){
                    Log.d("Reveal Percentage","onRevealPercentChangedListener"+String.valueOf(percent));
                }
            }
        });


        binding.back.setOnClickListener(new View.OnClickListener() {
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
                            binding.totalCoins.setText(model.getCoins()+"");

                        }

                    }
                });

    }

    private void loadScratch(){
        firestore.collection("users").document(auth.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserModel model = documentSnapshot.toObject(UserModel.class);
                        if (documentSnapshot.exists()){
                            binding.scratchNumber.setText(model.getScratch()+"");

                        }
                    }
                });
    }

    public void resetTimer(){
        timer = new CountDownTimer(6000,1000) {
            @Override
            public void onTick(long l) {
                binding.scratchTimer.setText(String.valueOf(l/1000));
            }

            @Override
            public void onFinish() {
                binding.scratchView.mask();
                timer.cancel();
                binding.scratchTimer.setVisibility(View.GONE);
                binding.winAnimation.pauseAnimation();
                binding.winAnimation.setVisibility(View.GONE);

            }
        };
    }


    @Override
    public void onBackPressed() {
        Admob.showInterstitialAd(ScratchCardActivity.this,true);
        super.onBackPressed();
    }
}