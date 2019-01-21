package com.rch.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rch.R;


/**
 * 自定义通用控件
 * Created by wangbin on 16/2/15.
 */
public class CommonView extends RelativeLayout {

    private ImageView ivCommon;//左边图表
    private TextView tvCommon;//左边文字
    private TextView tvNotes;//右边按钮文字
    private ImageView ivArrow;//右边箭头
    private TextView tv_centtext;//左边文字邮编的描述


    /**
     * 字体颜色
     */
    private int desTextColor;
    /**
     * 分类的图片
     */
    private Drawable categoryImage;
    /**
     * 文字描述
     */
    private String desText;
    /**
     * 文字大小 默认16
     */
    private float desTextSize;
    /**
     * 是否显示分类图片
     */
    private boolean isShowCategoryImage;
    /**
     * 分类的状态图片
     */
    private Drawable statusImage;
    /**
     * 右边箭头图片
     */
    private int ivImage;
    /**
     * 分类的状态图片的高 默认16
     */
    private float statusImage_width;
    /**
     * 分类的状态图片的宽 默认16
     */
    private float statusImage_height;

    /**
     * 是否显示最邮编文字
     */
    private boolean isShowTextNotes;
    private String notesText;
    private int notesTextColor;
    private float notesTextSize;
    private boolean flag;
    private boolean tvNotesSingleLine;


    private boolean isShowArraw;//显示邮编箭头


    /**
     * 是否显示最左边文字邮编的文字
     */
    private boolean isShowTextcent;
    private String centText;
    private int centextColor;
    private float centTextSize;
    private boolean centflag;
    private boolean tvcentSingleLine;

    public CommonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonView);
        desTextColor = mTypedArray.getColor(R.styleable.CommonView_desTextColor,
               getResources().getColor(R.color.black_1));//字体颜色
        categoryImage = mTypedArray
                .getDrawable(R.styleable.CommonView_categoryImage);

        desText = mTypedArray.getString(R.styleable.CommonView_desText);//字体

        desTextSize = mTypedArray.getDimensionPixelSize(
                R.styleable.CommonView_desTextSize, 28);//字体大小

        isShowCategoryImage = mTypedArray.getBoolean(
                R.styleable.CommonView_isShowCategoryImage, true);//是否显示左边图片(这里没用到)

        statusImage = mTypedArray.getDrawable(R.styleable.CommonView_statusImage);//左边图片

//        statusImage_width = mTypedArray.getDimensionPixelSize(
//                R.styleable.CommonView_statusImage_width, 36);
//
//        statusImage_height = mTypedArray.getDimensionPixelSize(
//                R.styleable.CommonView_statusImage_height, 36);

        isShowTextNotes = mTypedArray.getBoolean(R.styleable.CommonView_isShowTextNotes, true);//是否显示
        notesText = mTypedArray.getString(R.styleable.CommonView_notesText);
        notesTextColor = mTypedArray.getColor(R.styleable.CommonView_notesTextColor, getResources().getColor(R.color.black_1));
        notesTextSize = mTypedArray.getDimensionPixelSize(R.styleable.CommonView_notesTextSize, 28);
        tvNotesSingleLine=mTypedArray.getBoolean(R.styleable.CommonView_isSingleTextNotes,true);

        isShowTextcent = mTypedArray.getBoolean(R.styleable.CommonView_isShowTextCent, false);//是否显示
        centText = mTypedArray.getString(R.styleable.CommonView_centText);
        centextColor = mTypedArray.getColor(R.styleable.CommonView_centTextColor, getResources().getColor(R.color.black_1));
        centTextSize = mTypedArray.getDimensionPixelSize(R.styleable.CommonView_centTextSize, 28);
        tvcentSingleLine=mTypedArray.getBoolean(R.styleable.CommonView_isSingleTextCent,true);

        isShowArraw=mTypedArray.getBoolean(R.styleable.CommonView_isShowArraw,true);

        mTypedArray.recycle();

        initData();

    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.common_view, this);
        ivCommon = (ImageView) view.findViewById(R.id.ivCommon);
        tvCommon = (TextView) view.findViewById(R.id.tvCommon);
        tvNotes = (TextView) view.findViewById(R.id.tvNotes);
        ivArrow =(ImageView) view.findViewById(R.id.ivArrow);
        tv_centtext= (TextView) view.findViewById(R.id.tv_centtext);
    }

    /**
     * 设置分类状态图片的宽和高
     *
     * @param statusImage_width  单位是dp 无需自己转换
     * @param statusImage_height 单位是dp 无需自己转换
     */
    public void setStatusImageWH(float statusImage_width,
                                 float statusImage_height) {
        android.view.ViewGroup.LayoutParams params = ivCommon
                .getLayoutParams();
        params.width = (int) statusImage_width;
        ivCommon.setLayoutParams(params);
        params.height = (int) statusImage_height;
        this.statusImage_width = statusImage_width;
        this.statusImage_height = statusImage_height;
    }

    /**
     * 设置分类状态的图片
     *
     * @param statusImage
     */
    @SuppressWarnings("deprecation")
    public void setStatusImage(Drawable statusImage) {
        if (statusImage != null) {
            ivCommon.setBackgroundDrawable(statusImage);
            this.statusImage = statusImage;
        }

    }

    /**
     * 设置是否显示分类图片
     *
     * @param isShowCategoryImage
     */
    public void setIsShowCategoryImage(boolean isShowCategoryImage) {
        if (isShowCategoryImage) {
            ivCommon.setVisibility(View.VISIBLE);
        } else {
            ivCommon.setVisibility(View.GONE);
        }
        this.isShowCategoryImage = isShowCategoryImage;

    }

    /**
     * 设置描述字体的大小
     *
     * @param desTextSize 单位是sp
     */
    public void setDesTextSize(float desTextSize) {
        tvCommon.setTextSize(TypedValue.COMPLEX_UNIT_PX, desTextSize);
        // tv_des.setTextSize( desTextSize);
        this.desTextSize = desTextSize;

    }

    /**
     * 设置描述信息
     *
     * @param desText
     */
    public void setDesText(String desText) {
        if (desText != null) {
            tvCommon.setText(desText);
            this.desText = desText;
        }
    }

    /**
     * 设置 分类的图片
     */
