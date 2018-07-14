package codewars.tuigroup.com.codewars.ui.challenges;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.tuigroup.codewars.data.remote.model.CompletedChallenge;

import java.util.List;

public class CompletedChallengesDiffCallback extends DiffUtil.Callback {

    private List<CompletedChallenge> oldItems;
    private List<CompletedChallenge> newItems;

    public CompletedChallengesDiffCallback(List<CompletedChallenge> oldItems, List<CompletedChallenge> newItems) {
        this.oldItems = oldItems;
        this.newItems = newItems;
    }

    @Override
    public int getOldListSize() {
        return oldItems.size();
    }

    @Override
    public int getNewListSize() {
        return newItems.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItems.get(oldItemPosition).getId().equals(newItems.get(newItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItems.get(oldItemPosition).equals(newItems.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}