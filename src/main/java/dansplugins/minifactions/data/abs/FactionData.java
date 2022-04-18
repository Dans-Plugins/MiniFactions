package dansplugins.minifactions.data.abs;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.definitions.core.FactionPlayer;
import dansplugins.minifactions.api.definitions.core.TerritoryChunk;

/**
 * @author Daniel McCoy Stephenson
 * @since April 17th, 2022
 */
public interface FactionData {
    Faction getFaction(String name) throws Exception;
    Faction getFaction(UUID factionUUID) throws Exception;
    boolean addFaction(Faction faction);
    Faction getFactionByPlayer(FactionPlayer factionPlayer);
    Faction getFactionByChunk(TerritoryChunk territoryChunk);
    boolean removeFaction(Faction faction);
    String getFactionList();
    List<Map<String, String>> getFactionsAsJson();
    void clearFactions();
}