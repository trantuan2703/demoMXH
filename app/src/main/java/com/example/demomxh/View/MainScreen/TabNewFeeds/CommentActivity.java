package com.example.demomxh.View.MainScreen.TabNewFeeds;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.demomxh.Adapter.CommentAdapter;
import com.example.demomxh.Common.BaseActivity;
import com.example.demomxh.Database.RealmContext;
import com.example.demomxh.Model.Comment;
import com.example.demomxh.Model.Post;
import com.example.demomxh.Model.SendFormComment;
import com.example.demomxh.Model.UserInfo;
import com.example.demomxh.R;
import com.example.demomxh.View.MainScreen.ProfileActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends BaseActivity implements CommentAdapter.onCommentClickListener {

    public static String KEY_GET_POST_IN_COMMENT ="1";

    @BindView(R.id.civ_comment_status_avatar)
    CircleImageView civAvatar;
    @BindView(R.id.tv_comment_status_fullname)
    TextView tvFullname;
    @BindView(R.id.tv_comment_status_created_date)
    TextView tvCreatedDate;
    @BindView(R.id.imv_post_more_options)
    ImageView imvOptions;
    @BindView(R.id.tv_comment_postcontent)
    TextView tvPostContent;
    @BindView(R.id.cv_comment_post_image)
    CardView cvImage;
    @BindView(R.id.imv_comment_post_image)
    ImageView imvImage;
    @BindView(R.id.rv_comments_list)
    RecyclerView rvComments;
    @BindView(R.id.imv_post_comment_send)
    ImageView imvSend;
    @BindView(R.id.edt_post_comment)
    EditText edtComment;
    @BindView(R.id.imv_comment_back)
    ImageView imvBack;

    private ArrayList<Comment> comments;
    private Post post;
    private CommentAdapter commentAdapter;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        Init();
        RegisterEvent();
    }

    private void RegisterEvent() {
        imvSend.setOnClickListener(v -> SendCommend());

       imvBack.setOnClickListener(v -> onBackPressed());
    }

    private void SendCommend() {
        String commentContent = edtComment.getText().toString();
        if (commentContent.isEmpty()){
            ShowToast("You must write the comment");
        }else{
            SendFormComment sendFormComment = new SendFormComment(userInfo.getUserId(),post.getPostId(),commentContent);
            retrofitService.postComment(sendFormComment).enqueue(new Callback<Comment>() {
                @Override
                public void onResponse(Call<Comment> call, Response<Comment> response) {
                    Comment comment = response.body();
                    if(response.code()==200){
                        AddComment(comment);
                        int newNumberComment = post.getNumberComment()+1;
                        post.setNumberComment(newNumberComment);
                        commentAdapter.notifyDataSetChanged();
                        edtComment.setText("");
                    }else {
                        ShowToast("Fail");
                    }
                }

                @Override
                public void onFailure(Call<Comment> call, Throwable t) {
                    ShowToast("Fail");
                }
            });
        }
    }

    private void AddComment(Comment comment) {
        if(comments.size()==0){
            comments.add(0,comment);
        }else {
            comments.add(comments.size(),comment);
        }
    }

    private void Init() {
        fetchData();
        getAllComments();
        commentAdapter = new CommentAdapter(comments,this);
        rvComments.setAdapter(commentAdapter);
        rvComments.setLayoutManager(new LinearLayoutManager(getBaseContext(),LinearLayoutManager.VERTICAL,false));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("number_comment",post.getNumberComment());
        setResult(Activity.RESULT_OK,intent);
        super.onBackPressed();
    }

    private void getAllComments() {
        comments = new ArrayList<>();
        retrofitService.getAllComment(post.getPostId()).enqueue(new Callback<ArrayList<Comment>>() {
            @Override
            public void onResponse(Call<ArrayList<Comment>> call, Response<ArrayList<Comment>> response) {
                ArrayList<Comment> responseList = response.body();
                if (response.code()==200 && responseList!=null){
                    comments.clear();
                    comments.addAll(responseList);
                    commentAdapter.notifyDataSetChanged();
                }else {
                    ShowToast("Can't get comments");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Comment>> call, Throwable t) {
                ShowToast("Can't get comments");
            }
        });
    }

    private void fetchData() {
        userInfo = RealmContext.getInstance().getUser();
        post = (Post) getIntent().getSerializableExtra(KEY_GET_POST_IN_COMMENT);
        Glide.with(this).load(post.getAuthorAvatar()).into(civAvatar);
        tvFullname.setText(post.getAuthorName());
        tvCreatedDate.setText(post.getCreateDate());
        if(post.getAuthor().equals(userInfo.getUsername())){
            imvOptions.setVisibility(View.VISIBLE);
        }else{
            imvOptions.setVisibility(View.GONE);
        }
        tvPostContent.setText(post.getContent());
        if (post.getImages().length != 0){
            cvImage.setVisibility(View.VISIBLE);
            Glide.with(this).load(post.getImages()[0]).into(imvImage);
        }else{
            cvImage.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAvatarOrNameClickListener(int pos) {
        String username = comments.get(pos).getUsername();
        Intent intent = new Intent(CommentActivity.this, ProfileActivity.class);
        intent.putExtra(ProfileActivity.KEY_GET_PROFILE,username);
        startActivity(intent);
    }
}