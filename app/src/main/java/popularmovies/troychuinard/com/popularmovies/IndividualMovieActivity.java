package popularmovies.troychuinard.com.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import popularmovies.troychuinard.com.popularmovies.Model.Movie;
import popularmovies.troychuinard.com.popularmovies.Model.Video;
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
    private RecyclerView mVideoRecyclerView;
    private MyAdapter mRecyclerAdapter;
    private String mYouTubeID;

    private List<Video> mVideoResults;

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
        mVideoResults = new ArrayList<>();


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
        Call<Videos> call = apiInterface.getVideos(API_KEY);
        call.enqueue(new Callback<Videos>() {
            @Override
            public void onResponse(Call<Videos> call, Response<Videos> response) {
                mVideoResults = response.body().getResults();
                Log.v("VIDEO_RESULTS", String.valueOf(mVideoResults.size()));
                Log.v("SUCCESSFUL", String.valueOf(response.isSuccessful()));
                mVideoRecyclerView = findViewById(R.id.main_recyclerview_video_results);
                LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
                lm.setOrientation(LinearLayoutManager.VERTICAL);
                mVideoRecyclerView.setLayoutManager(lm);
                mRecyclerAdapter = new MyAdapter(mVideoResults);
                mVideoRecyclerView.setAdapter(mRecyclerAdapter);
            }

            @Override
            public void onFailure(Call<Videos> call, Throwable t) {

            }
        });




        mMovieTitle.setText(title);
        Picasso.get()
                .load("http://image.tmdb.org/t/p/w185" + movie.getPoster_path())
                .into(mMoviePoster);


        float vote_average = movie.getVote_average().floatValue();
        mMovieReleaseDate.setText(movie.getRelease_date());
        mRatingBar.setRating(vote_average);
        mSynopsis.setText(movie.getOverview());






    }

    public class MyAdapter extends RecyclerView.Adapter<IndividualMovieActivity.MyAdapter.ViewHolder> {


        public class ViewHolder extends RecyclerView.ViewHolder {

            protected TextView mTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                mTextView = itemView.findViewById(R.id.trailer_text);
            }
        }

        public MyAdapter(List<Video> mDataset) {
            mVideoResults = mDataset;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_view, parent, false);
            return new ViewHolder(v);
        }


        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.mTextView.setText("Trailer " + String.valueOf(position +1) );
            final String youTubeID = mVideoResults.get(position).getKey();
            Log.v("YOUTUBE_ID", youTubeID);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + youTubeID));
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + youTubeID));

                    //TODO: Why is there a try-catch? What is best practice for a Try-Catch?
                    try {
                        getApplicationContext().startActivity(appIntent);
                    } catch (ActivityNotFoundException ex) {
                        getApplicationContext().startActivity(webIntent);
                    }
                }
            });

        }


        @Override
        public int getItemCount() {
            return mVideoResults.size();
        }

    }


    public interface ApiInterface {
        @GET("videos?language=en-US")
        Call<Videos> getVideos(@Query("api_key") String api_key);
    }
}



