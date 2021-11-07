package dansplugins.medievalfactionsredesigned.objects;

import dansplugins.medievalfactionsredesigned.json.JsonMember;
import dansplugins.medievalfactionsredesigned.json.Jsonify;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PowerRecord implements Jsonify {

    @JsonMember(identifier = "id")
    private final UUID id;

    @JsonMember(identifier = "power")
    private double power = 0;

    /**
     *  Method to create a power record with a {@link UUID}.
     *
     * @param uuid of the record.
     */
    public PowerRecord(@NotNull UUID uuid) {
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
    public PowerRecord(@NotNull OfflinePlayer player) {
        this(player.getUniqueId());
    }

    /**
     *  Method to set the amount of power associated with this record.
     *
     * @param newPower overridden power value to set.
     */
    public void setPower(double newPower) {
        power = newPower;
    }

    /**
     *  Method to get the amount of power associated with this record.
     */
    public double getPower() {
        return power;
    }

}
