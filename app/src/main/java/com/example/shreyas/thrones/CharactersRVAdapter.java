package com.example.shreyas.thrones;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.turingtechnologies.materialscrollbar.INameableAdapter;

import java.util.List;

/**
 * Created by Shreyas on 11/13/2016.
 */

public class CharactersRVAdapter extends RecyclerView.Adapter<CharactersRVAdapter.ViewHolder> implements INameableAdapter {

    private List<CharacterFormat> characterList;

    private Context mContext;

    //View Holder Definition
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;

        public ViewHolder(View v)
        {
            super(v);
            name = (TextView)v.findViewById(R.id.name);
            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(mContext,CharacterInfoActivity.class);
                    intent.putExtra("CharacterName",name.getText().toString());

                    CharacterFormat character = new CharacterFormat();

                    //Get Adapter Position
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                         character = characterList.get(position);
                    }

                    intent.putExtra("playedBy",character.getPlayedBy());
                    intent.putExtra("gender",character.getGender());
                    intent.putExtra("died",character.getDied());
                    intent.putExtra("born",character.getBorn());
                    intent.putExtra("imageUrl",character.getImageUrl());
                    mContext.startActivity(intent);
                }
            });
        }

    }

    @Override
    public Character getCharacterForElement(int element) {

        String characterName = characterList.get(element).getName();
        return characterName.charAt(0);

    }

    //Contructor for HouseRV
    public CharactersRVAdapter(List<CharacterFormat> characterList, Context mContext) {
        this.characterList = characterList;
        this.mContext = mContext;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public CharactersRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.character_list_item_format,parent,false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(CharactersRVAdapter.ViewHolder holder, int position) {

        holder.name.setText(characterList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return characterList.size();
    }

}
