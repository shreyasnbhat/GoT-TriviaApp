package com.example.shreyas.thrones.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shreyas.thrones.ItemFormats.CharacterFormat;
import com.example.shreyas.thrones.ItemFormats.DividerFormat;
import com.example.shreyas.thrones.RealmHouseFormat;
import com.example.shreyas.thrones.ViewHolders.CharacterSearchViewHolder;
import com.example.shreyas.thrones.ItemFormats.HouseFormat;
import com.example.shreyas.thrones.ViewHolders.HouseSearchViewHolder;
import com.example.shreyas.thrones.R;
import com.example.shreyas.thrones.ViewHolders.SearchItemTypeViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shreyas on 1/22/2017.
 */

public class SearchRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> searchList = new ArrayList<>();
    private final int CHARACTER = 0;
    private final int HOUSE = 1;
    private final int DIVIDER = 2;
    private Context mContext;

    public SearchRVAdapter(List<Object> searchList, Context context) {
        this.searchList = searchList;
        this.mContext = context;

    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    @Override
    public int getItemViewType(int position) {

        //Used to identify which Type is the object of
        if (searchList.get(position) instanceof CharacterFormat) {
            return CHARACTER;
        } else if (searchList.get(position) instanceof RealmHouseFormat) {
            return HOUSE;
        } else {
            return DIVIDER;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case CHARACTER:
                View character = inflater.inflate(R.layout.character_search_item_format, parent, false);
                viewHolder = new CharacterSearchViewHolder(character, mContext);
                break;
            case HOUSE:
                View house = inflater.inflate(R.layout.house_search_item_format, parent, false);
                viewHolder = new HouseSearchViewHolder(house, mContext);
                break;
            case DIVIDER:
                View divider = inflater.inflate(R.layout.search_item_type_divider_format, parent, false);
                viewHolder = new SearchItemTypeViewHolder(divider, mContext);
                break;

        }

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case CHARACTER:
                CharacterSearchViewHolder v1 = (CharacterSearchViewHolder) holder;
                configureCharacterHolder(v1, position);
                break;
            case HOUSE:
                HouseSearchViewHolder v2 = (HouseSearchViewHolder) holder;
                configureHouseHolder(v2, position);
                break;
            case DIVIDER:
                SearchItemTypeViewHolder v3 = (SearchItemTypeViewHolder) holder;
                configureDividerHolder(v3, position);
                break;
        }

    }

    //Setting Fields in the ViewHolder
    public void configureCharacterHolder(CharacterSearchViewHolder v, int position) {

        CharacterFormat character = (CharacterFormat) searchList.get(position);
        if (character != null) {
            v.getNameTextView().setText(character.getName());
            v.getPlayedByTextView().setText(character.getPlayedBy());
            v.setBorn(character.getBorn());
            v.setDied(character.getDied());
            v.setGender(character.getGender());
            v.setImageUrl(character.getImageUrl());
            Uri imageUri = Uri.parse(character.getImageUrl());
            v.getCharacterImageView().setImageURI(imageUri);
        }

    }

    public void configureHouseHolder(HouseSearchViewHolder v, int position) {

        RealmHouseFormat house = (RealmHouseFormat) searchList.get(position);
        if (house != null) {
            v.getHouseNameTextView().setText(house.getName());
            v.setHouseId(house.getHouseId());
        }
    }

    public void configureDividerHolder(SearchItemTypeViewHolder v, int position) {

        DividerFormat divider = (DividerFormat) searchList.get(position);
        if (divider != null) {
            v.getItemTypeTextView().setText(divider.getDividerText());
        }

    }
}
