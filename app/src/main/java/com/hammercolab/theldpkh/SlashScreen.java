package com.hammercolab.theldpkh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.hammercolab.theldpkh.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SlashScreen extends AppCompatActivity {
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_slash_screen);

        Random random = new Random(System.nanoTime() % 100000);
        int randomInt = random.nextInt(1000000000);
        final String session = String.valueOf(randomInt);

        if (PrefUtils.getCurrentUser(SlashScreen.this) == null) {
            user = new User(session);
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<User> userCall = service.generateSession(user);
            userCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    user = response.body();
                    if (user.getMsg().equals("success")) {
                        User mUser = new User();
                        mUser.setSession(session);
                        PrefUtils.setCurrentUser(mUser, SlashScreen.this);
                        int secondsDelayed = 1;
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                startActivity(new Intent(SlashScreen.this, MainActivity.class));
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                finish();
                            }
                        }, secondsDelayed * 1000);
                    } else {
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    finish();
                }
            });
        } else {
            int secondsDelayed = 1;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    startActivity(new Intent(SlashScreen.this, MainActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
            }, secondsDelayed * 1500);
        }

    }

}
