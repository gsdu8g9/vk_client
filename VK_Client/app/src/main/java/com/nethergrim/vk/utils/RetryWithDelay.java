package com.nethergrim.vk.utils;

import com.nethergrim.vk.web.WebRequestManagerImpl;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author Andrew Drobyazko (c2q9450@gmail.com) on 05.09.15.
 */
public class RetryWithDelay
        implements Func1<Observable<? extends Throwable>, Observable<?>> {

    private final int _maxRetries;
    private final int _retryDelayMillis;
    private int _retryCount;

    public static RetryWithDelay getInstance(){
        return new RetryWithDelay(WebRequestManagerImpl.MAX_RETRY_COUNT, WebRequestManagerImpl.MIN_RETRY_DELAY_MS);
    }

    public RetryWithDelay(final int maxRetries, final int retryDelayMillis) {
        _maxRetries = maxRetries;
        _retryDelayMillis = retryDelayMillis;
        _retryCount = 0;
    }

    @Override
    public Observable<?> call(Observable<? extends Throwable> attempts) {
        return attempts.flatMap(throwable -> {
            if (++_retryCount < _maxRetries) {
                // When this Observable calls onNext, the original
                // Observable will be retried (i.e. re-subscribed).

                return Observable.timer(_retryCount * _retryDelayMillis,
                        TimeUnit.MILLISECONDS);
            }

            // Max retries hit. Just pass the error along.
            return Observable.error(throwable);
        });
    }
}