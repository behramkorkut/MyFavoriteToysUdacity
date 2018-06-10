package com.yenimobile.myfavoritetoysudacity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yenimobile.myfavoritetoysudacity.utilities.NetworkUtils;
import com.yenimobile.myfavoritetoysudacity.utilities.ToyAppNetworkUtilities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ToyappActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    private static final String LOGTAG = ToyappActivity.class.getName();
    private static final String RESULTS_TOBE_INSTANCE_SAVED = "results";
    private static final String QUERY_URL_TOBE_SAVED = "queryUrl";
    private static final int UNIQUE_LOADER_ID = 22;


    private EditText mSearchBoxET;
    private TextView mUrlDisplayTV, mSearchResultsTV, mErrorMessage;
    private ProgressBar mProgressbar;
    private Button mSendDataButton;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String theQueryUrlToSave = mUrlDisplayTV.getText().toString();
        outState.putString(QUERY_URL_TOBE_SAVED, theQueryUrlToSave);
        //String theResultsTobeSaved = mSearchResultsTV.getText().toString(); //obsolete we use a loader now
        //outState.putString(RESULTS_TOBE_INSTANCE_SAVED, theResultsTobeSaved);//obsolete
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toyapp);

        mSearchBoxET = findViewById(R.id.et_search_box);
        mUrlDisplayTV = findViewById(R.id.tv_url_display);
        mSearchResultsTV = findViewById(R.id.tv_github_search_results_json);
        mErrorMessage = findViewById(R.id.error_message);mErrorMessage.setVisibility(View.INVISIBLE);
        mProgressbar = findViewById(R.id.progress_bar); mProgressbar.setVisibility(View.INVISIBLE);
        mSendDataButton = findViewById(R.id.toyapp_button_send_intent_data);

        if (savedInstanceState != null){
            if(savedInstanceState.containsKey(QUERY_URL_TOBE_SAVED)){
                mUrlDisplayTV.setText(savedInstanceState.getString(QUERY_URL_TOBE_SAVED));
                //mSearchResultsTV.setText(savedInstanceState.getString(RESULTS_TOBE_INSTANCE_SAVED));
            }
        }

        mSendDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = mSearchBoxET.getText().toString();
                if (str != null && !str.equals(" ")){
                    Intent intent = new Intent(view.getContext(), ChildtoyappActivity.class);
                    intent.putExtra(Intent.EXTRA_TEXT, str);
                    startActivity(intent);
                }
            }
        });

    }


    //------------------------------------LoaderManager callbacks-----------------------------------------------

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable final Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            private String asyncGithubJsonString;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(args == null){
                    return;
                }
                mProgressbar.setVisibility(View.VISIBLE);

                if(asyncGithubJsonString != null){
                    deliverResult(asyncGithubJsonString);
                }else {
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public String loadInBackground() {
                String queryUrlString = args.getString(QUERY_URL_TOBE_SAVED);
                if(queryUrlString == null || TextUtils.isEmpty(queryUrlString)){
                    return null;
                }

                try {
                    URL queryURL = new URL(queryUrlString);
                    String responseStr = NetworkUtils.getResponseFromHttpUrl(queryURL);
                    return responseStr;
                }catch (IOException e ){
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void deliverResult(@Nullable String data) {
                asyncGithubJsonString = data;
                super.deliverResult(data);
            }


        };
    }



    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        mProgressbar.setVisibility(View.INVISIBLE);
        if(data != null && !TextUtils.isEmpty(data) && !data.equals(" ")){
            showTheJsonData();
            mSearchResultsTV.setText(data);
        }else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
    //-------------------------------------End of LoaderManager callbacks---------------------------------------


    //-----------------------------------------The Async task -------------------------------------------------
    //the Async task
    private class GithubSearchAsync extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(LOGTAG, "preexecute triggered ----------------------------------");
            mProgressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            Log.i(LOGTAG, "do in background triggered ----------------------------------");
            URL url = urls[0];
            String githubSearchResult = null;
            try {
                githubSearchResult = ToyAppNetworkUtilities.getResponseFromHttpUrl(url);
            }catch (IOException e ){
                e.printStackTrace();
            }
            return githubSearchResult;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i(LOGTAG, "postExecute triggered ----------------------------------");
            mProgressbar.setVisibility(View.INVISIBLE);
            if (s != null && !s.equals(" ")){
                showTheJsonData();
                mSearchResultsTV.setText(s);
            }else {
                showErrorMessage();
            }

        }
    }
    //-----------------------------------------end of Async Task------------------------------------------------


    private void makeGithubSearchQuery(){
        String githubQuery = mSearchBoxET.getText().toString();
        URL githubSearchURL = ToyAppNetworkUtilities.buildUrl(githubQuery);
        mUrlDisplayTV.append(githubSearchURL.toString());


        //new GithubSearchAsync().execute(githubSearchURL); //totally obsolete call

        Bundle queryBundle = new Bundle();
        queryBundle.putString(QUERY_URL_TOBE_SAVED, githubSearchURL.toString());

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> githubLoader = loaderManager.getLoader(UNIQUE_LOADER_ID);

        if (githubLoader == null) {
            loaderManager.initLoader(UNIQUE_LOADER_ID, queryBundle, this);
        }else {
            loaderManager.restartLoader(UNIQUE_LOADER_ID, queryBundle, this);
        }

    }

    private void showTheJsonData(){
        mErrorMessage.setVisibility(View.INVISIBLE);
        mSearchResultsTV.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mErrorMessage.setVisibility(View.VISIBLE);
        mSearchResultsTV.setVisibility(View.INVISIBLE);
    }



//------------------------------------------------------------------------------------------------------
// the overrides needed for actionBar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_map:
                //Toast.makeText(this, "search button is pressed yo", Toast.LENGTH_SHORT).show();
                makeGithubSearchQuery();
                return true;
            default:
                Log.i(LOGTAG, "default pressed on actionbar");
                return super.onOptionsItemSelected(item);
        }

    }
//------------------------------------------------------------------------------------------------------

}
