package com.example.demomxh.View.MainScreen.TabNewFeeds;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import com.bumptech.glide.Glide;
import com.example.demomxh.Common.BaseActivity;
import com.example.demomxh.Model.Post;
import com.example.demomxh.Model.SendformPost;
import com.example.demomxh.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostActivity extends BaseActivity {

    @BindView(R.id.civ_create_post_avatar)
    CircleImageView civAvatar;
    @BindView(R.id.edt_create_post_content)
    EditText edtContent;
    @BindView(R.id.tv_create_post)
    TextView tvPost;
    @BindView(R.id.rl_choose_image)
    RelativeLayout rlChooseImage;
    @BindView(R.id.tv_create_post_cancel)
    TextView tvCancel;
    @BindView(R.id.imv_create_post_image)
    ImageView imvImageContent;

    private String[] permissionsUploadList={Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int RCPI = 480;
    private StorageReference storageReference;
    private String pickedImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        Init();
        RegisterEvent();
    }

    private void RegisterEvent() {
        rlChooseImage.setOnClickListener(v -> ensurePermissions());
        tvCancel.setOnClickListener(v -> onBackPressed());
        tvPost.setOnClickListener(v -> postStatus());
    }

    private void postStatus() {
        String content = edtContent.getText().toString();
        String[] images = {pickedImage};
        SendformPost sendformPost = new SendformPost(realmContext.getUser().getUserId(),content,images);
        retrofitService.postaStatus(sendformPost).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.code()==200){
                    ShowToast("Succesful");
                    onBackPressed();
                }else{
                    ShowToast("Unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }


    private void Init() {
        ButterKnife.bind(this);
        Glide.with(this).load(realmContext.getUser().getAvatarUrl()).into(civAvatar);
        edtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               if (s.length()!=0){
                   tvPost.setVisibility(View.VISIBLE);
               }else{
                   tvPost.setVisibility(View.GONE);
               }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    private void ensurePermissions() {
        if (checkPermission(this,permissionsUploadList)){
            openGallery();
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsUploadList,RCPI);
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,RCPI);
    }

    private boolean checkPermission(Context context, String[] permissions){
        if (context!=null){
            for (String permission:permissions){
                if (ActivityCompat.checkSelfPermission(context,permission)!= PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RCPI && resultCode == Activity.RESULT_OK && data!=null){
            Glide.with(this).load(data.getData()).into(imvImageContent);
            UploadImageTofirebase(data.getData());
        }
    }

    private void UploadImageTofirebase(Uri data) {
        StorageReference srf = storageReference.child("image/"+data.getLastPathSegment());
        UploadTask uploadTask = srf.putFile(data);
        uploadTask.continueWithTask(task -> srf.getDownloadUrl()).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                pickedImage = task.getResult().toString();
                Log.d("TestUploadFirebase",task.getResult().toString());
            }else{
                ShowToast("Unsucessful");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RCPI){
            if (checkPermission(CreatePostActivity.this,permissionsUploadList)){
                openGallery();
            }else {
                ShowToast("Permission has been denied!");
            }
        }
    }

}