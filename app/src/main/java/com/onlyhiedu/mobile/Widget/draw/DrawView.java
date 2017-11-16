package com.onlyhiedu.mobile.Widget.draw;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.onlyhiedu.mobile.Model.bean.board.LineBean;
import com.onlyhiedu.mobile.R;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.agore.openvcall.ui.ChatPresenter;

/**
 * Created by Ing. Oscar G. Medina Cruz on 06/11/2016.
 * <p>
 * This view was created for draw or paint anything you want.
 * <p>
 * <p>
 * This view can be configurated for change draw color, width size, can use tools like pen, line, circle, square.
 * </p>
 *
 * @author Ing. Oscar G. Medina Cruz
 */
public class DrawView extends FrameLayout implements View.OnTouchListener {

    // FINAL VARS
    final String TAG = "DrawView";

    // LISTENER
    private OnDrawViewListener onDrawViewListener;

    // VARS
    private int mDrawColor;
    private float mDrawWidth;
    private int mDrawAlpha;
    private boolean mAntiAlias;
    private boolean mDither;
    private Paint.Style mPaintStyle;
    private Paint.Cap mLineCap;
    private Typeface mFontFamily;
    private float mFontSize;
    private float mEraserSize = 12;
    private int mBackgroundColor;

    private DrawingMode mDrawingMode;
    private DrawingTool mDrawingTool;

    private List<DrawMove> mDrawMoveHistory;
    private int mDrawMoveHistoryIndex = -1;
    private Bitmap mContentBitmap;
    private Canvas mContentCanvas;
    private int mDrawMoveBackgroundIndex = -1;
    private File mBackgroundImage;
    private Bitmap mBackgroundImageBitmap;
    private int mLastTouchEvent = -1;
    private float mZoomFactor = 1.0f;
    private Rect mCanvasClipBounds;
    private RectF mAuxRect;

    private Long mySchoolTime;

    public void setMySchoolTime(Long mySchoolTime) {
        this.mySchoolTime = mySchoolTime;
    }

    /**
     * Default constructor
     *
     * @param context
     */
    public DrawView(Context context) {
        super(context);
        initVars();
    }

