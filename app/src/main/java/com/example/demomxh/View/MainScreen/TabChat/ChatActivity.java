package com.example.demomxh.View.MainScreen.TabChat;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demomxh.Adapter.MessageAdapter;
import com.example.demomxh.Common.BaseActivity;
import com.example.demomxh.Database.RealmContext;
import com.example.demomxh.Model.Message;
import com.example.demomxh.Model.UserInfo;
import com.example.demomxh.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends BaseActivity {

    public static final String KEY_GET_GROUP_CHAT = "KEY_GET_GROUP_CHAT";
    private static final int REQUEST_CODE_SPEECH_INPUT = 18;

    @BindView(R.id.view_flipper_chat_activity)
    ViewFlipper vfChats;
    @BindView(R.id.rcv_messages_list)
    RecyclerView rvMessages;
    @BindView(R.id.imv_send_chat)
    ImageView imvSend;
    @BindView(R.id.edt_send_chat)
    EditText edtSend;
    @BindView(R.id.imv_mic_chat)
    ImageView imvMic;

    private ArrayList<Message> messages;
    private MessageAdapter messageAdapter;
    private String groupId;
    private UserInfo userInfo;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("https://hubbook.herokuapp.com/");
        } catch (URISyntaxException e) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Init();
        RegisterEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off("new_message", onNewMessageListener);
        mSocket.close();
    }


    private void RegisterEvent() {
        imvSend.setOnClickListener(view -> {
            String content = edtSend.getText().toString().trim();
            if(TextUtils.isEmpty(content)){
                ShowToast("Vui lòng nhập lại tin nhắn");
            }
            else {
                edtSend.setText("");
                mSocket.emit("create_messge", groupId, userInfo.getUserId(), content);
                ShowToast(mSocket.toString());
            }
        });

        imvMic.setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something...");
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        });
    }

    private void Init() {
        ButterKnife.bind(this);
        userInfo = RealmContext.getInstance().getUser();
        groupId = getIntent().getStringExtra(KEY_GET_GROUP_CHAT);

        mSocket.emit("join_chat", groupId, userInfo.getUserId());
        mSocket.on("new_message", onNewMessageListener);
        mSocket.connect();

        messages = new ArrayList<>();
        getAllMessages(groupId);
        messageAdapter = new MessageAdapter(messages);
        rvMessages.setAdapter(messageAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        layoutManager.setSmoothScrollbarEnabled(true);
        rvMessages.setLayoutManager(layoutManager);
    }

    private Emitter.Listener onNewMessageListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            ChatActivity.this.runOnUiThread(() -> {
                if (args != null) {
                    String data = args[0].toString();
                    Message message = new Gson().fromJson(data, Message.class);
                    messages.add(message);
                    messageAdapter.notifyDataSetChanged();
                    if (!messages.isEmpty()) {
                        rvMessages.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                    }
                }
            });
        }
    };

    private void getAllMessages(String groupId){
        retrofitService.getAllMessages(groupId).enqueue(new Callback<ArrayList<Message>>() {
            @Override
            public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
                ArrayList<Message> responseList = response.body();
                if(response.code()==200 && responseList!=null){
                    messages.clear();
                    messages.addAll(responseList);
                    messageAdapter.notifyDataSetChanged();
                    vfChats.setDisplayedChild(3);
                }
                else {
                    vfChats.setDisplayedChild(1);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Message>> call, Throwable t) {
                vfChats.setDisplayedChild(2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                if (result!=null){
                    String currentSpeech = ""+result.get(0);
                    edtSend.setText(currentSpeech);
                }
            }
        }
    }
}