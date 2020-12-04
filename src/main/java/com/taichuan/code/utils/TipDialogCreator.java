package com.taichuan.code.utils;

import android.content.Context;
import android.content.DialogInterface;

import com.taichuan.code.mvp.view.viewimpl.CancelAble;
import com.taichuan.code.ui.dialog.TipDialog;

/**
 * @author gui
 * @date 2020/12/3
 */
public class TipDialogCreator {
    private Context context;
    private CancelAble cancelAble;

    public TipDialogCreator(Context context, CancelAble cancelAble) {
        this.context = context;
        this.cancelAble = cancelAble;
    }

    public void showTipDialog(String tipMsg, final boolean isFinishWhenCancel) {
        showTipDialog(tipMsg, isFinishWhenCancel, false, null, null, null);
    }


    public void showTipDialog(String tipMsg, TipDialog.TipClickCallBack tipClickCallBack) {
        showTipDialog(tipMsg, false, false, null, null, tipClickCallBack);
    }

    public void showTipDialog(String tipMsg, boolean canceledOnTouchOutside, TipDialog.TipClickCallBack tipClickCallBack) {
        showTipDialog(tipMsg, false, canceledOnTouchOutside, null, null, tipClickCallBack);
    }

    public void showTipDialog(String tipMsg, boolean canceledOnTouchOutside, String cancelString, String confirmString, TipDialog.TipClickCallBack tipClickCallBack) {
        showTipDialog(tipMsg, false, canceledOnTouchOutside, cancelString, confirmString, tipClickCallBack);
    }

    public void showTipDialog(String tipMsg, final boolean isFinishWhenCancel, boolean canceledOnTouchOutside, String cancelString, String confirmString, TipDialog.TipClickCallBack tipClickCallBack) {
        TipDialog tipDialog = new TipDialog(context);
        tipDialog.setTipClickCallBack(tipClickCallBack);
        tipDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        tipDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (isFinishWhenCancel && cancelAble != null) {
                    cancelAble.toCancel();
                }
            }
        });
        tipDialog.setTipText(tipMsg);
        tipDialog.setButtonText(cancelString, confirmString);
        try {
            tipDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
