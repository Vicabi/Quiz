import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Protocol {
    final protected int INITIAL = 0;    //Spelare ansluten
    final protected int WAITING = 1;    //Väntar på motståndare
    final protected int SENDING_CATEGORIES = 2;   //Skickar kategorier till spelaren
    final protected int SENDING_QUESTIONS = 3;  //Skickar frågor från vald kategori
    final protected int ANSWERING_QUESTIONS = 4;  //Spelarna svarar på frågorna
    final protected int GAME_FINISHED = 5;   //Alla omgångar avslutade

    protected int state = INITIAL;

    public Object getOutput(boolean anotherRound) { //Spelaren klickar på "Historia"-knappen och skickar hit
        Object output = null;

        if (state == INITIAL) {         //Spelare ansluten
            System.out.println("initial state");

            state = WAITING;
        } else if (state == WAITING) {  //Väntar på motståndare
            System.out.println("waiting state");

            state = SENDING_CATEGORIES;
        } else if (state == SENDING_CATEGORIES) {  //Skickar kategorier till spelaren
            System.out.println("sending state");

            state = SENDING_QUESTIONS;
        } else if (state == SENDING_QUESTIONS) {  //Skickar frågor från vald kategori
            System.out.println("choosing state");

            state = ANSWERING_QUESTIONS;
        } else if (state == ANSWERING_QUESTIONS) {  //Spelarna svarar på frågorna
            System.out.println("answering state");

            if (anotherRound) {
                state = WAITING;
            } else state = GAME_FINISHED;

        } else if (state == GAME_FINISHED) {  //Alla omgångar avslutade
            System.out.println("finished state");
        }

        return output;
    }
}
