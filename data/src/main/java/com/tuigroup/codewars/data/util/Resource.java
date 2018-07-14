package com.tuigroup.codewars.data.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.tuigroup.codewars.data.util.Status.LOCAL;
import static com.tuigroup.codewars.data.util.Status.REMOTE;

/**
 * A generic class that holds a value with its loading status.
 *
 * @param <T>
 */
public class Resource<T> {
    @NonNull
    public final Status status;
    @Nullable
    public final T data;

    private Resource(@NonNull Status status, @Nullable T data) {
        this.status = status;
        this.data = data;
    }

    public static <T> Resource<T> remote(@NonNull T data) {
        return new Resource<>(REMOTE, data);
    }

    public static <T> Resource<T> local(@Nullable T data) {
        return new Resource<>(LOCAL, data);
    }
}