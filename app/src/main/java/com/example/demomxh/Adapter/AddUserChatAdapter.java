package com.example.demomxh.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class AddUserChatAdapter extends RecyclerView.Adapter<AddUserChatAdapter.ViewHolder> {

    public interface onAddUserClickListener{
        void onClick();
    }

    ArrayList<Friend> friendsList;
    onAddUserClickListener listener;

    public AddUserChatAdapter(ArrayList<Friend> friendsList, onAddUserClickListener listener) {
        this.friendsList = friendsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_add_chat,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindView(friendsList.get(position));
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imv_item_user_add_chat_avatar)
        CircleImageView imvAvatar;
        @BindView(R.id.tv_item_user_add_chat_username)
        TextView tvUsername;
        @BindView(R.id.imv_item_user_add_chat_add)
        ImageView imvAdd;
        @BindView(R.id.imv_item_user_add_chat_check)
        ImageView imvCheck;

        private Friend friend;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(view -> {
                friend.setSelected(!friend.isSelected());
                notifyDataSetChanged();
                listener.onClick();
            });
        }
        private void bindView(Friend friend){
            this.friend = friend;
            Glide.with(itemView.getContext()).load(friend.getAvatarUrl()).into(imvAvatar);
            tvUsername.setText(friend.getUsername());
            if(friend.isSelected()){
                imvCheck.setVisibility(View.VISIBLE);
                imvAdd.setVisibility(View.GONE);
            }
            if (!friend.isSelected()){
                imvCheck.setVisibility(View.GONE);
                imvAdd.setVisibility(View.VISIBLE);
            }
        }
    }
}
