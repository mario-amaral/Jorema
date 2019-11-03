package org.academiadecodigo.splicegirls.Jorema.Server;

import java.util.LinkedList;
import java.util.Map;

public class GameLogic {

    public LinkedList<Player> countVotes(Map<String, Player > hashtable ) {

        Player p;
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

        LinkedList<Player> winners = new LinkedList<>();
        int winnerVotes = 0;

        for (String i: hashtable.keySet()){
            p = hashtable.get(i);

            if (p.getVotes() > winnerVotes){
                winnerVotes = p.getVotes();
            }
        }

        for (String i: hashtable.keySet()){
            p = hashtable.get(i);

            if (p.getVotes() == winnerVotes){
                winners.add(p);
            }
        }
        incrementScore(winners);
        return winners;
    }

    private void incrementScore(LinkedList<Player> winners) {
        for (Player p: winners) {
            p.incrementScore();
        }

    }

    public LinkedList<Player> countScore(Map<String, Player > hashtable ) {

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
        incrementScore(finalWinners);
        return finalWinners;
    }
}
