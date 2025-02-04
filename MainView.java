package org.example;

import com.vaadin.flow.component.UI;

import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.menubar.MenuBar;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import org.app.salafitness.web.views.echipamente.FormEchipamentView;
import org.app.salafitness.web.views.echipamente.NavigableGridEchipamenteView;
import org.app.salafitness.web.views.utilizatori.FormUtilizatorView;
import org.app.salafitness.web.views.utilizatori.NavigableGridUtilizatoriView;

/**
 * The main view contains a button and a click listener.
 */
@Route
public class MainView extends VerticalLayout implements RouterLayout {
    public MainView() {
        setMenuBar();
    }
    private void setMenuBar() {
        MenuBar mainMenu = new MenuBar();
        MenuItem homeMenu = mainMenu.addItem("Home");
        homeMenu.addClickListener(event -> UI.getCurrent().navigate(MainView.class));
//
        MenuItem gridFormsUtilizatoriMenu = mainMenu.addItem("Utilizatori");
        SubMenu gridFormsUtilizatoriMenuBar = gridFormsUtilizatoriMenu.getSubMenu();
        gridFormsUtilizatoriMenuBar.addItem("Lista Utilizatori...",
                event -> UI.getCurrent().navigate(NavigableGridUtilizatoriView.class));
        gridFormsUtilizatoriMenuBar.addItem("Form Editare Utilizator...",
                event -> UI.getCurrent().navigate(FormUtilizatorView.class));
//


        MenuItem gridFormsEchipamenteMenu = mainMenu.addItem("Echipamente");
        SubMenu gridFormsEchipamenteMenuBar = gridFormsEchipamenteMenu.getSubMenu();
        gridFormsEchipamenteMenuBar.addItem("Lista Echipamente...",
                event -> UI.getCurrent().navigate(NavigableGridEchipamenteView.class));
        gridFormsEchipamenteMenuBar.addItem("Form Editare Echipament...",
                event -> UI.getCurrent().navigate(FormEchipamentView.class));
//
        add(new HorizontalLayout(mainMenu));
    }
}
