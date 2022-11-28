import java.net.ServerSocket;

public class Server {

    public static void main(String[] args) throws Exception {

        ServerSocket listener = new ServerSocket(44444);

        try {
            while (true) {
                Game game = new Game();
                System.out.println("Game skapat");
                ServerPlayer player1 = new ServerPlayer(listener.accept(), "Spelare 1",game);
                System.out.println("Spelare 1 ansluten");
                ServerPlayer player2 = new ServerPlayer(listener.accept(),"Spelare 2",game);
                System.out.println("Spelare 2 ansluten");

                player1.setOpponent(player2);
                player2.setOpponent(player1);
                game.player1 = player1;
                game.player2 = player2;
                game.currentPlayer = player1;
                player1.start();
                player2.start();
                System.out.println("Slutet av main loop");

            }
        } finally {
            listener.close();
        }
    }
}