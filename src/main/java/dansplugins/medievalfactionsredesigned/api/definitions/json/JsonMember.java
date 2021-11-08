package dansplugins.medievalfactionsredesigned.api.definitions.json;

import dansplugins.medievalfactionsredesigned.api.data.temp.JsonFaction;

import java.lang.annotation.*;

/**
 * @author Callum Johnson
 * @since 31/10/2021 - 17:54
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsonMember {

    /**
     * The identifier of the Json object to be stored as a Json entry in the map.
     *
     * @return name of the object.
     * @see JsonFaction
     */
    String identifier();

}
