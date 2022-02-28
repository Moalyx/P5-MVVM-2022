package com.cleanup.todoc.ui.create_task;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.cleanup.todoc.R;
import com.cleanup.todoc.data.entity.Project;
import com.cleanup.todoc.ui.ViewModelFactory;

import java.util.List;

public class CreateTaskDialogFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_task_dialog_fragment, container, true);

        EditText editText = view.findViewById(R.id.create_task_et_name);
        Spinner spinnerProject = view.findViewById(R.id.create_task_spi_project);
        Button button = view.findViewById(R.id.create_task_btn);

        ArrayAdapter<ProjectViewState> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item);
        spinnerProject.setAdapter(adapter);

        CreateTaskViewModel viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(CreateTaskViewModel.class);

        viewModel.getListProjectViewState().observe(this, new Observer<List<ProjectViewState>>() {
            @Override
            public void onChanged(List<ProjectViewState> projectViewStates) {
                adapter.clear();
                adapter.addAll(projectViewStates);
            }
        });

        return view;
    }
}
