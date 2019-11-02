package org.academiadecodigo.splicegirls.Jorema.Server.Store;

import org.academiadecodigo.splicegirls.Jorema.Server.Player;

import java.util.Hashtable;
import java.util.Map;

public class PlayerStoreImp implements PlayerStore {

    Map<String,Player> players;


    public PlayerStoreImp(){
        players = new Hashtable<>();
    }

    @Override
    public void addPlayer(String name) {

        if(players.keySet().contains(name)){
            System.out.println("There is already a player with that name");
        } else {
            players.put(name, new Player(name));
        }
    }

    @Override
    public void removePlayer(String name) {
        players.remove(name);
    }

    @Override
    public Player getPlayer(String name) {
        return players.get(name);
    }

    @Override
    public Map<String, Player> getPlayerTable() {
        return players;
    }


}
