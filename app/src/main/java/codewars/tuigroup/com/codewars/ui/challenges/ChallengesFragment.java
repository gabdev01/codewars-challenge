package codewars.tuigroup.com.codewars.ui.challenges;

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

import com.tuigroup.codewars.data.remote.model.CompletedChallenge;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import codewars.tuigroup.com.codewars.R;
import codewars.tuigroup.com.codewars.di.ActivityScoped;
import dagger.android.support.DaggerFragment;

@ActivityScoped
public class ChallengesFragment extends DaggerFragment implements ChallengesContract.View {

    @Inject
    ChallengesContract.Presenter challengesPresenter;
    @Inject
    ChallengesAdapter challengesAdapter;

    @BindView(R.id.recyclerview_challenges)
    RecyclerView challengesRecyclerView;

    @Inject
    public ChallengesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_challenges, container, false);
        ButterKnife.bind(this, rootView);

        challengesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        challengesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration decoration = new DividerItemDecoration(
                getContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(new ColorDrawable(getResources().getColor(R.color.search_user_item_divider)));
        challengesRecyclerView.addItemDecoration(decoration);
        challengesRecyclerView.setAdapter(challengesAdapter);

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

    }

    @Override
    public void showLoadingChallengesError() {

    }

    @Override
    public void showLoadingChallengesNoInternetError() {

    }

    @Override
    public void showChallenges(List<CompletedChallenge> challenges) {
        challengesAdapter.update(challenges);
    }

    @Override
    public void showChallengeView() {

    }
}
