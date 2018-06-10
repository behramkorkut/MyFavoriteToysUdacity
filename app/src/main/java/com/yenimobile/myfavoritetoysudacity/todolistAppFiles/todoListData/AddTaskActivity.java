package com.yenimobile.myfavoritetoysudacity.todolistAppFiles.todoListData;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.yenimobile.myfavoritetoysudacity.R;

public class AddTaskActivity extends AppCompatActivity {

    // Declare a member variable to keep track of a task's selected mPriority
    private int mPriority;
    private RadioButton mRadio1, mRadio2, mRadio3;
    private EditText mEditTask;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        mEditTask = findViewById(R.id.editTextTaskDescription);

        // Initialize to highest mPriority by default (mPriority = 1)
        mRadio1 = findViewById(R.id.radButton1);
        mRadio1.setChecked(true);
        mPriority = 1;
    }


    /**
     * onClickAddTask is called when the "ADD" button is clicked.
     * It retrieves user input and inserts that new task data into the underlying database.
     */
    public void onClickAddTask(View view) {
        String taskString = mEditTask.getText().toString();
        if (taskString.isEmpty()){
            return;
        }

        ContentValues cv = new ContentValues();
        cv.put(TaskContract.TaskEntry.COLUMN_DESCRIPTION, taskString);
        cv.put(TaskContract.TaskEntry.COLUMN_PRIORITY, mPriority);

        Uri uri = getContentResolver().insert(TaskContract.TaskEntry.CONTENT_URI, cv);
        if (uri != null){
            Toast.makeText(this, "the uri is : "+ uri, Toast.LENGTH_LONG).show();
        }

        finish();
    }


    /**
     * onPrioritySelected is called whenever a priority button is clicked.
     * It changes the value of mPriority based on the selected button.
     */
    public void onPrioritySelected(View view) {
        if (((RadioButton) findViewById(R.id.radButton1)).isChecked()) {
            mPriority = 1;
        } else if (((RadioButton) findViewById(R.id.radButton2)).isChecked()) {
            mPriority = 2;
        } else if (((RadioButton) findViewById(R.id.radButton3)).isChecked()) {
            mPriority = 3;
        }
    }
}
