package com.mykal.capturingphotos;

import android.content.Context;
import android.graphics.Camera;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import java.io.IOException;
import java.util.List;

/**
 * Created by mykal on 3/20/15.
 */
abstract public class Preview extends ViewGroup implements SurfaceHolder.Callback {
    SurfaceView mSurfaceView;
    SurfaceHolder mHolder;
    Camera mCamera;

    Preview (Context context) {
        super(context);

        mSurfaceView = new SurfaceView(context);
        mHolder = mSurfaceView.getHolder();

        addView(mSurfaceView);
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void setCamera(Camera camera) {

        if (mCamera == camera) {
            return;
        }

        stopPreviewFreeCamera();

        mCamera = camera;

        if (mCamera != null) {
            List<Size> localSizes = mCamera.getParameter().getSupportedPreviewSizes();
            mSupportedPreviews = localSizes;

            requestLayout();

            try {
                mCamera.setPreviewDisplay(mHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mCamera.startPreview();
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        Camera.Parameters parameters = mCamera.getParameters();

        parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
        requestLayout();
        mCamera.setParamaters(parameters);
        mCamera.startPreview;
    }

    @Override
    public void onClick(View view) {
        switch (mPreviewState) {
            case K_STATE_FROZEN:
                mCamera.startPreview();
                mPreviewState = K_STATE_PREVIEW;
                break;

            default:
                mCamera.takePicture(null, rawCallback, null);
                mPreviewState = K_STATE_BUSY;
        }

        shutterBtnConfig();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {

        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }

    public void stopPreviewFreeCamera() {

        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();

            mCamera = null;
        }
    }


}
