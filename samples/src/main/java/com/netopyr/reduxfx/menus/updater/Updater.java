package com.netopyr.reduxfx.menus.updater;

import com.netopyr.reduxfx.menus.actions.Action;
import com.netopyr.reduxfx.menus.actions.AlertWasClosedAction;
import com.netopyr.reduxfx.menus.actions.OpenAlertAction;
import com.netopyr.reduxfx.menus.actions.OpenModalAlertAction;
import com.netopyr.reduxfx.menus.state.AppModel;
import javafx.stage.Modality;
import javaslang.API;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static javaslang.API.$;
import static javaslang.API.Case;
import static javaslang.API.Match;
import static javaslang.Predicates.instanceOf;

/**
 * The {@code Updater} is the heart of every ReduxFX-application. This is where the main application logic resides.
 * <p>
 * An {@code Updater} consists of a single function ({@link #update(AppModel, Object)} in this class), which takes
 * the current state (an instance of {@link AppModel}) and an {@link Action} and calculates the new state from that.
 * <p>
 * Please note that {@code Updater} has no internal state. Everything that is needed for {@code update} is passed in
 * the parameters.
 */
public class Updater {

    private static final Logger LOG = LoggerFactory.getLogger(Updater.class);

    private Updater() {
    }

    /**
     * The method {@code update} is the central piece of the TodoMVC-application. The whole application logic is
     * implemented here.
     * <p>
     * This method takes the current state (an instance of {@link AppModel}) and an {@link Action} and calculates the
     * new state from that.
     * <p>
     * Please note that {@code update} does not require any internal state. Everything that is needed, is passed in the
     * parameters. Also {@code update} has no side effects. It is a pure function.
     * <p>
     * Also please note, that {@code AppModel} is an immutable data structure. This means that {@code update} does not
     * modify the old state, but instead creates a new instance of {@code AppModel}, if anything changes.
     *
     * @param state  the current state
     * @param action the {@code Action} that needs to be performed
     * @return the new state
     * @throws NullPointerException if state or action are {@code null}
     */
    public static AppModel update(AppModel state, Object action) {
        Objects.requireNonNull(state, "The parameter 'state' must not be null");
        Objects.requireNonNull(action, "The parameter 'action' must not be null");

        // Here we assign the new state
        final AppModel newState =

                // This is part of Javaslang's pattern-matching API. It works similar to the regular switch-case
                // in Java, except that it is much more flexible and that it can be used as an expression.
                // We check which of the cases is true and in that branch we specify the value that will be assigned
                // to newState.
                Match(action).of(

                        API.Case(instanceOf(OpenAlertAction.class),
                                openAlertAction ->
                                        state.withAlertVisible(true)
                                                .withAlertIsModel(Modality.NONE)
                        ),

                        API.Case(instanceOf(OpenModalAlertAction.class),
                                openModalAlertAction ->
                                        state.withAlertVisible(true)
                                                .withAlertIsModel(Modality.APPLICATION_MODAL)
                        ),

                        API.Case(instanceOf(AlertWasClosedAction.class),
                                alertWasClosedAction ->
                                        state.withAlertVisible(false)
                        ),

                        // This is the default branch of this switch-case. If an unknown Action was passed to the
                        // updater, we simple return the old state. This is a convention, that is not needed right
                        // now, but will help once you start to decompose your updater.
                        Case($(), state)
                );

        LOG.trace("\nUpdater Old State:\n{}\nUpdater Action:\n{}\nUpdater New State:\n{}\n\n",
                state, action, newState);
        return newState;
    }
}
