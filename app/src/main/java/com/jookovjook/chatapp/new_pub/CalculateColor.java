package com.jookovjook.chatapp.new_pub;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.IOException;

class CalculateColor implements Runnable {

    private String uri;
    private Context context;
    private CalculateColorCallback callback;

    interface CalculateColorCallback{
        void onColorCalculated(int color);
    }

    CalculateColor(String uri, Context context, CalculateColorCallback callback){
        this.uri = uri;
        this.context = context;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.fromFile(new File(uri)));
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            if (h >= w) {
                bitmap = Bitmap.createBitmap(bitmap, 0, (int) ((h - w) / 2.0), (int) (w / 5.0), (int) (w / 5.0));
            } else {
                bitmap = Bitmap.createBitmap(bitmap, (int) ((w - h) / 2.0), 0, (int) (h / 5.0), (int) (h / 5.0));
            }
            int redColors = 0;
            int greenColors = 0;
            int blueColors = 0;
            int pixelCount = 0;
            w = bitmap.getWidth();
            h = bitmap.getHeight();
            float speed = 24f;
            Log.i("wh", String.valueOf(w));
            for (int y = 0; y < (h/speed); y++) {
                for (int x = 0; x < (w/speed); x++) {
                    int c = bitmap.getPixel((int) (speed*x), (int) (speed*y));
                    pixelCount++;
                    redColors += Color.red(c);
                    greenColors += Color.green(c);
                    blueColors += Color.blue(c);
                }
            }
            int red = (redColors / pixelCount);
            int green = (greenColors / pixelCount);
            int blue = (blueColors / pixelCount);

            float gray = (red + green + blue);
            Log.i("gray", String.valueOf(gray));
            if(gray < 383)
            {
                callback.onColorCalculated(1);
                //color = 1;
            }else {
                callback.onColorCalculated(2);
                //color = 2;
            }

        } catch (IOException e) {
            callback.onColorCalculated(0);
            e.printStackTrace();
        }

    }

}
