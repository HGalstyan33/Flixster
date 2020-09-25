package hasmik.example.flixster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import hasmik.example.flixster.R;
import hasmik.example.flixster.models.Movie;

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

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.idPoster);
        }

        public void bind(Movie movie) {
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
        }
    }

    public class PopularViewHolder extends RecyclerView.ViewHolder {

        ImageView backdrop;

        public PopularViewHolder(@NonNull View itemView) {
            super(itemView);
            backdrop = itemView.findViewById(R.id.idBackdrop);
        }

        public void bind(Movie movie) {
            Glide.with(context).load(movie.getBackdropPath()).placeholder(R.drawable.loading).into(backdrop);
        }
    }
}