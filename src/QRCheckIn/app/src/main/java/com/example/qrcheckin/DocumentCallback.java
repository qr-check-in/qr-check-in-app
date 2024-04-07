package com.example.qrcheckin;

import java.util.Map;

public interface DocumentCallback {
    void onSuccess(Map<String, Object> data);
    void onFailure(Exception e);
}
