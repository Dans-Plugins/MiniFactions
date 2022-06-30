package dansplugins.minifactions.api.data.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import dansplugins.minifactions.MiniFactions;
import dansplugins.minifactions.api.data.JsonPowerRecord;
import dansplugins.minifactions.api.data.abs.PowerData;
import dansplugins.minifactions.api.definitions.PowerRecord;
import dansplugins.minifactions.api.exceptions.PowerRecordFileException;

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
 * @author Daniel McCoy Stephenson
 * @since April 16th, 2022
 */
public class PowerRecordHandler implements PowerData {

    /**
     * File linked to the power record data.
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
     * @param miniFactions
     */
    public PowerRecordHandler(MiniFactions miniFactions) {
        file = new File(miniFactions.getDataFolder(), "powerRecords.json");
        try {
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    System.out.println("Failed to load '" + file.getName() + "'!");
                    throw new PowerRecordFileException("Unable to create new file.");
                }
            }
        } catch (IOException exception) {
            throw new PowerRecordFileException("IOException experienced:\t" + exception.getMessage());
        }
        load();
    }

    /**
     * Method to obtain a powerrecord from its Id.
     * <p>
     *     Due to the nature of Json and GSON specifically, unchecked is dampened
     *     we hard-cast the data to a 'String:String' data-set.
     * </p>
     *
     * @param powerRecordId to get a powerrecord for.
     * @return {@link PowerRecord} if found, or {@code null} if it isn't.
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public PowerRecord getPowerRecord(@NotNull UUID powerRecordId) {
        if (!data.containsKey(powerRecordId) && !data.containsKey(powerRecordId.toString())) {
            return null;
        }

        Object object = data.getOrDefault(powerRecordId, null);
        if (object == null) {
            data.getOrDefault(powerRecordId.toString(), null);
        }
        if (object == null) {
            return null;
        }

        final PowerRecord powerrecord = new JsonPowerRecord(powerRecordId);
        powerrecord.fromJSON((Map<String, String>) object);

        return powerrecord;
    }

    /**
     * 
     */
    public void save() {
        // TODO: implement
    }

    /**
     * Method to load the data for powerrecord handling.
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
    public boolean addPowerRecord(PowerRecord powerRecord) {
        // TODO: implement
        return false;
    }

    @Override
    public boolean hasPowerRecord(UUID playerUUID) {
        // TODO: implement
        return false;
    }

    @Override
    public List<Map<String, String>> getPowerRecordsAsJson() {
        // TODO: implement
        return null;
    }

    @Override
    public void clearPowerRecords() {
        // TODO: implement
        
    }
}