package popularmovies.troychuinard.com.popularmovies;

import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Handler;

public class AppExecutors{

    //for Singleton Instantiation
    private static final Object LOCK = new Object();
    private static AppExecutors sInstance;
    private final Executor diskIO;
    private final Executor mainThread;
    private final Executor networkIO;

    public AppExecutors(Executor diskIO, Executor mainThread, Executor networkIO) {
        this.diskIO = diskIO;
        this.mainThread = mainThread;
        this.networkIO = networkIO;
    }


    public static AppExecutors getsInstance(){
        if (sInstance == null){
            synchronized (LOCK){
                sInstance = new AppExecutors(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        new MainThreadExecutor());
            }
        }
        return sInstance;
    };

    public Executor diskIO(){return diskIO;};
    public Executor mainThread(){return mainThread;}
    public Executor netWorkIO(){return networkIO;}

    private static class MainThreadExecutor implements Executor{

        private android.os.Handler mainThreadHandler = new android.os.Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable runnable) {
            mainThreadHandler.post(runnable);
        }
    }



}
