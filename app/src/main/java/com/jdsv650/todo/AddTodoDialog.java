package com.jdsv650.todo;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

/**
 * Created by james on 10/7/17.
 */

public class AddTodoDialog extends DialogFragment implements View.OnClickListener {

     public interface AddTodoDialogListener {
            void onFinishEditDialog(com.jdsv650.todo.ToDo todo);
     }

    private EditText titleEditText;
    private EditText descriptionEditText;
    private DatePicker datePicker;
    private AddTodoDialogListener listener;

    public AddTodoDialog() {
        // Empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_todo, container);
        titleEditText = (EditText) view.findViewById(R.id.title_editText);
        descriptionEditText = (EditText) view.findViewById(R.id.description_editText);
        datePicker = (DatePicker) view.findViewById(R.id.datePicker);

        ((Button) view.findViewById(R.id.cancelButton)).setOnClickListener(this);
        ((Button) view.findViewById(R.id.saveButton)).setOnClickListener(this);

        getDialog().setTitle("Add Todo");
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            // instantiate the AddTodoDialogListener to send events back
            listener = (AddTodoDialogListener) context;
        } catch (ClassCastException e) {
            // activity doesn't implement the interface
            throw new ClassCastException(context.toString()
                    + " must implement AddTodoDialogListener");
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.cancelButton:

                dismiss();
                break;
            case R.id.saveButton:

                Log.i("TITLE TO SAVE = ", titleEditText.getText().toString());
                Log.i("DESCRIPTION TO SAVE = ", descriptionEditText.getText().toString());

                AddTodoDialogListener activity = (AddTodoDialogListener) getActivity();
                ToDo todo = new ToDo("", "", "", 0);

                String month = String.valueOf(datePicker.getMonth()+1);
                String year = String.valueOf(datePicker.getYear());
                String day = String.valueOf(datePicker.getDayOfMonth());
                String dateAsString = month + "/" + day + "/" + year;

                todo.setTitle(titleEditText.getText().toString());
                todo.setDescription(descriptionEditText.getText().toString());
                todo.setDate(dateAsString);
                todo.setStatus(0);

                activity.onFinishEditDialog(todo);

                dismiss();
                break;
        }
    }

}