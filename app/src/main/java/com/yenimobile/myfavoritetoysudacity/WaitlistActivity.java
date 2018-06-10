package com.yenimobile.myfavoritetoysudacity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.yenimobile.myfavoritetoysudacity.someUtils.TestUtils;
import com.yenimobile.myfavoritetoysudacity.sqlitefiles.GuestlistAdapter;
import com.yenimobile.myfavoritetoysudacity.sqlitefiles.WaitlistContract;
import com.yenimobile.myfavoritetoysudacity.sqlitefiles.WaitlistDBHelper;

import java.io.IOException;

public class WaitlistActivity extends AppCompatActivity {

    private SQLiteDatabase mDb;
    private GuestlistAdapter mAdapter;
    private RecyclerView mRV;

    private EditText mGuestNameET, mPartysizeET;
    private Button mAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waitlist);

        mGuestNameET = findViewById(R.id.person_name_edit_text);
        mPartysizeET = findViewById(R.id.party_count_edit_text);
        mAddButton = findViewById(R.id.add_to_waitlist_button);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToWaitList();
            }
        });

        mRV = findViewById(R.id.guestlist_all_guest_rv);
        mRV.setLayoutManager(new LinearLayoutManager(this));
        mRV.setHasFixedSize(true);

        WaitlistDBHelper dbHelper = new WaitlistDBHelper(this);
        mDb = dbHelper.getWritableDatabase();
        TestUtils.insertFakeData(mDb);
        Cursor cursor = getAllGuests();


        mAdapter = new GuestlistAdapter(this, cursor);


        mRV.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT |ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction == ItemTouchHelper.LEFT){
                   long databaseEntryId = (long) viewHolder.itemView.getTag();
                   removeGuest(databaseEntryId);
                   mAdapter.swpaCursor(getAllGuests());
                }else {
                    mAdapter.swpaCursor(getAllGuests());
                }

            }
        }).attachToRecyclerView(mRV);



    }


    private Cursor getAllGuests(){
       return mDb.query(
               WaitlistContract.WaitlistEntry.TABLE_NAME,
               null,
               null,
               null,
               null,
               null,
               WaitlistContract.WaitlistEntry.COLUMN_TIMESTAMP
       );
    }



    public void addToWaitList(){
        if (mGuestNameET.getText().toString().isEmpty() && mPartysizeET.getText().toString().isEmpty())
            return;

        int partysize = 1;
        try {
            partysize = Integer.parseInt(mPartysizeET.getText().toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        addNewGuest(mGuestNameET.getText().toString(), partysize);
        mAdapter.swpaCursor(getAllGuests());
        mGuestNameET.clearFocus(); mGuestNameET.getText().clear(); mPartysizeET.getText().clear();
    }

    private long addNewGuest(String name, int partysize){
        ContentValues cv = new ContentValues();
        cv.put(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME, name);
        cv.put(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE, partysize);

        return mDb.insert(WaitlistContract.WaitlistEntry.TABLE_NAME, null, cv);
    }

    private boolean removeGuest(long id){
        return mDb.delete(
                WaitlistContract.WaitlistEntry.TABLE_NAME,
                WaitlistContract.WaitlistEntry._ID + "=" + id,
                null
        ) > 0;

    }



}






















