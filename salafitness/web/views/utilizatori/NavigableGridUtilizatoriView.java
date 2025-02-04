package org.app.salafitness.web.views.utilizatori;


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
import entity.Utilizator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.MainView;

@PageTitle("utilizatori")
@Route(value = "utilizatori", layout = MainView.class)
public class NavigableGridUtilizatoriView extends VerticalLayout implements HasUrlParameter<Integer>{
    // Definire model date
    private EntityManager em;
    private List<Utilizator> utilizatori = new ArrayList<>();
    private Utilizator utilizator = null;

    // Definire componente view
    private H1 titluForm = new H1("Lista Utilizatori");
    // Definire componente suport navigare
    private VerticalLayout gridLayoutToolbar;
    private TextField filterText = new TextField();
    private Button cmdEditUtilizator = new Button("Editeaza utilizator...");
    private Button cmdAdaugaUtilizator = new Button("Adauga utilizator...");
    private Button cmdStergeUtilizator = new Button("Sterge utilizator");
    private Grid<Utilizator> grid = new Grid<>(Utilizator.class);
    // init Data Model
    private void initDataModel(){
        System.out.println("DEBUG START FORM >>> ");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SalaFitnessJPA");
        em = emf.createEntityManager();
        List<Utilizator> lst = em
                .createQuery("SELECT u FROM Utilizator u ORDER BY u.id", Utilizator.class)
                .getResultList();
        utilizatori.addAll(lst);
        if (lst != null && !lst.isEmpty()){
            Collections.sort(this.utilizatori, (u1, u2) -> u1.getId().compareTo(u2.getId()));
            this.utilizator = utilizatori.get(0);
            System.out.println("DEBUG: utilizator init >>> " + utilizator.getId());
        }
        //
        grid.setItems(this.utilizatori);

        grid.asSingleSelect().setValue(this.utilizator);}
    // init View Model
    private void initViewLayout() {
        // Layout navigare -------------------------------------//
        // Toolbar navigare
        filterText.setPlaceholder("Filter by nume...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        HorizontalLayout gridToolbar = new HorizontalLayout(filterText,
                cmdEditUtilizator, cmdAdaugaUtilizator, cmdStergeUtilizator);
        // Grid navigare
        grid.setColumns("id", "nume");
        grid.addComponentColumn(item -> createGridActionsButtons(item)).setHeader("Actiuni");
        // Init Layout navigare
        gridLayoutToolbar = new VerticalLayout(gridToolbar, grid);
        // ---------------------------
        this.add(titluForm, gridLayoutToolbar);
        //
    }
    private Component createGridActionsButtons(Utilizator item) {
        //
        Button cmdEditItem = new Button("Edit");
        cmdEditItem.addClickListener(e -> {
            grid.asSingleSelect().setValue(item);
            editUtilizator();
        });
        Button cmdDeleteItem = new Button("Sterge");
        cmdDeleteItem.addClickListener(e -> {
            System.out.println("Sterge item: " + item);
            grid.asSingleSelect().setValue(item);
            stergeUtilizator();
            refreshForm();
        } );
        //
        return new HorizontalLayout(cmdEditItem, cmdDeleteItem);
    }
    // init Controller components
    private void initControllerActions() {
        // Navigation Actions
        filterText.addValueChangeListener(e -> updateList());
        cmdEditUtilizator.addClickListener(e -> {
            editUtilizator();
        });
        cmdAdaugaUtilizator.addClickListener(e -> {
            adaugaUtilizator();
        });
        cmdStergeUtilizator.addClickListener(e -> {
            stergeUtilizator();
            refreshForm();
        });}
    // CRUD actions
    // Adaugare: delegare catre Formular detalii utilizator
    private void adaugaUtilizator() {
        this.getUI().ifPresent(ui -> ui.navigate(FormUtilizatorView.class, 999));
    }
    // Editare: delegare catre Formular detalii utilizator
    private void editUtilizator() {
        this.utilizator = this.grid.asSingleSelect().getValue();
        System.out.println("Selected utilizator:: " + utilizator);
        if (this.utilizator != null) {
            this.getUI().ifPresent(ui -> ui.navigate(
                    FormUtilizatorView.class, this.utilizator.getId())
            );
        }}
    // CRUD actions
    // Stergere: tranzactie locala cu EntityManager
    private void stergeUtilizator() {
        this.utilizator = this.grid.asSingleSelect().getValue();
        System.out.println("To remove: " + this.utilizator);
        this.utilizatori.remove(this.utilizator);
        if (this.em.contains(this.utilizator)) {
            this.em.getTransaction().begin();
            this.em.remove(this.utilizator);
            this.em.getTransaction().commit();
        }
        if (!this.utilizatori.isEmpty())
            this.utilizator = this.utilizatori.get(0);
        else
            this.utilizator = null;
    }
    // Start Form
    public NavigableGridUtilizatoriView() {
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
            List<Utilizator> lstUtilizatoriFiltered = this.utilizatori;
            if (filterText.getValue() != null) {
                lstUtilizatoriFiltered = this.utilizatori.stream()
                        .filter(u -> u.getNume().contains(filterText.getValue()))
                        .toList();
                grid.setItems(lstUtilizatoriFiltered);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Resincronizare componente-view cu modelul de date
    private void refreshForm() {
        System.out.println("Utilizator curent: " + this.utilizator);
        if (this.utilizator != null) {
            grid.setItems(this.utilizatori);
            grid.select(this.utilizator);
        }
    }// … … //

    // … … //

    // Navigation Management:
    // URL-ul http://localhost:8080/utilizatori/3 asigură selecția utilizatorului cu ID 3
    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
        if (id != null) {
            this.utilizator = em.find(Utilizator.class, id);
            System.out.println("Back Utilizator: " + utilizator);
            if (this.utilizator == null) {
                // DELETED Item
                if (!this.utilizatori.isEmpty())
                    this.utilizator = this.utilizatori.get(0);
            }
            // else: EDITED or NEW Item
        }
        this.refreshForm();
    }
    // … … //
}

