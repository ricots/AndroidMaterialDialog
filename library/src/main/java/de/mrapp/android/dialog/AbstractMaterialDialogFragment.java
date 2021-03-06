/*
 * Copyright 2014 - 2016 Michael Rapp
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package de.mrapp.android.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import de.mrapp.android.dialog.decorator.MaterialDialogDecorator;
import de.mrapp.android.dialog.model.MaterialDialog;
import de.mrapp.android.util.DisplayUtil;

import static de.mrapp.android.util.Condition.ensureNotNull;
import static de.mrapp.android.util.DisplayUtil.getDeviceType;
import static de.mrapp.android.util.DisplayUtil.getOrientation;

/**
 * An abstract base class for all dialogs, which are designed according to Android 5's Material
 * design guidelines even on pre-Lollipop devices and are able to show fragments.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public abstract class AbstractMaterialDialogFragment extends DialogFragment
        implements MaterialDialog {

    /**
     * The decorator, which is used by the dialog.
     */
    private final MaterialDialogDecorator decorator;

    /**
     * The resource id of the theme, which should be used by the dialog.
     */
    private int themeResourceId;

    /**
     * The context, which is used by the dialog.
     */
    private Context context;

    /**
     * The listener, which should be notified, when the dialog has been shown.
     */
    private OnShowListener showListener;

    /**
     * The listener, which is notified, when the dialog has been dismissed.
     */
    private OnDismissListener dismissListener;

    /**
     * The listener, which is notified, when the dialog has been cancelled.
     */
    private OnCancelListener cancelListener;

    /**
     * Inflates the dialog's root view.
     *
     * @return The view, which has been inflated, as an instance of the class {@link View}. The view
     * may not be null
     */
    private View inflateLayout() {
        return View.inflate(getContext(), R.layout.material_dialog, null);
    }

    /**
     * Creates and returns the layout params, which should be used by the dialog.
     *
     * @param window
     *         The dialog's window as an instance of the class {@link Window}. The window may not be
     *         null
     * @return The layout params, which should be used by the dialog, as an instance of the class
     * {@link WindowManager.LayoutParams}
     */
    private WindowManager.LayoutParams createLayoutParams(@NonNull final Window window) {
        WindowManager.LayoutParams layoutParams = window.getAttributes();

        if (getDeviceType(getContext()) == DisplayUtil.DeviceType.PHONE &&
                getOrientation(getContext()) == DisplayUtil.Orientation.PORTRAIT) {
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        }

        return layoutParams;
    }

    /**
     * Sets the resource id of the theme, which should be used by the dialog.
     *
     * @param themeResourceId
     *         The resource id of the theme, which should be used by the dialog, as an {@link
     *         Integer} value. The resource id must correspond to a valid theme
     */
    protected final void setThemeResourceId(@StyleRes final int themeResourceId) {
        this.themeResourceId = themeResourceId;
    }

    /**
     * Sets the context, which should be used by the dialog.
     *
     * @param context
     *         The context, which should be set, as an instance of the class {@link Context}. The
     *         context may not be null
     */
    protected final void setContext(@NonNull final Context context) {
        ensureNotNull(context, "The context may not be null");
        this.context = context;
    }

    /**
     * Creates a dialog, which is designed according to Android 5's Material Design guidelines even
     * on pre-Lollipop devices and is able to show fragments.
     */
    public AbstractMaterialDialogFragment() {
        this.decorator = new MaterialDialogDecorator(this);
    }

    /**
     * The method, which is invoked when the dialog's decorators should be attached. This method may
     * be overridden by subclasses in order to attach additional decorators.
     *
     * @param view
     *         The root view of the view hierarchy, which should be modified by the decorators, as
     *         an instance of the class {@link View}. The view may not be null
     * @param fragmentManager
     *         The fragment manager, which can be used to show fragment within the dialog, as an
     *         instance of the class FragmentManager. The fragment manager may not be null
     */
    @CallSuper
    protected void onAttachDecorators(@NonNull final View view,
                                      @NonNull final FragmentManager fragmentManager) {
        decorator.attach(view);
    }

    /**
     * The method, which is invoked when the dialog's decorators should be detached. This method
     * must be overridden by subclasses if they attach additional decorators.
     */
    @CallSuper
    protected void onDetachDecorators() {
        decorator.detach();
    }

    /**
     * The method, which is invoked when the dialog is re-created in order to restore its state.
     * This method may be overridden by subclasses in order to restore the properties, which are
     * stored in the <code>onSaveInstanceState</code>-method.
     *
     * @param savedInstanceState
     *         The bundle, which has been used to store the state, as an instance of the class
     *         {@link Bundle}. The bundle may not be null
     */
    @CallSuper
    protected void onRestoreInstanceState(@NonNull final Bundle savedInstanceState) {
        decorator.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public final void setOnShowListener(@Nullable final OnShowListener listener) {
        showListener = listener;
    }

    @Override
    public final void setOnCancelListener(@Nullable final OnCancelListener listener) {
        cancelListener = listener;
    }

    @Override
    public void setOnDismissListener(@Nullable final OnDismissListener listener) {
        dismissListener = listener;
    }

    @Override
    public final Drawable getIcon() {
        return decorator.getIcon();
    }

    @Override
    public final void setIcon(@Nullable final Bitmap icon) {
        decorator.setIcon(icon);
    }

    @Override
    public final void setIcon(@DrawableRes final int resourceId) {
        decorator.setIcon(resourceId);
    }

    @Override
    public final void setIconAttribute(@AttrRes final int attributeId) {
        decorator.setIconAttribute(attributeId);
    }

    @Override
    public final int getTitleColor() {
        return decorator.getTitleColor();
    }

    @Override
    public final void setTitleColor(@ColorInt final int color) {
        decorator.setTitleColor(color);
    }

    @Override
    public final int getMessageColor() {
        return decorator.getMessageColor();
    }

    @Override
    public final void setMessageColor(@ColorInt final int color) {
        decorator.setMessageColor(color);
    }

    @Override
    public final Drawable getBackground() {
        return decorator.getBackground();
    }

    @Override
    public final void setBackground(@Nullable final Bitmap background) {
        decorator.setBackground(background);
    }

    @Override
    public final void setBackground(@DrawableRes final int resourceId) {
        decorator.setBackground(resourceId);
    }

    @Override
    public final void setBackgroundColor(@ColorInt final int color) {
        decorator.setBackgroundColor(color);
    }

    @Override
    public final void setCustomTitle(@Nullable final View view) {
        decorator.setCustomTitle(view);
    }

    @Override
    public final void setCustomTitle(@LayoutRes final int resourceId) {
        decorator.setCustomTitle(resourceId);
    }

    @Override
    public final void setCustomMessage(@Nullable final View view) {
        decorator.setCustomMessage(view);
    }

    @Override
    public final void setCustomMessage(@LayoutRes final int resourceId) {
        decorator.setCustomMessage(resourceId);
    }

    @Override
    public final void setView(@Nullable final View view) {
        decorator.setView(view);
    }

    @Override
    public final void setView(@LayoutRes final int resourceId) {
        decorator.setView(resourceId);
    }

    @Override
    public final CharSequence getMessage() {
        return decorator.getMessage();
    }

    @Override
    public final void setMessage(@Nullable final CharSequence message) {
        decorator.setMessage(message);
    }

    @Override
    public final void setMessage(@StringRes final int resourceId) {
        decorator.setMessage(resourceId);
    }

    @Override
    public final CharSequence getTitle() {
        return decorator.getTitle();
    }

    @Override
    public final void setTitle(@Nullable final CharSequence title) {
        decorator.setTitle(title);
    }

    @Override
    public final void setTitle(@StringRes final int resourceId) {
        decorator.setTitle(resourceId);
    }

    @Override
    public final void cancel() {
        if (getDialog() != null) {
            getDialog().cancel();
        }
    }

    @Override
    public final Context getContext() {
        Context context = super.getContext();

        if (context == null) {
            ensureNotNull(this.context, "No context has been set", IllegalStateException.class);
            context = this.context;
        }

        return context;
    }

    @NonNull
    @Override
    public final Dialog onCreateDialog(final Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext(), themeResourceId);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setAttributes(createLayoutParams(dialog.getWindow()));
        return dialog;
    }

    @Override
    public final View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                                   final Bundle savedInstanceState) {

        View view = inflateLayout();

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        onAttachDecorators(view, getChildFragmentManager());
        return view;
    }

    @Override
    public final void onStart() {
        super.onStart();

        if (showListener != null) {
            showListener.onShow(getDialog());
        }
    }

    @Override
    public final void onDestroy() {
        super.onDestroy();
        onDetachDecorators();
    }

    @Override
    public final void onDestroyView() {
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }

        super.onDestroyView();
    }

    @Override
    public final void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);

        if (dismissListener != null) {
            dismissListener.onDismiss(dialog);
        }
    }

    @Override
    public final void onCancel(final DialogInterface dialog) {
        super.onCancel(dialog);

        if (cancelListener != null) {
            cancelListener.onCancel(dialog);
        }
    }

    @CallSuper
    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        decorator.onSaveInstanceState(outState);
    }

}