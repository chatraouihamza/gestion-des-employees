/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestiondesemployees;



import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import com.sun.rowset.internal.Row;
import connexion.NewHibernateUtil;
import java.awt.Insets;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pack.Depart;
import javafx.beans.property.IntegerProperty;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Cell;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import pack.Employee;


/**
 *
 * @author hp
 */
public class FXMLDocumentController implements Initializable {
    @FXML
    private Button fermerbutton;
    @FXML
    private Button fermerbuttonempl;
    
    
    
    @FXML 
    private TextField supptxt;
    @FXML
    private TextField suppemptxt;


    
    
    @FXML
    private TextField nomtxt;
    @FXML
    private TextField prenomtxt;
    @FXML
    private DatePicker datedenaissancetxt;
    @FXML
    private TextField salairetxt;
    @FXML
    private TextField departidtxt;
    @FXML
    private TextField nomdepartementtxt;
    @FXML
    private TextField chefdepartementtxt;
    
    
    
    @FXML
    private TableView<Depart> tableView;

    @FXML
    private TableColumn<Depart, Integer> idColumn;

    @FXML
    private TableColumn<Depart, String> nomDeDepartementColumn;

    @FXML
    private TableColumn<Depart, String> chefDeDepartementColumn;

    
    
    
    
    @FXML
    private TableView<Employee> tableViewfx;

    @FXML
    private TableColumn<Employee, Integer> idColumnfx;

    @FXML
    private TableColumn<Employee, String> nomColumn;

    @FXML
    private TableColumn<Employee, String> prenomColumn;

    @FXML
    private TableColumn<Employee, Date> dateNaissanceColumn;

    @FXML
    private TableColumn<Employee, String> salaireColumn;

    @FXML
    private TableColumn<Employee, Integer> departIdColumn;

    
    
    
    
    
    @FXML
    private void fermer(){
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("comfirmation");
        alert.setHeaderText("Voulez-vous vraiment quitter cette interface ?");
        Optional<ButtonType> result=alert.showAndWait();
        if(result.get()==ButtonType.OK){
            Stage stage1 =(Stage) fermerbutton.getScene().getWindow();
            stage1.close();
            Stage stage2 =(Stage) fermerbuttonempl.getScene().getWindow();
            stage2.close();
        }
    }
    @FXML
     
