package dansplugins.medievalfactionsredesigned.objects;

import dansplugins.medievalfactionsredesigned.json.JsonMember;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PowerRecord {

    @JsonMember(identifier = "id")
    private UUID id;

    @JsonMember(identifier = "power")
    private double power = 0;

    /**
     *  Method to create a power record with a {@link UUID}.
     *
     * @param uuid
     */
    public PowerRecord(UUID uuid) {
        this.id = uuid;
    }

    /**
     *  Method to create a power record with a player.
     *
     * @param player
     */
    public PowerRecord(Player player) {
        this(player.getUniqueId());
    }

    /**
     *  Method to set the amount of power associated with this record.
     *
     * @param newPower
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
