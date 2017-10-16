package com.jdsv650.todo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

// display only completed tasks
public class CompletedTodoActivity extends AppCompatActivity implements
        AdapterView.OnItemLongClickListener {

    ArrayList<ToDo> records = new ArrayList<ToDo>();

    ListView listView;
    CustomAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_todo);

        listView = (ListView) findViewById(R.id.listViewCompleted);
        listView.setOnItemLongClickListener(this);

        // Create a cutom adapter and set for the listview
        arrayAdapter = new CustomAdapter(this, records);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // clear or records and reread from db
        records.clear();
        readDB();
    }

    // fetch records from sqlite db
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

                    // only return records where status = 1
                    Cursor cursor = db.query("TODO", null, "STATUS=?", new String[] { "1" }, null, null, null);
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
                    db.close();
                    sortByDateAsc();
                    arrayAdapter.notifyDataSetChanged();

                }
                catch (Exception ex)
                {
                    Log.i("READ ERROR", "ERROR READING FROM DB");
                }
                finally {
                    db.close();
                }

            }
        };

        runnable.run();
    }

    // call delete to remove a todo item
    public void deleteTodoDB(Integer id, Integer listIndex)
    {
        ToDoDatabase todoDb = new ToDoDatabase(this);  // get read - write database
        SQLiteDatabase db = todoDb.getWritableDatabase();

        Integer resultId = db.delete("TODO", "ID=?", new String[] { id.toString() });

        if (resultId == -1)  // failed
        {
            Toast.makeText(this, "Error deleting record. Try Again." , Toast.LENGTH_SHORT).show();
        }
        else // resultId = id generated for row inserted
        {
            Toast.makeText(this, "Record deleted" , Toast.LENGTH_SHORT).show();

            arrayAdapter.deleteTodo(listIndex);
            arrayAdapter.notifyDataSetChanged();
        }

        db.close();
    }

    @Override // delete record on long press
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        deleteTodoDB(((ToDo) arrayAdapter.getItem(i)).getId().intValue(), i);
        return true;
    }

    // sort list ascending
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

    // helper to convert a date as string to Date
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
}
