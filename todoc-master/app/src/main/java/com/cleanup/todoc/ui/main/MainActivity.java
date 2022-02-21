package com.cleanup.todoc.ui.main;

import static com.cleanup.todoc.ui.main.MainViewModel.*;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cleanup.todoc.R;
import com.cleanup.todoc.ui.ViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MainViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.list_tasks);
        TaskAdapter adapter = new TaskAdapter(new TaskAdapter.DeleteTaskListener() {
            @Override
            public void onDeleteItemClicked(long id) {
                viewModel.deleteTask(id);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        TextView noTasksImage = findViewById(R.id.lbl_no_task);
        FloatingActionButton floatingActionButton = findViewById(R.id.fab_add_task);

        viewModel.getViewStateLiveData().observe(this, new Observer<List<TaskViewState>>() {
            @Override
            public void onChanged(List<TaskViewState> taskViewStates) {
                if (taskViewStates.isEmpty()) {
                    noTasksImage.setVisibility(View.VISIBLE);
                } else {
                    noTasksImage.setVisibility(View.GONE);
                }
                adapter.submitList(taskViewStates);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.addRandomTask();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.filter_alphabetical:
               viewModel.onSortMethodChanged(SortMethod.ALPHABETICAL);
                break;
            case R.id.filter_alphabetical_inverted:
               viewModel.onSortMethodChanged(SortMethod.ALPHABETICAL_INVERTED);
                break;
            case R.id.filter_oldest_first:
               viewModel.onSortMethodChanged(SortMethod.OLD_FIRST);
                break;
            case R.id.filter_recent_first:
               viewModel.onSortMethodChanged(SortMethod.RECENT_FIRST);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

//    private void onPositiveButtonClick(DialogInterface dialogInterface) {
//
//        if (dialogEditText != null && dialogSpinner != null) {
//            String taskName = dialogEditText.getText().toString();
//
//            Project project = null;
//            long projectId = 0;
//            if (dialogSpinner.getSelectedItem() instanceof Project) {
//                project = (Project) dialogSpinner.getSelectedItem();
//                projectId = project.getId();
//            }
//
//            if (taskName.trim().isEmpty()) {
//                dialogEditText.setError(getString(R.string.empty_task_name));
//            } else if (project != null) {
//                long timeStamp = (long) (Math.random() * 50000);
//
//                viewModel.onAddTaskButtonClick(projectId, taskName, timeStamp);
//
//                dialogInterface.dismiss();
//            } else {
//                dialogInterface.dismiss();
//            }
//        } else {
//            dialogInterface.dismiss();
//        }
//    }

//    private void showAddTaskDialog() {
//        final AlertDialog dialog = getAddTaskDialog();
//
//        dialog.show();
//
//        dialogEditText = dialog.findViewById(R.id.txt_task_name);
//        dialogSpinner = dialog.findViewById(R.id.project_spinner);
//
//        populateDialogSpinner();
//    }

//    private void updateTasks(List<Task> tasks) {
//        if (viewModel.getAllTask() == null) {
//            noTasksImage.setVisibility(View.VISIBLE);
//            recyclerView.setVisibility(View.GONE);
//        } else {
//            noTasksImage.setVisibility(View.GONE);
//            recyclerView.setVisibility(View.VISIBLE);
//            switch (sortMethod) {
//                case ALPHABETICAL:
//                    Collections.sort(this.tasks, new Task.TaskAZComparator());
//                    break;
//                case ALPHABETICAL_INVERTED:
//                    Collections.sort(this.tasks, new Task.TaskZAComparator());
//                    break;
//                case RECENT_FIRST:
//                    Collections.sort(this.tasks, new Task.TaskRecentComparator());
//                    break;
//                case OLD_FIRST:
//                    Collections.sort(this.tasks, new Task.TaskOldComparator());
//                    break;
//            }
//        }
//    }

//    @NonNull
//    private AlertDialog getAddTaskDialog() {
//        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.Dialog);
//
//        alertBuilder.setTitle(R.string.add_task);
//        alertBuilder.setView(R.layout.dialog_add_task);
//        alertBuilder.setPositiveButton(R.string.add, null);
//        alertBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                dialogEditText = null;
//                dialogSpinner = null;
//                dialog = null;
//            }
//        });
//
//        dialog = alertBuilder.create();
//        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
//
//            @Override
//            public void onShow(DialogInterface dialogInterface) {
//
//                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                button.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View view) {
//                        onPositiveButtonClick(dialog);
//                    }
//                });
//            }
//        });
//
//        return dialog;
//    }
//
//    private void populateDialogSpinner() {
//        final ArrayAdapter<Project> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allProjects);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        if (dialogSpinner != null) {
//            dialogSpinner.setAdapter(adapter);
//        }
//    }

}
