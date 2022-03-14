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
import com.cleanup.todoc.ui.create_task.CreateTaskDialogFragment;
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
                openDialog();
            }
        });
    }

    public void openDialog(){
        CreateTaskDialogFragment fragment = new CreateTaskDialogFragment();
        fragment.show(getSupportFragmentManager(), null);
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

}
