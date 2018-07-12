package codewars.tuigroup.com.codewars.ui.search;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.tuigroup.codewars.data.local.model.UserSearchHistory;

import java.util.List;

public class UsersSearchHistoryDiffCallback extends DiffUtil.Callback {

    private List<UserSearchHistory> oldItems;
    private List<UserSearchHistory> newItems;

    public UsersSearchHistoryDiffCallback(List<UserSearchHistory> oldItems, List<UserSearchHistory> newItems) {
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
        return oldItems.get(oldItemPosition).getSearchDate().equals(newItems.get(newItemPosition).getSearchDate());
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