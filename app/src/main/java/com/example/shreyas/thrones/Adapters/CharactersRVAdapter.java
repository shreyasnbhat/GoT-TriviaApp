package com.example.shreyas.thrones.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.shreyas.thrones.ItemFormats.CharacterFormat;
import com.example.shreyas.thrones.CharacterInfoActivity;
import com.example.shreyas.thrones.HouseInfo;
import com.example.shreyas.thrones.ItemFormats.RealmCharacterFormat;
import com.example.shreyas.thrones.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.turingtechnologies.materialscrollbar.INameableAdapter;

import java.util.List;

/**
 * Created by Shreyas on 11/13/2016.
 */

public class CharactersRVAdapter extends RecyclerView.Adapter<CharactersRVAdapter.ViewHolder> implements INameableAdapter {

    private List<RealmCharacterFormat> characterList;

    private Context mContext;

    //View Holder Definition
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        SimpleDraweeView image;
        FrameLayout parentFrame;


        public ViewHolder(View v)
        {
            super(v);
            name = (TextView)v.findViewById(R.id.name);
            image = (SimpleDraweeView)v.findViewById(R.id.character_image);
            parentFrame = (FrameLayout)v.findViewById(R.id.parent_frame);

            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(mContext,CharacterInfoActivity.class);
                    intent.putExtra("CharacterName",name.getText().toString());

                    RealmCharacterFormat character = new RealmCharacterFormat();

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
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity)mContext, image, "characterImageTransition");
                    mContext.startActivity(intent,options.toBundle());
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
    public CharactersRVAdapter(List<RealmCharacterFormat> characterList, Context mContext) {
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
        if(mContext instanceof HouseInfo)
        {
            holder.parentFrame.setLayoutParams(new FrameLayout.LayoutParams(450, FrameLayout.LayoutParams.MATCH_PARENT));
            holder.parentFrame.setPadding(8,8,8,8);

        }
        else{
            holder.parentFrame.setBackgroundColor(0xff1e1e1e);
        }
        if(characterList.get(position).getImageUrl()!=null) {
            Uri imageUri = Uri.parse(characterList.get(position).getImageUrl());
            holder.image.setImageURI(imageUri);
        }
        else
        {
            Uri imageUri = Uri.parse("https://www.couchcms.com/forum/styles/comboot/theme/images/default_avatar.jpg");
            holder.image.setImageURI(imageUri);
        }

    }

    @Override
    public int getItemCount() {
        return characterList.size();
    }

}
