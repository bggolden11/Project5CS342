import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

//************************************************************************************
//creates object card
class Card {
    private String sentence;  //variables stored into each card

    Card(String s){  //constructor
        sentence = s;
    }

    //getters and setters
    String getSentence() {
        return sentence;
    }

}
//************************************************************************************
//object of deck
class Deck {

    //data members of Deck
    private int currentCard = 0; //keeps track of what card to deal
    private int totalNumCards = 0;
    private int totalNumQuestions = 0;
    private ArrayList<Card> Deck = new ArrayList<>(); //contains all the cards for deck
    private ArrayList<Card> Scenarios = new ArrayList<>(); //contains scenarios for cards against humanity


    //what the constructor calls to create the Deck
    void createDeck() {

        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/answers.txt"));
            String line = reader.readLine();
            while (line != null) {
                totalNumCards++;
                this.Deck.add(new Card(line));
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/questions.txt"));
            String line = reader.readLine();
            while (line != null) {
                totalNumQuestions++;
                this.Scenarios.add(new Card(line));
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Shuffle(); //shuffles deck


    }

    //construcor that creates the Deck
    Deck(){
        createDeck();
    }
    //sets current card and when shuffled assigns it to index 0
    void setCurrentCard(int currentCard) {
        this.currentCard = currentCard;
    }
    //used to print deck for testing card creation
     /*void PrintDeck(){
        for (Card card : Deck) {
            System.out.println(card.getVal() + " of " + card.getSentence());
        }
    }*/
    //shufffles the deck of cards to make sure cards dealt are random
    void Shuffle() {
        Collections.shuffle(Deck);
        Collections.shuffle(Deck);
        Collections.shuffle(Scenarios);
        Collections.shuffle(Scenarios);
    }
    //deals a single card and when you reach the end of deck it shuffles and starts at 0 index
    Card dealCard(){
        if(currentCard < totalNumCards){
            return Deck.get(currentCard++);
        } else {
            Shuffle();
            currentCard = 0;
            return Deck.get(currentCard);
        }
    }
    //deals an entire hand using the dealCard function
    ArrayList<Card> dealHand(){
        ArrayList<Card> temp = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            temp.add(dealCard()) ;
        }
        return temp;
    }

}

//************************************************************************************
//CAH implimentation of the game
class CAH  {
    //data members of pitch
    private Deck card = new Deck(); //creates and constructs the deck for the game
    private ArrayList<Player> Players  = new ArrayList<>();  //list of players
    ArrayList<Card> CenterCards = new ArrayList<>(); //list of cards in the center
    private int numplayers = 0; //keeps track of num players
    private int p1 = 0,p2 = 0,p3 = 0,p4 = 0;


    //adds cards to center that each person plays
    void  addToCenter(Card temp){
        CenterCards.add(temp);
    }
    //plays the game if a player has trump card and the rest do not they get a point
    void play() {
    }

    public void addVote(int num){
        switch (num) {
            case 1:
                p1++;
            case 2:
                p2++;
            case 3:
                p3++;
            case 4: p4++;
            default: break;
        }
    }

    //prints the cards in center//used for testing
    void printCenter(){
        for(int i =0; i < numplayers;i++){
            System.out.println( CenterCards.get(i).getSentence());
        }
    }
    //default constructor for pitch
    CAH(){

    }
    //sets the trump card from gui

    //gets the deck of cards
    Deck getCard() {
        return card;
    }
    //gets list of Players
    ArrayList<Player> getPlayers() {
        return Players;
    }
    //gets the number of players
    int getNumplayers() {
        return numplayers;
    }
    //sets the number of players from gui
    void setNumplayers(int numplayers) {
        this.numplayers = numplayers;
        card.setCurrentCard(0);
        //creates each player and assigns them a hand of cards
        for (int i = 0; i < numplayers; i++) {
            Player temp = new Player();
            Players.add(temp);
            Players.get(i).setHand(card.dealHand());
        }
    }

    ArrayList<Card> getCenter(){
        return CenterCards;
    }

}


//************************************************************************************

interface Dealer{
    ArrayList<Card> dealHand();
}

//************************************************************************************
//player object
class Player {
    //data members of the player object
    private ArrayList<Card> hand = new ArrayList<>();
    private int bet = 0, score = 0;

    //gets the hand from the player
    ArrayList<Card> getHand() {
        return hand;
    }
    //get the bet the player makes

    int getScore() {
        return score;
    }
    //sets player score
    void setScore(int score) {
        this.score = score;
    }
    //gives the player the hand of cards
    void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

}
//************************************************************************************
//creates AIPlayer
class AIPlayer extends Player{
    //AI data members
    private ArrayList<Card> hand = new ArrayList<>();
    private int bet = 0, score = 0;
    //gets the hand
    @Override
    public ArrayList<Card> getHand() {
        return hand;
    }
    //sets the AI players hand
    @Override
    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

}

//************************************************************************************
//GUI CLASS
public class Gui extends Application {
    //basic data fields and types
    private Button card1,card6,card5,card4,card3,card2,player2,player3,player4;
    private HashMap<String, Scene> sceneMap;
    private Stage myStage;
    private int cardsUsed = 0;

