package es.udc.apm.classroommanagement.utils.vuforia;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by Alejandro on 13/05/2017.
 */

public final class LoadingDialogHandler extends Handler
{
    private final WeakReference<Activity> mActivity;
    // Constants for Hiding/Showing Loading dialog
    public static final int HIDE_LOADING_DIALOG = 0;
    public static final int SHOW_LOADING_DIALOG = 1;

    public View mLoadingDialogContainer;


    public LoadingDialogHandler(Activity activity)
    {
        mActivity = new WeakReference<Activity>(activity);
    }


    public void handleMessage(Message msg)
    {
        Activity imageTargets = mActivity.get();

        if (msg.what == SHOW_LOADING_DIALOG)
        {
            mLoadingDialogContainer.setVisibility(View.VISIBLE);

        } else if (msg.what == HIDE_LOADING_DIALOG)
        {
            mLoadingDialogContainer.setVisibility(View.GONE);
        }
    }

}
