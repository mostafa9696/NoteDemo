package com.example.mostafahussien.rx_demo.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mostafa Hussien on 04/06/2018.
 */

public class User extends BaseResponse {
    @SerializedName("api_key")
    String apiKey;

    public String getApiKey() {
        return apiKey;
    }
}
