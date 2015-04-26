package com.example.lucas.cameraapp;

        import android.app.Activity;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.os.Bundle;
        import android.provider.MediaStore;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.RadioButton;
        import android.widget.SeekBar;
        import android.widget.Toast;

        import java.io.FileNotFoundException;
        import java.io.IOException;

/**
 * Created by Zeming Wu on 4/25/2015.
 */
public class photoedit extends Filters {

    private Bitmap image; // this is the bitmap file of the image that needs to be edited
    private Bitmap edited;
    private int intensity;
    private SeekBar slider;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photoedit);

// load image from previous activity
        int requestID = getIntent().getExtras().getInt(main.TAG_PROCEED);
        if (requestID == 1){ // 1 is takePhoto
            try {
                // fot the captured image to display in the middle, ready for filters
                image = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),main.capturedImageUri);
                ImageView imageTobeEdited = (ImageView) findViewById(R.id.photoInEditor);
                imageTobeEdited.setImageBitmap(image);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
        }else{
            // fot the selected image from gallery to display in the middle, ready for filters
            Intent getSelectedImageIntent = getIntent();
            image = (Bitmap) getSelectedImageIntent.getParcelableExtra("UserSelectedImage");
            ImageView imageTobeEdited = (ImageView) findViewById(R.id.photoInEditor);
            imageTobeEdited.setImageBitmap(image);
        }

// SeekBar, the slider, to get the integer as intensity parameter for filters
        slider = (SeekBar) findViewById(R.id.sliderBar);
        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                intensity = progress+1;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(), String.valueOf(intensity) + "/" + slider.getMax(), Toast.LENGTH_SHORT).show();
            }
        });

        Button save = (Button) findViewById(R.id.buttonSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO save the new image bitmap onto cell phone
            }
        });
    }



    // applying  filters
    public Bitmap selectFilters(View v,float value,float C,Float B, Bitmap original){
        boolean checked = ((RadioButton) v).isChecked();
        Bitmap filtered;
        switch (v.getId()){
            case R.id.buttonSaturationFilter:
                filtered = SaturationFilter(original,value);
                return filtered;

            case R.id.buttonBlur:
                //TODO: implement the filter
                break;
            case R.id.buttonBrightness:
                filtered = ContrastBrightness( original, C, B);
                return filtered;

            case R.id.buttonChannelMixer:
                filtered=invert(original);
                return filtered;

        }
        return original;
    }



// SAVE Button



}