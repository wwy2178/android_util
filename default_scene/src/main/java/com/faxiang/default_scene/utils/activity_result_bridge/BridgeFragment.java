package com.faxiang.default_scene.utils.activity_result_bridge;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

public class BridgeFragment extends Fragment {

    private Map<Integer, CallbackResult> mCallbackMap = new HashMap<>();   //结果集合

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CallbackResult callback = mCallbackMap.remove(requestCode);
        if (null != callback) {
            callback.onActivityResult(requestCode, resultCode, data);
        }
    }

    //进行回调跳转
    public void startResult(Intent intent, int requestCode, CallbackResult callback) {
        mCallbackMap.put(requestCode, callback);
        try {
            startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}