    public void AjouterOnAction() {
    String nom = nomtxt.getText();
    String prenom = prenomtxt.getText();
    LocalDate dateDeNaissance = datedenaissancetxt.getValue();
    String salaire = salairetxt.getText() + " MAD";
    String departementId = departidtxt.getText();

    Session session = NewHibernateUtil.openSession();
    Transaction transaction = null;

    try {
        transaction = session.beginTransaction();

        // Vérifier si l'employé existe déjà
        if (employeeExists(session, nom, prenom)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("Un employé avec le même nom et prénom existe déjà.");
            alert.showAndWait();
            clearFields();
            return;
        }

        // Si l'employé n'existe pas, procéder à l'ajout
        String sqlQuery = "INSERT INTO employee (nom, prenom, datenaiss, salaire, depart_id) " +
                          "VALUES (:nom, :prenom, :date, :salaire, :departementId)";
        int count = session.createSQLQuery(sqlQuery)
            .setParameter("nom", nom)
            .setParameter("prenom", prenom)
            .setParameter("date", dateDeNaissance.toString()) // Assuming the date format is 'yyyy-MM-dd'
            .setParameter("salaire", salaire)
            .setParameter("departementId", Integer.parseInt(departementId))
            .executeUpdate();

        transaction.commit();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText("Employé ajouté avec succès.");
        alert.showAndWait();
        afficherDonnees();
        clearFields();
    } catch (Exception e) {
        if (transaction != null) {
            transaction.rollback();
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText("Une erreur s'est produite lors de l'ajout de l'employé.");
        alert.showAndWait();
        e.printStackTrace();
    } finally {
        session.close();
    }
}
    private boolean employeeExists(Session session, String nom, String prenom) {
    String hql = "FROM Employee WHERE nom = :nom AND prenom = :prenom";
    Query query = session.createQuery(hql);
    query.setParameter("nom", nom);
    query.setParameter("prenom", prenom);
    return !query.list().isEmpty();
}


    @FXML
 
    public void ajouterDepartementOnAction() {
        String nomDept = nomdepartementtxt.getText();
        String chefDept = chefdepartementtxt.getText();

        Session session = NewHibernateUtil.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            
            String sql = "INSERT INTO depart (nom_dept, chef_dept) VALUES (:nomDept, :chefDept)";
            session.createSQLQuery(sql)
                   .setParameter("nomDept", nomDept)
                   .setParameter("chefDept", chefDept)
                   .executeUpdate();
            
            transaction.commit();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Département ajouté avec succès.");
            alert.showAndWait();
            afficherDepartements();
            clearFields();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    private void afficherDepartements() {
    Session session = NewHibernateUtil.openSession();
    try {
        Query query = session.createQuery("FROM Depart");
        List<Depart> departements = query.list();
        tableView.getItems().clear();
        tableView.getItems().addAll(departements);
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        session.close();
    }
}
    
   private void afficherDonnees() {
    Session session = NewHibernateUtil.openSession();
    try {
        // Démarrez une transaction Hibernate
        session.beginTransaction();
        
        // Créez une requête pour sélectionner tous les employés avec leurs départements associés
        Query query = session.createQuery("SELECT e FROM Employee e JOIN FETCH e.depart");
        
        // Exécutez la requête pour obtenir une liste d'employés avec leurs départements associés
        List<Employee> employees = query.list();
        tableViewfx.getItems().clear();
        
        
        // Ajoutez les employés à votre TableView
        tableViewfx.getItems().addAll(employees);
        
        // Validez et terminez la transaction Hibernate
        session.getTransaction().commit();
    } catch (Exception e) {
        // En cas d'erreur, annulez la transaction Hibernate
        session.getTransaction().rollback();
        e.printStackTrace();
    } finally {
        // Fermez la session Hibernate
        session.close();
    }
}



    
    @FXML 
    private void clearFields() {
        nomtxt.clear();
        prenomtxt.clear();
        datedenaissancetxt.setValue(null); // Clear DatePicker
        salairetxt.clear();
        departidtxt.clear();
        nomdepartementtxt.clear();
        chefdepartementtxt.clear();
        supptxt.clear();
        suppemptxt.clear();
}
    
    @FXML
public void supprimerDepartement() {
    String departementIdStr = supptxt.getText();
    if (departementIdStr != null && !departementIdStr.isEmpty()) {
        int departementId = Integer.parseInt(departementIdStr);

        Session session = NewHibernateUtil.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            Depart depart = (Depart) session.get(Depart.class, departementId);
            if (depart != null) {
                session.delete(depart);
                transaction.commit();
                afficherDepartements(); // Refresh the TableView
                clearFields();
                
                // Afficher un message de succès
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("Département supprimé avec succès.");
                alert.showAndWait();
            } else {
                // Afficher un message si l'ID est introuvable
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Département introuvable avec l'ID : " + departementId);
                alert.showAndWait();
                afficherDepartements();
                clearFields();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            
            // Afficher un message d'erreur en cas d'exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur s'est produite lors de la suppression du département.");
            alert.showAndWait();
            clearFields();
        } finally {
            session.close();
        }
    } else {
        // Afficher un message si l'ID n'est pas valide
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Avertissement");
        alert.setHeaderText(null);
        alert.setContentText("Veuillez entrer un ID valide.");
        alert.showAndWait();
        clearFields();
    }
}


@FXML
public void supprimerEmploye() {
    String employeIdStr = suppemptxt.getText();
    if (employeIdStr != null && !employeIdStr.isEmpty()) {
        try {
            int employeId = Integer.parseInt(employeIdStr);

            Session session = NewHibernateUtil.openSession();
            Transaction transaction = null;

            try {
                transaction = session.beginTransaction();
                Employee employe = (Employee) session.get(Employee.class, employeId);
                if (employe != null) {
                    session.delete(employe);
                    transaction.commit();
                    afficherDonnees();
                    clearFields();

                    // Afficher un message de succès
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Succès");
                    alert.setHeaderText(null);
                    alert.setContentText("Employé supprimé avec succès.");
                    alert.showAndWait();
                    clearFields();
                } else {
                    // Afficher un message si l'ID est introuvable
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Employé introuvable avec l'ID : " + employeId);
                    alert.showAndWait();
                    clearFields();
                }
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();

                // Afficher un message d'erreur en cas d'exception
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Une erreur s'est produite lors de la suppression de l'employé.");
                alert.showAndWait();
                clearFields();
            } finally {
                session.close();
            }
        } catch (NumberFormatException e) {
            // Afficher un message si l'ID n'est pas valide
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez entrer un ID valide.");
            alert.showAndWait();
            clearFields();
        }
    } else {
        // Afficher un message si l'ID est vide
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Avertissement");
        alert.setHeaderText(null);
        alert.setContentText("Veuillez entrer un ID.");
        alert.showAndWait();
        clearFields();
    }
}



    public void updateDepartement() {
    String departementId = supptxt.getText();

    Session session = NewHibernateUtil.openSession();

    try {
        // Récupérer le département depuis la base de données en utilisant son ID
        Depart departement = (Depart) session.get(Depart.class, Integer.parseInt(departementId));

        if (departement != null) {
            // Afficher une boîte de dialogue de confirmation avec les données actuelles du département et des champs de texte pour la modification
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Confirmation");
            dialog.setHeaderText("Modifier les données du département ?");
            dialog.setContentText("Nom du département:");
            dialog.getEditor().setText(departement.getNomDept()); // Afficher le nom actuel du département dans le champ de texte

            // Ajouter un champ de texte pour le chef de département
            TextField chefDeptField = new TextField();
            chefDeptField.setPromptText("Chef du département");
            chefDeptField.setText(departement.getChefDept()); // Afficher le chef actuel du département dans le champ de texte
            dialog.getDialogPane().setContent(new VBox(8, dialog.getEditor(), chefDeptField));

            // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
            Optional<String> result = dialog.showAndWait();

            if (result.isPresent()) {
                // L'utilisateur a cliqué sur "OK", vous pouvez maintenant procéder à la modification
                // Mettre à jour les données du département avec les valeurs des champs de texte
                departement.setNomDept(dialog.getEditor().getText());
                departement.setChefDept(chefDeptField.getText());

                // Mettre à jour le département dans la base de données en utilisant Hibernate
                session.beginTransaction();
                session.update(departement);
                session.getTransaction().commit();

                // Afficher un message de confirmation
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Succès");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Département mis à jour avec succès.");
                successAlert.showAndWait();
                afficherDepartements();
                clearFields();
                
            } else {
                // L'utilisateur a cliqué sur "Annuler", ne faites rien
            }
        } else {
            // Afficher un message d'avertissement si le département n'est pas trouvé
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("Département introuvable.");
            alert.showAndWait();
            clearFields();
        }
    } catch (NumberFormatException e) {
        // Afficher un message d'erreur si l'ID du département n'est pas valide
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText("Veuillez saisir un ID de département valide.");
        alert.showAndWait();
        clearFields();
    } finally {
        session.close();
    }
}
    
    
    public void updateEmployee() {
    String employeeId = suppemptxt.getText();
    Session session = NewHibernateUtil.openSession();

    try {
        Employee employee = (Employee) session.get(Employee.class, Integer.parseInt(employeeId));
        if (employee != null) {
            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(5);

            gridPane.addRow(0, new Label("Nom de l'employé:"), new TextField(employee.getNom()));
            gridPane.addRow(1, new Label("Prénom de l'employé:"), new TextField(employee.getPrenom()));
            gridPane.addRow(2, new Label("ID du département:"), new TextField(String.valueOf(employee.getDepart().getId())));

            String dateNaissance = "";
            if (employee.getDatenaiss() != null) {
                dateNaissance = employee.getDatenaiss().toString();
            }
            TextField dateNaissanceField = new TextField(dateNaissance);
            dateNaissanceField.setPromptText("Date de naissance de l'employé");
            gridPane.addRow(3, new Label("Date de naissance de l'employé:"), dateNaissanceField);

            gridPane.addRow(4, new Label("Salaire de l'employé:"), new TextField(employee.getSalaire()));

            Optional<String> result = showDialog(gridPane);

            if (result.isPresent()) {
                employee.setNom(((TextField) gridPane.getChildren().get(1)).getText());
                employee.setPrenom(((TextField) gridPane.getChildren().get(3)).getText());
                
                // Modification de l'ID du département si nécessaire
                String newDepartId = ((TextField) gridPane.getChildren().get(5)).getText();
                if (!newDepartId.equals(String.valueOf(employee.getDepart().getId()))) {
                    // Récupérer le nouveau département à partir de la base de données
                    Depart newDepartement = (Depart) session.get(Depart.class, Integer.parseInt(newDepartId));
                    if (newDepartement == null) {
                        // Le nouveau département n'existe pas, afficher une alerte
                        showAlert(Alert.AlertType.ERROR, "Erreur", null, "Le département avec l'ID " + newDepartId + " n'existe pas.");
                        clearFields();
                        return; // Arrêter la mise à jour
                    }
                    // Associer l'employé à ce nouveau département
                    employee.setDepart(newDepartement);
                }

                employee.setDatenaiss(java.sql.Date.valueOf(((TextField) gridPane.getChildren().get(7)).getText()));
                employee.setSalaire(((TextField) gridPane.getChildren().get(9)).getText());

                session.beginTransaction();
                session.update(employee);
                session.getTransaction().commit();
                showAlert(Alert.AlertType.INFORMATION, "Succès", null, "Employé mis à jour avec succès.");
                afficherDonnees();
                clearFields();
            } else {
                // L'utilisateur a cliqué sur "Annuler", ne rien faire
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Attention", null, "Employé introuvable.");
            clearFields();
        }
    } catch (NumberFormatException e) {
        showAlert(Alert.AlertType.ERROR, "Erreur", null, "Veuillez saisir un ID d'employé valide.");
        clearFields();
    } finally {
        session.close();
    }
}
    // Méthode pour afficher une boîte de dialogue personnalisée avec un contenu personnalisé
    private Optional<String> showDialog(GridPane content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Confirmation");
        dialog.setHeaderText("Modifier les données de l'employé ?");
        dialog.setGraphic(null);
        dialog.getDialogPane().setContent(content);
        return dialog.showAndWait();
    }

    // Méthode pour afficher une boîte de dialogue d'alerte avec un type, un titre, un en-tête et un message donnés
    private void showAlert(Alert.AlertType type, String title, String header, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    
    
    
    
    public void searchDepartment() {
    // Récupérer l'ID du département à partir du champ de texte
    String departId = supptxt.getText();

    // Ouvrir une session Hibernate
    Session session = NewHibernateUtil.openSession();

    try {
        // Récupérer le département depuis la base de données en utilisant son ID
        Depart depart = (Depart) session.get(Depart.class, Integer.parseInt(departId));

        // Vérifier si le département existe
        if (depart != null) {
            // Créer un GridPane pour afficher les informations du département
            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(5);

            // Ajouter des labels pour les informations du département
            gridPane.addRow(0, new Label("ID du département:"), new Label(String.valueOf(depart.getId())));
            gridPane.addRow(1, new Label("Nom du département:"), new Label(depart.getNomDept()));
            gridPane.addRow(2, new Label("Chef du département:"), new Label(depart.getChefDept()));

            // Afficher une boîte de dialogue contenant les informations du département
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informations sur le département");
            alert.setHeaderText(null);
            alert.getDialogPane().setContent(gridPane);
            alert.showAndWait();
            clearFields();
        } else {
            // Afficher un message d'avertissement si le département n'est pas trouvé
            showAlert(Alert.AlertType.WARNING, "Attention", null, "Département introuvable.");
        }
    } catch (NumberFormatException e) {
        // Afficher un message d'erreur si l'ID du département n'est pas valide
        showAlert(Alert.AlertType.ERROR, "Erreur", null, "Veuillez saisir un ID de département valide.");
        clearFields();
    } finally {
        // Fermer la session Hibernate
        session.close();
    }
}

    
    public void searchEmployee() {
    // Récupérer l'ID de l'employé à partir du champ de texte
    String employeeId = suppemptxt.getText();

    // Ouvrir une session Hibernate
    Session session = NewHibernateUtil.openSession();

    try {
        // Récupérer l'employé depuis la base de données en utilisant son ID
        Employee employee = (Employee) session.get(Employee.class, Integer.parseInt(employeeId));

        // Vérifier si l'employé existe
        if (employee != null) {
            // Créer un GridPane pour afficher les informations de l'employé
            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(5);

            // Ajouter des labels pour les informations de l'employé
            gridPane.addRow(0, new Label("ID de l'employé:"), new Label(String.valueOf(employee.getId())));
            gridPane.addRow(1, new Label("Nom de l'employé:"), new Label(employee.getNom()));
            gridPane.addRow(2, new Label("Prénom de l'employé:"), new Label(employee.getPrenom()));
            gridPane.addRow(3, new Label("ID du département:"), new Label(String.valueOf(employee.getDepart().getId())));
            gridPane.addRow(4, new Label("Date de naissance de l'employé:"), new Label(employee.getDatenaiss().toString()));

            // Afficher une boîte de dialogue contenant les informations de l'employé
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informations sur l'employé");
            alert.setHeaderText(null);
            alert.getDialogPane().setContent(gridPane);
            alert.showAndWait();
            clearFields();
        } else {
            // Afficher un message d'avertissement si l'employé n'est pas trouvé
            showAlert(Alert.AlertType.WARNING, "Attention", null, "Employé introuvable.");
        }
    } catch (NumberFormatException e) {
        // Afficher un message d'erreur si l'ID de l'employé n'est pas valide
        showAlert(Alert.AlertType.ERROR, "Erreur", null, "Veuillez saisir un ID d'employé valide.");
    } finally {
        // Fermer la session Hibernate
        session.close();
    }
}


    
    
  public void saveDepartementDataToCSV() {
    // Ouvrir une session Hibernate
    Session session = NewHibernateUtil.openSession();

    try {
        // Récupérer tous les départements depuis la base de données
        List<Depart> departements = session.createQuery("FROM Depart").list();

        // Définir le chemin du fichier CSV
        String filePath = "C:\\Users\\hp\\Documents\\base de donnees projet\\departements.csv";

        // Créer un FileWriter pour écrire dans le fichier CSV
        try (FileWriter writer = new FileWriter(filePath)) {
            // Écrire l'en-tête dans le fichier CSV
            writer.append("ID du département,Nom du département,Chef du département\n");

            // Écrire les données des départements dans le fichier CSV
            for (Depart departement : departements) {
                writer.append(String.valueOf(departement.getId()))
                        .append(",")
                        .append(departement.getNomDept())
                        .append(",")
                        .append(departement.getChefDept())
                        .append("\n");
            }

            // Afficher une confirmation à l'utilisateur
            showAlert(Alert.AlertType.INFORMATION, "Succès", null, "Les données des départements ont été enregistrées dans le fichier departements.csv.");
        } catch (IOException e) {
            // Gérer les erreurs liées à l'écriture dans le fichier CSV
            showAlert(Alert.AlertType.ERROR, "Erreur", null, "Erreur lors de l'enregistrement des données des départements dans le fichier CSV.");
        }
    } finally {
        // Fermer la session Hibernate
        session.close();
    }
}

public void saveEmployeeDataToCSV() {
    // Ouvrir une session Hibernate
    Session session = NewHibernateUtil.openSession();

    try {
        // Récupérer tous les employés depuis la base de données
        List<Employee> employees = session.createQuery("FROM Employee").list();

        // Définir le chemin du fichier CSV
        String filePath = "C:\\Users\\hp\\Documents\\base de donnees projet\\employees.csv";

        // Créer un FileWriter pour écrire dans le fichier CSV
        try (FileWriter writer = new FileWriter(filePath)) {
            // Écrire l'en-tête dans le fichier CSV
            writer.append("ID de l'employé,Nom,Prenom,ID du département,Date de naissance,Salaire\n");

            // Écrire les données des employés dans le fichier CSV
            for (Employee employee : employees) {
                writer.append(String.valueOf(employee.getId()))
                        .append(",")
                        .append(employee.getNom())
                        .append(",")
                        .append(employee.getPrenom())
                        .append(",")
                        .append(String.valueOf(employee.getDepart().getId()))
                        .append(",")
                        .append(String.valueOf(employee.getDatenaiss()))
                        .append(",")
                        .append(String.valueOf(employee.getSalaire()))
                        .append("\n");
            }

            // Afficher une confirmation à l'utilisateur
            showAlert(Alert.AlertType.INFORMATION, "Succès", null, "Les données des employés ont été enregistrées dans le fichier employees.csv.");
        } catch (IOException e) {
            // Gérer les erreurs liées à l'écriture dans le fichier CSV
            showAlert(Alert.AlertType.ERROR, "Erreur", null, "Erreur lors de l'enregistrement des données des employés dans le fichier CSV.");
        }
    } finally {
        // Fermer la session Hibernate
        session.close();
    }
}




    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomDeDepartementColumn.setCellValueFactory(new PropertyValueFactory<>("nomDept"));
        chefDeDepartementColumn.setCellValueFactory(new PropertyValueFactory<>("chefDept"));
        
        idColumnfx.setCellValueFactory(new PropertyValueFactory<>("id"));
        departIdColumn.setCellValueFactory(new PropertyValueFactory<>("depart"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        dateNaissanceColumn.setCellValueFactory(new PropertyValueFactory<>("datenaiss"));
        salaireColumn.setCellValueFactory(new PropertyValueFactory<>("salaire"));
        afficherDepartements();
        afficherDonnees();
        

        

    }    

    public void actualiser(){
        afficherDepartements();
        afficherDonnees();
    }
}
