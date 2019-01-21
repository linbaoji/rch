package com.rch.common;

/**
 * Created by Administrator on 2018/9/3.
 */

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

/**
 * 2
 * <p>* 用户输入验证器* <p>* <p>* 3* <p>* <p/>* <p>* <p>* 4* <p>* Created by zhuwentao on 2016-08-04.* <p>* <p>* 5
 */


public class ExamineTextWatcher implements TextWatcher {
    private static final String TAG = "ExamineTextWatcher";

    /*** 10* <p>* 帐号* <p>* <p>* 11*/


    public static final int TYPE_ACCOUNT = 1;

    /**15<p>金额<p><p>16*/

    public static final int TYPE_MONEY = 2;

    public static final int TYPE_LICHENG = 3;

    public static final int TYPE_PL = 4;

    public static final int TYPE_ZCMONEY = 5;

    public static final int TYPE_UNIFIEDCODE =6 ;
    /*** 20* <p>* 输入框* <p>* <p>* 21*/
    private EditText mEditText;

    /*** 25* <p>* 验证类型* <p>* <p>* 26*/
    private int examineType;
    /*** 30* <p>* 输入前的文本内容* <p>* <p>* 31*/
    private String beforeText;
    /*** 35* <p>* 构造器* <p>* <p>* 36* <p>* <p>* <p>* <p>* 37** @param type 验证类型* <p>* <p>* 38* @param editText 输入框*<p>* <p>* 39*/
    public ExamineTextWatcher(int type, EditText editText) {

        this.examineType = type;

        this.mEditText = editText;

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // 输入前的字符
        beforeText = s.toString();
        Log.d(TAG, "beforeText =>>>" + beforeText);
    }


    @Override


    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // 输入后的字符
        String afterText = s.toString();
        Log.d(TAG, "afterText =>>>" + afterText);
        boolean isValid = true;
        if (!TextUtils.isEmpty(afterText)) {
            switch (examineType) {
                case TYPE_ACCOUNT:
                    isValid = ValidateUtil.isAccount(afterText);
                    break;
                case TYPE_MONEY:
                    isValid = ValidateUtil.isMoney(afterText);
                    break;
                case TYPE_LICHENG:
                    isValid = ValidateUtil.isLicheng(afterText);
                    break;
                case TYPE_PL:
                    isValid = ValidateUtil.isPl(afterText);
                    break;
                case TYPE_ZCMONEY:
                    isValid = ValidateUtil.isZcMoney(afterText);
                    break;
                case TYPE_UNIFIEDCODE:
                    isValid = ValidateUtil.isUnified(afterText);
                    break;

            }


            if (!isValid) {
                // 用户现在输入的字符数减去之前输入的字符数，等于新增的字符数
                int differ = afterText.length() - beforeText.length();
                // 如果用户的输入不符合规范，则显示之前输入的文本
                mEditText.setText(beforeText);
                // 光标移动到文本末尾
                mEditText.setSelection(afterText.length() - differ);
            }

        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }


}



