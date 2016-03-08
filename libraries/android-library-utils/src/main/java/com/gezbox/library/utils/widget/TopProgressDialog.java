package com.gezbox.library.utils.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gezbox.library.utils.R;




public class TopProgressDialog extends Dialog {

    private static final int LOADING = 0;                          //正在加载
    private static final int LOADINGFAIL = 1;                      //加载失败
    private static final int LOADINGSUCCESS = 2;                   //加载成功

    private TextView tv_title;                                    //对话框标题
    private ImageView iv_icon;                                    //对话框左侧Icon
    private RelativeLayout rl_progress_dialog;                    //对话框背景
    private ImageView iv_status;                                  //对话框右侧Icon
    private AnimationDrawable animationDrawable;

    private static TopProgressDialog progressDialog;
    private static Context sContext;
    private static Handler sHandler;
    private static Runnable sFinishRunnable;


    public static void onLoading(final Context context, final String message) {
        build(context).setStatus(LOADING, message);
    }

    public static void onLoadSuccess(final Context context, final String message) {
        build(context).setStatus(LOADINGSUCCESS, message);
    }

    public static void onLoadFailure(final Context context, final String message) {
        build(context).setStatus(LOADINGFAIL, message);
    }

    private static TopProgressDialog build(Context context) {

        finishDialogByContext(context);     //若context发生变化，则直接销毁旧dialog对象

        if (progressDialog == null) {
            progressDialog = new TopProgressDialog(context, R.style.util_top_dialog);
        }
        return progressDialog;
    }


    private TopProgressDialog(Context context, int theme) {
        super(context, theme);
        initView(this);
        sHandler = new Handler();
        sFinishRunnable = new Runnable() {
            @Override
            public void run() {
                finishDialog();
            }
        };
    }

    private void initView(Dialog dialog) {
        dialog.setTitle("");
        dialog.setContentView(R.layout.dialog_top_progress);
        tv_title = (TextView) dialog.findViewById(R.id.tv_title);
        iv_icon = (ImageView) dialog.findViewById(R.id.iv_icon);
        rl_progress_dialog = (RelativeLayout) dialog.findViewById(R.id.rl_progress_dialog);
        iv_status = (ImageView) dialog.findViewById(R.id.iv_status);
        dialog.setCancelable(false);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setWindowAnimations(R.style.util_dialog_view_anim);
        dialog.show();
    }


    private void setStatus(int status, String text) {

        tv_title.setText(text);

        switch (status) {
            case LOADING:
                startAnimation();
                break;
            case LOADINGFAIL:
                stopAnimation();
                setLoadFailureView();
                break;
            case LOADINGSUCCESS:
                stopAnimation();
                setLoadSuccessView();
                break;
        }
    }


    private void startAnimation() {
        iv_icon.setBackgroundResource(R.drawable.anim_top_loading_bg);
        animationDrawable = (AnimationDrawable) iv_icon.getBackground();
        animationDrawable.start();
    }

    private void stopAnimation() {
        if (animationDrawable != null && animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
    }


    private void setLoadSuccessView() {
        iv_icon.setBackgroundResource(R.drawable.ic_top_load_ok);
        sHandler.postDelayed(sFinishRunnable, 1000);
    }



    private void setLoadFailureView() {
        iv_icon.setBackgroundResource(R.drawable.ic_top_load_fail);
        tv_title.setTextColor(getContext().getResources().getColor(R.color.util_white));
        rl_progress_dialog.setBackgroundResource(R.color.util_red);
        iv_status.setVisibility(View.VISIBLE);
        iv_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishDialog();
            }
        });
    }


    private static void finishDialog() {

        if (progressDialog == null) {
            return;
        }

        if (sHandler != null) {
            sHandler.removeCallbacks(sFinishRunnable);
            sHandler = null;
        }

        try {
            progressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            progressDialog = null;
        }
    }


    private static void finishDialogByContext(Context context) {
        if (sContext != null && !context.getClass().getSimpleName().equals(sContext.getClass().getSimpleName())) {
            finishDialog();
        }
        sContext = context;
    }

}