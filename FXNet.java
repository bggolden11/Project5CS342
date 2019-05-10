package projectFive;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import projectFive.Game.GameCommands;

public class FXNet extends Application {

	private static boolean isServer;

	private NetworkConnection conn;
	private TextArea messages = new TextArea();
	private int port = 0;
	private int id;
	private String ip = "127.0.0.1"; /* Default IP */
	
	private final String NEWLINE = "\n";
	private final String DBLNEWLINE = "\n\n";
	
	private Stage stage;
	private Game gameEngine;

	/* Server GUI */
	
	/* Main Menu */
	private Parent initServerMenuUI() {
		getStage().setTitle(isServer ? "Server Menu" : "Client Menu");
		
		TextField textPort = new TextField("Enter Port ####");
		Button btnStart = new Button("Start Server");
		Button btnExit = new Button("Exit Game");

		HBox root = new HBox(20, textPort, btnStart, btnExit);
		root.setPrefSize(600, 600);

		btnStart.setOnAction(event -> {
			if (!Game.isInteger(textPort.getText()))
				textPort.setText("Integers Only eg: 5555");
			else
				port = Integer.parseInt(textPort.getText());

			conn = createServer();
			try {
				conn.startConn();

				getStage().setScene(new Scene(initServerGameUI()));
			} catch (Exception e) {
				System.out.println("Error starting connection.");
			}

		});

		btnExit.setOnAction(event -> {
			try {
				conn.closeConn();
			} catch (Exception e) {}

			System.exit(0);
		});
		
		//init decks
		gameEngine = new Game();

		return root;
	}
	
	/* Run Games */
	private Parent initServerGameUI() {
		messages.setPrefHeight(550);

		getStage().setTitle(ip + " " + "Game Server " + port);

		
		Button btnAnnounce = new Button("View Player Responses");
		Button btnExit = new Button("Exit Game");
		Button btnScenario = new Button("View All Scenarios");
		Button btnAnswers = new Button("View All Answers");
		Button btnClear = new Button("Clear");
		
		Button btnSelectAnswer = new Button("Select Response #");
		
		TextField textAnswerSelect = new TextField();

		btnAnnounce.setOnAction(event -> {
			try {
				conn.send("");//winner text
			} catch (Exception e) {
				System.out.println("Error sending command data.");
			}
		});

		btnExit.setOnAction(event -> {
			try {
				conn.closeConn();
			} catch (Exception e) {}
			System.exit(0);

		});

		VBox vbox = new VBox(messages);
		vbox.setPrefSize(600, 500);
		
		btnScenario.setOnAction(event -> {
			messages.appendText(gameEngine.printDeck(GameCommands.DECK_SCENARIO));
		});
		
		btnAnswers.setOnAction(event -> {
			messages.appendText(gameEngine.printDeck(GameCommands.DECK_ANSWERS));
		});
		
		btnClear.setOnAction(event -> {
			messages.clear();
		});
		
		btnSelectAnswer.setOnAction(event -> {
			if(Game.isInteger(textAnswerSelect.getText()))
			{
				messages.appendText(sendCommand(GameCommands.CLIENT_AWARD, textAnswerSelect.getText()) + NEWLINE);
			}
			else
				messages.appendText("Enter Response # and press Select" + NEWLINE);
		});
		
		HBox hbox = new HBox(10, btnScenario, btnAnswers, btnClear, btnExit);
		HBox bottomhbox = new HBox(10, textAnswerSelect, btnSelectAnswer, btnAnnounce);
		
		BorderPane border = new BorderPane();
		border.setTop(hbox);
		border.setCenter(vbox);
		border.setBottom(bottomhbox);

		return border;

	}

	/* Client GUI */
	
