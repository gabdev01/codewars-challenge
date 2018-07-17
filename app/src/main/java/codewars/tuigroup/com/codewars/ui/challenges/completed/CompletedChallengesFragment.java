package codewars.tuigroup.com.codewars.ui.challenges.completed;

import android.arch.paging.PagedList;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tuigroup.codewars.data.local.model.CompletedChallengeEntity;

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
public class CompletedChallengesFragment extends DaggerFragment implements
        CompletedChallengesContract.View,
        CompletedChallengesAdapter.OnChallengeClickListener {

    @Inject
    CompletedChallengesContract.Presenter challengesPresenter;

    @BindView(R.id.recyclerview_completedchallenges)
    RecyclerView challengesRecyclerView;
    @BindView(R.id.contentloadingview_completedchallenges)
    ContentLoadingView contentLoadingView;
    @BindView(R.id.contentloadingview_completedchallenges_loadmore)
    ContentLoadingView moreContentLoadingView;
    private CompletedChallengesAdapter completedChallengesAdapter;

    @Inject
    public CompletedChallengesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_completed_challenges, container, false);

        ButterKnife.bind(this, rootView);

        completedChallengesAdapter = new CompletedChallengesAdapter(getDiffCallback());
        challengesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        challengesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        challengesRecyclerView.setNestedScrollingEnabled(true);
        DividerItemDecoration decoration = new DividerItemDecoration(
                getContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(new ColorDrawable(getResources().getColor(R.color.search_user_item_divider)));
        challengesRecyclerView.addItemDecoration(decoration);
        completedChallengesAdapter.setOnChallengeClickListener(this);
        challengesRecyclerView.setAdapter(completedChallengesAdapter);

        contentLoadingView.setOnRetryRequestedListener(() -> {
            challengesPresenter.loadChallenges();
        });
        moreContentLoadingView.setTextColor(getResources().getColor(R.color.secondary_text_light_normal));
        moreContentLoadingView.setOnRetryRequestedListener(() -> {
            challengesPresenter.retryLoadMoreChallenges();
        });

        return rootView;
    }

    @NonNull
    private DiffUtil.ItemCallback<CompletedChallengeEntity> getDiffCallback() {
        return new DiffUtil.ItemCallback<CompletedChallengeEntity>() {
            @Override
            public boolean areItemsTheSame(CompletedChallengeEntity oldItem, CompletedChallengeEntity newItem) {
                return oldItem.getId().equals(newItem.getId());
            }

            @Override
            public boolean areContentsTheSame(CompletedChallengeEntity oldItem, CompletedChallengeEntity newItem) {
                return oldItem.equals(newItem);
            }
        };
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
            contentLoadingView.setVisibility(View.VISIBLE);
            contentLoadingView.hideMessage();
            contentLoadingView.showProgressBar();
        } else if (!enabled && contentLoadingView.isProgressBarShown()) {
            challengesRecyclerView.setVisibility(View.INVISIBLE);
            contentLoadingView.setVisibility(View.INVISIBLE);
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
        contentLoadingView.setVisibility(View.VISIBLE);
        contentLoadingView.showMessage(
                R.drawable.ic_cloud_off_24,
                R.string.generic_error_no_internet,
                true);
    }

    @Override
    public void showChallenges(PagedList<CompletedChallengeEntity> challenges) {
        challengesRecyclerView.setVisibility(View.VISIBLE);
        contentLoadingView.setVisibility(View.INVISIBLE);

        completedChallengesAdapter.submitList(challenges);
    }

    @Override
    public void showNoChallenges() {
        challengesRecyclerView.setVisibility(View.INVISIBLE);
        contentLoadingView.setVisibility(View.VISIBLE);
        contentLoadingView.showMessage(
                R.drawable.ic_no_content_24,
                R.string.completed_challenges_empty_label,
                false);
    }

    @Override
    public void showLoadingMoreChallengesIndicator(boolean enabled) {
        contentLoadingView.setVisibility(View.INVISIBLE);
        if (enabled && !moreContentLoadingView.isProgressBarShown()) {
            moreContentLoadingView.hideMessage();
            moreContentLoadingView.showProgressBar();
        } else if (!enabled && moreContentLoadingView.isProgressBarShown()) {
            moreContentLoadingView.hideMessage();
            moreContentLoadingView.hideProgressBar();
        }
    }

    @Override
    public void showLoadingMoreChallengesError() {
        moreContentLoadingView.showMessage(
                -1,
                -1,
                true);
    }

    @Override
    public void showChallengeView(String challengeId) {
        Intent intent = new Intent(getContext(), ChallengeDetailsActivity.class);
        intent.putExtra(EXTRA_CHALLENGE_ID, challengeId);
        startActivity(intent);
    }

    @Override
    public void showAllDataLoaded() {
        // TODO This view should be scrollable with the recyclerview for a better effect
        moreContentLoadingView.showMessage(R.string.completed_challenges_all_data_loaded);
    }

    @Override
    public void onChallengeClicked(CompletedChallengeEntity challenge) {
        challengesPresenter.openChallenge(challenge);
    }
}
