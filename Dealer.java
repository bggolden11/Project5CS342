package sample;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dealer {
    private List<String> cards = new ArrayList<>();

    public Dealer()
    {
        //TODO DECLARE ALL THE CARDS
    }
    public List<String> getCards()
    {
        return cards;
    }
    public void shuffle()
    {
        Collections.shuffle(cards);

    }
    public List<String> initalDeal()
    {
        shuffle();
        List<String> temp = new ArrayList<>();
        for(int i =0; i< 5; i++)
        {
            temp.add(cards.remove(cards.size()-1));
        }
        return temp;
    }


}
