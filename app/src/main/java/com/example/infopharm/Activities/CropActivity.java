package com.example.infopharm.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.infopharm.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.UUID;

public class CropActivity extends AppCompatActivity {

    String sourceUri, destinationuri;
    Uri uriNya;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        Intent intent = getIntent();
        if(intent.getExtras() != null){
            sourceUri = intent.getStringExtra("DataGambar");
            uriNya = Uri.parse(sourceUri);
        }

        destinationuri = new StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString();

        UCrop.Options options = new UCrop.Options();
        UCrop.of(uriNya, Uri.fromFile(new File(getCacheDir(), destinationuri)))
                .withOptions(options)
                .withAspectRatio(1,1)
                .withMaxResultSize(2000, 2000)
                .start(CropActivity.this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);

            Intent intent = new Intent();
            intent.putExtra("CROP", resultUri + "");
            setResult(101, intent);
            finish();

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

}