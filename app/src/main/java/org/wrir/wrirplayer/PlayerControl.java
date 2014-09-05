package org.wrir.WrirPlayer;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;


import android.content.ActivityNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import org.wrir.WrirPlayer.util.BlogSiteViewer;


/**
 * Created by xha89407 on 9/3/14.
 */
public class PlayerControl extends Activity {
    public TextView songName,startTimeField,endTimeField, blogUrl;
    public ImageView poster;
    private MediaPlayer mediaPlayer;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar = null;
    private ImageButton playButton,pauseButton;
    public static int oneTimeOnly = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_control);

        //Get data to player!
        String mp3 = "";
        String title = "";
        String blogurl = "";

        Log.w("WRIR", "WRIR: creating PlayerControl");
        Bundle extras = getIntent().getExtras();
        mp3 = extras.getString("mp3");
        title = extras.getString("title");
        blogurl = extras.getString("blogurl");
        String posterurl = extras.getString("poster");
        Log.w("WRIR", "WRIR: " + mp3 + " " + title);
        Uri mp3url = Uri.parse(mp3);
        Log.w("WRIR", "WRIR: parsed uri");
        blogUrl = (TextView) findViewById(R.id.blogsiteurl);
        blogUrl.setText(blogurl);
        poster = (ImageView) findViewById(R.id.poster);
        /*
        poster.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                try {
                    blogUrl =(TextView) view.findViewById(R.id.blogurl);
                    String currentUrl = blogUrl.getText().toString();
                    Log.w("WRIR","Getting blogUrl" + currentUrl);
                    //Uri uri = Uri.parse(currentUrl);
                    //Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
                    //startActivity(browserIntent);
                    //startActivity(new Intent(Intent.ACTION_VIEW,uri));
                    loadBlogViewer(currentUrl);

                } catch (ActivityNotFoundException e) {
                    //Toast.makeText(PlayerControl.CONTEXT_IGNORE_SECURITY, "No application can handle this request!", Toast.LENGTH_LONG ).show();
                    e.printStackTrace();
                    Log.w("WRIR","Failed blogUrl");
                }
                return true;

            }
        });
        */
        /*
        poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    blogUrl =(TextView) view.findViewById(R.id.blogurl);
                    String currentUrl = blogUrl.getText().toString();
                    Log.w("WRIR","Getting blogUrl" + currentUrl);
                    //Uri uri = Uri.parse(currentUrl);
                    //Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
                    //startActivity(browserIntent);
                    //startActivity(new Intent(Intent.ACTION_VIEW,uri));
                    loadBlogViewer(currentUrl);

                } catch (ActivityNotFoundException e) {
                    //Toast.makeText(PlayerControl.CONTEXT_IGNORE_SECURITY, "No application can handle this request!", Toast.LENGTH_LONG ).show();
                    e.printStackTrace();
                    Log.w("WRIR","Failed blogUrl");
                }
            }
        });
        */
        songName = (TextView) findViewById(R.id.textView4);
        startTimeField = (TextView) findViewById(R.id.textView1);
        endTimeField = (TextView) findViewById(R.id.textView2);
        seekbar = (SeekBar) findViewById(R.id.seekBar1);
        playButton = (ImageButton) findViewById(R.id.imageButton1);
        pauseButton = (ImageButton) findViewById(R.id.imageButton2);

        songName.setText(title);
        /*
        songName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w("WRIR","CLICKED SONG!");
                try {
                    blogUrl = (TextView) view.findViewById(R.id.blogsiteurl);
                    String currentUrl = blogUrl.getText().toString();
                    Log.w("WRIR","Getting blogUrl" + currentUrl);
                    //Uri uri = Uri.parse(currentUrl);
                    //Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
                    //startActivity(browserIntent);
                    //startActivity(new Intent(Intent.ACTION_VIEW,uri));
                    loadBlogViewer(currentUrl);


                } catch (ActivityNotFoundException e) {
                    //Toast.makeText(this, "No application can handle this request!", Toast.LENGTH_LONG ).show();
                    e.printStackTrace();
                    Log.w("WRIR","Failed blogUrl");
                }
            }
        });

           */

        /*
        songName.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                try {
                    blogUrl =(TextView) view.findViewById(R.id.blogurl);
                    String currentUrl = blogUrl.getText().toString();
                    Log.w("WRIR","Getting blogUrl" + currentUrl);
                    //Uri uri = Uri.parse(currentUrl);
                    //Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
                    //startActivity(browserIntent);
                    //startActivity(new Intent(Intent.ACTION_VIEW,uri));
                    loadBlogViewer(currentUrl);

                } catch (ActivityNotFoundException e) {
                    //Toast.makeText(PlayerControl.CONTEXT_IGNORE_SECURITY, "No application can handle this request!", Toast.LENGTH_LONG ).show();
                    e.printStackTrace();
                    Log.w("WRIR","Failed blogUrl");
                }
                return true;
            }
        });
        */
        Log.w("WRIR", "WRIR: About to create uri");
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mediaPlayer = MediaPlayer.create(this, mp3url);
        Log.w("WRIR", "WRIR: Created uri");
        seekbar.setClickable(true);
        pauseButton.setEnabled(false);

        new DownloadImageTask((ImageView) findViewById(R.id.poster)).execute(posterurl);



        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                   progressChanged = progress;
              }

                public void onStartTrackingTouch(SeekBar seekBar) {
                    //mediaPlayer.pause();
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                    //Toast.makeText(SeekbarActivity.this,"seek bar progress:"+progressChanged,
                    //       Toast.LENGTH_SHORT).show();
                    //startTime = startTime + progressChanged;
                    mediaPlayer.seekTo((int) progressChanged);
                    //mediaPlayer.start();
                }

        });
    }



    public void play(View view){
        Toast.makeText(getApplicationContext(), "Playing sound",
                Toast.LENGTH_SHORT).show();
        mediaPlayer.start();
        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();
        if(oneTimeOnly == 0){
            seekbar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }

        endTimeField.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                        toMinutes((long) finalTime)))
        );
        startTimeField.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                        toMinutes((long) startTime)))
        );
        seekbar.setProgress((int)startTime);
        myHandler.postDelayed(UpdateSongTime,100);
        pauseButton.setEnabled(true);
        playButton.setEnabled(false);
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            startTimeField.setText(String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                            toMinutes((long) startTime)))
            );
            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };

    public void pause(View view){
        Toast.makeText(getApplicationContext(), "Pausing sound",
                Toast.LENGTH_SHORT).show();

        mediaPlayer.pause();
        pauseButton.setEnabled(false);
        playButton.setEnabled(true);
    }
    public void forward(View view){
        int temp = (int)startTime;
        if((temp+forwardTime)<=finalTime){
            startTime = startTime + forwardTime;
            mediaPlayer.seekTo((int) startTime);
        }
        else{
            Toast.makeText(getApplicationContext(),
                    "Cannot jump forward 5 seconds",
                    Toast.LENGTH_SHORT).show();
        }

    }
    public void rewind(View view){
        int temp = (int)startTime;
        if((temp-backwardTime)>0){
            startTime = startTime - backwardTime;
            mediaPlayer.seekTo((int) startTime);
        }
        else{
            Toast.makeText(getApplicationContext(),
                    "Cannot jump backward 5 seconds",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
    public void loadBlogViewer(String blogUrl) {
        //define a new Intent for the second Activity
        Log.w("WRIR","WRIR: about to Intent");
        Intent intent = new Intent(this, BlogSiteViewer.class);
        Log.w("WRIR","WRIR: Putting Extras" + blogUrl );

        intent.putExtra("blogUrl", blogUrl);
        //start the second Activity
        Log.w("WRIR","WRIR: StartActivity blogUrl NOW");
        this.startActivity(intent);
        Log.w("WRIR", "WRIR: BlogUrl Done!");
    }

}