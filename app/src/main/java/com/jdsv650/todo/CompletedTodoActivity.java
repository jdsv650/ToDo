package com.jdsv650.todo;

import android.content.ContentValues;
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

public class CompletedTodoActivity extends AppCompatActivity implements
        AdapterView.OnItemLongClickListener {

    ArrayList<ToDo> records = new ArrayList<ToDo>();

    ListView listView;
    CustomAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_todo);

        //read db

        listView = (ListView) findViewById(R.id.listViewCompleted);
        listView.setOnItemLongClickListener(this);

        // Create a cutom adapter and set to for the listview
        arrayAdapter = new CustomAdapter(this, records);
        listView.setAdapter(arrayAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        records.clear();
        readDB();
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
                    arrayAdapter.notifyDataSetChanged();

                }
                catch (Exception ex)
                {
                    // COULDN"T READ FROM DB
                    Log.i("READ ERROR", "ERROR READING FROM DB");
                }
                finally {
                    db.close();
                }

            }
        };

        runnable.run();
    }

    public void deleteTodoDB(Integer id, Integer listIndex)
    {
        ToDoDatabase todoDb = new ToDoDatabase(this);  // get read - write database
        SQLiteDatabase db = todoDb.getWritableDatabase();

        Integer resultId = db.delete("TODO", "ID=?", new String[] { id.toString() });

        if (resultId == -1)  // failed
        {
            Toast.makeText(this, "Couldn't delete record" , Toast.LENGTH_SHORT).show();
        }
        else // resultId = id generated for row inserted
        {
            Toast.makeText(this, "record deleted" , Toast.LENGTH_SHORT).show();

            arrayAdapter.deleteTodo(listIndex);
            arrayAdapter.notifyDataSetChanged();

        }

        db.close();

    }



    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        // toggles status
        Toast.makeText(this,"LONG Click PRESSES", Toast.LENGTH_SHORT).show();

        deleteTodoDB(((ToDo) arrayAdapter.getItem(i)).getId().intValue(), i);

        return true;
    }
}
