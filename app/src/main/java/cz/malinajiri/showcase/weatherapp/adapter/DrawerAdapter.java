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
import cz.malinajiri.showcase.weatherapp.entity.DrawerItemEntity;


public class DrawerAdapter extends ArrayAdapter<DrawerItemEntity> {



    public DrawerAdapter(Context context, List<DrawerItemEntity> objects) {
        super(context, 0, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(WeatherAppApplication
                    .getInstance());
            convertView = inflater.inflate(R.layout.drawer_item,
                    parent, false);

            holder = getViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        final DrawerItemEntity item = getItem(position);

        holder.mName.setText(item.getName());

        holder.mImg.setImageResource(item.getImgId());

        return convertView;

    }


    private ViewHolder getViewHolder(View view) {
        ViewHolder holder = new ViewHolder();
        holder.mName = (TextView) view.findViewById(R.id.section_title);
        holder.mImg = (ImageView) view.findViewById(R.id.section_img);
        return holder;
    }


    public class ViewHolder {
        private TextView mName;
        private ImageView mImg;
    }
}
