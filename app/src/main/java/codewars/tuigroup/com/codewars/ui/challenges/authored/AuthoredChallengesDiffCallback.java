package codewars.tuigroup.com.codewars.ui.challenges.authored;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.tuigroup.codewars.data.local.model.AuthoredChallengeEntity;

import java.util.List;

public class AuthoredChallengesDiffCallback extends DiffUtil.Callback {

    private List<AuthoredChallengeEntity> oldItems;
    private List<AuthoredChallengeEntity> newItems;

    public AuthoredChallengesDiffCallback(List<AuthoredChallengeEntity> oldItems, List<AuthoredChallengeEntity> newItems) {
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