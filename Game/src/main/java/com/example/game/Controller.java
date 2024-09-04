package com.example.game;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {
    private static final int COLUMNS = 7;
    private static final int ROW = 6;
    private static final int CD = 80;
    private static final String color1 = "#24303E";
    private static final String color2 = "#301934";

    private static String Player1 = "Player One";
    private static String Player2 = "Player Two";

    private boolean isPlayer1Turn = true;

    private Disc[][] insert = new Disc[ROW][COLUMNS];

    @FXML
    public GridPane Gp;

    @FXML
    public Label play1;
    @FXML
    public Pane P2;

    private boolean allow=true;
    public void Playground() {
        Shape rectanglewhole = creategames();
        Gp.add(rectanglewhole, 0, 1);
        List<Rectangle> rectangleList = clickedcolumn();
        for (Rectangle rec : rectangleList) {
            Gp.add(rec, 0, 1);
        }
    }

    private Shape creategames() {
        Shape rectanglewhole = new Rectangle((COLUMNS + 1) * CD, (ROW + 1) * CD);

        for (int row = 0; row < ROW; row++) {

            for (int col = 0; col < COLUMNS; col++) {
                Circle circle = new Circle();
                circle.setRadius(CD / 2);
                circle.setCenterX(CD / 2);
                circle.setCenterY(CD / 2);
                circle.setSmooth(true);
                circle.setTranslateX(col * (CD + 5) + CD / 4);
                circle.setTranslateY(row * (CD + 5) + CD / 4);
                rectanglewhole = Shape.subtract(rectanglewhole, circle);

            }
        }


        rectanglewhole.setFill(Color.WHITE);
        return rectanglewhole;

    }

    private List<Rectangle> clickedcolumn() {
        List<Rectangle> rectangleList = new ArrayList<>();
        for (int col = 0; col < COLUMNS; col++) {
            Rectangle rec = new Rectangle(CD, (ROW + 1) * CD);
            rec.setFill(Color.TRANSPARENT);
            rec.setTranslateX(col * (CD + 5) + CD / 4);

            rec.setOnMouseEntered(event -> rec.setFill(Color.valueOf("#eeeeee26")));
            rec.setOnMouseExited(event -> rec.setFill(Color.TRANSPARENT));
            final int column = col;
            rec.setOnMouseClicked(event -> {
                if(allow){
                    allow = false;
                    insetDisc(new Disc(isPlayer1Turn), column);
                }
            });

            rectangleList.add(rec);

        }
        return rectangleList;
    }

    private void insetDisc(Disc disc, int column) {
        int row = ROW - 1;
        while (row >= 0) {
            if (getDiscPresent(row, column) == null)
                break;
            row--;
        }
        if (row < 0)
            return;
        insert[row][column] = disc;
        P2.getChildren().add(disc);
        disc.setTranslateX(column * (CD + 5) + CD / 4);
        int currentrow = row;
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.25), disc);
        translateTransition.setToY(row * (CD + 5) + CD / 4);
        translateTransition.setOnFinished(event -> {
            allow=true;
            if (gameisend(currentrow, column)) {
                gameOver();
                return;

            }
            isPlayer1Turn = !isPlayer1Turn;
            play1.setText(isPlayer1Turn ? Player1 : Player2);
        });
        translateTransition.play();

    }


    private boolean gameisend(int row, int column) {
        List<Point2D> verticaalpoint = IntStream.rangeClosed(row - 3, row + 3)
                .mapToObj(r -> new Point2D(r, column)).collect(Collectors.toList());
        List<Point2D> horizantalpoint = IntStream.rangeClosed(column - 3, column + 3)
                .mapToObj(coll -> new Point2D(row, coll)).collect(Collectors.toList());

        Point2D startpoint1 = new Point2D(row - 3, column + 3);
        List<Point2D> digonalpoint = IntStream.rangeClosed(0, 6).mapToObj(i -> startpoint1.add(i, -i)).collect(Collectors.toList());

        Point2D startpoint2 = new Point2D(row - 3, column - 3);
        List<Point2D> digonalpoint1 = IntStream.rangeClosed(0, 6).mapToObj(i -> startpoint2.add(i, i)).collect(Collectors.toList());

        boolean isEnded = checkCombination(verticaalpoint) ||
                checkCombination(horizantalpoint) || checkCombination(digonalpoint) || checkCombination(digonalpoint1);
        return isEnded;
    }


    private boolean checkCombination(List<Point2D> points) {
        int chain = 0;
        for (Point2D point : points) {
            int rowindex = (int) point.getX();
            int columnindex = (int) point.getY();
            Disc dics = getDiscPresent(rowindex, columnindex);
            if (dics != null && dics.isplayone == isPlayer1Turn) {
                chain++;
                if (chain == 4) {
                    return true;
                      }
                } else {
                    chain = 0;
                }
            }
            return false;
        }

        private Disc getDiscPresent ( int row, int column){
            if (row >= ROW || row < 0 || column >= COLUMNS || column < 0)
                return null;

            return insert[row][column];

        }
        private void gameOver () {
            String winner = isPlayer1Turn ? Player1 : Player2;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Connect Four");
            alert.setHeaderText("The winner is: "+winner);
            alert.setContentText("Do you want to play again?");
            ButtonType ybtn = new ButtonType("Yes");
            ButtonType nbtn = new ButtonType("No");
            alert.getButtonTypes().setAll(ybtn,nbtn);
            Platform.runLater(()->{
                Optional<ButtonType>btnc = alert.showAndWait();
                if(btnc.isPresent() && btnc.get()==ybtn){
                    resetGame();
                }else {
                    Platform.exit();
                    System.exit(0);
                }
            });
        }

    public void resetGame() {
        P2.getChildren().clear();
        for (int row =0;row<insert.length;row++){
            for(int col = 0; col<insert[row].length;col++){
                insert[row][col]=null;
            }
        }
        isPlayer1Turn = true;
        play1.setText(Player1);
    }


    private static class Disc extends Circle{
        private final boolean isplayone;
        public Disc (boolean isplayone){
        this.isplayone=isplayone;
        setRadius(CD/2);
        setFill(isplayone?Color.valueOf(color1):Color.valueOf(color2));
        setCenterX(CD/2);
        setCenterY(CD/2);
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}