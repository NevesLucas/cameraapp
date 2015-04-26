package com.example.lucas.cameraapp;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

public class Filters extends Activity {

    //applies created filter onto bitmap and returns a new bitmap with filters applied
    //BLUR FILTER DOES NOT REQUIRE THIS
    protected Bitmap process(Bitmap original, ColorMatrix colorMatrix) {
        Bitmap bitmap = Bitmap.createBitmap(
                original.getWidth(), original.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(original, 0, 0, paint);

        return bitmap;
    }
    //creates saturation filter where S is between 0 and 1.
//EXPERIMENTAL may give unexpected results
    public Bitmap SaturationFilter(Bitmap original, float S) {

        float sr=(1-S)*.3086f;
        float sg=(1-S)*.6094f;
        float sb = (1-S)*.0820f;
        ColorMatrix Cmat=  new ColorMatrix(new float[]{

                sr+S, sr, sr, 0, 0,
                sg, sg+S, sg, 0, 0,
                sb, sb, sb+S, 0, 0,
                0,   0,    0, 1, 0
        });

        return this.process(original,Cmat);
    }

    //EXPERIMENTAL RGB should be between 0 and 1
    public Bitmap ChannelMixer(Bitmap original,float R, float G, float B) {


        ColorMatrix Cmat=  new ColorMatrix(new float[]{

                R, 0, 0, 0, 0,
                0, G, 0, 0, 0,
                0, 0, B, 0, 0,
                0, 0, 0, 1, 0,
        });

        return this.process(original,Cmat);
    }
    //gets negative of image
    public Bitmap invert(Bitmap original) {


        ColorMatrix Cmat=  new ColorMatrix(new float[]{

                -1, 0, 0, 0, 255,
                0, -1, 0, 0, 255,
                0, 0, -1, 0, 255,
                0, 0,  0, 1,   0,
        });

        return this.process(original,Cmat);
    }
    //adjusts the contrast and brightness of the image
    public Bitmap ContrastBrightness(Bitmap original, float C,float B) {


        ColorMatrix Cmat=  new ColorMatrix(new float[]{

                C, 0, 0, 0, B,
                0, C, 0, 0, B,
                0, 0, C, 0, B,
                0, 0, 0, 1, 0,
        });

        return this.process(original,Cmat);
    }

    //make sepia color filter
    public Bitmap SepiaColor(Bitmap original) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);

        ColorMatrix colorScale = new ColorMatrix();
        colorScale.setScale(1, 1, 0.8f, 1);

        // Convert to grayscale, then apply brown color
        colorMatrix.postConcat(colorScale);

        return this.process(original,colorMatrix);
    }
    //make grayscale image
    public Bitmap GrayscaleColor(Bitmap original) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        return this.process(original,colorMatrix);
    }




    //THIS FILTER HAS A BUG TODO
    //blurring effect input radius for intensity of blurring effect
    protected Bitmap blur(Bitmap original, float radius) {

        Bitmap bitmap = Bitmap.createBitmap(original.getWidth(), original.getHeight(), original.getConfig());


        RenderScript RenS =  RenderScript.create(this);

        Allocation allocIn = Allocation.createFromBitmap(RenS, original, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_GRAPHICS_TEXTURE);
        Allocation allocOut = Allocation.createFromBitmap(RenS, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(RenS, Element.U8_4(RenS));

        blur.setInput(allocIn);
        blur.setRadius(radius);
        blur.forEach(allocOut);

        allocOut.copyTo(bitmap);

        RenS.destroy();

        return bitmap;
    }



}


