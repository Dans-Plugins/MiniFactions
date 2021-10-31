package dansplugins.medievalfactionsredesigned.json;

import dansplugins.medievalfactionsredesigned.objects.Faction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Callum Johnson
 * @since 31/10/2021 - 17:54
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsonMember {

    /**
     * The identifier of the Json object to be stored as a Json entry in the map.
     *
     * @return name of the object.
     * @see Faction
     */
    String identifier();

}
