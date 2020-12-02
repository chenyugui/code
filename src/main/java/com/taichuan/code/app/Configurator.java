package com.taichuan.code.app;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.taichuan.code.page.web.event.EventManager;
import com.taichuan.code.page.web.event.WebEvent;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;


/**
 * @author gui
 * @date 2017/7/15
 * 全局配置类（单例）
 */
public class Configurator {
    private static final String TAG = "Configurator";
    /**
     * 存储Handler、ApiHost、ApplicationContext等全局通用信息。<br>
     * 特别提醒：不适合存放太多东西，最好只存放常用的、内存小的对象，不适合放大对象。
     */
    private static final Map<Enum<ConfigType>, Object> CONFIGS = new HashMap<>();
    /**
     * 全局Handler
     */
    private static final Handler HANDLER = new Handler();

    private static class Holder {
        private static final Configurator INSTANCE = new Configurator();
    }

    public static Configurator getInstance() {
        return Holder.INSTANCE;
    }

    private Configurator() {
        CONFIGS.put(ConfigType.CONFIG_READY, false);
        CONFIGS.put(ConfigType.HANDLER, HANDLER);
    }

    private void checkConfiguration() {
        final boolean isReady = (boolean) CONFIGS.get(ConfigType.CONFIG_READY);
        if (!isReady) {
            throw new RuntimeException("Configuration is not ready,call configure");
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getConfiguration(Enum<ConfigType> configType) {
        checkConfiguration();
        return (T) CONFIGS.get(configType);
    }

    public Configurator configure() {
        CONFIGS.put(ConfigType.CONFIG_READY, true);
        // 初始化logger
        Logger.addLogAdapter(new AndroidLogAdapter());
        return this;
    }

    Configurator withApplicationContext(Context context) {
        CONFIGS.put(ConfigType.APPLICATION_CONTEXT, context.getApplicationContext());
        return this;
    }

    public Configurator withApiHost(String apiHost) {
        Log.d(TAG, "apiHost: ");
        CONFIGS.put(ConfigType.API_HOST, apiHost);
        return this;
    }

    public Configurator withJavaScriptInterface(@NonNull String name) {
        CONFIGS.put(ConfigType.JAVASCRIPT_INTERFACE, name);
        return this;
    }

    public Configurator withWebEvent(@NonNull String action, @NonNull WebEvent event) {
        EventManager.getInstance().addEvent(action, event);
        return this;
    }

    public Configurator withWebHost(@NonNull String webHost) {
        CONFIGS.put(ConfigType.WEB_HOST, webHost);
        return this;
    }

    public Configurator withPublicRESTFulParams(Map<String, Object> params) {
        CONFIGS.put(ConfigType.PUBLIC_RESTFUL_PARAMS, params);
        return this;
    }

    public Configurator withDbName(String dbName) {
        CONFIGS.put(ConfigType.DB_NAME, dbName);
        return this;
    }

    /**
     * 网络请求超时毫秒数
     */
    public Configurator withNetTimeOutMilliSeconds(long timeOutSeconds) {
        CONFIGS.put(ConfigType.TIME_OUT_MILLISECONDS, timeOutSeconds);
        return this;
    }

    public Configurator withThemeColor(@ColorInt Integer color) {
        CONFIGS.put(ConfigType.THEME_COLOR, color);
        return this;
    }

    public Configurator withTipDialogLayout(@ColorInt Integer dialogLayout) {
        CONFIGS.put(ConfigType.TIP_DIALOG_LAYOUT, dialogLayout);
        return this;
    }

    public Configurator withTopBarTextColor(@ColorInt Integer color) {
        CONFIGS.put(ConfigType.TOP_BAR_COLOR, color);
        return this;
    }
}
