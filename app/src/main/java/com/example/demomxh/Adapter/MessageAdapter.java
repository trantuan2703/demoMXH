package com.example.demomxh.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.demomxh.Database.RealmContext;
import com.example.demomxh.Model.Message;
import com.example.demomxh.Model.UserInfo;
import com.example.demomxh.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_MY_MESSAGE = 0;
    private static final int TYPE_FRIEND_MESSAGE = 1;

    private ArrayList<Message> messageList;
    private UserInfo userInfo;

    public MessageAdapter(ArrayList<Message> messageList) {
        this.messageList = messageList;
        userInfo = RealmContext.getInstance().getUser();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_MY_MESSAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_message, parent, false);
            return new ViewHolderMyMessage(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_message, parent, false);
            return new ViewHolderFriendMessage(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderMyMessage) {
            ((ViewHolderMyMessage) holder).bindDataMyMessage(messageList.get(position));
        } else if (holder instanceof ViewHolderFriendMessage) {
            ((ViewHolderFriendMessage) holder).bindDataFriendMessage(messageList.get(position));
        }
    }


    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        if (message.getUserId().equals(userInfo.getUserId())) {
            return TYPE_MY_MESSAGE;
        } else {
            return TYPE_FRIEND_MESSAGE;
        }
    }

    class ViewHolderMyMessage extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_item_my_message_content)
        TextView txtMessage;

        public ViewHolderMyMessage(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bindDataMyMessage(Message message) {
            txtMessage.setText(message.getContent());
        }
    }

    class ViewHolderFriendMessage extends RecyclerView.ViewHolder {

        @BindView(R.id.imv_item_friend_message_avatar)
        CircleImageView imvAvatar;
        @BindView(R.id.tv_item_friend_message_message)
        TextView txtMessage;

        public ViewHolderFriendMessage(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bindDataFriendMessage(Message message) {
            Glide.with(itemView.getContext()).load(message.getAvatar()).into(imvAvatar);
            txtMessage.setText(message.getContent());
        }
    }
}
