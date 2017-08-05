package com.sundaymorning.coincharge;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class NAGcm {

    private final int TIME_OUT = 20000;
    private final byte[] ivBytes = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    private final String CYKEY = "1db1578f99d8ecb40dd9912774bd3332";
    private final String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'()";
    private final String URL_REGISTER = "";
    //http://push.nextapps.com/register.asp?
    //http://herogenie.kr/api/gcm/insert.jsp?
    private final String P = "p";
    private final String T = "t";
    private final String D = "d";
    private final String OS = "os";

    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_ONOFF = "onoff"; // true면 on // false면 off
    private static final String FileName = "hggcm";

    private GoogleCloudMessaging gcm = null;
    private String regid = "";
    private String senderid = "";

    private Context context = null;

    private volatile static NAGcm singleton = null;

    public static void registerGCM(Context context, String senderid) {
        getInstance().connectGCM(context, senderid);
    }

    public static String getToken(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (TextUtils.isEmpty(registrationId)) {
            //Registration not found.//
            return "";
        }

        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, 0);

        int currentVersion = 0;

        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            currentVersion = packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen //
            currentVersion = -1;
        }

        if (registeredVersion != currentVersion) {
            //App version changed.//
            return "";
        }

        return registrationId;
    }

    public static boolean isEnable(Context context) {
        boolean res = true;
        final SharedPreferences prefs = context.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        res = prefs.getBoolean(PROPERTY_ONOFF, true);
        return res;
    }

    public static void setEnable(Context context, boolean enable) {
        final SharedPreferences prefs = context.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PROPERTY_ONOFF, enable);
        editor.commit();
    }

    private static NAGcm getInstance() {
        if (null == singleton) {
            synchronized (NAGcm.class) {
                if (null == singleton)
                    singleton = new NAGcm();
            }
        }
        return singleton;
    }

    private static void releaseInstance() {
        if (singleton != null) {
            synchronized (NAGcm.class) {
                if (singleton != null)
                    singleton = null;
            }
        }
    }

    private void connectGCM(Context context, String senderid) {
        this.context = context;
        this.senderid = senderid;
        if (this.context != null && !TextUtils.isEmpty(this.senderid)) {
            if (checkPlayServices()) {
                gcm = GoogleCloudMessaging.getInstance(this.context);
                regid = getRegistrationId();
                if (TextUtils.isEmpty(regid)) {
                    registerInBackground();
                } else {
                    sendRegistrationId();
                }
            } else {
                //No valid Google Play Services APK found.
                releaseInstance();
            }
        } else {
            releaseInstance();
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            return false;
        }
        return true;
    }

    private String getRegistrationId() {

        final SharedPreferences prefs = getGCMPreferences();
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (TextUtils.isEmpty(registrationId)) {
            //Registration not found.//
            return "";
        }

        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, 0);
        int currentVersion = getAppVersion();
        if (registeredVersion != currentVersion) {
            //App version changed.//
            return "";
        }

        return registrationId;
    }

    private SharedPreferences getGCMPreferences() {
        return context.getSharedPreferences(FileName, Context.MODE_PRIVATE);
    }

    private int getAppVersion() {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen //
            return -1;
        }
    }

    private void registerInBackground() {
        new RegisterGCM().execute();
    }

    private class RegisterGCM extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                regid = gcm.register(senderid);
                sendRegistrationIdToBackend();
                storeRegistrationId(regid);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            releaseInstance();
            return null;
        }
    }

    private void sendRegistrationId() {
        if (context != null && !TextUtils.isEmpty(regid)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    sendDeviceId(context, regid);
                    releaseInstance();
                }
            }).start();
        } else {
            releaseInstance();
        }
    }

    private void sendRegistrationIdToBackend() {
        if (context != null && !TextUtils.isEmpty(regid)) {
            sendDeviceId(context, regid);
        }
    }

    private void storeRegistrationId(String regId) {
        final SharedPreferences prefs = getGCMPreferences();
        int appVersion = getAppVersion();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    private void execute(Context context, String packageName, String registerID, String deviceID) {
        if (context != null && !TextUtils.isEmpty(registerID) && !TextUtils.isEmpty(deviceID))
            httpPost(URL_REGISTER, P, packageName, T, registerID, D, deviceID, OS, "2");
    }

    private void sendDeviceId(final Context context, final String registerID) {
        if (context != null) {
            String deviceID = "";
            try {
                deviceID = getDeviceID(context);
            } catch (Exception e) {
                deviceID = "";
            }
            execute(context, context.getPackageName(), registerID, encryptAES(deviceID));
        }
    }

    private String httpPost(String URL, String... param) {

        String res = "";

        try {

            String body = makePostBody(param);

            URL url = new URL(URL);

            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setConnectTimeout(TIME_OUT);
            urlConn.setReadTimeout(TIME_OUT);
            urlConn.setAllowUserInteraction(false);
            urlConn.setRequestMethod("POST");
            urlConn.setDoOutput(true);

            OutputStream os = null;
            if (body != null && 0 < body.length()) {
                os = urlConn.getOutputStream();
                os.write(body.getBytes("UTF-8"));
            }

            urlConn.connect();

            BufferedInputStream bis = new BufferedInputStream(urlConn.getInputStream());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[512];

            while (true) {
                int readlen = bis.read(buf);
                if (readlen < 1)
                    break;
                bos.write(buf, 0, readlen);
            }

            res = new String(bos.toByteArray(), "UTF-8");

            bos.flush();
            bos.close();

            bis.close();

            if (os != null) {
                os.flush();
                os.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            res = "";
        }

        return res;

    }

    private String makePostBody(String... param) {
        String res = "";
        if (2 <= param.length) {
            String param1 = "";
            String param2 = "";
            try {
                for (int i = 0; i < param.length / 2; i++) {

                    param1 = URLEncoder.encode(param[i * 2], "UTF-8");
                    param2 = URLEncoder.encode(param[i * 2 + 1], "UTF-8");

                    if (i == 0) {
                        res = param1 + "=" + param2;
                    } else {
                        res = res + "&" + param1 + "=" + param2;
                    }

                }
            } catch (Exception e) {
                res = "";
            }
        }
        return res;
    }

    private String getDeviceID(Context context) throws Exception {
        String res = "";

        TelephonyManager telephone = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);
        res = telephone.getDeviceId();

        if (null == res) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Service.WIFI_SERVICE);
            if (null != wifiManager) {
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                if (null != wifiInfo) {
                    res = wifiInfo.getMacAddress();
                    if (res != null)
                        res = res.toUpperCase(Locale.getDefault());
                }
            }
        }

        if (null == res) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
                res = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            } else {
                res = "";
            }
        }

        if (null == res) {
            res = "";
        }

        return res;
    }

    private String encryptAES(String str) {
        String res = "";

        try {
            res = AES_Encode(str);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return encodeURIComponent(res);
    }

    private String AES_Encode(String str)
            throws UnsupportedEncodingException,
            NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {

        byte[] keyData = CYKEY.getBytes();

        SecretKey secureKey = new SecretKeySpec(keyData, "AES");

        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(ivBytes));

        byte[] encrypted = c.doFinal(str.getBytes());

        String enStr = new String(Base64.encode(encrypted, Base64.NO_WRAP));

        return enStr;
    }

    private String encodeURIComponent(String input) {
        if (input.equals("")) {
            return input;
        }

        int l = input.length();
        StringBuilder o = new StringBuilder(l * 3);
        try {
            for (int i = 0; i < l; i++) {
                String e = input.substring(i, i + 1);
                if (ALLOWED_CHARS.indexOf(e) == -1) {
                    byte[] b = e.getBytes("utf-8");
                    o.append(getHex(b));
                    continue;
                }
                o.append(e);
            }
            return o.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return input;
    }

    private String getHex(byte buf[]) {
        StringBuilder o = new StringBuilder(buf.length * 3);
        for (int i = 0; i < buf.length; i++) {
            int n = (int) buf[i] & 0xff;
            o.append("%");
            if (n < 0x10) {
                o.append("0");
            }
            o.append(Long.toString(n, 16).toUpperCase(Locale.getDefault()));
        }
        return o.toString();
    }

}
