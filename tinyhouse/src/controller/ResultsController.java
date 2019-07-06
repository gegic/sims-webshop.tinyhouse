package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import model.Aplikacija;
import model.Proizvod;
import comparators.ProizvodComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ResultsController extends Controller {

    @FXML private TilePane tilePane;
    @FXML private ScrollPane scrollPane;
    @FXML private BorderPane borderPane;
    @FXML private ComboBox<TipSortiranja> cb;


    private Aplikacija model;

    private List<Proizvod> results;

    public ResultsController(){
        model = Aplikacija.getInstance();
        results = new ArrayList<>();
    }



    private void setNoResults() {
        Label prazno = new Label("Nema rezultata");
        prazno.setId("prazno");
        prazno.setStyle("../styles/style.css");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        tilePane.prefWidthProperty().bind(scrollPane.widthProperty());
        tilePane.prefHeightProperty().bind(scrollPane.heightProperty());
        tilePane.setOrientation(Orientation.VERTICAL);
        tilePane.setAlignment(Pos.CENTER);
        tilePane.setTileAlignment(Pos.CENTER);
        ImageView icon = new ImageView(
                new Image(getClass().getResourceAsStream("../styles/images/noresultsicon.png")));
        icon.setFitHeight(100);
        icon.setFitWidth(100);
        tilePane.getChildren().addAll(icon, prazno);
    }

    public void populate() {
        cb.getItems().setAll(
                TipSortiranja.CIJENA_RASTUCE, TipSortiranja.CIJENA_OPADAJUCE,
                TipSortiranja.NAZIV_RASTUCE, TipSortiranja.NAZIV_OPADAJUCE);
        TopBarController tbc = new TopBarController();
        borderPane.setTop(tbc.create());
        tilePane.prefWidthProperty().bind(scrollPane.widthProperty());

        if (results.size() == 0) {
            setNoResults();
        }

        for (Proizvod p : results) {
            ProizvodTileController c = new ProizvodTileController();
            tilePane.getChildren().add(c.create(p));
        }
    }
        @FXML
        public void akcija(ActionEvent e){
            if (cb.getSelectionModel().getSelectedItem() == TipSortiranja.CIJENA_RASTUCE) {
                Collections.sort(results, new ProizvodComparator("price_asc"));

            } else if (cb.getSelectionModel().getSelectedItem() == TipSortiranja.CIJENA_OPADAJUCE) {
                Collections.sort(results, new ProizvodComparator("price_desc"));


            } else if (cb.getSelectionModel().getSelectedItem() == TipSortiranja.NAZIV_RASTUCE) {
                Collections.sort(results, new ProizvodComparator("name_asc"));


            } else if (cb.getSelectionModel().getSelectedItem() == TipSortiranja.NAZIV_OPADAJUCE) {
                Collections.sort(results, new ProizvodComparator("name_desc"));
            }
            tilePane.getChildren().clear();
            for (Proizvod p : results) {
                ProizvodTileController c = new ProizvodTileController();
                tilePane.getChildren().add(c.create(p));
            }
        }



    @FXML
    public void povratak(ActionEvent e){
        GeneralMainController c = new GeneralMainController();
        SceneSwitcher.switchScene(c, "../view/general_main_view.fxml", "nebitno");
    }

    public TilePane getTilePane() {
        return tilePane;
    }


    public void setList(List<Proizvod> list) {
        results = list;
    }
}
