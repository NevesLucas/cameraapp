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

/**
 * Created by Lucas on 4/10/2015.
 */
public class Filters extends Activity {




    protected Bitmap process(Bitmap original, ColorMatrix colorMatrix) {
        Bitmap bitmap = Bitmap.createBitmap(
                original.getWidth(), original.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(original, 0, 0, paint);

        return bitmap;
    }
    private ColorMatrix setColorFilter(float R,float G, float B) {

     float sr=(1-R)*.3086f;
     float sg=(1-G)*.6094f;
     float sb = (1-B)*.0820f;
      ColorMatrix Cmat=  new ColorMatrix(new float[]{

                sr+R, sr, sr, 0, 0,
                sg, sg+G, sg, 0, 50,
                sb, sb, sb+B,0,0,
                  0, 0, 0, 0, 1,
                    0, 0, 0, 0, 1
        });

        return Cmat;
    }



    private ColorMatrix getSepiaColorMatrix() {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);

        ColorMatrix colorScale = new ColorMatrix();
        colorScale.setScale(1, 1, 0.8f, 1);

        // Convert to grayscale, then apply brown color
        colorMatrix.postConcat(colorScale);

        return colorMatrix;
    }

    private ColorMatrix getGrayscaleColorMatrix() {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        return colorMatrix;
    }

    //blurring effect
    protected Bitmap blur(android.graphics.Bitmap original, float radius) {
        Bitmap bitmap = android.graphics.Bitmap.createBitmap(
                original.getWidth(), original.getHeight(),
                android.graphics.Bitmap.Config.ARGB_8888);

        RenderScript RenS = RenderScript.create(this);

        Allocation allocIn = Allocation.createFromBitmap(RenS, original);
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
