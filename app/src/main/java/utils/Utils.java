package utils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class Utils {

	public static String leadZero(int p){
		if(p < 10) return "0" + p;
		else return String.valueOf(p);
	}
	
	public static void ShowToast(Context con, String msg) {
		Toast.makeText(con, msg, Toast.LENGTH_SHORT).show();
	}

	public static void ShowLogCat(String TAG, String MGS) {
		Log.e(TAG, MGS);
	}

	public static boolean isNetworkAvailable(Context con) {
		ConnectivityManager connectivity = (ConnectivityManager) con
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static void showAlert(Context c, String msg) {
		AlertDialog.Builder alert = new AlertDialog.Builder(c);
		alert.setMessage(msg);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {

			}
		}).show();
	}

	public static ProgressDialog show_ProgressDialog(Context context) {
		/*
		 * Dialog dialog = new Dialog(context);
		 * dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //
		 * dialog.setContentView(R.layout.loading_data); //
		 * dialog.setCancelable(true); dialog.getWindow().setBackgroundDrawable(
		 * new ColorDrawable(android.graphics.Color.TRANSPARENT));
		 * dialog.show(); return dialog;
		 */
		ProgressDialog pd = new ProgressDialog(context);
		pd.setMessage("Wait");
		return pd;
	}
	public static ProgressDialog show_ProgressDialog_for_register(Context context) {
		/*
		 * Dialog dialog = new Dialog(context);
		 * dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //
		 * dialog.setContentView(R.layout.loading_data); //
		 * dialog.setCancelable(true); dialog.getWindow().setBackgroundDrawable(
		 * new ColorDrawable(android.graphics.Color.TRANSPARENT));
		 * dialog.show(); return dialog;
		 */
		ProgressDialog pd = new ProgressDialog(context);
		pd.setMessage("Creating your profile");
		return pd;
	}
	
	
	/*
	 * Show Toast
	 */
	public static void show_Toast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	/*
	 * Show Progress Dialog
	 */

	private static ProgressDialog dialog;
	public static boolean listIsFullyDisplayed = false;
	public static boolean listIsFullyDisplayedForPhotos = false;

	public static ProgressDialog showDialog(String message, Context context) {
		dialog = new ProgressDialog(context);
		dialog.setMessage(message);
		dialog.setCancelable(true);
		dialog.setIndeterminate(false);
		dialog.show();
		return dialog;
	}

	/*
	 * Dismiss Dialog
	 */
	public static void dismissDialog(ProgressDialog dialog) {
		if (dialog != null && dialog.isShowing()) {
			dialog.cancel();
		}

	}

	/*
	 * Print Logcat with String
	 */
	public static void printLocCat(String TAG, String message) {
		Log.e(TAG, message);
	}
	
	public static String GetAddressFromLatLng(Context context, double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(context, Locale.US);
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                for(int i = 0; i < address.getMaxAddressLineIndex(); i++){
                    result.append(address.getAddressLine(i)).append(" ");
                }
//                result.append(address.getLocality()).append("\n");
//                result.append(address.getCountryName());
            }
        } catch (IOException e) {
        }

        return result.toString();
    }

}
