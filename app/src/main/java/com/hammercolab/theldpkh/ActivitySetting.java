package com.hammercolab.theldpkh;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;

import com.hammercolab.theldpkh.model.Setting;
import com.hammercolab.theldpkh.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySetting extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private Toolbar toolbar;
    private SwitchCompat switchCompat;
    private User user, mUser;
    private Setting setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_setting);
        user = PrefUtils.getCurrentUser(ActivitySetting.this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        switchCompat = (SwitchCompat) findViewById(R.id.notification);
        toolbar.setTitle("Setting");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
            }
        });
        findViewById(R.id.about).setOnClickListener(this);
        findViewById(R.id.feedback).setOnClickListener(this);
        findViewById(R.id.shareApp).setOnClickListener(this);
        findViewById(R.id.rating).setOnClickListener(this);
        switchCompat.setOnCheckedChangeListener(this);

        mUser = new User(user.getSession());
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<User> userCall = service.getNotification(mUser);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                mUser = response.body();
                if (mUser.getNotification().equals("on")) {
                    switchCompat.setChecked(true);
                } else {
                    switchCompat.setChecked(false);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about:
                startActivity(new Intent(ActivitySetting.this, AboutActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.feedback:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "thuondeveloper@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "LDP Cambodia's Feedback");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                break;
            case R.id.shareApp:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
                i.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.hammercolab.theldpkh");
                startActivity(Intent.createChooser(i, "Share with"));
                break;
            case R.id.rating:
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.notification:
                if (isChecked) {
                    onOffNotification("on");
                } else {
                    onOffNotification("off");
                }
                break;
        }
    }

    public void onOffNotification(String mStatus) {
        setting = new Setting(user.getSession(), mStatus);
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        final Call<Setting> userCall = service.offNotification(setting);
        userCall.enqueue(new Callback<Setting>() {
            @Override
            public void onResponse(Call<Setting> call, Response<Setting> response) {
                setting = response.body();
            }

            @Override
            public void onFailure(Call<Setting> call, Throwable t) {

            }
        });
    }
}
