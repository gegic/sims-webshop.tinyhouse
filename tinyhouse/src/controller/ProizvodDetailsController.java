package controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.Aplikacija;
import model.Proizvod;
import model.StavkaNarudzbine;

import java.util.ArrayList;


public class ProizvodDetailsController extends Controller {

    @FXML private Button btPretraga;
    @FXML private TextField tfPretraga;
    @FXML private Label lbNaziv;
    @FXML private Text textOpis;
    @FXML private Label lbCena;
    @FXML private ImageView ivPhoto;
    @FXML private ImageView iv1;
    @FXML private ImageView iv2;
    @FXML private ImageView iv3;
    @FXML private HBox hbox1;
    @FXML private HBox hbox2;
    @FXML private HBox hbox3;
    @FXML private Button btDodaj;

    private Aplikacija model;
    private Proizvod proizvod;
    public ProizvodDetailsController(){
        model = Aplikacija.getInstance();

    }

    @FXML
    public void dodajUKorpu(ActionEvent e){
        StavkaNarudzbine s = new StavkaNarudzbine(1, proizvod.getTrenutnaCijena().getJedinicnaCena(), proizvod);
        model.addToKorpa(s);
        btDodaj.setDisable(true);
    }

    @FXML
    public void povratak(ActionEvent e){
        GeneralMainController c = new GeneralMainController();
        SceneSwitcher.switchScene(c, "../view/general_main_view.fxml");
    }

    @FXML
    private void enter_pretraga(KeyEvent e){
        if(e.getCode() == KeyCode.ENTER){
            pretraga();
        }
    }

    @FXML
    public void pretraga(ActionEvent e){
        pretraga();
    }


    public void pretraga(){
        if(btPretraga.getText().equals("Pretraga")){
            tfPretraga.setPrefWidth(120);
            btPretraga.setText("");
            btPretraga.setStyle("-fx-background-radius:30");
            tfPretraga.requestFocus();
        } else {
            tfPretraga.setPrefWidth(0);
            btPretraga.setText("Pretraga");
            btPretraga.setStyle("-fx-background-radius:15");
            ArrayList<Proizvod> ps = search(tfPretraga.getText());
            tfPretraga.setText("");
            ResultsController c = new ResultsController();
            try {
                FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource("../view/results_view.fxml"));
                Parent root = loader.load();
                c = loader.getController();
                c.setList(ps);
                c.setStage(stage);
                c.populate();
                stage.setScene(new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight()));
            } catch(Exception ex){
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    @FXML
    public void prijava(ActionEvent e){
        LoginController c = new LoginController();
        SceneSwitcher.switchScene(c, "../view/login_view.fxml");
    }

    @FXML
    public void korpa(ActionEvent e){
        KorpaController c = new KorpaController();
        SceneSwitcher.switchScene(c, "../view/korpa_view.fxml", true, "nema nista");
    }

    private ArrayList<Proizvod> search(String term){
        ArrayList<Proizvod> proizvodi = new ArrayList<>();
        for(Proizvod p : model.proizvodi){
            if(p.getNaziv().toLowerCase().contains(term.toLowerCase())) proizvodi.add(p);
        }
        return proizvodi;
    }

    @FXML
    public void setImage1(){
        ivPhoto.setImage(iv1.getImage());
    }

    @FXML
    public void  setImage2(){
        ivPhoto.setImage(iv2.getImage());
    }

    @FXML
    public void setImage3(){
        ivPhoto.setImage(iv3.getImage());
    }


    private void setTextSizes(Number newWidth){
        int size = (int)newWidth.doubleValue() / 40;
        int opisSize = (int) size * 3 / 4;
        int opisWidth = (int) newWidth.doubleValue() / 4;
        if(size > 28) size = 28;
        lbNaziv.setStyle("-fx-font-size: " + size + ";"
                +"-fx-font-weight: bold");
        lbCena.setStyle("-fx-font-size: " + size + ";"
                +"-fx-font-weight: bold");
        if(opisSize > 24) opisSize = 24;
        textOpis.setStyle("-fx-font-size: " + opisSize);
        if(opisWidth > 400) opisWidth = 400;
        textOpis.prefWidth(opisWidth);
    }

    private void setImageSize(Number newHeight){
        int size = (int)newHeight.doubleValue() / 2;
        if(size > 550) size = 550;
        ivPhoto.setFitWidth(size);
        ivPhoto.setFitHeight(size);
    }
    public void setInfo(String id){
        setTextSizes(stage.getWidth());
        setImageSize(stage.getHeight());
        stage.widthProperty().addListener(
        (ObservableValue<? extends Number> observableValue, Number oldWidth, Number newWidth) -> {
            setTextSizes(newWidth);
        });

        stage.heightProperty().addListener(
        (ObservableValue<? extends Number> observableValue, Number oldHeight, Number newHeight) -> {
            setImageSize(newHeight);
        });

        stage.maximizedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
            setTextSizes(stage.getWidth());
            setImageSize(stage.getWidth());
        });
        proizvod = model.pronadjiProizvod(Integer.valueOf(id));
        if(proizvod.getKolicinaZaOnline() == 0){
            btDodaj.setDisable(true);
        }
        if(model.getTrenutnaKorpa().contains(proizvod)){
            btDodaj.setDisable(true);
        }
        lbNaziv.setText(proizvod.getNaziv());
        lbCena.setText(String.valueOf(proizvod.getTrenutnaCijena().getJedinicnaCena()) + " RSD");
        textOpis.setText(proizvod.getOpis());
        Image[] images = proizvod.getSlike();
        ivPhoto.setImage(images[0]);
        iv2.setImage(images[0]);
        if(images[1] != null) {
            iv1.setImage(proizvod.getSlike()[1]);
            hbox1.setDisable(false);
        }
        if(images[2] != null) {
            iv3.setImage(proizvod.getSlike()[2]);
            hbox3.setDisable(false);
        }

    }
}
