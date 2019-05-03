package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.sum;


import java.time.format.TextStyle;


public class FXNet extends Application{

    private boolean isServer = false;
    int score = 0;
    String newIpAddress;
    TextField getIp;
    int newPort;
    TextField getPort;
    Button btn, btn2, btn3, btn4, btn5, btn6, btn7;
    Button connect = new Button("Connect to Server");
    Button rock, paper, scissors, lizard, spock;
    Scene firstScene;
    Client conn;
    //private NetworkConnection  conn = isServer ? createServer() : createClient();
    private TextArea messages = new TextArea();


    private Scene createContent() {
		/*messages.setPrefHeight(550);
		TextField input = new TextField();
		
		input.setOnAction(event -> {
			String message = isServer ? "Server: " : "Client: ";
			message += input.getText();
			input.clear();
			
			messages.appendText(message + "\n");
			try {
				conn.send(message);
			}
			catch(Exception e) {
				
			}
			
		});
		
		VBox root = new VBox(20, messages, input);
		root.setPrefSize(600, 600);
		
		return root;*/
        btn4 = new Button();
        btn4.setText("Enter IP Address and Port");

        getIp = new TextField();
        getPort = new TextField();
        getPort.setPrefWidth(60);

        BorderPane centerPane = new BorderPane();
        HBox layout = new HBox(0);
        layout.getChildren().add(btn4);
        layout.getChildren().add(getIp);
        layout.getChildren().add(getPort);
        layout.getChildren().add(connect);
        TextArea messages2 = new TextArea();
        messages2.setPrefHeight(550);
        centerPane.setCenter(layout);
        Scene firstScene = new Scene(centerPane);

        return firstScene;


    }
    private Scene createClientGUI(Stage primaryStage) {
        messages.setPrefHeight(250);
        /*TextField input = new TextField();

        input.setOnAction(event -> {
            String message = isServer ? "Server: " : "Client: ";
            message += input.getText();
            input.clear();

            messages.appendText(message + "\n");
            try {
                conn.send(message);
            }
            catch(Exception e) {

            }

        });*/

        btn2 = new Button();
        btn2.setText("Exit");
        btn2.setOnAction(event -> {
            try {
                conn.send("Exit");
                primaryStage.close();
            }
            catch (Exception invalidHand){

            }
        });
        btn3 = new Button();
        btn3.setText("Play Again");
        btn3.setOnAction(event -> {
            try {
                conn.send("Play Again");
            }
            catch (Exception invalidHand){

            }
        });

        // Clickable Buttons
        rock = new Button();
        rock.setText("Rock");
        rock.setOnAction(event -> {
            try {
                conn.send("User Played Rock");
            }
            catch (Exception invalidHand){

            }
        });
        paper = new Button();
        paper.setText("Paper");
        paper.setOnAction(event -> {
            try {
                conn.send("User Played Paper");
            }
            catch (Exception invalidHand){

            }
        });
        scissors = new Button();
        scissors.setText("Scissors");
        scissors.setOnAction(event -> {
            try {
                conn.send("User Played Scissors");
            }
            catch (Exception invalidHand){

            }
        });
        lizard = new Button();
        lizard.setText("Lizard");
        lizard.setOnAction(event -> {
            try {
                conn.send("User Played Lizard");
            }
            catch (Exception invalidHand){

            }
        });
        spock = new Button();
        spock.setText("Spock");
        spock.setOnAction(event -> {
            try {
                conn.send("User Played Spock");
            }
            catch (Exception invalidHand){

            }
        });

        /*VBox clientL = new VBox(20, messages, input);
        clientL.getChildren().add(btn);
        clientL.getChildren().add(btn2);
        clientL.getChildren().add(btn3);
        //clientL.getChildren().add(rock);
        //clientL.getChildren().add(paper);
        //clientL.getChildren().add(scissors);
        //clientL.getChildren().add(lizard);
        //clientL.getChildren().add(spock);
        //clientL.setPrefSize(600, 600);
        */
        VBox root = new VBox(20, messages);
        root.setPrefSize(250, 250);

        HBox clientL = new HBox(20);
        clientL.getChildren().add(btn2);
        clientL.getChildren().add(btn3);
        //clientL.setPrefSize(600, 600);

        HBox options = new HBox(60);
        options.getChildren().add(rock);
        options.getChildren().add(paper);
        options.getChildren().add(scissors);
        options.getChildren().add(lizard);
        options.getChildren().add(spock);
        //options.setPrefSize(50, 50);

        BorderPane main = new BorderPane();
        //main.setPrefSize(600, 600);
        main.setTop(root);
        main.setCenter(clientL);
        main.setBottom(options);
        firstScene = new Scene(main);

        return firstScene;

    }
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        primaryStage.setScene(createContent());
        connect.setOnAction(event -> {
            newIpAddress = getIp.getText();
            int convertPort = parseInt(getPort.getText());
            newPort = convertPort;
            conn=createClient(newIpAddress,newPort);
            // try and catch exception
            try {
                conn.startConn();
            }
            catch (Exception connection){
                connection.printStackTrace();
            }

            // set the scene
            primaryStage.setScene(createClientGUI(primaryStage));

        });
        primaryStage.show();

    }
    //@Override
    /*public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        primaryStage.setScene(new Scene(createClientGUI()));
        primaryStage.show();

    }*/


    /*@Override
	public void init() throws Exception{
		//conn.startConn();
	}*/

    @Override
    public void stop() throws Exception{
        conn.closeConn();
    }

    private Server createServer() {
        return new Server(5555, data-> {
            Platform.runLater(()->{
                messages.appendText(data.toString() + "\n");
            });
        });
    }

    private Client createClient(String ip, int port) {
        return new Client(ip, port, data -> {
            Platform.runLater(()->{
                messages.appendText(data.toString() + "\n");
            });
        });
    }

}
