package com.tuigroup.codewars.data.util;

import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public abstract class NetworkBoundSource<LocalType, RemoteType> {

    private FlowableEmitter<Resource<LocalType>> emitter;
    private Disposable firstDataDisposable;

    public NetworkBoundSource(FlowableEmitter<Resource<LocalType>> emitter) {
        this.emitter = emitter;
        this.firstDataDisposable = getLocal()
                .map(Resource::local)
                .subscribe(
                        result -> {
                            firstDataDisposable.dispose();
                            emitter.onNext(result);
                            requestRemote();
                        }, throwable -> {
                            firstDataDisposable.dispose();
                            emitter.onError(throwable);
                            requestRemote();
                        });
    }

    private void requestRemote() {
        getRemote().map(mapper())
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(
                        localTypeData -> {
                            saveCallResult(localTypeData);
                            getLocal().map(Resource::remote).subscribe(
                                    emitter::onNext, emitter::onError);
                        }, emitter::onError);
    }

    public abstract Single<RemoteType> getRemote();

    public abstract Flowable<LocalType> getLocal();

    public abstract void saveCallResult(LocalType data);

    public abstract Function<RemoteType, LocalType> mapper();

}