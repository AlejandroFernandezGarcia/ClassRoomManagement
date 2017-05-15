package es.udc.apm.classroommanagement.utils.vuforia;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;

import es.udc.apm.classroommanagement.services.RoomService;

/**
 * Created by Alejandro on 13/05/2017.
 */

public final class ScanningResultDialogHandler extends Handler {
    private final WeakReference<Activity> mActivity;
    // Constants for Hiding/Showing scanning result dialog
    public static final int HIDE_DIALOG = -1;
    public static final int SHOW_DIALOG = 1;

    public View mScanningResultContainer;


    public ScanningResultDialogHandler(Activity activity) {
        mActivity = new WeakReference<Activity>(activity);
    }


    public void handleMessage(Message msg) {
        Activity imageTargets = mActivity.get();

        if (msg.what == HIDE_DIALOG) {
            mScanningResultContainer.setVisibility(View.GONE);
        } else {
            if (mScanningResultContainer.getVisibility() != View.VISIBLE) {
                mScanningResultContainer.setVisibility(View.VISIBLE);
                try {
                    ((TextView) mScanningResultContainer).setText(new RoomService().getRoomInfo((short) msg.what).toString());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
