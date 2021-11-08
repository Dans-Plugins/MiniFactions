package dansplugins.medievalfactionsredesigned.api.definitions;

import dansplugins.medievalfactionsredesigned.MedievalFactions;
import dansplugins.medievalfactionsredesigned.api.MedievalFactionsAPI;
import dansplugins.medievalfactionsredesigned.api.definitions.json.Jsonify;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author Callum Johnson
 * @since 07/11/2021 - 11:29
 */
public interface FactionEntity extends Jsonify {

    /**
     * Method to obtain the Id of the FactionEntity.
     *
     * @return {@link UUID} never {@code null}.
     */
    @NotNull
    UUID getId();

    /**
     * Method to get the API from {@link MedievalFactions}.
     *
     * @return {@link MedievalFactionsAPI} instance.
     */
    default MedievalFactionsAPI getAPI() {
        return MedievalFactions.getInstance().getMedievalFactionsAPI();
    }

}
