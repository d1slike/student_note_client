package ru.disdev.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import ru.disdev.R;

public class GroupManageActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int CREATE_GROUP_REQUEST = 665;

    private LinearLayout manageGroupLayout;
    private FrameLayout requestGroupLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideProgressBar();
        manageGroupLayout = (LinearLayout) findViewById(R.id.manage_group_layout);
        requestGroupLayout = (FrameLayout) findViewById(R.id.request_group_layout);
        requestGroupLayout.setVisibility(View.INVISIBLE);
        //showProgressBar();
        //ConcurrencyUtils.sendPacketToServerAndHandleAnswer();
    }

    public void showProgressBar() {
        setContentView(R.layout.wait_layout);
    }

    public void hideProgressBar() {
        setContentView(R.layout.activity_group_manage_main);
    }

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_GROUP_REQUEST && resultCode == CreateGroupActivity.SUCCESS_CREATE_GROUP_RESULT_ID) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_group_button:
                break;
            case R.id.create_group_button:
                Intent intent = new Intent(this, CreateGroupActivity.class);
                startActivityForResult(intent, CREATE_GROUP_REQUEST);
                break;
            case R.id.left_group_button:
                break;
        }
    }
}
