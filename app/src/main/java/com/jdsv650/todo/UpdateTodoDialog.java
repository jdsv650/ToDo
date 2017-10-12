package com.jdsv650.todo;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by james on 10/7/17.
 */


public class UpdateTodoDialog extends DialogFragment implements View.OnClickListener {

     public interface UpdateTodoDialogListener {
            void onFinishEditDialog(ToDo todo);
     }

    private EditText titleEditText;
    private EditText descriptionEditText;
    private DatePicker datePicker;
    private UpdateTodoDialogListener listener;

    public UpdateTodoDialog() {
        // Empty constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_todo, container);
        titleEditText = (EditText) view.findViewById(R.id.update_title_editText);
        descriptionEditText = (EditText) view.findViewById(R.id.update_description_editText);
        datePicker = (DatePicker) view.findViewById(R.id.update_datePicker);

        String theTitle = getArguments().getString("title");
        titleEditText.setText(theTitle);

        String theDesc = getArguments().getString("description");
        descriptionEditText.setText(theDesc);

        String theDate = getArguments().getString("date");

        // 11/11/2008 - our format
        //datePicker.updateDate(2016, 5, 22);

        String[] dateArr = theDate.split("/");

        try{
            if (dateArr.length >= 3)
            {
                datePicker.updateDate(Integer.parseInt(dateArr[2]), Integer.parseInt(dateArr[0]) - 1, Integer.parseInt(dateArr[1]));
            }

        }
        catch (NumberFormatException ex)
        {
        }

        ((Button) view.findViewById(R.id.update_cancelButton)).setOnClickListener(this);
        ((Button) view.findViewById(R.id.update_saveButton)).setOnClickListener(this);

        getDialog().setTitle("Update Todo");
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            // instantiate the AddTodoDialogListener to send events back
            listener = (UpdateTodoDialogListener) context;
        } catch (ClassCastException e) {
            // activity doesn't implement the interface
            throw new ClassCastException(context.toString()
                    + " must implement UpdateTodoDialogListener");
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.update_cancelButton:

                Log.i("HEYNOWWWWWWWW", "CANCEL");
                dismiss();
                break;
            case R.id.update_saveButton:

                Log.i("TITLE TO SAVE = ", titleEditText.getText().toString());
                Log.i("DESCRIPTION TO SAVE = ", descriptionEditText.getText().toString());

                UpdateTodoDialogListener activity = (UpdateTodoDialogListener) getActivity();

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