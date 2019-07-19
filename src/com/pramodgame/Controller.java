package com.pramodgame;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {

    private static final int Colums=9;
    private static final int Rows=8;
    private static final int circle_diemeter=80;
    public  static String player1,playe2;
    private static final String diccolor1="#24303e";
    private static final String diccolor2="#0000ff";
    private boolean isplayerone=true;
    private Disc[][] insertdiscarrey=new Disc[Rows][Colums];// for structure changes

    @FXML
    public GridPane gridpane;
    @FXML
    public Pane toppane;
    @FXML
    public Pane leftpane;

    @FXML
    public Label playerone;

    @FXML
    public Label turn;

    @FXML
    public TextField textfield1,textfield2;;

    @FXML
    public Button setbutton;

    private boolean isallowedplayer=true;// this is for avaoid the multiple time disc

    public void  createplayground() {
        Shape shapewidth = creategamestructure();// call here method

        gridpane.add(shapewidth, 0, 1);

        List<Rectangle> rectangleList = createclickablecol();

        for (Rectangle rectangle : rectangleList) {
            gridpane.add(rectangle, 0, 1);
        }

			setbutton.setOnAction(event -> {
                setnames();
               // invalid();
            });
    }

	private void setnames() {

		//only take string value  don't take integer or other values
		player1=textfield1.getText();
		playe2=textfield2.getText();

		playerone.setText(isplayerone?player1:playe2);

	}

	private void invalid() {

		Alert alert=new Alert(Alert.AlertType.ERROR);
		alert.setTitle("ERROR");
		alert.setHeaderText("INVALID INPUT");
		alert.setContentText("Please enter valid input only accept names");
		alert.show();
	}


	public Shape creategamestructure() {

        // to create game structures of circle

        Shape shapewidth = new Rectangle((Colums+1) * circle_diemeter, (Rows +1)* circle_diemeter);

        for (int row = 0; row < Rows; row++) {

            for (int col = 0; col < Colums; col++) {


                Circle circle = new Circle();
                circle.setRadius(circle_diemeter / 2);
                circle.setCenterX(circle_diemeter / 2);
                circle.setCenterY(circle_diemeter / 2);
                circle.setTranslateX(col * (circle_diemeter + 5) + (circle_diemeter / 4));// this is for margin space right on x
                circle.setTranslateY(row * (circle_diemeter + 5) + (circle_diemeter / 4));// this is for margin space bottom on y
                circle.setSmooth(true);
                shapewidth=Shape.subtract(shapewidth,circle);

            }
        }

        shapewidth.setFill(Color.WHITE);
        return shapewidth;
       }

       private  List<Rectangle> createclickablecol() {
           List<Rectangle> rectangleList = new ArrayList<>();

           for (int col=0;col<Colums;col++) {

               Rectangle ractangle = new Rectangle(circle_diemeter, (Rows + 1) * circle_diemeter);// this is for create click color
               ractangle.setFill(Color.TRANSPARENT);
               ractangle.setTranslateX(col * (circle_diemeter + 5)+circle_diemeter/4);// this is for transparent hover color
               rectangleList.add(ractangle);

               ractangle.setOnMouseClicked(event -> ractangle.setFill(Color.RED));
               ractangle.setOnMouseClicked(event -> ractangle.setFill(Color.TRANSPARENT));

               final int  column=col;
               ractangle.setOnMouseClicked(event -> {
                   if(isallowedplayer) {
                       isallowedplayer = false;// this is for avoid multiple disc on multiple time clicked
                       insertdisc(new Disc(isplayerone), column);

                   }
               });

           }
           return rectangleList;
    }

        private  void insertdisc(Disc disc ,int column){
        // this is for add disc one by one on top
        int row=Rows-1;

        while(row>=0) {

            if (getdiscpresent(row,column) == null)
                break;
            row--;
        }
            if(row<0)// for full disc u can't enter more disc
                return;


            insertdiscarrey[row][column]=disc;// for structure change for developer

            leftpane.getChildren().add(disc);
            disc.setTranslateX(column * (circle_diemeter + 5)+circle_diemeter/4);

          int currentrow=row;
            TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.5),disc);

            translateTransition.setToY(row* (circle_diemeter + 5)+circle_diemeter/4);
            translateTransition.setOnFinished(event -> {

                isallowedplayer=true;// this is for turn to next player

                if (gameEnded(currentrow, column)) {
                    gameover();

                }
                isplayerone=!isplayerone;
                playerone.setText(isplayerone?player1:playe2);// set names one by one on lable


            });

            translateTransition.play();

        }
        private boolean gameEnded(int row ,int column){

        List<Point2D> verticalline= IntStream.rangeClosed(row-3,row+3)//range of the value 0,1,2,3,4,5
                .mapToObj(r-> new Point2D(r,column))//0,3 1,3 2,3 3,3 4,3 5,3
                .collect(Collectors.toList());


            List<Point2D> horizontalline= IntStream.rangeClosed(column-3,column+3)//range of the value 0,1,2,3,4,5
             .mapToObj(col-> new Point2D(row,col))//0,3 1,3 2,3 3,3 4,3 5,3
            .collect(Collectors.toList());

            Point2D startpoint1= new Point2D(row-3, column+3);
            List<Point2D>diagnalpoints1=IntStream.rangeClosed(0,6)
                    .mapToObj(i->startpoint1.add(i, -i))
                    .collect(Collectors.toList());

            Point2D startpoint2= new Point2D(row-3, column-3);
            List<Point2D>diagnalpoints2=IntStream.rangeClosed(0,6)
                    .mapToObj(i->startpoint2.add(i, i))
                    .collect(Collectors.toList());

            boolean isEnded= checkcombination(verticalline)||checkcombination(horizontalline)
                    ||checkcombination(diagnalpoints1)||checkcombination(diagnalpoints2);

            return isEnded;
        }
         private boolean checkcombination(List<Point2D> points){

              int chain=0;

             for (Point2D point : points){

                 int rowarreyindex= (int) point.getX();
                int colunmarreyindex= (int) point.getY();

            Disc disc=getdiscpresent(rowarreyindex,colunmarreyindex);

            if(disc !=null && disc.isplayeronemove==isplayerone) { // this is for disc is empty

                chain++;

                if (chain == 4) {// this is for check combination of disc
                    return true;

                }
            }

            else{
                    chain =0; // this is for no combination
                }

            }



             return false;

         }
          private Disc getdiscpresent(int row, int column){ //to prevent arreyindexout of bound


            if(row >=Rows ||row <0|| column>=Colums || column <0)// if row or column index is invalid
            return null;

            return  insertdiscarrey[row][column];

          }

        private void gameover(){
         String winner = isplayerone ? player1:playe2;//Announce winner name here
         System.out.println("Winner is :" + winner);

            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("GUI GAME CONNECT 4");
            alert.setHeaderText("Winner is :"+ winner);
            alert.setContentText("Do you want to play again");

            ButtonType yesbtn=new ButtonType("YES");
            ButtonType nobtn=new ButtonType("NO");
            alert.getButtonTypes().setAll(yesbtn,nobtn);

            Platform.runLater(()-> { // this is for execute code later on

                Optional<ButtonType> clicked = alert.showAndWait();
                if (clicked.isPresent() && clicked.get() == yesbtn) {  // this is for clicked yes until button present

                    resetgame();
                } else {
                    Platform.exit();
                    System.exit(0);

                }
            });

    }

    public void resetgame() {
    	leftpane.getChildren().clear();// remove all disc from pane
        textfield1.clear();
        textfield2.clear();

	    for (int row=0;row<insertdiscarrey.length;row++){//rhis is for remove all  row disc elements

                for(int col=0;col<insertdiscarrey[row].length;col++){// this is  remove for all col disc elements
                    insertdiscarrey[row][col]=null;


                }

            }
            isplayerone=true;
            playerone.setText("");// turn first player again

            createplayground();// refresh play ground

    }


    // this is for create players to change color
        private static class Disc extends Circle{

        private boolean isplayeronemove;

          public Disc( boolean isplayeronemove){

               this.isplayeronemove=isplayeronemove;// move player
               setRadius(circle_diemeter/2);
               setFill(isplayeronemove? Color.valueOf(diccolor1):Color.valueOf(diccolor2));

	          setCenterX(circle_diemeter/2);
               setCenterY(circle_diemeter/2);

          }
        }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
