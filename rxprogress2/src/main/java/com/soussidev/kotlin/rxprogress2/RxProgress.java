package com.soussidev.kotlin.rxprogress2;

import org.reactivestreams.Publisher;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.annotation.StringRes;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by Soussi on 12/10/2017.
 */

public final class RxProgress {

    public static Builder from(@NonNull Activity activity) {
        return new Builder(activity);
    }

    private final Builder builder;

    private RxProgress(Builder builder) {
        this.builder = builder;
    }

    private <T> Flowable<T> forFlowable(Flowable<T> source, BackpressureStrategy backpressureStrategy) {
        return Flowable.using(this::makeDialog,
                new Function<ProgressDialog, Publisher<? extends T>>() {
                    @Override
                    public Publisher<? extends T> apply(@NonNull ProgressDialog dialog) throws Exception {
                        return Flowable.create(emitter -> {
                            if (builder.cancelable) {
                                dialog.setOnCancelListener(dialogInterface -> emitter.onComplete());
                            }
                            dialog.setOnDismissListener(dialogInterface -> emitter.onComplete());
                            source.subscribe(emitter::onNext, emitter::onError, emitter::onComplete);
                        }, backpressureStrategy);
                    }
                }, Dialog::dismiss);
    }

    private <T> Observable<T> forObservable(Observable<T> source) {
        return Observable.using(this::makeDialog,
                new Function<ProgressDialog, ObservableSource<? extends T>>() {
                    @Override
                    public ObservableSource<? extends T> apply(@NonNull ProgressDialog dialog) throws Exception {
                        return Observable.create(emitter -> {
                            if (builder.cancelable) {
                                dialog.setOnCancelListener(dialogInterface -> emitter.onComplete());
                            }
                            dialog.setOnDismissListener(dialogInterface -> emitter.onComplete());
                            source.subscribe(emitter::onNext, emitter::onError, emitter::onComplete);
                        });
                    }
                }, Dialog::dismiss);
    }

    private ProgressDialog makeDialog() {
        return ProgressDialog.show(builder.activity, builder.title, builder.message, builder.indeterminate, builder.cancelable);
    }

    public static class Builder {

        private final @NonNull Activity activity;

        private boolean cancelable;

        private boolean indeterminate;

        private CharSequence message;

        private CharSequence title;

        public Builder(@NonNull Activity activity) {
            this.activity = activity;
            this.indeterminate = true;
            this.message = activity.getString(R.string.loading);
        }

        public <T> Flowable<T> forFlowable(Flowable<T> source, BackpressureStrategy backpressureStrategy) {
            return new RxProgress(this).forFlowable(source, backpressureStrategy);
        }

        public <T> Observable<T> forObservable(Observable<T> source) {
            return new RxProgress(this).forObservable(source);
        }

        public Builder withCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder withIndeterminate(boolean indeterminate) {
            this.indeterminate = indeterminate;
            return this;
        }

        public Builder withMessage(@StringRes int message) {
            this.message = activity.getString(message);
            return this;
        }

        public Builder withMessage(CharSequence message) {
            this.message = message;
            return this;
        }

        public Builder withTitle(CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder withTitle(@StringRes int title) {
            this.title = activity.getString(title);
            return this;
        }
    }
}