	/* Main Menu */
	private Parent initClientMenuUI() {

		
		TextField textIP = new TextField("127.0.0.1");
		TextField textPort = new TextField("Enter Port ####");
		Button btnStart = new Button("Connect to Server");
		Button btnExit = new Button("Exit Game");

		HBox root = new HBox(20, textIP, textPort, btnStart, btnExit);
		root.setPrefSize(650, 100);

		btnStart.setOnAction(event -> {
			if (!Game.isInteger(textPort.getText()))
				textPort.setText("Integers Only eg: 5555");
			else if (textIP.getText().equals(""))
				textIP.setText("127.0.0.1");
			else {
				ip = textIP.getText();
				port = Integer.parseInt(textPort.getText());

				conn = createClient(this);
				try {
					conn.startConn();
					getStage().setScene(new Scene(initClientGameUI()));
				} catch (Exception e) {
					System.out.println("Error sending command data.");
				}
			}
		});

		return root;
	}

	/* Run Game */
	private Parent initClientGameUI() {

		//getStage().setTitle(ip + ":" + port + " Player");

		TextField textPlayerSelect = new TextField();
		Button btnSentenceSubmit = new Button("Select Sentence #");
		btnSentenceSubmit.setOnAction(event -> {
			if(Game.isInteger(textPlayerSelect.getText()))
			{
				messages.appendText(sendCommand(GameCommands.CLIENT_RESPONSE, textPlayerSelect.getText()) + NEWLINE);
			}
			else
				messages.appendText("Enter Sentence # and press Enter" + NEWLINE);
		});
		
		HBox hbox = new HBox(20, textPlayerSelect, btnSentenceSubmit);

		VBox vbox = new VBox(20, messages);

		Button btnWho = new Button("Who Am I?");
		Button btnLob = new Button("List of Players in Match");
		Button btnExit = new Button("Exit Game");
		
		btnWho.setOnAction(event -> {
			messages.appendText(sendCommand(GameCommands.CLIENT_WHOAMI) + NEWLINE);
		});
		
		btnLob.setOnAction(event -> {
			messages.appendText(sendCommand(GameCommands.CLIENT_LOBBY) + NEWLINE);
		});

		btnExit.setOnAction(event -> {
			try {
				conn.closeConn();
			} catch (Exception e) {
			}

			System.exit(0);

		});
		
		HBox hboxTwo = new HBox(20, btnLob, btnWho, btnExit);
		
		BorderPane border = new BorderPane();
		border.setTop(hboxTwo);
		border.setCenter(vbox);
		border.setBottom(hbox);
		

		Platform.runLater(() -> {
			sendCommand(GameCommands.CLIENT_WHOAMI); //ask server
		});

		return border;
	}

	/* Send Commands between Server/Client */
	String sendCommand(GameCommands command) {
		return sendCommand(command, "");
	}
	
	void setStage(Stage stage) {
		this.stage = stage;
	}
	
	Stage getStage() {
		return stage;
	}
	
	void assignClient() {
		Platform.runLater(() -> {
			getStage().setTitle(ip + ":" + port + " You are Player " + conn.getLocalID());
		});
	}
	
	String sendCommand(GameCommands command, String param) {
		try {
			conn.send(command.toString() + param);
		} catch (Exception e) {
			System.out.println("Error sending command data.");
		}
		return command.toString() + param;
	}

	/* Must be called to define whether instance will be Server */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			if (args[0].equals("-s"))
				isServer = true;
			else if (args[0].equals("-c"))
				isServer = false;
			else {
				System.out.println("Usage: -s for Server, -c for Client");
				System.exit(-1);
			}
		} catch (Exception e) {
			System.out.println("Fatal Error... are you trying to launch without arguments?");
			System.exit(-1);
		}

		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		setStage(primaryStage);
		
		getStage().setScene(
				isServer ? new Scene(initServerMenuUI()) : new Scene(initClientMenuUI()));
		
		getStage().show();

	}

	@Override
	public void init() throws Exception {

	}

	@Override
	public void stop() throws Exception {
		try {
			conn.closeConn();
		} catch (Exception e) {
			
		}
	}

	private Server createServer() {
		return new Server(port, data -> {
			Platform.runLater(() -> {
				messages.appendText(data.toString() + DBLNEWLINE);
			});
		});
	}

	private Client createClient(FXNet ui) {
		return new Client(ip, port, ui, data -> {
			Platform.runLater(() -> {
				messages.appendText(data.toString() + DBLNEWLINE);
			});
		});
	}

}
