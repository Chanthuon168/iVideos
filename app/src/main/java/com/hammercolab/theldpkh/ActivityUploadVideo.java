package com.hammercolab.theldpkh;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hammercolab.theldpkh.model.News;
import com.hammercolab.theldpkh.model.Video;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.PhotoLoader;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityUploadVideo extends AppCompatActivity implements View.OnClickListener {
    private Video video;
    private EditText cate, key;
    private static final int SELECT_PHOTO = 100;
    private ArrayList<String> imageList = new ArrayList<>();
    private GalleryPhoto galleryPhoto;
    private static String photoPath;
    private ImageView logo;
    private static String encoded;
    private News news;
    private boolean isUpload = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);
        findViewById(R.id.save).setOnClickListener(this);
        logo = (ImageView) findViewById(R.id.logo);
        logo.setOnClickListener(this);
        cate = (EditText) findViewById(R.id.category);
        key = (EditText) findViewById(R.id.code);
        galleryPhoto = new GalleryPhoto(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                if (isUpload) {
                    uploadFile();
                } else {
                    String id = cate.getText().toString();
                    String code = key.getText().toString();
                    if (!id.isEmpty() && !code.isEmpty()) {
                        int catId = Integer.parseInt(id);
                        saveVideo(catId, code);
                    } else {
                        Toast.makeText(getApplicationContext(),"Invalid",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.logo:
                isUpload = true;
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                break;
        }
    }

    private void saveVideo(int cate, String key) {
        video = new Video(key, cate);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageList.clear();
        if (requestCode == SELECT_PHOTO && resultCode == this.RESULT_OK) {
            galleryPhoto.setPhotoUri(data.getData());
            photoPath = galleryPhoto.getPath();
            imageList.add(photoPath);
            Log.d("path", photoPath);
            try {
                Bitmap bitmap = PhotoLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                logo.setImageBitmap(bitmap);
                encoded = getEncoded64ImageStringFromBitmap(bitmap);

            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(), "Error while loading image", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void uploadFile() {
        news = new News(encoded);
        ApiInterface serviceUploadFile = ApiClient.getClient().create(ApiInterface.class);
        Call<News> callUploadFile = serviceUploadFile.uploadFile(news);
        callUploadFile.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                news = response.body();
                Toast.makeText(getApplicationContext(), news.getMsg(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return imgString;
    }
}
