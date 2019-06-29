package controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Aplikacija;
import model.Korisnik;
import model.TipKorisnika;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField k_ime;

    @FXML
    private PasswordField lozinka;

    private Stage stage;


    private Aplikacija model;

    public LoginController(){
        this.model = Aplikacija.getInstance();
    }

    @FXML
    public void prijavljen(MouseEvent e){
        prijava();
    }

    @FXML
    public void enter_prijavljen(KeyEvent e){
        if(e.getCode() == KeyCode.ENTER) {
            prijava();
        }
    }

    void prijava(){
        Korisnik k = model.prijava(k_ime.getText(), lozinka.getText());
        if (k == null){
            k_ime.setStyle("-fx-border-color: red");
            lozinka.setStyle("-fx-border-color: red");
        } else if(k.getTip() == TipKorisnika.admin){
            model.setUlogovani(k);
            try {
                scenaAdminGlavna();
            }catch (Exception ex) {
                System.out.println("Couldn't load next scene");
                System.out.println(ex.getMessage());
            }
        } else if (k.getTip() == TipKorisnika.moderator){
            model.setUlogovani(k);
            try {
                scenaModeratorGlavna();
            }catch (Exception ex) {
                System.out.println("Couldn't load next scene");
                System.out.println(ex.getMessage());
            }
        }
    }

    public void scenaModeratorGlavna() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/moderator_main_view.fxml"));
        Parent root = loader.load();

        ModeratorMainController c = loader.getController();
        c.setStage(stage);

        stage.setScene(new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    public void scenaAdminGlavna() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/admin_main_view.fxml"));
        Parent root = loader.load();

        AdminMainController c = loader.getController();
        c.setStage(stage);

        stage.setScene(new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }


    public Aplikacija getModel() {
        return model;
    }

    public void setModel(Aplikacija model) {
        this.model = model;
    }
    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}