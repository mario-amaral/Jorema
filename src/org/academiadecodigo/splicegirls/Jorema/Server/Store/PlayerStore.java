package org.academiadecodigo.splicegirls.Jorema.Server.Store;
import org.academiadecodigo.splicegirls.Jorema.Server.Player;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public interface PlayerStore {

    public void addPlayer(String playerName);

    public void removePlayer(String name);

    public Player getPlayer(String name);

    public Map<String, Player> getPlayerTable();
}
