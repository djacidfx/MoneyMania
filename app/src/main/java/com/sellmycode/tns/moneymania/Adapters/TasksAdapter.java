package com.sellmycode.tns.moneymania.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.sellmycode.tns.moneymania.Activites.DailyRewardActivity;
import com.sellmycode.tns.moneymania.Activites.GuessNumberActivity;
import com.sellmycode.tns.moneymania.Activites.LuckyActivity;
import com.sellmycode.tns.moneymania.Activites.ScratchCardActivity;
import com.sellmycode.tns.moneymania.Activites.SpinWheelActivity;
import com.sellmycode.tns.moneymania.Activites.WatchRewardActivity;
import com.sellmycode.tns.moneymania.Activites.WatchVideoActivity;
import com.sellmycode.tns.moneymania.Models.TasksModel;
import com.sellmycode.tns.moneymania.R;
import com.sellmycode.tns.moneymania.databinding.RvTaskDesignBinding;

import java.util.ArrayList;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.viewHolder>{

    Context context;
    ArrayList<TasksModel> list;
    FirebaseFirestore firestore;
    public TasksAdapter(Context context, ArrayList<TasksModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_task_design,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        TasksModel model = list.get(position);
        holder.binding.tasksName.setText(model.getName());
        holder.binding.tasksImage.setImageResource(model.getImage());
        firestore = FirebaseFirestore.getInstance();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position==0){
                    Intent intent = new Intent(context, DailyRewardActivity.class);
                    context.startActivity(intent);
                    
                } else if (position==1) {
                    Intent intent = new Intent(context, SpinWheelActivity.class);
                    context.startActivity(intent);
                    
                } else if (position==2) {
                    Intent intent = new Intent(context, ScratchCardActivity.class);
                    context.startActivity(intent);
                }
                else if (position==3) {
                    Intent intent = new Intent(context, GuessNumberActivity.class);
                    context.startActivity(intent);
                    
                }
                else if (position==4) {
                    Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show();
                }
                else if (position==5) {
                    Intent intent = new Intent(context, LuckyActivity.class);
                    context.startActivity(intent);
                }
                else if (position==6) {
                    Intent intent = new Intent(context, WatchVideoActivity.class);
                    context.startActivity(intent);
                }
                else if (position==7) {
                    Intent intent = new Intent(context, WatchRewardActivity.class);
                    context.startActivity(intent);
                }
                else if (position==8) {
                    Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show();
                }
                
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        RvTaskDesignBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RvTaskDesignBinding.bind(itemView);
        }
    }
}
