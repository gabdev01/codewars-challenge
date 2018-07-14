package codewars.tuigroup.com.codewars.ui.challenges;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tuigroup.codewars.data.remote.model.CompletedChallenge;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import codewars.tuigroup.com.codewars.R;
import codewars.tuigroup.com.codewars.ui.util.adapter.BaseQueuedAdapter;


public class ChallengesAdapter extends BaseQueuedAdapter<CompletedChallenge,
        RecyclerView.ViewHolder> {

    private ChallengesContract.Presenter challengesPresenter;

    @Inject
    public ChallengesAdapter(ChallengesContract.Presenter challengesPresenter) {
        this.challengesPresenter = challengesPresenter;
    }

    @Override
    public DiffUtil.Callback createItemDiffCallback(List<CompletedChallenge> oldItems,
                                                    List<CompletedChallenge> newItems) {
        return new CompletedChallengesDiffCallback(oldItems, newItems);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.view_challenges_item, parent, false);
        RecyclerView.ViewHolder viewHolder = new CompletedChallengesItemViewHolder(itemView, challengesPresenter);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ((CompletedChallengesItemViewHolder) viewHolder).bind(getItem(position));
    }

    public class CompletedChallengesItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ChallengesContract.Presenter challengePresenter;

        @BindView(R.id.textview_challengesitem_title)
        TextView titleTextView;

        public CompletedChallengesItemViewHolder(View view, ChallengesContract.Presenter presenter) {
            super(view);

            challengePresenter = presenter;

            ButterKnife.bind(this, itemView);

            view.setOnClickListener(this);
        }

        public void bind(CompletedChallenge challenge) {
            titleTextView.setText(challenge.getName());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) {
                return;
            }
            CompletedChallenge challenge = getItem(position);
            challengePresenter.openChallenge(challenge);
        }
    }
}
