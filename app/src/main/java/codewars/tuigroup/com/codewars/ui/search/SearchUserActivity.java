package codewars.tuigroup.com.codewars.ui.search;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tuigroup.codewars.data.UserRepositoryContract;
import com.tuigroup.codewars.data.local.model.UserEntity;
import com.tuigroup.codewars.data.local.model.UserSearchHistory;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import codewars.tuigroup.com.codewars.R;
import codewars.tuigroup.com.codewars.ui.challenges.UserChallengesActivity;
import dagger.android.support.DaggerAppCompatActivity;


public class SearchUserActivity extends DaggerAppCompatActivity implements View.OnClickListener, SearchUserContract.View {

    @Inject
    SearchUserContract.Presenter searchUserPresenter;

    @BindView(R.id.edittext_searchuser_toolbarsearch)
    EditText searchEditText;
    @BindView(R.id.cardview_searchuser_searchresult)
    CardView searchResultCardView;
    @BindView(R.id.textview_searchuser_searchresult)
    TextView searchResultTextView;
    @BindView(R.id.progressbar_searchuser_searchresult)
    ProgressBar searchResultProgressBar;
    @BindView(R.id.imageview_usersearch_clearsearch)
    ImageView clearSearchImageView;
    @BindView(R.id.linearlayout_searchuseritem_root)
    LinearLayout rootSearchUserItemLinearLayout;
    @BindView(R.id.textview_searchuseritem_leaderboardposition)
    TextView leaderboardPositionTextView;
    @BindView(R.id.textview_searchuseritem_username)
    TextView usernameTextView;
    @BindView(R.id.textview_searchuseritem_clan)
    TextView clanTextView;
    @BindView(R.id.textview_searchuseritem_honor)
    TextView honorTextView;
    @BindView(R.id.imageview_searchuser_sortsearchhistory)
    ImageView sortSearchHistoryImageView;
    @BindView(R.id.textview_searchuser_searchhistory)
    TextView searchHistoryTextView;
    @BindView(R.id.recyclerview_searchuser_searchhistory)
    RecyclerView searchHistoryRecyclerView;


