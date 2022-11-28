import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws Exception {

        ServerSocket listener = new ServerSocket(44444);

        try {
            while (true) {
                Socket player1 = listener.accept();
                Socket player2 = listener.accept();
                Game game = new Game(player1, player2);
                System.out.println("Game skapat");
                game.start();
                System.out.println("Slutet av main loop");

            }
        } finally {
            listener.close();
        }
    }
}