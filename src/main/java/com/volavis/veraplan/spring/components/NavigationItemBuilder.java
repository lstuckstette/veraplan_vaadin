package com.volavis.veraplan.spring.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.router.RouterLink;

public class NavigationItemBuilder {

    private String linkText = "";
    private String href = "";
    private Class<? extends Component> targetClass = null;
    private boolean rightAlign = false;
    private String iconClass = "";
    private String hoverColor = "w3-hover-white";
    private String backgroundColor = "";
    private String onClick = "";

    private RouterLink buildRouterLink() {
        RouterLink rlink = new RouterLink(linkText, targetClass);
        if (rightAlign) {
            rlink.setClassName("w3-bar-item w3-button w3-hide-small w3-padding-large w3-right " + hoverColor + " " + backgroundColor);
        } else {
            rlink.setClassName("w3-bar-item w3-button w3-hide-small w3-padding-large " + hoverColor + " " + backgroundColor);
        }
        if (!iconClass.equals("")) {
            HtmlContainer icon = new HtmlContainer("i");
            icon.setClassName(iconClass);
            rlink.add(icon);
        }
        return rlink;
    }

    private Anchor buildAnchor() {
        Anchor alink = new Anchor();
        if (rightAlign) {
            alink.setClassName("w3-bar-item w3-button w3-hide-small w3-padding-large w3-right " + hoverColor + " " + backgroundColor);
        } else {
            alink.setClassName("w3-bar-item w3-button w3-hide-small w3-padding-large " + hoverColor + " " + backgroundColor);
        }
        alink.setText(linkText);
        alink.setHref(href);
        if (!iconClass.equals("")) {
            HtmlContainer icon = new HtmlContainer("i");
            icon.setClassName(iconClass);
            alink.add(icon);
        }
        return alink;
    }

    private HtmlContainer buildButton() {
        HtmlContainer button = new HtmlContainer("button");
        if (rightAlign) {
            button.setClassName("w3-bar-item w3-button w3-hide-small w3-padding-large w3-right " + hoverColor + " " + backgroundColor);
        } else {
            button.setClassName("w3-bar-item w3-button w3-hide-small w3-padding-large " + hoverColor + " " + backgroundColor);
        }
        button.setText(linkText);
        button.getElement().setAttribute("on-click", onClick);
        if (!iconClass.equals("")) {
            HtmlContainer icon = new HtmlContainer("i");
            icon.setClassName(iconClass);
            button.add(icon);
        }
        return button;
    }

    public Component build() {

        if (null == targetClass && onClick.equals("")) {
            return buildAnchor();

        } else if (null == targetClass) {
            return buildButton();
        } else {
            return buildRouterLink();
        }
    }

    public NavigationItemBuilder linkText(String linkText) {
        this.linkText = linkText;
        return this;
    }

    public NavigationItemBuilder href(String href) {
        this.href = href;
        return this;
    }

    public NavigationItemBuilder targetClass(Class<? extends Component> targetClass) {
        this.targetClass = targetClass;
        return this;
    }

    public NavigationItemBuilder rightAlign(boolean rightAlign) {
        this.rightAlign = rightAlign;
        return this;
    }

    public NavigationItemBuilder iconClass(String iconClass) {
        this.iconClass = iconClass;
        return this;
    }

    public NavigationItemBuilder hovercolor(String hoverColor) {
        this.hoverColor = hoverColor;
        return this;
    }

    public NavigationItemBuilder backgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public NavigationItemBuilder onClick(String onClick) {
        this.onClick = onClick;
        return this;
    }

}
