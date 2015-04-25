package com.example.lucas.cameraapp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class main extends Activity{

    private static final String CAMERA_LOG = "camera";
    private Uri imageUri;
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
                startActivityForResult(uploadIntent.createChooser(uploadIntent,"Select a Photo"),UPLOAD_PHOTO);

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
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        df.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()).toString();
        String photoName = "blur_pic.jpg" + df;
        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),photoName);
        imageUri = Uri.fromFile(photo);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(cameraIntent,TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode,resultCode,intent);

        if(resultCode == Activity.RESULT_OK){
            Intent resultActivity = new Intent(main.this,photoedit.class);
            startActivity(resultActivity);
            Uri selectedImage = imageUri;
            getContentResolver().notifyChange(selectedImage,null);

            ImageView imageView = (ImageView) findViewById(R.id.photoInEditor);
            ContentResolver cr = getContentResolver();
            Bitmap bitmap;

            try{
                bitmap = MediaStore.Images.Media.getBitmap(cr,selectedImage);
                imageView.setImageBitmap(bitmap);
                // Toast.makeText(main.this,selectedImage.toString(),Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                Log.e(CAMERA_LOG,e.toString());
            }
        }else if (resultCode == Activity.RESULT_OK && requestCode == UPLOAD_PHOTO) {
            Uri gallaryImageUri = intent.getData();
            InputStream inputStream;
            try {
                inputStream = getContentResolver().openInputStream(gallaryImageUri);
                // get bitmap from stream
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                ((ImageView) findViewById(R.id.photoInEditor)).setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Unable to Open Image", Toast.LENGTH_LONG).show();
            }
            // ImageView editorImage = (ImageView) findViewById(R.id.photoInEditor);
            //editorImage.setImageURI(intent.getData());
            // Intent photoEditActivity = new Intent(main.this, photoedit.class);
            // startActivity(photoEditActivity);
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
