package com.taichuan.code.mvp.view.base;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.taichuan.code.lifecycle.LifeCycle;
import com.taichuan.code.mvp.view.viewimpl.CancelAble;
import com.taichuan.code.mvp.view.viewimpl.ViewBaseInterface;
import com.taichuan.code.ui.dialog.TipDialog;
import com.taichuan.code.utils.TipDialogCreator;
import com.taichuan.code.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;

/**
 * @author gui
 * @date 2018/12/20
 */
public abstract class BaseDialog extends Dialog implements ViewBaseInterface, LifeCycle, CancelAble {
    protected final String TAG = getClass().getSimpleName();

    /*** 订阅切断者容器 */
    private CompositeDisposable compositeDisposable;
    /*** 存放Retrofit2请求的call列表，用于onDestroy的时候进行cancel */
    private List<Call> callList;
    private TipDialogCreator tipDialogCreator;

    public BaseDialog(@NonNull Context context) {
        super(context);
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void toCancel() {
        cancel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (useEventBus()) {
            EventBus.getDefault().register(this);
        }
        Object obj = setRootLayout();
        View rootView;
        if (obj instanceof View) {
            rootView = (View) obj;
        } else if (obj instanceof Integer) {
            rootView = LayoutInflater.from(getContext()).inflate((Integer) obj, null, true);
        } else {
            throw new RuntimeException("setChildContentView type err");
        }
        setContentView(rootView);
        tipDialogCreator = new TipDialogCreator(getContext(), this);

        initView();
        initListener();
        onBindView(savedInstanceState);
    }

    protected abstract Object setRootLayout();

    public abstract void initView();

    public abstract void initListener();

    /**
     * 绑定、渲染视图
     */
    protected abstract void onBindView(@Nullable Bundle savedInstanceState);

    @Override
    public void cancel() {
        if (useEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        // 切断所有订阅
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
        if (callList != null && callList.size() > 0) {
            for (Call call : callList) {
                call.cancel();
            }
        }
        super.cancel();
    }

    @Override
    public LifeCycle getLifeCycle() {
        return null;
    }

    @Override
    public void showShort(CharSequence text) {
        showToast(text, Toast.LENGTH_SHORT);
    }

    @SuppressLint("ShowToast")
    @Override
    public void showShort(int textSrc) {
        showToast(getContext().getString(textSrc), Toast.LENGTH_SHORT);
    }

    @Override
    public void showLong(String text) {
        showToast(text, Toast.LENGTH_LONG);
    }


    @Override
    public void showTipDialog(String tipMsg, final boolean isFinishWhenCancel) {
        showTipDialog(tipMsg, isFinishWhenCancel, false, null, null, null);
    }

    @Override
    public void showTipDialog(String tipMsg, TipDialog.TipClickCallBack tipClickCallBack) {
        showTipDialog(tipMsg, false, false, null, null, tipClickCallBack);
    }

    @Override
    public void showTipDialog(String tipMsg, boolean canceledOnTouchOutside, TipDialog.TipClickCallBack tipClickCallBack) {
        showTipDialog(tipMsg, false, canceledOnTouchOutside, null, null, tipClickCallBack);
    }

    @Override
    public void showTipDialog(String tipMsg, boolean canceledOnTouchOutside, String cancelString, String confirmString, TipDialog.TipClickCallBack tipClickCallBack) {
        showTipDialog(tipMsg, false, canceledOnTouchOutside, cancelString, confirmString, tipClickCallBack);
    }

    @Override
    public void showTipDialog(String tipMsg, final boolean isFinishWhenCancel, boolean canceledOnTouchOutside, String cancelString, String confirmString, TipDialog.TipClickCallBack tipClickCallBack) {
        tipDialogCreator.showTipDialog(tipMsg, isFinishWhenCancel, canceledOnTouchOutside, cancelString, confirmString, tipClickCallBack);
    }

    private void showToast(CharSequence text, int time) {
        if (isAlive()) {
            ToastUtil.show(getContext(), text, time);
        }
    }

    protected void startActivity(Class activityClass) {
        getContext().startActivity(new Intent(getContext(), activityClass));
    }

    protected boolean useEventBus() {
        return false;
    }

    @Override
    public void addDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    @Override
    public boolean isAlive() {
        return isShowing();
    }

    @Override
    public void addCall(Call call) {
        if (callList == null) {
            callList = new ArrayList<>();
        }
        callList.add(call);
    }
}
