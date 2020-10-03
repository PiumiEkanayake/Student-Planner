package com.example.timetable.Class;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.timetable.App;
import com.example.timetable.ColorPicker;
import com.example.timetable.Course.AddCourseFragment;
import com.example.timetable.Course.DisplayCourseFragment;
import com.example.timetable.Database.DBHandler;
import com.example.timetable.OptionsMenu;
import com.example.timetable.R;
import com.example.timetable.ReminderBroadcast;
import com.example.timetable.SelectDateFragment;
import com.google.android.material.tabs.TabLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addClass#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addClass extends Fragment  {

    EditText className,type,teacher,classRoom,note;
    Spinner course,subject,day;
    Button colorbtn,save;
    DBHandler db ;
    int selectedTab;
    String stab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DBHandler(getActivity().getApplicationContext());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_class, container, false);

        final ImageView addIcon = view.findViewById(R.id.addIcon);
        final  ImageView calendaricon = view.findViewById(R.id.calendarIcon);
        LayoutInflater layoutInflater= (LayoutInflater)getActivity().getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.list_popup, null);
        final PopupWindow popupWindow = new PopupWindow(popupView,450, ViewGroup.LayoutParams.WRAP_CONTENT);
        OptionsMenu.displayMenu(calendaricon,addIcon,popupWindow,popupView);

        className = (EditText) view.findViewById(R.id.className);
        type = (EditText) view.findViewById(R.id.type);
        teacher = (EditText) view.findViewById(R.id.teacher);
        classRoom =  (EditText) view.findViewById(R.id.classRoom);
        note = (EditText) view.findViewById(R.id.note);
        subject = (Spinner) view.findViewById(R.id.subjectSelect);
        course= (Spinner) view.findViewById(R.id.courseSelect);
        day = (Spinner) view.findViewById(R.id.daySelect);
        save = (Button) view.findViewById(R.id.saveClass);

        // Subject Selector
        Cursor cs = db.getAllSubjects();
        int count = db.getSubjectCount();
        String[] subjects = new String[count];
        int i = 0;
        while (cs.moveToNext()){
            subjects[i] = cs.getString(1);
            i++;
        }

        ArrayAdapter subjectAdapter= new ArrayAdapter(getActivity().getApplicationContext(), R.layout.spinner_item, subjects);
        final Spinner subjectSpinner= (Spinner) view.findViewById(R.id.subjectSelect);
        subjectSpinner.setAdapter(subjectAdapter);

        // Course Selector
        Cursor cc = db.getAllCourse();
        int countCourse = db.getCourseCount();
        String[] courses = new String[countCourse];
        int j = 0;
        while (cc.moveToNext()){
            courses[j] = cc.getString(1);
            j++;
        }

        ArrayAdapter courseAdapter= new ArrayAdapter(getActivity().getApplicationContext(), R.layout.spinner_item, courses);
        final Spinner courseSpinner= (Spinner) view.findViewById(R.id.courseSelect);
        courseSpinner.setAdapter(courseAdapter);

        //Colour Picker
        final Button colorbtn = (Button) view.findViewById(R.id.colorbtn);
        final Button bgBtn = (Button) view.findViewById(R.id.testbtn);
        colorbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ColorPicker cp = new ColorPicker() ;
                cp.openColorPicker(getChildFragmentManager(),colorbtn, bgBtn);
            }

        });




        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                boolean isInserted = false;

                DateFormat timeFormat = new SimpleDateFormat("HH:mm");
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date d1 = null;
                Date d2 = null;
                Date d3 = null;
                Date d4 = null;
                long remDate = 0;
                String stime,etime,saDate = null,eaDate = null;

                if(selectedTab == 0){
                   stime = ClassWeekFragment.stime.getText().toString();
                   etime = ClassWeekFragment.etime.getText().toString();
                   saDate =ClassWeekFragment.start.getText().toString();
                   eaDate = ClassWeekFragment.end.getText().toString();

                   try {
                       d3 =dateFormat.parse(saDate);
                       d4 = dateFormat.parse(eaDate);
                       remDate = d4.getTime() - d3.getTime();
                   }catch (ParseException e){
                       e.printStackTrace();
                   }

                }else{
                    stime = ClassDateFragment.stime.getText().toString();
                    etime = ClassDateFragment.etime.getText().toString();
                }

                try {
                    d1 = timeFormat.parse(stime);
                    d2 = timeFormat.parse(etime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long rem = d2.getTime() - d1.getTime();

                if (className.getText().toString().length() == 0) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter a Class Name", Toast.LENGTH_LONG).show();
                }else if(rem <= 0){
                    Toast.makeText(getActivity().getApplicationContext(), "Please select a valid end time", Toast.LENGTH_LONG).show();
                }else if(selectedTab == 0 && remDate <= 0) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please select a valid end date", Toast.LENGTH_LONG).show();
                }else if (selectedTab == 0) {
                    stab = "Weekly";
                    isInserted = db.addClass(className.getText().toString(), course.getSelectedItem().toString(), subject.getSelectedItem().toString(), type.getText().toString(), teacher.getText().toString(), classRoom.getText().toString(), note.getText().toString(), ((ColorDrawable) bgBtn.getBackground()).getColor(), stab, ClassWeekFragment.day.getSelectedItem().toString(), ClassWeekFragment.stime.getText().toString(), ClassWeekFragment.etime.getText().toString(), ClassWeekFragment.start.getText().toString(), ClassWeekFragment.end.getText().toString(), ClassWeekFragment.reminder.getSelectedItem().toString());
                } else {
                    stab = "Date";
                    isInserted = db.addClass(className.getText().toString(), course.getSelectedItem().toString(), subject.getSelectedItem().toString(), type.getText().toString(), teacher.getText().toString(), classRoom.getText().toString(), note.getText().toString(), ((ColorDrawable) bgBtn.getBackground()).getColor(), stab, "", ClassDateFragment.stime.getText().toString(), ClassDateFragment.etime.getText().toString(), ClassDateFragment.sdate.getText().toString(), "", ClassDateFragment.reminder.getSelectedItem().toString());
                }

                if(isInserted == true) {

                    Intent intent = new Intent(getActivity().getApplicationContext(), ReminderBroadcast.class);
                    intent.putExtra("reminderType", "Class Reminder");

                    String date = null;
                    Date startTime = null;
                    Date sdate = null;
                    Date edate = null;
                    Date endTime = null;
                    String eDate = null;
                    String rtype;
                    String day = null;
                    int dayValue = 0;
                    Calendar cald = Calendar.getInstance();
                    Calendar calt = Calendar.getInstance();

                    Calendar caled = Calendar.getInstance();
                    Calendar calet = Calendar.getInstance();
                    if(selectedTab == 0) {
                        intent.putExtra("text", className.getText().toString() + " Class starts at " + ClassWeekFragment.stime.getText().toString() + " on " + ClassWeekFragment.day.getSelectedItem().toString());
                        stime = ClassWeekFragment.stime.getText().toString();
                        etime = ClassWeekFragment.etime.getText().toString();
                        date = ClassWeekFragment.start.getText().toString();
                        eDate = ClassWeekFragment.end.getText().toString();
                        day = ClassWeekFragment.day.getSelectedItem().toString();
                        rtype = ClassWeekFragment.reminder.getSelectedItem().toString();
                    }else{
                        intent.putExtra("text", className.getText().toString() + " Class starts at " + ClassDateFragment.stime.getText().toString() + " on " + ClassDateFragment.sdate.getText().toString());
                        stime = ClassDateFragment.stime.getText().toString();
                        date = ClassDateFragment.sdate.getText().toString();
                        rtype = ClassDateFragment.reminder.getSelectedItem().toString();
                    }

                    int min = 0;

                    if(rtype.equals("5 minutes")){
                        min = -5;
                    }else  if(rtype.equals("10 minutes")){
                        min = -10;
                    } else  if(rtype.equals("15 minutes")){
                        min = -15;
                    } else  if(rtype.equals("30 minutes")){
                        min = -30;
                    } else  if(rtype.equals("1 hour")){
                        min = -60;
                    } else  if(rtype.equals("2 hours")){
                        min = -120;
                    } else  if(rtype.equals("6 hours")){
                        min = -360;
                    }else  if(rtype.equals("12 hours")){
                        min = -720;
                    }else  if(rtype.equals("1 day")){
                        min = 1440;
                    }
                    try {
                        startTime = timeFormat.parse(stime);
                        sdate = dateFormat.parse(date);
                        if(selectedTab == 0){
                            edate = dateFormat.parse(eDate);
                            endTime = timeFormat.parse(etime);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    cald.setTime(sdate);
                    calt.setTime(startTime);


                    cald.add(Calendar.HOUR_OF_DAY,calt.get(Calendar.HOUR_OF_DAY));
                    cald.add(Calendar.MINUTE,calt.get(Calendar.MINUTE));
                    cald.add(Calendar.MINUTE,min);

                    if(selectedTab == 0){
                        caled.setTime(edate);
                        calet.setTime(endTime);

                        caled.add(Calendar.HOUR_OF_DAY,calet.get(Calendar.HOUR_OF_DAY));
                        caled.add(Calendar.MINUTE,calet.get(Calendar.MINUTE));
                        caled.add(Calendar.MINUTE,min);
                    }



                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(),db.getLastClassIndex(),intent,0);
                    AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    long timeAtButtonClick = cald.getTimeInMillis();

                    if(selectedTab==0){
                        LocalDate slocalDate = sdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        if(day.equals("Monday"))
                            slocalDate = slocalDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
                        else if(day.equals("Tuesday"))
                            slocalDate = slocalDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY));
                        else if(day.equals("Wednesday"))
                            slocalDate = slocalDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.WEDNESDAY));
                        else if(day.equals("Thursday"))
                            slocalDate = slocalDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.THURSDAY));
                        else if(day.equals("Friday"))
                            slocalDate = slocalDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
                        else if(day.equals("Saturday"))
                            slocalDate = slocalDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
                        else if(day.equals("Sunday"))
                            slocalDate = slocalDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

                        calt.set(Calendar.DAY_OF_MONTH,slocalDate.getDayOfMonth());
                        calt.set(Calendar.MONTH,slocalDate.getMonthValue()-1);
                        calt.set(Calendar.YEAR,cald.get(Calendar.YEAR));
                        calt.add(Calendar.MINUTE,min);

                        int weekInMillis = 7*24*60*60*1000;
                        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calt.getTimeInMillis(),weekInMillis,pendingIntent);

                    }else{
                        alarmManager.set(AlarmManager.RTC_WAKEUP,
                                timeAtButtonClick ,
                                pendingIntent);
                    }

                        Toast.makeText(getActivity().getApplicationContext(), "Class Added Successfully", Toast.LENGTH_LONG).show();
                        ViewClassFragment fragment = new ViewClassFragment();
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                }else
                    Toast.makeText(getActivity().getApplicationContext(),"Insert Failed, Try again",Toast.LENGTH_LONG).show();
            }
        });


        final TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayoutClass);
        tabLayout.addTab(tabLayout.newTab().setText("Weekly"));
        tabLayout.addTab(tabLayout.newTab().setText("Date"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.add_class_pager);
        PgAdapter pgadapter = new PgAdapter(getChildFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pgadapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                selectedTab = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return view;
    }



}
class PgAdapter extends FragmentStatePagerAdapter {

    int noOfTabs;

    public PgAdapter(FragmentManager fm, int NumberOfTabs) {
        super(fm);
        this.noOfTabs = NumberOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;


        if (position == 0) {
            fragment = new ClassWeekFragment();
        }
        if (position == 1) {
            fragment = new ClassDateFragment();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
