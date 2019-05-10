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
import sample.Client;
import sample.NetworkConnection;
import sample.Server;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class ServerGUI extends Application{


    private boolean isServer = true;

    private Server conn; //=createServer();
    private TextArea messages = new TextArea();
    Client conn1;// = createClient("127.0.0.1",5555);
    TextField IPAddress;
    TextField port;
    Button display = new Button("Input port");
    Button sumbit = new Button("Conenct");
    Button Exit = new Button("Exit");
    List<String> temp = new ArrayList<>();

    private Scene BeginScene() {

       // IPAddress = new TextField();
        port = new TextField();
        port.setPrefWidth(60);

        BorderPane borderPaneCenter = new BorderPane();
        HBox centerBox = new HBox(0);
        centerBox.getChildren().add(display);
        //centerBox.getChildren().add(IPAddress);
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
    private Parent createContent(Stage Primary) {
        messages.setPrefHeight(550);
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
            if (conn.checkPlayers()) {
                conn.Game();
            }

        });

        VBox root = new VBox(20, messages, input);
        root.setPrefSize(600, 600);
        root.getChildren().add(Exit);
        Exit.setOnAction(e->
        {
            Primary.close();
        });

        return root;



    }
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        primaryStage.setScene((BeginScene()));
        sumbit.setOnAction(e->
                {
                    int portNew;
                  //  IPAddressNew = IPAddress.getText();
                    portNew = parseInt(port.getText());
                    conn=createServer(portNew);
                    try {
                        conn.startConn();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                    primaryStage.setScene(new Scene(createContent(primaryStage)));
                }
        );
        primaryStage.show();

    }

    @Override
    public void init() throws Exception{
        //conn.startConn();
    }

    @Override
    public void stop() throws Exception{
        conn.closeConn();
    }
   /* public void Game() {
        String player1 = conn1.getThreads.get(0).move;
        String player2 = threads.get(1).move;
        if (threads.get(0).move == threads.get(1).move) {
            getCallback().accept("Tie");
        }
        if (player1 == "Scissor") {
            if (player2 == "Paper" || player2 == "Lizard") {
                getCallback().accept("Player 1 wins");
            }
            if (player2 == "Rock" || player2 == "Spock") ;
            {
                getCallback().accept("Player 2 wins");
            }


        }
    }

*/
    private Server createServer(int port) {
        return new Server(port, data-> {
            Platform.runLater(()->{

                messages.appendText(data.toString() + "\n");
            });
        });
    }


    private Client createClient() {
        return new Client("127.0.0.1", 5555, data -> {
            Platform.runLater(()->{
                messages.appendText(data.toString() + "\n");

            });
        });
    }

}
