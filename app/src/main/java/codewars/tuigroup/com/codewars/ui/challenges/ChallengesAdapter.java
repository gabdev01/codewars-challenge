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
import codewars.tuigroup.com.codewars.di.ActivityScoped;
import codewars.tuigroup.com.codewars.ui.util.adapter.BaseQueuedAdapter;

@ActivityScoped
public class ChallengesAdapter extends BaseQueuedAdapter<CompletedChallenge,
        RecyclerView.ViewHolder> {

    private OnChallengeClickListener onChallengeClickListener;

    @Inject
    public ChallengesAdapter() {
    }

    public void setOnChallengeClickListener(OnChallengeClickListener listener) {
        onChallengeClickListener = listener;
    }

    private void performChallengeClicked(CompletedChallenge challenge) {
        if (onChallengeClickListener != null) {
            onChallengeClickListener.onChallengeClicked(challenge);
        }
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
        RecyclerView.ViewHolder viewHolder = new CompletedChallengesItemViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ((CompletedChallengesItemViewHolder) viewHolder).bind(getItem(position));
    }

    public class CompletedChallengesItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.textview_challengesitem_title)
        TextView titleTextView;

        public CompletedChallengesItemViewHolder(View view) {
            super(view);

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
            performChallengeClicked(challenge);
        }
    }

    public interface OnChallengeClickListener {
        void onChallengeClicked(CompletedChallenge challenge);
    }
}
