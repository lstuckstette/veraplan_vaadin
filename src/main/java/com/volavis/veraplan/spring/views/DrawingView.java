package com.volavis.veraplan.spring.views;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.volavis.veraplan.spring.MainLayout;

@Tag("drawing-view")
@HtmlImport("views/drawing-view.html")
@JavaScript("bower_components/yjs/y.js")
@Route(value = "drawing", layout = MainLayout.class)
public class DrawingView extends PolymerTemplate<TemplateModel> {

    public DrawingView(){
    init();
    }

    private void init(){

    }
}
