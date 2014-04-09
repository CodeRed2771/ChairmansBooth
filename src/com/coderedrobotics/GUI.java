package com.coderedrobotics;

import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author Michael
 */
public class GUI extends javax.swing.JFrame {

    Image coderedlogo;

    public enum State {

        QUESTION, CORRECT, INCORRECT, HOME, END
    }

    State state = State.HOME;
    private int currentCorrectAnswer;
    Preferences prefs;
    QuestionManager questionManager;
    Random rand;
    Timer timer;
    Horn horn;

    double correct;
    double incorrect;
    Question currentQuestion;

    /**
     * Creates new form GUI
     */
    public GUI() {
        initComponents();
        //<editor-fold defaultstate="collapsed" desc=" Center Window to Screen ">
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = g.getScreenDevices();

        int width = devices[0].getDisplayMode().getWidth();
        int height = devices[0].getDisplayMode().getHeight();

        int w = this.getSize().width;
        int h = this.getSize().height;
        int x = (width - w) / 2;
        int y = (height - h) / 2;
        this.setLocation(x, y);
        //</editor-fold>

        addKeyListener(new GUI.KeyListener());
        coderedlogo = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/coderedrobotics/coderedsmall.png"));

        prefs = Preferences.loadPrefs();
        QuestionLoader.readCSVFile("questions.csv", prefs.getCSVSplitBy());
        rand = new Random();
        questionManager = new QuestionManager();
        timer = new Timer();
        horn = new Horn();
        timer.start();
        horn.start();

        if (prefs.getQuestionOrder() == Preferences.QuestionOrder.SHUFFLED) {
            questionManager.shuffleQuestions();
        }

        setHome();
    }

    public final void setQuestion(Question question) {
        String answerString = "";
        ArrayList<String> order = new ArrayList<>();
        order.add(question.getCorrectAnswer());
        order.add(question.getOtherAnswers()[0]);
        order.add(question.getOtherAnswers()[1]);
        order.add(question.getOtherAnswers()[2]);
        Collections.shuffle(order);
        currentCorrectAnswer = order.indexOf(question.getCorrectAnswer());
        currentQuestion = question;
        for (int i = 0; i < 4; i++) {
            String character = "";
            switch (i) {
                case 0:
                    character = "A) ";
                    break;
                case 1:
                    character = "B) ";
                    break;
                case 2:
                    character = "C) ";
                    break;
                case 3:
                    character = "D) ";
                    break;
            }
            answerString += "<h1>" + character + order.get(i) + "</h1>";
        }
        panel.setBackground(Color.BLACK);
        mainPanel.setText("<html><center>"
                + "<h2>Please choose the best response to the following question.  </h2>"
                + "<p>" + question.getQuestion() + "</p>"
                + "</center><br>" + answerString + "</html>");
        if(prefs.usingTimer()){
            timer.startAndControlClock(timerLabel, prefs.getTimerDuration() + 1);
        }
    }

    public final void setHome() {
        panel.setBackground(Color.BLACK);
        mainPanel.setText("<html><center><h1>How well do you know the code?</h2>"
                + "<p>Test your knowledge of Code Red Robotics Facts by taking a brief "
                + String.valueOf(prefs.getNumberOfQuestions()) + " question multiple "
                + "choice quiz.</p>"
                + "<br><i>Press any button to begin...</i></center></html>");
        timer.stop();
    }

    public final void setEnd() {
        panel.setBackground(Color.BLACK);
        String text;
        if (correct > incorrect || correct == incorrect) {
            text = "<h1>YOU WIN!</h1>";
        } else {
            text = "<h1>YOU LOSE!</h1>";
        }
        text += "<h2>You answered " + (int) (correct) + " of "
                + (int) (correct + incorrect) + " questions correctly.  ("
                + ((int) ((correct / (correct + incorrect)) * 100)) + "%)</h2>"
                + "<br><i>Press any key to continue...</i>";
        mainPanel.setText("<html><center>" + text + "</center></html>");
        timer.startAndControlClock(timerLabel, 11);
    }

