package com.netopyr.reduxfx;

import com.netopyr.reduxfx.mainloop.MainLoop;
import com.netopyr.reduxfx.updater.Update;
import com.netopyr.reduxfx.vscenegraph.VNode;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.function.BiFunction;
import java.util.function.Function;

public class SimpleReduxFX<STATE, ACTION> {

    private final MainLoop<ACTION> mainLoop;

    private SimpleReduxFX(STATE initialState, BiFunction<STATE, ACTION, STATE> simpleUpdater, Function<STATE, VNode<ACTION>> view, Parent root) {
        final BiFunction<STATE, ACTION, Update<STATE>> updater = (state, action) -> Update.of(simpleUpdater.apply(state, action));
        mainLoop = new MainLoop<>(initialState, updater, view, root);
    }

    public static <STATE, ACTION> SimpleReduxFX<STATE, ACTION> start(STATE initialState, BiFunction<STATE, ACTION, STATE> updater, Function<STATE, VNode<ACTION>> view, Stage stage) {
        return new SimpleReduxFX<>(initialState, updater, view, setupRoot(stage));
    }

    public static <STATE, ACTION> SimpleReduxFX<STATE, ACTION> start(STATE initialState, BiFunction<STATE, ACTION, STATE> updater, Function<STATE, VNode<ACTION>> view, Group root) {
        return new SimpleReduxFX<>(initialState, updater, view, root);
    }

    public static <STATE, ACTION> SimpleReduxFX<STATE, ACTION> start(STATE initialState, BiFunction<STATE, ACTION, STATE> updater, Function<STATE, VNode<ACTION>> view, Pane root) {
        return new SimpleReduxFX<>(initialState, updater, view, root);
    }

    private static Parent setupRoot(Stage stage) {
        final StackPane root = new StackPane();
        root.setMinWidth(Region.USE_PREF_SIZE);
        root.setMinHeight(Region.USE_PREF_SIZE);
        root.setMaxWidth(Region.USE_PREF_SIZE);
        root.setMaxHeight(Region.USE_PREF_SIZE);

        stage.setScene(new Scene(root));

        return root;
    }

    public void stop() {
        mainLoop.stop();
    }
}