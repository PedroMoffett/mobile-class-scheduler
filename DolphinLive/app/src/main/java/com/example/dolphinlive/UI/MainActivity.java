package com.example.dolphinlive.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.dolphinlive.Database.Repository;
import com.example.dolphinlive.Entity.Assessment;
import com.example.dolphinlive.Entity.Course;
import com.example.dolphinlive.Entity.Term;
import com.example.dolphinlive.R;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static int numAlert = 0;
    private ImageView hamburger;
    private TextView toolbarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Repository r = new Repository(getApplication());
        Toolbar toolbar = findViewById(R.id.toolbar);
        Log.d("DEBUG", "TextView reference: " + toolbar);
        setSupportActionBar(toolbar);
        hamburger = toolbar.findViewById(R.id.menuIcon);
        toolbarText = toolbar.findViewById(R.id.toolbarText);
        List<Term> deleteTerms;
        deleteTerms = r.getALlTerms();
        toolbarText.setText("Welcome");
        /*for (Term t : deleteTerms){
            r.deleteTerm(t);
        }
        for (Course c : r.getAllCourses()){
            r.deleteCourse(c);
        }
        for(Assessment a : r.getAllAssessments()){
            r.deleteAssessment(a);
        }

         */
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
        popupMenu.inflate(R.menu.menu_main);

        // Set a click listener on the menu items
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menuAllTerms:
                        goToTermScreen(view);
                        return true;
                    case R.id.menuAllCourses:
                        goToCourseScreen(view);
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

    public void goToTermScreen(View view){
        Intent intent = new Intent(MainActivity.this, TermScreen.class);
        startActivity(intent);

    }
    public void goToAssessmentScreen(View view){
        Intent intent = new Intent(MainActivity.this, AssessmentScreen.class);
        startActivity(intent);
    }

    public void goToCourseScreen(View view){
        Intent intent = new Intent(MainActivity.this, CourseScreen.class);
        startActivity(intent);
    }

}