package es.udc.apm.classroommanagement.utils.vuforia;

import com.vuforia.State;

/**
 * Created by Alejandro on 13/05/2017.
 */

public interface AppRendererControl {

    // This method has to be implemented by the Renderer class which handles the content rendering
    // of the sample, this one is called from SampleAppRendering class for each view inside a loop
    void renderFrame(State state, float[] projectionMatrix);

}