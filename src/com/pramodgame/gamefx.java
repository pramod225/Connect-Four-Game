package com.pramodgame;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class gamefx extends Application {

    public Controller control;

    @Override
    public void start(Stage primaryStage) throws Exception {



    	FXMLLoader loder = new FXMLLoader(getClass().getResource("gamef.fxml"));
        GridPane grid = loder.load();

        MenuBar menuBar = createmenu();

        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());// this is for manage propery of menu

	    Pane pane=(Pane) grid.getChildren().get(0);// start with 0.top menu
	    pane.getChildren().add(menuBar);// this for show menubar

	    control = loder.getController();// this is for control gridpane
	    control.createplayground();// call the method here

        Scene sc = new Scene(grid);

        primaryStage.setScene(sc);
        primaryStage.setTitle("GUI GAME");
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    public MenuBar createmenu() {

        Menu filemenu = new Menu("File");
        MenuItem newgame = new MenuItem("New game");
        newgame.setOnAction(event -> control.resetgame());

        MenuItem resetgame = new MenuItem("Reset");
        resetgame.setOnAction(event -> control.resetgame());
        SeparatorMenuItem sep = new SeparatorMenuItem();

        MenuItem exit = new MenuItem("Exit game");
        exit.setOnAction(event -> Exit());

        filemenu.getItems().addAll(newgame, resetgame, sep, exit);

        Menu help = new Menu("Help");

        MenuItem about = new MenuItem("About game");
        about.setOnAction(event -> aboutgame());
        SeparatorMenuItem sep1 =new SeparatorMenuItem();

        MenuItem aboutme =new MenuItem("About me ");
        aboutme.setOnAction(event -> aboutme());

        help.getItems().addAll(about,sep1,aboutme);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(filemenu, help);

        return menuBar;

    }

    private void Exit() {

        Platform.exit();
        System.exit(0);
    }

    private void aboutme() {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ABOUT DEVELOPER");
        alert.setHeaderText("Pramod Kumar");
        alert.setContentText("This is my first developed GAME that i have created with coding.This is GUI game.Every one " +
                "" +"can play very easily.Enjoy this game.I love it.");
        alert.show();
    }

    private void aboutgame() {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ABOUT GAME");
        alert.setHeaderText("How to play");
        alert.setContentText("GUI GAME is a two-player connection game in which the players first choose a color and then take turns dropping colored " +
                "discs from the top into a seven-column, six-row vertically suspended grid. The pieces fall straight down, occupying the next available" +
                " space within the column. The objective of the game is to be the first to form a horizontal, vertical, or diagonal line " +
                "of four of one's own discs. Connect Four is a solved game. The first player can always win by playing the right moves.");
        alert.show();


    }


    public static void main(String[] args) {

        launch(args);
    }
}
