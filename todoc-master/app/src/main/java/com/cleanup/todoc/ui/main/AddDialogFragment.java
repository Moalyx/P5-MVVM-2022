package com.cleanup.todoc.ui.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class AddDialogFragment extends DialogFragment {

    private AlertDialog dialog;
    private DialogViewModel dialogviewModel;
    EditText dialogEditText = null;
    Spinner dialogSpinner = null;
    private List<Project> allProjects = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogviewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(DialogViewModel.class);
        getProjects();
        getAddTaskDialog();
        showAddTaskDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.dialog_add_task, container, false);
    }

    @NonNull
    private AlertDialog getAddTaskDialog() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity(), R.style.Dialog);

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

        // This instead of listener to positive button in order to avoid automatic dismiss
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(view -> {
                    AddDialogFragment.this.onPositiveButtonClick(dialog);
                });
            }
        });

        return dialog;
    }

    public void showAddTaskDialog() {
        final AlertDialog dialog = getAddTaskDialog();

        dialog.show();

        dialogEditText = dialog.findViewById(R.id.txt_task_name);
        dialogSpinner = dialog.findViewById(R.id.project_spinner);

        final ArrayAdapter<Project> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, allProjects);
        Spinner dialogSpinner = dialog.findViewById(R.id.project_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (dialogSpinner != null) {
            dialogSpinner.setAdapter(adapter);
        }

        //populateDialogSpinner();
    }

    private void onPositiveButtonClick(DialogInterface dialogInterface) {

        EditText dialogEditText = dialog.findViewById(R.id.txt_task_name);
        Spinner dialogSpinner = dialog.findViewById(R.id.project_spinner);

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
                long timeStamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

                dialogviewModel.onAddTaskButtonClick(projectId, taskName, timeStamp);

                dialogInterface.dismiss();
            } else {
                dialogInterface.dismiss();
            }
        } else {
            dialogInterface.dismiss();
        }
    }

    private void getProjects() {
        dialogviewModel.getAllProjects().observe(this, new Observer<List<Project>>() {
            @Override
            public void onChanged(@Nullable List<Project> projects) {
                AddDialogFragment.this.updateProjects(projects);
            }
        });
    }

        private void updateProjects(List<Project> projects) {
        allProjects = projects;
    }

    public void populateDialogSpinner() {
        //allProjects.add(new Project(1, "Projet Tartampion", 0xFFEADAD1));
        final ArrayAdapter<Project> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, dialogviewModel.getallTheProjects());
        Spinner dialogSpinner = dialog.findViewById(R.id.project_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (dialogSpinner != null) {
            dialogSpinner.setAdapter(adapter);
        }
    }


}
