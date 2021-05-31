package com.example.demomxh.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.demomxh.Database.RealmContext;
import com.example.demomxh.Model.Post;
import com.example.demomxh.Model.UserInfo;
import com.example.demomxh.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    ArrayList<Post> posts;
    UserInfo userInfo;
    onPostClickListener postClickListener;

    public PostAdapter(ArrayList<Post> posts, onPostClickListener postClickListener) {
        this.posts = posts;
        this.postClickListener = postClickListener;
        userInfo = RealmContext.getInstance().getUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.BindView(posts.get(position),position);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.civ_item_post_avatar)
        CircleImageView civAvatar;
        @BindView(R.id.tv_post_author)
        TextView tvAuthor;
        @BindView(R.id.tv_post_created_date)
        TextView tvCreatedDate;
        @BindView(R.id.tv_post_content)
        TextView tvContent;
        @BindView(R.id.imv_post_image)
        ImageView imvImage;
        @BindView(R.id.tv_item_post_like_number)
        TextView tvNumberLike;
        @BindView(R.id.tv_item_post_comment_number)
        TextView tvNumberComment;
        @BindView(R.id.cv_item_post_image)
        CardView cvImage;
        @BindView(R.id.imv_item_post_icon_like)
        ImageView imvIconLike;
        @BindView(R.id.imv_item_post_more_options)
        ImageView imvOptions;
        @BindView(R.id.ll_item_post_like)
        LinearLayout llLike;
        @BindView(R.id.ll_item_post_comment)
        LinearLayout llComment;

        private int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            llComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postClickListener.onCommentClick(position);
                }
            });

            civAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postClickListener.onAvatarOrNameClickListner(position);
                }
            });

            tvAuthor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postClickListener.onAvatarOrNameClickListner(position);
                }
            });

            imvIconLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postClickListener.onLikeListener(position);
                }
            });

            imvOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postClickListener.onPostMoreOptions(v,position);
                }
            });
        }
        private void BindView(Post post,int position){
            this.position=position;
            Glide.with(itemView.getContext()).load(post.getAuthorAvatar()).into(civAvatar);
            tvAuthor.setText(post.getAuthorName());
            tvCreatedDate.setText(post.getCreateDate());
            tvContent.setText(post.getContent());
            tvNumberComment.setText(String.valueOf(post.getNumberComment()));
            tvNumberLike.setText(String.valueOf(post.getNumberLike()));

            //Check if post has image
            if (post.getImages().length != 0){
                cvImage.setVisibility(View.VISIBLE);
                Glide.with(itemView.getContext()).load(post.getImages()[0]).into(imvImage);
            }else{
                cvImage.setVisibility(View.GONE);
            }

            //Check if user like post
            if (post.isLike()){
                Glide.with(itemView.getContext()).load(R.drawable.ic_like).into(imvIconLike);
            }else {
                Glide.with(itemView.getContext()).load(R.drawable.ic_dont_like).into(imvIconLike);
            }

            //Check if post is belong to user
            if (!post.getAuthor().equals(userInfo.getUsername())){
                imvOptions.setVisibility(View.GONE);
            }else {
                imvOptions.setVisibility(View.VISIBLE);
            }
        }
    }
    public interface onPostClickListener{
        void onCommentClick(int position);
        void onAvatarOrNameClickListner(int pos);
        void onLikeListener(int pos);
        void onPostMoreOptions(View view,int pos);
    }
}
