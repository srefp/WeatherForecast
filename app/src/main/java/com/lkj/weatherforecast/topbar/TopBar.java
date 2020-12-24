//package com.lkj.weatherforecast.topbar;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.drawable.Drawable;
//import android.util.AttributeSet;
//import android.util.TypedValue;
//import android.view.Gravity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.lkj.weatherforecast.R;
//
//
//public class TopBar extends RelativeLayout {
//    //包含topbar上的元素：左按钮，右按钮，标题
//    private Button mLeftButton,mRightButton;
//    private TextView mTitleView;
//    //布局属性，用来控制组建元素在viewgroup中的位置，
//    private LayoutParams mLeftParams,mTitleParams,mRightParams;
//    //左按钮的属性值
//    private int mLeftTextColor;
//    private Drawable mleftBackground;
//    private String mLeftText;
//    //右按钮属性
//    private int mRightTextColor;
//    private Drawable mRightBackground;
//    private String mRightText;
//    //标题的属性值
//    private float mTitleTextSize;
//    private int mTitleTextColor;
//    private String mTitle;
//
//    //映射传入的借口对象
//    private TopbarClickListener mListener;
//
//    public interface TopbarClickListener{
//        //左按钮点击事件
//        void leftClick();
//        //右按钮点击事件
//        void rightClick();
//    }
//
//    public void setOnTopbarClickListener(TopbarClickListener mListener){
//        this.mListener=mListener;
//    }
//
//    public TopBar(Context context) {
//        super(context);
//    }
//
//    public TopBar(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//    public TopBar(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        //设置topbar背景
//        setBackgroundColor(0xFFF59563);
//        //将atts.xml中定义的declare-styleable的所有属性的值存储到TypedArray中
//        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.TopBar);
//        //从TypedArray中读取出对应的值来要为设置的属性赋值
//        mLeftTextColor=ta.getColor(R.styleable.TopBar_leftTextColor,0);
//        mleftBackground=ta.getDrawable(R.styleable.TopBar_leftBackground);
//        mLeftText=ta.getString(R.styleable.TopBar_leftText);
//        mRightTextColor=ta.getColor(R.styleable.TopBar_rightTextColor,0);
//        mRightBackground=ta.getDrawable(R.styleable.TopBar_rightBackground);
//        mRightText=ta.getString(R.styleable.TopBar_rightText);
//        mTitleTextSize=ta.getInteger(R.styleable.TopBar_titleTextSize,10);
//        mTitleTextColor=ta.getColor(R.styleable.TopBar_titleTextColor,0);
//        mTitle=ta.getString(R.styleable.TopBar_title);
//        //获取完TypedArray的值后，一般要调用recycle方法来避免重新创建的时候的错误。
//        ta.recycle();
//        mLeftButton=new Button(context);
//        mRightButton=new Button(context);
//        mTitleView=new TextView(context);
//        //为创建的组件元素赋值，值来源于我们在引用的xml文件中给对应出行的赋值
//        mLeftButton.setTextColor(mLeftTextColor);
//        mLeftButton.setBackground(mleftBackground);
//        mLeftButton.setText(mLeftText);
//
//        mRightButton.setTextColor(mRightTextColor);
//        mRightButton.setBackground(mRightBackground);
//        mRightButton.setText(mRightText);
//
//        mTitleView.setText(mTitle);
//        mTitleView.setTextColor(mTitleTextColor);
//        mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP,mTitleTextSize);
//        mTitleView.setGravity(Gravity.CENTER);
//
//        //为组件元素设置相应的布局元素
//        mLeftParams=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
//        mLeftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,TRUE);
//        addView(mLeftButton,mLeftParams);
//
//        mRightParams=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
//        mRightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,TRUE);
//        addView(mRightButton,mRightParams);
//
//        mTitleParams=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
//        mTitleParams.addRule(RelativeLayout.CENTER_IN_PARENT,TRUE);
//        addView(mTitleView,mTitleParams);
//
//        //按钮的点击事件
//        mRightButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mListener.rightClick();
//            }
//        });
//
//        mLeftButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mListener.leftClick();
//            }
//        });
//    }
//
//    //设置按钮的显示与否，通过id区分按钮，flag区分是否显示
//    public void setButtonVisable(int id,boolean flag){
//        if (flag){
//            if (id==0){
//                mLeftButton.setVisibility(VISIBLE);
//            }else {
//                mRightButton.setVisibility(VISIBLE);
//            }
//        }else {
//            if (id==0){
//                mLeftButton.setVisibility(GONE);
//            }else {
//                mRightButton.setVisibility(GONE);
//            }
//        }
//    }
//}