package com.taichuan.code.mvp.view.permission;

import android.app.Activity;

import androidx.fragment.app.DialogFragment;

/**
 * @author gui
 * @date 2016/11/28
 * 封装了运行时权限请求的Activity
 */
public class PermissionBaseDialogFragment extends DialogFragment {
    public void checkPermissions(final String[] permissions, PermissionBaseActivity.OnPermissionResultListener onPermissionResultListener) {
        Activity activity = getActivity();
        if (activity instanceof PermissionBaseActivity) {
            ((PermissionBaseActivity) activity).checkPermissions(permissions, onPermissionResultListener);
        } else {
            throw new ClassCastException("Want to use checkPermissions, The fragment's Activity must extends PermissionBaseActivity");
        }
    }
}
