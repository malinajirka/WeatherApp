package cz.malinajiri.showcase.weatherapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cz.malinajiri.showcase.weatherapp.R;
import cz.malinajiri.showcase.weatherapp.WeatherAppApplication;
import cz.malinajiri.showcase.weatherapp.database.model.LocationWeatherDatabaseEntity;


public class LocationWeatherAdapter extends ArrayAdapter<LocationWeatherDatabaseEntity> {

    private List<LocationWeatherDatabaseEntity> mObjects;
    private Context mContext;

    public LocationWeatherAdapter(Context context, List<LocationWeatherDatabaseEntity> objects) {
        super(context, 0, objects);
        this.mObjects = objects;
        mContext = context;

    }


    public void swapData(List<LocationWeatherDatabaseEntity> data) {
        mObjects.clear();
        mObjects.addAll(data);
        notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(WeatherAppApplication
                    .getInstance());
            convertView =inflater.inflate(R.layout.list_item_default, parent, false);
            holder = getViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        LocationWeatherDatabaseEntity item = getItem(position);

        if(item.isCurrentLocation()){
            holder.mTitle.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_current,0);
            holder.mTitle.setCompoundDrawablePadding((int) mContext.getResources().getDimension(R.dimen.fragment_location_list_drawable_padding));
        }else{
            holder.mTitle.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        }
        holder.mTitle.setText(item.getCity());

        holder.mSubtitle.setText(item.getCity());

        holder.mTemperature.setText(item.getFormattedTemp());

        holder.mWeatherImg.setImageDrawable(mContext.getResources().getDrawable(item.getWeatherImageDrawable()));

        return convertView;

    }


    private ViewHolder getViewHolder(View view) {
        ViewHolder holder = new ViewHolder();
        holder.mWeatherImg = (ImageView) view.findViewById(R.id.weather_img);
        holder.mTitle = (TextView) view.findViewById(R.id.title);
        holder.mSubtitle = (TextView) view.findViewById(R.id.subtitle);
        holder.mTemperature = (TextView) view.findViewById(R.id.temperature);
        return holder;
    }


    private class ViewHolder {
        private ImageView mWeatherImg;
        private TextView mTitle;
        private TextView mSubtitle;
        private TextView mTemperature;
    }

}
