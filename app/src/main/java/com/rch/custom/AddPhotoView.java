package com.rch.custom;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rch.R;
import com.rch.activity.CarDetailActivity;
import com.rch.common.GeneralUtils;
import com.rch.common.GlideUtils;
import com.rch.common.SpUtils;


/**
 * Created by acer on 2018/8/15.
 */

public class AddPhotoView extends LinearLayout {
    private Context context;

    ImageView iv_fm;
    ImageView iv_fm_deleat;
    LinearLayout ll_fmpr;
    ProgressBar pb_fm;
    TextView tv_fmjd;
    ImageView iv_fmlog ;
    TextView tv_name;
    TextView red;
    LinearLayout ll_phone;

    public AddPhotoView(Context context) {
        super(context);
        this.context = context;
    }

    public AddPhotoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public AddPhotoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void initDate(final String type, String url) {
         removeAllViews();

         View view = View.inflate(context, R.layout.item_adddphoto, null);
         iv_fm = (ImageView) view.findViewById(R.id.iv_fm);
         iv_fm_deleat = (ImageView) view.findViewById(R.id.iv_fm_deleat);
         ll_fmpr = (LinearLayout) view.findViewById(R.id.ll_fmpr);
         pb_fm = (ProgressBar) view.findViewById(R.id.pb_fm);
         tv_fmjd = (TextView) view.findViewById(R.id.tv_fmjd);
         iv_fmlog = (ImageView) view.findViewById(R.id.iv_fmlog);
         tv_name = (TextView) view.findViewById(R.id.tv_name);
         red = (TextView) view.findViewById(R.id.tv_red);
         ll_phone= (LinearLayout) view.findViewById(R.id.ll_phone);


         int with=(SpUtils.getScreenWith(context)-GeneralUtils.dip2px(context,50))/3;
         int screen_height= SpUtils.get_View_heigth(with,1,1);

        ViewGroup.LayoutParams params=ll_phone.getLayoutParams();
        params.width=with;
        params.height=screen_height;
        ll_phone.setLayoutParams(params);

        iv_fmlog.setVisibility(GONE);
        ll_fmpr.setVisibility(GONE);
        red.setVisibility(GONE);
        iv_fm_deleat.setVisibility(GONE);
        checktext(type);//字体
        if(type.equals("01")){
            iv_fmlog.setVisibility(VISIBLE);
        }

        if(TextUtils.isEmpty(url)) {//图片
            checkImageType(type);
        }else {
            GlideUtils.showImg(context,url,R.mipmap.car_emp,DiskCacheStrategy.ALL,iv_fm);
//            Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.car_emp).into(iv_fm);
            iv_fm_deleat.setVisibility(VISIBLE);
            iv_fm.setEnabled(false);
        }

        iv_fm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                opencanme.open(type,iv_fm,iv_fm_deleat,ll_fmpr);
            }
        });

        iv_fm_deleat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_fm.setEnabled(true);
                iv_fm_deleat.setVisibility(GONE);
                checkImageType(type);
                deleat.deleat(type);
            }
        });


        addView(view);
    }

    private void checktext(String type) {
        if (type.equals("01")) {
            tv_name.setText("左前侧45度");
            red.setVisibility(VISIBLE);
        }
        if (type.equals("02")) {
            tv_name.setText("左侧");
            red.setVisibility(VISIBLE);

        }
        if (type.equals("03")) {
            tv_name.setText("左后侧45度");

        }
        if (type.equals("04")) {
            tv_name.setText("驾驶舱座椅");

        }
        if (type.equals("05")) {
            tv_name.setText("仪表盘");

        }
        if (type.equals("06")) {
            tv_name.setText("发动机舱");

        }
        if (type.equals("07")) {
            tv_name.setText("车头");

        }
        if (type.equals("08")) {
            tv_name.setText("车尾");

        }
        if (type.equals("09")) {
            tv_name.setText("右侧");
        }
        if (type.equals("10")) {
            tv_name.setText("右后侧45度");
        }
        if (type.equals("11")) {
            tv_name.setText("右前侧45度");
        }
        if (type.equals("12")) {
            tv_name.setText("轮毂轮胎");

        }
        if (type.equals("13")) {
            tv_name.setText("后排座椅");

        }
        if (type.equals("14")) {
            tv_name.setText("后背箱");
        }
    }

    private void checkImageType(String type) {
        if (type.equals("01")) {
            iv_fm.setImageResource(R.mipmap.fbfm);
        }
        if (type.equals("02")) {
            iv_fm.setImageResource(R.mipmap.fbzc);
        }
        if (type.equals("03")) {
            iv_fm.setImageResource(R.mipmap.fbzhc);

        }
        if (type.equals("04")) {
            iv_fm.setImageResource(R.mipmap.fbjsczy);

        }
        if (type.equals("05")) {
            iv_fm.setImageResource(R.mipmap.fbybp);

        }
        if (type.equals("06")) {
            iv_fm.setImageResource(R.mipmap.fdjc);

        }
        if (type.equals("07")) {
            iv_fm.setImageResource(R.mipmap.fbct);

        }
        if (type.equals("08")) {
            iv_fm.setImageResource(R.mipmap.fbcw);

        }
        if (type.equals("09")) {
            iv_fm.setImageResource(R.mipmap.fbyc);
        }
        if (type.equals("10")) {
            iv_fm.setImageResource(R.mipmap.fbyhc);

        }
        if (type.equals("11")) {
            iv_fm.setImageResource(R.mipmap.fbyqc);
        }
        if (type.equals("12")) {
            iv_fm.setImageResource(R.mipmap.fbryrt);

        }
        if (type.equals("13")) {
            iv_fm.setImageResource(R.mipmap.fbhpzy);

        }
        if (type.equals("14")) {
            iv_fm.setImageResource(R.mipmap.fbhbx);

        }
    }

     openCanme opencanme;
     deleatUrl deleat;


    public void setOpencanme(AddPhotoView.openCanme opencanme) {
        this.opencanme = opencanme;
    }

    public interface openCanme{
       public void open(String type,ImageView iv_fm,ImageView iv_deleat,LinearLayout ll_pr);
    }


    public void setDeleat(deleatUrl deleat) {
        this.deleat = deleat;
    }

    public interface deleatUrl{
        public void deleat(String type);
    }


}
