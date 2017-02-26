package com.example.shreyas.thrones.ViewHolders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.shreyas.thrones.HouseInfo;
import com.example.shreyas.thrones.ItemFormats.RealmHouseFormat;
import com.example.shreyas.thrones.R;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Shreyas on 1/22/2017.
 */

public class HouseSearchViewHolder extends RecyclerView.ViewHolder {

    private TextView houseName;
    private String houseId;
    private String region;
    private String words;
    private String currentLord;


    public HouseSearchViewHolder(View itemView, final Context context) {
        super(itemView);
        houseName = (TextView)itemView.findViewById(R.id.name);

        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,HouseInfo.class);
                intent.putExtra("houseName",houseName.getText().toString().replace("House ",""));
                intent.putExtra("HousePosition",houseId);
                intent.putExtra("region",region);
                intent.putExtra("currentLord",currentLord);
                intent.putExtra("words",words);

                context.startActivity(intent);
            }
        });
    }

    public void setName(TextView name) {
        this.houseName = name;
    }

    public TextView getHouseNameTextView() {
        return houseName;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setCurrentLord(String currentLord) {
        this.currentLord = currentLord;
    }

}
