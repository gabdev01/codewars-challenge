package codewars.tuigroup.com.codewars.ui.challenges;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import codewars.tuigroup.com.codewars.R;
import codewars.tuigroup.com.codewars.ui.challenges.authored.AuthoredChallengesFragment;
import codewars.tuigroup.com.codewars.ui.challenges.completed.CompletedChallengesFragment;
import dagger.android.support.DaggerAppCompatActivity;


public class UserChallengesActivity extends DaggerAppCompatActivity
        implements UserChallengesContract.View {

    public static final String EXTRA_USER_ID = "com.tuigroup.codewars.extra.EXTRA_USER_ID";

    @Inject
    UserChallengesContract.Presenter userChallengesContract;
    @Inject
    String userId;

    @BindView(R.id.bottomnav_userchallenges)
    BottomNavigationView bottomNavigationView;

    private CompletedChallengesFragment completedCompletedChallengesFragment;
    private AuthoredChallengesFragment authoredChallengesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_challenges);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(userId);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        ButterKnife.bind(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_userchallenges_completed:
                                replaceContentFragmentBy(completedCompletedChallengesFragment);
                                return true;
                            case R.id.menu_userchallenges_authored:
                                replaceContentFragmentBy(authoredChallengesFragment);
                                return true;
                        }
                        return false;
                    }
                });


        completedCompletedChallengesFragment = new CompletedChallengesFragment();
        authoredChallengesFragment = new AuthoredChallengesFragment();

        userChallengesContract.attachView(this);
        userChallengesContract.openCompletedChallengesView();
        bottomNavigationView.setSelectedItemId(R.id.menu_userchallenges_completed);
    }

    private void replaceContentFragmentBy(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.framelayout_userchallenges_content, fragment)
                .commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        userChallengesContract.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        userChallengesContract.unsubscribe();
    }

    @Override
    public void onDestroy() {
        userChallengesContract.detachView();
        super.onDestroy();
    }

    @Override
    public void showAuthoredChallengesView() {
        replaceContentFragmentBy(authoredChallengesFragment);
    }

    @Override
    public void showCompletedChallengesView() {
        replaceContentFragmentBy(completedCompletedChallengesFragment);
    }
}
