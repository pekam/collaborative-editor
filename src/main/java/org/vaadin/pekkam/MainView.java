package org.vaadin.pekkam;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.Route;

@Push
@Route("")
@StyleSheet("styles.css")
public class MainView extends VerticalLayout {

    public MainView() {
        add(new PushComponent());
        add(new BroadcastComponent());
    }
}
