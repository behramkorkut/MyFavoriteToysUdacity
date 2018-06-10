package com.yenimobile.myfavoritetoysudacity;


import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.yenimobile.myfavoritetoysudacity.todolistAppFiles.todoListData.AddTaskActivity;
import com.yenimobile.myfavoritetoysudacity.todolistAppFiles.todoListData.CustomCursorAdapter;
import com.yenimobile.myfavoritetoysudacity.todolistAppFiles.todoListData.TaskContract;

public class BuildingcontentproviderActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    // Constants for logging and referring to a unique loader
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int TASK_LOADER_ID = 0;

    // Member variables for the adapter and RecyclerView
    private CustomCursorAdapter mAdapter;
    RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buildingcontentprovider);

        // Set the RecyclerView to its corresponding view
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewTasks);

        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new CustomCursorAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                if (swipeDir == ItemTouchHelper.LEFT){
                    int idTodelete = (int) viewHolder.itemView.getTag();
                    String idString = String.valueOf(idTodelete);
                    Uri uri = TaskContract.TaskEntry.CONTENT_URI.buildUpon()
                            .appendPath(idString).build();
                    int deleteInt = getContentResolver().delete(uri, null, null);
                    //int numberOfItemDeleted = getContentResolver().delete(TaskContract.TaskEntry.CONTENT_URI,TaskContract.TaskEntry._ID + "=" + idTodelete, null);
                    getSupportLoaderManager().restartLoader(TASK_LOADER_ID,
                            null,
                            BuildingcontentproviderActivity.this);
                }else {
                    getSupportLoaderManager().restartLoader(TASK_LOADER_ID,
                            null,
                            BuildingcontentproviderActivity.this);
                }
            }
        }).attachToRecyclerView(mRecyclerView);

        /*
         Set the Floating Action Button (FAB) to its corresponding View.
         Attach an OnClickListener to it, so that when it's clicked, a new intent will be created
         to launch the AddTaskActivity.
         */
        FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.fab);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new intent to start an AddTaskActivity
                Intent addTaskIntent = new Intent(BuildingcontentproviderActivity.this, AddTaskActivity.class);
                startActivity(addTaskIntent);
            }
        });

        /*
         Ensure a loader is initialized and active. If the loader doesn't already exist, one is
         created, otherwise the last created loader is re-used.
         */
        //getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
        initTheLoader();
    }


    private void initTheLoader(){
        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
    }


    /**
     * This method is called after this activity has been paused or restarted.
     * Often, this is after new data has been inserted through an AddTaskActivity,
     * so this restarts the loader to re-query the underlying data for any changes.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // re-queries for all tasks
        getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }

//-------------------------------------AsyncTask Loader---------------------------------------------
    /**
     * Instantiates and returns a new AsyncTaskLoader with the given ID.
     * This loader will return task data as a Cursor or null if an error occurs.
     *
     * Implements the required callbacks to take care of loading data at all stages of loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the task data
            Cursor mTaskData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI,
                            null,null, null, TaskContract.TaskEntry.COLUMN_PRIORITY);

                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }

            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };

    }


    /**
     * Called when a previously created loader has finished its load.
     *
     * @param loader The Loader that has finished.
     * @param data The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update the data that the adapter uses to create ViewHolders
        mAdapter.swapCursor(data);
    }


    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.
     * onLoaderReset removes any references this activity had to the loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
//------------------------------end of AsyncTaskLoader---------------------------------------------


//------------------------------menu for the filtering---------------------------------------------


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tesk_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.tesk_menu_filter_important:
                mAdapter.swapCursor(getPriorityTasksFor(1));
                break;
            case R.id.tesk_menu_filter_medium:
                mAdapter.swapCursor(getPriorityTasksFor(2));
                break;
            case R.id.tesk_menu_filter_low:
                mAdapter.swapCursor(getPriorityTasksFor(3));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private Cursor getPriorityTasksFor(int priority){

        Cursor cursor = getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI,
                null,
                TaskContract.TaskEntry.COLUMN_PRIORITY + "=" + priority,
                null,
                null);

        return cursor;
    }
}
