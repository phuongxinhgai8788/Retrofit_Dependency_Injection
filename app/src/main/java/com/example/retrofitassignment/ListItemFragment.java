package com.example.retrofitassignment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retrofitassignment.network.FlickersAPIService;
import com.example.retrofitassignment.network.FlickrResponse;
import com.example.retrofitassignment.network.GalleryItem;
import com.example.retrofitassignment.network.MarsAPIService;
import com.example.retrofitassignment.network.MarsProperty;
import com.example.retrofitassignment.network.ThumbnailDownloader;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListItemFragment extends Fragment {

    private final String TAG = "ListMarsFragment";

    private boolean isMars;
    private RecyclerView recyclerView;
    private List<MarsProperty> marsList = new ArrayList<>();
    private List<GalleryItem> galleryList = new ArrayList<>();
    private Context context;
    private ListItemViewModel listMarsViewModel;
    private ThumbnailDownloader<ItemHolder> thumbnailDownloader;
    private ItemsAdapter marsAdapter = new ItemsAdapter();

    public ListItemFragment() {
        // Required empty public constructor
    }

    public static ListItemFragment newInstance() {
        ListItemFragment fragment = new ListItemFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        Handler responseHandler = new Handler();
        thumbnailDownloader = new ThumbnailDownloader<ItemHolder>() {
            @Override
            public void onThumbnailDownloaded(ItemHolder marsHolder, Bitmap bitmapImg) {
                Drawable drawable = new BitmapDrawable(getResources(), bitmapImg);
                marsHolder.bindDrawable(drawable);
            }
        };
        thumbnailDownloader.setResponseHandler(responseHandler);
        getLifecycle().addObserver(thumbnailDownloader);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_mars, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerView.setAdapter(marsAdapter);
        listMarsViewModel = new ViewModelProvider(this).get(ListItemViewModel.class);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getLifecycle().removeObserver(thumbnailDownloader);
    }

    public void changeList(boolean isMars) {
        this.isMars = isMars;
        loadData();
    }

    private void loadData() {
        if (isMars) {
            listMarsViewModel.marsList.observe(getViewLifecycleOwner(), mars -> {
                if (mars != null) {
                    marsList = mars;
                    marsAdapter.notifyDataSetChanged();
                }
            });
        } else {
            listMarsViewModel.flickersList.observe(getViewLifecycleOwner(), flickers -> {
                if (flickers != null) {
                    galleryList = flickers;
                    Log.i(TAG, "Flickers is loaded!");
                    marsAdapter.notifyDataSetChanged();
                }
            });
        }
    }


    private class ItemHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }

        public void bindDrawable(Drawable drawable) {
            imageView.setImageDrawable(drawable);
        }

    }

    private class ItemsAdapter extends RecyclerView.Adapter<ItemHolder> {

        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.mar_item_holder, viewGroup, false);
            return new ItemHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder marsHolder, int i) {
            if(isMars){
                MarsProperty marsProperty = marsList.get(i);
                Drawable placeHolder = ContextCompat.getDrawable(
                        context,
                        R.mipmap.ic_buy_foreground);
                marsHolder.bindDrawable(placeHolder);
                thumbnailDownloader.queueThumbnail(marsHolder, marsProperty.getImgSrcUrl(), isMars);
            }else {
                GalleryItem galleryItem = galleryList.get(i);
                Drawable placeHolder = ContextCompat.getDrawable(
                        context,
                        R.mipmap.ic_rent_foreground);
                marsHolder.bindDrawable(placeHolder);
                thumbnailDownloader.queueThumbnail(marsHolder, galleryItem.getUrl(), isMars);
            }
        }

        @Override
        public int getItemCount() {
            if(isMars){
                return marsList.size();
            }else{
                return galleryList.size();
            }
        }
    }
}