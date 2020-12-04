# 通用核心代码库 -- code

简要说明：

- 整理封装这个库的目的，`不是为了解决特定的某一类问题`，而是集成我们平时App开发中，大部分项目都要遇到的工具、组件，让新开发的app只要一依赖这个库，底层的框架就基本搭建完毕。 免去每个新建的app重复搭建框架的时间。
- **但是注意，集成的只是基础架构、工具，绝对不要把业务逻辑相关的代码写进来。**



下面我会一一介绍每个组件。




## 一. AppGlobal
一个App，经常需要设置并使用很多全局要用的对象，例如ApplicationContext对象、服务器API接口的BaseUrl、app主题颜色、全局的Handler等，AppGlobal就是为了方便设置和获取这些对象而设计的。



AppGlobal组件的设计有几个要点：

- 统一位置配置

  - 如果能在同一个位置，对这些配置进行设置的话，后期如果需要维护修改，直接进到这个位置快速查找修改即可。

- 方便

  - 方便在项目中任何代码位置获取配置对象

  - 采用可以链式调用设置的设计

- 不能什么都往里塞

  - 由于存放的对象是放在一个静态Map里面的，它们和App的生命周期一致，会一直占用着内存，所有不适合放太多东西，更不适合存放大对象。



使用代码例子：

- 设置通用对象
```java
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppGlobal.init(this)
                .withApiHost(Config.BASE_URL)// 后台接口BaseUrl
                .withThemeColor(getResources().getColor(R.color.themeColor))// 主题颜色
                .configure();
    }
}
```

- 获取配置的对象：
``` java
AppGlobal.getApplicationContext();
AppGlobal.getHandler();
AppGlobal.getConfiguration(ConfigType.API_HOST);
```



组件内部采用了单例模式的`Configurator`类来管理、存放这些对象，而`AppGlobal`类充当了外观类的角色，不过它只提供了获取`最基本最常用的`数据的方法，例如：获取Application的context对象、获取全局共用的一个handler对象。 其他不常用的数据，就调用AppGlobal.getConfiguration(ConfigType configType)去获取就行了。



## 二. 网络框架二次封装

网络框架的采用可谓是一个app最重要的一个环节。本库采用对Retrofit2进行二次封装的方式，加上builder模式，让使用更方便，代码更清晰，建议是在已熟悉Retrofit2的使用的基础上再使用。

下面是一个post请求的示例：

```java
RestClient.builder()
        .loading(getContext())// 请求前开启loadingDialog，请求结束自动关闭
        .addInterceptor(new MQTTAuthorInterceptor())// 添加拦截器
        .exitPageAutoCancel(this)// 退出界面自动关闭请求
        .url(url)// 设置请求url
        .header("connection", "close")// 设置请求头
        .param("param1", param1)// 设置请求参数
        .param("param2", param2)// 设置请求参数
        .callback(new ResultDataCallBack<Boolean>(Boolean.class) {
            @Override
            public void onSuccess(Boolean data) {
                controlSuccess(temp, mode, windSpeed);
            }

            @Override
            public void onFail(String code, String err) {
                showShort(err);
            }
        })
        .build()
        .post();// 进行post请求
```

## 三. andrioid6.0动态权限获取封装
待说明

## 四. 7.0 FileProvider
待说明


## 五. 监听Wifi状态的NetBroadcastReceiver
待说明

## 六. BaseActivity、BaseFragment
- MVP
todo
- LifeCycle
todo
- fragmentation
待说明

## 七.  其他常用工具类
待说明

- EventData
待说明

## 八. other

