package com.coderedrobotics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael
 */
public class Preferences {

    public enum QuestionOrder {

        RANDOM, SHUFFLED, SEQUENTIAL
    }
    private QuestionOrder questionOrder = QuestionOrder.SHUFFLED;
    private boolean displayColors = true;
    private boolean useHorn = true;
    private int hornDuration = 500;
    private boolean useTimer = true;
    private int timerDuration = 15;
    private int numberOfQuestions = 5;
    private String CSVSplitBy = "^";

    private static final String ymlFile = "# This is the main configuration file for Code Red Robotic's\r\n"
            + "# Chairman's Quiz App.  You can find more help at the bottom\r\n"
            + "# of the file.\r\n"
            + "# http://www.coderedrobotics.com/\r\n"
            + "#\r\n"
            + "# CONFIGURATION:\r\n"
            + "    questionOrder = SHUFFLED\r\n"
            + "    displayColors = true\r\n"
            + "    useHorn = true\r\n"
            + "    hornDuration = 500\r\n"
            + "    useTimer = true\r\n"
            + "    timerDuration = 15\r\n"
            + "    numberOfQuestions = 5\r\n"
            + "    CSVSplitBy = ^\r\n"
            + "#\r\n"
            + "# So, How do I configure Stuff? Well.....\r\n"
            + "#\r\n"
            + "# questionOrder can either be RANDOM, SHUFFLED, or SEQUENTIAL.\r\n"
            + "#\r\n"
            + "# displayColors determines whether colors are displayed when a\r\n"
            + "# question is right or wrong.  Can be true/false.\r\n"
            + "#\r\n"
            + "# useHorn determines whether the horn is used. Can be true/false.\r\n"
            + "#\r\n"
            + "# hornDuration sets how long the horn is played, and can be any \r\n"
            + "# positive integer, only applicable if useHorn has been set to true.  \r\n"
            + "# \r\n"
            + "# useTimer determines whether or not the player plays agains a clock.\r\n"
            + "# This can be true/false.\r\n"
            + "#\r\n"
            + "# timerDuration sets how long the timer is set for, if the useTimer\r\n"
            + "# variable is set to true.  Can be any positive integer.\r\n"
            + "#\r\n"
            + "# numberOfQuestions sets how many questions are in one quiz. IT\r\n"
            + "# can be any positive integer.\r\n"
            + "#\r\n"
            + "# CSVSplitBy can equal any character, or any string of characters.\r\n"
            + "# This determines what to split the question CSV file by.  Please\r\n"
            + "# only modify this if you are absolutely sure you know what you \r\n"
            + "# are doing.  (The recommended character is \"^\").";
    
    public Preferences(QuestionOrder questionOrder, boolean displayColors,
            boolean useHorn, int hornDuration, boolean useTimer, int timerDuration,
            int numberOfQuestions, String CSVSplitBy) {
        this.questionOrder = questionOrder;
        this.displayColors = displayColors;
        this.useHorn = useHorn;
        this.hornDuration = hornDuration;
        this.useTimer = useTimer;
        this.timerDuration = timerDuration;
        this.numberOfQuestions = numberOfQuestions;
        this.CSVSplitBy = CSVSplitBy;
    }

    public Preferences() {
    }

    public QuestionOrder getQuestionOrder() {
        return questionOrder;
    }

    public void setQuestionOrder(QuestionOrder questionOrder) {
        this.questionOrder = questionOrder;
    }

    public boolean displayingColors() {
        return displayColors;
    }

    public void setDisplayColors(boolean displayColors) {
        this.displayColors = displayColors;
    }

    public boolean useHorn() {
        return useHorn;
    }

    public void setUseHorn(boolean useHorn) {
        this.useHorn = useHorn;
    }

    public int getHornDuration() {
        return hornDuration;
    }

    public void setHornDuration(int hornDuration) {
        this.hornDuration = hornDuration;
    }

    public boolean usingTimer() {
        return useTimer;
    }

    public void setUseTimer(boolean useTimer) {
        this.useTimer = useTimer;
    }

    public int getTimerDuration() {
        return timerDuration;
    }

    public void setTimerDuration(int timerDuration) {
        this.timerDuration = timerDuration;
    }

    public String getCSVSplitBy() {
        return CSVSplitBy;
    }

    public void setCSVSplitBy(String CSVSplitBy) {
        this.CSVSplitBy = CSVSplitBy;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public static Preferences loadPrefs() {
        try {
            File file = new File("preferences.yml");
            if (!file.exists()) {
                file.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(ymlFile);
                writer.flush();
                writer.close();
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            QuestionOrder questionOrder = QuestionOrder.SHUFFLED;
            boolean displayColors = true;
            boolean useHorn = true;
            int hornDuration = 500;
            boolean useTimer = true;
            int timerDuration = 15;
            int numberOfQuestions = 5;
            String CSVSplitBy = "^";
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                line = line.replaceAll(" ", "");
                if (!"#".equals(line.substring(0, 1))) {
                    Object[] params = line.split("=");
                    switch (params[0].toString()) {
                        case "questionOrder":
                            questionOrder = QuestionOrder.valueOf(params[1].toString());
                            break;
                        case "displayColors":
                            displayColors = Boolean.valueOf(params[1].toString());
                            break;
                        case "useHorn":
                            useHorn = Boolean.valueOf(params[1].toString());
                            break;
                        case "hornDuration":
                            hornDuration = Integer.parseInt(params[1].toString());
                            break;
                        case "useTimer":
                            useTimer = Boolean.valueOf(params[1].toString());
                            break;
                        case "timerDuration":
                            timerDuration = Integer.parseInt(params[1].toString());
                            break;
                        case "numberOfQuestions":
                            numberOfQuestions = Integer.parseInt(params[1].toString());
                        case "CSVSplitBy":
                            CSVSplitBy = params[1].toString();
                            break;
                    }
                }
            }
            reader.close();
            return new Preferences(questionOrder, displayColors, useHorn,
                    hornDuration, useTimer, timerDuration, numberOfQuestions, CSVSplitBy);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
            return new Preferences();
        } catch (IOException ex) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
            return new Preferences();
        }
    }
}
