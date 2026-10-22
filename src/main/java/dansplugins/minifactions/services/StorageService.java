package dansplugins.minifactions.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dansplugins.minifactions.data.PersistentData;
import dansplugins.minifactions.objects.FactionImpl;
import dansplugins.minifactions.objects.PowerRecordImpl;
import dansplugins.minifactions.objects.TerritoryChunkImpl;

/**
 * Saves the factions, power records and territory chunks held in {@link PersistentData} to one
 * JSON file each in the plugin's data folder, and loads them back.
 * <p>
 * Each file is a list with one entry per record, as produced by the record's {@code toJSON()}.
 * </p>
 */
public class StorageService {
    public static final String FACTIONS_FILE_NAME = "factions.json";
    public static final String POWER_RECORDS_FILE_NAME = "powerRecords.json";
    public static final String TERRITORY_CHUNKS_FILE_NAME = "territoryChunks.json";

    private static final Type RECORDS_TYPE = new TypeToken<List<Map<String, String>>>() {}.getType();

    private final File dataFolder;
    private final Logger logger;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Set when a file exists but could not be read. Saving would then replace the records still
    // in that file with whatever was loaded, so it is refused until the plugin is next enabled.
    private boolean loadFailed = false;

    public StorageService(File dataFolder, Logger logger) {
        this.dataFolder = dataFolder;
        this.logger = logger;
    }

    /**
     * Adds every saved record to {@link PersistentData}. A missing file is not an error: nothing
     * has been saved yet.
     */
    public void load() {
        PersistentData persistentData = PersistentData.getInstance();
        load(TERRITORY_CHUNKS_FILE_NAME, record -> persistentData.addTerritoryChunk(new TerritoryChunkImpl(record)));
        load(POWER_RECORDS_FILE_NAME, record -> persistentData.addPowerRecord(new PowerRecordImpl(record)));
        load(FACTIONS_FILE_NAME, record -> persistentData.addFaction(new FactionImpl(record)));
    }

    /**
     * Writes every record held in {@link PersistentData} to disk.
     *
     * @return {@code true} if all three files were written.
     */
    public boolean save() {
        if (loadFailed) {
            logger.severe("Faction data was not saved, because a data file could not be read when the plugin was enabled and saving would overwrite it. Fix or move the file named above, then restart the server.");
            return false;
        }
        PersistentData persistentData = PersistentData.getInstance();
        try {
            write(FACTIONS_FILE_NAME, persistentData.getFactionsAsJson());
            write(POWER_RECORDS_FILE_NAME, persistentData.getPowerRecordsAsJson());
            write(TERRITORY_CHUNKS_FILE_NAME, persistentData.getTerritoryChunksAsJson());
            return true;
        } catch (IOException | RuntimeException e) {
            logger.severe("Faction data could not be saved: " + e);
            return false;
        }
    }

    private void load(String fileName, Consumer<Map<String, String>> addRecord) {
        File file = new File(dataFolder, fileName);
        if (!file.exists()) {
            return;
        }
        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            List<Map<String, String>> records = gson.fromJson(reader, RECORDS_TYPE);
            if (records == null) {
                return;
            }
            for (Map<String, String> record : records) {
                addRecord.accept(record);
            }
        } catch (IOException | RuntimeException e) {
            loadFailed = true;
            logger.severe("Could not read " + file.getPath() + ": " + e + ". Its records were not loaded, and saving is turned off until the next restart so the file is left as it is.");
        }
    }

    /**
     * Writes to a temporary file first, so a failed write leaves the previous file in place.
     */
    private void write(String fileName, List<Map<String, String>> records) throws IOException {
        if (!dataFolder.isDirectory() && !dataFolder.mkdirs()) {
            throw new IOException("Could not create " + dataFolder.getPath());
        }
        File file = new File(dataFolder, fileName);
        File temporaryFile = new File(dataFolder, fileName + ".tmp");
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(temporaryFile), StandardCharsets.UTF_8)) {
            gson.toJson(records, RECORDS_TYPE, writer);
        }
        Files.move(temporaryFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}
