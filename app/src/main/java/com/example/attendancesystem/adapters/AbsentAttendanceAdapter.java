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

public class AbsentAttendanceAdapter extends RecyclerView.Adapter<AbsentAttendanceAdapter.AttendanceViewHolder> {

    private Context context;
    private ArrayList<String> absentAttendanceArrayList;

    public AbsentAttendanceAdapter(Context context, ArrayList<String> absentAttendanceArrayList) {
        this.context = context;
        this.absentAttendanceArrayList = absentAttendanceArrayList;
    }

    @NonNull
    @Override
    public AbsentAttendanceAdapter.AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AttendanceViewHolder(LayoutInflater.from(context).inflate(R.layout.absent_attendance_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {

        holder.subject.setText(absentAttendanceArrayList.get(position));
    }


    @Override
    public int getItemCount() {
        return absentAttendanceArrayList.size();
    }

    class AttendanceViewHolder extends RecyclerView.ViewHolder {

        TextView subject;

        AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);

            subject = itemView.findViewById(R.id.absent_subject_textView);
        }
    }
}
