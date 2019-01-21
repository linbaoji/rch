package com.rch.custom;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rch.R;

/**
 * Created by Administrator on 2018/5/14.
 */

public class CertifiedPromptDialog extends Dialog {
    Context context;
    public CertifiedPromptDialog(@NonNull Context context) {
        super(context);
        this.context=context;
        init();
    }

    public CertifiedPromptDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context=context;
        init();
    }

    protected CertifiedPromptDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context=context;
        init();
    }

    private void init()
    {
        setContentView(R.layout.certified_prompt_dialog);
    }

//    public void setDesc(String desc)
//    {
//        ((TextView) findViewById(R.id.certified_prompt_desc)).setText(desc);
//    }
//
//    TextView tvBtn;

    ImageView iv_shareorfinsh;

    public void setBgImage(int image)
    {
        findViewById(R.id.rl_rzbg).setBackgroundResource(image);
    }


    public void setBtnImage(int image, final OnClickListener listener)
    {
        iv_shareorfinsh= (ImageView) findViewById(R.id.iv_shareorfinsh);
        iv_shareorfinsh.setImageResource(image);
        iv_shareorfinsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    listener.onClick();
                }
            }
        });
    }



//    public void setButtonOnClickListener(String btnText,final OnClickListener listener)
//    {
//        tvBtn= (TextView) findViewById(R.id.certified_prompt_btn);
//        tvBtn.setText(btnText);
//        tvBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(listener!=null)
//                    listener.onClick();
//            }
//        });
//    }

    public interface OnClickListener{
        public void onClick();
    }
}
