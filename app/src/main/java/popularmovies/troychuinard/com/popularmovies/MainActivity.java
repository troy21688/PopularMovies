package popularmovies.troychuinard.com.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.facebook.stetho.Stetho;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import popularmovies.troychuinard.com.popularmovies.Model.Movie;
import popularmovies.troychuinard.com.popularmovies.Model.Movies;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mMovieResults;
    private MyAdapter mMovieResultsAdapter;
    private String query;
    private String mBaseURL;
//    private static final String API_KEY = popularmovies.troychuinard.com.popularmovies.BuildConfig.TMD_API_KEY;
    private static final String API_KEY = "09b0a9a9d5d9ddee2b3bc69e78b02457";

    private ArrayList<String> mMovieURLS;
    private ArrayList<Movie> mMovies;
    private List<Movie> mMovieResultsList;

    private static final String SPINNER_SELECTION = "SPINNER_SELECTION";

    private boolean mIsFavoriteSelected;

    private AppDatabase mDb;

    private Spinner mSpinner;

    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mPrefsEditor;

    private static Bundle mBundleRecyclerViewState;


    private int saved_selection = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("ONCREATE", "ONCREATE");
        if (savedInstanceState != null){
            saved_selection = savedInstanceState.getInt(SPINNER_SELECTION);
        }

        if (isOnline()) {
            setContentView(R.layout.activity_main);
            Stetho.initializeWithDefaults(this);
            mDb = AppDatabase.getInstance(getApplicationContext());
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
            mPrefs = getSharedPreferences("prefs", MODE_PRIVATE);

            mPrefsEditor = mPrefs.edit();
            switch (saved_selection){
                    case 0:
                        mBaseURL = "https://api.themoviedb.org/3/movie/popular/";
                        calltoRetrofit(mBaseURL);
                        break;
                    case 1:
                        mBaseURL = "https://api.themoviedb.org/3/movie/top_rated/";
                        calltoRetrofit(mBaseURL);
                        break;
                    case 2:
                        mIsFavoriteSelected = true;
                        mMovieURLS.clear();
                        retrieveMovies();
                        break;

                    default:
                        mBaseURL = "https://api.themoviedb.org/3/movie/popular/";
                        break;
                }


        } else {
            setContentView(R.layout.activity_no_internet);
            return;
        }

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_spinner, menu);
        MenuItem item = menu.findItem(R.id.spinner);
        mSpinner = (Spinner) item.getActionView();
        if (isOnline()) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.spiner_list_item_array, R.layout.custom_spinner);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner.setAdapter(adapter);
            if (saved_selection >= 0){
                mSpinner.setSelection(saved_selection);
            }
            mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    mPrefsEditor.putInt(SPINNER_SELECTION, i);
                    mPrefsEditor.commit();
                    switch (i) {
                        case 0:
                            mBaseURL = "https://api.themoviedb.org/3/movie/popular/";
                            calltoRetrofit(mBaseURL);
                            break;
                        case 1:
                            mBaseURL = "https://api.themoviedb.org/3/movie/top_rated/";
                            calltoRetrofit(mBaseURL);
                            break;
                        case 2:
                            mIsFavoriteSelected = true;
                            mMovieURLS.clear();
                            retrieveMovies();
                            break;

                        default:
                            mBaseURL = "https://api.themoviedb.org/3/movie/popular/";
                            break;
                    }


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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SPINNER_SELECTION, mSpinner.getSelectedItemPosition());
        super.onSaveInstanceState(outState);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int x = item.getItemId();
        Log.v("OPTIONS_ID", String.valueOf(x));
        return true;
    }

    private void retrieveMovies() {
        //TODO: I am unsure if I am handling correctly. When I favorite and unfavorite a movie, and then go back to the MainActivity, it is not displaying the correct item from the Spinner.
        //TODO: For example, favorites may be listed in the Spinner, however the screen may be displaying "TOP"
        //TODO: How could I tell if I am making unnecessary queries to Room? I know it is a project requirement, but I was not sure how to handle if I am making unnecessary calls
        //TODO: on screen rotation
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        final LiveData<List<Movie>> movies = viewModel.getMovies();
                movies.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable final List<Movie> movies) {
                for (Movie movie : movies) {
                    String photoURL = "http://image.tmdb.org/t/p/w185" + movie.getPoster_path();
                    Log.v("FAVORITE_URL", photoURL);
                    mMovieURLS.add(photoURL);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMovieResultsList = movies;
                        mMovieResultsAdapter.notifyDataSetChanged();
                    }
                });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        switch (saved_selection){
//            case 0:
//                mBaseURL = "https://api.themoviedb.org/3/movie/popular/";
//                calltoRetrofit(mBaseURL);
//                break;
//            case 1:
//                mBaseURL = "https://api.themoviedb.org/3/movie/top_rated/";
//                calltoRetrofit(mBaseURL);
//                break;
//            case 2:
//                mIsFavoriteSelected = true;
//                mMovieURLS.clear();
//                retrieveMovies();
//                break;
//
//            default:
//                mBaseURL = "https://api.themoviedb.org/3/movie/popular/";
//                break;
//        }
//        mSpinner.setSelection(saved_selection);
    }

    private void calltoRetrofit(String mBaseURL) {
        mMovieURLS.clear();
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
                        mMovieURLS.add(photoURL);
                    }
                    mMovieResultsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                Log.v("FAILURE", t.toString());
            }
        });
    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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

    static class SavedState extends android.view.View.BaseSavedState {
        public int mScrollPosition;
        SavedState(Parcel in) {
            super(in);
            mScrollPosition = in.readInt();
        }
        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(mScrollPosition);
        }
        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

}

