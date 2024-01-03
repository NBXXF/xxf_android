package com.xxf.media.audio.uicomponent;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.xxf.media.audio.AudioRecorder;

import java.io.File;

public class RecordAudioView extends androidx.appcompat.widget.AppCompatButton {

    /**
     * 滑动取消方向
     */
    public enum CancelDirection {
        TO_TOP,
        TO_RIGHT;
    }

    private static final String TAG = "RecordAudioView";

    private Context context;
    private IRecordAudioListener recordAudioListener;
    private boolean isCanceled;
    private float downPointX;
    private float downPointY;
    private static final float DEFAULT_SLIDE_HEIGHT_CANCEL = 150;
    private boolean isRecording;
    private CancelDirection cancelDirection = CancelDirection.TO_TOP;

    public void setCancelDirection(CancelDirection cancelDirection) {
        this.cancelDirection = cancelDirection;
    }

    public RecordAudioView(Context context) {
        super(context);
        initView(context);
    }

    public RecordAudioView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RecordAudioView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (isEnabled()&&recordAudioListener != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    setSelected(true);
                    downPointX = event.getX();
                    downPointY = event.getY();
                    recordAudioListener.onFingerPress();
                    startRecordAudio();
                    break;
                case MotionEvent.ACTION_UP:
                    setSelected(false);
                    onFingerUp();
                    break;
                case MotionEvent.ACTION_MOVE:
                    onFingerMove(event);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    isCanceled = true;
                    onFingerUp();
                    break;
                default:

                    break;
            }
        }
        return true;
    }

    /**
     * 手指抬起,可能是取消录制也有可能是录制成功
     */
    public void onFingerUp() {
        if (isRecording) {
            if (isCanceled) {
                isRecording = false;
                AudioRecorder.getInstance().stopRecord();
                recordAudioListener.onRecordCancel();
            } else {
                stopRecordAudio();
            }
        }
    }

    private void onFingerMove(MotionEvent event) {
        float currentX = event.getX();
        float currentY = event.getY();
        isCanceled = checkCancel(downPointX, downPointY, currentX, currentY);
        if (isCanceled) {
            recordAudioListener.onSlideOutSide();
        } else {
            recordAudioListener.onFingerPress();
        }
    }

    /**
     * 默认向上滑动取消
     *
     * @param downPointX
     * @param downPointY
     * @param currentX
     * @param currentY
     * @return
     */
    protected boolean checkCancel(float downPointX, float downPointY,
                                  float currentX, float currentY) {
        switch (cancelDirection) {
            case TO_TOP: {
                return downPointY - currentY >= DEFAULT_SLIDE_HEIGHT_CANCEL;
            }
            case TO_RIGHT: {
                return currentX - downPointX >= DEFAULT_SLIDE_HEIGHT_CANCEL;
            }
        }
        return false;
    }

    /**
     * 检查是否ready录制,如果已经ready则开始录制
     */
    private void startRecordAudio() throws RuntimeException {
        boolean isPrepare = recordAudioListener.onRecordPrepare();
        if (isPrepare) {
            String audioFileName = recordAudioListener.onRecordStart();
            //准备就绪开始录制
            try {
                AudioRecorder.getInstance().prepareRecord(MediaRecorder.AudioSource.MIC,
                        MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.AudioEncoder.AAC,
                        192000, 192000, new File(audioFileName));
                AudioRecorder.getInstance().startRecord();
                isRecording = true;
            } catch (Exception e) {
                this.recordAudioListener.onRecordCancel();
            }
        }
    }

    /**
     * 停止录音
     */
    public void stopRecordAudio() throws RuntimeException {
        if (isRecording) {
            try {
                isRecording = false;
                AudioRecorder.getInstance().stopRecord();
                this.recordAudioListener.onRecordStop();
            } catch (Exception e) {
                this.recordAudioListener.onRecordCancel();
            }
        }
    }

    /**
     * 需要设置IRecordAudioStatus,来监听开始录音结束录音等操作,并对权限进行处理
     *
     * @param recordAudioListener
     */
    public void setRecordAudioListener(IRecordAudioListener recordAudioListener) {
        this.recordAudioListener = recordAudioListener;
    }

    public void invokeStop() {
        onFingerUp();
    }

    public interface IRecordAudioListener {
        boolean onRecordPrepare();

        String onRecordStart();

        boolean onRecordStop();

        boolean onRecordCancel();

        /**
         * 移动到外边界
         */
        void onSlideOutSide();

        void onFingerPress();
    }
}
