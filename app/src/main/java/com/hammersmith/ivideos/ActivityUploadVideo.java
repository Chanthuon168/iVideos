package com.hammersmith.ivideos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hammersmith.ivideos.model.Video;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityUploadVideo extends AppCompatActivity implements View.OnClickListener {
    private Video video;
    private EditText cate, key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);
        findViewById(R.id.save).setOnClickListener(this);
        cate = (EditText) findViewById(R.id.category);
        key = (EditText) findViewById(R.id.code);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                String id = cate.getText().toString();
                String code = key.getText().toString();
                if (!id.isEmpty() && !code.isEmpty()) {
                    int catId = Integer.parseInt(id);
                    saveVideo(catId, code);
                } else {
                    Toast.makeText(getApplicationContext(),"Invalid",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void saveVideo(int cate, String key) {
        video = new Video(cate, key);
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<Video> videoCall = service.postVideo(video);
        videoCall.enqueue(new Callback<Video>() {
            @Override
            public void onResponse(Call<Video> call, Response<Video> response) {
                video = response.body();
                if (video != null) {
                    Toast.makeText(getApplicationContext(),video.getMsg(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Video> call, Throwable t) {

            }
        });

    }
}
