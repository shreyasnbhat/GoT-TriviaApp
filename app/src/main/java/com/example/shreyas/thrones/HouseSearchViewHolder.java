package com.example.shreyas.thrones;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Shreyas on 1/22/2017.
 */

public class HouseSearchViewHolder extends RecyclerView.ViewHolder {

    private TextView houseName;

    public HouseSearchViewHolder(View itemView, final Context context) {
        super(itemView);
        houseName = (TextView)itemView.findViewById(R.id.name);

        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,CharacterInfoActivity.class);
                intent.putExtra("houseName",houseName.getText().toString());
                intent.putExtra("HousePosition",7);
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
}
