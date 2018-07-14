package codewars.tuigroup.com.codewars.ui.challenge;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tuigroup.codewars.data.local.model.CodeChallengeEntity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import codewars.tuigroup.com.codewars.R;
import codewars.tuigroup.com.codewars.ui.widget.ContentLoadingView;
import dagger.android.support.DaggerAppCompatActivity;


public class ChallengeDetailsActivity extends DaggerAppCompatActivity implements ChallengeDetailsContract.View, ContentLoadingView.OnRetryRequestedListener {

    public static final String EXTRA_CHALLENGE_ID = "com.tuigroup.codewars.extra.EXTRA_CHALLENGE_ID";

    @Inject
    ChallengeDetailsContract.Presenter challengePresenter;
    @Inject
    String challengeId;

    @BindView(R.id.contentloadingview_challengedetails)
    ContentLoadingView contentLoadingView;
    @BindView(R.id.linearlayout_challengedetails_content)
    LinearLayout contentLinearLayout;
    @BindView(R.id.textview_challengedetails_title)
    TextView titleTextView;
    @BindView(R.id.textview_challengedetails_category)
    TextView categoryTextView;
    @BindView(R.id.textview_challengedetails_description)
    TextView descriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(challengeId);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        ButterKnife.bind(this);

        contentLoadingView.setOnRetryRequestedListener(this);

        challengePresenter.attachView(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        challengePresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        challengePresenter.unsubscribe();
    }

    @Override
    public void onDestroy() {
        challengePresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showLoadingChallengeIndicator(boolean enabled) {
        if (enabled && !contentLoadingView.isProgressBarShown()) {
            contentLinearLayout.setVisibility(View.GONE);
            contentLoadingView.hideMessage();
            contentLoadingView.showProgressBar();
        } else if (!enabled && contentLoadingView.isProgressBarShown()) {
            contentLinearLayout.setVisibility(View.GONE);
            contentLoadingView.hideMessage();
            contentLoadingView.hideProgressBar();
        }
    }

    @Override
    public void showChallenge(CodeChallengeEntity challenge) {;
        contentLinearLayout.setVisibility(View.VISIBLE);
        contentLoadingView.setVisibility(View.INVISIBLE);
        contentLoadingView.hideMessage();

        titleTextView.setText(challenge.getName());
        categoryTextView.setText(challenge.getCategory());
        descriptionTextView.setText(challenge.getDescription());
    }

    @Override
    public void showLoadingChallengeError() {
        contentLinearLayout.setVisibility(View.INVISIBLE);
        contentLoadingView.setVisibility(View.VISIBLE);
        contentLoadingView.showMessage(
                R.drawable.ic_error_24,
                R.string.generic_error_problem_server,
                true);
    }

    @Override
    public void showLoadingChallengeNoInternetError() {
        contentLinearLayout.setVisibility(View.INVISIBLE);
        contentLoadingView.setVisibility(View.VISIBLE);
        contentLoadingView.showMessage(
                R.drawable.ic_cloud_off_24,
                R.string.generic_error_no_internet,
                true);
    }

    @Override
    public void onRetryRequested() {
        challengePresenter.loadChallenge();
    }
}
