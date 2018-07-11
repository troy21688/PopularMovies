package popularmovies.troychuinard.com.popularmovies;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.constraint.BuildConfig;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import popularmovies.troychuinard.com.popularmovies.Model.Movie;
import popularmovies.troychuinard.com.popularmovies.Model.Movies;
import popularmovies.troychuinard.com.popularmovies.Model.TheMovieDatabase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static android.provider.LiveFolders.INTENT;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mMovieResults;
    private MyAdapter mMovieResultsAdapter;
    private String query;
    private String mBaseURL;
    private static final String API_KEY = popularmovies.troychuinard.com.popularmovies.BuildConfig.TMD_API_KEY;

    private ArrayList<String> mMovieURLS;
    private ArrayList<Movie> mMovies;
    private List<Movie> mMovieResultsList;

    private AppDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isOnline()){
            setContentView(R.layout.activity_main);

            mMovieResults = findViewById(R.id.main_recyclerview_image_results);
            GridLayoutManager glm = new GridLayoutManager(this, 3);
            glm.setOrientation(LinearLayoutManager.VERTICAL);
            mMovieResults.setLayoutManager(glm);
            mMovieURLS = new ArrayList<>();
            mMovies = new ArrayList<>();
            mMovieResultsList = new ArrayList<>();

            mMovieResultsAdapter = new MyAdapter(mMovieResultsList);
            mMovieResults.setAdapter(mMovieResultsAdapter);
            mDb = AppDatabase.getInstance(getApplicationContext());

        } else{
            setContentView(R.layout.activity_no_internet);
            return;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_spinner, menu);
        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) item.getActionView();
        if (isOnline()){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.spiner_list_item_array, R.layout.custom_spinner);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    switch (i) {
                        case 0:
                            query = "popular";
                            mBaseURL = "https://api.themoviedb.org/3/movie/popular/";
                            break;
                        case 1:
                            query = "top_rated";
                            mBaseURL = "https://api.themoviedb.org/3/movie/top_rated/";
                            break;
                        default:
                            query = "popular";
                            mBaseURL = "https://api.themoviedb.org/3/movie/popular/";
                            break;
                    }
                    mMovieURLS.clear();
                    mMovieResultsAdapter.notifyDataSetChanged();
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

                    Log.v("API", API_KEY);
                    Call<Movies> call = apiInterface.getImages(API_KEY);
                    call.enqueue(new Callback<Movies>() {
                        @Override
                        public void onResponse(Call<Movies> call, Response<Movies> response) {
                            Log.v("RESPONSE", response.body().toString());
                            String totalPages = String.valueOf(response.body().getTotal_pages());
                            Log.v("TOTAL", totalPages);
                            mMovieResultsList = response.body().getResults();
                            for (Movie movie : mMovieResultsList) {
                                if (movie.getPoster_path() != null) {
                                    String photoURL = "http://image.tmdb.org/t/p/w185" + movie.getPoster_path();
//                                Log.v("MOVIE_URL", photoURL);
                                    mMovieURLS.add(photoURL);
                                }
                                mMovieResultsAdapter.swapDataSet(mMovieURLS);
                            }
                        }

                        @Override
                        public void onFailure(Call<Movies> call, Throwable t) {
                            Log.v("FAILURE", t.toString());
                        }
                    });
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            return true;
        } else {
            return true;
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {


        public class ViewHolder extends RecyclerView.ViewHolder {

            protected ImageView mResultImage;

            public ViewHolder(View itemView) {
                super(itemView);
                mResultImage = itemView.findViewById(R.id.movie_poster_image);
            }
        }

        public MyAdapter(List<Movie> mDataset) {
            mMovieResultsList = mDataset;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_poster_image, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            final String urlForPhoto = "http://image.tmdb.org/t/p/w185" + mMovieResultsList.get(position).getPoster_path();

            Picasso.get()
                    .load(urlForPhoto)
                    .into(holder.mResultImage);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), IndividualMovieActivity.class);
                    i.putExtra("Movie", mMovieResultsList.get(position));
                    startActivity(i);
                }
            });

        }

        @Override
        public int getItemCount() {
            return mMovieResultsList.size();
        }

        public void swapDataSet(ArrayList<String> dataset) {
            mMovieURLS = dataset;
            notifyDataSetChanged();

        }



    }

    public interface ApiInterface {
        @GET("?language=en-US")
        Call<Movies> getImages(@Query("api_key") String api_key);
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}

