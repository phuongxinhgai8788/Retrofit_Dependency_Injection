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

import com.example.retrofitassignment.network.GalleryItem;
import com.example.retrofitassignment.network.MarsProperty;
import com.example.retrofitassignment.network.ThumbnailDownloader;

import java.util.ArrayList;
import java.util.List;

public class ListMarsFragment extends Fragment {

//    private static final String ARG_PARAM1 = "isBought";
    private final String TAG = "ListMarsFragment";

//    private boolean isBought;
    private RecyclerView recyclerView;
    private List<GalleryItem> marsPropertyList = new ArrayList<>();
    private Context context;
    private ListMarsViewModel listMarsViewModel;
    private ThumbnailDownloader<MarsHolder> thumbnailDownloader;
    private MarsAdapter marsAdapter = new MarsAdapter();

    public ListMarsFragment() {
        // Required empty public constructor
    }

    public static ListMarsFragment newInstance(/*boolean param1*/) {
        ListMarsFragment fragment = new ListMarsFragment();
        Bundle args = new Bundle();
//        args.putBoolean(ARG_PARAM1, param1);
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
//            isBought = getArguments().getBoolean(ARG_PARAM1);
        }
        Handler responseHandler = new Handler();
        thumbnailDownloader = new ThumbnailDownloader<MarsHolder>() {
            @Override
            public void onThumbnailDownloaded(MarsHolder marsHolder, Bitmap bitmapImg) {
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
        listMarsViewModel = new ViewModelProvider(this).get(ListMarsViewModel.class);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getLifecycle().removeObserver(thumbnailDownloader);
    }

//    public void changeMarsList(boolean isBought) {
//        this.isBought = isBought;
//        loadData();
//    }

    private void loadData() {
//        if (isBought) {
            listMarsViewModel.galleryList.observe(getViewLifecycleOwner(), mars -> {
                if (mars != null) {
                    Log.i(TAG, "List is fetched!");
                    marsPropertyList = mars;
                    marsAdapter.notifyDataSetChanged();
                }
            });
//        } else {
//            listMarsViewModel.rentMarsList.observe(getViewLifecycleOwner(), mars -> {
//                if (mars != null) {
//                    marsPropertyList = mars;
//                    marsAdapter.notifyDataSetChanged();
//                }
//            });
//        }
    }

    private class MarsHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public MarsHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }

        public void bindDrawable(Drawable drawable) {
            imageView.setImageDrawable(drawable);
        }

    }

    private class MarsAdapter extends RecyclerView.Adapter<MarsHolder> {

        @NonNull
        @Override
        public MarsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.mar_item_holder, viewGroup, false);
            return new MarsHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MarsHolder marsHolder, int i) {
            GalleryItem marsProperty = marsPropertyList.get(i);
            Drawable placeHolder = ContextCompat.getDrawable(
                    context,
                    R.mipmap.ic_buy_foreground);
            marsHolder.bindDrawable(placeHolder);
            thumbnailDownloader.queueThumbnail(marsHolder, marsProperty.getUrl());
        }

        @Override
        public int getItemCount() {
            return marsPropertyList.size();
        }
    }
}