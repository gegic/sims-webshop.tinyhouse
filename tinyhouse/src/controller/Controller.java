package controller;

import javafx.stage.Stage;



public abstract class Controller {
    protected Stage stage;

    public Stage getStage() {return stage;}
    public void setStage(Stage s){
        this.stage = s;
    }

    public void populate(){};

    public void setInfo(Object o){};

}