    @Inject
    UsersSearchHistoryAdapter usersSearchHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        View customToolBarView = LayoutInflater.from(this)
                .inflate(R.layout.view_search_user_toolbar, null);
        Toolbar.LayoutParams params = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.MATCH_PARENT,
                Toolbar.LayoutParams.MATCH_PARENT);
        customToolBarView.setLayoutParams(params);
        getSupportActionBar().setCustomView(customToolBarView, params);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        ButterKnife.bind(this);

        searchEditText.setCursorVisible(false);
        searchEditText.setOnClickListener(v -> {
            searchEditText.setCursorVisible(true);
        });
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return false;
            }
            return false;
        });
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    clearSearchImageView.setVisibility(View.INVISIBLE);
                } else {
                    clearSearchImageView.setVisibility(View.VISIBLE);
                }
            }
        });

        // As the card view has its own ripple effect it is better to remove this one
        rootSearchUserItemLinearLayout.setBackground(null);

        searchHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchHistoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration decoration = new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(new ColorDrawable(getResources().getColor(R.color.search_user_item_divider)));
        searchHistoryRecyclerView.addItemDecoration(decoration);
        searchHistoryRecyclerView.setAdapter(usersSearchHistoryAdapter);

        clearSearchImageView.setOnClickListener(this);
        searchResultCardView.setOnClickListener(this);
        sortSearchHistoryImageView.setOnClickListener(this);

        searchResultCardView.setClickable(false);

        searchUserPresenter.attachView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        searchUserPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        searchUserPresenter.unsubscribe();
    }

    @Override
    public void onDestroy() {
        searchUserPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imageview_usersearch_clearsearch) {
            performClearSearch();
        } else if (v.getId() == R.id.cardview_searchuser_searchresult) {
            performOpenUserFound();
        } else if (v.getId() == R.id.imageview_searchuser_sortsearchhistory) {
            performSortSearchHistory();
        }
    }

    private void performSearch() {
        searchEditText.setCursorVisible(false);
        String searchText = searchEditText.getText().toString();
        searchUserPresenter.searchUser(searchText);
    }

    private void performOpenUserFound() {
        searchUserPresenter.openUserFoundChallenges();
    }

    private void performClearSearch() {
        searchEditText.getText().clear();
    }

    private void performSortSearchHistory() {
        String[] sortMenus = new String[2];
        sortMenus[0] = getResources().getString(R.string.search_user_sort_by_date);
        sortMenus[1] = getResources().getString(R.string.search_user_sort_by_rank);
        AlertDialog.Builder sortDialog = new AlertDialog.Builder(this);
        sortDialog.setTitle(getResources().getString(R.string.search_user_sort_title));
        sortDialog.setItems(sortMenus, (dialog, index) -> {
            if (index == 1) {
                searchUserPresenter.loadSearchHistory(UserRepositoryContract.UserOrderBy.HIGHEST_RANK);
            } else {
                searchUserPresenter.loadSearchHistory(UserRepositoryContract.UserOrderBy.DATE_ADDED);
            }
            dialog.dismiss();
        });
        AlertDialog alert = sortDialog.create();
        alert.show();
    }

    @Override
    public void showSearchUserIndicator(boolean enabled) {
        if (enabled) {
            searchResultProgressBar.setVisibility(View.VISIBLE);
        } else {
            searchResultProgressBar.setVisibility(View.INVISIBLE);
        }
        searchResultTextView.setVisibility(View.INVISIBLE);
        rootSearchUserItemLinearLayout.setVisibility(View.INVISIBLE);
        searchResultCardView.setClickable(false);
    }

    @Override
    public void showSearchUserNotFound() {
        showSearchUserMessage(
                getString(R.string.search_user_search_result_not_found),
                getResources().getColor(R.color.primary_text_light_normal));
        searchResultCardView.setClickable(false);
    }

    @Override
    public void showSearchUserError() {
        showSearchUserMessage(
                getString(R.string.generic_error_problem_server),
                getResources().getColor(R.color.error_text_light_normal));
        searchResultCardView.setClickable(false);
    }

    @Override
    public void showSearchUserNoInternetError() {
        showSearchUserMessage(
                getString(R.string.generic_error_no_internet),
                getResources().getColor(R.color.error_text_light_normal));
        searchResultCardView.setClickable(false);
    }

    private void showSearchUserMessage(String message, @ColorInt int color) {
        searchResultProgressBar.setVisibility(View.INVISIBLE);
        searchResultTextView.setVisibility(View.VISIBLE);
        rootSearchUserItemLinearLayout.setVisibility(View.INVISIBLE);
        searchResultTextView.setTextColor(color);
        searchResultTextView.setText(message);
    }

    @Override
    public void showSearchUserSuccess(UserEntity user) {
        searchResultProgressBar.setVisibility(View.INVISIBLE);
        searchResultTextView.setVisibility(View.INVISIBLE);
        rootSearchUserItemLinearLayout.setVisibility(View.VISIBLE);
        searchResultCardView.setClickable(true);

        leaderboardPositionTextView.setText(String.format(
                getString(R.string.search_user_item_leaderboard_position), user.getLeaderboardPosition()));
        usernameTextView.setText(user.getUsername());
        clanTextView.setText(user.getClan());
        honorTextView.setText(String.valueOf(user.getHonor()));
    }

    @Override
    public void showUsersSearchHistory(List<UserSearchHistory> usersSearchHistory) {
        searchHistoryRecyclerView.setVisibility(View.VISIBLE);
        searchHistoryTextView.setVisibility(View.INVISIBLE);
        usersSearchHistoryAdapter.update(usersSearchHistory);
    }

    @Override
    public void showNoUsersSearchHistory() {
        searchHistoryRecyclerView.setVisibility(View.INVISIBLE);
        searchHistoryTextView.setVisibility(View.VISIBLE);
        searchHistoryTextView.setTextColor(
                getResources().getColor(R.color.primary_text_light_normal));
        searchHistoryTextView.setText(R.string.search_user_search_history_empty);
    }

    @Override
    public void showUsersSearchHistoryError() {
        searchHistoryRecyclerView.setVisibility(View.INVISIBLE);
        searchHistoryTextView.setVisibility(View.VISIBLE);
        searchHistoryTextView.setTextColor(
                getResources().getColor(R.color.error_text_light_normal));
        searchHistoryTextView.setText(R.string.search_user_search_history_error);
    }

    @Override
    public void showUserChallengesView(String username) {
        Intent intent = new Intent(this, UserChallengesActivity.class);
        intent.putExtra(UserChallengesActivity.EXTRA_USER_ID, username);
        startActivity(intent);
    }
}
