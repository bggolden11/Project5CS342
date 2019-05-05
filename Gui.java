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
    private String Suit;  //variables stored into each card

    Card(String s){  //constructor
        Suit = s;
    }

    //getters and setters
    String getSentence() {
        return Suit;
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
                System.out.println(line);
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
                System.out.println(line);
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
class CAH implements DealerType {
    //data members of pitch
    private Deck card = new Deck(); //creates and constructs the deck for the game
    private ArrayList<Player> Players  = new ArrayList<>();  //list of players
    ArrayList<Card> CenterCards = new ArrayList<>(); //list of cards in the center
    private int numplayers = 0; //keeps track of num players
    private String trumpCard; //keeps track of trump card

    //adds cards to center that each person plays
    void  addToCenter(Card temp){
        CenterCards.add(temp);
    }
    //plays the game if a player has trump card and the rest do not they get a point
    void play() {

        if(numplayers ==2){
            if(CenterCards.get(0).getSentence().equals(trumpCard) && !CenterCards.get(1).getSentence().equals(trumpCard)){
                int x = (Players.get(0).getScore()+1);
                Players.get(0).setScore(x);
            }
            if(!CenterCards.get(0).getSentence().equals(trumpCard) && CenterCards.get(1).getSentence().equals(trumpCard)){
                int x = (Players.get(1).getScore()+1);
                Players.get(1).setScore(x);
            }
        } else if(numplayers ==3){
            if(CenterCards.get(0).getSentence().equals(trumpCard) && !CenterCards.get(1).getSentence().equals(trumpCard) && !CenterCards.get(2).getSentence().equals(trumpCard)){
                int x = (Players.get(0).getScore()+1);
                Players.get(0).setScore(x);
            }
            if(!CenterCards.get(0).getSentence().equals(trumpCard) && CenterCards.get(1).getSentence().equals(trumpCard) && !CenterCards.get(2).getSentence().equals(trumpCard)){
                int x = (Players.get(1).getScore()+1);
                Players.get(1).setScore(x);
            }
            if(!CenterCards.get(0).getSentence().equals(trumpCard) && !CenterCards.get(1).getSentence().equals(trumpCard) && CenterCards.get(2).getSentence().equals(trumpCard)){
                int x = (Players.get(2).getScore()+1);
                Players.get(2).setScore(x);
            }
        }else if(numplayers ==4) {
            if (CenterCards.get(0).getSentence().equals(trumpCard) && !CenterCards.get(1).getSentence().equals(trumpCard)
                    && !CenterCards.get(2).getSentence().equals(trumpCard) && !CenterCards.get(3).getSentence().equals(trumpCard)) {
                int x = (Players.get(0).getScore() + 1);
                Players.get(0).setScore(x);
            }
            if (!CenterCards.get(0).getSentence().equals(trumpCard) && CenterCards.get(1).getSentence().equals(trumpCard)
                    && !CenterCards.get(2).getSentence().equals(trumpCard) && !CenterCards.get(3).getSentence().equals(trumpCard)) {
                int x = (Players.get(1).getScore() + 1);
                Players.get(1).setScore(x);
            }
            if (!CenterCards.get(0).getSentence().equals(trumpCard) && !CenterCards.get(1).getSentence().equals(trumpCard)
                    && CenterCards.get(2).getSentence().equals(trumpCard) && !CenterCards.get(3).getSentence().equals(trumpCard)) {
                int x = (Players.get(2).getScore() + 1);
                Players.get(2).setScore(x);
            }
            if (!CenterCards.get(0).getSentence().equals(trumpCard) && !CenterCards.get(1).getSentence().equals(trumpCard)
                    && !CenterCards.get(2).getSentence().equals(trumpCard) && CenterCards.get(3).getSentence().equals(trumpCard)) {
                int x = (Players.get(3).getScore() + 1);
                Players.get(3).setScore(x);
            }
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
    void setTrumpCard(String trumpCard) {
        this.trumpCard = trumpCard;
    }
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
    //creates pitch dealer
    @Override
    public Dealer createDealer() {
        return new pitchDealer();
    }
    //Gets the trump card to help players get scores
    public String getTrumpCard() {
        return trumpCard;
    }
}
//************************************************************************************
//Pitch dealer
class pitchDealer implements Dealer {
    //not used but it would be used to deal hands and shuffle
    @Override
    public ArrayList<Card> dealHand() {

        ArrayList<Card> hand = new ArrayList<>();
        return null;
    }
}
//************************************************************************************

interface DealerType{
    Dealer createDealer();
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
    int getBet() {
        return bet;
    }
    //sets player bet
    void setBet(int bet) {
        this.bet = bet;
    }
    //gets player score
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
    private static void showCards(Button card1, Button card2, Button card3, Button card4, Button card5, Button card6) {
        card1.setVisible(true);card2.setVisible(true);card3.setVisible(true);
        card4.setVisible(true);card5.setVisible(true);card6.setVisible(true);
    }
    //Allows GUI cards to be enabled
    private static void enableCards(Button card1, Button card2, Button card3, Button card4, Button card5, Button card6) {
        card1.setDisable(true);card2.setDisable(true);card3.setDisable(true);
        card4.setDisable(true);card5.setDisable(true);card6.setDisable(true);
    }
    //Set the Trump choice buttons visible
    private void setAsVisible(Button clubs, Button spades, Button hearts, Button diamonds) {
        SuitNotVisible(clubs, spades, hearts, diamonds);
        showCards(card1, card2, card3, card4, card5, card6);
    }
    //Makes trump card gui not visible and enables the cards
    private void SuitNotVisible(Button clubs, Button spades, Button hearts, Button diamonds) {
        clubs.setVisible(false);spades.setVisible(false);hearts.setVisible(false);diamonds.setVisible(false);
        card1.setDisable(false);card2.setDisable(false);card3.setDisable(false);card4.setDisable(false);
        card5.setDisable(false);card6.setDisable(false);
    }

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
        game.setNumplayers(1); //assigns players to one so that gui will run

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
            game.setNumplayers(2);
            score1.setVisible(true);score2.setVisible(true);score3.setVisible(false);score4.setVisible(false);
        });

        //if two players then creates 3 players and makes textbox 1,2,3 visbible
        player3.setOnAction(event ->{
            myStage.setScene(sceneMap.get("gamePlay"));
            game.setNumplayers(3);
            score1.setVisible(true);score2.setVisible(true);score3.setVisible(true);score4.setVisible(false);
        });
        //if two players then creates 4 players and makes textbox 1,2,3,4 visbible

        player4.setOnAction(event ->{
            myStage.setScene(sceneMap.get("gamePlay"));
            game.setNumplayers(4);
            score1.setVisible(true);score2.setVisible(true);score3.setVisible(true);score4.setVisible(true);
        });

//*****************************************************************************
        //Gameplay scene
        //bid buttons

        //***************************************************************

        //choose trump card
        Button clubs = new Button("Clubs");
        clubs.setPrefSize(60,60);
        Button spades = new Button("Spades");
        spades.setPrefSize(60,60);
        Button hearts = new Button("Hearts");
        hearts.setPrefSize(60,60);
        Button diamonds = new Button("Diamonds");
        diamonds.setPrefSize(60,60);



        //chooses clubs as trump cards
        clubs.setOnAction(event -> {
            game.setTrumpCard("Clubs");
            setAsVisible(clubs, spades, hearts, diamonds);
        });
        //chooses spades as trump card
        spades.setOnAction(event -> {
            game.setTrumpCard("Spades");
            setAsVisible(clubs, spades, hearts, diamonds);
        });
        //sets hearts as trump card
        hearts.setOnAction(event -> {
            game.setTrumpCard("Hearts");
            setAsVisible(clubs, spades, hearts, diamonds);
        });
        //sets diamonds as trump card
        diamonds.setOnAction(event -> {
            game.setTrumpCard("Diamonds");
            SuitNotVisible(clubs, spades, hearts, diamonds);
        });

        //Sets the cards to a different size
        //creates the card buttons and sets all player card vals inside the buttons
        card1 = new Button( game.getPlayers().get(0).getHand().get(0).getSentence());
        card2 = new Button( game.getPlayers().get(0).getHand().get(1).getSentence());
        card3 = new Button( game.getPlayers().get(0).getHand().get(2).getSentence());
        card4 = new Button( game.getPlayers().get(0).getHand().get(3).getSentence());
        card5 = new Button( game.getPlayers().get(0).getHand().get(4).getSentence());
        card6 = new Button( game.getPlayers().get(0).getHand().get(5).getSentence());
        //enables the cards
        enableCards(card1, card2, card3, card4, card5, card6);

        //action event that handles what occurs when a card is picked
        EventHandler<ActionEvent> returnButton = event -> {
            Button b = (Button) event.getSource();
            //when card is clicked it adds player and ai player card to middle of deck and compares them to score points
            if(cardsUsed < 6) {
                game.CenterCards.clear();
                if (b == card1) {
                    for (int i = 0; i < game.getNumplayers(); i++) {
                        game.addToCenter(game.getPlayers().get(i).getHand().get(0));
                    }
                }
                if (b == card2) {
                    for (int i = 0; i < game.getNumplayers(); i++) {
                        game.addToCenter(game.getPlayers().get(i).getHand().get(1));
                    }
                }
                if (b == card3) {
                    for (int i = 0; i < game.getNumplayers(); i++) {
                        game.addToCenter(game.getPlayers().get(i).getHand().get(2));
                    }
                }
                if (b == card4) {
                    for (int i = 0; i < game.getNumplayers(); i++) {
                        game.addToCenter(game.getPlayers().get(i).getHand().get(3));
                    }
                }
                if (b == card5) {
                    for (int i = 0; i < game.getNumplayers(); i++) {
                        game.addToCenter(game.getPlayers().get(i).getHand().get(4));
                    }
                }
                if (b == card6) {
                    for (int i = 0; i < game.getNumplayers(); i++) {
                        game.addToCenter(game.getPlayers().get(i).getHand().get(5));
                    }
                    //game.getPlayers().get(1).
                }


                b.setDisable(true); //disables buttons
                b.setVisible(false); //makes button invisible
                cardsUsed++; //incriments cards used
                game.play(); //allows players to score based on cards in center

                //updates the textboxs based on numer of players and player scores
                if(game.getNumplayers() == 2) {
                    score1.setText("Player 1 " +Integer.toString(game.getPlayers().get(0).getScore()));
                    score2.setText("Player 2 " + Integer.toString(game.getPlayers().get(1).getScore()));
                } else if(game.getNumplayers() == 3){
                    scores(game, score1, score2, score3);
                } else if(game.getNumplayers() ==4){
                    scores(game, score1, score2, score3);
                    score4.setText("Player 4 " + (game.getPlayers().get(3).getScore()));
                }
            } if (cardsUsed == 6) { //if all cards used them reinables cards and makes cards visible

                showCards(card1, card2, card3, card4, card5, card6);
                enableCards(card1, card2, card3, card4, card5, card6);
                //gives each player a new hand of cards
                for (int k = 0; k < game.getNumplayers();k++) {
                    game.getPlayers().get(k).setHand(game.getCard().dealHand()) ;
                }
                //re-assigns values to match player cards in buttons
                card1.setText( game.getPlayers().get(0).getHand().get(0).getSentence());
                card2.setText( game.getPlayers().get(0).getHand().get(1).getSentence());
                card3.setText( game.getPlayers().get(0).getHand().get(2).getSentence());
                card4.setText( game.getPlayers().get(0).getHand().get(3).getSentence());
                card5.setText( game.getPlayers().get(0).getHand().get(4).getSentence());
                card6.setText( game.getPlayers().get(0).getHand().get(5).getSentence());

                cardsUsed = 0; //sets cards used to zero
            }
        };
        //sets cards as return buttons
        card1.setOnAction(returnButton); card2.setOnAction(returnButton); card3.setOnAction(returnButton);
        card4.setOnAction(returnButton); card5.setOnAction(returnButton); card6.setOnAction(returnButton);

        //*********************************************************************************
        //building gui
        BorderPane bottom = new BorderPane();
        HBox newScene = new HBox(10, card1,card2,card3,card4,card5,card6);
        HBox Suits = new HBox(10, hearts,clubs,diamonds,spades);
        Suits.setPadding(new Insets(180) );
        bottom.setCenter(Suits);
        bottom.setBottom(newScene);
        Scene Gameplay = new Scene(bottom, 750, 600);
        sceneMap.put("gamePlay", Gameplay);

        BorderPane menu = new BorderPane();
        menu.setPadding(new Insets(150));
        VBox PaneCenter = new VBox(10, players, player2, player3, player4);
        VBox scores = new VBox(5,score1,score2,score3,score4);
        scores.setPadding(new Insets(30));

        bottom.setRight(scores);
        menu.setCenter(PaneCenter);
        Scene MainMenu = new Scene(menu, 400, 400);
        sceneMap.put("How Many Players", MainMenu);
        primaryStage.setScene(sceneMap.get("How Many Players"));
        primaryStage.show();
    }
}
