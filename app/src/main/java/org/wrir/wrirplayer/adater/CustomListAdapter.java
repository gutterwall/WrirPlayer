package org.wrir.WrirPlayer.adater;

import org.wrir.WrirPlayer.R;
import org.wrir.WrirPlayer.app.AppController;
import org.wrir.WrirPlayer.model.Show;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Show> ShowItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<Show> ShowItems) {
        this.activity = activity;
        this.ShowItems = ShowItems;
    }

    @Override
    public int getCount() {
        return ShowItems.size();
    }

    @Override
    public Object getItem(int location) {
        return ShowItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView presenter = (TextView) convertView.findViewById(R.id.presenter);
        TextView type = (TextView) convertView.findViewById(R.id.type);
        TextView datestamp = (TextView) convertView.findViewById(R.id.datestamp);
        TextView mp3 = (TextView) convertView.findViewById(R.id.mp3);
        //Button button = (Button) convertView.findViewbyId(R.id.playShow);

        // getting Show data for the row
        Show m = ShowItems.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        // title
        title.setText(m.getTitle());
        //String urlString = m.getTitle() + "&lt;a href=\'" + m.getMp3() + "'&gt;Link!&lt;/&gt;";
        //title.setText(Html.fromHtml(urlString));
        // rating
        presenter.setText(m.getPresenter());

        // genre
        type.setText(m.getType());
        String datestampraw = m.getDatestamp();
        //String year = datestampraw.substring(0,4);
        //String month = datestampraw.substring(5,2);
        //String day = datestampraw.substring(7,2);
        //String hour = datestampraw.substring(9,2);
        //String minute = datestampraw.substring(11,2);
        String showtime = "";
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
            //final Date dateObj = sdf.parse(hour + ":" + minute);
            final Date dateObj = sdf.parse(datestampraw);
            showtime = new SimpleDateFormat("MM/dd/yyyy K:mm a").format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
            //showtime = hour + ":" + minute;
        }

        // release year
       //datestamp.setText(m.getDatestamp());
        datestamp.setText(showtime);
        //datestamp.setText( month + "/" + day + "/" + year + " " + showtime);

        mp3.setText(m.getMp3());
        /*button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });
        */
        return convertView;
    }
}

