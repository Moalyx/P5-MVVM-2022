package com.cleanup.todoc.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cleanup.todoc.R;
import com.cleanup.todoc.ui.viewmodel.MainViewModel;
import com.cleanup.todoc.data.entity.Project;
import com.cleanup.todoc.data.entity.Task;
import com.cleanup.todoc.ui.viewmodelfactory.ViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MainViewModel viewModel;

    SortMethod sortMethod = SortMethod.NONE;

    private List<Project> allProjects;

    @NonNull
    private final ArrayList<Task> tasks = new ArrayList<>();

    public TaskAdapter adapter;

    @Nullable
    public AlertDialog dialog = null;

    @Nullable
    private EditText dialogEditText = null;

    @Nullable
    private Spinner dialogSpinner = null;

    @NonNull
    private FloatingActionButton floatingActionButton;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private RecyclerView recyclerView;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private TextView noTasksImage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MainViewModel.class);
        initRecyclerView();
        getProjects();
        getTasks();

    }

    public void initRecyclerView() {
        viewModel.getListTaskViewstate().observe(this, new Observer<List<TaskViewState>>() {
            @Override
            public void onChanged(List<TaskViewState> taskViewStates) {
                adapter.submitList(taskViewStates);
            }
        });
        recyclerView = findViewById(R.id.list_tasks);
        noTasksImage = findViewById(R.id.lbl_no_task);
        floatingActionButton = findViewById(R.id.fab_add_task);

        adapter = new TaskAdapter(new TaskAdapter.DeleteTaskListener() {
            @Override
            public void onDeleteItemClicked(long id) {
                viewModel.deleteTask(id);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTaskDialog();
            }
        });
        //updateTasks(tasks);
    }

    private void getProjects() {
        viewModel.getAllProjects().observe(this, new Observer<List<Project>>() {
            @Override
            public void onChanged(@Nullable List<Project> projects) {
                MainActivity.this.updateProjects(projects);
            }
        });
    }

    private void getTasks(){
        viewModel.getAllTask().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                viewModel.getTasks(tasks);
            }
        });
    }

    private void updateProjects(List<Project> projects) {
        allProjects = projects;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        viewModel.sortTasks(tasks);

        if (id == R.id.filter_alphabetical) {
            sortMethod = SortMethod.ALPHABETICAL;
        } else if (id == R.id.filter_alphabetical_inverted) {
            sortMethod = SortMethod.ALPHABETICAL_INVERTED;
        } else if (id == R.id.filter_oldest_first) {
            sortMethod = SortMethod.OLD_FIRST;
        } else if (id == R.id.filter_recent_first) {
            sortMethod = SortMethod.RECENT_FIRST;
        }
        viewModel.getListTaskViewstate();

        //viewModel.getAllTask();
        return super.onOptionsItemSelected(item);
    }

    private void onPositiveButtonClick(DialogInterface dialogInterface) {

        if (dialogEditText != null && dialogSpinner != null) {
            String taskName = dialogEditText.getText().toString();

            Project project = null;
            long projectId = 0;
            if (dialogSpinner.getSelectedItem() instanceof Project) {
                project = (Project) dialogSpinner.getSelectedItem();
                projectId = project.getId();
            }

            if (taskName.trim().isEmpty()) {
                dialogEditText.setError(getString(R.string.empty_task_name));
            } else if (project != null) {
                long timeStamp = (long) (Math.random() * 50000);

                viewModel.onAddTaskButtonClick(projectId, taskName, timeStamp);

                dialogInterface.dismiss();
            } else {
                dialogInterface.dismiss();
            }
        } else {
            dialogInterface.dismiss();
        }
    }

    private void showAddTaskDialog() {
        final AlertDialog dialog = getAddTaskDialog();

        dialog.show();

        dialogEditText = dialog.findViewById(R.id.txt_task_name);
        dialogSpinner = dialog.findViewById(R.id.project_spinner);

        populateDialogSpinner();
    }

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

    @NonNull
    private AlertDialog getAddTaskDialog() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.Dialog);

        alertBuilder.setTitle(R.string.add_task);
        alertBuilder.setView(R.layout.dialog_add_task);
        alertBuilder.setPositiveButton(R.string.add, null);
        alertBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dialogEditText = null;
                dialogSpinner = null;
                dialog = null;
            }
        });

        dialog = alertBuilder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        onPositiveButtonClick(dialog);
                    }
                });
            }
        });

        return dialog;
    }

    private void populateDialogSpinner() {
        final ArrayAdapter<Project> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allProjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (dialogSpinner != null) {
            dialogSpinner.setAdapter(adapter);
        }
    }

    private enum SortMethod {
        ALPHABETICAL,
        ALPHABETICAL_INVERTED,
        RECENT_FIRST,
        OLD_FIRST,
        NONE
    }
}
