package com.yenimobile.myfavoritetoysudacity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ToyimplicitActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mOpenwebpage, mOpenMap, mShareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toyimplicit);

        mOpenwebpage = findViewById(R.id.implicit_openwebpage);
        mOpenMap = findViewById(R.id.implicit_openmap);
        mShareButton = findViewById(R.id.implicit_sharebutton);
        mOpenwebpage.setOnClickListener(this);
        mOpenMap.setOnClickListener(this);
        mShareButton.setOnClickListener(this);
    }

    private void openWebpage(String urlString){
        Uri webpageUri = Uri.parse(urlString);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpageUri);
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }

    private void openMap(String locationString){
        Uri geolocation = Uri.parse("geo:0,0").buildUpon()
                .appendQueryParameter("q", locationString)
                .appendQueryParameter("z", "10")
                .build();
        /*Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("geo")
                .path("0,0")
                .query(locationString)
                .appendQueryParameter("z", "10");
        Uri geolocation = uriBuilder.build();*/
        Log.i("Toyimplicit", "the full uri is " + geolocation.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geolocation);
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }

    private void onClickShareButton(View view){
        String mimetype = "text/plain";
        String title = "learning how to share";
        String textToShare = "Hello share!";

        ShareCompat.IntentBuilder.from(this)
                .setChooserTitle(title)
                .setType(mimetype)
                .setText(textToShare)
                .startChooser();
    }

    @Override
    public void onClick(View view) {
        if (view == mOpenwebpage){
            Toast.makeText(this, "button pressed", Toast.LENGTH_SHORT).show();
            openWebpage("http://www.google.com");
        }else if (view == mOpenMap){
            openMap("30 rue des poissonniers, Paris, France");
        }else if (view == mShareButton){
            onClickShareButton(view);
        }
    }
}
