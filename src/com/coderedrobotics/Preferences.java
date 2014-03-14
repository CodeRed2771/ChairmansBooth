package com.coderedrobotics;

/**
 *
 * @author Michael
 */
public class Preferences {

    public enum QuestionOrder {

        RANDOM, SHUFFLED, SEQUENTIAL
    }
    private QuestionOrder questionOrder;
    private boolean displayColors;
    private boolean useHorn;
    private int hornDuration;
    private boolean useTimer;
    private int timerDuration;
    private String CSVSplitBy = ",";

    public Preferences(QuestionOrder questionOrder, boolean displayColors, boolean useHorn, int hornDuration, boolean useTimer, int timerDuration) {
        this.questionOrder = questionOrder;
        this.displayColors = displayColors;
        this.useHorn = useHorn;
        this.hornDuration = hornDuration;
        this.useTimer = useTimer;
        this.timerDuration = timerDuration;
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
}
