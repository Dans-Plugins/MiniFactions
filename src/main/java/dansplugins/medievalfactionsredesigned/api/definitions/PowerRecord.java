package dansplugins.medievalfactionsredesigned.api.definitions;

/**
 * @author Callum Johnson
 * @since 08/11/2021 - 08:11
 */
public interface PowerRecord extends FactionEntity {

    /**
     * Method to get the power related to this record.
     *
     * @return double power.
     */
    double getPower();

    /**
     * Method to set the power related to this record.
     *
     * @param power to set.
     */
    void setPower(double power);

    /**
     * Method to add the specified amount of power to the record.
     *
     * @param power to add.
     */
    default void addPower(double power) {
        setPower(getPower()+power);
    }

    /**
     * Method to remove the specified amount of power to the record.
     *
     * @param power to remove.
     */
    default void removePower(double power) {
        setPower(getPower()-power);
    }

    /**
     * Method to determine if this record has enough power to handle an operation.
     *
     * @param required to do an action.
     * @return {@code true} if it does.
     */
    default boolean hasEnoughPower(double required) {
        return getPower()>=required;
    }

}
