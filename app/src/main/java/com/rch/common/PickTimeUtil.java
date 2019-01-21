package com.rch.common;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.rch.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/7/17.
 */

public class PickTimeUtil {

    public static TimePickerView pvTime;

    public static void pickShow(Context context, TextView tv, FrameLayout layout){
        pvTime = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                /*btn_Time.setText(getTime(date));*/
                TextView tv = (TextView) v;
                tv.setText(getTime(date));
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {//自定义控件

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        TextView ivCancel = (TextView) v.findViewById(R.id.tv_cancle);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvTime.returnData();
                                pvTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvTime.dismiss();
                            }
                        });
                    }
                })
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "", "", "") //设置空字符串以隐藏单位提示   hide label
                .setDividerColor(Color.DKGRAY)
                .setBackgroundId(0x00000000)
                .setOutSideCancelable(false)
                .setDecorView(layout)//非dialog模式下,设置ViewGroup, pickerView将会添加到这个ViewGroup中
                .build();

//                .setContentTextSize(20)
//                .setDate(startDate)
//                .setRangDate(startDate, selectedDate)

        pvTime.setKeyBackCancelable(false);//系统返回键监听屏蔽掉
        pvTime.show(tv,false);
    }

    private static String getTime(Date date) {//可根据需要自行截取数据显示
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String getTimeYM(Date date) {//可根据需要自行截取数据显示
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(date);
    }

    public interface timeListener{
        public void time(String date);
    }


    public static void pickShow(Context context, TextView tv, final timeListener timeListener ){
        pvTime = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
               /*btn_Time.setText(getTime(date));*/
                TextView tv = (TextView) v;
                tv.setText(getTime(date));
                if(timeListener!=null)
                {
                    timeListener.time(getTime(date));
                }
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {//自定义控件

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        TextView ivCancel = (TextView) v.findViewById(R.id.tv_cancle);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvTime.returnData();
                                pvTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvTime.dismiss();
                            }
                        });
                    }
                })
                .setType(new boolean[]{true, true, false, false, false, false})
                .setLabel("年", "月", "", "", "", "") //设置空字符串以隐藏单位提示   hide label
                .setDividerColor(Color.DKGRAY)
                .setBackgroundId(0x00000000)
                .setOutSideCancelable(false)
                .build();

                 /*.setContentTextSize(20)
                .setDate(startDate)
                .setRangDate(startDate, selectedDate)*/

        pvTime.setKeyBackCancelable(false);//系统返回键监听屏蔽掉
        pvTime.show(tv,false);
    }

    public static void pickShowYM(Context context, TextView tv, final timeListener timeListener ){
        pvTime = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
               /*btn_Time.setText(getTime(date));*/
                TextView tv = (TextView) v;
                tv.setText(getTimeYM(date));
                if(timeListener!=null)
                {
                    timeListener.time(getTimeYM(date));
                }
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {//自定义控件

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        TextView ivCancel = (TextView) v.findViewById(R.id.tv_cancle);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvTime.returnData();
                                pvTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvTime.dismiss();
                            }
                        });
                    }
                })
                .setType(new boolean[]{true, true, false, false, false, false})
                .setLabel("年", "月", "", "", "", "") //设置空字符串以隐藏单位提示   hide label
                .setDividerColor(Color.DKGRAY)
                .setBackgroundId(0x00000000)
                .setOutSideCancelable(false)
                .build();

                 /*.setContentTextSize(20)
                .setDate(startDate)
                .setRangDate(startDate, selectedDate)*/

        pvTime.setKeyBackCancelable(false);//系统返回键监听屏蔽掉
        pvTime.show(tv,false);
    }

    public interface ageListener{
        public void ageItem(String ageLable);
    }
    public static void ShowPickerView(final Context context, final TextView textView, final ageListener listener) {// 弹出选择器

        final List<String> options1Items= testData();
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                textView.setText(options1Items.get(options1));
                String ageLable="";
                textView.setTextColor(context.getResources().getColor(R.color.black_2));
//                if(options1==options1Items.size()-1)
//                    ageLable=String.valueOf(options1Items.get(options1));
//                else
//                    ageLable=String.valueOf(options1+1);
//                if(listener!=null)
                    listener.ageItem(options1Items.get(options1));
            }
        })

               /*  .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("城市选择")//标题
                .setSubCalSize(14)//确定和取消文字大小
                .setTitleSize(16)//标题文字大小
               .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                .setCancelColor(Color.BLUE)//取消按钮文字颜色
                .setTitleBgColor(0xFF333333)//标题背景颜色 Night mode
                .setBgColor(0xFF000000)//滚轮背景颜色 Night mode
                .setContentTextSize(12)//滚轮文字大小
                .setLinkage(false)//设置是否联动，默认true
                .setLabels("省", "市", "区")//设置选择的三级单位
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(1, 1, 1)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .isDialog(true)//是否显示为对话框样式
                .build();*/
                //.setTitleText("城市选择")
