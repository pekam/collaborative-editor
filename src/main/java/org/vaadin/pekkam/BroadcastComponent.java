package org.vaadin.pekkam;

import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;

public class BroadcastComponent extends VerticalLayout {

    public static class Broadcaster {

        static Executor executor = Executors.newSingleThreadExecutor();
        static LinkedList<Consumer<String>> listeners = new LinkedList<>();

        public static synchronized Registration subscribe(
                Consumer<String> listener) {
            listeners.add(listener);

            return () -> {
                synchronized (Broadcaster.class) {
                    listeners.remove(listener);
                }
            };
        }

        public static synchronized void publish(String message) {
            listeners.forEach(listener -> executor
                    .execute(() -> listener.accept(message)));
        }

    }

    private Registration subscription;
    private TextArea field;

    public BroadcastComponent() {
        field = new TextArea();
        field.setValueChangeMode(ValueChangeMode.EAGER);

        field.addValueChangeListener(e -> {
            if (e.isFromClient()) {
                Broadcaster.publish(e.getValue());
            }
        });
        subscription = Broadcaster.subscribe(this::pushValue);

        add(new H3("Broadcasting With Server Push"), field);
    }

    private void pushValue(String value) {
        getUI().ifPresent(ui -> ui.access(() -> field.setValue(value)));
    }

    @Override
    protected void onDetach(final DetachEvent detachEvent) {
        subscription.remove();
        subscription = null;
    }
}
