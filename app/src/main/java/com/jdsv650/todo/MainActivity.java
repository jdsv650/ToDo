package com.jdsv650.todo;

import android.app.ActionBar;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList persons;
    CustomAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // seed some data into ArrayList
        seedData();

        listView = (ListView) findViewById(R.id.listView);

        // Create a cutom adapter and set to for the listview
        arrayAdapter = new CustomAdapter(this, persons);
        listView.setAdapter(arrayAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Integer itemId = item.getItemId();
        if (itemId == R.id.add_todo)
        {
            Toast.makeText(this, "Add Todo Selected", Toast.LENGTH_SHORT).show();
        }
        else if (itemId == R.id.view_completed)
        {
            Toast.makeText(this, "View Completed Todo Items", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    // seed some dummy data
    private void seedData()
    {
        persons = new ArrayList<ToDo>();
        persons.add(new ToDo("Make App", "Make ToDo app", "11/12/2017", 0));
        persons.add(new ToDo("Do Assignment", "Assignment 3", "9/17/2017", 1));
        persons.add(new ToDo("Get Phone", "Buy new Pixel phone", "12/25/2017", 1));
        persons.add(new ToDo("Play Switch", "Try out Nintendo Switch", "9/30/2017", 0));
        persons.add(new ToDo("Watch Star Wars", "Watch new Star Wars movie", "12/28/2017", 0));

    }
}
