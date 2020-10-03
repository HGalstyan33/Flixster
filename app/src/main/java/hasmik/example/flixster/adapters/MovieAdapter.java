package hasmik.example.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import androidx.core.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;
import hasmik.example.flixster.DetailActivity;
import hasmik.example.flixster.MainActivity;
import hasmik.example.flixster.R;
import hasmik.example.flixster.models.Movie;
import okhttp3.Headers;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static int NORMAL_MOVIE = 1;
    private static int POPULAR_MOVIE = 2;

    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter","onCreateViewHolder");
        View movieView;
        if (viewType == NORMAL_MOVIE) {
            movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
            return new ViewHolder(movieView);
        } else {
            movieView = LayoutInflater.from(context).inflate(R.layout.item_popular_movie, parent, false);
            return new PopularViewHolder(movieView);
        }
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d("MovieAdapter","onBindViewHolder " + position);

        // Get the movie at the passed-in position
        Movie movie = movies.get(position);

        // Bind the movie data into the ViewHolder
        if (getItemViewType(position) == NORMAL_MOVIE) {
            ((ViewHolder) holder).bind(movie);
        } else {
            ((PopularViewHolder) holder).bind(movie);
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public int getItemViewType(int position) {
        Movie movie = movies.get(position);
        if (movie.getVoteAverage() >= 7.5) {
            return POPULAR_MOVIE;
        }
        return NORMAL_MOVIE;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout container;
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.idPoster);
        }

        public void bind(final Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageURL;

            // If phone is in landscape, use backdrop
            // Else use poster
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageURL = movie.getBackdropPath();
            } else {
                imageURL = movie.getPosterPath();
            }

            Glide.with(context).load(imageURL).placeholder(R.drawable.loading).into(ivPoster);

            // 1. Register the click listener on the whole row
            // 2. Navigate to new activity on tap
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("movie", Parcels.wrap(movie));
                    //Pair<View, String> poster = Pair.create((View)ivPoster, "poster");
                    //Pair<View, String> title = Pair.create((View)tvTitle, "title");
                    //Pair<View, String> overview = Pair.create((View)tvOverview, "overview");
                    //ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, poster, title, overview);
                    context.startActivity(i);
                }
            });
        }
    }

    public class PopularViewHolder extends RecyclerView.ViewHolder {

        private static final String YOUTUBE_API_KEY = "AIzaSyC_hSIfA1AFNxb_YYouitzbhfYSAxr210k";
        private static final String VIDEOS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

        RelativeLayout container;
        ImageView backdrop;
        ImageView playButton;
        //YouTubePlayerView youTubePlayerView;

        public PopularViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            backdrop = itemView.findViewById(R.id.idBackdrop);
            playButton = itemView.findViewById(R.id.playButton);
            //youTubePlayerView = itemView.findViewById(R.id.player);
        }

        public void bind(final Movie movie) {
            Glide.with(context).load(movie.getBackdropPath()).placeholder(R.drawable.loading).into(backdrop);

            //AsyncHttpClient client = new AsyncHttpClient();
            //client.get(String.format(VIDEOS_URL, movie.getMovieId()), new JsonHttpResponseHandler() {
            //    @Override
            //    public void onSuccess(int statusCode, Headers headers, JSON json) {
            //        try {
            //            JSONArray results = json.jsonObject.getJSONArray("results");
            //            String site =  results.getJSONObject(0).getString("site");
            //            if (results.length() == 0 || !site.equals("YouTube")) {
            //                return;
            //            } else {
            //                final String youtubeKey = results.getJSONObject(0).getString("key");
            //                container.setOnClickListener(new View.OnClickListener() {
            //                    @Override
            //                    public void onClick(View v) {
            //                        backdrop.setVisibility(View.INVISIBLE);
            //                        playButton.setVisibility(View.INVISIBLE);
            //                        youTubePlayerView.setVisibility(View.VISIBLE);
            //                        initializeYoutube(youtubeKey);
            //                    }
            //                });
            //            }
            //        } catch (JSONException e) {
            //            Log.e("MovieAdapter","Failed to parse JSON");
            //        }
            //    }

            //    @Override
            //    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            //    }
            //});
        }

        //private void initializeYoutube(final String youtubeKey) {
        //    youTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
        //        @Override
        //        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        //            Log.d("MovieAdapter", "onSuccess");
        //            youTubePlayer.cueVideo(youtubeKey);
        //        }
        //        @Override
        //        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        //            Log.d("MovieAdapter", "onFailure");
        //        }
        //    });
        //}
    }
}