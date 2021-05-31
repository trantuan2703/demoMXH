package com.example.demomxh.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.demomxh.Database.RealmContext;
import com.example.demomxh.Model.GroupChat;
import com.example.demomxh.Model.UserInfo;
import com.example.demomxh.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class GroupsChatAdapter extends RecyclerView.Adapter<GroupsChatAdapter.ViewHolder> {

    private ArrayList<GroupChat> groupChats;
    private UserInfo userInfo;
    private onGroupChatClick listener;

    public interface onGroupChatClick{
        void onGroupChatClick(int position);
    }

    public GroupsChatAdapter(ArrayList<GroupChat> groupChats, onGroupChatClick listener) {
        this.groupChats = groupChats;
        userInfo = RealmContext.getInstance().getUser();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_chat,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindView(groupChats.get(position),position);
    }

    @Override
    public int getItemCount() {
        return groupChats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_last_message)
        TextView tvLastMessage;
        @BindView(R.id.tv_item_group_chat_name)
        TextView tvGroupChatName;
        @BindView(R.id.vf_item_group_chat_avatar)
        ViewFlipper viewFlipper;
        @BindView(R.id.imv_item_group_chat_avatar)
        CircleImageView imvAvatar;
        @BindView(R.id.imv_item_group_chat_avatar_1)
        CircleImageView imvAvatar1;
        @BindView(R.id.imv_Item_group_chat_avatar_2)
        CircleImageView imvAvatar2;
        private int position;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onGroupChatClick(position);
                }
            });
        }
        private void bindView(GroupChat groupChat, int position) {
            this.position=position;
            tvLastMessage.setText(groupChat.getLastMessage());
            ArrayList<UserInfo> users = groupChat.getUsers();

            String groupName = "";
            for (UserInfo user : users){
                if(!user.getUserId().equals(userInfo.getUserId())){
                    groupName+=user.getFullName()+",";
                }
            }
            tvGroupChatName.setText(groupName);

            if (users.size() > 2) {
                viewFlipper.setDisplayedChild(1);
                boolean loadImv1Done = false;
                for (UserInfo user : users) {
                    if (!user.getUserId().equals(userInfo.getUserId())) {
                        if (!loadImv1Done) {
                            Glide.with(itemView.getContext()).load(user.getAvatarUrl()).into(imvAvatar1);
                            loadImv1Done = true;
                        }
                        else {
                            Glide.with(itemView.getContext()).load(user.getAvatarUrl()).into(imvAvatar2);
                            break;
                        }
                        //groupName+=user.getFullName()+",";
                    }
                }
                tvGroupChatName.setText(groupName);
            } else {
                viewFlipper.setDisplayedChild(0);
                for (UserInfo user : users) {
                    if (!user.getUserId().equals(userInfo.getUserId())) {
                        Glide.with(itemView.getContext()).load(user.getAvatarUrl()).into(imvAvatar);
                        // tvGroupChatName.setText(user.getFullName());
                        break;
                    }
                }
            }
        }
    }

}
