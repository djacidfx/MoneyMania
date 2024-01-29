package com.sellmycode.tns.moneymania.Fragements;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.sellmycode.tns.moneymania.Activites.RedeemActivity;
import com.sellmycode.tns.moneymania.Adapters.TasksAdapter;
import com.sellmycode.tns.moneymania.Models.TasksModel;
import com.sellmycode.tns.moneymania.Models.UserModel;
import com.sellmycode.tns.moneymania.R;
import com.sellmycode.tns.moneymania.databinding.ActivityRedeemBinding;
import com.sellmycode.tns.moneymania.databinding.FragmentHomeBinding;

import java.util.ArrayList;


public class HomeFragment extends Fragment {


    FragmentHomeBinding binding;
    ArrayList<TasksModel>list;
    TasksAdapter adapter;
    FirebaseFirestore firestore;
    Dialog dialog;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        firestore = FirebaseFirestore.getInstance();


        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.loding_dialog);
        dialog.show();
        loadUserData();
        binding.swipe.setOnRefreshListener(this::loadUserData2);


        list = new ArrayList<>();

        list.add(new TasksModel("Daily Reward", R.drawable.daily));
        list.add(new TasksModel("Spin Wheel", R.drawable.spin));
        list.add(new TasksModel("Scratch Card", R.drawable.card));
        list.add(new TasksModel("Guess Number", R.drawable.guess));
        list.add(new TasksModel("Flip and Win", R.drawable.flip));
        list.add(new TasksModel("Lucky Box", R.drawable.lucky));
        list.add(new TasksModel("Watch Video", R.drawable.video));
        list.add(new TasksModel("Earn Rewards", R.drawable.rewardtol));
        list.add(new TasksModel("Refer and Earn", R.drawable.refer));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rvTasks.setLayoutManager(layoutManager);

        adapter = new TasksAdapter(getContext(), list);
        binding.rvTasks.setAdapter(adapter);
        binding.btnRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RedeemActivity.class);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }

    private void loadUserData() {

        firestore.collection("users").document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserModel model = documentSnapshot.toObject(UserModel.class);

                        if (documentSnapshot.exists()){
                            binding.userName.setText("Hello,"+model.getName());
                            binding.userCoin.setText(model.getCoins()+"");
                            binding.totalCoin.setText(model.getCoins()+"");

                            Picasso.get()
                                    .load(model.getProfile())
                                    .placeholder(R.drawable.profiletol)
                                    .into(binding.profile);

                            dialog.dismiss();

                        }
                    }
                });
    }
    private void loadUserData2() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                firestore.collection("users").document(FirebaseAuth.getInstance().getUid())
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                UserModel model = documentSnapshot.toObject(UserModel.class);

                                if (documentSnapshot.exists()){
                                    binding.userName.setText("Hello,"+model.getName());
                                    binding.userCoin.setText(model.getCoins()+"");
                                    binding.totalCoin.setText(model.getCoins()+"");

                                    Picasso.get()
                                            .load(model.getProfile())
                                            .placeholder(R.drawable.profiletol)
                                            .into(binding.profile);

                                    binding.swipe.setRefreshing(false);

                                }
                            }
                        });
            }
        },1000);


    }
}