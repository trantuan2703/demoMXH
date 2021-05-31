package com.example.demomxh.View.MainScreen.TabNewFeeds;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demomxh.Adapter.PostAdapter;
import com.example.demomxh.Common.BaseFragment;
import com.example.demomxh.Model.Post;
import com.example.demomxh.Model.SendformLike;
import com.example.demomxh.Network.API;
import com.example.demomxh.R;
import com.example.demomxh.View.MainScreen.ProfileActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewFeedsFragment extends BaseFragment implements PostAdapter.onPostClickListener {

    public static final int RCFRS =2703;

    @BindView(R.id.vf_new_feeds)
    ViewFlipper vfNewFeeds;
    @BindView(R.id.rv_new_feeds)
    RecyclerView rvNewFeeds;
    @BindView(R.id.fab_write_post)
    FloatingActionButton fabWritePost;

    private ArrayList<Post> posts;
    private PostAdapter postAdapter;
    private Post currentPost;
    private String url;

    public NewFeedsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_feeds, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        Init();
        RegisterEvent();
    }

    private void RegisterEvent() {
        fabWritePost.setOnClickListener(v -> goToCreatePost());
    }

    private void goToCreatePost() {
        Intent intent = new Intent(getContext(),CreatePostActivity.class);
        startActivity(intent);
    }

    private void Init() {
        posts = new ArrayList<>();
        postAdapter = new PostAdapter(posts,this);
        rvNewFeeds.setAdapter(postAdapter);
        rvNewFeeds.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        GetAllPosts();
    }

    @Override
    public void onResume() {
        super.onResume();
        GetAllPosts();
    }

    private void GetAllPosts() {
        if(userInfo!=null){
            retrofitService.getAllPosts(userInfo.getUserId()).enqueue(new Callback<ArrayList<Post>>() {
                @Override
                public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
                    ArrayList<Post> responseList = response.body();
                    if (response.code()==200 && responseList!=null){
                        posts.clear();
                        posts.addAll(responseList);
                        postAdapter.notifyDataSetChanged();
                        vfNewFeeds.setDisplayedChild(3);
                        Log.d("TESTGETPOST",""+posts.size());
                    }else {
                        ShowToast("Unsucessfull");
                        vfNewFeeds.setDisplayedChild(1);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
                    vfNewFeeds.setDisplayedChild(2);
                }
            });
        }
    }

    @Override
    public void onCommentClick(int position) {
        currentPost = posts.get(position);
        Intent intent = new Intent(getContext(),CommentActivity.class);
        intent.putExtra(CommentActivity.KEY_GET_POST_IN_COMMENT,currentPost);
        startActivityForResult(intent,RCFRS);
    }

    @Override
    public void onAvatarOrNameClickListner(int pos) {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra(ProfileActivity.KEY_GET_PROFILE,posts.get(pos).getAuthor());
        startActivity(intent);
    }

    @Override
    public void onLikeListener(int pos) {
        Post currentPost = posts.get(pos);
        HandleLike(currentPost);
        LikeStatus(currentPost);
    }

    @Override
    public void onPostMoreOptions(View view, int pos) {
        PopupMenu popupMenu = new PopupMenu(getContext(),view);
        popupMenu.inflate(R.menu.menu_option_status);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.menu_item_delete:
                    String url = API.DELETE_EDIT_POST+"/"+posts.get(pos).getPostId();
                    retrofitService.deletePost(userInfo.getUserId(),url).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code()==200){
                                ShowToast("Sucessfully");
                                posts.remove(pos);//<-important line
                                postAdapter.notifyDataSetChanged();
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
        postAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RCFRS && resultCode == Activity.RESULT_OK && data!=null){
            int numberComment = data.getIntExtra("number_comment",0);
            currentPost.setNumberComment(numberComment);
            postAdapter.notifyDataSetChanged();
        }
    }
}