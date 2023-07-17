package com.example.dolphinlive.UI;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dolphinlive.Database.Repository;
import com.example.dolphinlive.Entity.Course;
import com.example.dolphinlive.Entity.Term;
import com.example.dolphinlive.R;


import java.util.List;

public class CourseScreen extends AppCompatActivity {
    private Repository repository;
    private ImageView hamburger;
    private ImageView home;
    private TextView toolbarText;
    private CourseAdapter courseAdapter;
    private List<Course> allCourses;
    private List<Term> allTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        hamburger = toolbar.findViewById(R.id.menuIcon);
        home = toolbar.findViewById(R.id.homeIcon);
        toolbarText = toolbar.findViewById(R.id.toolbarText);
        toolbarText.setText("All Courses");
        //recycler
        RecyclerView recyclerView = findViewById(R.id.courseRecyclerView);
        courseAdapter = new CourseAdapter(this, 0);
        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        repository = new Repository(getApplication());
        allCourses = repository.getAllCourses();
        allTerms = repository.getALlTerms();
        courseAdapter.setCourseList(allCourses);
        courseAdapter.setTermList(allTerms);


        //set buttons
        hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goHome(view);
            }
        });

    }

    private void showPopupMenu(View view) {
        // Inflate the menu using the PopupMenu class
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.menu_coursescreen);

        // Set a click listener on the menu items
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menuAllTerms:
                        Intent intent = new Intent(CourseScreen.this, TermScreen.class);
                        startActivity(intent);
                        return true;
                    case R.id.menuAllAssessments:
                        goToAssessmentScreen(view);
                        return true;
                    default:
                        return false;
                }
            }
        });
        // Show the menu
        popupMenu.show();
    }

    public void goToAssessmentScreen(View view) {
        Intent intent = new Intent(CourseScreen.this, AssessmentScreen.class);
        startActivity(intent);
    }

    public void goToTermScreen(View view) {
        Intent intent = new Intent(CourseScreen.this, TermScreen.class);
        Toast.makeText(getApplicationContext(), "Select Term to Add Courses. Tap + if none available.",
                Toast.LENGTH_LONG).show();
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        allCourses = repository.getAllCourses();
        courseAdapter.setCourseList(allCourses);
        courseAdapter.notifyDataSetChanged();
        if (allCourses.size() != 0) {
            TextView label = findViewById(R.id.noCoursesLabel);
            label.setVisibility(View.GONE);
        } else {
            TextView label = findViewById(R.id.noCoursesLabel);
            label.setVisibility(View.VISIBLE);
        }
    }
    private void goHome(View view) {
        Intent intent = new Intent(CourseScreen.this, MainActivity.class);
        startActivity(intent);
    }
}