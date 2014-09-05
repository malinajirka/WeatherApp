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
import cz.malinajiri.showcase.weatherapp.entity.gson.ServerForecastEntity;


public class DailyForecastAdapter  extends ArrayAdapter<ServerForecastEntity> {

        private List<ServerForecastEntity> mObjects;
        private Context mContext;

        public DailyForecastAdapter(Context context, List<ServerForecastEntity> objects) {
            super(context, 0, objects);
            this.mObjects = objects;
            mContext = context;

        }


        public void swapData(List<ServerForecastEntity> data) {
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

            ServerForecastEntity item = getItem(position);

            holder.mTitle.setText(item.getDay());

            holder.mSubtitle.setText(item.getWeather());

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

