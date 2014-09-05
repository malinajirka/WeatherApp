package cz.malinajiri.showcase.weatherapp.utility;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.util.LruCache;


public class TypefaceUtils {

    private static LruCache<String, Typeface> mTypefaceCache = new LruCache<String, Typeface>(
            5);


    public static Typeface getTypeface(String fontName, Context ctx) {
        Typeface typeface = mTypefaceCache.get(fontName);
        if (fontName != null) {
            typeface = Typeface.createFromAsset(ctx.getAssets(),
                    "fonts/" + fontName);
            mTypefaceCache.put(fontName, typeface);
        }
        return typeface;
    }
}
