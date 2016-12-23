package com.zzm.zzmvp.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.zzm.zzmlibrary.utils.LogUtils;
import com.zzm.zzmvp.R;

/**
 * Created by ZhongZiMing on 2016/12/21.
 */

public class FuelFillingView extends View {


    private final float startAngle = 180; //圆弧起始角度
    private final float sweepAngle = 180; //旋转的角度

    private int mViewWidth,mViewHeight; //控件的宽和高
    private int mRadius; //默认圆弧半径
    private int mStrokeWidth; //默认圆弧宽度
    private int mRadiusChoose;//蓝色扇形半径
    private int mRadiusSmall;//白色扇形半径
    private int selectedColor; //选中状态的颜色
    private int unSelectedColor; //未选中状态的颜色
    private int backgroundColor; //背景颜色
    private int mSection = 10; // 长刻度等分份数
    private int mPortion = 5;  // 一个长刻度等分份数
    private int mScaleLongLenth; //长刻度相对圆心的长度
    private int mScaleShortLenth; //短刻度相对圆心的长度
    private String[] mTexts ; //长刻度上的文字数组
    private String[] mTextsValue; //跟刻度数字对应的值

    private Context mContext;
    private Paint mPaintArc; //默认圆弧
    private Paint mPaintArcChoose; //选中的蓝色扇形
    private Paint mPaintArcSmall; //内部白色扇形
    private Paint mPaintText; //文字
    private Path mPath;

    private RectF mRectFArc;  // 默认圆弧
    private RectF mRectFArcChoose;  // 蓝色扇形
    private RectF mRectFArcSmall;  //白色扇形
    private Rect mRectText; //月份
    private RectF mRectFInnerArc; //文字

    private Bitmap mBitmapCar; //小汽车
    private Bitmap mBitmapPoint; //指针
    private Matrix matrix ;

    private float eventX ; //手指按下的X坐标
    private float eventY ; //手指按下的Y坐标
    private int mEvent = MotionEvent.ACTION_UP;

    /**
     * 旋转的角度
     */
    private float mSweepAngle = 90;

    public FuelFillingView(Context context) {
        this(context,null);
    }

