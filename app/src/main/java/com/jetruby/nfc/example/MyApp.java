package com.jetruby.nfc.example;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MyApp extends Application {
    private ImageView img; // the image
    private ImageView editImg; // the image
    private ImageView profileImg; // the image

    private Bitmap nav; // the image in the Bitmap format
    private Bitmap editBitmap; // the image in the Bitmap format
    private Bitmap profileBitmap; // the image in the Bitmap format

    private LinearLayout bgimg; // layout of the activity
    private Bitmap background; // background in the Bitmap format
    private BitmapDrawable bg; // background in the Drawable format


    public void loadBackground(int id) {
        background = BitmapFactory.decodeStream(getResources().openRawResource(id));
        bg = new BitmapDrawable(background);
        bgimg.setBackgroundDrawable(bg);
    }
    public void unloadBackground() {
        if (bgimg != null)
            bgimg.setBackgroundDrawable(null);
        if (bg!= null) {
            background.recycle();
        }
        bg = null;
    }

    //load picture for registration picture user
    public void loadBitmapRegister(int id) {
        profileBitmap = BitmapFactory.decodeStream(getResources().openRawResource(id));
        profileImg.setImageBitmap(profileBitmap);
    }
    public void unloadBitmapRegister() {
        if (profileImg != null)
            profileImg.setImageBitmap(null);
        if (profileBitmap!= null) {
            profileBitmap.recycle();
        }
        profileBitmap = null;
    }

    //load the picture for the buton edition
    public void loadBitmapEdit(int id) {
        editBitmap = BitmapFactory.decodeStream(getResources().openRawResource(id));
        editImg.setImageBitmap(editBitmap);
    }
    public void unloadBitmapEdit(){
        if (editImg != null)
            editImg.setImageBitmap(null);
        if (editBitmap!= null) {
            editBitmap.recycle();
        }
        editBitmap = null;
    }

    //load the picture for user
    public void loadBitmap(int id) {
        nav = BitmapFactory.decodeStream(getResources().openRawResource(id));
        img.setImageBitmap(nav);
    }
    public void unloadBitmap() {
        if (img != null)
            img.setImageBitmap(null);
        if (nav!= null) {
            nav.recycle();
        }
        nav = null;
    }

    //load the background picture
    public void setBackground(LinearLayout i, int sourceid) {
        unloadBackground();
        bgimg = i;
        loadBackground(sourceid);
    }

    public void setImage(ImageView i, int sourceid) {
        unloadBitmap();
        img = i;
        loadBitmap(sourceid);
    }

    public void setImageEdit(ImageView i, int sourceid) {
        unloadBitmapEdit();
        editImg = i;
        loadBitmapEdit(sourceid);
    }

    public void setImageProfile(ImageView i, int sourceid) {
        unloadBitmapRegister();
        profileImg = i;
        loadBitmapRegister(sourceid);
    }

}