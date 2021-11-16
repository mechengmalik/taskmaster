package com.example.taskmaster;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>{
    List<Task> allTasks = new ArrayList<Task>();


    public TaskAdapter(List<Task> allTasks){
        this.allTasks = allTasks;
    }


    @NonNull
    @Override
    public TaskAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task,parent,false);
       TaskViewHolder taskViewHolder = new TaskViewHolder(view);


        return taskViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.TaskViewHolder holder, int position) {




        holder.task = allTasks.get(position);

        Button taskButton = holder.itemView.findViewById(R.id.taskFragmentButton);

        taskButton.setText(holder.task.title);

        TextView taskTitle  = holder.itemView.findViewById(R.id.titleInFregment);
        taskTitle.setText(holder.task.title);

        TextView taskBody  = holder.itemView.findViewById(R.id.bodyInFregment);
        taskBody.setText(holder.task.body);

        TextView taskState  = holder.itemView.findViewById(R.id.stateInFregment);
        taskState.setText(holder.task.state);

        String goToTaskDetail = taskTitle.getText().toString();

        taskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Task_Detail.class);
                intent.putExtra("title", goToTaskDetail);
                v.getContext().startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return allTasks.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        public Task task;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }


}