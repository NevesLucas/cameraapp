package com.example.lucas.cameraapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;


public class main extends Activity{

    public static final String TAG_PROCEED = "Proceed to next activity, takePhotoID is 1, uploadPhotoID is 2";
    private static final String CAMERA_LOG = "camera activated";
    protected static Uri capturedImageUri = null;
    private static int TAKE_PHOTO = 1;
    private static int UPLOAD_PHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button uploadPhoto = (Button) findViewById(R.id.uploadPhoto);
        Button takePhoto = (Button) findViewById(R.id.takePhoto);

        uploadPhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent uploadIntent = new Intent();
                uploadIntent.setType("image/*");
                uploadIntent.setAction(uploadIntent.ACTION_GET_CONTENT);
                startActivityForResult(uploadIntent.createChooser(uploadIntent, "Select a Photo"), UPLOAD_PHOTO);

            }
        });

        takePhoto.setOnClickListener(cameraListener);
    }


    private OnClickListener cameraListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            takePhoto(v);
        }
    };

    private void takePhoto(View v){
        Calendar calendar = Calendar.getInstance();
        File file = new File(Environment.getExternalStorageDirectory(), (calendar.getTimeInMillis()+".jpg"));
        if (!file.exists()){
            try{
                file.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            file.delete();
            try{
                file.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        capturedImageUri = Uri.fromFile(file);
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri);
        startActivityForResult(takePhotoIntent, TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == Activity.RESULT_OK && requestCode == TAKE_PHOTO) {
            Intent passImageToNextActivity = new Intent(main.this, photoedit.class);
            passImageToNextActivity.putExtra(TAG_PROCEED,TAKE_PHOTO);
            startActivity(passImageToNextActivity);
        } else if (resultCode == Activity.RESULT_OK && requestCode == UPLOAD_PHOTO) {
            Uri galleryImageUri = intent.getData();
            Intent passImageToNextActivity = new Intent(main.this, photoedit.class);
            InputStream inputStream;
            try {
                // a stream of data from the file
                inputStream = getContentResolver().openInputStream(galleryImageUri);
                // get bitmap from stream
                Bitmap selectedBMP = BitmapFactory.decodeStream(inputStream);
                passImageToNextActivity.putExtra(TAG_PROCEED,UPLOAD_PHOTO);
                passImageToNextActivity.putExtra("UserSelectedImage", selectedBMP);
                startActivity(passImageToNextActivity);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Unable to Open Image", Toast.LENGTH_LONG).show();

            }

        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
