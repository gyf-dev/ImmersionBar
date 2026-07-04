package com.gyf.immersionbar;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Request manager retriever.
 *
 * @author geyifeng
 * @date 2019 /4/12 4:21 PM
 */
class RequestManagerRetriever implements Handler.Callback {

    private final String mTag = ImmersionBar.class.getName() + ".";

    private final String mNotOnly = ".tag.notOnly.";

    private final Handler mHandler;

    private static final int ID_REMOVE_FRAGMENT_MANAGER = 1;
    private static final int ID_REMOVE_SUPPORT_FRAGMENT_MANAGER = 2;
    private static final int ID_REMOVE_FRAGMENT_MANAGER_REMOVE = 3;
    private static final int ID_REMOVE_SUPPORT_FRAGMENT_MANAGER_REMOVE = 4;

    private static class Holder {
        private static final RequestManagerRetriever INSTANCE = new RequestManagerRetriever();
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    static RequestManagerRetriever getInstance() {
        return Holder.INSTANCE;
    }

    private RequestManagerRetriever() {
        mHandler = new Handler(Looper.getMainLooper(), this);
    }

    private final Map<android.app.FragmentManager, Map<String, RequestBarManagerFragment>> mPendingFragments = new HashMap<>();
    private final Map<FragmentManager, Map<String, SupportRequestBarManagerFragment>> mPendingSupportFragments = new HashMap<>();

    private final Map<String, RequestBarManagerFragment> mPendingRemoveFragments = new HashMap<>();
    private final Map<String, SupportRequestBarManagerFragment> mPendingSupportRemoveFragments = new HashMap<>();

    /**
     * Get immersion bar.
     *
     * @param activity the activity
     * @param isOnly   the is only
     * @return the immersion bar
     */
    public ImmersionBar get(Activity activity, boolean isOnly) {
        checkNotNull(activity, "activity is null");
        String tag = mTag;
        tag += activity.getClass().getName();
        if (!isOnly) {
            tag += System.identityHashCode(activity) + mNotOnly;
        }
        if (activity instanceof FragmentActivity) {
            return getSupportFragment(((FragmentActivity) activity).getSupportFragmentManager(), tag).get(activity);
        } else {
            return getFragment(activity.getFragmentManager(), tag).get(activity);
        }
    }

    /**
     * Get immersion bar.
     *
     * @param fragment the fragment
     * @param isOnly   the is only
     * @return the immersion bar
     */
    public ImmersionBar get(Fragment fragment, boolean isOnly) {
        checkNotNull(fragment, "fragment is null");
        checkNotNull(fragment.getActivity(), "fragment.getActivity() is null");
        if (fragment instanceof DialogFragment) {
            checkNotNull(((DialogFragment) fragment).getDialog(), "fragment.getDialog() is null");
        }
        String tag = mTag;
        tag += fragment.getClass().getName();
        if (!isOnly) {
            tag += System.identityHashCode(fragment) + mNotOnly;
        }
        return getSupportFragment(fragment.getChildFragmentManager(), tag).get(fragment);
    }


    /**
     * Get immersion bar.
     *
     * @param fragment the fragment
     * @param isOnly   the is only
     * @return the immersion bar
     */
    @RequiresApi(api = Version.JELLY_BEAN_MR1)
    public ImmersionBar get(android.app.Fragment fragment, boolean isOnly) {
        checkNotNull(fragment, "fragment is null");
        checkNotNull(fragment.getActivity(), "fragment.getActivity() is null");
        if (fragment instanceof android.app.DialogFragment) {
            checkNotNull(((android.app.DialogFragment) fragment).getDialog(), "fragment.getDialog() is null");
        }
        String tag = mTag;
        tag += fragment.getClass().getName();
        if (!isOnly) {
            tag += System.identityHashCode(fragment) + mNotOnly;
        }
        return getFragment(fragment.getChildFragmentManager(), tag).get(fragment);
    }

    /**
     * @param activity the activity
     * @param dialog   the dialog
     * @param isOnly   the is only
     * @return the immersion bar
     */
    public ImmersionBar get(Activity activity, Dialog dialog, boolean isOnly) {
        checkNotNull(activity, "activity is null");
        checkNotNull(dialog, "dialog is null");
        String tag = mTag;
        tag += dialog.getClass().getName();
        if (!isOnly) {
            tag += System.identityHashCode(dialog) + mNotOnly;
        }
        bindDialogAutoDestroy(activity, dialog, isOnly);
        if (activity instanceof FragmentActivity) {
            return getSupportFragment(((FragmentActivity) activity).getSupportFragmentManager(), tag).get(activity, dialog);
        } else {
            return getFragment(activity.getFragmentManager(), tag).get(activity, dialog);
        }
    }

