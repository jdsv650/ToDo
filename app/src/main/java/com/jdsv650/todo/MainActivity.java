package com.jdsv650.todo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
//import android.support.v4.app.FragmentManager;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddTodoDialog.AddTodoDialogListener,
        UpdateTodoDialog.UpdateTodoDialogListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener
{

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
        //populateDB();

        //readDB();

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(this);

        // Create a cutom adapter and set to for the listview
        arrayAdapter = new CustomAdapter(this, records);
        listView.setAdapter(arrayAdapter);

    }

    @Override
    protected void onPause() {
        super.onPause();

        records.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();

        records.clear();
        readDB();
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
            Intent i = new Intent(this, CompletedTodoActivity.class);
            startActivity(i);
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

        Long result = db.insert("TODO", null, vals);
        if (result == -1) // try insert
        {
            Toast.makeText(this, "Couldn't insert record" , Toast.LENGTH_SHORT).show();
        }
        else // was succcess and result contains gen id
        {

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
                try {
                    Cursor cursor = db.query("TODO", null, null, null, null, null, null);

                    cursor.moveToFirst();  // go to first item

                    if (cursor != null) {
                        do {
                            Long id = cursor.getLong(0);      // build a todo item
                            String title = cursor.getString(1);
                            String description = cursor.getString(2);
                            String date = cursor.getString(3);
                            Integer status = cursor.getInt(4);

                            ToDo t = new ToDo(title, description, date, status);
                            t.setId(id);

                            records.add(t);   // add it to the list

                        } while (cursor.moveToNext());
                    }

                    cursor.close();

                }
                catch (Exception ex)
                {
                    // COULDN"T READ FROM DB
                    Log.i("READ ERROR", "ERROR READING FROM DB");
                }
                finally {

                    arrayAdapter.notifyDataSetChanged();
                    db.close();
                }

            }
        };

        runnable.run();
    }

    public void addTodoDB(ToDo todo)
    {
        ToDoDatabase todoDb = new ToDoDatabase(this);  // get read - write database
        SQLiteDatabase db = todoDb.getWritableDatabase();

        // setup record to insert

        ContentValues vals = new ContentValues();
        vals.put("TITLE", todo.getTitle());
        vals.put("DESCRIPTION", todo.getDescription());
        vals.put("DATE", todo.getDate());
        vals.put("Status", 0);

        Long resultId = db.insert("TODO", null, vals); // try insert

        if (resultId == -1)  // failed
        {
            Toast.makeText(this, "Couldn't insert record" , Toast.LENGTH_SHORT).show();
        }
        else // resultId = id generated for row inserted
        {
            Toast.makeText(this, "record inserted" , Toast.LENGTH_SHORT).show();
            todo.setId(resultId);
            arrayAdapter.addToDoItem(todo);
            arrayAdapter.notifyDataSetChanged();

        }

        db.close();

    }

    AddTodoDialog dialog;

    private void showAddTodoDialog() {
        //FragmentManager fm = getSupportFragmentManager();
        FragmentManager fm = getFragmentManager();

        AddTodoDialog addTodoDialog = new AddTodoDialog();
        dialog = addTodoDialog;
        addTodoDialog.show(fm, "Add Todo");
    }

    UpdateTodoDialog updateDialog;

    private void showUpdateTodoDialog(ToDo todo) {

        FragmentManager fm = getFragmentManager();

        UpdateTodoDialog updateTodoDialog = new UpdateTodoDialog();
        updateDialog = updateTodoDialog;

        Bundle args = new Bundle();

        args.putLong("id", todo.getId());
        args.putString("title", todo.getTitle());
        args.putString("description", todo.getDescription());
        args.putString("date", todo.getDate());
        args.putInt("status", todo.getStatus());

        updateTodoDialog.setArguments(args);
        updateTodoDialog.show(fm, "Update Todo");


    }

    @Override
    public void onFinishEditDialog(ToDo todo) {

        Toast.makeText(this, "TITLE RETURNED TO ACTIVITY = " + todo.getTitle() + "\nDESC = " + todo.getDescription()
                + "\nDATE = " + todo.getDate()
                + "\nSTATUS = " + todo.getStatus(), Toast.LENGTH_LONG).show();

        // save todo
        addTodoDB(todo);

    }


    public void updateTodoDB(ToDo todo)
    {
        ToDoDatabase todoDb = new ToDoDatabase(this);  // get read - write database
        SQLiteDatabase db = todoDb.getWritableDatabase();

        // setup record to insert

        ContentValues vals = new ContentValues();
        vals.put("TITLE", todo.getTitle());
        vals.put("DESCRIPTION", todo.getDescription());
        vals.put("DATE", todo.getDate());
        vals.put("Status", todo.getStatus());

        try
        {
            Integer result = db.update("TODO", vals, "id = ?", new String[]{ todo.getId().toString() });

            if (result == -1)
            {
                Toast.makeText(this, "Couldn't update record" , Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "record updated" , Toast.LENGTH_SHORT).show();
                // update list item

                records.set(itemToUpdate, todo);
                arrayAdapter.notifyDataSetChanged();
            }
        }
        catch (Exception ex)
        {

        }
        finally {
            db.close();
        }



    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        // toggles status
        Toast.makeText(this,"LONG Click PRESSES", Toast.LENGTH_SHORT).show();

        com.jdsv650.todo.ToDo todo = arrayAdapter.getTodo(i);

        if (todo.getStatus() == 0)
        {
            todo.setStatus(1);
        }
        else
        {
            todo.setStatus(0);
        }
        // try to write to db - if success toggle status
        updateTodoDB(todo);

        return true;
    }

    Integer itemToUpdate = -1;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        itemToUpdate = i;
        Toast.makeText(this, "Regular click", Toast.LENGTH_SHORT).show();
        com.jdsv650.todo.ToDo todo = arrayAdapter.getTodo(i);
        showUpdateTodoDialog(todo);

    }

    @Override
    public void onFinishUpdateDialog(ToDo todo) {

        Toast.makeText(this, "TITLE RETURNED TO ACTIVITY = " + todo.getTitle() + "\nDESC = " + todo.getDescription()
                + "\nDATE = " + todo.getDate()
                + "\nSTATUS = " + todo.getStatus(), Toast.LENGTH_LONG).show();

        updateTodoDB(todo);

    }
}
