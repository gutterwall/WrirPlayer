package org.wrir.WrirPlayer;
import android.content.Intent;
import org.wrir.WrirPlayer.adater.CustomListAdapter;
import org.wrir.WrirPlayer.app.AppController;
import org.wrir.WrirPlayer.model.Show;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wrir.WrirPlayer.util.BlogSiteViewer;


import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
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
                TextView titleView = (TextView) view.findViewById(R.id.title);
                String title = titleView.getText().toString();
                TextView posterView = (TextView) view.findViewById(R.id.poster);
                String posterurl = posterView.getText().toString();
                TextView blogUrl = (TextView) view.findViewById(R.id.blogurl);
                String blogurl = blogUrl.getText().toString();
                //Toast.makeText(getApplicationContext(), mp3, Toast.LENGTH_LONG).show();
                try {
                    killMediaPlayer();
                    loadPlayerControl(mp3, title, posterurl, blogurl);
                    //playAudio(mp3);
                    //playLocalAudio();
                    //playLocalAudio_UsingDescriptor();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                TextView blogUrl = (TextView) view.findViewById(R.id.blogurl);
                String blogurl = blogUrl.getText().toString();
                Toast.makeText(getApplicationContext(), blogurl, Toast.LENGTH_LONG).show();
                try {
                    Uri uri = Uri.parse(blogurl);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(browserIntent);
                   // loadBlogSiteView(blogurl);

                } catch (Exception e) {

                    e.printStackTrace();
                }
                return true;
            }
        });


        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        // changing action bar color
        //getActionBar().setBackgroundDrawable(
        //        new ColorDrawable(Color.parseColor("#1b1b1b")));


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
                                Show.setBlogUrl(obj.getString("showurl"));

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
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void loadPlayerControl(String mp3, String title, String poster, String blogUrl) {
        //define a new Intent for the second Activity
        Log.w("WRIR","WRIR: about to Intent");
        Intent intent = new Intent(this, PlayerControl.class);
        Log.w("WRIR","WRIR: Putting Extras" + mp3 + " " + title);
        intent.putExtra("mp3", mp3);
        intent.putExtra("title", title);
        intent.putExtra("poster", poster);
        intent.putExtra("blogurl", blogUrl);
        //start the second Activity
        Log.w("WRIR","WRIR: StartActivity NOW");
        this.startActivity(intent);
        Log.w("WRIR", "WRIR: Done!");
    }

   public void loadBlogSiteView(String blogUrl) {
       Log.w("WRIR","WRIR: about to Intent Blog");
       Intent intent = new Intent(this, BlogSiteViewer.class);
       Log.w("WRIR","WRIR: Putting Extras " + blogUrl);
       intent.putExtra("blogUrl", blogUrl);
       //start the second Activity
       Log.w("WRIR","WRIR: StartActivity BlogSiteViewer NOW");
       this.startActivity(intent);
       Log.w("WRIR", "WRIR: BlogSiteViewer Done!");
   }


}

