package com.example.attendancesystem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancesystem.R;
import com.example.attendancesystem.classes.Attendance;
import com.example.attendancesystem.classes.Student;

import java.util.ArrayList;

public class StudentsReportAdapter extends RecyclerView.Adapter<StudentsReportAdapter.StudentReportViewHolder> {

    private Context context;
    private ArrayList<Student> studentsList;

    public StudentsReportAdapter(Context context, ArrayList<Student> studentsList) {
        this.context = context;
        this.studentsList = studentsList;
    }

    @NonNull
    @Override
    public StudentsReportAdapter.StudentReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StudentReportViewHolder(LayoutInflater.from(context).inflate(R.layout.student_list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StudentReportViewHolder holder, int position) {

        holder.name.setText(studentsList.get(position).getsName());
        holder.rollNo.setText(studentsList.get(position).getsId());
        holder.percent.setText(studentsList.get(position).getsCurrentAttendance() + "%");
    }

    @Override
    public int getItemCount() {
        return studentsList.size();
    }

    class StudentReportViewHolder extends RecyclerView.ViewHolder {

        TextView name, rollNo, percent;

        StudentReportViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            rollNo = itemView.findViewById(R.id.roll_number);
            percent = itemView.findViewById(R.id.percentage);
        }
    }

}