//                .setDividerColor(Color.BLACK)
//                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
//                .setTitleColor(Color.BLUE)//标题文字颜色
//                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
//                .setCancelColor(Color.BLUE)//取消按钮文字颜色
//                .setTitleBgColor(context.getResources().getColor(R.color.gray))//标题背景颜色 Night mode
//                .setContentTextSize(20)
//                .build();

                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(context.getResources().getColor(R.color.gray_13))//确定按钮文字颜色
                .setCancelColor(context.getResources().getColor(R.color.gray_3))//取消按钮文字颜色
                .setTitleBgColor(context.getResources().getColor(R.color.gray))//标题背景颜色 Night mode
                .setContentTextSize(20)
                .build();


        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        //pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器
        pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.show();
    }

    private static List<String> testData()
    {
        List<String> options1Items=new ArrayList<>();
        options1Items.add("1年内");
        options1Items.add("2年内");
        options1Items.add("3年内");
        options1Items.add("4年内");
        options1Items.add("5年内");
        options1Items.add("5年以上");
        return options1Items;
    }

    private static List<String> statusData()
    {
        List<String> options1Items=new ArrayList<>();
        options1Items.add("待回访");
        options1Items.add("客户到店看车");
        options1Items.add("门店已确认看车");
        options1Items.add("客户已买车");
        options1Items.add("门店确认失败");
        options1Items.add("已取消");
        return options1Items;
    }


    public interface statusListener{
        public void statusItem(String statusLable);
    }

    public static void ShowStatusView(Context context, final TextView textView, final statusListener listener) {// 弹出选择器

        final List<String> options1Items=  statusData();
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                textView.setText(options1Items.get(options1));
                String ageLable=String.valueOf(options1+1);
                if(listener!=null)
                    listener.statusItem(ageLable);
            }
        })


//                .setDividerColor(Color.BLACK)
//                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
//                .setTitleColor(Color.BLUE)//标题文字颜色
//                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
//                .setCancelColor(Color.BLUE)//取消按钮文字颜色
//                .setTitleBgColor(context.getResources().getColor(R.color.gray))//标题背景颜色 Night mode
//                .setContentTextSize(20)
//                .build();


                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(context.getResources().getColor(R.color.gray_13))//确定按钮文字颜色
                .setCancelColor(context.getResources().getColor(R.color.gray_3))//取消按钮文字颜色
                .setTitleBgColor(context.getResources().getColor(R.color.gray))//标题背景颜色 Night mode
                .setContentTextSize(20)
                .build();


        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        //pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器
        pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.show();
    }


    public interface CheckListion{
        public void over();
    }

    public static void picktimeShow(final Context context, final String type, TextView tv, final CheckListion listion){
        pvTime = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                /*btn_Time.setText(getTime(date));*/
                TextView tv = (TextView) v;
                if(type.equals("1")) {
                    tv.setText(getTime(date));
                }else {
                    tv.setText(getTimeYM(date)+"-01");
                }
                tv.setTextColor(context.getResources().getColor(R.color.black_2));
                listion.over();
            }
        })
//                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {//自定义控件
//
//                    @Override
//                    public void customLayout(View v) {
//                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
//                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
//                        tvSubmit.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                pvTime.returnData();
//                                pvTime.dismiss();
//                            }
//                        });
//                        ivCancel.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                pvTime.dismiss();
//                            }
//                        });
//                    }
//                })
                .setType(new boolean[]{true, true, false, false, false, false})
                .setLabel("年", "月", "", "", "", "") //设置空字符串以隐藏单位提示   hide label
                .setDividerColor(Color.DKGRAY)
                .setSubmitColor(context.getResources().getColor(R.color.gray_13))
                .setCancelColor(context.getResources().getColor(R.color.gray_3))
                .setBackgroundId(0x00000000)
                .setOutSideCancelable(false)
//                .setDecorView(layout)//非dialog模式下,设置ViewGroup, pickerView将会添加到这个ViewGroup中比如从title下面里面弹出来就可以在title下面放一个线
                .build();

//                .setContentTextSize(20)
//                .setDate(startDate)
//                .setRangDate(startDate, selectedDate)

        pvTime.setKeyBackCancelable(false);//系统返回键监听屏蔽掉
        pvTime.show(tv,false);
    }

    public static void pickShowYMD(final Context context, TextView tv, final CheckListion listion){
        pvTime = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                /*btn_Time.setText(getTime(date));*/
                TextView tv = (TextView) v;
                tv.setText(getTime(date));
                tv.setTextColor(context.getResources().getColor(R.color.black_2));
                listion.over();
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "", "", "") //设置空字符串以隐藏单位提示   hide label
                .setDividerColor(Color.DKGRAY)
                .setBackgroundId(0x00000000)
                .setOutSideCancelable(false)
//                .setDecorView(layout)//非dialog模式下,设置ViewGroup, pickerView将会添加到这个ViewGroup中比如从title下面里面弹出来就可以在title下面放一个线
                .build();
        pvTime.setKeyBackCancelable(false);//系统返回键监听屏蔽掉
        pvTime.show(tv,false);
    }

}
