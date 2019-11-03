package org.academiadecodigo.splicegirls.Jorema.Utils;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Clock {

    public static final int SECOND = 1000;

    public static void main(String[] args) {

        final Timer timer = new Timer();
        final TimerTask timedReset = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Code Block that resets the game for Live Players");
                timer.cancel();
            }
        };
        final TimerTask timedWarning = new TimerTask() {
            int timeLimit = 15;
                @Override
                public void run() {
                    System.out.println("The game will reset in " + (timeLimit -= 5) + " seconds");
                    System.out.println(new Date().toString().substring(11, 19));
                    if (timeLimit == 0){
                        timer.schedule(timedReset, 30);
                    }
                }
        };


        timer.scheduleAtFixedRate(timedWarning, 0, 5 * SECOND);





    }




}
