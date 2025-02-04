package org.app.salafitness.web.views.echipamente;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
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

@PageTitle("echipament")
@Route(value = "echipament", layout = MainView.class)
public class FormEchipamentView extends VerticalLayout implements HasUrlParameter<Integer>{
    // … … //
    // Navigation Management:
    // URL-ul http://localhost:8080/echipament/3 asigură afișare detaliilor echipamentului cu cod 3
    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer cod) {
        System.out.println("Echipament cod: " + cod);
        if (cod != null) {
            // EDIT Item
            this.echipament = em.find(Echipament.class, cod);
            System.out.println("Selected echipament to edit:: " + echipament);
            if (this.echipament == null) {
                System.out.println("ADD echipament:: " + echipament);
                // NEW Item
                this.adaugaEchipament();
                this.echipament.setCod(cod);
                this.echipament.setDenumire("Echipament NOU " + cod);
            }
        }
        this.refreshForm();
    }
    // Definire model date
    private EntityManager em;
    private Echipament echipament= null;
    private Binder<Echipament> binder = new BeanValidationBinder<>(Echipament.class);
    // Definire componente view
    // Definire Form
    private VerticalLayout formLayoutToolbar;
    private H1 titluForm = new H1("Form Echipament");
    private IntegerField cod = new IntegerField("Cod Echipament:");
    private TextField denumire = new TextField("Nume echipament: ");
    // Definire componente actiuni Form-Controller
    private Button cmdAdaugare = new Button("Adauga");
    private Button cmdSterge = new Button("Sterge");
    private Button cmdAbandon = new Button("Abandon");
    private Button cmdSalveaza = new Button("Salveaza");
    // … … //// init Data Model
    private void initDataModel(){
        System.out.println("DEBUG START FORM >>> ");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SalaFitnessJPA");
        this.em = emf.createEntityManager();
        this.echipament = em
                .createQuery("SELECT e FROM Echipament e ORDER BY e.cod", Echipament.class)
                .getResultStream().findFirst().get();
        //
        binder.forField(cod).bind("cod");
        binder.forField(denumire).bind("denumire");
        //
        refreshForm();
    }
    // init View Model
    private void initViewLayout() {
        // Form-Master-Details -----------------------------------//
        // Form-Master
        FormLayout formLayout = new FormLayout();
        formLayout.add(cod, denumire);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        formLayout.setMaxWidth("400px");
        // Toolbar-Actions-Master
        HorizontalLayout actionToolbar =
                new HorizontalLayout(cmdAdaugare, cmdSterge, cmdAbandon, cmdSalveaza);
        actionToolbar.setPadding(false);
        //
        this.formLayoutToolbar = new VerticalLayout(formLayout, actionToolbar);
        // ---------------------------
        this.add(titluForm, formLayoutToolbar);}
    //// init Controller components
    private void initControllerActions() {
        // Transactional Master Actions
        cmdAdaugare.addClickListener(e -> {
            adaugaEchipament();
            refreshForm();
        });
        cmdSterge.addClickListener(e -> {
            stergeEchipament();
            // Navigate back to NavigableGridEchipamenteForm
            this.getUI().ifPresent(ui -> ui.navigate(
                    NavigableGridEchipamenteView.class)
            );
        });
        cmdAbandon.addClickListener(e -> {
            // Navigate back to NavigableGridEchipamenteForm
            this.getUI().ifPresent(ui -> ui.navigate(
                    NavigableGridEchipamenteView.class, this.echipament.getCod())
            );
        });
        cmdSalveaza.addClickListener(e -> {
            salveazaEchipament();
            // refreshForm();
            // Navigate back to NavigableGridEchipamenteForm
            this.getUI().ifPresent(ui -> ui.navigate(
                    NavigableGridEchipamenteView.class, this.echipament.getCod())
            );
        });

    }private void refreshForm() {
        System.out.println("Echipament curent: " + this.echipament);
        if (this.echipament != null) {
            binder.setBean(this.echipament);
        }

    }
    // CRUD actions
    private void salveazaEchipament() {
        try {
            this.em.getTransaction().begin();
            this.echipament = this.em.merge(this.echipament);
            this.em.getTransaction().commit();
            System.out.println("Echipament Salvat");
        } catch (Exception ex) {
            if (this.em.getTransaction().isActive())
                this.em.getTransaction().rollback();
            System.out.println("*** EntityManager Validation ex: " + ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }
    // CRUD actions
    private void adaugaEchipament() {
        this.echipament = new Echipament();
        this.echipament.setCod(999); // id arbitrar, inexistent în baza de date
        this.echipament.setDenumire("Echipament Nou");
    }
    // CRUD actions
    private void stergeEchipament() {
        System.out.println("To remove: " + this.echipament);
        if (this.em.contains(this.echipament)) {
            this.em.getTransaction().begin();
            this.em.remove(this.echipament);
            this.em.getTransaction().commit();
        }
    }
    // Start Form
    public FormEchipamentView() {
        //
        initDataModel();
        //
        initViewLayout();
        //
        initControllerActions();
    }
}
