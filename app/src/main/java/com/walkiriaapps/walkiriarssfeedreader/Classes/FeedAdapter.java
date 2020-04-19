package com.walkiriaapps.walkiriarssfeedreader.Classes;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.walkiriaapps.walkiriarssfeedreader.FeedPost;
import com.walkiriaapps.walkiriarssfeedreader.MainActivity;
import com.walkiriaapps.walkiriarssfeedreader.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
    private final List<FeedPost> feedPost;
    private final Context mainActivity;

    public FeedAdapter(ArrayList<FeedPost> feedPost, MainActivity mainActivity) {
        this.feedPost = feedPost;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View v;

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_posts, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedAdapter.ViewHolder holder, int position) {

        final FeedPost fP = feedPost.get(position);
        holder.postTitle.setText(fP.getTitle());

        String data = fP.getCategory();
        Object json = null;
        try {
            json = new JSONTokener(data).nextValue();
            if (json instanceof JSONArray) {
                String categories = "";
                for (int i = 0; i < ((JSONArray) json).length(); i++) {
                    categories += ((JSONArray) json).getJSONObject(i).getString("content") + ", ";
                }
                holder.postCategory.setText(categories.substring(0, categories.length() - 2));
            }
            else
            {
                holder.postCategory.setText(fP.getCategory());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            holder.postCategory.setText(fP.getCategory());
        }

    }

    @Override
    public int getItemCount() {
        return feedPost.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView postTitle;
        final TextView postCategory;
        final LinearLayout father;

        ViewHolder(View v) {
            super(v);
            postTitle = v.findViewById(R.id.post_title);
            postCategory = v.findViewById(R.id.post_category);
            father = v.findViewById(R.id.father);

            father.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(feedPost.get(pos).getLink()));
            mainActivity.startActivity(i);
        }
    }
}
