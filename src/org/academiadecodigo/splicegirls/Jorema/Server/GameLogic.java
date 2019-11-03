package org.academiadecodigo.splicegirls.Jorema.Server;

import java.util.LinkedList;
import java.util.Map;

public class GameLogic {

    public LinkedList<Player> returnRoundWinners(Map<String, Player> hashtable) {

        countVotes(hashtable);

        Player p;

        LinkedList<Player> winners = new LinkedList<>();
        int maxVotes = 0;

        for (String i: hashtable.keySet()){
            p = hashtable.get(i);

            if (p.getVotes() > maxVotes){
                maxVotes = p.getVotes();
            }
        }

        for (String i: hashtable.keySet()){
            p = hashtable.get(i);

            if (p.getVotes() == maxVotes){
                winners.add(p);
            }
        }

        incrementScore(winners);
        resetVotes(hashtable);

        return winners;
    }

    public void countVotes(Map<String, Player> hashtable){
        Player p1;
        Player p2;

        for (String i: hashtable.keySet()) {
            for (String j: hashtable.keySet()) {
                p1 = hashtable.get(i);
                p2 = hashtable.get(j);

                if (p1.getCurrentAnswer().equals(p2.getMyVote())){
                    p1.incrementVotes();
                }
            }
        }
    }


    private void incrementScore(LinkedList<Player> winners) {
        for (Player p: winners) {
            p.incrementScore();
        }
    }

    private void resetVotes(Map<String, Player > hashtable){
        for (String key: hashtable.keySet()){
            hashtable.get(key).resetVotes();
        }
    }



    public LinkedList<Player> returnFinalWinners(Map<String, Player> hashtable ) {

        Player p;

        LinkedList<Player> finalWinners = new LinkedList<>();
        int score = 0;

        for (String i: hashtable.keySet()){
            p = hashtable.get(i);

            if (p.getVotes() > score){
                score = p.getScore();
            }
        }

        for (String i: hashtable.keySet()){
            p = hashtable.get(i);

            if (p.getScore() == score){
                finalWinners.add(p);
            }
        }

        resetScore(hashtable);

        return finalWinners;
    }

    private void resetScore(Map<String, Player > hashtable){
        for (String key: hashtable.keySet()){
            hashtable.get(key).resetScore();
        }
    }
}
