package dansplugins.minifactions.api.data.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import dansplugins.minifactions.MiniFactions;
import dansplugins.minifactions.api.data.JsonFaction;
import dansplugins.minifactions.api.definitions.core.Faction;
import dansplugins.minifactions.api.exceptions.FactionFileException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Daniel McCoy Stephenson
 * @since April 16th, 2022
 */
public class FactionHandler {

    /**
     * File linked to the faction data.
     */
    private final File file;

    /**
     * Data from the file.
     */
    private HashMap<?, ?> data = null;

    /**
     * Constructor to initialise {@link #file} and then populate {@link #data}.
     *
     * @see #load()
     */
    public FactionHandler() {
        file = new File(MiniFactions.getInstance().getDataFolder(), "factions.json");
        try {
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    System.out.println("Failed to load '" + file.getName() + "'!");
                    throw new FactionFileException("Unable to create new file.");
                }
            }
        } catch (IOException exception) {
            throw new FactionFileException("IOException experienced:\t" + exception.getMessage());
        }
        load();
    }

    /**
     * Method to obtain a faction from its Id.
     * <p>
     *     Due to the nature of Json and GSON specifically, unchecked is dampened
     *     we hard-cast the data to a 'String:String' data-set.
     * </p>
     *
     * @param factionId to get a faction for.
     * @return {@link Faction} if found, or {@code null} if it isn't.
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public Faction getFaction(@NotNull UUID factionId) {
        if (!data.containsKey(factionId) && !data.containsKey(factionId.toString())) {
            return null;
        }

        Object object = data.getOrDefault(factionId, null);
        if (object == null) {
            data.getOrDefault(factionId.toString(), null);
        }
        if (object == null) {
            return null;
        }

        final Faction faction = new JsonFaction();
        faction.fromJSON((Map<String, String>) object);

        return faction;
    }

    /**
     * 
     */
    public void save() {
        // TODO: implement
    }

    /**
     * Method to load the data for faction handling.
     */
    private void load() {
        try {
            final FileInputStream fileInputStream = new FileInputStream(file);
            final InputStreamReader inputStream = new InputStreamReader(fileInputStream);
            final JsonReader reader = new JsonReader(inputStream);
            final Gson gson = new GsonBuilder().setPrettyPrinting().create();
            final Object jsonData = gson.fromJson(reader, HashMap.class);

            if (!(jsonData instanceof HashMap)) {
                return;
            }
            
            this.data = ((HashMap<?, ?>) jsonData);

            // Close the Readers ♥
            reader.close();
            inputStream.close();
            fileInputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            this.data = new HashMap<>();
        }
    }
}