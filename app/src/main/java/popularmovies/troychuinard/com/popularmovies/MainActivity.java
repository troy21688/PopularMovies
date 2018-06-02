package popularmovies.troychuinard.com.popularmovies;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import popularmovies.troychuinard.com.popularmovies.Model.TheMovieDatabase;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mMovieResults;
    private RecyclerView.Adapter mMovieResultsAdapter;

    private ArrayList<String> mMovieURLS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieResults = findViewById(R.id.main_recyclerview_image_results);
        GridLayoutManager glm = new GridLayoutManager(this, 21);
        glm.setOrientation(LinearLayoutManager.VERTICAL);
        mMovieResults.setLayoutManager(glm);

        mMovieResultsAdapter = new MyAdapter(mMovieURLS);
        mMovieResults.setAdapter(mMovieResultsAdapter);
        mMovieURLS = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_spinner, menu);
        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) item.getActionView();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.spiner_list_item_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), String.valueOf(i), Toast.LENGTH_LONG).show();
                mMovieURLS.clear();
                mMovieResultsAdapter.notifyDataSetChanged();
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient client = new OkHttpClient
                        .Builder()
                        .addInterceptor(interceptor)
                        .build();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://image.tmdb.org/t/p/")
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiInterface apiInterface = retrofit.create(ApiInterface.class);
                Call<TheMovieDatabase> call = apiInterface.getimages(query)
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return true;

    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {


        public class ViewHolder extends RecyclerView.ViewHolder {

            protected ImageView mResultImage;

            public ViewHolder(View itemView) {
                super(itemView);
                mResultImage = itemView.findViewById(R.id.movie_poster_image);
            }
        }

        public MyAdapter(ArrayList<String> mDataset) {
            mMovieURLS = mDataset;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_poster_image, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final String urlForPhoto = mMovieURLS.get(position);
            Picasso.with(getApplicationContext())
                    .load(urlForPhoto)
                    .into(holder.mResultImage);

        }

        @Override
        public int getItemCount() {
            return mMovieURLS.size();
        }
    }

    public interface ApiInterface{
        @GET("""""""");
        Call<TheMovieDatabase> getImages(@Query("text") String query);

    }

}

