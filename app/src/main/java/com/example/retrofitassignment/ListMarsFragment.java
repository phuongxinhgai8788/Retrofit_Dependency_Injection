package com.example.retrofitassignment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.retrofitassignment.network.MarsProperty;

import java.util.ArrayList;
import java.util.List;

public class ListMarsFragment extends Fragment {

    private static final String ARG_PARAM1 = "isBought";

    private boolean isBought;
    private RecyclerView recyclerView;
    private List<MarsProperty> marsPropertyList = new ArrayList<>();
    private Context context;
    private ListMarsViewModel listMarsViewModel;
    public ListMarsFragment() {
        // Required empty public constructor
    }

    public static ListMarsFragment newInstance(boolean param1) {
        ListMarsFragment fragment = new ListMarsFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
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
            isBought = getArguments().getBoolean(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_mars, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        listMarsViewModel = new ViewModelProvider(this).get(ListMarsViewModel.class);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(isBought){
            listMarsViewModel.boughtMarsList.observe(getViewLifecycleOwner(), mars -> {
                if(mars != null){
                    marsPropertyList = mars;
                    updateUI();
                }
            });
        }else{
            listMarsViewModel.boughtMarsList.observe(getViewLifecycleOwner(), mars -> {
                if(mars != null){
                    marsPropertyList = mars;
                    updateUI();
                }
            });
        }
    }

    private void updateUI() {
        MarsAdapter marsAdapter = new MarsAdapter();
        recyclerView.setAdapter(marsAdapter);
    }

    private class MarsHolder extends RecyclerView.ViewHolder{
        private TextView priceTV;
        private ImageView imageView;
        public MarsHolder(@NonNull View itemView) {
            super(itemView);
            priceTV = itemView.findViewById(R.id.price);
            imageView = itemView.findViewById(R.id.image);
        }

        public void bind(MarsProperty mars){
            priceTV.setText(mars.getPrice()+"");
            if(isBought) {
                imageView.setImageResource (R.mipmap.ic_buy);
            }else{
                imageView.setImageResource (R.mipmap.ic_rent);
            }
        }
    }

    private class MarsAdapter extends RecyclerView.Adapter<MarsHolder>{

        @NonNull
        @Override
        public MarsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.mar_item_holder, viewGroup, false);
            return new MarsHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MarsHolder marsHolder, int i) {
            MarsProperty marsProperty = marsPropertyList.get(i);
            marsHolder.bind(marsProperty);
        }

        @Override
        public int getItemCount() {
            return marsPropertyList.size();
        }
    }

}