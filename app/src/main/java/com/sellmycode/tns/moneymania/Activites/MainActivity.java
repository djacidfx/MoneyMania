package com.sellmycode.tns.moneymania.Activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.iammert.library.readablebottombar.ReadableBottomBar;
import com.sellmycode.tns.moneymania.Fragements.HomeFragment;
import com.sellmycode.tns.moneymania.Fragements.LeaderBoardFragment;
import com.sellmycode.tns.moneymania.Fragements.ProfileFragment;
import com.sellmycode.tns.moneymania.R;
import com.sellmycode.tns.moneymania.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.Container,new HomeFragment());
        transaction.commit();

        binding.readableBottomBar.setOnItemSelectListener(new ReadableBottomBar.ItemSelectListener() {
            @Override
            public void onItemSelected(int i) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (i){
                    case 0:
                        transaction.replace(R.id.Container, new HomeFragment());
                        break;

                    case 1:
                        transaction.replace(R.id.Container,new LeaderBoardFragment());
                        break;

                    case 2:
                        transaction.replace(R.id.Container,new ProfileFragment());
                        break;
                }
                transaction.commit();
            }
        });
    }
}