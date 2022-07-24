package com.example.retrofitassignment.network;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import kotlin.Suppress;

public abstract class ThumbnailDownloader<T> extends HandlerThread implements LifecycleEventObserver {

    private final String TAG = "ThumbnailDownloader";
    private final int MESSAGE_DOWNLOAD = 0;
    private boolean hasQuit = false;
    private Handler requestHandler = new Handler();
    private Handler responseHandler = new Handler();
    private Repository repository = Repository.get();
    private ConcurrentHashMap<T, String> requestMap = new ConcurrentHashMap<>();


    public ThumbnailDownloader() {
        super("ThumbnailDownloader");
    }

    @Override
    public boolean quit() {
        hasQuit = true;
        return super.quit();
    }

    public void queueThumbnail(T target, String url) {
        Log.i(TAG, "Got a URL: " + url);
        requestMap.put(target, url);
        requestHandler.obtainMessage(MESSAGE_DOWNLOAD, target).sendToTarget();
    }

    @Suppress(names = "UNCHECKED_CAST")
    @SuppressLint("HandlerLeak")
    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        requestHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == MESSAGE_DOWNLOAD) {
                    T target = (T) msg.obj;
                    Log.i(TAG, "Got a request for URL: " + requestMap.get(target));
                    try {
                        handleRequest(target);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Creating bitmap throws error", e);
                    }
                }
            }
        };
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        switch (source.getLifecycle().getCurrentState()) {
            case CREATED:
                setup();
                break;
            case DESTROYED:
                tearDown();
        }
    }

    private void tearDown() {
        Log.i(TAG, "Destroying background thread");
        requestHandler.removeMessages(MESSAGE_DOWNLOAD);
        requestMap.clear();
    }

    private void setup() {
        Log.i(TAG, "Starting background thread");
        start();
        getLooper();
    }

    private void handleRequest(T target) throws IOException {
        Log.i(TAG, "Request is being handled");
        String url = requestMap.get(target);
        Bitmap bitmap = repository.fetchPhoto(url);
        responseHandler.post(() -> {
//            if(!requestMap.get(target).equals(url) || hasQuit){
//                return;
//            }
//            requestMap.remove(target);
            onThumbnailDownloaded(target, bitmap);
        });
    }

    public abstract void onThumbnailDownloaded(T target, Bitmap bitmapImg);

    public void setResponseHandler(Handler responseHandler) {
        this.responseHandler = responseHandler;
    }

}
