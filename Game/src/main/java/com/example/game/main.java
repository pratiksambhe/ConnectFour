package com.example.game;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class main extends Application {
    private  Controller controller;

    public void start(Stage primayStage) throws Exception{
        FXMLLoader fxmlLoader= new FXMLLoader(main.class.getResource("hello-view.fxml"));
        GridPane Gp= fxmlLoader.load();
        controller =fxmlLoader.getController();
        controller.Playground();
        MenuBar menuBar = createMenu();
        menuBar.prefWidthProperty().bind(primayStage.widthProperty());
        Pane menupane = (Pane) Gp.getChildren().get(0);
        menupane.getChildren().add(menuBar);
        Scene scene = new Scene(Gp);

        primayStage.setScene(scene);
        primayStage.setTitle("Connect Four");
        primayStage.setResizable(false);
        primayStage.show();
    }
    private MenuBar createMenu() {
        Menu fileMenu = new Menu("File");

        MenuItem newGame = new MenuItem("New game");
        newGame.setOnAction(actionEvent -> controller.resetGame());

        MenuItem Restart = new MenuItem("Restart");
        Restart.setOnAction(actionEvent -> controller.resetGame());

        SeparatorMenuItem s2 = new SeparatorMenuItem();

        MenuItem Quit = new MenuItem("Quit");
        Quit.setOnAction(actionEvent -> Exit());

        MenuBar menuBar = new MenuBar();
        fileMenu.getItems().addAll(newGame,Restart,s2,Quit);

        //help menu
        Menu helpMenu = new Menu("Help");

        MenuItem about =new MenuItem("About");
        about.setOnAction(actionEvent -> about());

        SeparatorMenuItem s3 = new SeparatorMenuItem();

        MenuItem me =new MenuItem("About Me");
        me.setOnAction(actionEvent -> me());

        helpMenu.getItems().addAll(about,s3,me);
        menuBar.getMenus().addAll(fileMenu,helpMenu);
        return menuBar;

    }

    private void me() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About me");
        alert.setHeaderText("Hello");
        alert.setContentText("my self Pratik i had developed this game for fun hope so you are enjoying the game " +
                "thankyou");
        alert.show();

    }

    private void about() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("How to play game?");
        alert.setContentText("Connect 4 is a two-player strategy game where the objective is to be the first to form a line of four of your colored discs. Players take turns dropping their discs into a vertical grid, which consists of seven columns and six rows. The discs fall to the lowest available space within the column.\n" +
                "\n" +
                "A player wins by arranging four consecutive discs in a row, either vertically, horizontally, or diagonally. The game continues until one player achieves this or the grid is completely filled, resulting in a draw if no one has connected four discs.");
        alert.show();

    }

    private void Exit() {
        Platform.exit();
        System.exit(0);

    }


    public static void main(String[] args) {
        launch(args);
    }

}