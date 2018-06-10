package com.yenimobile.myfavoritetoysudacity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.yenimobile.myfavoritetoysudacity.adapters.GreenAdapter;
import com.yenimobile.myfavoritetoysudacity.adapters.SomeAdapter;

public class SomerecyclerviewActivity extends AppCompatActivity implements SomeAdapter.SomeClickInterface {

    private static final int NUM_LIST_ITEMS = 100;
    private GreenAdapter mAdatper;
    private SomeAdapter mSomeAdapter;
    private RecyclerView mRecylerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_somerecyclerview);

        mRecylerView = findViewById(R.id.some_recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecylerView.setLayoutManager(linearLayoutManager);
        mRecylerView.setHasFixedSize(true);
        mSomeAdapter = new SomeAdapter(NUM_LIST_ITEMS, this);
        mRecylerView.setAdapter(mSomeAdapter);
    }

    //------------------------------------- Menu implementation ------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_map:
                Toast.makeText(this, "search button pressed", Toast.LENGTH_SHORT).show();
                mSomeAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
    //----------------------------------------------------------------------------------------------


    //-----------------------------------click listener interface implementation--------------------------------
    @Override
    public void onListItemClick(int index) {
        Toast.makeText(this, "the pressed item is "+ index, Toast.LENGTH_SHORT).show();
    }

    //----------------------------------------------------------------------------------------------
}
