package com.cleanup.todoc.ui.create_task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cleanup.todoc.R;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {

    private final Context context;
    private List<ProjectViewState> projectViewStates;
    private final int[] colorList;
    private final String[] nameProject;

    public SpinnerAdapter(Context context, int[] colorList, String [] nameProject) {
        this.context = context;
        this.colorList = colorList;
        this.nameProject = nameProject;
    }


    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") View rootView = LayoutInflater.from(context)
                .inflate(R.layout.spinner_adapter, viewGroup, false);
        ImageView image = rootView.findViewById(R.id.colorItem);
        TextView textView = rootView.findViewById(R.id.textspinner);
        image.setImageResource(colorList[position]);
        textView.setText(nameProject[position]);
        return rootView;
    }
}
