package es.udc.apm.classroommanagement.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v13.app.FragmentCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.content.DialogInterface;
import android.view.ViewGroup.LayoutParams;

import com.vuforia.CameraDevice;
import com.vuforia.DataSet;
import com.vuforia.ObjectTracker;
import com.vuforia.STORAGE_TYPE;
import com.vuforia.State;
import com.vuforia.Trackable;
import com.vuforia.Tracker;
import com.vuforia.TrackerManager;
import com.vuforia.Vuforia;

import java.util.ArrayList;

import es.udc.apm.classroommanagement.MainActivity;
import es.udc.apm.classroommanagement.R;
import es.udc.apm.classroommanagement.utils.Constants;
import es.udc.apm.classroommanagement.utils.vuforia.ApplicationSession;
import es.udc.apm.classroommanagement.utils.vuforia.ImageTargetRenderer;
import es.udc.apm.classroommanagement.utils.vuforia.LoadingDialogHandler;
import es.udc.apm.classroommanagement.utils.vuforia.ApplicationControl;
import es.udc.apm.classroommanagement.utils.vuforia.ApplicationException;
import es.udc.apm.classroommanagement.utils.vuforia.ApplicationGLView;
import es.udc.apm.classroommanagement.utils.vuforia.ScanningResultDialogHandler;

import static es.udc.apm.classroommanagement.utils.Utils.showToast;
import static es.udc.apm.classroommanagement.utils.Utils.logError;
import static es.udc.apm.classroommanagement.utils.Utils.logInfo;

public class IndoorLocationFragment extends Fragment implements ApplicationControl {

    ///region Var

    ApplicationSession vuforiaAppSession;

    private DataSet mCurrentDataset;
    private int mCurrentDatasetSelectionIndex = 0;
    private int mStartDatasetsIndex = 0;
    private int mDatasetsNumber = 0;
    private boolean mContAutofocus = false;
    private ArrayList<String> mDatasetStrings = new ArrayList<String>();

    // Our OpenGL view:
    private ApplicationGLView mGlView;

    // Our renderer:
    private ImageTargetRenderer mRenderer;

    private boolean mSwitchDatasetAsap = false;
    private boolean mExtendedTracking = false;

    private RelativeLayout mUILayout;
    private RelativeLayout scanResultLayout;

    public LoadingDialogHandler loadingDialogHandler = new LoadingDialogHandler(getActivity());
    public ScanningResultDialogHandler scanningResultDialogHandler;

    // Alert Dialog used to display SDK errors
    private AlertDialog mErrorDialog;

    boolean mIsDroidDevice = false;

    ///endregion

    public IndoorLocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).showLateralMenu(true);

        vuforiaAppSession = new ApplicationSession(this);

        if (ContextCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{android.Manifest.permission.CAMERA}, Constants.REQUEST_CAMERA_PERMISSION);
        } else {


            startLoadingAnimation();
            initScanningResultView();
            mDatasetStrings.add("ApmMarks.xml");

            vuforiaAppSession.initAR(getActivity(), ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            //        mGestureDetector = new GestureDetector(this, new GestureListener());

            mIsDroidDevice = android.os.Build.MODEL.toLowerCase().startsWith(
                    "droid");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Constants.REQUEST_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showToast(getActivity().getApplicationContext(), "No tiene permisos para acceder a la cámara");
                }
                break;
            case Constants.REQUEST_VIBRATOR_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_indoor_location, container, false);
        return mGlView;
    }

    // Process Single Tap event to trigger autofocus