    public void handleResponse(int i) {
        switch (state) {
            case HOME:
                state = State.QUESTION;
                if (prefs.getQuestionOrder() != Preferences.QuestionOrder.RANDOM) {
                    setQuestion(questionManager.getNextQuestion());
                } else {
                    setQuestion(questionManager.getRandomQuestion());
                }
                break;
            case QUESTION:
                if (i - 1 == currentCorrectAnswer) {
                    state = State.CORRECT;
                    if (prefs.displayingColors()) {
                        panel.setBackground(Color.GREEN);
                    }
                    mainPanel.setText("<html><center><h1>CORRECT!</h1>"
                            + "<p>" + currentQuestion.getExplanation() + "</p>"
                            + "<br><i>Press any key to continue...</i></center></html>");
                    correct++;
                } else {
                    state = State.INCORRECT;
                    if (prefs.displayingColors()) {
                        panel.setBackground(Color.RED);
                    }
                    mainPanel.setText("<html><center><h1>INCORRECT!</h1>"
                            + "<h2>The correct answer is: " + currentQuestion.getCorrectAnswer()
                            + "<p>" + currentQuestion.getExplanation() + "</p>"
                            + "<br><i>Press any key to continue...</i></center></html>");

                    if (prefs.useHorn()) {
                        horn.burstHorn(prefs.getHornDuration());
                    }
                    incorrect++;
                }
                timer.startAndControlClock(timerLabel, 31);
                break;
            case CORRECT:
                if (incorrect + correct < prefs.getNumberOfQuestions()) {
                    state = State.QUESTION;
                    if (prefs.getQuestionOrder() != Preferences.QuestionOrder.RANDOM) {
                        setQuestion(questionManager.getNextQuestion());
                    } else {
                        setQuestion(questionManager.getRandomQuestion());
                    }
                } else if (incorrect + correct == prefs.getNumberOfQuestions()) {
                    state = State.END;
                    setEnd();
                }
                break;
            case INCORRECT:
                if (incorrect + correct < prefs.getNumberOfQuestions()) {
                    state = State.QUESTION;
                    if (prefs.getQuestionOrder() != Preferences.QuestionOrder.RANDOM) {
                        setQuestion(questionManager.getNextQuestion());
                    } else {
                        setQuestion(questionManager.getRandomQuestion());
                    }
                } else if (incorrect + correct == prefs.getNumberOfQuestions()) {
                    state = State.END;
                    setEnd();
                }
                break;
            case END:
                state = State.HOME;
                correct = 0;
                incorrect = 0;
                currentQuestion = null;
                setHome();
                break;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JPanel();
        logo = new javax.swing.JLabel();
        title = new javax.swing.JLabel();
        mainPanel = new javax.swing.JLabel();
        timerLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chairman's Quiz");
        setUndecorated(true);

        panel.setBackground(new java.awt.Color(0, 0, 0));

        logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/coderedrobotics/coderedsmall.png"))); // NOI18N
        logo.setText("jLabel1");

        title.setFont(new java.awt.Font("Tahoma", 1, 90)); // NOI18N
        title.setForeground(new java.awt.Color(255, 255, 255));
        title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title.setText("DO YOU KNOW THE CODE?");

        mainPanel.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        mainPanel.setForeground(new java.awt.Color(255, 255, 255));
        mainPanel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mainPanel.setText("SOME PANEL");
        mainPanel.setMaximumSize(new java.awt.Dimension(702, 30));

        timerLabel.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        timerLabel.setForeground(new java.awt.Color(255, 255, 255));
        timerLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        timerLabel.setText(" ");

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelLayout.createSequentialGroup()
                        .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(title, javax.swing.GroupLayout.DEFAULT_SIZE, 1467, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(timerLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(logo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 818, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(timerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the System Default Look and Feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_CAPS_LOCK, false);
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel logo;
    private javax.swing.JLabel mainPanel;
    private javax.swing.JPanel panel;
    private javax.swing.JLabel timerLabel;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables

    private class KeyListener extends KeyAdapter {

        public KeyListener() {
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_1:
                    handleResponse(1);
                    break;
                case KeyEvent.VK_2:
                    handleResponse(2);
                    break;
                case KeyEvent.VK_3:
                    handleResponse(3);
                    break;
                case KeyEvent.VK_4:
                    handleResponse(4);
                    break;
                case KeyEvent.VK_CAPS_LOCK:
                    break;
                case KeyEvent.VK_SCROLL_LOCK:
                    break;
                case KeyEvent.VK_NUM_LOCK:
                    break;
                case KeyEvent.VK_Q:                   
                    state = State.HOME;
                    correct = 0;
                    incorrect = 0;
                    currentQuestion = null;
                    setHome();
                    break;
                default:
                    handleResponse(0);
                    break;
            }
        }
    }
}
