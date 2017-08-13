package com.lany.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class StateLayout extends FrameLayout {
    private final String TAG = "StateLayout";
    private View mContentView;
    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;
    private View mNetworkView;

    @StateLayout.State
    private int mState = State.CONTENT;

    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
        int CONTENT = 0;
        int ERROR = 1;
        int EMPTY = 2;
        int LOADING = 3;
        int NETWORK = 4;
    }

    private OnRetryClickListener mListener;

    public interface OnRetryClickListener {
        void onRetry(@State int state);
    }

    public void setOnRetryClickListener(OnRetryClickListener listener) {
        this.mListener = listener;
    }

    public StateLayout(Context context) {
        this(context, null);
    }

    public StateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public StateLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.StateLayout);
        int loadingViewResId = a.getResourceId(R.styleable.StateLayout_loadingView, R.layout.state_view_loading);
        int emptyViewResId = a.getResourceId(R.styleable.StateLayout_emptyView, R.layout.state_view_empty);
        int errorViewResId = a.getResourceId(R.styleable.StateLayout_errorView, R.layout.state_view_error);
        int networkViewResId = a.getResourceId(R.styleable.StateLayout_networkView, R.layout.state_view_network);
        int viewState = a.getInt(R.styleable.StateLayout_viewState, State.CONTENT);
        a.recycle();
        mState = viewState;
        mLoadingView = addStateView(loadingViewResId);

        mEmptyView = addStateView(emptyViewResId);
        mEmptyView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRetry(State.EMPTY);
                }
            }
        });

        mErrorView = addStateView(errorViewResId);
        mErrorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRetry(State.ERROR);
                }
            }
        });

        mNetworkView = addStateView(networkViewResId);
        mNetworkView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRetry(State.NETWORK);
                }
            }
        });
    }

    private View addStateView(@LayoutRes int layoutId) {
        View view = LayoutInflater.from(getContext()).inflate(layoutId, this, false);
        addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        return view;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mContentView == null)
            throw new IllegalArgumentException("Content view is not defined");
        setViewState(State.CONTENT);
    }

    @Override
    public void addView(View child) {
        getContentView(child);
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        getContentView(child);
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        getContentView(child);
        super.addView(child, index, params);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        getContentView(child);
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int width, int height) {
        getContentView(child);
        super.addView(child, width, height);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
        getContentView(child);
        return super.addViewInLayout(child, index, params);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        getContentView(child);
        return super.addViewInLayout(child, index, params, preventRequestLayout);
    }

    private void getContentView(View child) {
        if (!(mContentView != null && mContentView != child) && child != mLoadingView && child != mErrorView && child != mEmptyView && child != mNetworkView) {
            mContentView = child;
        }
    }

    private void setViewState(@State int state) {
        if (state == mState) {
            return;
        }
        mState = state;
        Log.i(TAG, "switchStateView: mState==" + mState);
        if (mLoadingView != null) {
            mLoadingView.setVisibility(mState == State.LOADING ? VISIBLE : GONE);
        } else {
            throw new NullPointerException("Loading View");
        }
        if (mErrorView != null) {
            mErrorView.setVisibility(mState == State.ERROR ? VISIBLE : GONE);
        } else {
            throw new NullPointerException("ErrorView View");
        }
        if (mEmptyView != null) {
            mEmptyView.setVisibility(mState == State.EMPTY ? VISIBLE : GONE);
        } else {
            throw new NullPointerException("EmptyView View");
        }
        if (mNetworkView != null) {
            mNetworkView.setVisibility(mState == State.NETWORK ? VISIBLE : GONE);
        } else {
            throw new NullPointerException("NetworkView View");
        }
        if (mContentView != null) {
            mContentView.setVisibility(mState == State.CONTENT ? VISIBLE : GONE);
        } else {
            throw new NullPointerException("ContentView View");
        }
    }


    private void setViewForState(View view, @State int state, boolean switchToState) {
        switch (state) {
            case State.LOADING:
                if (mLoadingView != null) removeView(mLoadingView);
                mLoadingView = view;
                addView(mLoadingView);
                break;
            case State.EMPTY:
                if (mEmptyView != null) removeView(mEmptyView);
                mEmptyView = view;
                addView(mEmptyView);
                break;
            case State.ERROR:
                if (mErrorView != null) removeView(mErrorView);
                mErrorView = view;
                addView(mErrorView);
                break;
            case State.CONTENT:
                if (mContentView != null) removeView(mContentView);
                mContentView = view;
                addView(mContentView);
                break;
            case State.NETWORK:
                if (mNetworkView != null)
                    removeView(mNetworkView);
                mNetworkView = view;
                addView(mNetworkView);
                break;
        }

        setViewState(State.CONTENT);
        if (switchToState)
            setViewState(state);
    }

    private void setViewForState(View view, @State int state) {
        setViewForState(view, state, false);
    }

    private void setViewForState(@LayoutRes int layoutRes, @State int state, boolean switchToState) {
        View view = LayoutInflater.from(getContext()).inflate(layoutRes, this, false);
        setViewForState(view, state, switchToState);
    }

    private void setViewForState(@LayoutRes int layoutRes, @State int state) {
        setViewForState(layoutRes, state, false);
    }

    public void showLoading() {
        setViewState(State.LOADING);
    }

    public void showContent() {
        setViewState(State.CONTENT);
    }

    public void showError() {
        setViewState(State.ERROR);
    }

    public void showNetwork() {
        setViewState(State.NETWORK);
    }

    public void showEmpty() {
        setViewState(State.EMPTY);
    }

    public void setEmptyView(@LayoutRes int layoutResId) {
        setViewForState(layoutResId, State.EMPTY, true);
    }

    public void setErrorView(@LayoutRes int layoutResId) {
        setViewForState(layoutResId, State.ERROR, true);
    }
}
