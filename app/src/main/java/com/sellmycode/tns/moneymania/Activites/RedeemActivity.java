package com.sellmycode.tns.moneymania.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sellmycode.tns.moneymania.Adapters.PaypalRedeemAdapter;
import com.sellmycode.tns.moneymania.Adapters.RedeemAdapter;
import com.sellmycode.tns.moneymania.Models.PaymentRequestModel;
import com.sellmycode.tns.moneymania.Models.RedeemModel;
import com.sellmycode.tns.moneymania.Models.UserModel;
import com.sellmycode.tns.moneymania.R;
import com.sellmycode.tns.moneymania.databinding.ActivityRedeemBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class RedeemActivity extends AppCompatActivity {

    ActivityRedeemBinding binding;
    ArrayList<RedeemModel> list;
    ArrayList<RedeemModel> paypalList;
    RedeemAdapter adapter;
    PaypalRedeemAdapter paypalRedeemAdapter;
    Dialog dialog;
    Dialog dialog2;
    AppCompatButton cancelBtn,redeemBtn;
    ImageView paymentMethodLog;
    TextView paymentMethod;
    EditText edtAmount,edtNumber;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    int availableCoin;
    //int requestCoin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRedeemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getData();
        binding.back6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        dialog = new Dialog(this);
        dialog2 = new Dialog(this);
        dialog2.setContentView(R.layout.loding_dialog);
        dialog.setContentView(R.layout.esewa_payment_dialog);
        if (dialog.getWindow() !=null){

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);
        }



        cancelBtn = dialog.findViewById(R.id.cancelBtn);
        redeemBtn = dialog.findViewById(R.id.redeemAmountBtn);
        edtAmount = dialog.findViewById(R.id.edtAmount);
        edtNumber = dialog.findViewById(R.id.tranNumber);
        paymentMethodLog = dialog.findViewById(R.id.trLogo);
        paymentMethod = dialog.findViewById(R.id.payMethods);

        list = new ArrayList<>();

        list.add(new RedeemModel(5000,R.drawable.payt));
        list.add(new RedeemModel(15000,R.drawable.payt));
        list.add(new RedeemModel(25000,R.drawable.payt));
        list.add(new RedeemModel(40000,R.drawable.payt));
        list.add(new RedeemModel(45000,R.drawable.payt));
        list.add(new RedeemModel(50000,R.drawable.payt));


        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        binding.recyPaytm.setLayoutManager(layoutManager1);

        adapter = new RedeemAdapter(this, list, new RedeemAdapter.AddListener() {
            @Override
            public void onLongClick(int position) {

                if (position==0){

                    checkCoinPaytm("please collect more coins",5000);

                } else if (position==1) {

                    checkCoinPaytm("please collect more coins",15000);

                }
                else if (position==2) {

                    checkCoinPaytm("please collect more coins",25000);

                }
                else if (position==3) {

                    checkCoinPaytm("please collect more coins",40000);

                }
                else if (position==4) {

                    checkCoinPaytm("please collect more coins",45000);

                }
                else if (position==5) {

                    checkCoinPaytm("please collect more coins",50000);

                }

            }
        });
        binding.recyPaytm.setAdapter(adapter);

        paypalList = new ArrayList<>();


        paypalList.add(new RedeemModel(5000,R.drawable.upi));
        paypalList.add(new RedeemModel(15000,R.drawable.upi));
        paypalList.add(new RedeemModel(20000,R.drawable.upi));
        paypalList.add(new RedeemModel(30000,R.drawable.upi));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        binding.recyPaypal.setLayoutManager(layoutManager);

        paypalRedeemAdapter = new PaypalRedeemAdapter(this, paypalList, new PaypalRedeemAdapter.AddListener() {
            @Override
            public void onLongClick(int position) {

                if (position==0){

                  checkCoin("please collect more coins",5000);

                } else if (position==1) {

                    checkCoin("please collect more coins",15000);

                }
                else if (position==2) {

                    checkCoin("please collect more coins",20000);

                }
                else if (position==3) {

                    checkCoin("please collect more coins",30000);

                }

            }
        });
        binding.recyPaypal.setAdapter(paypalRedeemAdapter);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        redeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String withdrawalMethod = paymentMethod.getText().toString();
                int requestCoin = Integer.parseInt(edtAmount.getText().toString());
                if (requestCoin>=5000){
                    dialog2.show();
                    uploadRedeemReq(withdrawalMethod,requestCoin);
                }
                else {
                    Toast.makeText(RedeemActivity.this, "Please enter a minimum of 5000", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void getData() {

        firestore.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        UserModel model = documentSnapshot.toObject(UserModel.class);
                        if (documentSnapshot.exists()){

                            availableCoin = model.getCoins();
                            binding.totalCoi.setText(model.getCoins()+"");
                            binding.avilableCoins.setText(model.getCoins()+"");
                            availableCoin = model.getCoins();

                        }

                    }
                });

    }

    private void uploadRedeemReq(String withdrawalMethod, int requestCoin) {

        int withdCoin = Integer.parseInt(edtAmount.getText().toString());
        String mobNumber = edtNumber.getText().toString();

        Calendar calForDate= Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("dd-MM-yy");
        String date= currentDate.format(calForDate.getTime());

        PaymentRequestModel model = new PaymentRequestModel(
                withdrawalMethod,
                mobNumber,
                "false",
                date,
                withdCoin
        );

        

        firestore.collection("redeem").document(FirebaseAuth.getInstance().getUid()).collection("request").document()
                .set(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                    upDateCoin(requestCoin);

                }
            }
        });



    }

    private void upDateCoin(int requestCoin) {

        firestore
                .collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .update("coins", FieldValue.increment(-requestCoin)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        firestore
                                .collection("users")
                                .document(FirebaseAuth.getInstance().getUid())
                                .update("redeemStatus","true").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Toast.makeText(RedeemActivity.this, "transaction completed, Please check Transaction History", Toast.LENGTH_SHORT).show();
                                        getData();
                                        dialog2.dismiss();
                                        dialog.dismiss();
                                    }
                                });
                    }
                });



    }

    public void checkCoin(String check,int coin){

        if (availableCoin>=coin){
            paymentMethodLog.setImageResource(R.drawable.paypal);
            paymentMethod.setText("UPI");
            edtNumber.setHint("Enter your upi ID");
            dialog.show();
        }
        else {
            Toast.makeText(RedeemActivity.this, check, Toast.LENGTH_SHORT).show();
        }

    }

    public void checkCoinPaytm(String check,int coin){

        if (availableCoin>=coin){

            paymentMethodLog.setImageResource(R.drawable.payt);
            paymentMethod.setText("Paytm");
            edtNumber.setHint("Enter paytm number");
            dialog.show();
        }
        else {
            Toast.makeText(RedeemActivity.this, check, Toast.LENGTH_SHORT).show();
        }

    }

}