package com.example.shreyas.thrones.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shreyas.thrones.ItemFormats.CharacterFormat;
import com.example.shreyas.thrones.ViewHolders.CharacterSearchViewHolder;
import com.example.shreyas.thrones.ItemFormats.HouseFormat;
import com.example.shreyas.thrones.ViewHolders.HouseSearchViewHolder;
import com.example.shreyas.thrones.R;

import java.util.List;

/**
 * Created by Shreyas on 1/22/2017.
 */

public class SearchRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> searchList;
    private final int CHARACTER = 0;
    private final int HOUSE = 1;
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
        } else {
            return HOUSE;
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
            default:
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

        HouseFormat house = (HouseFormat) searchList.get(position);
        if (house != null) {
            v.getHouseNameTextView().setText(house.getName());
            v.setHouseId(house.getHouseId());
        }
    }
}
