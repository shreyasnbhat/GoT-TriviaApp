package com.example.shreyas.thrones;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by Shreyas on 1/22/2017.
 */

public class CharacterSearchViewHolder extends RecyclerView.ViewHolder {

    private TextView name;
    private TextView playedBy;
    private String born;
    private String died;
    private String imageUrl;
    private String gender;
    private SimpleDraweeView characterImage;



    public CharacterSearchViewHolder(View itemView, final Context context) {
        super(itemView);
        name = (TextView)itemView.findViewById(R.id.name);
        playedBy = (TextView)itemView.findViewById(R.id.playedBy);
        characterImage = (SimpleDraweeView)itemView.findViewById(R.id.character_image);

        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,CharacterInfoActivity.class);
                intent.putExtra("CharacterName",name.getText().toString());
                intent.putExtra("playedBy",playedBy.getText().toString());
                intent.putExtra("gender",gender);
                intent.putExtra("died",died);
                intent.putExtra("born",born);
                intent.putExtra("imageUrl",imageUrl);
                context.startActivity(intent);
            }
        });
    }

    public TextView getNameTextView()
    {
        return this.name;
    }

    public TextView getPlayedByTextView()
    {
        return this.playedBy;
    }

    public void setNameTextView(TextView name) {
        this.name = name;
    }

    public void setPlayedByTextView(TextView playedBy) {
        this.playedBy = playedBy;
    }

    public void setBorn(String born) {
        this.born = born;
    }

    public void setDied(String died) {
        this.died = died;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public SimpleDraweeView getCharacterImageView() {
        return characterImage;
    }
}
