package popularmovies.troychuinard.com.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import popularmovies.troychuinard.com.popularmovies.Model.Movie;

public class IndividualMovieActivity extends AppCompatActivity {

    private TextView mMovieTitle;
    private ImageView mMoviePoster;
    private TextView mMovieReleaseDate;
    private RatingBar mRatingBar;
    private TextView mSynopsis;

    private static final String NEW_DATE_FORMAT = "MM/dd/yyyy";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_movie);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("Movie");
        String title = movie.getOriginal_title();
        Log.v("POSTER_PATH", movie.getPoster_path());
        Log.v("RELEASE", movie.getRelease_date().toString());

        mMovieTitle = findViewById(R.id.movie_name);
        mMoviePoster = findViewById(R.id.movie_details_movie_poster_image);
        mMovieReleaseDate = findViewById(R.id.movie_details_release_date);
        mRatingBar = findViewById(R.id.movie_details_rating_bar);
        mSynopsis = findViewById(R.id.movie_details_synopsis);

        mMovieTitle.setText(title);
        Picasso.get()
                .load("http://image.tmdb.org/t/p/w185" + movie.getPoster_path())
                .into(mMoviePoster);

        float vote_average = movie.getVote_average().floatValue();
        mMovieReleaseDate.setText(movie.getRelease_date());
        mRatingBar.setRating(vote_average);
        mSynopsis.setText(movie.getOverview());

    }
}
