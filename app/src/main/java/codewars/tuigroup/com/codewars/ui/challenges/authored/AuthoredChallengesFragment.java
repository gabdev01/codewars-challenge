package codewars.tuigroup.com.codewars.ui.challenges.authored;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tuigroup.codewars.data.local.model.AuthoredChallengeEntity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import codewars.tuigroup.com.codewars.R;
import codewars.tuigroup.com.codewars.di.ActivityScoped;
import codewars.tuigroup.com.codewars.ui.challenge.ChallengeDetailsActivity;
import codewars.tuigroup.com.codewars.ui.widget.ContentLoadingView;
import dagger.android.support.DaggerFragment;

import static codewars.tuigroup.com.codewars.ui.challenge.ChallengeDetailsActivity.EXTRA_CHALLENGE_ID;

@ActivityScoped
public class AuthoredChallengesFragment extends DaggerFragment implements
        AuthoredChallengesContract.View,
        AuthoredChallengesAdapter.OnChallengeClickListener, ContentLoadingView.OnRetryRequestedListener {

    @Inject
    AuthoredChallengesContract.Presenter challengesPresenter;
    @Inject
    AuthoredChallengesAdapter authoredChallengesAdapter;

    @BindView(R.id.recyclerview_authoredchallenges)
    RecyclerView challengesRecyclerView;
    @BindView(R.id.contentloadingview_authoredchallenges)
    ContentLoadingView contentLoadingView;


    @Inject
    public AuthoredChallengesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_authored_challenges, container, false);

        ButterKnife.bind(this, rootView);

        contentLoadingView.setOnRetryRequestedListener(this);

        challengesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        challengesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration decoration = new DividerItemDecoration(
                getContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(new ColorDrawable(getResources().getColor(R.color.search_user_item_divider)));
        challengesRecyclerView.addItemDecoration(decoration);
        challengesRecyclerView.setNestedScrollingEnabled(false);
        authoredChallengesAdapter.setOnChallengeClickListener(this);
        challengesRecyclerView.setAdapter(authoredChallengesAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        challengesPresenter.attachView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        challengesPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        challengesPresenter.unsubscribe();
    }

    @Override
    public void onDestroy() {
        challengesPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showLoadingChallengesIndicator(boolean enabled) {
        if (enabled && !contentLoadingView.isProgressBarShown()) {
            challengesRecyclerView.setVisibility(View.INVISIBLE);
            contentLoadingView.hideMessage();
            contentLoadingView.showProgressBar();
        } else if (!enabled && contentLoadingView.isProgressBarShown()) {
            challengesRecyclerView.setVisibility(View.INVISIBLE);
            contentLoadingView.hideMessage();
            contentLoadingView.hideProgressBar();
        }
    }

    @Override
    public void showLoadingChallengesError() {
        challengesRecyclerView.setVisibility(View.INVISIBLE);
        contentLoadingView.setVisibility(View.VISIBLE);
        contentLoadingView.showMessage(
                R.drawable.ic_error_24,
                R.string.generic_error_problem_server,
                true);
    }

    @Override
    public void showLoadingChallengesNoInternetError() {
        challengesRecyclerView.setVisibility(View.INVISIBLE);
        contentLoadingView.setVisibility(View.VISIBLE);
        contentLoadingView.showMessage(
                R.drawable.ic_cloud_off_24,
                R.string.generic_error_no_internet,
                true);
    }

    @Override
    public void showChallenges(List<AuthoredChallengeEntity> challenges) {
        challengesRecyclerView.setVisibility(View.VISIBLE);
        contentLoadingView.setVisibility(View.INVISIBLE);
        contentLoadingView.hideMessage();

        authoredChallengesAdapter.update(challenges);
    }

    @Override
    public void showNoChallenges() {
        challengesRecyclerView.setVisibility(View.INVISIBLE);
        contentLoadingView.setVisibility(View.VISIBLE);
        contentLoadingView.showMessage(
                R.drawable.ic_no_content_24,
                R.string.authored_challenges_empty_label,
                false);
    }

    @Override
    public void showChallengeView(String challengeId) {
        Intent intent = new Intent(getContext(), ChallengeDetailsActivity.class);
        intent.putExtra(EXTRA_CHALLENGE_ID, challengeId);
        startActivity(intent);
    }

    @Override
    public void onChallengeClicked(AuthoredChallengeEntity challenge) {
        challengesPresenter.openChallenge(challenge);
    }

    @Override
    public void onRetryRequested() {
        challengesPresenter.loadChallenges();
    }
}
