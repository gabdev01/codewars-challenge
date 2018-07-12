package codewars.tuigroup.com.codewars.ui.search;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tuigroup.codewars.data.local.model.UserEntity;
import com.tuigroup.codewars.data.local.model.UserSearchHistory;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import codewars.tuigroup.com.codewars.R;
import codewars.tuigroup.com.codewars.ui.util.adapter.BaseQueuedAdapter;


public class UsersSearchHistoryAdapter extends BaseQueuedAdapter<UserSearchHistory,
        RecyclerView.ViewHolder> {

    private SearchUserContract.Presenter searchUserPresenter;

    @Inject
    public UsersSearchHistoryAdapter(SearchUserContract.Presenter searchUserPresenter) {
        this.searchUserPresenter = searchUserPresenter;
    }

    @Override
    public DiffUtil.Callback createItemDiffCallback(List<UserSearchHistory> oldItems,
                                                    List<UserSearchHistory> newItems) {
        return new UsersSearchHistoryDiffCallback(oldItems, newItems);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.view_search_user_item, parent, false);
        RecyclerView.ViewHolder viewHolder = new UserItemViewHolder(itemView, searchUserPresenter);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ((UserItemViewHolder) viewHolder).bind(getItem(position));
    }

    public class UserItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private SearchUserContract.Presenter searchUserPresenter;

        @BindView(R.id.textview_searchuseritem_leaderboardposition)
        TextView leaderboardPositionTextView;
        @BindView(R.id.textview_searchuseritem_username)
        TextView usernameTextView;
        @BindView(R.id.textview_searchuseritem_clan)
        TextView clanTextView;
        @BindView(R.id.textview_searchuseritem_honor)
        TextView honorTextView;

        public UserItemViewHolder(View view, SearchUserContract.Presenter presenter) {
            super(view);

            searchUserPresenter = presenter;

            ButterKnife.bind(this, itemView);

            view.setOnClickListener(this);
        }

        public void bind(UserSearchHistory searchedUser) {
            UserEntity user = searchedUser.getUser();
            Context context = itemView.getContext();
            leaderboardPositionTextView.setText(String.format(context.getString(
                    R.string.search_user_item_leaderboard_position), user.getLeaderboardPosition()));
            usernameTextView.setText(user.getUsername());
            clanTextView.setText(user.getClan());
            honorTextView.setText(String.valueOf(user.getHonor()));
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) {
                return;
            }
            UserSearchHistory searchedUser = getItem(position);
            searchUserPresenter.openUserDetails(searchedUser.getUser());
        }
    }
}
