package com.gyf.immersionbar;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

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

    private final Map<android.app.FragmentManager, RequestBarManagerFragment> mPendingFragments = new HashMap<>();
    private final Map<FragmentManager, SupportRequestBarManagerFragment> mPendingSupportFragments = new HashMap<>();

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
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
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
        if (activity instanceof FragmentActivity) {
            return getSupportFragment(((FragmentActivity) activity).getSupportFragmentManager(), tag).get(activity, dialog);
        } else {
            return getFragment(activity.getFragmentManager(), tag).get(activity, dialog);
        }
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
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
                android.app.FragmentManager fm = (android.app.FragmentManager) msg.obj;
                mPendingFragments.remove(fm);
                break;
            case ID_REMOVE_SUPPORT_FRAGMENT_MANAGER:
                FragmentManager supportFm = (FragmentManager) msg.obj;
                mPendingSupportFragments.remove(supportFm);
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

    private RequestBarManagerFragment getFragment(android.app.FragmentManager fm, String tag, boolean destroy) {
        RequestBarManagerFragment fragment = (RequestBarManagerFragment) fm.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = mPendingFragments.get(fm);
            if (fragment == null) {
                if (destroy) {
                    return null;
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        for (android.app.Fragment fmFragment : fm.getFragments()) {
                            if (fmFragment instanceof RequestBarManagerFragment) {
                                String oldTag = fmFragment.getTag();
                                if (oldTag == null) {
                                    fm.beginTransaction().remove(fmFragment).commitAllowingStateLoss();
                                } else {
                                    if (oldTag.contains(mNotOnly)) {
                                        fm.beginTransaction().remove(fmFragment).commitAllowingStateLoss();
                                    }
                                }
                            }
                        }
                    }
                }
                fragment = new RequestBarManagerFragment();
                mPendingFragments.put(fm, fragment);
                fm.beginTransaction().add(fragment, tag).commitAllowingStateLoss();
                mHandler.obtainMessage(ID_REMOVE_FRAGMENT_MANAGER, fm).sendToTarget();
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
            fragment = mPendingSupportFragments.get(fm);
            if (fragment == null) {
                if (destroy) {
                    return null;
                } else {
                    for (Fragment fmFragment : fm.getFragments()) {
                        if (fmFragment instanceof SupportRequestBarManagerFragment) {
                            String oldTag = fmFragment.getTag();
                            if (oldTag == null) {
                                fm.beginTransaction().remove(fmFragment).commitAllowingStateLoss();
                            } else {
                                if (oldTag.contains(mNotOnly)) {
                                    fm.beginTransaction().remove(fmFragment).commitAllowingStateLoss();
                                }
                            }
                        }
                    }
                }
                fragment = new SupportRequestBarManagerFragment();
                mPendingSupportFragments.put(fm, fragment);
                fm.beginTransaction().add(fragment, tag).commitAllowingStateLoss();
                mHandler.obtainMessage(ID_REMOVE_SUPPORT_FRAGMENT_MANAGER, fm).sendToTarget();
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
}
