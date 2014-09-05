package cz.malinajiri.showcase.weatherapp.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.malinajiri.showcase.weatherapp.R;
import cz.malinajiri.showcase.weatherapp.WeatherAppApplication;
import cz.malinajiri.showcase.weatherapp.adapter.SimpleListDialogAdapter;
import cz.malinajiri.showcase.weatherapp.listener.OnListDialogItemClickedListener;
import cz.malinajiri.showcase.weatherapp.utility.TypefaceUtils;

public class ListDialogFragment extends DialogFragment {

	private static final String TITLE = "title";
	private static final String ITEMS = "items";

	private int dialogRequestCode;
	private static final String REQUEST_CODE = "requestcode";

	/**
	 * 
	 * @param title
	 *            Title of your dialog
	 * 
	 * @param requestCode
	 *            Request code for settarget. You can use this code in
	 *            AlertDialogFragmentListener implementation to determine which
	 *            of your dialogs is displayed (if you have multiple dialogs in
	 *            one fragment).
	 * @param items
	 *            Id of array resource
	 * @param targetFragment
	 *            Target fragment of this dialog.
	 * @return New instance of an list dialog.
	 */
	public static ListDialogFragment newInstance(int title, int items,
			int requestCode, Fragment targetFragment) {

		ListDialogFragment frag = new ListDialogFragment();
		frag.setTargetFragment(targetFragment, requestCode);
		frag.dialogRequestCode = requestCode;
		Bundle args = new Bundle();
		args.putInt(ITEMS, items);
		args.putInt(TITLE, title);
		frag.setArguments(args);
		return frag;
	}





    @Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// setRetainInstance(true);
		if (savedInstanceState != null) {
			dialogRequestCode = savedInstanceState.getInt(REQUEST_CODE);
		}


	}


    @Override
	public void onSaveInstanceState(Bundle arg0) {
		arg0.putInt(REQUEST_CODE, dialogRequestCode);
		super.onSaveInstanceState(arg0);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		Bundle args = getArguments();
		if (args == null) {
			return null;
		}
		int title = args.getInt(TITLE);

		int items = args.getInt(ITEMS);
        String[] itemsArray = getResources().getStringArray(items);

        List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
        for(int i = 0; i < itemsArray.length; i++){
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(SimpleListDialogAdapter.MAP_KEY,itemsArray[i]);
            fillMaps.add(map);
        }
        int[] to = new int[] { R.id.title};


		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title).setAdapter(new SimpleListDialogAdapter(getActivity(),fillMaps,
                R.layout.list_dialog_layout,new String[] {SimpleListDialogAdapter.MAP_KEY},to),
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ((OnListDialogItemClickedListener) getTargetFragment())
                        .onDialogItemClicked(which, dialogRequestCode);

            }
        });
		return builder.create();
	}



    private class SimpleListDialogAdapter extends SimpleAdapter {

       protected final static String MAP_KEY = "title";
        /**
         * Constructor
         *
         * @param context  The context where the View associated with this SimpleAdapter is running
         * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
         *                 Maps contain the data for each row, and should include all the entries specified in
         *                 "from"
         * @param resource Resource identifier of a view layout that defines the views for this list
         *                 item. The layout file should include at least those named views defined in "to"
         * @param from     A list of column names that will be added to the Map associated with each
         *                 item.
         * @param to       The views that should display column in the "from" parameter. These should all be
         *                 TextViews. The first N views in this list are given the values of the first N columns
         */
        public SimpleListDialogAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(WeatherAppApplication
                        .getInstance());
                convertView =inflater.inflate(R.layout.list_item_dialog, parent, false);
                holder = getViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Map<String, String> item = (Map<String, String>) getItem(position);

            holder.mTitle.setText(item.get(MAP_KEY).toString());


            return convertView;

        }


        private ViewHolder getViewHolder(View view) {
            ViewHolder holder = new ViewHolder();
            holder.mTitle = (TextView) view.findViewById(R.id.title);
            return holder;
        }


        private class ViewHolder {
            private TextView mTitle;
        }
    }
}
