package com.gyf.immersionbar.simple.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.simple.OnSplashListener;
import com.gyf.immersionbar.simple.R;
import com.gyf.immersionbar.simple.utils.GlideUtils;
import com.gyf.immersionbar.simple.utils.Utils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author geyifeng
 * @date 2019-04-22 15:27
 */
public class SplashFragment extends BaseFragment {

    @BindView(R.id.iv_splash)
    ImageView ivSplash;
    @BindView(R.id.tv_time)
    TextView tvTime;

    private static String mKey = "TotalTime";

    private String mTag = this.getClass().getSimpleName();

    private long mTotalTime = 3;

    private Disposable mSubscribe;
    private OnSplashListener mOnSplashListener;

    @Override
    public void onDestroy() {
        super.onDestroy();
        finish();
    }

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    public static SplashFragment newInstance(long totalTime) {
        SplashFragment splashFragment = new SplashFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(mKey, totalTime);
        splashFragment.setArguments(bundle);
        return splashFragment;
    }

    @Override
    protected void initDataBeforeView(Bundle savedInstanceState) {
        super.initDataBeforeView(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mTotalTime = arguments.getLong(mKey);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_splash;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        super.initView();
        ImmersionBar.setTitleBar(mActivity, tvTime);
        GlideUtils.load(Utils.getFullPic(), ivSplash, R.drawable.pic_all);
        Observable.interval(1, TimeUnit.SECONDS)
                .map(aLong -> mTotalTime - aLong)
                .take(mTotalTime + 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mSubscribe = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        tvTime.setText("我是" + aLong + "s欢迎页，点我可以关闭");
                        if (mOnSplashListener != null) {
                            mOnSplashListener.onTime(aLong, mTotalTime);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        finish();
                    }

                    @Override
                    public void onComplete() {
                        finish();
                    }
                });
    }

    @Override
    protected void setListener() {
        super.setListener();
        tvTime.setOnClickListener(v -> {
            finish();
        });
    }

    private void finish() {
        disposedSubscribe();
        if (getFragmentManager() != null) {
            Fragment fragment = getFragmentManager().findFragmentByTag(mTag);
            if (fragment != null) {
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out)
                        .remove(fragment)
                        .commitAllowingStateLoss();
            }
            mOnSplashListener = null;
        }
    }

    private void disposedSubscribe() {
        if (mSubscribe != null && !mSubscribe.isDisposed()) {
            mSubscribe.dispose();
            mSubscribe = null;
        }
    }

    public void setOnSplashListener(OnSplashListener onSplashListener) {
        this.mOnSplashListener = onSplashListener;
    }

    @Override
    public boolean immersionBarEnabled() {
        return false;
    }
}
