package org.app.salafitness.web.views.echipamente;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import entity.Echipament;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.MainView;

@PageTitle("echipamente")
@Route(value = "echipamente", layout = MainView.class)
public class NavigableGridEchipamenteView extends VerticalLayout implements HasUrlParameter<Integer>{
    // Definire model date
    private EntityManager em;
    private List<Echipament> echipamente = new ArrayList<>();
    private Echipament echipament = null;

    // Definire componente view
    private H1 titluForm = new H1("Lista Echipamente");
    // Definire componente suport navigare
    private VerticalLayout gridLayoutToolbar;
    private TextField filterText = new TextField();
    private Button cmdEditEchipament = new Button("Editeaza echipament...");
    private Button cmdAdaugaEchipament = new Button("Adauga echipament...");
    private Button cmdStergeEchipament = new Button("Sterge echipament");
    private Grid<Echipament> grid = new Grid<>(Echipament.class);
    // init Data Model
    private void initDataModel(){
        System.out.println("DEBUG START FORM >>> ");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SalaFitnessJPA");
        em = emf.createEntityManager();
        List<Echipament> lst = em
                .createQuery("SELECT e FROM Echipament e ORDER BY e.cod", Echipament.class)
                .getResultList();
        echipamente.addAll(lst);
        if (lst != null && !lst.isEmpty()){
            Collections.sort(this.echipamente, (e1, e2) -> e1.getCod().compareTo(e2.getCod()));
            this.echipament = echipamente.get(0);
            System.out.println("DEBUG: echipament init >>> " + echipament.getCod());
        }
        //
        grid.setItems(this.echipamente);

        grid.asSingleSelect().setValue(this.echipament);}
    // init View Model
    private void initViewLayout() {
        // Layout navigare -------------------------------------//
        // Toolbar navigare
        filterText.setPlaceholder("Filter by nume...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        HorizontalLayout gridToolbar = new HorizontalLayout(filterText,
                cmdEditEchipament, cmdAdaugaEchipament, cmdStergeEchipament);
        // Grid navigare
        grid.setColumns("cod", "denumire");
        grid.addComponentColumn(item -> createGridActionsButtons(item)).setHeader("Actiuni");
        // Init Layout navigare
        gridLayoutToolbar = new VerticalLayout(gridToolbar, grid);
        // ---------------------------
        this.add(titluForm, gridLayoutToolbar);
        //
    }
    private Component createGridActionsButtons(Echipament item) {
        //
        Button cmdEditItem = new Button("Edit");
        cmdEditItem.addClickListener(e -> {
            grid.asSingleSelect().setValue(item);
            editEchipament();
        });
        Button cmdDeleteItem = new Button("Sterge");
        cmdDeleteItem.addClickListener(e -> {
            System.out.println("Sterge item: " + item);
            grid.asSingleSelect().setValue(item);
            stergeEchipament();
            refreshForm();
        } );
        //
        return new HorizontalLayout(cmdEditItem, cmdDeleteItem);
    }
    // init Controller components
    private void initControllerActions() {
        // Navigation Actions
        filterText.addValueChangeListener(e -> updateList());
        cmdEditEchipament.addClickListener(e -> {
            editEchipament();
        });
        cmdAdaugaEchipament.addClickListener(e -> {
            adaugaEchipament();
        });
        cmdStergeEchipament.addClickListener(e -> {
            stergeEchipament();
            refreshForm();
        });}
    // CRUD actions
    // Adaugare: delegare catre Formular detalii echipament
    private void adaugaEchipament() {
        this.getUI().ifPresent(ui -> ui.navigate(FormEchipamentView.class, 999));
    }
    // Editare: delegare catre Formular detalii echipament
    private void editEchipament() {
        this.echipament = this.grid.asSingleSelect().getValue();
        System.out.println("Selected echipament:: " + echipament);
        if (this.echipament != null) {
            this.getUI().ifPresent(ui -> ui.navigate(
                    FormEchipamentView.class, this.echipament.getCod())
            );
        }}
    // CRUD actions
    // Stergere: tranzactie locala cu EntityManager
    private void stergeEchipament() {
        this.echipament = this.grid.asSingleSelect().getValue();
        System.out.println("To remove: " + this.echipament);
        this.echipamente.remove(this.echipament);
        if (this.em.contains(this.echipament)) {
            this.em.getTransaction().begin();
            this.em.remove(this.echipament);
            this.em.getTransaction().commit();
        }
        if (!this.echipamente.isEmpty())
            this.echipament = this.echipamente.get(0);
        else
            this.echipament = null;
    }
    // Start Form
    public NavigableGridEchipamenteView() {
        //
        initDataModel();
        //
        initViewLayout();
        //
        initControllerActions();
    }
    // Populare grid cu set de date din model - filtrare
    private void updateList() {
        try {
            List<Echipament> lstEchipamenteFiltered = this.echipamente;
            if (filterText.getValue() != null) {
                lstEchipamenteFiltered = this.echipamente.stream()
                        .filter(e -> e.getDenumire().contains(filterText.getValue()))
                        .toList();
                grid.setItems(lstEchipamenteFiltered);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Resincronizare componente-view cu modelul de date
    private void refreshForm() {
        System.out.println("Echipament curent: " + this.echipament);
        if (this.echipament != null) {
            grid.setItems(this.echipamente);
            grid.select(this.echipament);
        }
    }// … … //

    // … … //

    // Navigation Management:
    // URL-ul http://localhost:8080/echipamente/3 asigură selecția echipamentului cu codul 3
    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer cod) {
        if (cod != null) {
            this.echipament = em.find(Echipament.class, cod);
            System.out.println("Back Echipament: " + echipament);
            if (this.echipament == null) {
                // DELETED Item
                if (!this.echipamente.isEmpty())
                    this.echipament = this.echipamente.get(0);
            }
            // else: EDITED or NEW Item
        }
        this.refreshForm();
    }
    // … … //
}

