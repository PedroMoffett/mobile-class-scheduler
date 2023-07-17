package com.example.dolphinlive.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dolphinlive.Database.Repository;
import com.example.dolphinlive.Entity.Assessment;
import com.example.dolphinlive.Entity.Course;
import com.example.dolphinlive.R;

import java.util.List;

public class AssessmentScreen extends AppCompatActivity {
    private ImageView hamburger;
    private ImageView home;
    private TextView toolbarText;
    private Repository repository;
    private AssessmentAdapter assessmentAdapter;
    private List<Course> allCourses;
    private List<Assessment> allAssessments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessments_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        hamburger = toolbar.findViewById(R.id.menuIcon);
        toolbarText = toolbar.findViewById(R.id.toolbarText);
        toolbarText.setText("All Assessments");
        RecyclerView recyclerView = findViewById(R.id.assessmentRecyclerView);
        assessmentAdapter= new AssessmentAdapter(this,0);
        recyclerView.setAdapter(assessmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        repository = new Repository(getApplication());
        home = toolbar.findViewById(R.id.homeIcon);
        allAssessments = repository.getAllAssessments();
        allCourses = repository.getAllCourses();
        assessmentAdapter.setAssessmentList(allAssessments);
        assessmentAdapter.setAllCourses(allCourses);
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

    public void goToCourseScreen(View view) {
        Intent intent = new Intent(AssessmentScreen.this, CourseScreen.class);
        Toast.makeText(getApplicationContext(), "Select Course to add Assessment to. Tap + if none available.",
                Toast.LENGTH_LONG).show();
        startActivity(intent);
    }
    private void showPopupMenu(View view) {
        // Inflate the menu using the PopupMenu class
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.menu_assessmentscreen);


        // Set a click listener on the menu items
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            Intent intent;
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menuAllTerms:
                        intent = new Intent(AssessmentScreen.this, TermScreen.class);
                        startActivity(intent);
                        return true;
                    case R.id.menuAllCourses:
                        intent = new Intent(AssessmentScreen.this, CourseScreen.class);
                        startActivity(intent);
                        return true;
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
        allAssessments = repository.getAllAssessments();
        assessmentAdapter.setAssessmentList(allAssessments);
        assessmentAdapter.notifyDataSetChanged();
        if (allAssessments.size() != 0){
            TextView label = findViewById(R.id.noAssessmentLabel);
            label.setVisibility(View.GONE);
        }
        else{
            TextView label = findViewById(R.id.noAssessmentLabel);
            label.setVisibility(View.VISIBLE);
        }
    }
    private void goHome(View view) {
        Intent intent = new Intent(AssessmentScreen.this, MainActivity.class);
        startActivity(intent);
    }
}