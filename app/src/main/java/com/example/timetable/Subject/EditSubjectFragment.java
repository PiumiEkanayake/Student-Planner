package com.example.timetable.Subject;

import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.timetable.ColorPicker;
import com.example.timetable.Database.DBHandler;
import com.example.timetable.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditSubjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditSubjectFragment extends Fragment {

    DBHandler db;
    EditText subjectName, teacherName , subjectDesc;
    int colour;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DBHandler(getActivity().getApplicationContext());
    }
    
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_subject, container, false);
        Button btn = view.findViewById(R.id.update_subject);
        int subjectId  = 0;
        Bundle bundle = this.getArguments();
        final Button colourBtn = view.findViewById(R.id.colorbtn);
        final Button bgBtn = view.findViewById(R.id.testbtn);
        colourBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ColorPicker cp = new ColorPicker() ;
                cp.openColorPicker(getChildFragmentManager(),colourBtn, bgBtn);

            }

        });

        if (bundle != null) {
            subjectId = Integer.parseInt(bundle.get("id").toString());
        }

        if (subjectId != 0) {
            String subjectNameTemp = null, teacherNameTemp = null, subjectDescTemp = null;
            Cursor c = db.getSingleSubject(subjectId);

            while (c.moveToNext()){
                subjectNameTemp = c.getString(1);
                teacherNameTemp = c.getString(2);
                subjectDescTemp = c.getString(3);
                colour = c.getInt(4);
            }

            subjectName = view.findViewById(R.id.subject_name);
            teacherName = view.findViewById(R.id.teacher_name);
            subjectDesc = view.findViewById(R.id.subject_desc);

            subjectName.setText(subjectNameTemp);
            teacherName.setText(teacherNameTemp);
            subjectDesc.setText(subjectDescTemp);
            colourBtn.setBackgroundTintList(ColorStateList.valueOf(colour));
            bgBtn.setBackgroundColor(colour);

            ImageView allSubBtn = view.findViewById(R.id.all_subjects_btn);

            allSubBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AllSubjectsFragment fragment = new AllSubjectsFragment();
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                }
            });

            final int finalId = subjectId;

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Validate inputs before updating the database
                    if (subjectName.getText().toString().equals("")) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter a subject name", Toast.LENGTH_LONG).show();
                    } else if (teacherName.getText().toString().equals("")) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter a teacher's name", Toast.LENGTH_LONG).show();
                    } else {
                        boolean isInserted = db.updateSubject(String.valueOf(finalId), subjectName.getText().toString(), teacherName.getText().toString(), subjectDesc.getText().toString(), ((ColorDrawable) bgBtn.getBackground()).getColor());

                        if (isInserted) {
                            Toast.makeText(getActivity().getApplicationContext(), "Updated Successfully", Toast.LENGTH_LONG).show();
                            AllSubjectsFragment fragment = new AllSubjectsFragment();
                            AppCompatActivity activity = (AppCompatActivity) view.getContext();

                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                        } else
                            Toast.makeText(getActivity().getApplicationContext(), "Update Failed, Please Try again", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        return view;
    }
}