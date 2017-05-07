package com.netopyr.reduxfx.vscenegraph.builders;

import com.netopyr.reduxfx.vscenegraph.impl.patcher.property.Accessors;
import com.netopyr.reduxfx.vscenegraph.impl.patcher.property.SceneAccessor;
import com.netopyr.reduxfx.vscenegraph.impl.patcher.property.WindowsAccessor;
import com.netopyr.reduxfx.vscenegraph.VNode;
import com.netopyr.reduxfx.vscenegraph.event.VEventHandler;
import com.netopyr.reduxfx.vscenegraph.event.VEventType;
import com.netopyr.reduxfx.vscenegraph.property.VProperty;
import javafx.stage.WindowEvent;
import javaslang.collection.Array;
import javaslang.collection.Map;
import javaslang.control.Option;

import static com.netopyr.reduxfx.vscenegraph.event.VEventType.CLOSE_REQUEST;

@SuppressWarnings("unused")
public class WindowBuilder<BUILDER extends WindowBuilder<BUILDER>> extends Builder<BUILDER> {

    private static final String DIALOGS = "dialogs";
    private static final String SCENE = "scene";
    private static final String SHOWING = "showing";
    private static final String WINDOWS = "windows";

    public WindowBuilder(Class<?> nodeClass,
                         Map<String, Array<VNode>> childrenMap,
                         Map<String, Option<VNode>> singleChildMap,
                         Map<String, VProperty> properties,
                         Map<VEventType, VEventHandler> eventHandlers) {
        super(nodeClass, childrenMap, singleChildMap, properties, eventHandlers);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected BUILDER create(
            Map<String, Array<VNode>> childrenMap,
            Map<String, Option<VNode>> singleChildMap,
            Map<String, VProperty> properties,
            Map<VEventType, VEventHandler> eventHandlers) {
        return (BUILDER) new WindowBuilder<>(getNodeClass(), childrenMap, singleChildMap, properties, eventHandlers);
    }


    public BUILDER dialogs(VNode... nodes) {
        Accessors.registerNodeListAccessor(getNodeClass(), DIALOGS, WindowsAccessor::new);
        return children(DIALOGS, nodes == null? Array.empty() : Array.of(nodes));
    }

    public BUILDER scene(SceneBuilder<?> value) {
        Accessors.registerNodeAccessor(getNodeClass(), SCENE, SceneAccessor::new);
        return child(SCENE, value);
    }

    public BUILDER windows(VNode... nodes) {
        Accessors.registerNodeListAccessor(getNodeClass(), WINDOWS, WindowsAccessor::new);
        return children(WINDOWS, nodes == null? Array.empty() : Array.of(nodes));
    }

    public BUILDER showing(boolean value) {
        return property(value? VProperty.Phase.SHOW_STAGE : VProperty.Phase.HIDE_STAGE, SHOWING, value);
    }


    public BUILDER onCloseRequest(VEventHandler<WindowEvent> eventHandler) {
        return onEvent(CLOSE_REQUEST, eventHandler);
    }

}