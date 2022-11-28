import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

public class Game extends Thread{
    protected Socket player1;  //Spelare 1
    protected Socket player2;  //Spelare 2
    protected Socket currentPlayer; //Håller koll på vilken spelare som ska välja kategori
    protected Socket tempPlayer;
    protected ObjectOutputStream p1Out;
    protected ObjectInputStream p1In;
    protected ObjectOutputStream p2Out;
    protected ObjectInputStream p2In;
    public Game(Socket player1, Socket player2) throws IOException {
        this.player1 = player1;
        this.player2 = player2;
        p1Out = new ObjectOutputStream(player1.getOutputStream());
        p1Out.writeObject("Välkommen Spelare 1");
        p2Out = new ObjectOutputStream(player2.getOutputStream());
        p2Out.writeObject("Välkommen Spelare 2");
    }

    protected int currentRound;
    protected int maxRounds;
    protected int numberOfQuestions;
    protected int p1points;  //Spelare 1 poäng
    protected int p2points;  //Spelare 2 poäng

    protected boolean[][] p1score; //Spelare 1 resultat array
    protected boolean[][] p2score; //Spelare 2 resultat array
    protected List<Questions> chosenQuestions;  //Frågor som skickas till den spelare som inte valt kategori
    protected boolean questionsReady;
    protected boolean p1answered;
    protected boolean p2answered;

    public void setupScoreArrays(int questions, int rounds){  //Skapar BÅDA spelarnas resultat arrayer
        p1score = new boolean[questions][rounds];
        p2score = new boolean[questions][rounds];
    }
    public void setP1score(int round,boolean[] roundScore){  //Spara spelarens resultat per omgång
        p1score[round] = roundScore;
    }
    public void setP2score(int round,boolean[] roundScore){
        p2score[round] = roundScore;
    }
    public boolean[][] getP1score(){  //Hämtar spelarens poäng
        return p1score;
    }
    public boolean[][] getP2score(){
        return p2score;
    }
    public void setChosenQuestions(List<Questions> inputQuestions){
        chosenQuestions = inputQuestions;
    }
    public List<Questions> getChosenQuestions(){
        return chosenQuestions;
    }

    public void setCurrentPlayer(Socket input){
        currentPlayer = input;
    }

    public void setScore(boolean[] score, int round){
//        if(player1){
//            p2score[round] = score;
//        }
//        else if(inputPlayer == player2){
//            p1score[round] = score;
//        }
    }

    @Override
    public void run(){
        Protocol protocol = new Protocol();
        Properties properties = new Properties();
        properties = new Properties();

        try {
            properties.load(new FileInputStream("src/Properties.properties"));
        } catch (IOException e) {
            System.out.println("Kunde inte läsa properties");
        }
        maxRounds = Integer.parseInt(properties.getProperty("ROUNDS", "2"));
        numberOfQuestions = Integer.parseInt(properties.getProperty("QUESTIONS", "2"));
        setupScoreArrays(numberOfQuestions, maxRounds);
        currentRound = 0;
        try {
            p1In = new ObjectInputStream(player1.getInputStream());
            p2In = new ObjectInputStream(player2.getInputStream());
            while (true) {
                if (protocol.state == protocol.INITIAL) {
                    p1Out.writeObject(new Intro());
                    p2Out.writeObject(new Intro());
                    protocol.getOutput(false);
                }
                if(protocol.state == protocol.WAITING){
                    p1Out.writeObject(new Waiting());
                    p2Out.writeObject(new Waiting());
                    protocol.getOutput(false);
                }
                if(protocol.state == protocol.SENDING_CATEGORIES){
                    currentRound++;
                    List<String> categories = getCategory();
                    if(player1 == currentPlayer){
                        p1Out.writeObject(categories);
                        System.out.println("Kategorier skickade till Spelare 1");
                    }
                    else if(currentPlayer == player2){
                        p2Out.writeObject(categories);
                        System.out.println("Kategorier skickade till Spelare 2");
                    }
                    protocol.getOutput(false);
                }
                if(protocol.state == protocol.SENDING_QUESTIONS){
                    String chosenCategory = "";
                    if(currentPlayer == player1){
                        chosenCategory = (String) p1In.readObject();
                        System.out.println("Vald kategori inläst från Spelare 1");

                    }
                    if(currentPlayer == player2){
                        chosenCategory = (String) p2In.readObject();
                        System.out.println("Vald kategori inläst från Spelare 2");

                    }
                    List<Questions> questionsToSend = getQuestions(numberOfQuestions, chosenCategory);
                    p1Out.writeObject(questionsToSend);
                    p2Out.writeObject(questionsToSend);
                    tempPlayer = currentPlayer;
                    currentPlayer = null;
                    protocol.getOutput(false);
                }
                if(protocol.state == protocol.ANSWERING_QUESTIONS){
                    System.out.println("Väntar på svar från båda spelarna");
                    p1score[currentRound-1] = (boolean[]) p1In.readObject();
                    System.out.println("Spelare 1 resultat sparat");
                    p2score[currentRound-1] = (boolean[]) p2In.readObject();
                    System.out.println("Spelare 2 resultat sparat");

                    System.out.println("Båda spelarna har skickat in svar");
                    protocol.getOutput(false);
                }
                if(protocol.state == protocol.UPDATE_RESULT){
                    System.out.println("Innan uppdatering av resultat");
                    p1Out.writeObject(p2score[currentRound-1]);
                    p2Out.writeObject(p1score[currentRound-1]);
                    System.out.println("Efter uppdatering av resultat");

                    if(currentRound < maxRounds){
                        protocol.getOutput(true);
                        if(tempPlayer == player1){
                            currentPlayer = player2;
                        }else currentPlayer = player1;
                    }
                    else protocol.getOutput(false);
                }

                if(protocol.state == protocol.GAME_FINISHED){
                    p1Out.writeObject(new GameFinished());
                    p2Out.writeObject(new GameFinished());
                    System.out.println("Spelet avslutat");
                    protocol.getOutput(false);
                }


            }
        }catch (Exception ignore){}

    }

