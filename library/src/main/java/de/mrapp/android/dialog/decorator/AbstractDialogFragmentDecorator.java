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
package de.mrapp.android.dialog.decorator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.View;

import de.mrapp.android.dialog.model.Dialog;
import de.mrapp.android.dialog.model.DialogDecorator;

import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * An abstract base class for all decorators, which allow to modify the view hierarchy of a dialog,
 * which is able to show fragments.
 *
 * @param <DialogType>
 *         The type of the dialog, whose view hierarchy is modified by the decorator
 * @author Michael Rapp
 * @since 3.2.0
 */
public abstract class AbstractDialogFragmentDecorator<DialogType extends Dialog>
        implements DialogDecorator {

    /**
     * The dialog, whose view hierarchy is modified by the decorator.
     */
    private final DialogType dialog;

    /**
     * The root view of the view hierarchy, which is modified by the decorator.
     */
    private View view;

    /**
     * The method, which is invoked, when the decorator is attached to the view hierarchy.
     *
     * @param view
     *         The root view of the dialog, which is modified by the decorator, as an instance of
     *         the class {@link View}. The view may not be null
     * @param fragmentManager
     *         The fragment manager, which should be used by the decorator, as an instance of the
     *         class FragmentManager. The fragment manager may not be null
     */
    protected abstract void onAttach(@NonNull final View view,
                                     @NonNull final FragmentManager fragmentManager);

    /**
     * The method, which is invoked, when the decorator is detached from the view hierarchy.
     */
    protected abstract void onDetach();

    /**
     * Creates a new decorator, which allows to modify the view hierarchy of a dialog.
     *
     * @param dialog
     *         The dialog, whose view hierarchy should be modified by the decorator, as an instance
     *         of the generic type DialogType. The dialog may not be null
     */
    public AbstractDialogFragmentDecorator(@NonNull final DialogType dialog) {
        ensureNotNull(dialog, "The dialog may not be null");
        this.dialog = dialog;
        this.view = null;
    }

    /**
     * Returns the dialog, whose view hierarchy is modified by the decorator.
     *
     * @return The dialog, whose view hierarchy is modified by the decorator, as an instance of the
     * generic type DialogType. The dialog may not be null
     */
    @NonNull
    protected final DialogType getDialog() {
        return dialog;
    }

    /**
     * Returns the root view of the view hierarchy, which is modified by the decorator.
     *
     * @return The root view of the view hierarchy, which is modified by the decorator, as an
     * instance of the class {@link View} or null, if the decorator is not attached
     */
    @Nullable
    protected final View getView() {
        return view;
    }

    @Override
    public final Context getContext() {
        return dialog.getContext();
    }

    /**
     * Attaches the decorator to the view hierarchy. This enables the decorator to modify the view
     * hierarchy until it is detached.
     *
     * @param view
     *         The root view of the view hierarchy, which should be modified by the decorator, as an
     *         instance of the class {@link View}. The view may not be null
     * @param fragmentManager
     *         The fragment manager, which should be used by the decorator, as an instance of the
     *         class FragmentManager. The fragment manager may not be null
     */
    public final void attach(@NonNull final View view,
                             @NonNull final FragmentManager fragmentManager) {
        ensureNotNull(view, "The view may not be null");
        ensureNotNull(fragmentManager, "The fragment manager may not be null");
        this.view = view;
        onAttach(view, fragmentManager);
    }

    /**
     * Detaches the decorator from the view hierarchy. This prevents the decorator from modifying
     * the view hierarchy until it is attached again.
     */
    public final void detach() {
        if (this.view != null) {
            this.view = null;
            onDetach();
        }
    }

}