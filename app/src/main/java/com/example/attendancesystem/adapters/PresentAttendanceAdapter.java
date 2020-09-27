package com.example.attendancesystem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancesystem.R;

import java.util.ArrayList;

public class PresentAttendanceAdapter extends RecyclerView.Adapter<PresentAttendanceAdapter.AttendanceViewHolder> {

    private Context context;
    private ArrayList<String> presentAttendanceArrayList;

    public PresentAttendanceAdapter(Context context, ArrayList<String> presentAttendanceArrayList) {
        this.context = context;
        this.presentAttendanceArrayList = presentAttendanceArrayList;
    }

    @NonNull
    @Override
    public PresentAttendanceAdapter.AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AttendanceViewHolder(LayoutInflater.from(context).inflate(R.layout.present_attendance_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {

        holder.subject.setText(presentAttendanceArrayList.get(position));

    }

    @Override
    public int getItemCount() {
        return presentAttendanceArrayList.size();
    }


    class AttendanceViewHolder extends RecyclerView.ViewHolder {

        TextView subject;

        AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);

            subject = itemView.findViewById(R.id.present_subject_textView);
        }
    }
}

