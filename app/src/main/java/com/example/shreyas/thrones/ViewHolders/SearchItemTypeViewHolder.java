package com.example.shreyas.thrones.ViewHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.example.shreyas.thrones.R;


/**
 * Created by Shreyas on 1/23/2017.
 */

public class SearchItemTypeViewHolder extends RecyclerView.ViewHolder {

    private TextView itemTypeText;
    private Context mContext;

    public SearchItemTypeViewHolder(View itemView, Context context) {
        super(itemView);
        itemTypeText = (TextView)itemView.findViewById(R.id.itemType);
        mContext = context;


    }

    public TextView getItemTypeTextView() {
        return itemTypeText;
    }

    public void setItemTypeTextView(TextView itemTypeText) {

        this.itemTypeText = itemTypeText;
    }
}
