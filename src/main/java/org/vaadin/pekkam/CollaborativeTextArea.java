package org.vaadin.pekkam;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;

@JavaScript("collaborativeEditor.js")
@Tag("textarea")
public class CollaborativeTextArea extends Component {

    public CollaborativeTextArea() {
        getElement().addAttachListener(e -> UI.getCurrent().getPage()
                .executeJavaScript("configureCollaborativeEditor($0)", this));

        getElement().addEventListener("operation", e -> {
            System.out.println(e.getEventData().getString("event.detail"));
        }).addEventData("event.detail");

        getElement().addEventListener("caret-move", e -> {
            System.out.println(e.getEventData().getNumber("event.detail"));
        }).addEventData("event.detail");
    }
}
