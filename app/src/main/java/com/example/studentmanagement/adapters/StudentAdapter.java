package com.example.studentmanagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.studentmanagement.R;
import com.example.studentmanagement.model.StudentModel;

import java.util.List;

public class StudentAdapter extends BaseAdapter {
    List<StudentModel> stuModels;
    Context context;
    int count = 0;

    public StudentAdapter(List<StudentModel> items, Context context){
        this.stuModels = items;
        this.context = context;
        count = 0;
    }

    @Override
    public int getCount() {
        return stuModels.size();
    }

    @Override
    public Object getItem(int position) {
        return stuModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.imageName = view.findViewById(R.id.txt_avatar);
            viewHolder.name = view.findViewById(R.id.txt_name);
            viewHolder.id = view.findViewById(R.id.txt_id);
            viewHolder.email = view.findViewById(R.id.txt_email);

            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }

        StudentModel item = stuModels.get(position);

        viewHolder.imageName.setText(item.avaName);
        viewHolder.name.setText(item.name);
        viewHolder.id.setText(String.valueOf(item.id));
        viewHolder.email.setText(item.email);

        return view;
    }
}

class ViewHolder {
    TextView imageName;
    TextView name;
    TextView id;
    TextView email;
}
