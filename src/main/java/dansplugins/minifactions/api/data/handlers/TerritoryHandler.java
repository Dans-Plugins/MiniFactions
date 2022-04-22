package dansplugins.minifactions.api.data.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import dansplugins.minifactions.MiniFactions;
import dansplugins.minifactions.api.data.JsonTerritoryChunk;
import dansplugins.minifactions.api.data.abs.TerritoryData;
import dansplugins.minifactions.api.definitions.core.TerritoryChunk;
import dansplugins.minifactions.api.exceptions.TerritoryFileException;

import org.bukkit.Chunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Callum Johnson
 * @since 11/12/2021 - 21:57
 */
public class TerritoryHandler implements TerritoryData {

    /**
     * File linked to the territory data.
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
    public TerritoryHandler() {
        file = new File(MiniFactions.getInstance().getDataFolder(), "territoryChunks.json");
        try {
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    System.out.println("Failed to load '" + file.getName() + "'!");
                    throw new TerritoryFileException("Unable to create new file.");
                }
            }
        } catch (IOException exception) {
            throw new TerritoryFileException("IOException experienced:\t" + exception.getMessage());
        }
        load();
    }

    /**
     * Method to obtain a chunk from its Id.
     * <p>
     *     Due to the nature of Json and GSON specifically, unchecked is dampened
     *     we hard-cast the data to a 'String:String' data-set.
     * </p>
     *
     * @param chunkId to get a chunk for.
     * @return {@link TerritoryChunk} if found, or {@code null} if it isn't.
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public TerritoryChunk getChunk(@NotNull UUID chunkId) {
        if (!data.containsKey(chunkId) && !data.containsKey(chunkId.toString())) {
            return null;
        }

        Object object = data.getOrDefault(chunkId, null);
        if (object == null) {
            data.getOrDefault(chunkId.toString(), null);
        }
        if (object == null) {
            return null;
        }

        final TerritoryChunk chunk = new JsonTerritoryChunk();
        chunk.fromJSON((Map<String, String>) object);

        return chunk;
    }

    /**
     * 
     */
    public void save() {
        // TODO: implement
    }

    /**
     * Method to load the data for territory handling.
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

    @Override
    public boolean addTerritoryChunk(TerritoryChunk territoryChunk) {
        // TODO: implement
        return false;
    }

    @Override
    public TerritoryChunk getTerritoryChunk(UUID territoryChunkUUID) {
        // TODO: implement
        return null;
    }

    @Override
    public TerritoryChunk getTerritoryChunk(Chunk chunk) {
        // TODO: implement
        return null;
    }

    @Override
    public TerritoryChunk getTerritoryChunk(int x, int z, UUID worldUUID) {
        // TODO: implement
        return null;
    }

    @Override
    public boolean doesTerritoryChunkExist(Chunk chunk) {
        // TODO: implement
        return false;
    }

    @Override
    public boolean isTerritoryChunkClaimed(TerritoryChunk territoryChunk) {
        // TODO: implement
        return false;
    }

    @Override
    public List<Map<String, String>> getTerritoryChunksAsJson() {
        // TODO: implement
        return null;
    }

    @Override
    public void clearTerritoryChunks() {
        // TODO: implement
        
    }
}