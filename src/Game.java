import java.net.Socket;
import java.util.List;

public class Game extends Thread{
    protected int currentRound;
    protected Socket player1;  //Spelare 1
    protected Socket player2;  //Spelare 2
    protected Socket currentPlayer; //Håller koll på vilken spelare som ska välja kategori
    protected Socket tempPlayer;

    public Game(Socket player1, Socket player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    protected int p1points;  //Spelare 1 poäng
    protected int p2points;  //Spelare 2 poäng

    protected boolean[][] p1score; //Spelare 1 resultat array
    protected boolean[][] p2score; //Spelare 2 resultat array
    protected List<Questions> chosenQuestions;  //Frågor som skickas till den spelare som inte valt kategori
    protected boolean questionsReady;
    protected Protocol protocol = new Protocol();
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

    public void setCurrentPlayer(ServerPlayer input){
        currentPlayer = input;
    }

    public void setScore(ServerPlayer inputPlayer, boolean[] score, int round){
        if(inputPlayer == player1){
            p2score[round] = score;
        }
        else if(inputPlayer == player2){
            p1score[round] = score;
        }
    }

    @Override
    public void run(){

    }
}
