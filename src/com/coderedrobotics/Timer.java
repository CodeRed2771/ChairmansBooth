package com.coderedrobotics;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author Michael
 */
public class Timer implements Runnable {

    private long setAt;
    private int setMillis;

    JLabel clock;

    public Timer() {

    }

    public void start() {
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        while (true) {
            if (!done() && clock != null){
                int secondsLeft = (int) (setAt + setMillis - System.currentTimeMillis()) / 1000;
                clock.setText(String.valueOf((int) (Math.floor(secondsLeft / 60))) 
                        + ":" + String.format("%02d", secondsLeft % 60));
            } else {
                clock.setText("");
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                Logger.getLogger(Timer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void startAndControlClock(JLabel clock, int time) {
        this.clock = clock;
        set(time * 1000);
    }

    public boolean done() {
        return setAt + setMillis < System.currentTimeMillis();
    }

    public void set(int millis) {
        setMillis = millis;
        setAt = System.currentTimeMillis();
    }
}
