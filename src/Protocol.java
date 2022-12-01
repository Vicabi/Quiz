public class Protocol {
    final protected int INITIAL = 0;    //Spelare ansluten
    final protected int WAITING = 1;    //Väntar på motståndare
    final protected int SENDING_CATEGORIES = 2;   //Skickar kategorier till spelaren
    final protected int SENDING_QUESTIONS = 3;  //Skickar frågor från vald kategori
    final protected int ANSWERING_QUESTIONS = 4;  //Spelarna svarar på frågorna
    final protected int UPDATE_RESULT = 5;   //Uppdatera resultat för båda spelarna
    final protected int GAME_FINISHED = 6;  //Alla omgångar avslutade
    final protected int END = 7;

    protected int state = INITIAL;

    public void getOutput(boolean anotherRound) { //Spelaren klickar på "Historia"-knappen och skickar hit
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

            state = UPDATE_RESULT;
        } else if(state == UPDATE_RESULT){
            System.out.println("update state");

            if (anotherRound) {
                state = WAITING;
            } else state = GAME_FINISHED;
        }
        else if (state == GAME_FINISHED) {  //Alla omgångar avslutade
            System.out.println("finished state");
            state = END;
        }
    }
}
