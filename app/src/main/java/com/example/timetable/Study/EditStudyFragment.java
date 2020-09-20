package com.example.timetable.Study;

import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.timetable.ColorPicker;
import com.example.timetable.Database.DBHandler;
import com.example.timetable.Database.SubjectMaster;
import com.example.timetable.R;
import com.example.timetable.SelectDateFragment;
import com.example.timetable.SelectTimeFragement;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditStudyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditStudyFragment extends Fragment {

    DBHandler db;
    Integer studyId;
    EditText studyTitle, studyNote;
    Button studyColour;
    TextView studyDate, studyStart, studyEnd;
    Spinner subject, repeat, reminderTime;
    SwitchCompat reminder;
    Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DBHandler(getActivity());
        context = getActivity();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_study, container, false);

        studyId  = 0;
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            studyId = Integer.parseInt(bundle.get("id").toString());
        }

        if (studyId != 0) {
            String studyTitleTemp = null, studyDateTemp = null, studyStartTemp = null, studyEndTemp = null,
                    repeatTemp = null, studyNoteTemp = null, reminderTimeTemp = null;
            int subjectTemp = 0, studyColourTemp = 0, reminderTemp = 0;

            Cursor c = db.getSingleStudy(studyId);

            while (c.moveToNext()){
                studyTitleTemp = c.getString(1);
                subjectTemp = c.getInt(2);
                studyColourTemp = c.getInt(3);
                studyDateTemp = c.getString(4);
                studyStartTemp = c.getString(5);
                studyEndTemp = c.getString(6);
                repeatTemp = c.getString(7);
                studyNoteTemp = c.getString(8);
                reminderTemp = c.getInt(9);
                reminderTimeTemp = c.getString(10);
            }

            studyTitle = (EditText) view.findViewById(R.id.study_title);
            subject = (Spinner) view.findViewById(R.id.subject);
            studyColour = view.findViewById(R.id.study_colour);
            studyDate = (TextView) view.findViewById(R.id.study_date);
            studyStart = (TextView) view.findViewById(R.id.study_start);
            studyEnd = (TextView) view.findViewById(R.id.study_end);
            repeat = (Spinner) view.findViewById(R.id.repeat);
            studyNote = (EditText) view.findViewById(R.id.study_note);
            reminder = (SwitchCompat) view.findViewById(R.id.reminder);
            reminderTime = (Spinner) view.findViewById(R.id.reminder_time);


            // Pre-fill form inputs with existing database values
            studyTitle.setText(studyTitleTemp);
            studyColour.setBackgroundColor(studyColourTemp);
            studyDate.setText(studyDateTemp);
            studyStart.setText(studyStartTemp);
            studyEnd.setText(studyEndTemp);
            studyNote.setText(studyNoteTemp);

            // Set Colour Button background tint
            final Button colourBtn = view.findViewById(R.id.colorbtn);
            colourBtn.setBackgroundTintList(ColorStateList.valueOf(studyColourTemp));


            // All Studies Button
            ImageView allStudiesBtn = view.findViewById(R.id.all_studies_btn);

            allStudiesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AllStudiesFragment fragment = new AllStudiesFragment();
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack(null).commit();
                }
            });


            // Add Studies Button
            ImageView addStudiesBtn = view.findViewById(R.id.add_study_btn);

            addStudiesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddStudyFragment fragment = new AddStudyFragment();
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                }
            });


            // Subject Selector
            Cursor subjectsCursor = db.getAllSubjects();

            String[] adapterCols = new String[]{SubjectMaster.Subjects.COLUMN_NAME_SUBJECT_NAME};
            int[] adapterRowViews = new int[]{android.R.id.text1};

            SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(getActivity(),
                    android.R.layout.simple_spinner_item, subjectsCursor, adapterCols, adapterRowViews, 0);
            cursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            subject.setAdapter(cursorAdapter);

            // Set selected subject
            for (int pos = 0; pos < subject.getCount(); pos++) {
                if (subject.getItemIdAtPosition(pos) == subjectTemp)
                    subject.setSelection(pos);
            }

            // Open subject selector if clicked anywhere on the surrounding layout
            LinearLayout subjectWrapper = (LinearLayout) view.findViewById(R.id.subject_wrapper);
            subjectWrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    subject.performClick();
                }
            });


            // Colour Picker
            colourBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ColorPicker cp = new ColorPicker();
                    cp.openColorPicker(getChildFragmentManager(), colourBtn, studyColour);
                }
            });

            // Open colour picker if clicked anywhere on the surrounding layout
            LinearLayout colourWrapper = (LinearLayout) view.findViewById(R.id.colour_wrapper);
            colourWrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    colourBtn.performClick();
                }
            });


            // Date Picker
            LinearLayout studyDatePicker = (LinearLayout) view.findViewById(R.id.study_date_picker);

            studyDatePicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Pass the TextView in order to set the value for the text
                    DialogFragment selectDateFragment = new SelectDateFragment(studyDate);
                    selectDateFragment.show(getFragmentManager(), "DatePicker");
                }
            });


            // Start Time Picker
            LinearLayout studyStartTimePicker = (LinearLayout) view.findViewById(R.id.study_start_time_picker);

            studyStartTimePicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogFragment selectStartTimeFragment = new SelectTimeFragement(studyStart);
                    selectStartTimeFragment.show(getFragmentManager(), "TimePicker");
                }
            });


            // End Time Picker
            LinearLayout studyEndTimePicker = (LinearLayout) view.findViewById(R.id.study_end_time_picker);

            studyEndTimePicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogFragment selectStartTimeFragment = new SelectTimeFragement(studyEnd);
                    selectStartTimeFragment.show(getFragmentManager(), "TimePicker");
                }
            });


            // Repeat Options Selector
            ArrayAdapter<CharSequence> repeatAdapter = ArrayAdapter
                    .createFromResource(context, R.array.repeat, android.R.layout.simple_spinner_item);
            repeatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            repeat.setAdapter(repeatAdapter);
            repeat.setSelection(repeatAdapter.getPosition(repeatTemp));

            // Open repeat options selector if clicked anywhere on the surrounding layout
            LinearLayout repeatWrapper = (LinearLayout) view.findViewById(R.id.repeat_wrapper);
            repeatWrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    repeat.performClick();
                }
            });


            // Reminder Time Selector
            ArrayAdapter<CharSequence> reminderTimeAdapter = ArrayAdapter
                    .createFromResource(context, R.array.times, android.R.layout.simple_spinner_item);
            reminderTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            reminderTime.setAdapter(reminderTimeAdapter);
            reminderTime.setSelection(reminderTimeAdapter.getPosition(reminderTimeTemp));

            // Open reminder time selector if clicked anywhere on the surrounding layout
            final LinearLayout reminderTimeWrapper = (LinearLayout) view.findViewById(R.id.reminder_time_wrapper);

            reminderTimeWrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (reminder.isChecked())
                        reminderTime.performClick();
                }
            });

            // Make the reminder time selector disabled and not clickable, if reminder is not enabled
            if (reminderTemp == 0) {
                reminderTime.setEnabled(false);
                reminderTimeWrapper.setClickable(false);
                reminderTimeWrapper.setFocusable(false);
            }


            // Reminder Toggle
            if (reminderTemp == 1)
                reminder.setChecked(true);

            // Enable reminder time selector only if reminder is enabled
            reminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked) {
                        reminderTime.setEnabled(true);
                        reminderTimeWrapper.setClickable(true);
                        reminderTimeWrapper.setFocusable(true);
                    }
                    else {
                        reminderTime.setEnabled(false);
                        reminderTimeWrapper.setClickable(false);
                        reminderTimeWrapper.setFocusable(false);
                    }
                }
            });

            // Toggle reminder if clicked anywhere on the surrounding layout
            LinearLayout reminderWrapper = (LinearLayout) view.findViewById(R.id.reminder_wrapper);
            reminderWrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reminder.toggle();
                }
            });


            // Submit Button
            Button updateStudyBtn = view.findViewById(R.id.update_study);

            updateStudyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Validate inputs before inserting to the database
                    if (studyTitle.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), "Please enter a study title", Toast.LENGTH_LONG).show();
                    }
                    else {
                        boolean isInserted = db.updateStudy(String.valueOf(studyId), studyTitle.getText().toString(), (int) subject.getSelectedItemId(),
                                ((ColorDrawable) studyColour.getBackground()).getColor(), studyDate.getText().toString(),
                                studyStart.getText().toString(), studyEnd.getText().toString(), repeat.getSelectedItem().toString(),
                                studyNote.getText().toString(), reminder.isChecked(), reminderTime.getSelectedItem().toString());

                        if (isInserted) {
                            Toast.makeText(getActivity(), "Study updated successfully", Toast.LENGTH_LONG).show();
                            AllStudiesFragment allStudiesFragment = new AllStudiesFragment();
                            AppCompatActivity activity = (AppCompatActivity) view.getContext();
                            activity.getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, allStudiesFragment)
                                    .addToBackStack(null).commit();
                        } else
                            Toast.makeText(getActivity(), "Insert failed, try again", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        return view;
    }
}