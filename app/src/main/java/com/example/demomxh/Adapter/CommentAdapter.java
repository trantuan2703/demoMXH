package com.example.demomxh.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.demomxh.Model.Comment;
import com.example.demomxh.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    ArrayList<Comment> comments;
    onCommentClickListener clickListener;

    public CommentAdapter(ArrayList<Comment> comments, onCommentClickListener clickListener) {
        this.comments = comments;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindView(comments.get(position),position);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.civ_item_comment_avatar)
        CircleImageView civAvatar;
        @BindView(R.id.tv_item_comment_full_name)
        TextView tvFullName;
        @BindView(R.id.tv_item_comment_content)
        TextView tvContent;
        private int pos;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            tvFullName.setOnClickListener(v -> clickListener.onAvatarOrNameClickListener(pos));

            civAvatar.setOnClickListener(v -> clickListener.onAvatarOrNameClickListener(pos));
        }
        public void bindView(Comment comment,int pos){
            this.pos = pos;
            Glide.with(itemView.getContext()).load(comment.getUserAvatar()).into(civAvatar);
            tvFullName.setText(comment.getFullName());
            tvContent.setText(comment.getContent());
        }
    }

    public interface onCommentClickListener{
        void onAvatarOrNameClickListener(int pos);
    }
}
