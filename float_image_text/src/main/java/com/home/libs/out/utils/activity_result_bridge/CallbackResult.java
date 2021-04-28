package com.home.libs.out.utils.activity_result_bridge;

import android.content.Intent;

public interface CallbackResult {
    void onActivityResult(int requestCode, int resultCode, Intent data);
}