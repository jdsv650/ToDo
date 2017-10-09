package com.jdsv650.todo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by james on 9/14/17.
 */

public class CustomAdapter extends BaseAdapter {

    private ArrayList myList;
    private LayoutInflater inflater;
    private Context context;

    CustomAdapter(Context context, ArrayList list)
    {
        this.context = context;
        inflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myList = list;
    }

    public void addToDoItem(com.jdsv650.todo.ToDo todo)
    {
        myList.add(todo);
    }

    public ToDo getTodo(int pos)
    {
        return (ToDo) myList.get(pos);
    }

    // override the following methods for custom adapter

    // return the size of data list
    @Override
    public int getCount() {
        return myList.size();
    }

    // return individual item
    @Override
    public Object getItem(int i) {
        return myList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    // returrn the view
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder holder;

        if(view==null){ // haven't created view

            view= inflater.inflate(R.layout.my_view, viewGroup, false); // inflate the view
            holder= new ViewHolder();
            holder.view= view.findViewById(R.id.view);
            holder.titleTextView= (TextView)view.findViewById(R.id.titleTextView);
            holder.descriptionTextView= (TextView)view.findViewById(R.id.descriptionTextView);
            holder.dateTextView= (TextView) view.findViewById(R.id.dateTextView);
            holder.imageView= (ImageView) view.findViewById(R.id.imageView);
            view.setTag(holder);
        }else {
            holder= (ViewHolder)view.getTag();
        }

        holder.view.setBackgroundColor(Color.DKGRAY);
        // set name and phone
        ToDo todo = (ToDo) myList.get(position);
        holder.titleTextView.setText(todo.getTitle());
        holder.descriptionTextView.setText(todo.getDescription());
        holder.dateTextView.setText(todo.getDate());

        if (todo.getStatus() == 0)
        {
            holder.imageView.setImageResource(R.drawable.incomplete);
        }
        else
        {
            holder.imageView.setImageResource(R.drawable.complete);
        }
        return view;
    }

    public class ViewHolder{
        TextView view;
        TextView titleTextView, descriptionTextView, dateTextView;
        ImageView imageView;
    }


}
