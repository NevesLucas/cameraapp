package com.example.lucas.cameraapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Handler;

/**
 * Created by Zeming Wu on 4/25/2015.
 */
public class photoedit extends Filters {
    public static final String TAG = photoedit.class.toString();

    private Bitmap image; // this is the bitmap file of the image that needs to be edited
    private float parameter1=1;
    private float parameter2=1;
    private float parameter3=1;
    private Bitmap edited;
    private SeekBar slider1;
    private SeekBar slider2;
    private SeekBar slider3;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private String selectedFilterName;
    private Handler handler;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photoedit);
// TextView, left tot the bar, indicating the scale of parameter
        tv1 = (TextView) findViewById(R.id.tv_sliderBar1);
        tv2 = (TextView) findViewById(R.id.tv_sliderBar2);
        tv3 = (TextView) findViewById(R.id.tv_sliderBar3);

// load image from previous activity
        int requestID = getIntent().getExtras().getInt(main.TAG_PROCEED);
        if (requestID == 1){ // 1 is takePhoto
            try {
                // fot the captured image to display in the middle, ready for filters
                image = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),main.capturedImageUri);
                ImageView display = (ImageView) findViewById(R.id.photoInEditor);
                display.setImageBitmap(image);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
        }else if (requestID == 2){
            // fot the selected image from gallery to display in the middle, ready for filters

           Intent getSelectedImageIntent = getIntent();
            String image_path = getSelectedImageIntent.getStringExtra("path");
;
            image = (Bitmap) BitmapFactory.decodeFile(image_path);
            ImageView display = (ImageView) findViewById(R.id.photoInEditor);
            display.setImageBitmap(image);

        } else {
            Intent getSelectedImageIntent = getIntent();
            image = (Bitmap) getSelectedImageIntent.getParcelableExtra("DefaultImage");
            ImageView display = (ImageView) findViewById(R.id.photoInEditor);
            display.setImageBitmap(image);
        }

// SeekBar1, the slider, to get the integer as intensity parameter for filters
        slider1 = (SeekBar) findViewById(R.id.sliderBar1);
        slider1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                parameter1 = (float) progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tv1.setText(String.valueOf((int)(parameter1))+"/" + slider1.getMax());
                ImageView display = (ImageView) findViewById(R.id.photoInEditor);
                //Toast.makeText(getApplicationContext(), String.valueOf(parameter1) + "/" + slider1.getMax(), Toast.LENGTH_SHORT).show();
                switch (selectedFilterName){
                    case "Contrast and Brightness":
                        edited = selectFilters(parameter1,parameter2,parameter3,image);
                        display.setImageBitmap(edited);
                        break;
                    case "Saturation":
                        edited = selectFilters(parameter1,parameter2,parameter3,image);
                        display.setImageBitmap(edited);
                        break;
                }
            }
        });



// SeekBar2, the slider, to get the integer as intensity parameter for filters
        slider2 = (SeekBar) findViewById(R.id.sliderBar2);
        slider2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                parameter2 = (float) progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tv2.setText(String.valueOf((int)(parameter2))+"/" + slider2.getMax());
                ImageView display = (ImageView) findViewById(R.id.photoInEditor);
               // Toast.makeText(getApplicationContext(), String.valueOf(parameter2) + "/" + slider2.getMax(), Toast.LENGTH_SHORT).show();
                switch (selectedFilterName){
                    case "Saturation":
                        edited = selectFilters(parameter1,parameter2,parameter3,image);
                        display.setImageBitmap(edited);
                        break;
                    case "Contrast and Brightness":
                        edited = selectFilters(parameter1,parameter2,parameter3,image);
                        display.setImageBitmap(edited);
                        break;
                    case "Channel Mixer":
                        edited = selectFilters(parameter1,parameter2,parameter3,image);
                        display.setImageBitmap(edited);
                        break;
                    case "Blur":
                        edited = selectFilters(parameter1,parameter2,parameter3,image);
                        display.setImageBitmap(edited);
                        break;

                }
            }
        });



// SeekBar3, the slider, to get the integer as intensity parameter for filters
        slider3 = (SeekBar) findViewById(R.id.sliderBar3);
        slider3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                parameter3 = (float) progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tv3.setText(String.valueOf((int) (parameter3)) + "/" + slider3.getMax());
                //Toast.makeText(getApplicationContext(), String.valueOf(parameter3) + "/" + slider3.getMax(), Toast.LENGTH_SHORT).show();
                switch (selectedFilterName) {
                    case "Channel Mixer":
                        edited = selectFilters(parameter1, parameter2, parameter3, image);
                        ImageView display = (ImageView) findViewById(R.id.photoInEditor);
                        display.setImageBitmap(edited);
                }
            }
        });



// Spinner: choose filters

        spinner = (Spinner) findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.filters, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedFilterName = spinner.getSelectedItem().toString();
                if(selectedFilterName.equals("Blur")){
                    slider2.setProgress(0);
                }
                showSliderBars(selectedFilterName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


// SAVE Button
        Button save = (Button) findViewById(R.id.buttonSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calendar calendar = Calendar.getInstance();
                //String fileName = Environment.getExternalStorageDirectory()+(calendar.getTimeInMillis()+".jpg");
                storeImage(edited);
            }
        });