//    private class GestureListener extends
//            GestureDetector.SimpleOnGestureListener
//    {
//        // Used to set autofocus one second after a manual focus is triggered
//        private final Handler autofocusHandler = new Handler();
//
//
//        @Override
//        public boolean onDown(MotionEvent e)
//        {
//            return true;
//        }
//
//
//        @Override
//        public boolean onSingleTapUp(MotionEvent e)
//        {
//            // Generates a Handler to trigger autofocus
//            // after 1 second
//            autofocusHandler.postDelayed(new Runnable()
//            {
//                public void run()
//                {
//                    boolean result = CameraDevice.getInstance().setFocusMode(
//                            CameraDevice.FOCUS_MODE.FOCUS_MODE_TRIGGERAUTO);
//
//                    if (!result)
//                        Log.e("SingleTapUp", "Unable to trigger focus");
//                }
//            }, 1000L);
//
//            return true;
//        }
//    }

    // Called when the activity will start interacting with the user.
    @Override
    public void onResume() {
        super.onResume();

        // This is needed for some Droid devices to force portrait
        if (mIsDroidDevice) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        try {
            vuforiaAppSession.resumeAR();
        } catch (ApplicationException e) {
            logError(this, e);
        }

        // Resume the GL view:
        if (mGlView != null) {
            mGlView.setVisibility(View.VISIBLE);
            mGlView.onResume();
        }

    }


    // Callback for configuration changes the activity handles itself
    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);

        vuforiaAppSession.onConfigurationChanged();
    }


    // Called when the system is about to start resuming a previous activity.
    @Override
    public void onPause() {
        super.onPause();

        if (mGlView != null) {
            mGlView.setVisibility(View.INVISIBLE);
            mGlView.onPause();
        }

        try {
            vuforiaAppSession.pauseAR();
        } catch (ApplicationException e) {
            logError(this, e.getString());
        }
    }

    // The final call you receive before your activity is destroyed.
    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            vuforiaAppSession.stopAR();
        } catch (ApplicationException e) {
            logError(this, e);
        }

        System.gc();
    }

    // Initializes AR application components.
    private void initApplicationAR() {
        // Create OpenGL ES view:
        int depthSize = 16;
        int stencilSize = 0;
        boolean translucent = Vuforia.requiresAlpha();

        mGlView = new ApplicationGLView(this.getContext());
        mGlView.init(translucent, depthSize, stencilSize);

        mRenderer = new ImageTargetRenderer(this, vuforiaAppSession);
        mGlView.setRenderer(mRenderer);
    }

    private void startLoadingAnimation() {
        mUILayout = (RelativeLayout) View.inflate(this.getActivity(), R.layout.camera_overlay_progress_bar,
                null);

        mUILayout.setVisibility(View.VISIBLE);
        mUILayout.setBackgroundColor(Color.BLACK);

        // Gets a reference to the loading dialog
        loadingDialogHandler.mLoadingDialogContainer = mUILayout.findViewById(R.id.loading_indicator);

        // Shows the loading indicator at start
        loadingDialogHandler
                .sendEmptyMessage(LoadingDialogHandler.SHOW_LOADING_DIALOG);

        // Adds the inflated layout to the view
        getActivity().addContentView(mUILayout, new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));

    }

    private void initScanningResultView() {
        scanningResultDialogHandler = new ScanningResultDialogHandler(this);
        scanResultLayout = (RelativeLayout) View.inflate(this.getActivity(), R.layout.camera_overlay_scanning_result, null);
        scanResultLayout.setVisibility(View.VISIBLE);

        scanningResultDialogHandler.mScanningResultContainer = scanResultLayout.findViewById(R.id.scanning_result);
        scanningResultDialogHandler.sendEmptyMessage(ScanningResultDialogHandler.HIDE_DIALOG);
        getActivity().addContentView(scanResultLayout, new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
    }

    // Methods to load and destroy tracking data.
    @Override
    public boolean doLoadTrackersData() {
        TrackerManager tManager = TrackerManager.getInstance();
        ObjectTracker objectTracker = (ObjectTracker) tManager
                .getTracker(ObjectTracker.getClassType());
        if (objectTracker == null)
            return false;

        if (mCurrentDataset == null)
            mCurrentDataset = objectTracker.createDataSet();

        if (mCurrentDataset == null)
            return false;

        if (!mCurrentDataset.load(
                mDatasetStrings.get(mCurrentDatasetSelectionIndex),
                STORAGE_TYPE.STORAGE_APPRESOURCE))
            return false;

        if (!objectTracker.activateDataSet(mCurrentDataset))
            return false;

        int numTrackables = mCurrentDataset.getNumTrackables();
        for (int count = 0; count < numTrackables; count++) {
            Trackable trackable = mCurrentDataset.getTrackable(count);
            if (isExtendedTrackingActive()) {
                trackable.startExtendedTracking();
            }

            String name = "Current Dataset : " + trackable.getName();
            trackable.setUserData(name);
            logInfo(this, "UserData:Set the following user data "
                    + (String) trackable.getUserData());
        }
        return true;
    }

    @Override
    public boolean doUnloadTrackersData() {
        // Indicate if the trackers were unloaded correctly
        boolean result = true;

        TrackerManager tManager = TrackerManager.getInstance();
        ObjectTracker objectTracker = (ObjectTracker) tManager
                .getTracker(ObjectTracker.getClassType());
        if (objectTracker == null)
            return false;

        if (mCurrentDataset != null && mCurrentDataset.isActive()) {
            if (objectTracker.getActiveDataSet(0).equals(mCurrentDataset)
                    && !objectTracker.deactivateDataSet(mCurrentDataset)) {
                result = false;
            } else if (!objectTracker.destroyDataSet(mCurrentDataset)) {
                result = false;
            }

            mCurrentDataset = null;
        }

        return result;
    }

    @Override
    public void onInitARDone(ApplicationException exception) {

        if (exception == null) {
            initApplicationAR();

            mRenderer.setActive(true);

            // Now add the GL surface view. It is important
            // that the OpenGL ES surface view gets added
            // BEFORE the camera is started and video
            // background is configured.
            getActivity().addContentView(mGlView, new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));

            // Sets the UILayout to be drawn in front of the camera
            mUILayout.bringToFront();
            scanResultLayout.bringToFront();
            // Sets the layout background to transparent
            mUILayout.setBackgroundColor(Color.TRANSPARENT);

            try {
                vuforiaAppSession.startAR(CameraDevice.CAMERA_DIRECTION.CAMERA_DIRECTION_DEFAULT);
            } catch (ApplicationException e) {
                logError(this, e);
            }

            boolean result = CameraDevice.getInstance().setFocusMode(
                    CameraDevice.FOCUS_MODE.FOCUS_MODE_CONTINUOUSAUTO);

            if (result)
                mContAutofocus = true;
            else
                logInfo(this, "Unable to enable continuous autofocus");

            loadingDialogHandler.sendEmptyMessage(LoadingDialogHandler.HIDE_LOADING_DIALOG);

        } else {
            logError(this, exception);
            showInitializationErrorMessage(exception.getString());
        }
    }

    // Shows initialization error messages as System dialogs
    public void showInitializationErrorMessage(String message) {
        final String errorMessage = message;
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                if (mErrorDialog != null) {
                    mErrorDialog.dismiss();
                }

                // Generates an Alert Dialog to show the error message
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        getActivity());
                builder
                        .setMessage(errorMessage)
                        .setTitle(getString(R.string.INIT_ERROR))
                        .setCancelable(false)
                        .setIcon(0)
                        .setPositiveButton(getString(R.string.button_OK),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        getActivity().finish();
                                    }
                                });

                mErrorDialog = builder.create();
                mErrorDialog.show();
            }
        });
    }

    @Override
    public void onVuforiaUpdate(State state) {
        if (mSwitchDatasetAsap) {
            mSwitchDatasetAsap = false;
            TrackerManager tm = TrackerManager.getInstance();
            ObjectTracker ot = (ObjectTracker) tm.getTracker(ObjectTracker
                    .getClassType());
            if (ot == null || mCurrentDataset == null
                    || ot.getActiveDataSet(0) == null) {
                logInfo(this, "Failed to swap datasets");
                return;
            }

            doUnloadTrackersData();
            doLoadTrackersData();
        }
    }

    @Override
    public boolean doInitTrackers() {
        // Indicate if the trackers were initialized correctly
        boolean result = true;

        TrackerManager tManager = TrackerManager.getInstance();
        Tracker tracker;

        // Trying to initialize the image tracker
        tracker = tManager.initTracker(ObjectTracker.getClassType());
        if (tracker == null) {
            logError(this, "Tracker not initialized. Tracker already initialized or the camera is already started");
            result = false;
        } else {
            logInfo(this, "Tracker successfully initialized");
        }
        return result;
    }

    @Override
    public boolean doStartTrackers() {
        // Indicate if the trackers were started correctly
        boolean result = true;

        Tracker objectTracker = TrackerManager.getInstance().getTracker(
                ObjectTracker.getClassType());
        if (objectTracker != null)
            objectTracker.start();

        return result;
    }

    @Override
    public boolean doStopTrackers() {
        // Indicate if the trackers were stopped correctly
        boolean result = true;

        Tracker objectTracker = TrackerManager.getInstance().getTracker(
                ObjectTracker.getClassType());
        if (objectTracker != null)
            objectTracker.stop();

        return result;
    }

    @Override
    public boolean doDeinitTrackers() {
        // Indicate if the trackers were deinitialized correctly
        boolean result = true;

        TrackerManager tManager = TrackerManager.getInstance();
        tManager.deinitTracker(ObjectTracker.getClassType());

        return result;
    }

    public boolean isExtendedTrackingActive() {
        return mExtendedTracking;
    }
}
