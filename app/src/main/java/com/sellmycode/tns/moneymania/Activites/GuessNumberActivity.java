package com.sellmycode.tns.moneymania.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.sellmycode.tns.moneymania.databinding.ActivityGuessNumberBinding;

import java.util.Random;

public class GuessNumberActivity extends AppCompatActivity {
    ActivityGuessNumberBinding binding;
    CountDownTimer timer;
    private  int givenNumber, guessNumber;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    private int currentCoins;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuessNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        resetTimer();
        loadCoins();
        Random random = new Random();
        givenNumber = random.nextInt(60);
        binding.firstCardNum.setText(givenNumber+"");
        binding.firstCardNum.setVisibility(View.VISIBLE);
        Random random2 = new Random();
        guessNumber = random2.nextInt(60);
        binding.secondCardNum.setText(guessNumber+"");

        binding.btnHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Admob.showVideoRewardedAd(GuessNumberActivity.this,true);
                binding.question.setVisibility(View.GONE);
                binding.secondCardNum.setVisibility(View.VISIBLE);
                binding.timer.setVisibility(View.VISIBLE);
                timer.start();
                if (guessNumber>givenNumber){
                    firestore.collection("users")
                            .document(auth.getUid())
                            .update("coins", FieldValue.increment(50));
                    loadCoins();
                    binding.result.setText("You win 50 coins");
                    Toast.makeText(GuessNumberActivity.this, "You win 50 coins", Toast.LENGTH_SHORT).show();
                    binding.result.setVisibility(View.VISIBLE);
                    binding.btnHigh.setEnabled(false);
                    binding.btnLow.setEnabled(false);
                    binding.btnSame.setEnabled(false);

                } else {
                    firestore.collection("users")
                            .document(auth.getUid())
                            .update("coins", FieldValue.increment(-150));
                    loadCoins();
                    binding.result.setText("You lose 150 coins");
                    Toast.makeText(GuessNumberActivity.this, "You lose 150 coins", Toast.LENGTH_SHORT).show();
                    binding.result.setVisibility(View.VISIBLE);
                    binding.btnHigh.setEnabled(false);
                    binding.btnLow.setEnabled(false);
                    binding.btnSame.setEnabled(false);
                }
            }
        });
        binding.btnLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Admob.showVideoRewardedAd(GuessNumberActivity.this,true);
                binding.question.setVisibility(View.GONE);
                binding.secondCardNum.setVisibility(View.VISIBLE);
                binding.timer.setVisibility(View.VISIBLE);
                timer.start();
                if (guessNumber<givenNumber){
                    firestore.collection("users")
                            .document(auth.getUid())
                            .update("coins", FieldValue.increment(50));
                    loadCoins();
                    binding.result.setText("You win 50 coins");
                    Toast.makeText(GuessNumberActivity.this, "You win 50 coins", Toast.LENGTH_SHORT).show();
                    binding.result.setVisibility(View.VISIBLE);
                    binding.btnHigh.setEnabled(false);
                    binding.btnLow.setEnabled(false);
                    binding.btnSame.setEnabled(false);

                } else {
                    firestore.collection("users")
                            .document(auth.getUid())
                            .update("coins", FieldValue.increment(-150));
                    loadCoins();
                    binding.result.setText("You lose 150 coins");
                    Toast.makeText(GuessNumberActivity.this, "You lose 150 coins", Toast.LENGTH_SHORT).show();
                    binding.result.setVisibility(View.VISIBLE);
                    binding.btnHigh.setEnabled(false);
                    binding.btnLow.setEnabled(false);
                    binding.btnSame.setEnabled(false);
                }
            }
        });
        binding.btnSame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Admob.showVideoRewardedAd(GuessNumberActivity.this,true);
                binding.question.setVisibility(View.GONE);
                binding.secondCardNum.setVisibility(View.VISIBLE);
                binding.timer.setVisibility(View.VISIBLE);
                timer.start();
                if (guessNumber==givenNumber){
                    firestore.collection("users")
                            .document(auth.getUid())
                            .update("coins", FieldValue.increment(100));
                    loadCoins();
                    binding.result.setText("You win 100 coins");
                    Toast.makeText(GuessNumberActivity.this, "You win 100 coins", Toast.LENGTH_SHORT).show();
                    binding.result.setVisibility(View.VISIBLE);
                    binding.btnHigh.setEnabled(false);
                    binding.btnLow.setEnabled(false);
                    binding.btnSame.setEnabled(false);

                } else {
                    firestore.collection("users")
                            .document(auth.getUid())
                            .update("coins", FieldValue.increment(-100));
                    loadCoins();
                    binding.result.setText("You lose 100 coins");
                    Toast.makeText(GuessNumberActivity.this, "You lose 100 coins", Toast.LENGTH_SHORT).show();
                    binding.result.setVisibility(View.VISIBLE);
                    binding.btnHigh.setEnabled(false);
                    binding.btnLow.setEnabled(false);
                    binding.btnSame.setEnabled(false);
                }
            }
        });
        binding.back2.setOnClickListener(new View.OnClickListener() {
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
                            binding.totalCoins2.setText(model.getCoins()+"");
                            currentCoins = model.getCoins();

                        }

                    }
                });

    }
    public void resetTimer(){
        timer = new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long l) {
                binding.timer.setText(String.valueOf(l/1000));
            }

            @Override
            public void onFinish() {
                binding.question.setVisibility(View.VISIBLE);
                binding.secondCardNum.setVisibility(View.GONE);
                timer.cancel();

                binding.timer.setVisibility(View.GONE);
                Random random = new Random();
                givenNumber = random.nextInt(60);
                binding.firstCardNum.setText(givenNumber+"");
                Random random2 = new Random();
                guessNumber = random2.nextInt(60);
                binding.secondCardNum.setText(guessNumber+"");

                binding.btnHigh.setEnabled(true);
                binding.btnLow.setEnabled(true);
                binding.btnSame.setEnabled(true);


            }
        };
    }

    @Override
    public void onBackPressed() {
        Admob.showInterstitialAd(GuessNumberActivity.this,true);
        super.onBackPressed();
    }
}