// RESTORE Button: to display the original bitmap image
        Button restore = (Button) findViewById(R.id.buttonRestore);
        restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView display = (ImageView) findViewById(R.id.photoInEditor);
                display.setImageBitmap(image);
                edited = image;
                resetSliders();
            }
        });
        // when RESTORE is hit, the slider bars should be reset as well
        resetSliders();
    }

    //Resets sliders to default values. Zero for blur, 76 (half) for others
    void resetSliders(){
        if(selectedFilterName != null && selectedFilterName.equals("Blur")) {
            slider2.setProgress(0);
        } else {
            slider2.setProgress(76);
        }
        slider1.setProgress(76);
        slider3.setProgress(76);
    }
    String mCurrentPhotoPath;

    public File storeImageHelp() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".JPG",         /* suffix */
                storageDir      /* directory */
        );
        MediaScannerConnection.scanFile(this, new String[]{image.getPath()}, new String[]{"image/jpeg"}, null);

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
// Helper function to save bitmap
    private boolean storeImage(Bitmap imageData) {

        //get path to external storage (SD card)
     //   String iconsStoragePath = Environment.getExternalStorageDirectory() + "/myAppDir/myImages/";
        try {
            File sdIconStorageDir = storeImageHelp();

            //create storage directories, if they don't exist
            //sdIconStorageDir.mkdirs();

            //could put this code in the async task if task works
            FileOutputStream fileOutputStream = new FileOutputStream(mCurrentPhotoPath);

            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);

            //choose another format if PNG doesn't suit you
            imageData.compress(Bitmap.CompressFormat.JPEG, 100, bos);

            bos.flush();
            bos.close();
            fileOutputStream.flush();
            fileOutputStream.close();

            //Executes async task
            //new UpdateGallery().execute(sdIconStorageDir);

            Toast.makeText(getApplicationContext(),"Image saved",Toast.LENGTH_LONG).show();


        } catch (FileNotFoundException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
            return false;
        } catch (IOException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
            return false;
        }

        return true;
    }

    //TODO test this method
    //function to update photo gallery. In a separate thread
    private class UpdateGallery extends AsyncTask<File, Integer, Long> {
        protected Long doInBackground(File... photos) {
            Log.d(TAG, "Do in background method called");
            //could put other code to save here
            MediaScannerConnection.scanFile(
                    photoedit.this, new String[] {photos.toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Log.d(TAG, "Scan completed " + path);
                        }
                    });
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Long result) {

        }
    }

// helper function to hide/show slider bars
    public void showSliderBars(String filterID){
        ImageView display = (ImageView) findViewById(R.id.photoInEditor);
        switch(filterID){
            case "Select a Filter":
                hide();
                Toast.makeText(getBaseContext(),"Select a Filter Type", Toast.LENGTH_SHORT);
                break;
            case "Saturation":
                hide();
                findViewById(R.id.sliderBar2).setVisibility(View.VISIBLE);
                findViewById(R.id.tv_sliderBar2).setVisibility(View.VISIBLE);
                break;
            case "Contrast and Brightness":
                hide();
                findViewById(R.id.sliderBar1).setVisibility(View.VISIBLE);
                findViewById(R.id.tv_sliderBar1).setVisibility(View.VISIBLE);
                findViewById(R.id.sliderBar2).setVisibility(View.VISIBLE);
                findViewById(R.id.tv_sliderBar2).setVisibility(View.VISIBLE);
                break;
            case "Channel Mixer":
                hide();
                findViewById(R.id.sliderBar1).setVisibility(View.VISIBLE);
                findViewById(R.id.tv_sliderBar1).setVisibility(View.VISIBLE);
                findViewById(R.id.sliderBar2).setVisibility(View.VISIBLE);
                findViewById(R.id.tv_sliderBar2).setVisibility(View.VISIBLE);
                findViewById(R.id.sliderBar3).setVisibility(View.VISIBLE);
                findViewById(R.id.tv_sliderBar3).setVisibility(View.VISIBLE);
                break;
            case "SepiaColor":
                hide();
                edited = selectFilters(parameter1,parameter2,parameter3,image);
                display.setImageBitmap(edited);
                break;
            case "Invert":
                hide();
                edited = selectFilters(parameter1,parameter2,parameter3,image);
                display.setImageBitmap(edited);
                break;
            case "Grey Scale":
                hide();
                edited = selectFilters(parameter1,parameter2,parameter3,image);
                display.setImageBitmap(edited);
                break;
            case "Blur":
                hide();
                findViewById(R.id.sliderBar2).setVisibility(View.VISIBLE);
                findViewById(R.id.tv_sliderBar2).setVisibility(View.VISIBLE);
                edited = selectFilters(parameter1,parameter2,parameter3,image);
                display.setImageBitmap(edited);
                break;
        }
    }
    public void hide(){
        findViewById(R.id.sliderBar1).setVisibility(View.INVISIBLE);
        findViewById(R.id.tv_sliderBar1).setVisibility(View.INVISIBLE);
        findViewById(R.id.sliderBar2).setVisibility(View.INVISIBLE);
        findViewById(R.id.tv_sliderBar2).setVisibility(View.INVISIBLE);
        findViewById(R.id.sliderBar3).setVisibility(View.INVISIBLE);
        findViewById(R.id.tv_sliderBar3).setVisibility(View.INVISIBLE);
    }





    // applying  filters
    public Bitmap selectFilters(float A,float B,float C, Bitmap original){
        switch (selectedFilterName){
            case "Saturation":
                return SaturationFilter(original,B);
            case "Channel Mixer":
                return ChannelMixer(original,A,B,C);
            case "Blur":
                return Blur(original,(int)parameter2);
            case "Contrast and Brightness":
                return ContrastBrightness(original, A, B);
            case "Invert":
                return invert(original);
            case "SepiaColor":
                return SepiaColor(original);
            case "Grey Scale":
                return GrayscaleColor(original);
        }
        return original;
    }

}