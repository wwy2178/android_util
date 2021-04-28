package com.faxiang.default_scene.utils.activity_result_bridge;

import android.content.Intent;

public interface CallbackResult {
    void onActivityResult(int requestCode, int resultCode, Intent data);
}