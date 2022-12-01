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

    protected boolean[] p1score; //Spelare 1 resultat array
    protected boolean[] p2score; //Spelare 2 resultat array

    @Override
    public void run(){
        Protocol protocol = new Protocol();
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream("src/Properties.properties"));
        } catch (IOException e) {
            System.out.println("Kunde inte läsa properties");
        }
        maxRounds = Integer.parseInt(properties.getProperty("ROUNDS", "2"));
        numberOfQuestions = Integer.parseInt(properties.getProperty("QUESTIONS", "2"));
        p1score = new boolean[1];
        p2score = new boolean[1];
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
                    if(player1 != currentPlayer){
                        p1Out.writeObject(new Result());

                    }else if(player2 != currentPlayer){
                        p2Out.writeObject(new Result());

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
                    boolean[] temp1 = (boolean[]) p1In.readObject();
                    System.out.println("Spelare 1 svar inläst");
                    p1score = temp1;
                    System.out.println("Spelare 1 resultat sparat");
                    boolean[] temp2 = (boolean[]) p2In.readObject();
                    System.out.println("spelare 2 svar inläst");
                    p2score= temp2;
                    calculatePoints(temp1, player1);
                    calculatePoints(temp2, player2);
                    System.out.println("Spelare 2 resultat sparat");

                    System.out.println("Båda spelarna har skickat in svar");
                    protocol.getOutput(false);
                }
                if(protocol.state == protocol.UPDATE_RESULT){
                    System.out.println("Innan uppdatering av resultat");
                    p1Out.writeObject(p2score);
                    p2Out.writeObject(p1score);

                    System.out.println("Spelare 1 poäng: "+p1points);
                    System.out.println("Spelare 2 poäng: "+p2points);
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
        Collections.shuffle(kategori);

        return List.of(kategori.get(0), kategori.get(1), kategori.get(2), kategori.get(3));
    }

    private List<Questions> getQuestions(int qAmount, String category){
        List<Questions> questions = new ArrayList<>();
        for (Category c : allCategories) {
            if (c.getCategory().equals(category)) {
                for (int j = 0; j < qAmount; j++) {
                    questions.add(c.questions.get(j));
                }
            }
        }
        return questions;
    }
    public void calculatePoints(boolean[] input, Socket player){
        if(player == player1){
            for (boolean b : input) {
                if (b) {
                    p1points++;
                }
            }
        }
        if(player == player2){
            for (boolean b : input) {
                if (b) {
                    p2points++;
                }
            }
        }
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

    Questions musikQ1 = new Questions("Vilket band var Paul McCartney med i?", "Backstreet Boys","NSYNC", "The Beatles", "New Kids on the Block", "The Beatles");
    Questions musikQ2 = new Questions("Vem sjöng hit sången \"Thriller\"", "Michael Jackson", "Bob Marley", "Christina Aguilera", "Bruno Mars", "Michael Jackson");
    Questions musikQ3 = new Questions("Vem vann Eurovision år 2009?", "Sverige", "Finland", "Tyskland", "Norge", "Norge");
    Category musik = new Category("Musik", musikQ1, musikQ2, musikQ3);

    Questions djurQ1 = new Questions("Vilket är väldens största levande djur?", "Blåval", "Elefant", "Anakonda", "Giraff", "Blåval");
    Questions djurQ2 = new Questions("Vad är färgen på en giraffs tunga?", "Rosa","Röd","Lila","Vit","Lila");
    Questions djurQ3 = new Questions("Vilket djur sägs ha nio liv?","Hund","Katt","Räv","Varg","Katt");
    Category djur = new Category("Djur", djurQ1,djurQ2,djurQ3);

    Questions litteraturQ1 = new Questions("Vem skrev boken 1984?","Stephen King","George Orwell","Agatha Christie","Franz Kafka","George Orwell");
    Questions litteraturQ2 = new Questions("Vem skrev Harry Potter böckerna?","George Orwell","Chris Columbus","Stephen King","J.K Rowling","J.K Rowling");
    Questions litteraturQ3 = new Questions("Vem skrev Sagan om ringen böckerna?","J.R.R. Tolkien","J.K Rowling","Peter Jackson","Elijah Wood","J.R.R. Tolkien");
    Category litteratur = new Category("Litteratur", litteraturQ1, litteraturQ2,litteraturQ3);

    Questions språkQ1 = new Questions("Vilket år blev svenskan det officiella språket i Sverige?","2006","2009","1973","1998","2009");
    Questions språkQ2 = new Questions("Vilket språk har flest modersmålstalare?","Engelska","Spanska","Hindi","Mandarin","Mandarin");
    Questions språkQ3 = new Questions("Vilket språk talas i Brasilien?","Portugisiska","Spanska","Engelska","Latinamerikanska","Portugisiska");
    Category språk = new Category("Språk", språkQ1,språkQ2,språkQ3);

    List<String> kategori = new java.util.ArrayList<>(List.of("Historia", "Sport", "Film", "Geografi", "Mat", "Matematik", "Musik", "Djur", "Litteratur", "Språk"));
    List<Category> allCategories = List.of(historia, sport, film, geografi, mat, matematik, musik, djur, litteratur, språk);
}
