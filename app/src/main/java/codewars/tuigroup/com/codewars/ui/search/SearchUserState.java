package codewars.tuigroup.com.codewars.ui.search;

import com.tuigroup.codewars.data.local.model.UserEntity;
import com.tuigroup.codewars.data.util.UserOrderBy;

import codewars.tuigroup.com.codewars.ui.util.RequestResultType;

public class SearchUserState implements SearchUserContract.State {

    private UserOrderBy orderBy;
    private UserEntity user;
    private RequestResultType resultType;


    public SearchUserState(UserOrderBy orderBy, UserEntity user, RequestResultType resultType) {
        this.orderBy = orderBy;
        this.user = user;
        this.resultType = resultType;
    }

    @Override
    public UserOrderBy getUsersSearchedOrderBy() {
        return orderBy;
    }

    @Override
    public UserEntity getUserFound() {
        return user;
    }

    @Override
    public RequestResultType getSearchUserResultType() {
        return resultType;
    }
}
