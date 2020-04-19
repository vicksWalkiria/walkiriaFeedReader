package com.walkiriaapps.walkiriarssfeedreader;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.walkiriaapps.walkiriarssfeedreader.Classes.AppData;
import com.walkiriaapps.walkiriarssfeedreader.Classes.FeedAdapter;
import com.walkiriaapps.walkiriarssfeedreader.Classes.VolleyRequestClassWalkiria;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class MainActivity extends AppCompatActivity {

    private TextView tvTitle;
    private ImageView imageView;
    private RecyclerView recyclerView;
    private ArrayList<FeedPost> feedPost;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTitle = findViewById(R.id.feed_title);
        imageView = findViewById(R.id.logo);
        recyclerView = findViewById(R.id.feed_recyclerview);
        progressBar = findViewById(R.id.progress_bar);

        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        feedPost = new ArrayList<FeedPost>();
        Map<String, String> params = new HashMap<String, String>();
        Log.d("WALKIRIA", "Params: " + params);

        new VolleyRequestClassWalkiria(MainActivity.this, AppData.FEED_URL, Request.Method.GET, params, progressBar, AppData.FEED_REQUEST);
    }

    public void onVolleyResponse(String response, int id) throws JSONException {

        if (id == AppData.FEED_REQUEST) {
            XmlToJson xmlToJson = new XmlToJson.Builder(response).build();
            Log.d("WALKIRIA", "RESPONSE IS: " + xmlToJson);

            JSONObject feed = xmlToJson.toJson().getJSONObject("rss").getJSONObject("channel");
            tvTitle.setText(feed.getJSONObject("image").getString("title"));
            Glide.with(this).load(feed.getJSONObject("image").getString("url")).into(imageView);

            for (int i = 0; i < feed.getJSONArray("item").length(); i++) {
                JSONObject obj = feed.getJSONArray("item").getJSONObject(i);
                String title = obj.getString("title");
                String content = obj.getString("content:encoded");
                String link = obj.getString("link");
                String category = obj.getString("category");

                feedPost.add(new FeedPost(title, content, link, category));
            }

            FeedAdapter feedAdapter = new FeedAdapter(feedPost, this);
            recyclerView.setAdapter(feedAdapter);
            DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
            divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.my_custom_divider));
            recyclerView.addItemDecoration(divider);
        }

    }

    public void displayAlertDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


}
