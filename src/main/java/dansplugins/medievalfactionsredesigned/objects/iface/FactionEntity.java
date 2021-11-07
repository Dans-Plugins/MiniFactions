package dansplugins.medievalfactionsredesigned.objects.iface;

import dansplugins.medievalfactionsredesigned.json.Jsonify;
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

}
