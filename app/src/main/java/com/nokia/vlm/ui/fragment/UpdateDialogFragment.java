package com.nokia.vlm.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.nokia.qrcode.camera.CameraManager;
import com.nokia.qrcode.camera.PreviewFrameShotListener;
import com.nokia.qrcode.camera.Size;
import com.nokia.qrcode.decode.DecodeListener;
import com.nokia.qrcode.decode.DecodeThread;
import com.nokia.qrcode.decode.LuminanceSource;
import com.nokia.qrcode.decode.PlanarYUVLuminanceSource;
import com.nokia.qrcode.decode.RGBLuminanceSource;
import com.nokia.qrcode.view.CaptureView;
import com.nokia.vlm.R;
import com.nokia.vlm.ui.activity.QRResultActivity;
import com.qx.framelib.utlis.ZLog;

import static android.content.Context.VIBRATOR_SERVICE;
import static com.nokia.vlm.ui.activity.QRCodeCaptureActivity.VIBRATE_DURATION;

/**
 * @创建者 DK-house
 * @创建时间 2017/12/20 10:40
 * @描述 ${TODO}
 * @更新者 ${AUTHOR}
 * @更新时间 2017/12/20
 * @更新描述 ${TODO}
 */

public class UpdateDialogFragment extends DialogFragment implements SurfaceHolder.Callback,
        PreviewFrameShotListener, DecodeListener, View.OnClickListener {
    private int     mEdittextId;
    private Context context;
    private boolean isToShow;
    private boolean isDecoding = false;
    private String mDecodedData;
    private CameraManager  mCameraManager;
    private DecodeThread   mDecodeThread;
    private Rect           previewFrameRect;


    private OnClickListener listener;
    private LinearLayout    mLlActions;
    private LinearLayout   mLlEdittextStyle;
    private LinearLayout   mLlEdittext;
    private LinearLayout   mLlContentView;
    private ImageView      mIVEdittext;
    public  Button         mBtnCancel;
    public  Button         mBtnApply;
    private EditText       mEtTobeUpdated;
    private SurfaceView    mSurfaceView;
    private CaptureView    mCaptureView;
    private RelativeLayout mRootlayout;
    private String mTextForET;
    //    private LinearLayout mRootlayout;


    public EditText getEtTobeUpdated() {
        return mEtTobeUpdated;
    }

    public UpdateDialogFragment(Context context, int edittextId, boolean isToShow) {
        super();

        this.context = context;
        this.mEdittextId = edittextId;
        this.isToShow = isToShow;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ZLog.d("UpdateDialogFragment", "111111");
        initListener();
        initEdittextStyle();

        //        ZLog.d("UpdateDialogFragment mRootlayout.getHeight",String.valueOf(mRootlayout.getHeight()));
        //        ZLog.d("UpdateDialogFragment mRootlayout.getWidth",String.valueOf(mRootlayout.getWidth()));
        //
        //        ZLog.d("UpdateDialogFragment mSurfaceView.getHeight",String.valueOf(mSurfaceView.getLayoutParams().height));
        //        ZLog.d("UpdateDialogFragment mSurfaceView.getWidth",String.valueOf(mSurfaceView.getLayoutParams().width));
        //
        //
        //        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        //        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        //        mRootlayout.measure(w, h);
        //        int height = mRootlayout.getMeasuredHeight();
        //        int width = mRootlayout.getMeasuredWidth();
        //        ZLog.d("UpdateDialogFragment","measure width=" + width + " height=" + height);

        mSurfaceView.getHolder().addCallback(this);
        mCameraManager = new CameraManager(getActivity());
        mCameraManager.setPreviewFrameShotListener(this);



        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZLog.d("UpdateDialogFragment", "33333");


    }


    private void initListener() {
        mBtnCancel.setOnClickListener(this);
        mBtnApply.setOnClickListener(this);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ZLog.d("UpdateDialogFragment", "22222");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View           view     = inflater.inflate(R.layout.fragment_dialog, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);

        mRootlayout = (RelativeLayout) view.findViewById(R.id.rl_rootlayout);
//        mRootlayout = (LinearLayout) view.findViewById(R.id.rl_rootlayout);
        mSurfaceView = (SurfaceView) view.findViewById(R.id.sv_preview);
        mCaptureView = (CaptureView) view.findViewById(R.id.cv_capture);
        mBtnCancel = (Button) view.findViewById(R.id.btn_cancel);
        mBtnApply = (Button) view.findViewById(R.id.btn_apply);
        mIVEdittext = (ImageView) view.findViewById(R.id.iv_edittextId);
        mLlEdittextStyle = (LinearLayout) view.findViewById(R.id.ll_edittext_style);
        mLlActions = (LinearLayout) view.findViewById(R.id.ll_actions);

        return builder.create();
    }


    private void initEdittextStyle() {
        mTextForET = getArguments().getString(QRResultActivity.EDITTEXT_TEXT);

        if (mEdittextId == R.id.rl_qr_code) {
            mIVEdittext.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_text_qr));
            LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LayoutInflater inflater1 = LayoutInflater.from(context);
            View           view1     = inflater1.inflate(R.layout.layout_update_edittext_style1, null);
            view1.setLayoutParams(lp1);
            mEtTobeUpdated = (EditText) view1.findViewById(R.id.et_update_data);
            if (null != mTextForET) {
                mEtTobeUpdated.setText(mTextForET);
            }
            mLlEdittextStyle.addView(view1);

        } else if (mEdittextId == R.id.rl_area) {
            mIVEdittext.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_text_area));
            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LayoutInflater inflater2 = LayoutInflater.from(context);
            View           view2     = inflater2.inflate(R.layout.layout_update_edittext_style1, null);
            view2.setLayoutParams(lp2);
            mEtTobeUpdated = (EditText) view2.findViewById(R.id.et_update_data);
            mLlEdittextStyle.addView(view2);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                if (listener != null) {
                    listener.onLeftClick();
                }
                break;
            case R.id.btn_apply:
                if (listener != null) {
                    listener.onRightClick();
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCameraManager.initCamera(holder);
        } catch (Exception e) {
            Toast.makeText(context, "相机权限被拒绝，请去设置里面打开", Toast.LENGTH_SHORT).show();
            dismiss();
            return;
        }
        if (!mCameraManager.isCameraAvailable()) {
            Toast.makeText(context, R.string.capture_camera_failed, Toast.LENGTH_SHORT).show();
            dismiss();
            return;
        }
        //        if (mCameraManager.isFlashlightAvailable()) {
        //            flashCb.setEnabled(true);
        //        }
        mCameraManager.startPreview();
        if (!isDecoding) {
            mCameraManager.requestPreviewFrameShot();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCameraManager.stopPreview();
        if (mDecodeThread != null) {
            mDecodeThread.cancel();
        }
        mCameraManager.release();
    }

    @Override
    public void onDecodeSuccess(Result result, LuminanceSource source, Bitmap bitmap) {
        Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(VIBRATE_DURATION);
        isDecoding = false;

        mDecodedData = result.getText();
        ZLog.d("onDecodeSuccess", "text:" + mDecodedData);
        if (mDecodedData == null) {
            return;
        } else {
            if (mDecodedData.length() == 0) {
                if (null != mEtTobeUpdated) {
                    mEtTobeUpdated.setText("");
                }
            } else {
                if (null != mEtTobeUpdated) {
                    mEtTobeUpdated.setText(mDecodedData);
                }
            }
        }
    }

    @Override
    public void onDecodeFailed(LuminanceSource source) {
        if (source instanceof RGBLuminanceSource) {
            Toast.makeText(getActivity(), R.string.capture_decode_failed, Toast.LENGTH_SHORT).show();
        }
        fial();
    }

    private void fial() {
        isDecoding = false;
        mCameraManager.requestPreviewFrameShot();
    }

    @Override
    public void foundPossibleResultPoint(ResultPoint resultPoint) {
        mCaptureView.addPossibleResultPoint(resultPoint);
    }

    @Override
    public void onPreviewFrame(byte[] data, Size dataSize) {
        if (mDecodeThread != null) {
            mDecodeThread.cancel();
        }
        if (previewFrameRect == null) {
            previewFrameRect = mCameraManager.getPreviewFrameRect(mCaptureView.getFrameRect());
        }
        PlanarYUVLuminanceSource luminanceSource = new PlanarYUVLuminanceSource(data, dataSize, previewFrameRect);
        mDecodeThread = new DecodeThread(luminanceSource, this);
        isDecoding = true;
        mDecodeThread.execute();
    }

    public interface OnClickListener {
        public void onLeftClick();

        public void onRightClick();

    }
}