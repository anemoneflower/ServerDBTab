package com.example.tabbed_activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.security.MessageDigest;


public class TabFragment4 extends Fragment {
    private Context mContext;
    private View view;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//
//        mContext = getApplicationContext();
//
//
//
//        getHashKey(mContext);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_4, container, false);
        return view;
    }
    // 프로젝트의 해시키를 반환

//    @Nullable
//
//    public static String getHashKey(Context context) {
//
//        final String TAG = "KeyHash";
//
//        String keyHash = null;
//
//        try {
//
//            PackageInfo info =
//
//                    context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
//
//
//
//            for (Signature signature : info.signatures) {
//
//                MessageDigest md;
//
//                md = MessageDigest.getInstance("SHA");
//
//                md.update(signature.toByteArray());
//
//                keyHash = new String(Base64.encode(md.digest(), 0));
//
//                Log.d(TAG, keyHash);
//
//            }
//
//        } catch (Exception e) {
//
//            Log.e("name not found", e.toString());
//
//        }
//
//
//
//        if (keyHash != null) {
//
//            return keyHash;
//
//        } else {
//
//            return null;
//
//        }
//
//    }
}
