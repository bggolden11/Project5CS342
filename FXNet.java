package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.sum;


public class FXNet extends Application{
	String IPAddressNew;
	int portNew;
	Scene scene2;
	Button display = new Button("Input IP and port");
	Button sumbit = new Button("Conenct");

	Button exit = new Button("Exit");
	Button playAgain = new Button("Play Again");
	Client conn1;// = createClient("127.0.0.1",5555);
	TextField IPAddress;
	TextField port;

	Button card1 = new Button("jobs");
	Button card2 = new Button("men");
	Button card3 = new Button("Powerful");
	Button card4 = new Button("Prancing");
	Button card5 = new Button("Repression");
	Label Hand = new Label("Your hand");
	List<String> playerCards = new ArrayList<>();


	private boolean isServer = false;
	
//	private NetworkConnection  conn = isServer ? createServer() : createClient();
	private TextArea messages = new TextArea();








	
	private Scene createContent() {

		IPAddress = new TextField();
		port = new TextField();
		port.setPrefWidth(60);

		BorderPane borderPaneCenter = new BorderPane();
		HBox centerBox = new HBox(0);
		centerBox.getChildren().add(display);
		centerBox.getChildren().add(IPAddress);
		centerBox.getChildren().add(port);
		centerBox.getChildren().add(sumbit);
		TextArea messages2 = new TextArea();
		messages2.setPrefHeight(550);
		borderPaneCenter.setCenter(centerBox);



		/*IPAddress.setOnAction(event ->
				{
					IPAddressNew = IPAddress.getText();
				}
				);*/
/*		sumbit.setOnAction(e->
				{
					IPAddressNew = IPAddress.getText();
					portNew = parseInt(port.getText());
				}
				);
*/



		// TODO Auto-generated method stub
		//primaryStage.setScene(new Scene(createContent()));
		Scene scene1 = new Scene(borderPaneCenter);

		return scene1;









	}
		//messages.setPrefHeight(550);
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
			
		});

		VBox root = new VBox(20, messages, input);
		root.setPrefSize(600, 600);
		
		return root;
		
				

	}*/



	private Scene InGame(Stage PrimaryStage)
	{



		messages.setPrefHeight(550);
		VBox root = new VBox(20, messages);

		root.setPrefSize(600, 600);
		VBox temp = new VBox(30);

		HBox game = new HBox(30);




		game.getChildren().addAll(card1,card2,card3,card4,card5,exit,playAgain);
		temp.getChildren().add(Hand);
		temp.getChildren().add(game);
		BorderPane borderPane2 = new BorderPane();
		borderPane2.setCenter(temp);
		borderPane2.setTop(root);
		card1.setOnAction(e->
		{
			try{
				conn1.send(card1.getText());
			}
			catch(Exception e1)
			{

			}
		});
		card2.setOnAction(e->
		{
			try{
				conn1.send(card2.getText());
			}
			catch(Exception e1)
			{

			}
		});
		card3.setOnAction(e->
		{
			try{
				conn1.send(card3.getText());
			}
			catch(Exception e1)
			{

			}
		});
		card4.setOnAction(e->
		{
			try{
				conn1.send(card1.getText());
			}
			catch(Exception e1)
			{

			}
		});
		card5.setOnAction(e->
		{
			try{
				conn1.send(card5.getText());
			}
			catch(Exception e1)
			{

			}
		});

		scene2 = new Scene(borderPane2);
		//messages.appendText();
		/*
		lizard.setOnAction(e->
		{
			try {
				conn1.send("Lizard");
			}
			catch(Exception e2){

			}
		});
		paper.setOnAction(e->
				{
					try {
						conn1.send("Paper");
					}
					catch(Exception e3){}
				});
		scissor.setOnAction(e->
		{
			try {
				conn1.send("Scissor");
			}
			catch(Exception e3){}
		});
		rock.setOnAction(e->
		{
			try {
				conn1.send("Rock");
			}
			catch(Exception e4){}


		});
		spock.setOnAction(e->
		{
			try {
				conn1.send("Spock");
			}
			catch (Exception e4){}
		});
		playAgain.setOnAction(e->
		{
			try {
				conn1.send("Play");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		exit.setOnAction(e->
		{
			try {
				conn1.send("Exit");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			PrimaryStage.close();

		});*/
		return scene2;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(createContent());
		sumbit.setOnAction(e->
				{
					IPAddressNew = IPAddress.getText();
					portNew = parseInt(port.getText());
					 conn1=createClient(IPAddressNew,portNew);
					try {
						conn1.startConn();
					} catch (Exception e1) {
						e1.printStackTrace();
					}

					primaryStage.setScene(InGame(primaryStage));
				}
		);
		primaryStage.show();

	}
	
	@Override
	public void init() throws Exception{
		//conn1.startConn();
	}
	
	@Override
	public void stop() throws Exception{
		conn1.closeConn();
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
