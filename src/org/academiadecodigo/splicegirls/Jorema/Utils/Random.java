package org.academiadecodigo.splicegirls.Jorema.Utils;

public class Random {

    public static int getRandInt(int max) {
        return Random.getRandInt(0, max);
    }

    public static int getRandInt(int min, int max) {
        return (int) (Math.random() * ((max - min) + 1)) + min; //[0...5]  + 10 = [10...15]
    }

    public static int d100(){
        return getRandInt(1, 100);
    }

    public static boolean percentChance(int percentile){
        return d100() > (100 - percentile);
    }

}
