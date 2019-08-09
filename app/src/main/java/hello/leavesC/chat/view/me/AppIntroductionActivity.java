package hello.leavesC.chat.view.me;

import androidx.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import hello.leavesC.chat.R;
import hello.leavesC.chat.view.base.BaseActivity;

/**
 * 作者：叶应是叶
 * 时间：2018/1/21 21:25
 * 说明：应用简介界面
 */
public class AppIntroductionActivity extends BaseActivity {

    private static final String TITLE = "title";

    private static final String CONTENT = "content";

    public static void navigation(Context context, String title, String content) {
        Intent intent = new Intent(context, AppIntroductionActivity.class);
        intent.putExtra(TITLE, title);
        intent.putExtra(CONTENT, content);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_introduction);
        String title = getIntent().getStringExtra(TITLE);
        String content = getIntent().getStringExtra(CONTENT);
        TextView tv_introduction = findViewById(R.id.tv_appIntroduction);
        tv_introduction.setText(content);
        setToolbarTitle(title);
    }

    @Override
    protected ViewModel initViewModel() {
        return null;
    }

}