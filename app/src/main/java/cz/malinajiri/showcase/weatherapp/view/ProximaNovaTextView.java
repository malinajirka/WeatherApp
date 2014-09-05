package cz.malinajiri.showcase.weatherapp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import cz.malinajiri.showcase.weatherapp.R;
import cz.malinajiri.showcase.weatherapp.utility.TypefaceUtils;


public class ProximaNovaTextView extends TextView {


    public ProximaNovaTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {
            init(attrs);
        }
    }


    public ProximaNovaTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init(attrs);
        }

    }


    public ProximaNovaTextView(Context context) {
        super(context);
        if (!isInEditMode()) {
            init(null);
        }
    }


    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs,
                    R.styleable.ProximaNova);
            String fontName = a.getString(R.styleable.ProximaNova_fontName);
            setTypeface(TypefaceUtils.getTypeface(fontName, getContext()));
            a.recycle();
        }
    }

}
