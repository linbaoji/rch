package com.rch.custom;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rch.R;

/**
 * Created by Administrator on 2018/5/25.
 */

public class ShareSuccessDialog extends Dialog {
    public ShareSuccessDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public ShareSuccessDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected ShareSuccessDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init()
    {
        setContentView(R.layout.share_dialog);
    }

    public void setBagRes(int res)
    {
        ((RelativeLayout)findViewById(R.id.rl_bg)).setBackgroundResource(res);
    }



    ImageView iv_deleat,iv_continu;
    public void setDeleatClick(final OnClickListener listener)
    {
        iv_deleat= (ImageView) findViewById(R.id.iv_delet);
        iv_deleat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)
                    listener.onClick();
            }
        });
    }

    public void setContinuClick(final OnClickListener listener)
    {
        iv_continu= (ImageView) findViewById(R.id.iv_continu);
        iv_continu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)
                    listener.onClick();
            }
        });
    }

    public interface OnClickListener{
        public void onClick();
    }

}