    /**
     * Default constructor
     *
     * @param context
     * @param attrs
     */
    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVars();
        initAttributes(context, attrs);
    }


    /**
     * Default constructor
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVars();
        initAttributes(context, attrs);
    }

    /**
     * Default constructor
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DrawView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initVars();
        initAttributes(context, attrs);
    }


    /**
     * Draw custom content in the view
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap init = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mContentBitmap = init.copy(Bitmap.Config.ARGB_8888, true);
        init.recycle();
        mContentCanvas = new Canvas(mContentBitmap);
        mContentBitmap.eraseColor(Color.TRANSPARENT);

        if (mDrawMoveBackgroundIndex != -1)
            drawBackgroundImage(mDrawMoveHistory.get(mDrawMoveBackgroundIndex), mContentCanvas);

        for (int i = 0; i < mDrawMoveHistoryIndex + 1; i++) {
            DrawMove drawMove = mDrawMoveHistory.get(i);
            switch (drawMove.getDrawingMode()) {
                case DRAW:
                    switch (drawMove.getDrawingTool()) {
                        case PEN:
                            if (drawMove.getDrawingPathList() != null &&
                                    drawMove.getDrawingPathList().size() > 0)
                                for (Path path : drawMove.getDrawingPathList()) {
                                    mContentCanvas.drawPath(path, drawMove.getPaint());
                                }
                            break;
                        case LINE:
                            mContentCanvas.drawLine(drawMove.getStartX(), drawMove.getStartY(),
                                    drawMove.getEndX(), drawMove.getEndY(), drawMove.getPaint());
                            break;
                        case RECTANGLE:
                            mContentCanvas.drawRect(drawMove.getStartX(), drawMove.getStartY(),
                                    drawMove.getEndX(), drawMove.getEndY(), drawMove.getPaint());
                            break;
                        case CIRCLE:
                            if (drawMove.getEndX() > drawMove.getStartX()) {
                                mContentCanvas.drawCircle(drawMove.getStartX(), drawMove.getStartY(),
                                        drawMove.getEndX() - drawMove.getStartX(), drawMove.getPaint());
                            } else {
                                mContentCanvas.drawCircle(drawMove.getStartX(), drawMove.getStartY(),
                                        drawMove.getStartX() - drawMove.getEndX(), drawMove.getPaint());
                            }
                        case OVAL:
                            RectF oval = new RectF(drawMove.getStartX(), drawMove.getStartY(), drawMove.getEndX(), drawMove.getEndY());
                            mContentCanvas.drawOval(oval, drawMove.getPaint());

//                            mAuxRect.set(drawMove.getEndX() - Math.abs(drawMove.getEndX() - drawMove.getStartX()),
//                                    drawMove.getEndY() - Math.abs(drawMove.getEndY() - drawMove.getStartY()),
//                                    drawMove.getEndX() + Math.abs(drawMove.getEndX() - drawMove.getStartX()),
//                                    drawMove.getEndY() + Math.abs(drawMove.getEndY() - drawMove.getStartY()));
//                            mContentCanvas.drawOval(mAuxRect, drawMove.getPaint());
                            break;
                    }
                    break;
                case TEXT:
                    if (drawMove.getText() != null && !drawMove.getText().equals("")) {
                        TextPaint textPaint = new TextPaint();
                        textPaint.setColor(drawMove.getPaint().getColor());
                        textPaint.setTextSize(drawMove.getPaint().getTextSize());
                        textPaint.setAntiAlias(true);
                        StaticLayout layout = new StaticLayout(drawMove.getText(), textPaint, 10000, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
                        mContentCanvas.save();
                        mContentCanvas.translate(drawMove.getStartX(), drawMove.getStartY());//从100，100开始画
                        layout.draw(mContentCanvas);
                        mContentCanvas.restore();//别忘了restore
//                        mContentCanvas.drawText(drawMove.getText(), drawMove.getStartX(), drawMove.getStartY(), drawMove.getPaint());
                    }
                    break;
                case ERASER:
                    if (drawMove.getDrawingPathList() != null &&
                            drawMove.getDrawingPathList().size() > 0) {
                        drawMove.getPaint().setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                        for (Path path : drawMove.getDrawingPathList()) {
                            Paint paint = drawMove.getPaint();
                            paint.setStrokeWidth(mEraserSize);
                            mContentCanvas.drawPath(path, paint);
                        }
                        drawMove.getPaint().setXfermode(null);
                    }
                    break;
                case ERASER_RECT:
                    if (drawMove.getDrawingPathList() != null && drawMove.getDrawingPathList().size() > 0) {
                        drawMove.getPaint().setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

                        Paint paint = drawMove.getPaint();
                        paint.setStyle(Paint.Style.FILL);
                        paint.setStrokeWidth(mEraserSize);
                        mContentCanvas.drawRect(drawMove.getStartX(), drawMove.getStartY(), drawMove.getEndX(), drawMove.getEndY(), paint);
                        drawMove.getPaint().setXfermode(null);
                    }
                    break;
            }
        }

        canvas.getClipBounds(mCanvasClipBounds);

        canvas.drawBitmap(mContentBitmap, 0, 0, null);

        super.onDraw(canvas);
    }

    private void drawBackgroundImage(DrawMove drawMove, Canvas canvas) {
        if (this.mBackgroundImage == null) {
            this.mBackgroundImage = drawMove.getBackgroundImage();
            this.mBackgroundImageBitmap = BitmapUtils.GetBitmapForDrawView(this, this.mBackgroundImage, 50, new Matrix());
        } else if (!this.mBackgroundImage.getAbsolutePath().equals(drawMove.getBackgroundImage().getAbsolutePath())) {
            this.mBackgroundImage = drawMove.getBackgroundImage();
            this.mBackgroundImageBitmap = BitmapUtils.GetBitmapForDrawView(this, this.mBackgroundImage, 50, new Matrix());
        }

        int centreX = (getWidth() - mBackgroundImageBitmap.getWidth()) / 2;
        int centreY = (getHeight() - mBackgroundImageBitmap.getHeight()) / 2;
        canvas.drawColor(mBackgroundColor);
        canvas.drawBitmap(this.mBackgroundImageBitmap, centreX, centreY, null);
    }


    StringBuffer sb = new StringBuffer();

    /**
     * Handle touch events in the view
     *
     * @param view
     * @param motionEvent
     * @return
     */
    private boolean isMoved;
    float touchX2 = 0;
    float touchY2 = 0;


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        float touchX = motionEvent.getX() / mZoomFactor + mCanvasClipBounds.left;
        float touchY = motionEvent.getY() / mZoomFactor + mCanvasClipBounds.top;

        if (motionEvent.getPointerCount() == 1) {

            switch (motionEvent.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    long l = System.currentTimeMillis() - mySchoolTime;
                    sb.append((int) (touchX / ChatPresenter.getScreenRate() + 0.5) + "," + (int) (touchY / ChatPresenter.getScreenRate() + 0.5) + "," + l + "|");
                    setDrawingMode(DrawingMode.values()[0]);
                    setDrawingTool(DrawingTool.values()[0]);
                    eventActionDown2(touchX, touchY);
                    touchX2 = motionEvent.getX() / mZoomFactor + mCanvasClipBounds.left;
                    touchY2 = motionEvent.getY() / mZoomFactor + mCanvasClipBounds.top;
                    break;
                case MotionEvent.ACTION_MOVE:
                    setDrawingMode(DrawingMode.values()[0]);
                    setDrawingTool(DrawingTool.values()[0]);
                    long ll = System.currentTimeMillis() - mySchoolTime;
                    sb.append((int) (touchX / ChatPresenter.getScreenRate() + 0.5) + "," + (int) (touchY / ChatPresenter.getScreenRate() + 0.5) + "," + ll + "|");

                    if (Math.abs(touchX2 - touchX) > 5 || Math.abs(touchY2 - touchY) > 5) {
                        isMoved = true;
                    }

                    eventActionMove(touchX, touchY);
                    break;
                case MotionEvent.ACTION_UP:
                    if (isMoved) {
                        setDrawingMode(DrawingMode.values()[0]);
                        setDrawingTool(DrawingTool.values()[0]);
                        long lll = System.currentTimeMillis() - mySchoolTime;
                        sb.append((int) (touchX / ChatPresenter.getScreenRate() + 0.5) + "," + (int) (touchY / ChatPresenter.getScreenRate() + 0.5) + "," + lll + "|");
                        eventActionUp(motionEvent.getX(), motionEvent.getY());

                        LineBean lineBean = new LineBean();
                        lineBean.points = sb.toString();
                        lineBean.drawMode = "01";
                        lineBean.lineWidth = (int) (mDrawWidth + 0.5);
                        lineBean.color = "(0,0,0)";
                        EventBus.getDefault().post(lineBean);
                        isMoved = false;
                    }
                    sb.delete(0, sb.length());
                    break;
                default:
                    return false;
            }
        } else {
            mLastTouchEvent = -1;
        }

        invalidate();
        return true;
    }

    public void eventActionDown(float touchX, float touchY) {
        mLastTouchEvent = MotionEvent.ACTION_DOWN;

        if (onDrawViewListener != null)
            onDrawViewListener.onStartDrawing();

        if (mDrawMoveHistoryIndex >= -1 &&
                mDrawMoveHistoryIndex < mDrawMoveHistory.size() - 1)
            mDrawMoveHistory = mDrawMoveHistory.subList(0, mDrawMoveHistoryIndex + 1);

        mDrawMoveHistory.add(DrawMove.newInstance()
                .setPaint(getNewPaintParams())
                .setStartX(touchX).setStartY(touchY)
                .setEndX(touchX).setEndY(touchY)
                .setDrawingMode(mDrawingMode).setDrawingTool(mDrawingTool));

        mDrawMoveHistoryIndex++;

        if (mDrawingTool == DrawingTool.PEN || mDrawingMode == DrawingMode.ERASER) {
            Path path = new Path();
            path.moveTo(touchX, touchY);
            path.lineTo(touchX, touchY);

            mDrawMoveHistory.get(mDrawMoveHistory.size() - 1).setDrawingPathList(new ArrayList<Path>());
            mDrawMoveHistory.get(mDrawMoveHistory.size() - 1).getDrawingPathList().add(path);
        }
    }

    public void eventActionDown2(float touchX, float touchY) {
        mLastTouchEvent = MotionEvent.ACTION_DOWN;

        if (onDrawViewListener != null)
            onDrawViewListener.onStartDrawing();

        if (mDrawMoveHistoryIndex >= -1 &&
                mDrawMoveHistoryIndex < mDrawMoveHistory.size() - 1)
            mDrawMoveHistory = mDrawMoveHistory.subList(0, mDrawMoveHistoryIndex + 1);

        mDrawMoveHistory.add(DrawMove.newInstance()
                .setPaint(getNewPaintParams2())
                .setStartX(touchX).setStartY(touchY)
                .setEndX(touchX).setEndY(touchY)
                .setDrawingMode(mDrawingMode).setDrawingTool(mDrawingTool));

        mDrawMoveHistoryIndex++;

        if (mDrawingTool == DrawingTool.PEN || mDrawingMode == DrawingMode.ERASER) {
            Path path = new Path();
            path.moveTo(touchX, touchY);
            path.lineTo(touchX, touchY);

            mDrawMoveHistory.get(mDrawMoveHistory.size() - 1).setDrawingPathList(new ArrayList<Path>());
            mDrawMoveHistory.get(mDrawMoveHistory.size() - 1).getDrawingPathList().add(path);
        }
    }

    public void eventActionMove(float touchX, float touchY) {
        if ((mLastTouchEvent == MotionEvent.ACTION_DOWN ||
                mLastTouchEvent == MotionEvent.ACTION_MOVE)) {
            mLastTouchEvent = MotionEvent.ACTION_MOVE;

            if (mDrawMoveHistory.size() > 0) {
                mDrawMoveHistory.get(mDrawMoveHistory.size() - 1).setEndX(touchX).setEndY(touchY);

                if (mDrawingTool == DrawingTool.PEN || mDrawingMode == DrawingMode.ERASER) {
                    mDrawMoveHistory.get(mDrawMoveHistory.size() - 1).getDrawingPathList()
                            .get(mDrawMoveHistory.get(mDrawMoveHistory.size() - 1).getDrawingPathList().size() - 1)
                            .lineTo(touchX, touchY);
                }
            }
        }
    }

    public void eventActionUp(float touchX, float touchY) {
        if (mLastTouchEvent == MotionEvent.ACTION_DOWN) {
            if (mDrawMoveHistory.size() > 0) {
                mDrawMoveHistory.remove(mDrawMoveHistory.size() - 1);
                mDrawMoveHistoryIndex--;
            }
        } else if (mLastTouchEvent == MotionEvent.ACTION_MOVE) {
            mLastTouchEvent = -1;
            if (mDrawMoveHistory.size() > 0) {
                mDrawMoveHistory.get(mDrawMoveHistory.size() - 1).setEndX(touchX).setEndY(touchY);

                if (mDrawingTool == DrawingTool.PEN || mDrawingMode == DrawingMode.ERASER) {
                    mDrawMoveHistory.get(mDrawMoveHistory.size() - 1).getDrawingPathList()
                            .get(mDrawMoveHistory.get(mDrawMoveHistory.size() - 1).getDrawingPathList().size() - 1)
                            .lineTo(touchX, touchY);
                }
            }
        }

        if (onDrawViewListener != null && mDrawingMode == DrawingMode.TEXT)
            onDrawViewListener.onRequestText();

        if (onDrawViewListener != null)
            onDrawViewListener.onEndDrawing();

        invalidate();
    }


    // PRIVATE METHODS

    /**
     * Initialize general vars for the view
     */
    private void initVars() {
        mDrawMoveHistory = new ArrayList<>();
        setOnTouchListener(this);
        mCanvasClipBounds = new Rect();

    }

    public void setCanvas(int x, int y) {
        mAuxRect = new RectF();
        Bitmap init = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
        mContentBitmap = init.copy(Bitmap.Config.ARGB_8888, true);
        init.recycle();
        mContentCanvas = new Canvas(mContentBitmap);
    }


    /**
     * Initialize view attributes
     *
     * @param context
     * @param attrs
     */
    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.DrawView, 0, 0);
        try {
            mDrawColor = typedArray.getColor(R.styleable.DrawView_dv_draw_color, Color.BLACK);
            mDrawWidth = typedArray.getInteger(R.styleable.DrawView_dv_draw_width, 3);
            mDrawAlpha = typedArray.getInteger(R.styleable.DrawView_dv_draw_alpha, 255);
            mAntiAlias = typedArray.getBoolean(R.styleable.DrawView_dv_draw_anti_alias, true);
            mDither = typedArray.getBoolean(R.styleable.DrawView_dv_draw_dither, true);
            int paintStyle = typedArray.getInteger(R.styleable.DrawView_dv_draw_style, 2);
            if (paintStyle == 0)
                mPaintStyle = Paint.Style.FILL;
            else if (paintStyle == 1)
                mPaintStyle = Paint.Style.FILL_AND_STROKE;
            else if (paintStyle == 2)
                mPaintStyle = Paint.Style.STROKE;
            int cap = typedArray.getInteger(R.styleable.DrawView_dv_draw_corners, 2);
            if (cap == 0)
                mLineCap = Paint.Cap.BUTT;
            else if (cap == 1)
                mLineCap = Paint.Cap.ROUND;
            else if (cap == 2)
                mLineCap = Paint.Cap.SQUARE;
            int typeface = typedArray.getInteger(R.styleable.DrawView_dv_draw_font_family, 0);
            if (typeface == 0)
                mFontFamily = Typeface.DEFAULT;
            else if (typeface == 1)
                mFontFamily = Typeface.MONOSPACE;
            else if (typeface == 2)
                mFontFamily = Typeface.SANS_SERIF;
            else if (typeface == 3)
                mFontFamily = Typeface.SERIF;
            mFontSize = typedArray.getInteger(R.styleable.DrawView_dv_draw_font_size, 12);
            if (getBackground() != null)
                try {
                    mBackgroundColor = ((ColorDrawable) getBackground()).getColor();
                } catch (Exception e) {
                    e.printStackTrace();
                    setBackgroundColor(Color.TRANSPARENT);
                    mBackgroundColor = ((ColorDrawable) getBackground()).getColor();
                    //TODO
//                    setBackgroundResource(R.drawable.launch_bg);
                }
            else {
                setBackgroundColor(Color.TRANSPARENT);
                mBackgroundColor = ((ColorDrawable) getBackground()).getColor();
//                setBackgroundResource(R.drawable.launch_bg);
            }


            mDrawingTool = DrawingTool.values()[typedArray.getInteger(R.styleable.DrawView_dv_draw_tool, 0)];
            mDrawingMode = DrawingMode.values()[typedArray.getInteger(R.styleable.DrawView_dv_draw_mode, 0)];
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * New paint parameters
     *
     * @return new paint parameters for initialize drawing
     */
    private Paint getNewPaintParams() {


        Paint paint = new Paint();

        if (mDrawingMode == DrawingMode.ERASER) {
            if (mDrawingTool != DrawingTool.PEN) {
                Log.i(TAG, "For use eraser drawing mode is necessary to use pen tool");
                mDrawingTool = DrawingTool.PEN;
            }
            paint.setColor(mBackgroundColor);
        } else {
            paint.setColor(mDrawColor);
        }
        paint.setStyle(mPaintStyle);
        paint.setDither(mDither);
//        if (mDrawingMode == DrawingMode.DRAW) {
//            if (mDrawingTool == DrawingTool.OVAL || mDrawingTool == DrawingTool.RECTANGLE || mDrawingTool == DrawingTool.LINE) {
//                Log.d("DrawView", "几何图形线框："+ 3 * ChatPresenter.getScreenRate() + "");
//                paint.setStrokeWidth(3 * ChatPresenter.getScreenRate());
//            } else {
//
//            }77
//        }
        paint.setStrokeWidth(mDrawWidth);
        paint.setAlpha(mDrawAlpha);
        paint.setAntiAlias(mAntiAlias);
        paint.setStrokeCap(mLineCap);
        paint.setTypeface(mFontFamily);
        paint.setTextSize(mFontSize);

        return paint;
    }

    private Paint getNewPaintParams2() {
        Paint paint = getNewPaintParams();
        paint.setColor(Color.BLACK);
        return paint;
    }

    // PUBLIC METHODS

    /**
     * Current paint parameters
     *
     * @return current paint parameters
     */
    public Paint getCurrentPaintParams() {
        Paint currentPaint;
        if (mDrawMoveHistory.size() > 0 && mDrawMoveHistoryIndex >= 0) {
            currentPaint = new Paint();
            currentPaint.setColor(
                    mDrawMoveHistory.get(mDrawMoveHistoryIndex).getPaint().getColor());
            currentPaint.setStyle(
                    mDrawMoveHistory.get(mDrawMoveHistoryIndex).getPaint().getStyle());
            currentPaint.setDither(
                    mDrawMoveHistory.get(mDrawMoveHistoryIndex).getPaint().isDither());
            currentPaint.setStrokeWidth(
                    mDrawMoveHistory.get(mDrawMoveHistoryIndex).getPaint().getStrokeWidth());
            currentPaint.setAlpha(
                    mDrawMoveHistory.get(mDrawMoveHistoryIndex).getPaint().getAlpha());
            currentPaint.setAntiAlias(
                    mDrawMoveHistory.get(mDrawMoveHistoryIndex).getPaint().isAntiAlias());
            currentPaint.setStrokeCap(
                    mDrawMoveHistory.get(mDrawMoveHistoryIndex).getPaint().getStrokeCap());
            currentPaint.setTypeface(
                    mDrawMoveHistory.get(mDrawMoveHistoryIndex).getPaint().getTypeface());
            currentPaint.setTextSize(mFontSize);
        } else {
            currentPaint = new Paint();
            currentPaint.setColor(mDrawColor);
            currentPaint.setStyle(mPaintStyle);
            currentPaint.setDither(mDither);
            currentPaint.setStrokeWidth(mDrawWidth);
            currentPaint.setAlpha(mDrawAlpha);
            currentPaint.setAntiAlias(mAntiAlias);
            currentPaint.setStrokeCap(mLineCap);
            currentPaint.setTypeface(mFontFamily);
            currentPaint.setTextSize(24f);
        }
        return currentPaint;
    }

    /**
     * Restart all the parameters and drawing history
     *
     * @return if the draw view can be restarted
     */
    public boolean restartDrawing() {
        if (mDrawMoveHistory != null) {
            mDrawMoveHistory.clear();
            mDrawMoveHistoryIndex = -1;
            invalidate();

            if (onDrawViewListener != null)
                onDrawViewListener.onClearDrawing();

            return true;
        }
        invalidate();
        return false;
    }

    /**
     * Undo last drawing action
     *
     * @return if the view can do the undo action
     */
    public boolean undo() {
        if (mDrawMoveHistoryIndex > -1 &&
                mDrawMoveHistory.size() > 0) {
            mDrawMoveHistoryIndex--;
            invalidate();
            return true;
        }
        invalidate();
        return false;
    }

    /**
     * Check if the draw view can do undo action
     *
     * @return if the view can do the undo action
     */
    public boolean canUndo() {
        return mDrawMoveHistoryIndex > -1 &&
                mDrawMoveHistory.size() > 0;
    }

    /**
     * Redo preview action
     *
     * @return if the view can do the redo action
     */
    public boolean redo() {
        if (mDrawMoveHistoryIndex <= mDrawMoveHistory.size() - 1) {
            mDrawMoveHistoryIndex++;
            invalidate();
            return true;
        }
        invalidate();
        return false;
    }

    /**
     * Check if the view can do the redo action
     *
     * @return if the view can do the redo action
     */
    public boolean canRedo() {
        return mDrawMoveHistoryIndex < mDrawMoveHistory.size() - 1;
    }

    /**
     * Create capture of the drawing view as bitmap or as byte array
     *
     * @param drawingCapture
     * @return Object in form of bitmap or byte array
     */
    public Object createCapture(DrawingCapture drawingCapture) {
        setDrawingCacheEnabled(false);
        setDrawingCacheEnabled(true);

        switch (drawingCapture) {
            case BITMAP:
                return getDrawingCache(true);
            case BYTES:
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                getDrawingCache(true).compress(Bitmap.CompressFormat.PNG, 100, stream);
                return stream.toByteArray();
        }
        return null;
    }

    /**
     * Refresh the text of the last movement item
     *
     * @param newText
     */
    public void refreshLastText(String newText) {
        if (mDrawMoveHistory.get(mDrawMoveHistory.size() - 1)
                .getDrawingMode() == DrawingMode.TEXT) {
            mDrawMoveHistory.get(mDrawMoveHistory.size() - 1).setText(newText);
            invalidate();
        } else
            Log.e(TAG, "The last item that you want to refresh text isn't TEXT element.");
    }

    /**
     * Delete las history element, this can help for cancel the text request.
     */
    public void cancelTextRequest() {
        if (mDrawMoveHistory != null && mDrawMoveHistory.size() > 0) {
            mDrawMoveHistory.remove(mDrawMoveHistory.size() - 1);
            mDrawMoveHistoryIndex--;
        }
    }


    // GETTERS
    public int getDrawAlpha() {
        return mDrawAlpha;
    }

    public int getDrawColor() {
        return mDrawColor;
    }

    public float getDrawWidth() {
        return mDrawWidth;
    }

    public DrawingMode getDrawingMode() {
        return mDrawingMode;
    }

    public DrawingTool getDrawingTool() {
        return mDrawingTool;
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public Paint.Style getPaintStyle() {
        return mPaintStyle;
    }

    public Paint.Cap getLineCap() {
        return mLineCap;
    }

    public Typeface getFontFamily() {
        return mFontFamily;
    }

    public float getFontSize() {
        return mFontSize;
    }

    public float getEraserSize() {
        return mEraserSize;
    }

    public void setEraserSize(float eraserSize) {
        mEraserSize = eraserSize;
    }

    public boolean isAntiAlias() {
        return mAntiAlias;
    }

    public boolean isDither() {
        return mDither;
    }

    // SETTERS

    /**
     * Set the new draw parametters easily
     *
     * @param paint
     * @return this instance of the view
     */
    public DrawView refreshAttributes(Paint paint) {
        mDrawColor = paint.getColor();
        mPaintStyle = paint.getStyle();
        mDither = paint.isDither();
        mDrawWidth = (int) paint.getStrokeWidth();
        mDrawAlpha = paint.getAlpha();
        mAntiAlias = paint.isAntiAlias();
        mLineCap = paint.getStrokeCap();
        mFontFamily = paint.getTypeface();
        mFontSize = paint.getTextSize();
        return this;
    }

    /**
     * Set the current alpha value for the drawing
     *
     * @param drawAlpha
     * @return this instance of the view
     */
    public DrawView setDrawAlpha(int drawAlpha) {
        this.mDrawAlpha = drawAlpha;
        return this;
    }

    /**
     * Set the current draw color for drawing
     *
     * @param drawColor
     * @return this instance of the view
     */
    public DrawView setDrawColor(int drawColor) {
        this.mDrawColor = drawColor;
        return this;
    }

    /**
     * Set the current draw width
     *
     * @param drawWidth
     * @return this instance of the view
     */
    public DrawView setDrawWidth(float drawWidth) {
        this.mDrawWidth = drawWidth;
        return this;
    }

    /**
     * Set the current draw mode like draw, text or eraser
     *
     * @param drawingMode
     * @return this instance of the view
     */
    public DrawView setDrawingMode(DrawingMode drawingMode) {
        this.mDrawingMode = drawingMode;
        return this;
    }

    /**
     * Set the current draw tool like pen, line, circle, rectangle, circle
     *
     * @param drawingTool
     * @return this instance of the view
     */
    public DrawView setDrawingTool(DrawingTool drawingTool) {
        this.mDrawingTool = drawingTool;
        return this;
    }

    /**
     * Set the current background color of draw view
     *
     * @param backgroundColor
     * @return this instance of the view
     */
    public DrawView setBackgroundDrawColor(int backgroundColor) {
        this.mBackgroundColor = backgroundColor;
        return this;
    }

    /**
     * Set the current paint style like fill, fill_stroke or stroke
     *
     * @param paintStyle
     * @return this instance of the view
     */
    public DrawView setPaintStyle(Paint.Style paintStyle) {
        this.mPaintStyle = paintStyle;
        return this;
    }

    /**
     * Set the current line cap like round, square or butt
     *
     * @param lineCap
     * @return this instance of the view
     */
    public DrawView setLineCap(Paint.Cap lineCap) {
        this.mLineCap = lineCap;
        return this;
    }

    /**
     * Set the current typeface for the view when we like to draw text
     *
     * @param fontFamily
     * @return this instance of the view
     */
    public DrawView setFontFamily(Typeface fontFamily) {
        this.mFontFamily = fontFamily;
        return this;
    }

    /**
     * Set the current font size for the view when we like to draw text
     *
     * @param fontSize
     * @return this instance of the view
     */
    public DrawView setFontSize(float fontSize) {
        this.mFontSize = fontSize;
        return this;
    }


    /**
     * Set the current anti alias value for the view
     *
     * @param antiAlias
     * @return this instance of the view
     */
    public DrawView setAntiAlias(boolean antiAlias) {
        this.mAntiAlias = antiAlias;
        return this;
    }

    /**
     * Set the current dither value for the view
     *
     * @param dither
     * @return this instance of the view
     */
    public DrawView setDither(boolean dither) {
        this.mDither = dither;
        return this;
    }

    // LISTENER

    /**
     * Setting new OnDrawViewListener for this view
     *
     * @param onDrawViewListener
     */
    public void setOnDrawViewListener(OnDrawViewListener onDrawViewListener) {
        this.onDrawViewListener = onDrawViewListener;
    }

    /**
     * Listener for registering drawing actions of the view
     */
    public interface OnDrawViewListener {
        void onStartDrawing();

        void onEndDrawing();

        void onClearDrawing();

        void onRequestText();
    }

}
