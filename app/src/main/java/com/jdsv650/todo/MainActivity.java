package com.jdsv650.todo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

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


    // seed some dummy data
    private void seedData()
    {
        persons = new ArrayList<ToDo>();
        persons.add(new ToDo("Make App", "Make ToDo app", "11/12/2017", 0));
        persons.add(new ToDo("Do Assignment", "Assignment 3", "9/17/2017", 0));
        persons.add(new ToDo("Get Phone", "Buy new Pixel phone", "12/25/2017", 0));
        persons.add(new ToDo("Play Switch", "Try out Nintendo Switch", "9/30/2017", 0));
        persons.add(new ToDo("Watch Star Wars", "Watch new Star Wars movie", "12/28/2017", 0));

    }
}
