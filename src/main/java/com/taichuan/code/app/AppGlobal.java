package com.taichuan.code.app;

import android.content.Context;
import android.os.Handler;

/**
 * @author gui
 * @date 2017/9/28
 * 全局管理类。 <br>
 * 为了方便在任何位置的代码都可以获取到一些配置信息或常用对象
 */
public class AppGlobal {
    private static Configurator getConfigurator() {
        return Configurator.getInstance();
    }

    public static Configurator init(Context context) {
        return getConfigurator().withApplicationContext(context.getApplicationContext());
    }

    public static <T> T getConfiguration(ConfigType configType) {
        return getConfigurator().getConfiguration(configType);
    }

    public static Handler getHandler() {
        return getConfiguration(ConfigType.HANDLER);
    }

    public static Context getApplicationContext() {
        return getConfiguration(ConfigType.APPLICATION_CONTEXT);
    }
}
