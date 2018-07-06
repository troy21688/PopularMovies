package popularmovies.troychuinard.com.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import popularmovies.troychuinard.com.popularmovies.Model.Movie;
import popularmovies.troychuinard.com.popularmovies.Model.Movies;
import popularmovies.troychuinard.com.popularmovies.Model.Videos;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class IndividualMovieActivity extends AppCompatActivity {

    private TextView mMovieTitle;
    private ImageView mMoviePoster;
    private ImageView mPlayButton;
    private TextView mMovieReleaseDate;
    private RatingBar mRatingBar;
    private TextView mSynopsis;
    private String mBaseURL;

    private static final String API_KEY = popularmovies.troychuinard.com.popularmovies.BuildConfig.TMD_API_KEY;

    private static final String NEW_DATE_FORMAT = "MM/dd/yyyy";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_movie);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("Movie");
        String title = movie.getOriginal_title();
        int movie_id = movie.getId();
        final String movie_id_string = String.valueOf(movie_id);
        mBaseURL = "https://api.themoviedb.org/3/movie/" + movie_id_string + "/";
        Log.v("POSTER_PATH", movie.getPoster_path());
        Log.v("RELEASE", movie.getRelease_date().toString());

        mMovieTitle = findViewById(R.id.movie_name);
        mMoviePoster = findViewById(R.id.movie_details_movie_poster_image);
        mPlayButton = findViewById(R.id.movie_details_play_button);
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

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient
                .Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mBaseURL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final ApiInterface apiInterface = retrofit.create(ApiInterface.class);


        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Videos> call = apiInterface.getVideos(API_KEY);
                call.enqueue(new Callback<Videos>() {
                    @Override
                    public void onResponse(Call<Videos> call, Response<Videos> response) {

                    }

                    @Override
                    public void onFailure(Call<Videos> call, Throwable t) {

                    }
                });





            }
        });


        }


    public interface ApiInterface {
        @GET("videos?language=en-US")
        Call<Videos> getVideos(@Query("api_key") String api_key);
    }
}



