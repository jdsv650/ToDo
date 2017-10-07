package com.jdsv650.todo;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by james on 10/7/17.
 */

public class AddTodoDialog extends DialogFragment {

    private EditText titleEditText;

    public AddTodoDialog() {
        // Empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_todo, container);
        titleEditText = (EditText) view.findViewById(R.id.title_editText);
        getDialog().setTitle("Add Todo");

        return view;
    }


}
