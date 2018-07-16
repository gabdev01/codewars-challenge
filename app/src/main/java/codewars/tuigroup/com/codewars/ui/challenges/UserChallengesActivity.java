package codewars.tuigroup.com.codewars.ui.challenges;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

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

    private static final String KEY_STATE_CHALLENGES_VIEW_TYPE = "com.tuigroup.codewars.KEY_STATE_CHALLENGES_VIEW_TYPE";

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
                item -> {
                    switch (item.getItemId()) {
                        case R.id.menu_userchallenges_completed:
                            userChallengesContract.openCompletedChallengesView();
                            return true;
                        case R.id.menu_userchallenges_authored:
                            userChallengesContract.openAuthoredChallengesView();
                            return true;
                    }
                    return false;
                });


        completedCompletedChallengesFragment = new CompletedChallengesFragment();
        authoredChallengesFragment = new AuthoredChallengesFragment();

        userChallengesContract.attachView(this, savedInstanceState != null ? readFromBundle(savedInstanceState) : null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        writeToBundle(outState, userChallengesContract.getState());
    }

    private void replaceContentFragmentBy(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.framelayout_userchallenges_content, fragment)
                .commit();
    }

    private UserChallengesContract.State readFromBundle(Bundle savedInstanceState) {
        ChallengesViewType viewType = ChallengesViewType
                .from(savedInstanceState.getInt(KEY_STATE_CHALLENGES_VIEW_TYPE));
        return new UserChallengesState(viewType);
    }

    private void writeToBundle(Bundle outState, UserChallengesContract.State state) {
        if (state == null) {
            return;
        }
        if (state.getChallengesViewType() != null) {
            outState.putInt(KEY_STATE_CHALLENGES_VIEW_TYPE, state.getChallengesViewType().getValue());
        }
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
        bottomNavigationView.getMenu().findItem(R.id.menu_userchallenges_authored).setChecked(true);
    }

    @Override
    public void showCompletedChallengesView() {
        replaceContentFragmentBy(completedCompletedChallengesFragment);
        bottomNavigationView.getMenu().findItem(R.id.menu_userchallenges_completed).setChecked(true);
    }
}
