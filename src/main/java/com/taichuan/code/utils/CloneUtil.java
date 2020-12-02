package com.taichuan.code.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author gui
 * @date 2020-02-26
 */
public class CloneUtil {
    private CloneUtil() {
        throw new AssertionError();
    }

    public static <T extends Serializable> T clone(Serializable input) throws IOException, ClassNotFoundException {
        // 说明：调用ByteArrayOutputStream或ByteArrayInputStream对象的close方法没有任何意义
        // 这两个基于内存的流只要垃圾回收器清理对象就能够释放资源，这一点不同于对外资源(如文件流)的释放
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(input);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (T) ois.readObject();
    }

    /**
     * 如果需要大量的序列话，请使用此方法，不要使用{@link #clone(Serializable)} <br>
     * （Parcelable 的性能比 Serializable 好，在内存开销方面较小） <br>
     * （因为android不同版本 Parcelable 可能不同，所以不推荐使用 Parcelable 进行数据持久化。如果需要持久化，则应该使用{@link #clone(Serializable)}）
     */
    public static <T extends Parcelable> T clone(Parcelable input) {
        Parcel parcel = null;
        try {
            parcel = Parcel.obtain();
            parcel.writeParcelable(input, 0);
            parcel.setDataPosition(0);
            return parcel.readParcelable(input.getClass().getClassLoader());
        } finally {
            if (parcel != null) {
                parcel.recycle();
            }
        }
    }
}
