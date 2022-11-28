import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws Exception {

        ServerSocket listener = new ServerSocket(44444);

        try {
            while (true) {
                Socket player1 = listener.accept();
                System.out.println("Spelare 1 ansluten");
                Socket player2 = listener.accept();
                System.out.println("Spelare 2 ansluten");
                Game game = new Game(player1, player2);
                System.out.println("Game skapat");
                game.currentPlayer = player1;
                game.start();
                System.out.println("Slutet av main loop");

            }
        } finally {
            listener.close();
        }
    }
}