package com.example.lucas.cameraapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.internal.widget.ContentFrameLayout;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;


public class Editor extends Activity {

    public static final String TAG = Editor.class.getSimpleName();
    private SeekBar adjustSlider;
    private Button saveButton;
    private Button cancelButton;
    private ImageView imageView;
    private Bitmap freshBitmap;
    private Bitmap editiedBitmap;
    private TextView instructionsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        registerForContextMenu(findViewById(R.id.feature_menu));
        Intent startEditorIntent = getIntent();
        freshBitmap = startEditorIntent.getParcelableExtra(getString(R.string.image_view_title));
        editiedBitmap = freshBitmap;
        imageView = (ImageView) findViewById(R.id.image_view);
        imageView.setImageBitmap(freshBitmap);
        instructionsTextView = (TextView) findViewById(R.id.instructions_text);
        saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveImageTask saveImageTask = new SaveImageTask();
                saveImageTask.doInBackground();
            }
        });
        cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageBitmap(freshBitmap);
                editiedBitmap = freshBitmap;
            }
        });
        adjustSlider = (SeekBar) findViewById(R.id.adjust_bar);
        //We can have the backend edit the images as the progress is changing (while the user is moving the slider)
        //or after the progress changes (user finishes moving slider)
        //fist method will look cooler since user can see changes in real time but will take longer
        adjustSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //If first method call image editing functions here
                //pass edited Bitmap
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //do any initialization needed for backend functions here
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //If second method call image editing functions here
                adjustSlider.setVisibility(View.INVISIBLE);
                instructionsTextView.setVisibility(View.VISIBLE);
            }
        });
        adjustSlider.setVisibility(View.INVISIBLE);
    }

    //I couldn't remember which editing features we were using so feel free to add/delete as necessary
    //Remember to also adjust if statment in onContextItemSelected
    //Lucas-- made some adjustments here to match filters, still working on tint TODO
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(getString(R.string.edit_options_title));
        menu.add(0, v.getId(), 0, getString(R.string.color));
        // menu.add(0, v.getId(), 0, getString(R.string.opacity));
        //menu.add(0, v.getId(), 0, getString(R.string.temperature));
        menu.add(0, v.getId(), 0, getString(R.string.tint));
        menu.add(0, v.getId(), 0, getString(R.string.contrast));
        menu.add(0, v.getId(), 0, getString(R.string.brightness));
        menu.add(0, v.getId(), 0, getString(R.string.blurring));
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == getString(R.string.color)) {
            instructionsTextView.setVisibility(View.INVISIBLE);
            adjustSlider.setVisibility(View.VISIBLE);
            adjustSlider.setMax(100);
            adjustSlider.setProgress(50);
        }
        /*
        else if (item.getTitle() == getString(R.string.opacity)) {
            instructionsTextView.setVisibility(View.INVISIBLE);
            adjustSlider.setVisibility(View.VISIBLE);
            adjustSlider.setMax(100);
            adjustSlider.setProgress(50);
        }

        else if (item.getTitle() == getString(R.string.temperature)) {
            instructionsTextView.setVisibility(View.INVISIBLE);
            adjustSlider.setVisibility(View.VISIBLE);
            adjustSlider.setMax(100);
            adjustSlider.setProgress(50);
        }
*/
        else if (item.getTitle() == getString(R.string.brightness)) {
            instructionsTextView.setVisibility(View.INVISIBLE);
            adjustSlider.setVisibility(View.VISIBLE);
            adjustSlider.setMax(100);
            adjustSlider.setProgress(50);
        }
        else if (item.getTitle() == getString(R.string.tint)) {
            instructionsTextView.setVisibility(View.INVISIBLE);
            adjustSlider.setVisibility(View.VISIBLE);
            adjustSlider.setMax(100);
            adjustSlider.setProgress(50);
        }
        else if (item.getTitle() == getString(R.string.contrast)) {
            instructionsTextView.setVisibility(View.INVISIBLE);
            adjustSlider.setVisibility(View.VISIBLE);
            adjustSlider.setMax(100);
            adjustSlider.setProgress(50);
        }

        else if (item.getTitle() == getString(R.string.blurring)) {
            instructionsTextView.setVisibility(View.INVISIBLE);
            adjustSlider.setVisibility(View.VISIBLE);
            adjustSlider.setMax(100);
            adjustSlider.setProgress(50);
        }
        else {
            return false;
        }
        return true;
    }

    private class SaveImageTask extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... params) {
            Calendar calendar = Calendar.getInstance();
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                //creates a file named with default image name and date, hour minute
                String fileName =  "" + R.string.file_name
                        + calendar.get(Calendar.DAY_OF_MONTH)
                        + calendar.get(Calendar.HOUR_OF_DAY)
                        + calendar.get(Calendar.MINUTE);
                File file = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), fileName);
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] buffer = new byte[2048];
                    fos.write(buffer);
                    fos.close();
                } catch (IOException e){
                    Toast.makeText(Editor.this, "Sorry, We Could Not Access Your File!", Toast.LENGTH_SHORT).show();
                }
            }
            return null;
        }
    }

}