    public FuelFillingView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FuelFillingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext =context;
//        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FuelFillView, defStyleAttr, 0);
        //添加attr属性
//        selectedColor = a.getColor(R.styleable.DashboardView_arcColor, Color.parseColor("#52adff"));

//        a.recycle();
        init();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
        mRadius = mViewWidth / 2 - dpToPx(10);
        mRadiusChoose = mViewWidth / 2;
        mRadiusSmall = mRadius / 2 - dpToPx(3);
        mRectFArc.set(-mRadius,-mRadius,mRadius,mRadius);
        mRectFArcChoose.set(-mRadiusChoose,-mRadiusChoose,mRadiusChoose,mRadiusChoose);
        mRectFArcSmall.set(-mRadiusSmall,-mRadiusSmall,mRadiusSmall,mRadiusSmall);
        mPaintText.setTextSize(spToPx(12));
        mPaintText.getTextBounds("0", 0, "0".length(), mRectText);
        mRectFInnerArc.set(-mRadius+mScaleLongLenth+mRectText.height()+dpToPx(6),
                -mRadius+mScaleLongLenth+mRectText.height()+dpToPx(6),
                mRadius-mScaleLongLenth-mRectText.height()-dpToPx(6),
                mRadius-mScaleLongLenth-mRectText.height()-dpToPx(6));
        eventX = mRadiusChoose;
        eventY = 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        setMeasuredDimension(width, width/2);
    }

    //根据xml的设定获取宽度
    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //wrap_content
        if (specMode == MeasureSpec.AT_MOST){
        }
        //match_parent或者精确值
        else if (specMode == MeasureSpec.EXACTLY){
        }
        return specSize;
    }


    private void init(){

        matrix = new Matrix();
        mStrokeWidth = dpToPx(1);
        mScaleLongLenth = dpToPx(10);
        mScaleShortLenth = mScaleLongLenth / 2;
        selectedColor = Color.parseColor("#52adff");
        unSelectedColor = Color.parseColor("#a1a5aa");
        backgroundColor = Color.parseColor("#ffffff");
        mTexts = new String[]{"","及时充","","3个月","","6个月","","12个月","","24个月",""};
        mTextsValue = new String[]{"","0折","","9.8折","","9.7折","","9.6折","","9.5折",""};
        mBitmapCar = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_car);
        Bitmap mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.pointer_icon);
        matrix.postRotate(-90);
        mBitmapPoint = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);

        mPaintArc = new Paint();
        mPaintArc.setAntiAlias(true);
        mPaintArc.setColor(selectedColor);
        mPaintArc.setStrokeWidth(mStrokeWidth);
        mPaintArc.setStyle(Paint.Style.STROKE);
        mPaintArc.setStrokeCap(Paint.Cap.ROUND);

        mPaintArcChoose = new Paint();
        mPaintArcChoose.setAntiAlias(true);
        mPaintArcChoose.setColor(Color.parseColor("#3352adff"));
        mPaintArcChoose.setStrokeCap(Paint.Cap.ROUND);

        mPaintArcSmall = new Paint();
        mPaintArcSmall.setAntiAlias(true);
        mPaintArcSmall.setColor(backgroundColor);
        mPaintArcSmall.setStrokeCap(Paint.Cap.ROUND);

        mPaintText = new Paint();
        mPaintText.setAntiAlias(true);
        mPaintText.setStyle(Paint.Style.FILL);

        mRectFArc = new RectF();
        mRectFArcChoose = new RectF();
        mRectFArcSmall = new RectF();
        mRectText = new Rect();
        mRectFInnerArc = new RectF();
        mPath = new Path();

        setBackgroundColor(backgroundColor);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mEvent = event.getAction();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //此处使用 getRawX，而不是 getX
                eventX = event.getRawX();
                eventY = event.getRawY();
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                //让指针指向最近的数据，不需要则可注释
                float surplus = mSweepAngle % (sweepAngle / mSection);
                int num = (int)((mSweepAngle - surplus) / (sweepAngle / mSection));
                if(TextUtils.isEmpty(mTexts[num])&&!TextUtils.isEmpty(mTexts[num+1])){
                    mSweepAngle = mSweepAngle + (sweepAngle / mSection) - surplus;
                }else if(!TextUtils.isEmpty(mTexts[num])){
                    mSweepAngle = mSweepAngle - surplus;
                }
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float[] pts = {eventX,eventY};

        canvas.translate(mViewWidth/2,mViewHeight);//将画布中心移动到控件底部中间

        if(mEvent==MotionEvent.ACTION_DOWN||mEvent==MotionEvent.ACTION_MOVE){
            changeCanvasXY(canvas,pts);//触摸点的坐标转换
        }

        drawArc(canvas);//画选中的圆弧和未被选中的圆弧

        drawLongLenth(canvas);//画选中和未选中的长刻度

        drawShortLenth(canvas);//画选中和未选中的短刻度

        drawText(canvas);//画刻度上的文字

        drawBlueArc(canvas);//画选中的蓝色扇形

        drawPoint(canvas); //画指针

        drawWhiteArc(canvas);//画内部白色扇形

        drawCar(canvas);//画小汽车

        drawValue(canvas); //画折扣价

    }


    private void changeCanvasXY(Canvas canvas,float[] pts){
        // 获得当前矩阵的逆矩阵
        Matrix invertMatrix = new Matrix();
        canvas.getMatrix().invert(invertMatrix);
        // 使用 mapPoints 将触摸位置转换为画布坐标
        invertMatrix.mapPoints(pts);
        float x = Math.abs(pts[0]);
        float y = Math.abs(pts[1]);
        double z = Math.sqrt(x*x+y*y);
        float round = (float)(Math.asin(y/z)/Math.PI*180);
        LogUtils.e("触摸的点：X==="+pts[0]+"Y==="+pts[1]+"===当前角度："+round);
        if(pts[0]<=0){
            mSweepAngle =  round;
        }else{
            mSweepAngle = sweepAngle - round;
        }
    }

    private void drawArc(Canvas canvas) {
        mPaintArc.setColor(selectedColor);
        canvas.drawArc(mRectFArc,startAngle,mSweepAngle,false,mPaintArc);
        mPaintArc.setColor(unSelectedColor);
        canvas.drawArc(mRectFArc,startAngle+mSweepAngle,sweepAngle-mSweepAngle,false,mPaintArc);
        canvas.save();
    }

    private void drawLongLenth(Canvas canvas) {
        float x0 = (float) (- mRadius + mStrokeWidth);
        float x1 = (float) (- mRadius + mStrokeWidth + mScaleLongLenth);
        mPaintArc.setStrokeWidth(mStrokeWidth*2);
        mPaintArc.setColor(selectedColor);
        canvas.drawLine(x0, 0, x1, 0, mPaintArc);
        float angle = sweepAngle * 1f / mSection;
        mPaintArc.setStrokeWidth(mStrokeWidth);
        float selectSection = mSweepAngle / (sweepAngle / mSection);
        for (int i = 1; i <= selectSection; i++) {
            canvas.rotate(angle, 0, 0);
            canvas.drawLine(x0, 0, x1, 0, mPaintArc);
        }
        mPaintArc.setColor(unSelectedColor);
        for (int i = 0; i < mSection - selectSection; i++) {
            if(i == mSection - (int)selectSection - 1){
                mPaintArc.setStrokeWidth(mStrokeWidth*2);
            }
            canvas.rotate(angle, 0, 0);
            canvas.drawLine(x0, 0, x1, 0, mPaintArc);
        }
        canvas.restore();
    }

    private void drawShortLenth(Canvas canvas) {
        canvas.save();
        mPaintArc.setStrokeWidth(mStrokeWidth/2);
        mPaintArc.setColor(selectedColor);
        float x0 = (float) (- mRadius + mStrokeWidth);
        float x2 = (float) (- mRadius + mStrokeWidth + mScaleShortLenth);
        canvas.drawLine(x0, 0, x2, 0, mPaintArc);
        float angle = sweepAngle * 1f / (mSection*mPortion);
        float mSelectSection = mSweepAngle / (sweepAngle / (mSection*mPortion));
        for (int i = 1; i <= mSelectSection ; i++) {
            canvas.rotate(angle, 0, 0);
            canvas.drawLine(x0, 0, x2, 0, mPaintArc);
        }
        mPaintArc.setColor(unSelectedColor);
        for (int i = 1; i <= (mSection * mPortion - mSelectSection); i++) {
            canvas.rotate(angle, 0, 0);
            canvas.drawLine(x0, 0, x2, 0, mPaintArc);
        }
        canvas.restore();
    }

    private void drawText(Canvas canvas) {
        mPaintText.setTextSize(spToPx(12));
        mPaintText.setTextAlign(Paint.Align.LEFT);
        mPaintText.setTypeface(Typeface.DEFAULT);
        float mSectionSelect = mSweepAngle / (sweepAngle / mSection);
        for (int i = 0; i < mTexts.length; i++) {
            if(i==(int)mSectionSelect){
                mPaintText.setColor(selectedColor);
            }else{
                mPaintText.setColor(unSelectedColor);
            }
            mPaintText.getTextBounds(mTexts[i], 0, mTexts[i].length(), mRectText);
            // 粗略把文字的宽度视为圆心角2*θ对应的弧长，利用弧长公式得到θ，下面用于修正角度
            float θ = (float) (180 * mRectText.width() / 2 /
                    (Math.PI * (mRadius - mScaleShortLenth - mRectText.height())));
            mPath.reset();
            mPath.addArc(
                    mRectFInnerArc,
                    startAngle + i * (sweepAngle / mSection) - θ, // 正起始角度减去θ使文字居中对准长刻度
                    sweepAngle
            );
            canvas.drawTextOnPath(mTexts[i], mPath, 0, 0, mPaintText);
        }
    }

    private void drawBlueArc(Canvas canvas) {
        canvas.drawArc(mRectFArcChoose,startAngle,mSweepAngle,true,mPaintArcChoose);
        canvas.save();
    }

    private void drawPoint(Canvas canvas) {
        canvas.rotate(mSweepAngle, 0, 0);
        canvas.drawBitmap(mBitmapPoint,-mRadiusChoose,-mBitmapPoint.getHeight()/2,null);
        canvas.restore();
    }

    private void drawWhiteArc(Canvas canvas) {
        canvas.drawArc(mRectFArcSmall,startAngle,sweepAngle,true,mPaintArcSmall);
    }

    private void drawCar(Canvas canvas) {
        canvas.drawBitmap(mBitmapCar,-mBitmapCar.getWidth()/2,-mRadiusSmall/2-mBitmapCar.getHeight(),null);
    }

    private void drawValue(Canvas canvas) {
        mPaintText.setTextSize(dpToPx(24));
        mPaintText.setTextAlign(Paint.Align.CENTER);
        mPaintText.setColor(Color.parseColor("#252c3d"));
        mPaintText.setTypeface(Typeface.DEFAULT_BOLD);
        int num = (int)(mSweepAngle / (sweepAngle / mSection));
        if(!TextUtils.isEmpty(mTextsValue[num])){
            canvas.drawText(mTextsValue[num],0, -dpToPx(12),mPaintText);
        }else{
            canvas.drawText(mTextsValue[num+1],0, -dpToPx(12),mPaintText);
        }

    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private int spToPx(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    /**
     * 设置长刻度上的文字数组和对应显示的值的数组，跟据数组个数来等分刻度
     * @param texts
     */
    public void setTextsArray(String[] texts,String[] textsValue){
        if(texts==null||texts.length==0){
            return;
        }
        this.mTexts = texts;
        this.mTextsValue = textsValue;
        this.mSection = texts.length - 1;
        invalidate();
    }

}
