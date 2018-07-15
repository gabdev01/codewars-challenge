package codewars.tuigroup.com.codewars.ui.challenges.completed;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tuigroup.codewars.data.local.model.CompletedChallengeEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import codewars.tuigroup.com.codewars.R;
import codewars.tuigroup.com.codewars.di.ActivityScoped;

@ActivityScoped
public class CompletedChallengesAdapter extends PagedListAdapter<CompletedChallengeEntity,
        RecyclerView.ViewHolder> {

    private OnChallengeClickListener onChallengeClickListener;

    protected CompletedChallengesAdapter(@NonNull DiffUtil.ItemCallback<CompletedChallengeEntity> diffCallback) {
        super(diffCallback);
    }

    public void setOnChallengeClickListener(OnChallengeClickListener listener) {
        onChallengeClickListener = listener;
    }

    private void performChallengeClicked(CompletedChallengeEntity challenge) {
        if (onChallengeClickListener != null) {
            onChallengeClickListener.onChallengeClicked(challenge);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.view_completed_challenges_item, parent, false);
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

        public void bind(CompletedChallengeEntity challenge) {
            if (challenge != null) {
                titleTextView.setText(challenge.getName());
            }
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) {
                return;
            }
            CompletedChallengeEntity challenge = getItem(position);
            performChallengeClicked(challenge);
        }
    }

    public interface OnChallengeClickListener {
        void onChallengeClicked(CompletedChallengeEntity challenge);
    }
}
