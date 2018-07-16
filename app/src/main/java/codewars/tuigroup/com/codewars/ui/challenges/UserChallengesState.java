package codewars.tuigroup.com.codewars.ui.challenges;

public class UserChallengesState implements UserChallengesContract.State {

    private ChallengesViewType challengesViewType;

    public UserChallengesState(ChallengesViewType challengesViewType) {
        this.challengesViewType = challengesViewType;
    }

    @Override
    public ChallengesViewType getChallengesViewType() {
        return challengesViewType;
    }
}
