package com.example.demomxh.View.MainScreen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.demomxh.Adapter.PostAdapter;
import com.example.demomxh.Common.BaseActivity;
import com.example.demomxh.Database.RealmContext;
import com.example.demomxh.Model.Post;
import com.example.demomxh.Model.Profile;
import com.example.demomxh.Model.SendformAvatarChange;
import com.example.demomxh.Model.SendformEdit;
import com.example.demomxh.Model.SendformLike;
import com.example.demomxh.Model.SendformWallpaperChange;
import com.example.demomxh.Model.UserInfo;
import com.example.demomxh.Network.API;
import com.example.demomxh.R;
import com.example.demomxh.View.MainScreen.TabNewFeeds.CommentActivity;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity implements PostAdapter.onPostClickListener{

    public static final String KEY_GET_PROFILE = "KEY_GET_PROFILE";
    public static final int profileRCFS = 273;
    public static final int REQUEST_CODE_CHANGE_AVATAR =1;
    public static final int REQUEST_CODE_CHANGE_WALLPAPER =2;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_profile_full_name)
    TextView tvName;
    @BindView(R.id.civ_profile_avatar)
    CircleImageView civAvatar;
    @BindView(R.id.imv_wallpaper)
    ImageView imvWallpaper;
    @BindView(R.id.tv_profile_location)
    TextView tvLocation;
    @BindView(R.id.tv_phone_number)
    TextView tvPhoneNumber;
    @BindView(R.id.tv_profile_birthday)
    TextView tvBirthday;
    @BindView(R.id.ll_profile_edit)
    LinearLayout llEdit;
    @BindView(R.id.rv_profile_posts)
    RecyclerView rvPosts;
    @BindView(R.id.tv_edit_profile)
    TextView tvEditProfile;
    @BindView(R.id.imv_change_avatar)
    CircleImageView civAvatarChange;
    @BindView(R.id.imv_change_wallpaper)
    CircleImageView civWallpaperChange;

    private String username;
    private UserInfo userInfo;
    private Profile profile;
    private ArrayList<Post> profilePosts;
    private PostAdapter profilePostAdapter;
    private Post currentPost;
    private StorageReference storageReference;
    private String[] permissionsUploadList={Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        Init();
        RegisterEvent();
    }

    private void RegisterEvent() {
        tvEditProfile.setOnClickListener(v -> editProfile());

        tvPhoneNumber.setOnClickListener(v-> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", profile.getPhone(), null));
            startActivity(intent);
        });

        tvLocation.setOnClickListener(v-> {
            if (profile != null) {
                String map = "http://maps.google.co.in/maps?q=" + profile.getAddress();
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                startActivity(i);
            }
        });

       civAvatarChange.setOnClickListener(v -> {
           if (checkPermission(ProfileActivity.this,permissionsUploadList)){
               openGallery(REQUEST_CODE_CHANGE_AVATAR);
           }else {
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                   requestPermissions(permissionsUploadList,REQUEST_CODE_CHANGE_AVATAR);
               }
           }
       });

        civWallpaperChange.setOnClickListener(v -> {
            if (checkPermission(ProfileActivity.this,permissionsUploadList)){
                openGallery(REQUEST_CODE_CHANGE_WALLPAPER);
            }else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(permissionsUploadList,REQUEST_CODE_CHANGE_WALLPAPER);
                }
            }
        });
    }

    private void openGallery(int s) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,s);
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

    private void editProfile() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View aleart = LayoutInflater.from(this).inflate(R.layout.dialog_edit_profile,null, false);
        builder.setView(aleart);

        EditText edtFullname;
        EditText edtPhone;
        EditText edtBirthday;
        EditText edtAddress;
        Button btnEdit;
        Button btnCancel;

        edtFullname = aleart.findViewById(R.id.edt_dialog_full_name);
        edtBirthday = aleart.findViewById(R.id.edt_dialog_birthday);
        edtAddress = aleart.findViewById(R.id.edt_dialog_address);
        edtPhone = aleart.findViewById(R.id.edt_dialog_number);
        btnEdit = aleart.findViewById(R.id.btn_dialog_save);
        btnCancel = aleart.findViewById(R.id.btn_dialog_cancel);

        edtFullname.setText(profile.getFullName());
        edtBirthday.setText(profile.getBirthday());
        edtAddress.setText(profile.getAddress());
        edtPhone.setText(profile.getPhone());
        AlertDialog dialog = builder.show();

        btnCancel.setOnClickListener(v-> {
            Toast.makeText(ProfileActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        btnEdit.setOnClickListener(v -> {
            String name = edtFullname.getText().toString().trim();
            String birthday = edtBirthday.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            SendformEdit sendformEdit = new SendformEdit(name,address,birthday,phone);
            retrofitService.editProfile(userInfo.getUserId(),sendformEdit).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code()==200){
                        ShowToast("Successfully");
                        tvName.setText(name);
                        tvBirthday.setText(birthday);
                        tvLocation.setText(address);
                        tvPhoneNumber.setText(phone);
                        dialog.dismiss();

                    }else {
                        ShowToast("Fail");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    ShowToast("Fail");
                }
            });
        });
    }

    private void Init() {
        username = getIntent().getStringExtra(KEY_GET_PROFILE);
        userInfo = RealmContext.getInstance().getUser();
        profile = new Profile();
        profilePosts = new ArrayList<>();
        getProfileDetail();
        profilePostAdapter = new PostAdapter(profilePosts,this);
        rvPosts.setAdapter(profilePostAdapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    private void getProfileDetail() {
        retrofitService.getProfileDetail(username,userInfo.getUserId()).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                Profile responseProfile = response.body();
                if (response.code()==200 && responseProfile!=null){
                    profile = responseProfile;
                    fetchData();
                }else {
                    ShowToast("Fail");
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                ShowToast("Fail");
            }
        });
    }

    private void fetchData() {
        tvName.setText(profile.getFullName());
        Glide.with(this).load(profile.getAvatarUrl()).into(civAvatar);
        Glide.with(this).load(profile.getCoverPhoto().get(0)).into(imvWallpaper);
        tvLocation.setText(profile.getAddress());
        tvPhoneNumber.setText(profile.getPhone());
        tvBirthday.setText(profile.getBirthday());
        if (profile.getUsername().equals(userInfo.getUsername())){
            llEdit.setVisibility(View.VISIBLE);
            civAvatarChange.setVisibility(View.VISIBLE);
            civWallpaperChange.setVisibility(View.VISIBLE);
        }else{
            llEdit.setVisibility(View.GONE);
            civAvatarChange.setVisibility(View.GONE);
            civWallpaperChange.setVisibility(View.GONE);
        }
        profilePosts.clear();
        profilePosts.addAll(profile.getPostList());
        profilePostAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCommentClick(int position) {
        currentPost = profilePosts.get(position);
        Intent intent = new Intent(ProfileActivity.this, CommentActivity.class);
        intent.putExtra(CommentActivity.KEY_GET_POST_IN_COMMENT,currentPost);
        startActivityForResult(intent,profileRCFS);
    }

    @Override
    public void onAvatarOrNameClickListner(int pos) {

    }

    @Override
    public void onLikeListener(int pos) {
        Post currentPost = profilePosts.get(pos);
        HandleLike(currentPost);
        LikeStatus(currentPost);
    }

    private void LikeStatus(Post currentPost) {
        SendformLike sendformLike = new SendformLike(userInfo.getUserId(),currentPost.getPostId());
        retrofitService.likeStatus(sendformLike).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code()==200){
                    ShowToast("Sucessfully");
                }else {
                    HandleLike(currentPost);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                HandleLike(currentPost);
            }
        });
    }

    private void HandleLike(Post currentPost) {
        currentPost.setLike(!currentPost.isLike());
        if(currentPost.isLike()){
            currentPost.setNumberLike(currentPost.getNumberLike()+1);
        }else {
            currentPost.setNumberLike(currentPost.getNumberLike()-1);
        }
        profilePostAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPostMoreOptions(View view, int pos) {
        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.inflate(R.menu.menu_option_status);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.menu_item_delete:
                    String url = API.DELETE_EDIT_POST+"/"+profilePosts.get(pos).getPostId();
                    retrofitService.deletePost(userInfo.getUserId(),url).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code()==200){
                                ShowToast("Sucessfully");
                                profilePosts.remove(pos);//<-important line
                                profilePostAdapter.notifyDataSetChanged();
                            }else {
                                ShowToast("Fail");
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            ShowToast("Fail");
                        }
                    });
                    break;
                case R.id.menu_item_cancel:
                    ShowToast("Cancel");
                    break;
                default:
                    break;
            }
            return false;
        });
        popupMenu.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProfileDetail();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == profileRCFS && resultCode == Activity.RESULT_OK && data!=null){
            int numberComment = data.getIntExtra("number_comment",0);
            currentPost.setNumberComment(numberComment);
            profilePostAdapter.notifyDataSetChanged();
        }

        if (requestCode == REQUEST_CODE_CHANGE_AVATAR){
            UploadImageToFirebase(data.getData(),civAvatar);
        }

        if (requestCode == REQUEST_CODE_CHANGE_WALLPAPER){
            UploadImageToFirebase(data.getData(),imvWallpaper);
        }
    }

    private void UploadImageToFirebase(Uri uri,ImageView imageView) {
        final StorageReference ref = storageReference.child("avatar-wallpaper/"+uri.getLastPathSegment());
        UploadTask uploadTask = ref.putFile(uri);
        uploadTask.continueWithTask(task -> ref.getDownloadUrl()).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                if (imageView == civAvatar){
                    changeAvatar(task.getResult().toString(),civAvatar);//firebaseUrl = task.getResult().toString();
                }
                if (imageView == imvWallpaper){
                    changeWallpaper(task.getResult().toString(),imvWallpaper);
                }
            }else{
                ShowToast("Fail");
            }
        });
    }

    private void changeWallpaper(String firebaseUrl, ImageView imvWallpaper) {
        SendformWallpaperChange sendformWallpaperChange= new SendformWallpaperChange();
        sendformWallpaperChange.setCoverPhoto(new String[]{firebaseUrl});
        retrofitService.changeWallpaper(userInfo.getUserId(),sendformWallpaperChange).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code()==200){
                    ShowToast("Successful");
                }else {
                    ShowToast("Fail"+response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                ShowToast("Fail");
            }
        });
        Glide.with(this).load(firebaseUrl).into(imvWallpaper);
    }

    private void changeAvatar(String firebaseUrl, CircleImageView civAvatar) {
        SendformAvatarChange avatarChangeSendForm = new SendformAvatarChange(firebaseUrl);
        retrofitService.changeAvatar(userInfo.getUserId(),avatarChangeSendForm).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code()==200){
                    ShowToast("Successful");
                }else {
                    ShowToast("Fail");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                ShowToast("Fail");
            }
        });
        Glide.with(this).load(firebaseUrl).into(civAvatar);
        userInfo.setAvatarUrl(firebaseUrl);
    }
}