//    @SuppressWarnings("deprecation")
//    public void setCategoryImage(Drawable categoryImage) {
//        if (categoryImage != null) {
//            iv_category.setBackgroundDrawable(categoryImage);
//            this.categoryImage = categoryImage;
//        }
//
//    }

    /**
     * 设置描述字体颜色
     *
     * @param desTextColor
     */
    public void setDesTextColor(int desTextColor) {
        tvCommon.setTextColor(desTextColor);
        this.desTextColor = desTextColor;
    }

    /**
     * 设置右边文字
     * @param notesText
     */
    public void setNotesText(String notesText) {
        if (notesText != null) {
            tvNotes.setText(notesText);
            this.notesText = notesText;
        }
    }

    /**
     * 设置中间文字
     * @param centText
     */
    public void setcentText(String centText) {
        if (centText != null) {
            tv_centtext.setText(centText);
            this.centText = centText;
        }
    }



    /**
     * 设置右边文字颜色
     * @param notesTextColor
     */
    public void setNotesTextColor(int notesTextColor){
        tvNotes.setTextColor(notesTextColor);
        this.notesTextColor=notesTextColor;
    }

    /**
     * 设置中间文字颜色
     * @param centTextColor
     */
    public void setCentTextColor(int centTextColor){
        tv_centtext.setTextColor(centTextColor);
        this.centextColor=centTextColor;
    }


    public void setSingleLine(boolean singleLine){
        tvNotes.setSingleLine(singleLine);
        this.tvNotesSingleLine=singleLine;
    }

    public void setcentSingleLine(boolean singleLine){
        tv_centtext.setSingleLine(singleLine);
        this.tvcentSingleLine=singleLine;
    }

    /***
     * 设置右边箭头可见不可见
     * @param flag
     */
    public void setIvArrow(boolean flag){
        if (flag==true){
            ivArrow.setVisibility(View.VISIBLE);
            setEnabled(true);
        }else {
            ivArrow.setVisibility(View.INVISIBLE);
            setEnabled(false);
        }
        this.isShowArraw=flag;
    }

    /***
     * 设置右边箭头可见不可见
     * @param flag
     */
    public void setIvArrow(int res,boolean flag){
        if (flag==true){
            ivArrow.setVisibility(View.VISIBLE);
            ivArrow.setImageResource(res);
            setEnabled(true);
        }else {
            ivArrow.setVisibility(View.INVISIBLE);
            setEnabled(false);
        }
        this.isShowArraw=flag;
    }




    /**
     * 设置字体大小
     * @param notesTextSize
     */
    public void setNotesTextSize(float notesTextSize){
        tvNotes.setTextSize(TypedValue.COMPLEX_UNIT_PX, notesTextSize);
        this.notesTextSize=notesTextSize;
    }

    /**
     * 设置字体大小
     * @param centTextSize
     */
    public void setcentTextSize(float centTextSize){
        tv_centtext.setTextSize(TypedValue.COMPLEX_UNIT_PX, centTextSize);
        this.centTextSize=centTextSize;
    }

    public void setIsShowNotesText(boolean isShowTextNotes){
        if(isShowTextNotes){
            tvNotes.setVisibility(View.VISIBLE);
        }else{
            tvNotes.setVisibility(View.GONE);
        }
        this.isShowTextNotes=isShowTextNotes;
    }


    /**
     * 设置中间文字是否课件
     * @param isShowTextCent
     */
    public void setIsShowCentText(boolean isShowTextCent){
        if(isShowTextCent){
            tv_centtext.setVisibility(View.VISIBLE);
        }else{
            tv_centtext.setVisibility(View.GONE);
        }
        this.isShowTextcent=isShowTextCent;
    }


    /**
     * 初始化数据参数
     */
    private void initData() {
        setDesTextColor(desTextColor);
//        setCategoryImage(categoryImage);
        setDesText(desText);
        setDesTextSize(desTextSize);
        setIsShowCategoryImage(isShowCategoryImage);
        setStatusImage(statusImage);
//        setStatusImageWH(statusImage_width, statusImage_height);

        setNotesText(notesText);
        setNotesTextColor(notesTextColor);
        setNotesTextSize(notesTextSize);
        setIsShowNotesText(isShowTextNotes);
        setSingleLine(tvNotesSingleLine);

        setcentSingleLine(tvcentSingleLine);
        setcentText(centText);
        setcentTextSize(centTextSize);
        setCentTextColor(centextColor);
        setIsShowCentText(isShowTextcent);
        setIvArrow(isShowArraw);
    }
}
