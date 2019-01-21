package com.rch.custom;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.rch.R;


/**
 * Created by Administrator on 2017/7/24.
 */

public class LoadingDialog extends Dialog {
    Context context;
    public LoadingDialog(@NonNull Context context) {
        super(context);
        this.context=context;
        init();
    }

    public LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context=context;
        init();
    }

    protected LoadingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context=context;
        init();
    }

    private void init()
    {
        setContentView(R.layout.loading_dialog);
//        ImageView imageView = (ImageView) findViewById(R.id.iv_anim);
//        Glide.with(context).load(R.drawable.animt).into(imageView);

    }

    public void setText(String msg)
    {
        ((TextView)findViewById(R.id.loading_msg)).setText(msg);
    }

    public void showAnim()
    {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_loading);
        ((ImageView)findViewById(R.id.loading_img)).startAnimation(animation);//开始动画
    }


    public void closeAnim()
    {
        ((ImageView)findViewById(R.id.loading_img)).clearAnimation();//开始动画
    }

}
