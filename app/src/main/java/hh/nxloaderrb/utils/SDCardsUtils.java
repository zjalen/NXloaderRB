package hh.nxloaderrb.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.text.TextUtils;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

/**
 * Created by laole918 on 2016/4/23.
 */
public class SDCardsUtils {

    private static String sdcard0 = null;
    private static String sdcard1 = null;
    private static boolean initialized = false;

    public static String getSDCard0(Context context) {
        initialize(context);
        return sdcard0;
    }

    public static String getSDCard0() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static String getSDCard1(Context context) {
        initialize(context);
        return sdcard1;
    }

    public static String getSDCard0State() {
        return Environment.getExternalStorageState();
    }

    public static String getSDCard0State(Context context) {
        return getSDCardState(context, sdcard0);
    }

    public static String getSDCard1State(Context context) {
        return getSDCardState(context, sdcard1);
    }

    @TargetApi(19)
    private static String getSDCardState(Context context, String sdcard) {
        initialize(context);
        if (!TextUtils.isEmpty(sdcard)) {
            File path = new File(sdcard);
            return Environment.getExternalStorageState(path);
        } else {
            return Environment.MEDIA_UNMOUNTED;
        }
    }

    private static void initialize(Context context) {
        if (!initialized) {
            synchronized (SDCardsUtils.class) {
                if (!initialized) {
                    String[] dirs = getExternalDirs(context);
                    if (dirs != null) {
                        int length = dirs.length;
                        if (length > 0) {
                            sdcard0 = dirs[0];
                        }
                        if (length > 1) {
                            sdcard1 = dirs[1];
                        }
                    }
                    initialized = true;
                }
            }
        }
    }

    private static String[] getExtendedMemoryPath(Context context) {
        Context mContext = context.getApplicationContext();
        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        try {
            Class<?> storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            final String[] paths = new String[length];
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                paths[i] = (String) getPath.invoke(storageVolumeElement);
            }
            return paths;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final String ENV_SECONDARY_STORAGE = "SECONDARY_STORAGE";

    public static String[] getExternalDirs(Context context) {
        Context mContext = context.getApplicationContext();
        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        try {
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Object result = getVolumeList.invoke(mStorageManager);
            assert result != null;
            final int length = Array.getLength(result);
            String[] paths = new String[length];
            File sdCard = Environment.getExternalStorageDirectory();
            paths[0] = sdCard.getAbsolutePath();
            final String rawSecondaryStorage = System.getenv(ENV_SECONDARY_STORAGE);
            for (int i = 0; i < length; i++) {
                if (!TextUtils.isEmpty(rawSecondaryStorage)) {
                    String[] externalCards = rawSecondaryStorage.split(":");
                    paths[i] = externalCards[i];
                }
            }
            return paths;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
