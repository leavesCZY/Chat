package hello.leavesC.chat.view.me;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import hello.leavesC.chat.R;
import hello.leavesC.chat.view.base.BaseActivity;
import hello.leavesC.common.common.OptionView;

/**
 * 作者：叶应是叶
 * 时间：2018/1/21 19:28
 * 说明：关于
 */
public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
    }

    private void initView() {
        setToolbarTitle("关于");
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ov_gitHub: {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/leavesC/Chat")));
                        break;
                    }
                    case R.id.ov_contact: {
                        AppIntroductionActivity.navigation(AboutActivity.this, "联系方式", getString(R.string.about_contact));
                        break;
                    }
                }
            }
        };
        OptionView ov_gitHub = findViewById(R.id.ov_gitHub);
        OptionView ov_contact = findViewById(R.id.ov_contact);
        ov_gitHub.setOnClickListener(clickListener);
        ov_contact.setOnClickListener(clickListener);
    }

}