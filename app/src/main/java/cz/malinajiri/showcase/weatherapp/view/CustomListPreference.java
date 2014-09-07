package cz.malinajiri.showcase.weatherapp.view;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cz.malinajiri.showcase.weatherapp.R;
import cz.malinajiri.showcase.weatherapp.WeatherAppApplication;


public class CustomListPreference extends ListPreference {
    public CustomListPreference(Context context) {
        super(context);
    }


    public CustomListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onBindView(View view) {

        super.onBindView(view);
    }




    @Override
    public View getView(View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null) {
            LayoutInflater inflater = LayoutInflater.from(WeatherAppApplication
                    .getInstance());
            convertView = inflater.inflate(R.layout.preference_list_item, parent, false);
            holder = getViewHolder(convertView);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }


        holder.mSummary.setText(getSummary());
        holder.mTitle.setText(getTitle());



        return convertView;
    }


    private ViewHolder getViewHolder(View view) {
        ViewHolder holder = new ViewHolder();
        holder.mTitle = (TextView) view.findViewById(R.id.title);
        holder.mSummary = (TextView) view.findViewById(R.id.summary);
        return holder;
    }


    private class ViewHolder {
        private TextView mTitle;
        private TextView mSummary;
    }
}
