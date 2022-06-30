package dansplugins.minifactions.api.definitions;

import dansplugins.minifactions.MiniFactions;
import dansplugins.minifactions.api.MiniFactionsAPI;
import dansplugins.minifactions.api.definitions.json.Jsonify;
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
     * Method to get the API from {@link MiniFactions}.
     *
     * @return {@link MiniFactionsAPI} instance.
     */
    default MiniFactionsAPI getAPI() {
        return miniFactions.getMiniFactionsAPI(); // TODO: figure out another way to get the API
    }

}
