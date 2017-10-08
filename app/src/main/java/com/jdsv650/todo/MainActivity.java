package com.jdsv650.todo;

import android.app.ActionBar;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
//import android.support.v4.app.FragmentManager;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddTodoDialog.AddTodoDialogListener {

    ArrayList<ToDo> records = new ArrayList<ToDo>();

    ListView listView;
    ArrayList persons;
    CustomAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // seed some data into ArrayList
       // seedData();

        populateDB();
        readDB();

        listView = (ListView) findViewById(R.id.listView);

        // Create a cutom adapter and set to for the listview
        arrayAdapter = new CustomAdapter(this, records);
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
            showAddTodoDialog();
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


    public void populateDB()
    {
        Toast.makeText(this, "Populating SQLlite db", Toast.LENGTH_LONG).show();

        ToDoDatabase todoDb = new ToDoDatabase(this);  // get read - write database
        SQLiteDatabase db = todoDb.getWritableDatabase();

        // setup record to insert

        ContentValues vals = new ContentValues();
        vals.put("TITLE", "Meet Ed");
        vals.put("DESCRIPTION", "RL");
        vals.put("DATE", "10/12/17");
        vals.put("Status", 0);

        if (db.insert("TODO", null, vals) == -1) // try insert
        {
            Toast.makeText(this, "Couldn't insert record" , Toast.LENGTH_SHORT).show();
        }

    }

    private void readDB()
    {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                // get read access to database
                ToDoDatabase todoDb = new ToDoDatabase(getBaseContext());
                SQLiteDatabase db = todoDb.getReadableDatabase();

                // setup the query
                Cursor cursor = db.query("TODO", null,null,null,null,null,null);
                cursor.moveToFirst();  // go to first item

                if (cursor != null)
                {
                    do {
                        Integer id = cursor.getInt(0);      // build a todo item
                        String title = cursor.getString(1);
                        String description = cursor.getString(2);
                        String date = cursor.getString(3);
                        Integer status = cursor.getInt(4);

                        ToDo t = new ToDo(title, description, date, status);

                        records.add(t);   // add it to the list

                    } while (cursor.moveToNext());
                }

            }
        };

        runnable.run();
    }

    AddTodoDialog dialog;

    private void showAddTodoDialog() {
        //FragmentManager fm = getSupportFragmentManager();
        FragmentManager fm = getFragmentManager();

        AddTodoDialog addTodoDialog = new AddTodoDialog();
        dialog = addTodoDialog;
        addTodoDialog.show(fm, "Add Todo");
    }

    @Override
    public void onFinishEditDialog(ToDo todo) {

        Toast.makeText(this, "TITLE RETURNED TO ACTIVITY = " + todo.getTitle() + "\nDESC = " + todo.getDescription()
                + "\nDATE = " + todo.getDate()
                + "\nSTATUS = " + todo.getStatus(), Toast.LENGTH_LONG).show();


    }
}
