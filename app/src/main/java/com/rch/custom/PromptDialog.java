package com.rch.custom;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rch.R;


/**
 * Created by Administrator on 2018/4/19.
 */

public class PromptDialog extends Dialog {
    public PromptDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public PromptDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected PromptDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init()
    {
        setContentView(R.layout.prompt_dialog);
    }

    public void setText(String msg)
    {
        ((TextView)findViewById(R.id.prompt_text)).setText(msg);
    }

    public void setLeftButtonVisible(boolean flag)
    {
        leftButton.setVisibility(View.GONE);
    }

    public void setRightButtonVisible(boolean flag)
    {
        rightButton.setVisibility(View.GONE);
    }

    TextView leftButton,rightButton;
    public void setLeftButtonText(String msg, final OnClickListener listener)
    {
        leftButton= (TextView) findViewById(R.id.prompt_left_btn);
        leftButton.setText(msg);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)
                    listener.onClick();
            }
        });
    }

    public void setRightButtonText(String msg, final OnClickListener listener)
    {
        rightButton= (TextView) findViewById(R.id.prompt_right_btn);
        rightButton.setText(msg);
        rightButton.setOnClickListener(new View.OnClickListener() {
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
