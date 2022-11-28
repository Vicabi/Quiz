import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Game extends Thread{
    protected Socket player1;  //Spelare 1
    protected Socket player2;  //Spelare 2
    protected Socket currentPlayer; //Håller koll på vilken spelare som ska välja kategori
    protected Socket tempPlayer;
    public Game(Socket player1, Socket player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    protected int currentRound;
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
        if(inputPlayer == player1){
            p2score[round] = score;
        }
        else if(inputPlayer == player2){
            p1score[round] = score;
        }
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

        while(true){


        }

    }

    private List<String> getCategory(){
//        Collections.shuffle(kategori);
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

    List<String> kategori = List.of("Historia", "Sport", "Film", "Geografi", "Mat", "Matematik");
    List<Category> allCategories = List.of(historia, sport, film, geografi, mat, matematik);
}
