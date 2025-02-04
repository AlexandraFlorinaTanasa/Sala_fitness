package org.app.salafitness.web.views.utilizatori;



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
import entity.Utilizator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.MainView;

@PageTitle("utilizator")
@Route(value = "utilizator", layout = MainView.class)
public class FormUtilizatorView extends VerticalLayout implements HasUrlParameter<Integer>{
    // … … //
    // Navigation Management:
    // URL-ul http://localhost:8080/utilizator/3 asigură afișare detaliilor clientului cu ID 3
    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
        System.out.println("Utilizator ID: " + id);
        if (id != null) {
            // EDIT Item
            this.utilizator = em.find(Utilizator.class, id);
            System.out.println("Selected utilizator to edit:: " + utilizator);
            if (this.utilizator == null) {
                System.out.println("ADD utilizator:: " + utilizator);
                // NEW Item
                this.adaugaUtilizator();
                this.utilizator.setId(id);
                this.utilizator.setNume("Utilizator NOU " + id);
            }
        }
        this.refreshForm();
    }
    // Definire model date
    private EntityManager em;
    private Utilizator utilizator= null;
    private Binder<Utilizator> binder = new BeanValidationBinder<>(Utilizator.class);
    // Definire componente view
    // Definire Form
    private VerticalLayout formLayoutToolbar;
    private H1 titluForm = new H1("Form Utilizator");
    private IntegerField id = new IntegerField("ID utilizator:");
    private TextField nume = new TextField("Nume utilizator: ");
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
        this.utilizator = em
                .createQuery("SELECT u FROM Utilizator u ORDER BY u.id", Utilizator.class)
                .getResultStream().findFirst().get();
        //
        binder.forField(id).bind("id");
        binder.forField(nume).bind("nume");
        //
        refreshForm();
    }
    // init View Model
    private void initViewLayout() {
        // Form-Master-Details -----------------------------------//
        // Form-Master
        FormLayout formLayout = new FormLayout();
        formLayout.add(id, nume);
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
            adaugaUtilizator();
            refreshForm();
        });
        cmdSterge.addClickListener(e -> {
            stergeUtilizator();
            // Navigate back to NavigableGridUtilizatoriForm
            this.getUI().ifPresent(ui -> ui.navigate(
                    NavigableGridUtilizatoriView.class)
            );
        });
        cmdAbandon.addClickListener(e -> {
            // Navigate back to NavigableGridUtilizatoriForm
            this.getUI().ifPresent(ui -> ui.navigate(
                    NavigableGridUtilizatoriView.class, this.utilizator.getId())
            );
        });
        cmdSalveaza.addClickListener(e -> {
            salveazaUtilizator();
            // refreshForm();
            // Navigate back to NavigableGridUtilizatoriForm
            this.getUI().ifPresent(ui -> ui.navigate(
                    NavigableGridUtilizatoriView.class, this.utilizator.getId())
            );
        });

    }private void refreshForm() {
        System.out.println("Utilizator curent: " + this.utilizator);
        if (this.utilizator != null) {
            binder.setBean(this.utilizator);
        }

    }
    // CRUD actions
    private void salveazaUtilizator() {
        try {
            this.em.getTransaction().begin();
            this.utilizator = this.em.merge(this.utilizator);
            this.em.getTransaction().commit();
            System.out.println("Utilizator Salvat");
        } catch (Exception ex) {
            if (this.em.getTransaction().isActive())
                this.em.getTransaction().rollback();
            System.out.println("*** EntityManager Validation ex: " + ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }
    // CRUD actions
    private void adaugaUtilizator() {
        this.utilizator = new Utilizator();
        this.utilizator.setId(999); // ID arbitrar, inexistent în baza de date
        this.utilizator.setNume("Utilizator Nou");
    }
    // CRUD actions
    private void stergeUtilizator() {
        System.out.println("To remove: " + this.utilizator);
        if (this.em.contains(this.utilizator)) {
            this.em.getTransaction().begin();
            this.em.remove(this.utilizator);
            this.em.getTransaction().commit();
        }
    }
    // Start Form
    public FormUtilizatorView() {
        //
        initDataModel();
        //
        initViewLayout();
        //
        initControllerActions();
    }
}
