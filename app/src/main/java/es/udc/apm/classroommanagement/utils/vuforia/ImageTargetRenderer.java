package es.udc.apm.classroommanagement.utils.vuforia;

import android.opengl.GLSurfaceView;

import com.vuforia.Device;
import com.vuforia.State;
import com.vuforia.Trackable;
import com.vuforia.TrackableResult;

import java.util.concurrent.ExecutionException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import es.udc.apm.classroommanagement.fragments.IndoorLocationFragment;
import es.udc.apm.classroommanagement.services.RoomService;

import static es.udc.apm.classroommanagement.utils.Utils.logInfo;

/**
 * Created by Alejandro on 13/05/2017.
 */

public class ImageTargetRenderer implements GLSurfaceView.Renderer, AppRendererControl {
    private ApplicationSession vuforiaAppSession;
    private IndoorLocationFragment mActivity;
    private AppRenderer mAppRenderer;
    private boolean mIsActive = false;

    public ImageTargetRenderer(IndoorLocationFragment activity, ApplicationSession session) {
        mActivity = activity;
        vuforiaAppSession = session;
        // AppRenderer used to encapsulate the use of RenderingPrimitives setting
        // the device mode AR/VR and stereo mode
        mAppRenderer = new AppRenderer(this, mActivity.getActivity(), Device.MODE.MODE_AR, false, 0.01f, 5f);
    }

    // Called to draw the current frame.
    @Override
    public void onDrawFrame(GL10 gl) {
        if (!mIsActive)
            return;

        // Call our function to render content from AppRenderer class
        mAppRenderer.render();
    }

    public void setActive(boolean active) {
        mIsActive = active;

        if (mIsActive)
            mAppRenderer.configureVideoBackground();
    }

    // Called when the surface is created or recreated.
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        logInfo(this, "GLRenderer.onSurfaceCreated");

        // Call Vuforia function to (re)initialize rendering after first use
        // or after OpenGL ES context was lost (e.g. after onPause/onResume):
        vuforiaAppSession.onSurfaceCreated();

        mAppRenderer.onSurfaceCreated();
    }

    // Called when the surface changed size.
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        logInfo(this, "GLRenderer.onSurfaceChanged");

        // Call Vuforia function to handle render surface size changes:
        vuforiaAppSession.onSurfaceChanged(width, height);

        // RenderingPrimitives to be updated when some rendering change is done
        mAppRenderer.onConfigurationChanged(mIsActive);

        initRendering();
    }

    // Function for initializing the renderer.
    private void initRendering() {

    }

    public void updateConfiguration() {
        mAppRenderer.onConfigurationChanged(mIsActive);
    }

    // The render function called from SampleAppRendering by using RenderingPrimitives views.
    // The state is owned by AppRenderer which is controlling it's lifecycle.
    // State should not be cached outside this method.
    public void renderFrame(State state, float[] projectionMatrix) {
        // Renders video background replacing Renderer.DrawVideoBackground()
        mAppRenderer.renderVideoBackground();

        //Found a trackeable element
        if (state.getNumTrackableResults() != 0) {
            TrackableResult result = state.getTrackableResult(0);
            Trackable trackable = result.getTrackable();
            logInfo(this, "Scanned " + trackable.getName());
            int roomId = Integer.parseInt(trackable.getName().replace("ROOM_", ""));

            mActivity.scanningResultDialogHandler
                    .sendEmptyMessage(roomId);
        } else {
            mActivity.scanningResultDialogHandler
                    .sendEmptyMessage(ScanningResultDialogHandler.HIDE_DIALOG);
        }
        VuforiaUtils.checkGLError("Render Frame");
    }
}

