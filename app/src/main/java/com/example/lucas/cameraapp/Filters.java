package com.example.lucas.cameraapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Lucas on 4/10/2015.
 */
public class Filters extends Activity implements GLSurfaceView.Renderer{
    private GLSurfaceView mGLView;
    private int[] mTextures = new int[2];
    private EffectContext mEffectContext;
    private Effect mEffect;
    private TextureRenderer mTextureRenderer = new TextureRenderer();
    private int mImageWidth;
    private int mImageHeight;
    private boolean mInitialized = false;
    int mCurrentEffect;


}
