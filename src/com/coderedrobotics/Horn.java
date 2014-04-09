package com.coderedrobotics;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael
 */
public class Horn implements Runnable {
    long setMillis;
    long setAt;
    
    public Horn(){ 
    }
    
    public void start(){
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        while (true) {
            if (done()){
                Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_CAPS_LOCK, false);
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                Logger.getLogger(Timer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void burstHorn(int millis){
        set(millis);
        Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_CAPS_LOCK, true);
    }
    
    public boolean done() {
        return setAt + setMillis < System.currentTimeMillis();
    }

    private void set(int millis) {
        setMillis = millis;
        setAt = System.currentTimeMillis();
    }
}
