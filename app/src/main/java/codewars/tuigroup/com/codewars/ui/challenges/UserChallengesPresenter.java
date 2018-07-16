package codewars.tuigroup.com.codewars.ui.challenges;

import javax.inject.Inject;

import codewars.tuigroup.com.codewars.ui.base.BasePresenter;
import codewars.tuigroup.com.codewars.ui.util.rx.SchedulerProvider;

public class UserChallengesPresenter extends BasePresenter<UserChallengesContract.View, UserChallengesContract.State>
        implements UserChallengesContract.Presenter {

    private ChallengesViewType challengesViewType;

    @Inject
    public UserChallengesPresenter(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
        challengesViewType = ChallengesViewType.COMPLETED;
    }

    @Override
    public void attachView(UserChallengesContract.View view, UserChallengesContract.State state) {
        super.attachView(view, state);
        if (state != null && state.getChallengesViewType() != null) {
            challengesViewType = state.getChallengesViewType();
        }
    }

    @Override
    public void subscribe() {
        super.subscribe();
        if (challengesViewType == ChallengesViewType.AUTHORED) {
            view.showAuthoredChallengesView();
        } else {
            view.showCompletedChallengesView();
        }
    }

    @Override
    public UserChallengesContract.State getState() {
        return new UserChallengesState(challengesViewType);
    }

    @Override
    public void openAuthoredChallengesView() {
        challengesViewType = ChallengesViewType.AUTHORED;
        view.showAuthoredChallengesView();
    }

    @Override
    public void openCompletedChallengesView() {
        challengesViewType = ChallengesViewType.COMPLETED;
        view.showCompletedChallengesView();
    }
}
