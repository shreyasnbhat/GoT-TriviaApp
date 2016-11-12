package com.example.shreyas.thrones;



import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.turingtechnologies.materialscrollbar.INameableAdapter;

import java.util.List;


/**
 * Created by Shreyas on 11/11/2016.
 */

public class HousesRV extends RecyclerView.Adapter<HousesRV.ViewHolder> implements INameableAdapter {

   private List<HouseFormat> houseList;

   private Context mContext;


   //View Holder Definition
   public class ViewHolder extends RecyclerView.ViewHolder {

       TextView housename,words,region,coatOfArms,currentLord;

       public ViewHolder(View v) {
           super(v);
           housename = (TextView) v.findViewById(R.id.housename);
           words = (TextView)v.findViewById(R.id.words);
           currentLord = (TextView)v.findViewById(R.id.currentLord);
           region = (TextView)v.findViewById(R.id.region);
           coatOfArms = (TextView)v.findViewById(R.id.coatOfArms);
           v.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent intent = new Intent(mContext,HouseMemberDisplay.class);
                   String toSendHouseName = housename.getText().toString();

                   final int loc = getAdapterPosition();
                   Log.v("Position",loc+"");

                   intent.putExtra("houseName",toSendHouseName);
                   intent.putExtra("HousePosition",loc+"");
                   mContext.startActivity(intent);


               }
           });

       }
   }

    @Override
    public Character getCharacterForElement(int element) {

        String housename = houseList.get(element).getName();
        return housename.charAt(0);

    }

    //Contructor for HouseRV
    public HousesRV(List<HouseFormat> houseList, Context context) {
        this.houseList = houseList;
        this.mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public HousesRV.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.house_list_item_format,parent,false);

        return new ViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(HousesRV.ViewHolder holder, int position) {


        HouseFormat house = houseList.get(position);
        holder.housename.setText(house.getName());
        holder.words.setText(house.getWords());
        holder.coatOfArms.setText(house.getCoatOfArms());
        holder.currentLord.setText(house.getCurrentLord());
        holder.region.setText(house.getRegion());
    }

    @Override
    public int getItemCount() {
        return houseList.size();
    }
}

