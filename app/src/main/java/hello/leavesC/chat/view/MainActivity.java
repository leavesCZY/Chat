package hello.leavesC.chat.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import hello.leavesC.chat.R;
import hello.leavesC.chat.cache.FriendCache;
import hello.leavesC.chat.cache.GroupCache;
import hello.leavesC.chat.view.base.BaseActivity;
import hello.leavesC.chat.view.contacts.ContactsFragment;
import hello.leavesC.chat.view.conversation.ConversationFragment;
import hello.leavesC.chat.view.group.SelectFriendToCreateGroupActivity;
import hello.leavesC.chat.view.me.MeFragment;
import hello.leavesC.chat.view.open.OpenActivity;
import hello.leavesC.presenter.event.FriendEvent;
import hello.leavesC.presenter.event.GroupEvent;
import hello.leavesC.presenter.event.MessageEvent;
import hello.leavesC.presenter.event.RefreshEvent;
import hello.leavesC.presenter.tls.service.TlsService;
import hello.leavesC.presenter.viewModel.base.BaseViewModel;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 21:18
 * 说明：主界面
 */
public class MainActivity extends BaseActivity {

    private List<Fragment> tabFragments;

    private ViewPager viewPager;

    private FragmentPagerAdapter adapter;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        viewPager.setAdapter(adapter);
    }

    @Override
    protected BaseViewModel initViewModel() {
        return null;
    }

    private void initData() {
        tabFragments = new ArrayList<>();
        ConversationFragment conversationFragment = new ConversationFragment();
        ContactsFragment contactsFragment = new ContactsFragment();
        MeFragment meFragment = new MeFragment();
        tabFragments.add(conversationFragment);
        tabFragments.add(contactsFragment);
        tabFragments.add(meFragment);
        final String[] titles = {"会话", "好友", "我"};
        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return tabFragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return tabFragments.get(arg0);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        };
    }

    private void initView() {
        initToolbar("云聊", false);
        viewPager = findViewById(R.id.viewpager_main);
        TabLayout tabLayout = findViewById(R.id.tabLayout_main);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_addFriend:
                startActivity(SearchUserActivity.class);
                break;
            case R.id.menu_createGroup:
                startActivity(SelectFriendToCreateGroupActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FriendCache.getInstance().clear();
        GroupCache.getInstance().clear();
        FriendEvent.getInstance().clean();
        GroupEvent.getInstance().clean();
        MessageEvent.getInstance().clean();
        RefreshEvent.getInstance().clean();
    }

    public void logout() {
        TlsService.getInstance(this).clearUserInfo();
        startActivity(OpenActivity.class);
        setResult(RESULT_OK);
        finish();
    }

}