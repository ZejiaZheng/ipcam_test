package com.zejiazheng.ipcam_test;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private String ImageUrl = "http://192.168.1.66:8080/video";
    public WebView wv;
    public Button btn_convert, btn_stop;
    public TextView imageView;
    public boolean stop;

    Timer timer;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wv = (WebView)findViewById(R.id.webview);
        imageView = (TextView)findViewById(R.id.imageview);

        wv.getSettings().setJavaScriptEnabled(true);

        wv.loadUrl(ImageUrl);

        btn_convert = (Button)findViewById(R.id.button);
        btn_convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Thread cThread = new Thread(new ClientThread());
                        cThread.start();
                    }
                }, 0, 1000);

                /*Bitmap bm = Bitmap.createBitmap(wv.getMeasuredWidth(),
                        wv.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

                stop = false;
                Canvas bigcanvas = new Canvas(bm);
                Paint paint = new Paint();
                int iHeight = bm.getHeight();
                bigcanvas.drawBitmap(bm, 0, iHeight, paint);
                wv.draw(bigcanvas);
                imageView.setImageBitmap(toGrayscale(bm));*/

                //while(!stop){
                    // TODO: 3/9/16 add timer!
                    //wv.draw(bigcanvas);
                    //imageView.setImageBitmap(toGrayscale(bm));
                //}*/
            }
        });

        btn_stop = (Button)findViewById(R.id.button_stop);
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timer != null) {
                    timer.cancel();
                }
            }
        });
    }

    public class ClientThread implements Runnable {
        public void run(){
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bm = Bitmap.createBitmap(wv.getMeasuredWidth(),
                            wv.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

                    stop = false;
                    Canvas bigcanvas = new Canvas(bm);
                    Paint paint = new Paint();
                    int iHeight = bm.getHeight();
                    bigcanvas.drawBitmap(bm, 0, iHeight, paint);
                    count += 1;
                    imageView.setText(Integer.toString(count));
                    //stop = true;
                    //wv.draw(bigcanvas);
                    //imageView.setImageBitmap(toGrayscale(bm));
                }
            });
        }
    }

    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }
}