    //shows the Cards



    // updates the scores near board
    private static void scores(CAH game, Label score1, Label score2, Label score3) {
        score1.setText("Player 1 " + (game.getPlayers().get(0).getScore()));
        score2.setText("Player 2 " + (game.getPlayers().get(1).getScore()));
        score3.setText("Player 3 " + (game.getPlayers().get(2).getScore()));
    }

    //main that runs game
    public static void main(String[] args){
        launch(args);
    }

    public void start(Stage primaryStage){
        //gui data members
        sceneMap = new HashMap<String, Scene>();
        myStage = primaryStage;
        CAH game = new CAH(); //creates the game
        game.setNumplayers(4); //assigns players to one so that gui will run
        game.addToCenter(game.getCard().dealCard());
        game.addToCenter(game.getCard().dealCard());
        game.addToCenter(game.getCard().dealCard());
        game.addToCenter(game.getCard().dealCard());


//*****************************************************************************
        //first scene choosing player numbers and labels

        primaryStage.setTitle("Pitch");
        Label players = new Label("How many Players");
        Label score1 = new Label("Player 1 0");
        Label score2 = new Label("Player 2 0");
        Label score3 = new Label("Player 3 0");
        Label score4 = new Label("Player 4 0");
        Button player2 = new Button("2 Players");
        Button player3 = new Button("3 Players");
        Button player4 = new Button("4 Players");
        //sets all num boxes to invisible
        score1.setVisible(false);score2.setVisible(false);score3.setVisible(false);score4.setVisible(false);


        //if two players then creates 2 players and makes textbox 1 and 2 visbible
        player2.setOnAction(event -> {
            myStage.setScene(sceneMap.get("gamePlay"));
            score1.setVisible(true);score2.setVisible(true);score3.setVisible(false);score4.setVisible(false);
        });

        //if two players then creates 3 players and makes textbox 1,2,3 visbible
        player3.setOnAction(event ->{
            myStage.setScene(sceneMap.get("gamePlay"));
            score1.setVisible(true);score2.setVisible(true);score3.setVisible(true);score4.setVisible(false);
        });
        //if two players then creates 4 players and makes textbox 1,2,3,4 visbible

        player4.setOnAction(event ->{
            myStage.setScene(sceneMap.get("gamePlay"));
            score1.setVisible(true);score2.setVisible(true);score3.setVisible(true);score4.setVisible(true);
        });

//*****************************************************************************
        //Gameplay scene
        //bid buttons

        //***************************************************************

        //choose trump card
        Button voteP1 = new Button("Clubs");
        Button voteP2 = new Button("Spades");
        Button voteP3 = new Button("Hearts");
        Button voteP4 = new Button("Diamonds");
        voteP4.setVisible(false); voteP3.setVisible(false); voteP2.setVisible(false); voteP1.setVisible(false);
        voteP4.setMaxWidth(Double.MAX_VALUE); voteP3.setMaxWidth(Double.MAX_VALUE);
        voteP2.setMaxWidth(Double.MAX_VALUE); voteP1.setMaxWidth(Double.MAX_VALUE);






        //Sets the cards to a different size
        //creates the card buttons and sets all player card vals inside the buttons
        card1 = new Button( game.getPlayers().get(0).getHand().get(0).getSentence());
        card1.setMaxWidth(Double.MAX_VALUE);
        card2 = new Button( game.getPlayers().get(0).getHand().get(1).getSentence());
        card2.setMaxWidth(Double.MAX_VALUE);
        card3 = new Button( game.getPlayers().get(0).getHand().get(2).getSentence());
        card3.setMaxWidth(Double.MAX_VALUE);
        card4 = new Button( game.getPlayers().get(0).getHand().get(3).getSentence());
        card4.setMaxWidth(Double.MAX_VALUE);
        card5 = new Button( game.getPlayers().get(0).getHand().get(4).getSentence());
        card5.setMaxWidth(Double.MAX_VALUE);
        card6 = new Button( game.getPlayers().get(0).getHand().get(5).getSentence());
        //enables the cards

        //action event that handles what occurs when a card is picked
        EventHandler<ActionEvent> returnButton = event -> {
            Button b = (Button) event.getSource();
            //when card is clicked it adds player and ai player card to middle of deck and compares them to score points

                game.CenterCards.clear();
                if (b == card1) {
                    for (int i = 0; i < game.getNumplayers(); i++) {
                        game.addToCenter(game.getPlayers().get(i).getHand().get(0));
                        game.getPlayers().get(i).getHand().set(0,game.getCard().dealCard());
                        card1.setText( game.getPlayers().get(i).getHand().get(0).getSentence());
                        voteP1.setText( game.getCenter().get(0).getSentence());
                        voteP1.setVisible(true); voteP2.setVisible(true); voteP3.setVisible(true); voteP4.setVisible(true);
                    }
                }
                if (b == card2) {
                    for (int i = 0; i < game.getNumplayers(); i++) {
                        game.addToCenter(game.getPlayers().get(i).getHand().get(1));
                        game.getPlayers().get(i).getHand().set(1,game.getCard().dealCard());
                        card2.setText( game.getPlayers().get(i).getHand().get(1).getSentence());
                        voteP1.setVisible(true); voteP2.setVisible(true); voteP3.setVisible(true); voteP4.setVisible(true);

                    }
                }
                if (b == card3) {
                    for (int i = 0; i < game.getNumplayers(); i++) {
                        game.addToCenter(game.getPlayers().get(i).getHand().get(2));
                        game.getPlayers().get(i).getHand().set(2,game.getCard().dealCard());
                        card3.setText( game.getPlayers().get(i).getHand().get(2).getSentence());
                        voteP1.setVisible(true); voteP2.setVisible(true); voteP3.setVisible(true); voteP4.setVisible(true);

                    }
                }
                if (b == card4) {
                    for (int i = 0; i < game.getNumplayers(); i++) {
                        game.addToCenter(game.getPlayers().get(i).getHand().get(3));
                        game.getPlayers().get(i).getHand().set(3,game.getCard().dealCard());
                        card4.setText( game.getPlayers().get(i).getHand().get(3).getSentence());
                        voteP1.setVisible(true); voteP2.setVisible(true); voteP3.setVisible(true); voteP4.setVisible(true);

                    }
                }
                if (b == card5) {
                    for (int i = 0; i < game.getNumplayers(); i++) {
                        game.addToCenter(game.getPlayers().get(i).getHand().get(4));
                        game.getPlayers().get(i).getHand().set(4,game.getCard().dealCard());
                        card5.setText( game.getPlayers().get(i).getHand().get(4).getSentence());
                        voteP1.setVisible(true); voteP2.setVisible(true); voteP3.setVisible(true); voteP4.setVisible(true);

                    }
                }
                if (b == card6) {
                    for (int i = 0; i < game.getNumplayers(); i++) {
                        game.addToCenter(game.getPlayers().get(i).getHand().get(5));
                        game.getPlayers().get(i).getHand().set(5,game.getCard().dealCard());
                        card6.setText( game.getPlayers().get(i).getHand().get(5).getSentence());
                        voteP1.setVisible(true); voteP2.setVisible(true); voteP3.setVisible(true); voteP4.setVisible(true);

                    }
                }
            voteP4.setMaxWidth(Double.MAX_VALUE); voteP3.setMaxWidth(Double.MAX_VALUE);
            voteP2.setMaxWidth(Double.MAX_VALUE); voteP1.setMaxWidth(Double.MAX_VALUE);
            voteP1.setText( game.getCenter().get(0).getSentence());
            voteP2.setText( game.getCenter().get(1).getSentence());
            voteP3.setText( game.getCenter().get(2).getSentence());
            voteP4.setText( game.getCenter().get(3).getSentence());



                //b.setDisable(true); //disables buttons
                //b.setVisible(false); //makes button invisible
                game.play(); //allows players to score based on cards in center

        };




        //chooses clubs as trump cards
        voteP1.setOnAction(event -> {
            game.addVote(1);
            System.out.println("vote 1");
        });
        //chooses spades as trump card
        voteP2.setOnAction(event -> {
            game.addVote(2);
            System.out.println("vote 2");

        });
        //sets hearts as trump card
        voteP3.setOnAction(event -> {
            game.addVote(3);
            System.out.println("vote 3");

        });
        //sets diamonds as trump card
        voteP4.setOnAction(event -> {
            System.out.println("vote 4");

            game.addVote(4);
        });
        //sets cards as return buttons
        card1.setOnAction(returnButton); card2.setOnAction(returnButton); card3.setOnAction(returnButton);
        card4.setOnAction(returnButton); card5.setOnAction(returnButton); card6.setOnAction(returnButton);

        //*********************************************************************************
        //building gui
        BorderPane bottom = new BorderPane();
        HBox Cards = new HBox(10, card1,card2,card3,card4,card5,card6);
        HBox votes = new HBox(10, voteP1,voteP2,voteP3,voteP4);
        votes.setPadding(new Insets(180) );
        bottom.setCenter(votes);
        bottom.setBottom(Cards);
        Scene Gameplay = new Scene(bottom);
        sceneMap.put("gamePlay", Gameplay);

        BorderPane menu = new BorderPane();
        menu.setPadding(new Insets(150));
        HBox PaneCenter = new HBox(10, players, player2, player3, player4);
        HBox scores = new HBox(5,score1,score2,score3,score4);
        scores.setPadding(new Insets(30));

        menu.setCenter(PaneCenter);
        Scene MainMenu = new Scene(menu);
        sceneMap.put("How Many Players", MainMenu);
        primaryStage.setScene(sceneMap.get("How Many Players"));
        primaryStage.show();
    }
}
