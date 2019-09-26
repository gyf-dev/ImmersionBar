package com.gyf.immersionbar.sample.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.OnSplashListener;
import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.utils.GlideUtils;
import com.gyf.immersionbar.sample.utils.Utils;

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
public class SplashFragment extends BaseFragment implements Observer<Long> {

    @BindView(R.id.iv_splash)
    ImageView ivSplash;
    @BindView(R.id.tv_time)
    TextView tvTime;

    private static String mKey = "TotalTime";

    private long mTotalTime = 3;

    private Disposable mSubscribe;
    private OnSplashListener mOnSplashListener;

    private boolean mIsFinish = false;

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopCountDown();
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

    @Override
    protected void initData() {
        super.initData();
        startCountDown();
    }

    @Override
    protected void initView() {
        super.initView();
        ImmersionBar.setTitleBar(mActivity, tvTime);
        GlideUtils.load(Utils.getFullPic(), ivSplash, R.drawable.pic_all);
    }

    @Override
    protected void setListener() {
        super.setListener();
        tvTime.setOnClickListener(v -> {
            if (mOnSplashListener != null) {
                mOnSplashListener.onTime(0, mTotalTime);
            }
            finish();
        });
    }

    /**
     * 关闭当前页面
     */
    private void finish() {
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
        mIsFinish = true;
    }

    /**
     * 开启倒计时
     */
    private void startCountDown() {
        Observable.interval(1, TimeUnit.SECONDS)
                .map(aLong -> mTotalTime - aLong)
                .take(mTotalTime + 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }

    /**
     * 关闭倒计时
     */
    private void stopCountDown() {
        if (mSubscribe != null && !mSubscribe.isDisposed()) {
            mSubscribe.dispose();
            mSubscribe = null;
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        mSubscribe = d;
    }

    @SuppressLint("SetTextI18n")
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

    public void setOnSplashListener(OnSplashListener onSplashListener) {
        this.mOnSplashListener = onSplashListener;
    }

    public boolean isFinish() {
        return mIsFinish;
    }
}
