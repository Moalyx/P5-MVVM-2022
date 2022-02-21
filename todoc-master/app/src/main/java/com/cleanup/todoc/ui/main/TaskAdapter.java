package com.cleanup.todoc.ui.main;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.cleanup.todoc.R;

public class TaskAdapter extends ListAdapter<TaskViewState, TaskAdapter.TaskViewHolder> {

    private final DeleteTaskListener listener;


    public TaskAdapter(DeleteTaskListener listener) {
        super(new TaskDiffCallBack());
        this.listener = listener;

    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bind(getItem(position), listener);

    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        private final AppCompatImageView imgProject;
        private final TextView lblTaskName;
        private final TextView lblProjectName;
        private final AppCompatImageView imgDelete;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProject = itemView.findViewById(R.id.img_project);
            lblTaskName = itemView.findViewById(R.id.lbl_task_name);
            lblProjectName = itemView.findViewById(R.id.lbl_project_name);
            imgDelete = itemView.findViewById(R.id.img_delete);
        }

        public void bind(TaskViewState taskViewState, DeleteTaskListener listener) {
            imgDelete.setOnClickListener(view -> listener.onDeleteItemClicked(taskViewState.getId()));

            lblTaskName.setText(taskViewState.getName());
            imgDelete.setTag(taskViewState);

            imgProject.setSupportImageTintList(ColorStateList.valueOf(taskViewState.getColorProject()));
            lblProjectName.setText(taskViewState.getProjectName());
        }
    }

    public static class TaskDiffCallBack extends DiffUtil.ItemCallback<TaskViewState> {

        @Override
        public boolean areItemsTheSame(@NonNull TaskViewState oldItem, @NonNull TaskViewState newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull TaskViewState oldItem, @NonNull TaskViewState newItem) {
            return oldItem.equals(newItem);
        }
    }

    public interface DeleteTaskListener {

        void onDeleteItemClicked(long id);

    }

}
