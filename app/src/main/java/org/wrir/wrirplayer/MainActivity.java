package org.wrir.WrirPlayer;

import org.wrir.WrirPlayer.adater.CustomListAdapter;
import org.wrir.WrirPlayer.app.AppController;
import org.wrir.WrirPlayer.model.Show;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

public class MainActivity extends Activity {
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();
    private MediaPlayer mediaPlayer;
    // Shows json url
    private static final String url = "http://files.wrir.org/cgi-bin/sl2-SpecialNeeds";
    private ProgressDialog pDialog;
    private List<Show> ShowList = new ArrayList<Show>();
    private ListView listView;
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, ShowList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                TextView mp3View = (TextView) view.findViewById(R.id.mp3);
                String mp3 = mp3View.getText().toString();
                // selected item
                //String mp3 = ((android.widget.TextView) view).getText().toString();

                // Launching new Activity on selecting single List Item
                //Intent i = new Intent(getApplicationContext(), SingleListItem.class);
                // sending data to new activity
                //i.putExtra("mp3", mp3);
                //startActivity(i);`
                Toast.makeText(getApplicationContext(), mp3, Toast.LENGTH_LONG).show();
                try {


                    playAudio(mp3);
                    //playLocalAudio();
                    //playLocalAudio_UsingDescriptor();
                } catch (Exception e) {
                    e.printStackTrace();
                }
             }
        });


        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        // changing action bar color
        getActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#1b1b1b")));


        // Creating volley request obj
        JsonArrayRequest ShowReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Show Show = new Show();
                                Show.setTitle(obj.getString("title"));
                                String poster = obj.getString("poster");
                                if (poster.contains("http://wrir.org/")) {
                                    Show.setThumbnailUrl(poster);
                                } else {
                                    Show.setThumbnailUrl("http://wrir.org" + poster);
                                }

                                Show.setPresenter(obj.getString("presenter"));
                                Show.setType(obj.getString("type"));
                                Show.setDatestamp(obj.getString("datestamp"));
                                Show.setMp3(obj.getString("mp3"));

                                // adding Show to Shows array
                                ShowList.add(Show);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        }
        );

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(ShowReq);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
        killMediaPlayer();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    private void killMediaPlayer() {
        if(mediaPlayer!=null) {
            try {
                mediaPlayer.release();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void playAudio(String url) throws Exception
    {
        killMediaPlayer();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(url);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }
    /*
    private void playLocalAudio() throws Exception
    {
        mediaPlayer = MediaPlayer.create(this, R.raw.musicfile);
        mediaPlayer.start();
    }

    private void playLocalAudio_UsingDescriptor() throws Exception {

        AssetFileDescriptor fileDesc = getResources().openRawResourceFd(
                R.raw.musicfile);
        if (fileDesc != null) {

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(fileDesc.getFileDescriptor(), fileDesc
                    .getStartOffset(), fileDesc.getLength());

            fileDesc.close();

            mediaPlayer.prepare();
            mediaPlayer.start();
        }
    }
*/
}
