package org.vaadin.pekkam;

import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class PushComponent extends VerticalLayout {
    private Label label = new Label("No updates");
    private Thread thread;

    private int counter = 0;

    public PushComponent() {

        Button startButton = new Button("Start updates", e -> startUpdates());
        Button stopButton = new Button("Stop updates", e -> cleanThread());

        HorizontalLayout hor = new HorizontalLayout(startButton, stopButton,
                label);
        hor.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(new H3("Testing Server Push"), hor);
    }

    private void update() {
        label.setText("Server push: " + counter++);
    }

    private void startUpdates() {
        if (thread != null) {
            return;
        }

        thread = new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(1000);
                    if (!getUI().isPresent()) {
                        cleanThread();
                    } else {
                        getUI().get().access(this::update);
                    }
                }

            } catch (final InterruptedException e) {
                // Expected to be interrupted when stopping the thread
            }
        });
        thread.start();
    }

    private void cleanThread() {
        if (thread == null) {
            return;
        }
        thread.interrupt();
        thread = null;
    }

    @Override
    protected void onDetach(final DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        cleanThread();
    }
}
