package com.taichuan.code.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.taichuan.code.R;
import com.taichuan.code.app.AppGlobal;
import com.taichuan.code.app.ConfigType;
import com.taichuan.code.mvp.view.base.BaseDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author gui
 * @date 2017/11/2
 */
public class TipDialog extends BaseDialog {
    private TipClickCallBack mTipClickCallBack;
    private TextView tv_tip;
    private TextView btn_confirm;
    private TextView btn_cancel;
    private View diving;

    private String tipText;
    private String cancelString;
    private String confirmString;

    public interface TipClickCallBack {
        void onConfirm();

        void onCancel();
    }

    public TipDialog(@NonNull Context context) {
        this(context, null);
    }

    public TipDialog(@NonNull Context context, TipClickCallBack tipClickCallBack) {
        super(context, R.style.Dialog_No_Border);
        mTipClickCallBack = tipClickCallBack;
    }

    @Override
    protected Object setRootLayout() {
        Integer tipDialogLayout = AppGlobal.getConfiguration(ConfigType.TIP_DIALOG_LAYOUT);
        if (tipDialogLayout != null) {
            return tipDialogLayout;
        } else {
            return R.layout.dialog_tip;
        }
    }

    @Override
    public void initView() {
        tv_tip = findViewById(R.id.tv_tip);
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_cancel = findViewById(R.id.btn_cancel);
        diving = findViewById(R.id.diving);

        // 颜色
        Integer themeColor = AppGlobal.getConfiguration(ConfigType.THEME_COLOR);
        if (themeColor != null) {
            btn_confirm.setTextColor(themeColor);
        }
        // 如果没有设置按钮点击设置，则隐藏"取消"按钮，显示"确定"按钮
        if (mTipClickCallBack == null) {
            if (btn_cancel != null) {
                btn_cancel.setVisibility(View.GONE);
            }
            if (diving != null) {
                diving.setVisibility(View.GONE);
            }
        }
        // 提示文本
        if (tipText != null) {
            tv_tip.setText(tipText);
        }
        if (cancelString != null) {
            btn_cancel.setText(cancelString);
        }
        if (confirmString != null) {
            btn_confirm.setText(confirmString);
        }
    }

    @Override
    public void initListener() {
        // 点击事件
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTipClickCallBack != null) {
                    mTipClickCallBack.onConfirm();
                }
                cancel();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTipClickCallBack != null) {
                    mTipClickCallBack.onCancel();
                }
                cancel();
            }
        });
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState) {
    }


    public void setTipText(String tipText) {
        this.tipText = tipText;
        if (tv_tip != null) {
            tv_tip.setText(tipText);
        }
    }

    public void setButtonText(String cancelString, String confirmString) {
        this.cancelString = cancelString;
        this.confirmString = confirmString;
        if (cancelString != null) {
            TextView btn_cancel = (TextView) findViewById(R.id.btn_cancel);
            if (btn_cancel != null) {
                btn_cancel.setText(cancelString);
            }
        }
        if (confirmString != null) {
            TextView btn_confirm = (TextView) findViewById(R.id.btn_confirm);
            if (btn_confirm != null) {
                btn_confirm.setText(confirmString);
            }
        }
    }

    public void setTipClickCallBack(TipClickCallBack tipClickCallBack) {
        mTipClickCallBack = tipClickCallBack;
    }
}