    private List<String> getCategory(){
        System.out.println("innan "+kategori);
        Collections.shuffle(kategori);
        System.out.println("efter " +kategori);
        //Fixa en shuffle metod som fungerar


        return List.of(kategori.get(0), kategori.get(1), kategori.get(2), kategori.get(3));
    }

    private List<Questions> getQuestions(int qAmount, String category){
//        System.out.println("inne i getquestions");
//        System.out.println(qAmount + " " + category);
//        System.out.println(allCategories.size());
        List<Questions> questions = new ArrayList<>();
        for (Category c : allCategories) {
//            System.out.println(c.getCategory());
            if (c.getCategory().equals(category)) {
                for (int j = 0; j < qAmount; j++) {
                    questions.add(c.questions.get(j));
                }
            }
        }
//        System.out.println(questions.size() + " " + questions.get(0).getQ());

        //Ta bort använd kategori
        return questions;
    }

    Questions historiaQ1 = new Questions("Mellan vilka år pågick först världskriget?", "1912 - 1917", "1914 - 1918", "1914 - 1919", "1913 - 1918", "1914 - 1918");
    Questions historiaQ2 = new Questions("Vad hette personen som höll det kända \"Jag har en dröm\" (\"I have a dream\") talet?", "Nelson Mandela", "Barack Obama", "Martin Luther King, jr", "Napoleon", "Martin Luther King, jr");
    Questions historiaQ3 = new Questions("Vem var Sveriges första statsminister?", "Carl Gustaf", "Carl Johan", "Gustav Vasa", "Louis de Geer", "Louis de Geer");
    Category historia = new Category("Historia", historiaQ1, historiaQ2, historiaQ3);

    Questions sportQ1 = new Questions("Var hölls sommar-OS år 2016?", "Rio de Janeiro", "London", "Peking", "Seoul", "Rio de Janeiro");
    Questions sportQ2 = new Questions("Vad heter högsta professionella ligan i England?", "Champions League", "La Liga", "Premier League", "Serie A", "Premier League");
    Questions sportQ3 = new Questions("Vem vann Uefa Champions League 2005/2006?", "AC Milan", "Manchester United", "FC Barcelona", "Chelsea", "FC Barcelona");
    Category sport = new Category ("Sport", sportQ1, sportQ2, sportQ3);

    Questions filmQ1 = new Questions("Vilken är den mest inkomstbringande filmen genom tiderna?", "Avatar", "Avengers: Endgame", "Titanic", "Frozen", "Avatar");
    Questions filmQ2 = new Questions("Vilket år kom filmen \"2012\" ut?", "2012", "2009", "2015", "2010", "2009");
    Questions filmQ3 = new Questions("För vilken film vann skådespelerskan \"Jennifer Lawrence\" Oscar 2013 (Bästa kvinnliga huvudroll)?", "The Hunger Games", "X-Men", "American Hustle", "Silver Linings Playbook", "Silver Linings Playbook");
    Category film = new Category("Film", filmQ1, filmQ2, filmQ3);

    Questions geografiQ1 = new Questions("Vad är Brasiliens huvudstad?", "Rio de Janeiro", "São Paulo", "Brasilia", "Buenos Aires", "Brasilia");
    Questions geografiQ2 = new Questions("Vilken är Europas längsta flod?", "Donau", "Nilen", "Kama", "Volga", "Volga");
    Questions geografiQ3 = new Questions("Vilket land har flest pyramider?", "Egypten", "Sudan", "Mexico", "Sydafrika", "Sudan");
    Category geografi = new Category("Geografi", geografiQ1, geografiQ2, geografiQ3);

    Questions matQ1 = new Questions("Vilket datum firas kanelbullens dag?", "4 september", "4 oktober", "3 september", "4 juni", "4 oktober");
    Questions matQ2 = new Questions("Hur många procent är fetthalten i lättmjölk?", "0,5%", "1,5%", "1%", "3%", "0,5%");
    Questions matQ3 = new Questions("Vilken är den näst mest populäraste läsken?", "Coca-Cola", "Mountain Dew", "Fanta", "Pepsi", "Pepsi");
    Category mat = new Category("Mat", matQ1, matQ2, matQ3);

    Questions matematikQ1 = new Questions("Vad väger mer, ett kilo fjäder eller ett kilo stål?", "Ett kilo stål", "Dem väger lika mycket", "Ett kilo fjäder", "Går ej att avgöra", "Dem väger lika mycket");
    Questions matematikQ2 = new Questions("Täljare / nämnare = ?", "Produkt", "Summa", "Kvot", "Differens", "Kvot");
    Questions matematikQ3 = new Questions("Vad är värdet på pi (fyra decimaler)", "3,1415", "3,1417", "3,1414", "3,1314", "3,1415");
    Category matematik = new Category("Matematik",matematikQ1, matematikQ2, matematikQ3);

    List<String> kategori = new java.util.ArrayList<>(List.of("Historia", "Sport", "Film", "Geografi", "Mat", "Matematik"));
//    String[] kategori = {"Historia", "Sport", "Film", "Geografi", "Mat", "Matematik"};
    List<Category> allCategories = List.of(historia, sport, film, geografi, mat, matematik);
}
