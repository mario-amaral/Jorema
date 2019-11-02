package org.academiadecodigo.splicegirls.Jorema.Server.Store;
import org.academiadecodigo.splicegirls.Jorema.Server.Player;

public interface PlayerStore {

    public void addPlayer(String playerName);

    public void removePlayer(String name);

    public Player getPlayer(String name);

}
