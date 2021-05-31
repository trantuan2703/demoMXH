package com.example.demomxh.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.demomxh.Model.Friend;
import com.example.demomxh.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    private ArrayList<Friend> friends;
    private onFriendClickListener listener;

    public FriendAdapter(ArrayList<Friend> friends, onFriendClickListener listener) {
        this.friends = friends;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindView(friends.get(position),position);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.civ_item_friend_avatar)
        CircleImageView civAvatar;
        @BindView(R.id.tv_item_friend_full_name)
        TextView tvName;
        private int pos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(pos);
                }
            });
        }
        private void bindView(Friend friend, int pos){
            this.pos = pos;
            Glide.with(itemView.getContext()).load(friend.getAvatarUrl()).into(civAvatar);
            tvName.setText(friend.getFullName());
        }
    }

    public interface onFriendClickListener{
        void onClick(int pos);
    }
}
