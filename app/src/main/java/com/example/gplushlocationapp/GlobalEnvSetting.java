package com.example.gplushlocationapp;

import java.util.Arrays;

import android.content.Context;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class GlobalEnvSetting {

    private static boolean isHms;

    public static void init(Context ctx) {

        boolean gAvailable = !Arrays.asList(ConnectionResult.SERVICE_DISABLED,
                ConnectionResult.SERVICE_MISSING,
                ConnectionResult.SERVICE_INVALID).contains(
                GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(ctx));
        boolean hAvailable = !Arrays.asList(com.huawei.hms.api.ConnectionResult.SERVICE_DISABLED,
                com.huawei.hms.api.ConnectionResult.SERVICE_MISSING,
                com.huawei.hms.api.ConnectionResult.SERVICE_INVALID).contains(
                com.huawei.hms.api.HuaweiApiAvailability.getInstance().isHuaweiMobileServicesAvailable(ctx));
        isHms = !gAvailable || hAvailable;
    }

    public static boolean isHms() {
        return isHms;
    }

    /**
     * Before calling this method, please make sure whether Google mobile services are supported in the runtime environment of mobile phone
     */
    public static void useGms() {
        isHms = false;
    }

    /**
     * Before calling this method, please make sure whether Huawei mobile services are supported in the runtime environment of mobile phone
     */
    public static void useHms() {
        isHms = true;
    }
}
