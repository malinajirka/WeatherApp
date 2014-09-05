package cz.malinajiri.showcase.weatherapp.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import cz.malinajiri.showcase.weatherapp.listener.OnListDialogItemClickedListener;

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
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title).setItems(items,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						((OnListDialogItemClickedListener) getTargetFragment())
								.onDialogItemClicked(which, dialogRequestCode);

					}
				});
		return builder.create();
	}
}
