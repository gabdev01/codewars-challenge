package com.tuigroup.codewars.data.paging;

import android.support.annotation.NonNull;

import com.tuigroup.codewars.data.local.CompletedChallengeDao;
import com.tuigroup.codewars.data.local.model.CompletedChallengeEntity;
import com.tuigroup.codewars.data.mapper.CompletedChallengeMapper;
import com.tuigroup.codewars.data.remote.UserRestApi;
import com.tuigroup.codewars.data.remote.model.CompletedChallengeResponse;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CompletedChallengesBoundaryCallback extends ObservableBoundaryCallback<CompletedChallengeEntity, String> {

    private UserRestApi userRestApi;
    private CompletedChallengeDao completedChallengeDao;

    private String username;
    private int pageIndex;
    private boolean isRequestRunning;
    private boolean isAllDataLoaded;

    private BoundaryCallbackRequestListener boundaryCallbackRequestListener;

    @Inject
    public CompletedChallengesBoundaryCallback(UserRestApi userRestApi,
                                               CompletedChallengeDao completedChallengeDao) {
        this.userRestApi = userRestApi;
        this.completedChallengeDao = completedChallengeDao;
        this.username = null;
        this.pageIndex = 0;
        this.isRequestRunning = false;
        this.isAllDataLoaded = false;
    }

    @Override
    public void setBoundaryCallbackRequestListener(BoundaryCallbackRequestListener listener) {
        boundaryCallbackRequestListener = listener;
    }

    @Override
    public void retryRequest() {
        getAndSaveData(pageIndex);
    }

    @Override
    public void setParameter(String parameter) {
        this.username = parameter;
    }

    private void performAllDataLoaded() {
        if (boundaryCallbackRequestListener != null) {
            boundaryCallbackRequestListener.onAllDataLoaded();
        }
    }

    private void performRequestInProgress(boolean inProgress) {
        if (boundaryCallbackRequestListener != null) {
            boundaryCallbackRequestListener.onRequestInProgress(inProgress);
        }
    }

    private void performRequestError(Throwable throwable) {
        if (boundaryCallbackRequestListener != null) {
            boundaryCallbackRequestListener.onRequestError(throwable);
        }
    }

    @Override
    public void onZeroItemsLoaded() {
        pageIndex = 0;
        getAndSaveData(0);
    }

    @Override
    public void onItemAtEndLoaded(@NonNull CompletedChallengeEntity itemAtEnd) {
        pageIndex = itemAtEnd.getPageIndexInResponse() + 1;
        getAndSaveData(pageIndex);
    }

    private void getAndSaveData(int pageIndex) {
        if (isRequestRunning || isAllDataLoaded) {
            return;
        }
        isRequestRunning = true;
        performRequestInProgress(isRequestRunning);
        getDataFromApi(pageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(challengesResponse -> {
                    isRequestRunning = false;
                    performRequestInProgress(isRequestRunning);

                    isAllDataLoaded = (pageIndex + 1) >= challengesResponse.getTotalPages();
                    if (isAllDataLoaded) {
                        performAllDataLoaded();
                    }
                })
                .doOnError(throwable -> {
                    isRequestRunning = false;
                    performRequestInProgress(isRequestRunning);
                    performRequestError(throwable);
                })
                .subscribe();
    }

    private Single<CompletedChallengeResponse> getDataFromApi(int pageIndex) {
        return userRestApi.getCompletedChallenges(username, pageIndex)
                .map(challengesResponse -> {
                            completedChallengeDao.insertAll(CompletedChallengeMapper.mapFromApiToEntity(
                                    challengesResponse.getData(),
                                    username,
                                    pageIndex));
                            return challengesResponse;
                        }
                );
    }
}