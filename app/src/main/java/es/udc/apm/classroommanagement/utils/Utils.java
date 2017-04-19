package es.udc.apm.classroommanagement.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by danib on 21/03/2017.
 */

public class Utils {

    public static void showToast(Context context, String message) {
        Toast toast =
                Toast.makeText(context,
                        message, Toast.LENGTH_LONG);
        toast.show();
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void logInfo(Object obj,String message){
        Log.i(obj.getClass().getSimpleName(),message);
    }

    public static void logError(Object obj,Exception e){
        Log.e(obj.getClass().toString(),e.getMessage());
    }

}
