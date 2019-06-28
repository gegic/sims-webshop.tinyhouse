package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.Aplikacija;
import model.Korisnik;

import java.io.IOException;

public class AdminUsersController {

    @FXML
    private ListView<Korisnik> modList;

    private Stage stage;

    private Aplikacija model;

    public AdminUsersController(){
        this.model = Aplikacija.getInstance();
    }

    public void populate(){
        ObservableList<Korisnik> observableList = FXCollections.observableList(model.korisnici);
        modList.setItems(observableList);
        modList.setCellFactory(e -> new KorisnikCellController());
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void odjava(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/login_scene.fxml"));
        Parent root = loader.load();
        model.setUlogovani(null);
        Controller c = loader.getController();
        c.setStage(stage);
        stage.setScene(new Scene(root, 800, 600));
    }

    @FXML
    public void povratak(ActionEvent e) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/admin_main_scene.fxml"));
        Parent root = loader.load();

        AdminController c = loader.getController();
        c.setStage(stage);
        stage.setScene(new Scene(root, 800, 600));
    }
}
