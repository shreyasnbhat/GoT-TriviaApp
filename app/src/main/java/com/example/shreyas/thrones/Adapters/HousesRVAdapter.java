package com.example.shreyas.thrones.Adapters;



import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shreyas.thrones.ItemFormats.HouseFormat;
import com.example.shreyas.thrones.HouseInfo;
import com.example.shreyas.thrones.R;
import com.example.shreyas.thrones.RealmHouseFormat;
import com.turingtechnologies.materialscrollbar.INameableAdapter;

import java.util.List;


/**
 * Created by Shreyas on 11/11/2016.
 */

public class HousesRVAdapter extends RecyclerView.Adapter<HousesRVAdapter.ViewHolder> implements INameableAdapter {

   private List<RealmHouseFormat> houseList;

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
                   Intent intent = new Intent(mContext,HouseInfo.class);
                   String toSendHouseName = housename.getText().toString();
                   toSendHouseName = toSendHouseName.replace("House ","");
                   final int loc = getAdapterPosition() + 1;
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
        housename = housename.replace("House ","");
        return housename.charAt(0);

    }



    //Contructor for HouseRV
    public HousesRVAdapter(List<RealmHouseFormat> houseList, Context context) {
        this.houseList = houseList;
        this.mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public HousesRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.house_list_item_format,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HousesRVAdapter.ViewHolder holder, int position) {


        RealmHouseFormat house = houseList.get(position);
        holder.housename.setText(house.getName().replace("House ",""));
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