    /**
     * 监听Dialog窗口detach，在Dialog消失时自动销毁，无需再手动调用destroy。
     * decorView的attach状态监听是累加的，不会覆盖业务自己设置的dismiss监听。
     *
     * @param activity the activity
     * @param dialog   the dialog
     * @param isOnly   the is only
     */
    private void bindDialogAutoDestroy(final Activity activity, final Dialog dialog, final boolean isOnly) {
        final Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        final View decorView = window.getDecorView();
        if (decorView.getTag(R.id.immersion_dialog_auto_destroy) != null) {
            return;
        }
        decorView.setTag(R.id.immersion_dialog_auto_destroy, Boolean.TRUE);
        decorView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                v.removeOnAttachStateChangeListener(this);
                v.setTag(R.id.immersion_dialog_auto_destroy, null);
                destroy(activity, dialog, isOnly);
            }
        });
    }

    public void destroy(Fragment fragment, boolean isOnly) {
        if (fragment == null) return;
        String tag = mTag;
        tag += fragment.getClass().getName();
        if (!isOnly) {
            tag += System.identityHashCode(fragment) + mNotOnly;
        }
        getSupportFragment(fragment.getChildFragmentManager(), tag, true);
    }

    @RequiresApi(api = Version.JELLY_BEAN_MR1)
    public void destroy(android.app.Fragment fragment, boolean isOnly) {
        if (fragment == null) {
            return;
        }
        String tag = mTag;
        tag += fragment.getClass().getName();
        if (!isOnly) {
            tag += System.identityHashCode(fragment) + mNotOnly;
        }
        getFragment(fragment.getChildFragmentManager(), tag, true);
    }

    /**
     * Destroy
     *
     * @param activity the activity
     * @param dialog   the dialog
     * @param isOnly   the isOnly
     */
    public void destroy(Activity activity, Dialog dialog, boolean isOnly) {
        if (activity == null || dialog == null) {
            return;
        }
        String tag = mTag;
        tag += dialog.getClass().getName();
        if (!isOnly) {
            tag += System.identityHashCode(dialog) + mNotOnly;
        }
        if (activity instanceof FragmentActivity) {
            getSupportFragment(((FragmentActivity) activity).getSupportFragmentManager(), tag, true);
        } else {
            getFragment(activity.getFragmentManager(), tag, true);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        boolean handled = true;
        switch (msg.what) {
            case ID_REMOVE_FRAGMENT_MANAGER:
                PendingFragment pendingFragment = (PendingFragment) msg.obj;
                removePendingFragment((android.app.FragmentManager) pendingFragment.fragmentManager, pendingFragment.tag);
                break;
            case ID_REMOVE_SUPPORT_FRAGMENT_MANAGER:
                PendingFragment pendingSupportFragment = (PendingFragment) msg.obj;
                removePendingSupportFragment((FragmentManager) pendingSupportFragment.fragmentManager, pendingSupportFragment.tag);
                break;
            case ID_REMOVE_FRAGMENT_MANAGER_REMOVE:
                String tag = (String) msg.obj;
                mPendingRemoveFragments.remove(tag);
                break;
            case ID_REMOVE_SUPPORT_FRAGMENT_MANAGER_REMOVE:
                String supportTag = (String) msg.obj;
                mPendingSupportRemoveFragments.remove(supportTag);
                break;
            default:
                handled = false;
                break;
        }
        return handled;
    }

    private RequestBarManagerFragment getFragment(android.app.FragmentManager fm, String tag) {
        return getFragment(fm, tag, false);
    }

    private RequestBarManagerFragment getPendingFragment(android.app.FragmentManager fm, String tag) {
        Map<String, RequestBarManagerFragment> pendingFragments = mPendingFragments.get(fm);
        return pendingFragments == null ? null : pendingFragments.get(tag);
    }

    private void putPendingFragment(android.app.FragmentManager fm, String tag, RequestBarManagerFragment fragment) {
        Map<String, RequestBarManagerFragment> pendingFragments = mPendingFragments.get(fm);
        if (pendingFragments == null) {
            pendingFragments = new HashMap<>();
            mPendingFragments.put(fm, pendingFragments);
        }
        pendingFragments.put(tag, fragment);
    }

    private void removePendingFragment(android.app.FragmentManager fm, String tag) {
        Map<String, RequestBarManagerFragment> pendingFragments = mPendingFragments.get(fm);
        if (pendingFragments != null) {
            pendingFragments.remove(tag);
            if (pendingFragments.isEmpty()) {
                mPendingFragments.remove(fm);
            }
        }
    }

    private SupportRequestBarManagerFragment getPendingSupportFragment(FragmentManager fm, String tag) {
        Map<String, SupportRequestBarManagerFragment> pendingFragments = mPendingSupportFragments.get(fm);
        return pendingFragments == null ? null : pendingFragments.get(tag);
    }

    private void putPendingSupportFragment(FragmentManager fm, String tag, SupportRequestBarManagerFragment fragment) {
        Map<String, SupportRequestBarManagerFragment> pendingFragments = mPendingSupportFragments.get(fm);
        if (pendingFragments == null) {
            pendingFragments = new HashMap<>();
            mPendingSupportFragments.put(fm, pendingFragments);
        }
        pendingFragments.put(tag, fragment);
    }

    private void removePendingSupportFragment(FragmentManager fm, String tag) {
        Map<String, SupportRequestBarManagerFragment> pendingFragments = mPendingSupportFragments.get(fm);
        if (pendingFragments != null) {
            pendingFragments.remove(tag);
            if (pendingFragments.isEmpty()) {
                mPendingSupportFragments.remove(fm);
            }
        }
    }

    private RequestBarManagerFragment getFragment(android.app.FragmentManager fm, String tag, boolean destroy) {
        RequestBarManagerFragment fragment = (RequestBarManagerFragment) fm.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = getPendingFragment(fm, tag);
            if (fragment == null) {
                if (destroy) {
                    return null;
                } else {
                    if (Build.VERSION.SDK_INT >= Version.O) {
                        for (android.app.Fragment fmFragment : fm.getFragments()) {
                            if (fmFragment instanceof RequestBarManagerFragment && fmFragment.getTag() == null) {
                                fm.beginTransaction().remove(fmFragment).commitAllowingStateLoss();
                            }
                        }
                    }
                }
                fragment = new RequestBarManagerFragment();
                putPendingFragment(fm, tag, fragment);
                fm.beginTransaction().add(fragment, tag).commitAllowingStateLoss();
                mHandler.obtainMessage(ID_REMOVE_FRAGMENT_MANAGER, new PendingFragment(fm, tag)).sendToTarget();
            }
        }
        if (destroy) {
            if (mPendingRemoveFragments.get(tag) == null) {
                mPendingRemoveFragments.put(tag, fragment);
                fm.beginTransaction().remove(fragment).commitAllowingStateLoss();
                mHandler.obtainMessage(ID_REMOVE_FRAGMENT_MANAGER_REMOVE, tag).sendToTarget();
            }
            return null;
        }
        return fragment;
    }

    private SupportRequestBarManagerFragment getSupportFragment(FragmentManager fm, String tag) {
        return getSupportFragment(fm, tag, false);
    }

    private SupportRequestBarManagerFragment getSupportFragment(FragmentManager fm, String tag, boolean destroy) {
        SupportRequestBarManagerFragment fragment = (SupportRequestBarManagerFragment) fm.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = getPendingSupportFragment(fm, tag);
            if (fragment == null) {
                if (destroy) {
                    return null;
                } else {
                    for (Fragment fmFragment : fm.getFragments()) {
                        if (fmFragment instanceof SupportRequestBarManagerFragment && fmFragment.getTag() == null) {
                            fm.beginTransaction().remove(fmFragment).commitAllowingStateLoss();
                        }
                    }
                }
                fragment = new SupportRequestBarManagerFragment();
                putPendingSupportFragment(fm, tag, fragment);
                fm.beginTransaction().add(fragment, tag).commitAllowingStateLoss();
                mHandler.obtainMessage(ID_REMOVE_SUPPORT_FRAGMENT_MANAGER, new PendingFragment(fm, tag)).sendToTarget();
            }
        }
        if (destroy) {
            if (mPendingSupportRemoveFragments.get(tag) == null) {
                mPendingSupportRemoveFragments.put(tag, fragment);
                fm.beginTransaction().remove(fragment).commitAllowingStateLoss();
                mHandler.obtainMessage(ID_REMOVE_SUPPORT_FRAGMENT_MANAGER_REMOVE, tag).sendToTarget();
            }
            return null;
        }
        return fragment;
    }

    private static <T> void checkNotNull(@Nullable T arg, @NonNull String message) {
        if (arg == null) {
            throw new NullPointerException(message);
        }
    }

    private static class PendingFragment {
        final Object fragmentManager;
        final String tag;

        PendingFragment(Object fragmentManager, String tag) {
            this.fragmentManager = fragmentManager;
            this.tag = tag;
        }
    }
}
