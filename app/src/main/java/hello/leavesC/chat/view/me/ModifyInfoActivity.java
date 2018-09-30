package hello.leavesC.chat.view.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import hello.leavesC.chat.R;
import hello.leavesC.chat.view.base.BaseActivity;
import hello.leavesC.presenter.listener.CallBackListener;
import hello.leavesC.presenter.manager.SelfProfileManager;
import hello.leavesC.presenter.viewModel.base.BaseViewModel;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 21:15
 * 说明：修改个人资料界面
 */
public class ModifyInfoActivity extends BaseActivity {

    public static final int REQUEST_ALTER_NICKNAME = 1;

    public static final int REQUEST_ALTER_SIGNATURE = 2;

    private static final String ORIGINAL = "original";

    private static final String REQUEST_TYPE = "requestType";

    private String original;

    private EditText et_alterInfo;

    private int requestType;

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().equals(original)) {
                getToolbarBtn().setEnabled(false);
            } else {
                getToolbarBtn().setEnabled(true);
            }
        }
    };

    private CallBackListener callBackListener = new CallBackListener() {
        @Override
        public void onSuccess() {
            if (!isFinishing() && !isDestroyed()) {
                dismissLoadingDialog();
                finish();
            }
        }

        @Override
        public void onError(int code, String desc) {
            if (!isFinishing() && !isDestroyed()) {
                dismissLoadingDialog();
                showToast("code:" + code + " describe:" + desc);
            }
        }
    };

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String result = et_alterInfo.getText().toString().trim();
            if (requestType == REQUEST_ALTER_NICKNAME) {
                SelfProfileManager.setNickname(result, callBackListener);
            } else {
                SelfProfileManager.setSignature(result, callBackListener);
            }
            showLoadingDialog("正在更新");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);
        requestType = getIntent().getIntExtra(REQUEST_TYPE, 0);
        setToolbarBtnText("保存");
        if (requestType == REQUEST_ALTER_NICKNAME) {
            initView();
            setToolbarTitle("设置昵称");
            TextView tv_alterInfo_hint = findViewById(R.id.tv_alterInfo_hint);
            tv_alterInfo_hint.setText("好名字可以让你的朋友更容易记住你。");
        } else if (requestType == REQUEST_ALTER_SIGNATURE) {
            initView();
            setToolbarTitle("设置个性签名");
            TextView tv_alterInfo_hint = findViewById(R.id.tv_alterInfo_hint);
            tv_alterInfo_hint.setText("写写你的想法。");
        } else {
            finish();
        }
    }

    @Override
    protected BaseViewModel initViewModel() {
        return null;
    }

    private void initView() {
        et_alterInfo = findViewById(R.id.et_alterInfo);
        original = getIntent().getStringExtra(ORIGINAL);
        et_alterInfo.setText(original);
        et_alterInfo.addTextChangedListener(textWatcher);
        setToolbarBtnClickListener(clickListener);
    }

    public static void navigation(Context context, int requestType, String original) {
        Intent intent = new Intent(context, ModifyInfoActivity.class);
        intent.putExtra(ORIGINAL, original);
        intent.putExtra(REQUEST_TYPE, requestType);
        context.startActivity(intent);
    }

}