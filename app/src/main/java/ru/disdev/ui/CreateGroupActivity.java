package ru.disdev.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ru.disdev.R;
import ru.disdev.UserSettings;
import ru.disdev.collbacks.Consumer;
import ru.disdev.network.packets.ClientPacket;
import ru.disdev.network.packets.ServerPacket;
import ru.disdev.network.packets.components.InboundPacketsKeys;
import ru.disdev.network.packets.in.CreateGroupAnswer;
import ru.disdev.network.packets.out.CreateGroupRequest;
import ru.disdev.utils.ConcurrencyUtils;
import ru.disdev.utils.ToastUtils;

public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int SUCCESS_CREATE_GROUP_RESULT_ID = 444;

    private EditText universityNameField;
    private EditText groupNameField;
    private Button makeGroupButton;

    private boolean allowPressBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        universityNameField = (EditText) findViewById(R.id.university_name_field);
        groupNameField = (EditText) findViewById(R.id.group_name_field);
        makeGroupButton = (Button) findViewById(R.id.make_group_button);
        allowPressBack = true;
    }

    @Override
    public void onBackPressed() {
        if (allowPressBack)
            super.onBackPressed();
    }

    private void hideProgressBar() {
        setContentView(R.layout.activity_create_group);
    }

    private void showProgressBar() {
        setContentView(R.layout.wait_layout);
    }

    public Consumer<ServerPacket> makePacketHandler() {
        return new Consumer<ServerPacket>() {
            @Override
            public void consume(ServerPacket packet) {
                CreateGroupAnswer answer = (CreateGroupAnswer) packet;
                boolean isSuccess = answer != null && answer.isSuccess();
                if (isSuccess) {
                    UserSettings.getUserInfo().setGroupId(answer.getGroupId());
                    UserSettings.makeElderOfGroup();
                    setResult(SUCCESS_CREATE_GROUP_RESULT_ID);
                    finish();
                } else {
                    hideProgressBar();
                    allowPressBack = true;
                    ToastUtils.showShortTimeToast(CreateGroupActivity.this, "Сервер не отвечает");
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.make_group_button) {
            final String universityName = universityNameField.getText().toString();
            final String groupName = groupNameField.getText().toString();
            boolean ok = true;
            if (universityName.isEmpty()) {
                universityNameField.setError("Название университета не может быть пустым");
                ok = false;
            }
            if (groupName.isEmpty()) {
                groupNameField.setError("Название группы не может быть пустым");
                ok = false; //todo make correct validation wtih regex
            }
            if (ok) {
                showProgressBar();
                allowPressBack = false;
                ClientPacket packet = new CreateGroupRequest(
                        UserSettings.getCurrentUserId(),
                        universityName,
                        groupName
                );
                ConcurrencyUtils.sendPacketToServerAndHandleAnswer(packet,
                        InboundPacketsKeys.CREATE_GROUP_ANSWER,
                        10, makePacketHandler());

            }
        }

    }
}
