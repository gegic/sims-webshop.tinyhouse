package controller;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Aplikacija;
import model.Kategorija;
import model.Proizvod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ModifyProizvodController extends Controller {

    @FXML private TextField tfIdProizvoda;
    @FXML private TextField tfNaziv;
    @FXML private TextArea taOpis;
    @FXML private Pane pane1;
    @FXML private Pane pane2;
    @FXML private Pane pane3;
    @FXML private Label lbDodaj1;
    @FXML private Label lbDodaj2;
    @FXML private Label lbDodaj3;
    @FXML private ImageView iv1;
    @FXML private ImageView iv2;
    @FXML private ImageView iv3;
    @FXML private Button btDodavanje;
    @FXML private Label warning;
    @FXML private TextField tfCijena;
    @FXML private ComboBox<Kategorija> cbKategorija;
    private Path cwd;
    private Image images[];

    private String paths[];
    private Stage stage;

    private Aplikacija model;

    public ModifyProizvodController(){
        this.model = Aplikacija.getInstance();
        cwd = Paths.get(".").toAbsolutePath().normalize();
        System.out.println(cwd.toString());
    }

    @FXML
    public void enter_izmena(KeyEvent e){
        if(e.getCode() == KeyCode.ENTER){
            izmena();
        }
    }
    @FXML
    public void izmena(ActionEvent e){
        izmena();
    }

    private String provera(int id, String naziv, String opis, Image[] slike, float cijena, Kategorija k){
        if(slike[0] == null){
            return "Mora se postaviti bar jedna slika";
        }
        if(cijena <= 0){
            return "Cijena mora da prelazi 0 dinara";
        }
        if(k == null){
            return "Mora se izabrati kategorija";
        }
        if(naziv.length() < 3 || naziv.length() > 15){
            return "Naziv proizvoda mora imati između 3 i 15 karaktera";
        }
        if(opis.length() < 3 || opis.length() > 255){
            return "Opis proizvoda mora imati između 3 i 255 karaktera";
        }
        return "";
    }

    public void izmena(){
        String message;
        try {
            int id = Integer.valueOf(tfIdProizvoda.getText());
            float cijena = Float.valueOf(tfCijena.getText());
            Kategorija selected = cbKategorija.getSelectionModel().getSelectedItem();
            if((message = provera(id, tfNaziv.getText(), taOpis.getText(), images, cijena, selected)).equals("")){
                model.izmeniProizvod(id, tfNaziv.getText(), taOpis.getText(), images, paths, cijena, selected);
                povratak();
            }
            else{
                warning.setTextFill(Color.RED);
                warning.setText(message);
                tfIdProizvoda.setStyle("-fx-border-color: red");
                tfNaziv.setStyle("-fx-border-color: red");
                taOpis.setStyle("-fx-border-color: red");
                tfCijena.setStyle("-fx-border-color: red");            }
        } catch (NumberFormatException e){
            warning.setTextFill(Color.RED);
            warning.setText("Sva polja moraju biti logično popunjena");
            tfIdProizvoda.setStyle("-fx-border-color: red");
            tfNaziv.setStyle("-fx-border-color: red");
            tfCijena.setStyle("-fx-border-color: red");
            taOpis.setStyle("-fx-border-color: red");
        }
    }

    @FXML
    public void povratak(ActionEvent e) throws IOException {
        povratak();
    }

    public void povratak() {
        ModeratorItemsController c = new ModeratorItemsController();
        SceneSwitcher.switchScene(c, "../view/moderator_items_view.fxml", true);
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void setBoundaries(){
        tfCijena.textProperty().addListener(
                (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    if (!newValue.matches("^\\d+\\.?\\d*$")) newValue = newValue.replaceAll("((?!\\.)\\D+\\.?)", "");
                    tfCijena.setText(newValue);
                });
        tfIdProizvoda.textProperty().addListener(
                (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    if (!newValue.matches("\\d*")) newValue = newValue.replaceAll("[^\\d]", "");
                    tfIdProizvoda.setText(newValue);
                });
    }


    public void setInfo(Object o){
        Proizvod p = (Proizvod) o;
        ObservableList<Kategorija> observableList = FXCollections.observableList(model.getKategorije());
        cbKategorija.setItems(observableList);
        setBoundaries();
        this.tfIdProizvoda.setText(String.valueOf(p.getId()));
        tfNaziv.setText(p.getNaziv());
        taOpis.setText(p.getOpis());
        tfCijena.setText(String.valueOf(p.getTrenutnaCijena().getJedinicnaCena()));
        cbKategorija.getSelectionModel().select(p.getKategorija());
        images = p.getSlike();
        paths = p.getPaths();
        if(images[0] != null){
            iv1.setImage(images[0]);
            pane2.setDisable(false);
            lbDodaj1.setVisible(false);
            btDodavanje.setDisable(false);
        }
        if(images[1] != null){
            iv2.setImage(images[1]);
            pane3.setDisable(false);
            lbDodaj2.setStyle("-fx-background-color: white");
            lbDodaj2.setText("ukloni sliku");

        }
        if(images[2] != null){
            iv3.setImage(images[2]);
            lbDodaj3.setStyle("-fx-background-color: white");
            lbDodaj3.setText("ukloni sliku");
            lbDodaj2.setText("slika 2");
            lbDodaj2.setVisible(false);
        }

    }

    @FXML
    public void selectImage1(MouseEvent e){

        File selectedFile = izbor_slike();
        if(selectedFile != null){
            paths[0] = cwd.relativize(Paths.get(selectedFile.toURI())).toString();
            images[0] = new Image(selectedFile.toURI().toString());
            iv1.setImage(images[0]);
            pane2.setDisable(false);
            lbDodaj1.setVisible(false);
            btDodavanje.setDisable(false);
        }
    }

    @FXML
    public void selectImage2(MouseEvent e){
        if(lbDodaj2.getText().equals("ukloni sliku")){
            images[1] = null;
            paths[1] = null;
            iv2.setImage(null);
            pane3.setDisable(true);
            lbDodaj2.setStyle("-fx-background-color: transparent");
            lbDodaj2.setText("dodaj sliku");
        } else{
            File selectedFile = izbor_slike();
            if(selectedFile != null){
                paths[1] = cwd.relativize(Paths.get(selectedFile.toURI())).toString();
                images[1] = new Image(selectedFile.toURI().toString());
                iv2.setImage(images[1]);
                pane3.setDisable(false);
                lbDodaj2.setStyle("-fx-background-color: white");
                lbDodaj2.setText("ukloni sliku");
            }
        }
    }

    @FXML
    public void selectImage3(MouseEvent e){
        if(lbDodaj3.getText().equals("ukloni sliku")){
            images[2] = null;
            paths[2] = null;
            iv3.setImage(null);
            lbDodaj3.setStyle("-fx-background-color: transparent");
            lbDodaj3.setText("dodaj sliku");
            lbDodaj2.setText("ukloni sliku");
            lbDodaj2.setVisible(true);
        } else {
            File selectedFile = izbor_slike();
            if (selectedFile != null) {
                paths[2] = cwd.relativize(Paths.get(selectedFile.toURI())).toString();
                images[2] = new Image(selectedFile.toURI().toString());
                iv3.setImage(images[2]);
                lbDodaj3.setStyle("-fx-background-color: white");
                lbDodaj3.setText("ukloni sliku");
                lbDodaj2.setText("slika 2");
                lbDodaj2.setVisible(false);
            }
        }
    }

    private File izbor_slike(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Slecet image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        return selectedFile;
    }
}
