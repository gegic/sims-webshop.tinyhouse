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
import model.Prodavnica;
import model.Proizvod;

import java.io.IOException;

public class ModeratorItemController {

    @FXML
    private ListView<Proizvod> itemsList;

    private Stage stage;

    private Aplikacija model;

    public ModeratorItemController(){
        this.model = Aplikacija.getInstance();
    }

    public void populate(){
        ObservableList<Proizvod> observableList = FXCollections.observableList(model.proizvodi);
        itemsList.setItems(observableList);
        itemsList.setCellFactory(e -> new ItemCellController());
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void odjava(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/login_view.fxml"));
        Parent root = loader.load();
        model.setUlogovani(null);
        LoginController c = loader.getController();
        c.setStage(stage);
        stage.setScene(new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    @FXML
    public void povratak(ActionEvent e) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/moderator_main_view.fxml"));
        Parent root = loader.load();

        ModeratorMainController c = loader.getController();
        c.setStage(stage);
        stage.setScene(new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    @FXML
    public void addItem(ActionEvent e) throws IOException {
        /*FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/add_prodavnica_view.fxml"));
        Parent root = loader.load();

        AddProdavnicaController c = loader.getController();
        c.setStage(stage);
        stage.setScene(new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight()));*/
    }
}
