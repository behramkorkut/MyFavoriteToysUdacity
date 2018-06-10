package com.yenimobile.myfavoritetoysudacity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yenimobile.myfavoritetoysudacity.data.DroidTermsExampleContract;

public class QuizcontentproviderActivity extends AppCompatActivity {

    //----------------------------------------------------------------------------------------------------------
    private Cursor mData;
    private int mCurrentState;
    private Button mButton;
    private TextView mDefinitionTV, mWordTV;

    private int mWordCol, mDefCol;

    // This state is when the word definition is hidden and clicking the button will therefore
    // show the definition
    private final int STATE_HIDDEN = 0;

    // This state is when the word definition is shown and clicking the button will therefore
    // advance the app to the next word
    private final int STATE_SHOWN = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizcontentprovider);

        // Get the views
        mDefinitionTV = findViewById(R.id.text_view_definition);
        mWordTV = findViewById(R.id.text_view_word);
        mButton = findViewById(R.id.button_next);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Either show the definition of the current word, or if the definition is currently
                // showing, move to the next word.
                switch (mCurrentState) {
                    case STATE_HIDDEN:
                        showDefinition();
                        break;
                    case STATE_SHOWN:
                        nextWord();
                        break;
                }
            }
        });

        new ContentProviderAsyncTask().execute();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // COMPLETED (5) Remember to close your cursor!
        mData.close();
    }

    public class ContentProviderAsyncTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(DroidTermsExampleContract.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            // COMPLETED (2) Initialize anything that you need the cursor for, such as setting up
            // the screen with the first word and setting any other instance variables

            //Set up a bunch of instance variables based off of the data

            // Set the data for MainActivity
            mData = cursor;
            // Get the column index, in the Cursor, of each piece of data
            mDefCol = mData.getColumnIndex(DroidTermsExampleContract.COLUMN_DEFINITION);
            mWordCol = mData.getColumnIndex(DroidTermsExampleContract.COLUMN_WORD);

            // Set the initial state
            nextWord();
        }
    }

    public void nextWord() {

        if (mData != null) {
            // Move to the next position in the cursor, if there isn't one, move to the first
            if (!mData.moveToNext()) {
                mData.moveToFirst();
            }
            // Hide the definition TextView
            mDefinitionTV.setVisibility(View.INVISIBLE);

            // Change button text
            mButton.setText(getString(R.string.show_definition));

            // Get the next word
            mWordTV.setText(mData.getString(mWordCol));
            mDefinitionTV.setText(mData.getString(mDefCol));

            mCurrentState = STATE_HIDDEN;

        }
    }

    public void showDefinition () {
        // COMPLETED (4) Show the definition
        if (mData != null) {
            // Show the definition TextView
            mDefinitionTV.setVisibility(View.VISIBLE);

            // Change button text
            mButton.setText(getString(R.string.next_word));

            mCurrentState = STATE_SHOWN;
        }
    }


}
