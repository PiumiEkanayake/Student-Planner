package com.example.timetable.Exam;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetable.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CompletedExamRecyclerView extends RecyclerView.Adapter<CompletedExamRecyclerView.ViewHolder>{

    private static final String TAG = "Exam Recycler View";
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> subject = new ArrayList<>();
    private ArrayList<String> due_date = new ArrayList<>();
    private ArrayList<Integer> color = new ArrayList<>();
    private ArrayList<Integer> ids = new ArrayList<>();

    private Context mContext;

    public CompletedExamRecyclerView(ArrayList<Integer> Id,ArrayList<String> title, ArrayList<String> Sub,ArrayList<Integer>color ,ArrayList<String> due_date, Context mContext) {
        this.ids = Id;
        this.titles = title;
        this.subject = Sub;
        this.color = color;
        this.due_date= due_date;

        this.mContext = mContext;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView subject;
        TextView date;

        FrameLayout examLayout;
        RelativeLayout examCard, viewBackground;

        public ViewHolder(View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.examName);
            subject = itemView.findViewById(R.id.sub);
            date = itemView.findViewById(R.id.examDate);
            examLayout = (FrameLayout)itemView.findViewById(R.id.Examlayout);
            examCard = (RelativeLayout) itemView.findViewById(R.id.examCard);
            viewBackground = (RelativeLayout) itemView.findViewById(R.id.view_background);
        }
    }
    @NonNull
    @Override
    public CompletedExamRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.completed_exam_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.title.setText(titles.get(position));
        holder.date.setText(due_date.get(position));
        holder.subject.setText(subject.get(position));
//        holder.homeworkCard.setBackgroundTintList(Color.valueOf(colors.get(position)));
        holder.examCard.setBackgroundColor(color.get(position));

//        holder.courseCard.setBackgroundTintList(ColorStateList.valueOf(-395151));
        holder.examLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(mContext,CourseNames.get(position),Toast.LENGTH_LONG).show();
                int id = ids.get(position);
                Bundle bundle = new Bundle();
                bundle.putInt("id",  id);

//                edithomework cFragment = new edithomework();
//                cFragment.setArguments(bundle);
//                AppCompatActivity activity = (AppCompatActivity) view.getContext();
//
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, cFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public void removeItem(int position) {
        titles.remove(position);
        subject.remove(position);
        due_date.remove(position);
        color.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }





}
