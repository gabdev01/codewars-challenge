package codewars.tuigroup.com.codewars.ui.search;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuigroup.codewars.data.UsersRepository;
import com.tuigroup.codewars.data.local.model.UserEntity;
import com.tuigroup.codewars.data.remote.CodewarsRestProvider;
import com.tuigroup.codewars.data.remote.UsersRestApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import codewars.tuigroup.com.codewars.CodewarsApplication;
import codewars.tuigroup.com.codewars.R;

import static com.google.common.base.Preconditions.checkNotNull;

public class SearchUserActivity extends AppCompatActivity implements View.OnClickListener, SearchUserContract.View {

    private SearchUserContract.Presenter searchUserPresenter;

    @BindView(R.id.edittext_searchuser_toolbarsearch)
    EditText searchEditText;
    @BindView(R.id.imageview_usersearch_clearsearch)
    ImageView clearSearchImageView;
    @BindView(R.id.textview_searchuseritem_leaderboardposition)
    TextView leaderboardPositionTextView;
    @BindView(R.id.textview_searchuseritem_username)
    TextView usernameTextView;
    @BindView(R.id.textview_searchuseritem_clan)
    TextView clanTextView;
    @BindView(R.id.textview_searchuseritem_honor)
    TextView honorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        View customToolBarView = LayoutInflater.from(this).inflate(R.layout.view_search_user_toolbar, null);
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

        clearSearchImageView.setOnClickListener(this);

        searchUserPresenter = new SearchUserPresenter(UsersRepository.getInstance(
                CodewarsRestProvider.getRestApi(UsersRestApi.class),
                CodewarsApplication.from(this).getDatabase().usersDao()));
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
        }
    }

    private void performSearch() {
        searchEditText.setCursorVisible(false);
        String searchText = searchEditText.getText().toString();
        searchUserPresenter.searchUser(searchText);
    }

    private void performClearSearch() {
        searchEditText.getText().clear();
    }

    @Override
    public void setPresenter(SearchUserContract.Presenter presenter) {
        searchUserPresenter = checkNotNull(presenter);
    }

    @Override
    public void showSearchUserIndicator(boolean enabled) {

    }

    @Override
    public void showSearchUserError() {

    }

    @Override
    public void showSearchUserNoInternetError() {

    }

    @Override
    public void showSearchUserSuccess(UserEntity user) {
        leaderboardPositionTextView.setText(
                String.format(getString(R.string.search_user_item_leaderboard_position), user.getLeaderboardPosition()));
        usernameTextView.setText(user.getUserName());
        clanTextView.setText(user.getClan());
        honorTextView.setText(String.valueOf(user.getHonor()));
    }
}
