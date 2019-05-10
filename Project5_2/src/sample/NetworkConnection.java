package sample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public abstract class NetworkConnection {
    public List<ClientThread> threads = new ArrayList<>();
    public boolean isServer;
    private ConnThread connthread = new ConnThread();
    private int numClients = 0;
    boolean clientOnethere;// = false;
    boolean clientTwothere;
    String clientOne = "";
    String clientTwo = "";
    String clientThree = "";
    String clientFour = "";
    int scorePlayer1 = 0;
    int scorePlayer2 = 0;

    public Consumer<Serializable> getCallback() {
        return callback;
    }

    public void setCallback(Consumer<Serializable> callback) {
        this.callback = callback;
    }

    //private ConnThread connThread2 = new ConnThread();
    private Consumer<Serializable> callback;

    public NetworkConnection(Consumer<Serializable> callback, boolean isServer) {
        this.callback = callback;
        this.isServer = isServer;

        connthread.setDaemon(true);


    }

    public void startConn() /*throws Exception*/ {
        connthread.start();

    }

    public String compare() {
        String player1 = clientOne;
        String player2 = clientTwo;
        String Player3 = clientThree;
        String player4 = clientFour;



        //String player

        String message = "hello ";
        if(clientOne != "")
        {
            message = clientOne;
            clientOne = "";
        }
        else if(clientFour != "")
        {
            message = clientFour;
            clientFour ="";
        }
        else if(clientTwo != "")
        {
            message = clientTwo;
            clientTwo = "";
        }
        else if(clientThree != "")
        {
            message = clientThree;
            clientThree = "";
        }
        else
        {
            message = "No data";

        }






        //callback.accept(clientOne);
        //callback.accept(clientTwo);
        /*if (clientOne.equals("Scissor")) {
            if (clientTwo.equals("Paper") || clientTwo.equals("Lizard")) {
                //     getCallback().accept("Player 1 wins");
                message = "Client one wins " + player1 + " beats " + player2;
                scorePlayer1++;
            }
            if (clientTwo.equals("Rock") || clientTwo.equals("Spock"))
            {
                //   getCallback().accept("Player 2 wins");
                message = "Client two wins " + player2 + " beats " + player1;
                scorePlayer2++;
            }


        }
        if (clientOne.equals("Paper")) {
            if (clientTwo.equals("Rock") || clientTwo.equals("Spock")) {
                // getCallback().accept("Player 1 wins");
                message = "Client one wins " + player1 + " beats " + player2;
                scorePlayer1++;
            } else {
                //getCallback().accept("Player 2 wins");
                message = "Client two wins " + player2 + " beats " + player1;
                scorePlayer2++;

            }

        }
        if (clientOne.equals("Rock")) {
            if (clientTwo.equals("Scissor") || clientTwo.equals("Lizard")) {
                // callback.accept("Player 1 wins");
                message = "Client one wins " + player1 + " beats " + player2;
                scorePlayer1++;
            } else {
                //callback.accept("Player 2 wins");
                message = "Client two wins " + player2 + " beats " + player1;
                scorePlayer2++;

            }
        }
        if (clientOne.equals("Spock")) {
            if (clientTwo.equals("Scissor") || clientTwo.equals("Rock")) {
                // callback.accept("Player 1 wins");
                message = "Client one wins " + player1 + " beats " + player2;
                scorePlayer1++;
            } else {
                //callback.accept("Player 2 wins");
                message = "Client two wins " + player2 + " beats " + player1;
                scorePlayer2++;
            }
        } else if (clientOne.equals("Lizard")) {
            if (clientTwo.equals("Paper") || clientTwo.equals("Spock")) {
                // callback.accept("Player 1 wins");
                message = "Client one wins " + player1 + " beats " + player2;
                scorePlayer1++;
            } else {
                // callback.accept("Player 2 wins");
                message = "Client two wins " + player2 + " beats " + player1;
                scorePlayer2++;
            }
        }
        if(clientOne.equals("Play")||clientTwo.equals("Play"))
        {
            message= "Playing Again Setting scores to 0";
            scorePlayer1 =0;
            scorePlayer2 =0;

        }
        else if(clientOne.equals("Exit"))
        {
            message = "Client one left";
        }
        else if(clientTwo.equals("Exit"))
        {
            message = "Client two left";
        }
        */
        return message;


     // return message;
    }

    public void send(Serializable data) throws Exception {
       // connthread.out.writeObject(data);
        if(isServer() && clientOnethere && clientTwothere)
        {
            String message = compare();
            threads.forEach(t -> {try{
                t.tout.writeObject(message);



            }
            catch(Exception e){e.printStackTrace();}
            });


        }
    else{
        connthread.out.writeObject(data);
            }
            }

    public void closeConn() throws Exception {
        connthread.socket.close();

    }
        abstract protected boolean isServer ();

        abstract protected String getIP ();

        abstract protected int getPort ();

    class ConnThread extends Thread {
        private Socket socket;

        public ObjectOutputStream out;

        public void run() {


            try {
                ServerSocket mysocket = new ServerSocket(getPort());
                //this.socket= mysocket.accept();
                //socket.setTcpNoDelay(true);

                //this.socket = server.accept();
                // Socket socket = server.accept();
                //Socket socket = isServer() ? server.accept() : new Socket(getIP(), getPort());

          /*      this.socket = socket;
                this.out = out;
                socket.setTcpNoDelay(true);
*/

                //ServerSocket server=new ServerSocket(getPort());
                // ServerSocket server = new ServerSocket(getPort());
                while (true) {
                    //ObjectInputStream in = new ObjectInputStream(server.accept().);
                    // ObjectInputStream in = new ObjectInputStream(mysocket.accept().getInputStream());
                    //  Serializable data = (Serializable)in.readObject();
                    //callback.accept(data);
                    ClientThread t1 = new ClientThread(mysocket.accept(), ++numClients);
                    //ClientThread t1 = new ClientThread(server.accept());
                    //callback.accept("Please work");

                    t1.start();
                    threads.add(t1);
                    //int numPlayers = threads.size();
                    callback.accept("There are " + numClients + " players connected");



                    //4

                }

            } catch (Exception e1) {
                callback.accept("connection Closed");
            }
        }

        public boolean checkPlayers() {
            if (threads.size() == 2 && threads.get(0).move != "NAN" && threads.get(0).move != "NAN") {
                callback.accept("RUnning");
                return true;

            } else {
                callback.accept("Not running");
                return false;
            }
        }

        public void Game() {
            String player1 = threads.get(0).move;
            String player2 = threads.get(1).move;
            String player4 = threads.get(2).move;
            String Player5 = threads.get(3).move;
            if (threads.get(0).move == threads.get(1).move) {
                getCallback().accept("Tie");
            }
            if (player1 == "Scissor") {
                if (player2 == "Paper" || player2 == "Lizard") {
                  //  getCallback().accept("Player 1 wins");
                }
                if (player2 == "Rock" || player2 == "Spock") ;
                {
                    //getCallback().accept("Player 2 wins");
                }


            }


        }
    }
    public class ClientThread extends Thread {
        Socket connection;
        ObjectOutputStream out;
        ObjectInputStream tin;
        ObjectOutputStream tout;
        String move = "NAN";
        private int id;

        //ObjectOutputStream out;
        //ObjectInputStream in;

       // ObjectInputStream in;
      //  ObjectInputStream in;
        //private Consumer<Serializable> callback;
        ClientThread(Socket S, int id) {



            //callback.accept("MAybe this whhfdf");
            this.connection = S;
            this.id = id;


           // this.in = in;
           // in = new ObjectInputStream(connection.getInputStream());

            //this.callback= callback;
        }
        public void run(){
           // callback.accept("MAybe this whhfdf");
            try {

                ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
                out = new ObjectOutputStream(connection.getOutputStream());
                connection.setTcpNoDelay(true);
                this.tout = out;
                this.tin = in;
                tout.writeObject("Welcome player: " + id);
                //this.in = in;
                //this.out= out;
                while (true) {

                    Serializable data = (Serializable)in.readObject();
                    if(id == 1)
                    {
                        clientOne = data.toString();
                        clientOnethere = true;
                    }
                    else if(id == 2)
                    {
                        clientTwo = data.toString();
                        clientTwothere =true;
                    }
                    else if(id == 3)
                    {
                        clientThree = data.toString();

                    }
                    else if(id ==4)
                    {
                        clientFour = data.toString();
                    }
                    callback.accept("Player " + id + " plays " + data);
                    //callback.accept(data);
                    //move = data.toString().intern();






                }

            }
            catch(Exception e) {
                callback.accept("Error");
            }
        }
    }

    }


