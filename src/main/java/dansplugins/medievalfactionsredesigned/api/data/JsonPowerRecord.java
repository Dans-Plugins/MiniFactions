package dansplugins.medievalfactionsredesigned.api.data;

import dansplugins.medievalfactionsredesigned.api.definitions.PowerRecord;
import dansplugins.medievalfactionsredesigned.api.definitions.json.JsonMember;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class JsonPowerRecord implements PowerRecord {

    @JsonMember(identifier = "id")
    private final UUID id;

    @JsonMember(identifier = "power")
    private double power = 0;

    /**
     *  Method to create a power record with a {@link UUID}.
     *
     * @param uuid of the record.
     */
    public JsonPowerRecord(@NotNull UUID uuid) {
        this.id = uuid;
    }

    /**
     *  Method to create a power record with a player.
     *  <p>
     *      As {@link Player} extends {@link OfflinePlayer}, using {@link OfflinePlayer} accepts both
     *      an {@link OfflinePlayer} and a {@link Player}.
     *      <br><a href="https://www.geeksforgeeks.org/polymorphism-in-java/" target="_blank">More Information.</a>
     *  </p>
     * @param player to create a record for.
     * @see Player
     */
    public JsonPowerRecord(@NotNull OfflinePlayer player) {
        this(player.getUniqueId());
    }


    /**
     * Method to obtain the Id of the FactionEntity.
     *
     * @return {@link UUID} never {@code null}.
     */
    @Override
    public @NotNull UUID getId() {
        return id;
    }

    /**
     * Method to get the power related to this record.
     *
     * @return double power.
     */
    @Override
    public double getPower() {
        return power;
    }

    /**
     * Method to set the power related to this record.
     *
     * @param power to set.
     */
    @Override
    public void setPower(double power) {
        this.power = power;
    }

}
