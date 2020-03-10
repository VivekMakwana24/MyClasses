package com.example.myclasses.adapter;

import
        android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myclasses.R;
import com.example.myclasses.databinding.RowCountryCodeBinding;
import com.example.myclasses.model.CountryCodePojoItem;

import java.util.ArrayList;

public class CountryCodeAdapter extends RecyclerView.Adapter<CountryCodeAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CountryCodePojoItem> arrayList;
    private countryCodeInterface codeInterface;

    public CountryCodeAdapter(Context context, ArrayList<CountryCodePojoItem> arrayList, countryCodeInterface codeInterface) {
        this.context = context;
        this.arrayList = arrayList;
        this.codeInterface = codeInterface;
    }

    public interface countryCodeInterface {
        void codeClick(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowCountryCodeBinding binding = DataBindingUtil.inflate(inflater, R.layout.row_country_code, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (arrayList.size() - 1 == position) {
            holder.binding.view.setVisibility(View.GONE);
        } else {
            holder.binding.view.setVisibility(View.VISIBLE);
        }
        holder.bind(arrayList.get(position));

        holder.binding.getRoot().setOnClickListener(view -> codeInterface.codeClick(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RowCountryCodeBinding binding;

        public ViewHolder(@NonNull RowCountryCodeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CountryCodePojoItem item) {
            binding.setCountry(item);
            binding.executePendingBindings();
        }

    }
}
