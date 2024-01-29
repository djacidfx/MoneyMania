package com.sellmycode.tns.moneymania.Activites;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sellmycode.tns.moneymania.AdMobAds.Admob;
import com.sellmycode.tns.moneymania.AdMobAds.Adsunit;
import com.sellmycode.tns.moneymania.Models.UserModel;
import com.sellmycode.tns.moneymania.R;
import com.sellmycode.tns.moneymania.databinding.ActivityWatchRewardBinding;

public class WatchRewardActivity extends AppCompatActivity {
    ActivityWatchRewardBinding binding;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWatchRewardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        resetTimer();
        loadScratch();
        loadSpins();
        loadCoins();
        binding.btnMoreScratch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRewarded(true);
                firestore.collection("users").document(auth.getUid())
                        .update("scratch", FieldValue.increment(1));
                loadScratch();
            }
        });
        binding.btnMoreSpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRewarded(true);
                firestore.collection("users").document(auth.getUid())
                        .update("spins", FieldValue.increment(1));
                loadSpins();
            }
        });
        binding.back3.setOnClickListener(new View.OnClickListener() {
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
                            binding.totalCoins3.setText(model.getCoins()+"");

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
                            binding.scratchNum.setText(model.getScratch()+"");

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
                            binding.spinNum.setText(model.getSpins()+"");

                        }
                    }
                });
    }
    void showRewarded(boolean isReload){

        if (Adsunit.mRewardedAd != null) {

            Adsunit.mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdClicked() {
                    // Called when a click is recorded for an ad.
                    Log.d(TAG, "Ad was clicked.");
                }
                @Override
                public void onAdDismissedFullScreenContent() {

                    if (isReload){

                        Adsunit.mRewardedAd = null;
                        Admob.loadVideoRewarded(WatchRewardActivity.this);
                    }
                    loadCoins();
                    loadSpins();
                    loadScratch();

                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    // Called when ad fails to show.
                    Log.e(TAG, "Ad failed to show fullscreen content.");
                    Adsunit.mRewardedAd = null;
                }

                @Override
                public void onAdImpression() {
                    // Called when an impression is recorded for an ad.
                    Log.d(TAG, "Ad recorded an impression.");
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    Log.d(TAG, "Ad showed fullscreen content.");
                }
            });


            Adsunit.mRewardedAd.show(WatchRewardActivity.this, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    binding.timer2.setVisibility(View.VISIBLE);
                    timer.start();

                }


            });

        }

    }

    public void resetTimer(){
        timer = new CountDownTimer(12000,1000) {
            @Override
            public void onTick(long l) {
                binding.timer2.setText(String.valueOf(l/1000));
               binding.btnMoreSpin.setEnabled(false);
               binding.btnMoreScratch.setEnabled(false);
            }

            @Override
            public void onFinish() {
                binding.btnMoreSpin.setEnabled(true);
                binding.btnMoreScratch.setEnabled(true);
                timer.cancel();
                binding.timer2.setVisibility(View.GONE);

            }
        };
    }
    @Override
    public void onBackPressed() {
        Admob.showInterstitialAd(WatchRewardActivity.this,true);
        super.onBackPressed();
    }
}