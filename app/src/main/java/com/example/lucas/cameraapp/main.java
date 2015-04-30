package com.example.lucas.cameraapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;


public class main extends Activity{

    public static final String TAG_PROCEED = "Proceed to next activity, takePhotoID is 1, uploadPhotoID is 2";
    private static final String CAMERA_LOG = "camera activated";
    protected static Uri capturedImageUri = null;
    private static int TAKE_PHOTO = 1;
    private static int UPLOAD_PHOTO = 2;
    private static int DEFAULT = 0;
    protected static String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView logo = (ImageView) findViewById(R.id.imageView2);
        //this click listener will pass the logo image to the editor if no image is uploaded or taken
        //useful for examples, debugging, and running in emulator rather than on device
        logo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(main.this, photoedit.class);
                //decodes logo drawable as bitmap and passes to editor activity
                Bitmap icon = BitmapFactory.decodeResource(main.this.getResources(), R.drawable.blurlogo1);
                nextActivity.putExtra(TAG_PROCEED, DEFAULT);
                nextActivity.putExtra("DefaultImage", icon);
                startActivity(nextActivity);
            }
        });

        //Buttons
        Button uploadPhoto = (Button) findViewById(R.id.uploadPhoto);
        Button takePhoto = (Button) findViewById(R.id.takePhoto);
        //Broadcasts intent to android system media store to access camera app

        // this is clicklistener in case user hit UPLOAD button
        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent uploadIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(uploadIntent,UPLOAD_PHOTO);
            }
        });
        takePhoto.setOnClickListener(cameraListener);
    }

    //Called when "Take Photo" button pressed
    // this is clicklistener in case user hit TAKE PHOTO button
    private OnClickListener cameraListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            takePhoto(v);
        }
    };
    //fuction that takes a photo
    // helper function for photo-taking: create files and store in disk
    private void takePhoto(View v){
        Calendar calendar = Calendar.getInstance();
        //new jegp file created using the current tile
        File file = new File(Environment.getExternalStorageDirectory(), (calendar.getTimeInMillis()+".jpg"));
        if (!file.exists()){
            //deletes file if it already exists and creates a new file
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

        MediaScannerConnection.scanFile(this, new String[]{file.getPath()}, new String[]{"image/jpeg"}, null);

        //starts editor activity with Uri of file created
        capturedImageUri = Uri.fromFile(file);
        // moving the image into next activity
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri);
        startActivityForResult(takePhotoIntent, TAKE_PHOTO);
    }

    //called on result of intent passed to main activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        //passes take photo code to editor if activity request code is the take photo code

        // this is the case when user hit TAKE PHOTO button.
        if (resultCode == Activity.RESULT_OK && requestCode == TAKE_PHOTO) {
            Intent passImageToNextActivity = new Intent(main.this, photoedit.class);
            passImageToNextActivity.putExtra(TAG_PROCEED,TAKE_PHOTO);
            startActivity(passImageToNextActivity);

        // this is the case when user hit UPLOAD button
        } else if (resultCode == Activity.RESULT_OK && requestCode == UPLOAD_PHOTO) {
            //passes uploaded imge code to editor if request code is upload photo code
            //gets Uri of selected photo from location from which intent was broadcast
            Uri selectedImage = intent.getData();
            //generates string paths to images in the camera roll then moves through images using cursor
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imagePath = cursor.getString(columnIndex);
            cursor.close();
            Intent passImageToNextActivity = new Intent(main.this, photoedit.class);
            //generates bitmap from file path selected by user and passes to editor
            Bitmap selectedBMP = BitmapFactory.decodeFile(picturePath);
            passImageToNextActivity.putExtra(TAG_PROCEED, UPLOAD_PHOTO);
            passImageToNextActivity.putExtra("UserSelectedImage", selectedBMP);
            startActivity(passImageToNextActivity);
        }
        
    }


    public String getImagePath(){
        return imagePath;
    }

    //generated code for menu options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //generated code for menu options
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
