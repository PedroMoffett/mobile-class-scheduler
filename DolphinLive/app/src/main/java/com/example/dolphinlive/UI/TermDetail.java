package com.example.dolphinlive.UI;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dolphinlive.DAO.CourseDAO;
import com.example.dolphinlive.Database.Repository;
import com.example.dolphinlive.Entity.Assessment;
import com.example.dolphinlive.Entity.Course;
import com.example.dolphinlive.Entity.Term;
import com.example.dolphinlive.R;
import com.example.dolphinlive.Utility.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TermDetail extends AppCompatActivity {
    EditText editTitle;
    EditText editStartDate;
    EditText editEndDate;
    DatePickerDialog.OnDateSetListener startDate;
    DatePickerDialog.OnDateSetListener endDate;
    final Calendar calendarStart = Calendar.getInstance();
    final Calendar calendarEnd = Calendar.getInstance();
    int id;
    Repository repository;
    CourseAdapter courseAdapter;
    List<Term> dummyList = new ArrayList<>();
    private ImageView hamburger;
    private ImageView home;
    private TextView toolbarText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        hamburger = toolbar.findViewById(R.id.menuIcon);
        home = toolbar.findViewById(R.id.homeIcon);
        toolbarText = toolbar.findViewById(R.id.toolbarText);
        //set title of action bar
        if(getIntent().getStringExtra("title") == null){
            toolbarText.setText("New Term");
        }
        else{
            toolbarText.setText(getIntent().getStringExtra("title"));
        }
        id = getIntent().getIntExtra("id",0);
        Button saveButton = findViewById(R.id.termDetailSaveButton);
        Button addCourseButton = findViewById(R.id.termDetailAddCourseToTermButton);
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editTitle = findViewById(R.id.editTextTermTitle);
        editStartDate = findViewById(R.id.editTextTermStart);
        editEndDate = findViewById(R.id.editTextTermEnd);
        Util.cacheCourses.clear();
        //recycler
        repository = new Repository(getApplication());
        RecyclerView recyclerView = findViewById(R.id.coursesInsideTermDetails);
        courseAdapter = new CourseAdapter(this, id);
        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //get associated courses
        if (id != 0){
            for (Course c : repository.getAllCourses()) {
                if (c.getTermID() == id) Util.cacheCourses.add(c);
            }
        }
        //get all terms
        dummyList = repository.getALlTerms();
        courseAdapter.setCourseList(Util.cacheCourses);
        courseAdapter.setTermList(dummyList);
        //set the EditText Views
        if (id == 0){
            editStartDate.setText(sdf.format(new Date()));
            editEndDate.setText(sdf.format(new Date()));
        }
        else{
            editStartDate.setText((getIntent().getStringExtra("start").toString()));
            editEndDate.setText((getIntent().getStringExtra("end").toString()));
            editTitle.setText(getIntent().getStringExtra("title").toString());
        }

// set up buttons///////////////////////////////////////////////////////////
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.validateDateRange(editStartDate.getText().toString(), editEndDate.getText().toString())) {
                    if(id == 0) {
                        id = (int)(repository.insertTerm(new Term(
                                id,
                                editTitle.getText().toString(),
                                editStartDate.getText().toString(),
                                editEndDate.getText().toString())));
                        for(Course c : Util.cacheCourses){
                            c.setTermID(id);
                            int courseID = (int)(repository.insertCourse(c));
                            for(Assessment assessment: c.getAssociatedAssessments()){
                                assessment.setCourseID(courseID);
                                repository.insertAssessment(assessment);
                            }
                        }
                    }
                    else{
                        repository.updateTerm(new Term(id,
                                editTitle.getText().toString(),
                                editStartDate.getText().toString(),
                                editEndDate.getText().toString()));
                    }
                    Util.cacheCourses.clear();
                    goToTermScreen(v);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Can't Save. Start Date is After end Date", Toast.LENGTH_LONG).show();
                }
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goHome(view);
            }
        });
        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCourseDetail(v);

            }
        });
        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = editStartDate.getText().toString();
                try{
                    calendarStart.setTime(sdf.parse(info));
                }
                catch (ParseException e){
                    e.printStackTrace();
                }
                new DatePickerDialog(TermDetail.this, startDate,
                        calendarStart.get(Calendar.YEAR),
                        calendarStart.get(Calendar.MONTH),
                        calendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendarStart.set(Calendar.YEAR, year);
                calendarStart.set(Calendar.MONTH, month);
                calendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel(editStartDate, calendarStart);
            }
        };
        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = editEndDate.getText().toString();
                try{
                    calendarEnd.setTime(sdf.parse(info));
                }
                catch (ParseException e){
                    e.printStackTrace();
                }
                new DatePickerDialog(TermDetail.this, endDate,
                        calendarEnd.get(Calendar.YEAR),
                        calendarEnd.get(Calendar.MONTH),
                        calendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        endDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendarEnd.set(Calendar.YEAR, year);
                calendarEnd.set(Calendar.MONTH, month);
                calendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel(editEndDate, calendarEnd);
            }
        };
        hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });


    }
    private void showPopupMenu(View view) {
        // Inflate the menu using the PopupMenu class
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.menu_termdetails);

        // Set a click listener on the menu items
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.removeTerm:
                        if (id != 0){
                            //Term has been saved
                            ArrayList<Course> temp = new ArrayList<>();
                            for(Course c : repository.getAllCourses()){
                                if(c.getTermID() == id){
                                    temp.add(c);
                                }
                            }
                            //if no assigned courses
                            if(temp.size()==0 && Util.cacheCourses.size()==0) {
                                repository.deleteTerm(new Term(id, null, null, null));
                                finish();
                            }
                            //term saved but has assigned courses
                            else{
                                Toast.makeText(getApplicationContext(), "Cannot delete until all assigned courses are deleted.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                        //term has not been saved
                        else{
                            //term has not been saved but has cached courses
                            if(Util.cacheCourses.size() != 0){
                                Toast.makeText(getApplicationContext(), "Cannot delete until all assigned courses are deleted.",
                                        Toast.LENGTH_LONG).show();
                            }
                            else{//term has not been saved and has no cached courses. can just finish.
                                finish();
                            }
                        }
                    default:
                        return false;
                }
            }
        });
        // Show the menu
        popupMenu.show();
    }
    @Override
    protected void onResume(){
        super.onResume();
        if (id == 0){
            courseAdapter.notifyDataSetChanged();
        }
        else{
            Util.cacheCourses.clear();
            for (Course c : repository.getAllCourses()) {
                if (c.getTermID() == id) Util.cacheCourses.add(c);
            }
            courseAdapter.notifyDataSetChanged();
        }
    }
    public void goToTermScreen(View view){
        Intent intent = new Intent(TermDetail.this, TermScreen.class);
        startActivity(intent);

    }
    public void goToCourseDetail(View view){
        Intent intent = new Intent(TermDetail.this, CourseDetail.class);
        intent.putExtra("termID", id);
        startActivity(intent);
    }
    private void goHome(View view) {
        Intent intent = new Intent(TermDetail.this, MainActivity.class);
        startActivity(intent);
    }


    private void updateDateLabel(EditText editText, Calendar calendar){
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(sdf.format(calendar.getTime()));
    }
}