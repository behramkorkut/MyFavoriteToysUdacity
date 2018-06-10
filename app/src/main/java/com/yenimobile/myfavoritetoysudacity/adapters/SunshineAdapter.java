package com.yenimobile.myfavoritetoysudacity.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yenimobile.myfavoritetoysudacity.R;
import com.yenimobile.myfavoritetoysudacity.utilities.ColorUtils;

public class SunshineAdapter extends RecyclerView.Adapter<SunshineAdapter.SunshineViewHolder> {

    private String[] mWeatherData;


    public void setmWeatherData(String[] data){
        mWeatherData = data;
        notifyDataSetChanged();
    }

    public interface SunshineClickInterface {
        void onSunshineItemClick(String weatherString);
    }


    private int mCount;
    private final SunshineClickInterface mClickInterface;

    public SunshineAdapter(int count, SunshineClickInterface clickListener){
        mCount = count;
        mClickInterface = clickListener;
    }

    @Override
    public SunshineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =  inflater.inflate(R.layout.sunshine_recycler_item_layout, parent, false);
        SunshineViewHolder sunshineViewHolder = new SunshineViewHolder(view);

        return sunshineViewHolder;
    }

    @Override
    public void onBindViewHolder(SunshineViewHolder holder, int position) {
        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        if (mWeatherData == null) {
            return 0;
        }else {
            return mWeatherData.length;
        }
    }


    //--------------------------------the view holder----------------------------------------------
    class SunshineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mWeathertv, mTemperatureTV;

        public SunshineViewHolder(View itemView) {
            super(itemView);
            mWeathertv = itemView.findViewById(R.id.weather_description);
            mTemperatureTV = itemView.findViewById(R.id.high_temperature);
            itemView.setOnClickListener(this);
        }

        public void bind(int index){

            mWeathertv.setText(mWeatherData[index]);
            mTemperatureTV.setText(String.valueOf(index));

            if (index %2 == 0 ){
                itemView.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.material50Green));
            }else {
                itemView.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.material150Green));
            }

        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            String weatherString = mWeatherData[clickedPosition];
            mClickInterface.onSunshineItemClick(weatherString);
            //mWeathertv.setText(String.valueOf(clickedPosition));
        }
    }
    //--------------------------------end of the view holder----------------------------------------





}
