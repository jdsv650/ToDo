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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

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

                    sortByDateAsc();
                    arrayAdapter.notifyDataSetChanged();
                    db.close();
                }
            }
        };

        runnable.run();
    }


    private void sortByDateAsc()
    {
        // sort records ascending
        Collections.sort(records, new Comparator<ToDo>() {
            public int compare(ToDo todo1, ToDo todo2) {

                Date d1 = convertStringToDate(todo1.getDate());
                Date d2 = convertStringToDate(todo2.getDate());
                return d1.compareTo(d2);
            }
        });
    }

    private Date convertStringToDate(String dateAsString)
    {
        Date date = new Date();
        String[] dateArr = dateAsString.split("/");

        if (dateArr.length >= 3) {
            date.setYear(Integer.parseInt(dateArr[2]));
            date.setMonth(Integer.parseInt(dateArr[0]) - 1);
            date.setDate(Integer.parseInt(dateArr[1]));
        }
        return date;
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
            sortByDateAsc();
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
                sortByDateAsc();
               // arrayAdapter.notifyDataSetChanged();
            }
        }
        catch (Exception ex)
        {

        }
        finally {
            arrayAdapter.notifyDataSetChanged();
            db.close();
        }



    }



    public void updateStatus(Integer i)
    {

        ToDoDatabase todoDb = new ToDoDatabase(this);  // get read - write database
        SQLiteDatabase db = todoDb.getWritableDatabase();

        com.jdsv650.todo.ToDo todo = arrayAdapter.getTodo(i);

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
            }
        }
        catch (Exception ex)
        {

        }
        finally {
            arrayAdapter.notifyDataSetChanged();
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
        updateStatus(i);

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
