package com.example.shreyas.thrones;



import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.List;


/**
 * Created by Shreyas on 11/11/2016.
 */

public class HousesRV extends RecyclerView.Adapter<HousesRV.ViewHolder> {

   private List<HouseDetails> houseList;

   private Context mContext;

   //View Holder Definition
   public class ViewHolder extends RecyclerView.ViewHolder {

       TextView housename;

       public ViewHolder(View v) {
           super(v);
           housename = (TextView) v.findViewById(R.id.housename);
       }
   }

    //Contructor for HouseRV
    public HousesRV(List<HouseDetails> houseList,Context context) {
        this.houseList = houseList;
        this.mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public HousesRV.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.house_list_item_format,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HousesRV.ViewHolder holder, int position) {

        HouseDetails house = houseList.get(position);
        holder.housename.setText(house.getName());
    }

    @Override
    public int getItemCount() {
        return houseList.size();
    }
}

