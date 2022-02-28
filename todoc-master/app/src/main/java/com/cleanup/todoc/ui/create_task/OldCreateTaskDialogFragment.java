package com.cleanup.todoc.ui.create_task;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class OldCreateTaskDialogFragment extends DialogFragment {

    private AlertDialog dialog;
    private CreateTaskViewModel dialogviewModel;
    EditText dialogEditText = null;
    Spinner dialogSpinner = null;
    private List<Project> allProjects = new  ArrayList<>();

    @NonNull
    @Override
    public AlertDialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialogviewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(CreateTaskViewModel.class);
        getProjects();

        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity(), R.style.Dialog);

        alertBuilder.setTitle(R.string.add_task);
        alertBuilder.setView(R.layout.dialog_add_task);
        alertBuilder.setPositiveButton(R.string.add, null);
        alertBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dialog = null;
            }
        });

        dialog = alertBuilder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {
                //populateDialogSpinner();

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

    private void getProjects() {
//        dialogviewModel.getAllProjects().observe(this, new Observer<List<Project>>() {
//            @Override
//            public void onChanged(@Nullable List<Project> projects) {
//                //CreateTaskDialogFragment.this.updateProjects(projects);
//                allProjects =  projects;
//            }
//        });
    }

    private AlertDialog getAddTaskDialog() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity(), R.style.Dialog);

        alertBuilder.setTitle(R.string.add_task);
        alertBuilder.setView(R.layout.dialog_add_task);
        alertBuilder.setPositiveButton(R.string.add, null);
        alertBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dialog = null;
            }
        });

        dialog = alertBuilder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {
                populateDialogSpinner();
                showAddTaskDialog();

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

//    private void getProjects() {
//        dialogviewModel.getAllProjects().observe(this, new Observer<List<Project>>() {
//            @Override
//            public void onChanged(@Nullable List<Project> projects) {
//                CreateTaskDialogFragment.this.updateProjects(projects);
//            }
//        });
//    }

    private void updateProjects(List<Project> projects) {
        allProjects = projects;
    }

    private void showAddTaskDialog() {
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

    private void populateDialogSpinner() {
        Spinner dialogSpinner = dialog.findViewById(R.id.project_spinner);
        final ArrayAdapter<Project> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, allProjects );
        Log.d("TAG", "populateDialogSpinner() called" + allProjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (dialogSpinner != null) {
            dialogSpinner.setAdapter(adapter);
        }
    }

}
