import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class GUI extends JFrame {

    protected static int PORT = 44444;
    protected Socket socket;
    protected ObjectInputStream objIn;
    protected ObjectOutputStream objOut;

    private JPanel mainPanel;
    private JPanel homeScreen;
    private JButton newGameButton;
    private JPanel loadingScreen;
    private JLabel jLabel1;
    private JPanel categoryScreen;
    private JButton category1Button;
    private JButton category2Button;
    private JButton category3Button;
    private JButton category4Button;
    private JLabel categoryJLabel;
    private JPanel gameScreen;
    private JLabel questionLabel;
    private JButton answerOptions1Button;
    private JButton answerOptions2Button;
    private JButton answerOptions3Button;
    private JButton answerOptions4Button;
    private JPanel resultScreen;
    private JButton newGameButton2;
    private JButton endGameButton;
    private JLabel player1JLabel;
    private JLabel resultJLabel;
    private JLabel player2JLabel;
    private JPanel PlayerResult;
    private JPanel OpponentResult;
    private JLabel playerPoints;
    private JLabel opponentPoints;
    private JLabel currentRound;

    protected List<String> listString;
    protected List<Questions> listQuestions;
    protected boolean[] answers;
    protected boolean[] opponentAnswers;
    protected int currentQuestion;
    protected int questionAmount;
    protected int rounds;
    protected boolean answered;
    protected int playerPointsCounter;
    protected int opponentPointsCounter;

    public GUI(String serverAddress) throws IOException {

        socket = new Socket(serverAddress, PORT);
        objIn = new ObjectInputStream(socket.getInputStream());
        objOut = new ObjectOutputStream(socket.getOutputStream());

        jLabel1.setFont(new Font("Poppins", Font.BOLD, 17));
        jLabel1.setForeground(Color.white);

        resultJLabel.setFont(new Font("Poppins", Font.BOLD, 17));
        resultJLabel.setForeground(Color.white);

        categoryJLabel.setFont(new Font("Poppins", Font.BOLD, 18));
        categoryJLabel.setForeground(Color.white);

        questionLabel.setFont(new Font("Poppins", Font.BOLD, 14));
        questionLabel.setForeground(Color.white);

        category1Button.setBackground(new Color(51,153,255));
        category1Button.setBorder(new SoftBevelBorder(BevelBorder.RAISED,new Color(0,191,255),new Color(70,130,180)));
        category1Button.setFont(new Font("Poppins", Font.BOLD, 17));
        category1Button.setForeground(Color.white);

        category2Button.setBackground(new Color(51,153,255));
        category2Button.setBorder(new SoftBevelBorder(BevelBorder.RAISED,new Color(0,191,255),new Color(70,130,180)));
        category2Button.setFont(new Font("Poppins", Font.BOLD, 17));
        category2Button.setForeground(Color.white);

        category3Button.setBackground(new Color(51,153,255));
        category3Button.setBorder(new SoftBevelBorder(BevelBorder.RAISED,new Color(0,191,255),new Color(70,130,180)));
        category3Button.setFont(new Font("Poppins", Font.BOLD, 17));
        category3Button.setForeground(Color.white);

        category4Button.setBackground(new Color(51,153,255));
        category4Button.setBorder(new SoftBevelBorder(BevelBorder.RAISED,new Color(0,191,255),new Color(70,130,180)));
        category4Button.setFont(new Font("Poppins", Font.BOLD, 17));
        category4Button.setForeground(Color.white);

        answerOptions1Button.setBorder(new SoftBevelBorder(BevelBorder.RAISED,new Color(0,191,255),new Color(95,158,160)));
        answerOptions1Button.setFont(new Font("Poppins", Font.BOLD, 12));

        answerOptions2Button.setBorder(new SoftBevelBorder(BevelBorder.RAISED,new Color(0,191,255),new Color(95,158,160)));
        answerOptions2Button.setFont(new Font("Poppins", Font.BOLD, 12));

        answerOptions3Button.setBorder(new SoftBevelBorder(BevelBorder.RAISED,new Color(0,191,255),new Color(95,158,160)));
        answerOptions3Button.setFont(new Font("Poppins", Font.BOLD, 12));

        answerOptions4Button.setBorder(new SoftBevelBorder(BevelBorder.RAISED,new Color(0,191,255),new Color(95,158,160)));
        answerOptions4Button.setFont(new Font("Poppins", Font.BOLD, 12));

        endGameButton.setBackground(new Color	(127,255,0));
        endGameButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED,new Color(152,251,152),new Color(50,205,50)));

        newGameButton.setBackground(new Color	(127,255,0));
        newGameButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED,new Color(152,251,152),new Color(50,205,50)));

        newGameButton2.setBackground(new Color	(127,255,0));
        newGameButton2.setBorder(new SoftBevelBorder(BevelBorder.RAISED,new Color(152,251,152),new Color(50,205,50)));




        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.setSize(500, 600);
        this.setLocationRelativeTo(null);
        setVisible(true);
        PlayerResult.setLayout(new GridLayout(10, 1));
        OpponentResult.setLayout(new GridLayout(10, 1));
        mainPanel.add(homeScreen);
        mainPanel.add(loadingScreen);
        mainPanel.add(categoryScreen);
        mainPanel.add(gameScreen);
        mainPanel.add(resultScreen);

        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                homeScreen.setVisible(false);
                loadingScreen.setVisible(true);

            }
        });
        category1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    objOut.writeObject(listString.get(0));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        category2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    objOut.writeObject(listString.get(1));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        category3Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    objOut.writeObject(listString.get(2));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        category4Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    objOut.writeObject(listString.get(3));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });

        answerOptions1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!answered) {
                    if (listQuestions.get(0).getA1().equals(listQuestions.get(0).getCorrectAnswer())) {
                        answers[currentQuestion] = true;
                        answerOptions1Button.setBackground(Color.GREEN);
                    } else {
                        answers[currentQuestion] = false;
                        answerOptions1Button.setBackground(Color.RED);
                    }
                    answered = true;
                }
            }
        });

        answerOptions2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!answered) {
                    if (listQuestions.get(0).getA2().equals(listQuestions.get(0).getCorrectAnswer())) {
                        answers[currentQuestion] = true;
                        answerOptions2Button.setBackground(Color.GREEN);
                    } else {
                        answers[currentQuestion] = false;
                        answerOptions2Button.setBackground(Color.RED);
                    }
                    answered = true;
                }
            }
        });

        answerOptions3Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!answered) {
                    if (listQuestions.get(0).getA3().equals(listQuestions.get(0).getCorrectAnswer())) {
                        answers[currentQuestion] = true;
                        answerOptions3Button.setBackground(Color.GREEN);
                    } else {
                        answers[currentQuestion] = false;
                        answerOptions3Button.setBackground(Color.RED);
                    }
                    answered = true;
                }
            }
        });

        answerOptions4Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!answered) {
                    if (listQuestions.get(0).getA4().equals(listQuestions.get(0).getCorrectAnswer())) {
                        answers[currentQuestion] = true;
                        answerOptions4Button.setBackground(Color.GREEN);
                    } else {
                        answers[currentQuestion] = false;
                        answerOptions4Button.setBackground(Color.RED);
                    }
                    answered = true;
                }
            }
        });
        endGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        newGameButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    play();
                    newGameButton2.setVisible(false);
                } catch (Exception ex) {
                    System.out.println("Kunde inte starta nytt spel");
                }
            }
        });
    }

    public void play() throws Exception {
        newGameButton2.setVisible(false);
        Object fromServer;
        String player = "TEMP_PLAYER1";
        String opponent = "TEMP_PLAYER2";
        String turn = player;
        rounds = 1;
        playerPointsCounter = 0;
        opponentPointsCounter = 0;
        try {
            System.out.println("GUI play");
            fromServer = objIn.readObject();
            if (fromServer instanceof String) {
                String s = (String) fromServer;
                if (s.startsWith("Välkommen")) {
                    player = s.substring(10);
                    if (player.equals("Spelare 1")) {
                        opponent = "Spelare 2";
                    } else opponent = "Spelare 1";
                }
                System.out.println("Spelare - " + player);
                setTitle(player);
                player1JLabel.setFont(new Font("Poppins", Font.BOLD, 17));
                player1JLabel.setForeground(Color.white);
                player1JLabel.setText("Du");
                player2JLabel.setFont(new Font("Poppins", Font.BOLD, 17));
                player2JLabel.setForeground(Color.white);
                player2JLabel.setText("Motståndare");

            }
            while (true) {
                fromServer = objIn.readObject();
                if (fromServer instanceof Intro) {
                    homeScreen.setVisible(true);
                    loadingScreen.setVisible(false);
                    categoryScreen.setVisible(false);
                    gameScreen.setVisible(false);
                    resultScreen.setVisible(false);

                } else if (fromServer instanceof Waiting) {
                    homeScreen.setVisible(false);
                    loadingScreen.setVisible(true);
                    categoryScreen.setVisible(false);
                    gameScreen.setVisible(false);
                    resultScreen.setVisible(false);

                } else if (fromServer instanceof GameFinished) {
                    homeScreen.setVisible(false);
                    loadingScreen.setVisible(false);
                    categoryScreen.setVisible(false);
                    gameScreen.setVisible(false);
                    resultScreen.setVisible(true);
                    newGameButton2.setVisible(true);

                    checkWinner();

                } else if (fromServer instanceof boolean[]) {
                    opponentAnswers = (boolean[]) fromServer;
                    updatePlayerResult(answers);
                    updateOpponentResult(opponentAnswers);

                } else if (fromServer instanceof Result) {
                    homeScreen.setVisible(false);
                    loadingScreen.setVisible(false);
                    categoryScreen.setVisible(false);
                    gameScreen.setVisible(false);
                    resultScreen.setVisible(true);


                } else if (fromServer instanceof String) {

                } else {
                    try {
                        listQuestions = (List<Questions>) fromServer;
                        listString = (List<String>) fromServer;
                    } catch (Exception ignore) {
                    }

                    if (listQuestions != null && listQuestions.size() != 0 && listQuestions.get(0) instanceof Questions) {
                        homeScreen.setVisible(false);
                        loadingScreen.setVisible(false);
                        categoryScreen.setVisible(false);
                        gameScreen.setVisible(true);
                        resultScreen.setVisible(false);
                        currentQuestion = 0;
                        questionAmount = listQuestions.size();
                        answers = new boolean[questionAmount];
                        currentRound.setText("Runda: " + rounds);
                        currentRound.setForeground(Color.white);
                        currentRound.setFont(new Font("Poppins", Font.BOLD, 14));
                        rounds++;

                        while (!listQuestions.isEmpty()) {
                            answered = false;
                            answerOptions1Button.setBackground(Color.WHITE);
                            answerOptions2Button.setBackground(Color.WHITE);
                            answerOptions3Button.setBackground(Color.WHITE);
                            answerOptions4Button.setBackground(Color.WHITE);
                            questionLabel.setText(listQuestions.get(0).getQ());
                            answerOptions1Button.setText(listQuestions.get(0).getA1());
                            answerOptions2Button.setText(listQuestions.get(0).getA2());
                            answerOptions3Button.setText(listQuestions.get(0).getA3());
                            answerOptions4Button.setText(listQuestions.get(0).getA4());

                            if (answered) {
                                listQuestions.remove(0);
                                currentQuestion++;
                            }
                        }
                        if (answers.length == questionAmount) {
                            gameScreen.setVisible(false);
                            resultScreen.setVisible(true);
                            objOut.reset();
                            objOut.writeObject(answers);
                        }

                    }
                    if (listString != null && listString.size() != 0 && listString.get(0) instanceof String) {
                        categoryJLabel.setText("Välj kategori");
                        category1Button.setText(listString.get(0));
                        category2Button.setText(listString.get(1));
                        category3Button.setText(listString.get(2));
                        category4Button.setText(listString.get(3));

                        homeScreen.setVisible(false);
                        loadingScreen.setVisible(false);
                        categoryScreen.setVisible(true);
                        gameScreen.setVisible(false);
                        resultScreen.setVisible(false);

                    }
                }
            }
        } finally {
            socket.close();
        }
    }

    public void updatePlayerResult(boolean[] input) {
        JPanel panel = new JPanel();
        for (int i = 0; i < questionAmount; i++) {
            JButton button = new JButton();
            panel.add(button);
            button.setEnabled(false);
            if (input[i]) {
                button.setBackground(Color.GREEN);
            } else button.setBackground(Color.RED);
        }
        for (boolean b : input) {
            if (b) {
                playerPointsCounter++;
            }
        }

        playerPoints.setText(String.valueOf(playerPointsCounter));
        PlayerResult.add(panel);
        PlayerResult.setVisible(true);

    }

    public void updateOpponentResult(boolean[] input) {
        JPanel panel = new JPanel();
        for (int i = 0; i < questionAmount; i++) {
            JButton button = new JButton();
            panel.add(button);
            button.setEnabled(false);
            if (input[i]) {
                button.setBackground(Color.GREEN);
            } else button.setBackground(Color.RED);
        }
        for (boolean b : input) {
            if (b) {
                opponentPointsCounter++;
            }
        }
        opponentPoints.setText(String.valueOf(opponentPointsCounter));
        OpponentResult.add(panel);
        OpponentResult.setVisible(true);

    }

    public void checkWinner() {
        if(playerPointsCounter > opponentPointsCounter){
            JOptionPane.showMessageDialog(null, "DU VANN!");
        } else if (opponentPointsCounter > playerPointsCounter) {
            JOptionPane.showMessageDialog(null, "DU FÖRLORADE!");
        }else {
            JOptionPane.showMessageDialog(null, "DET BLEV LIKA!");
        }
    }

    public static void main(String[] args) throws Exception {
        String serverAddress = (args.length == 0) ? "localhost" : args[1];
        GUI client = new GUI(serverAddress);
        client.play();

    }
}