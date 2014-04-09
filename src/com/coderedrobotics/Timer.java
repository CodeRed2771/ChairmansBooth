package com.coderedrobotics;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
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
    private boolean told;
    
    Robot robot;
    
    JLabel clock;

    public Timer() {
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(Timer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void start() {
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        while (true) {
            if (!done() && clock != null) {
                int secondsLeft = (int) (setAt + setMillis - System.currentTimeMillis()) / 1000;
                clock.setText(String.valueOf((int) (Math.floor(secondsLeft / 60)))
                        + ":" + String.format("%02d", secondsLeft % 60));
            } else {
                if (clock != null) {
                    clock.setText("");
                }
                if (!told) {
                    robot.keyPress(KeyEvent.VK_Q);
                    robot.keyRelease(KeyEvent.VK_Q);
                    told = true;
                }
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
        told = false;
    }

    public boolean done() {
        return setAt + setMillis < System.currentTimeMillis();
    }

    public void set(int millis) {
        setMillis = millis;
        setAt = System.currentTimeMillis();
    }
    
    public void stop() {
        told = true;
        setAt = 0;
        setMillis = 0;
    }
}
