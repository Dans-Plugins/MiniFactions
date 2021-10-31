package dansplugins.medievalfactionsredesigned.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Callum Johnson
 * @since 31/10/2021 - 17:49
 */
public interface Jsonify {

    /**
     * Method to load an entry to JSON.
     * <p>
     *     To support older versions of Minecraft, we need to support
     *     older versions of Java, deprecation is here because of old methods.
     * </p>
     *
     * @return JSON representation of the in-class data.
     */
    @SuppressWarnings("deprecation")
    @NotNull
    default Map<String, String> toJSON() {
        final Map<String, Field> fields = validateFields();
        final Map<String, String> data = new HashMap<>();
        final Gson gson = obtainGson();
        try {
            for (Map.Entry<String, Field> entity : fields.entrySet()) {
                if (!entity.getValue().isAccessible()) entity.getValue().setAccessible(true);
                data.put(entity.getKey(), gson.toJson(entity.getValue().get(this)));
            }
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
        }
        return data;
    }

    /**
     * Method to load an entry from JSON.
     *
     * @param json data to load the in-class data from.
     */
    default void fromJSON(@NotNull Map<String, String> json) {
        final Map<String, Field> fields = validateFields();
        final Gson gson = obtainGson();
        try {
            for (Map.Entry<String, Field> entity : fields.entrySet()) {
                final Field value = entity.getValue();
                value.setAccessible(true);
                value.set(this, gson.fromJson(json.get(entity.getKey()), value.getType()));
            }
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Collect all valid fields from within the Jsonify class.
     *
     * @return Map of Id:Field.
     * @throws IllegalStateException if the identifiers in the variables are not compatible with each other.
     */
    @NotNull
    default Map<String, Field> validateFields() throws IllegalStateException {
        final Map<String, Field> fields = new HashMap<>();
        final Class<?> overhead = getClass();
        for (Field declaredField : overhead.getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(JsonMember.class)) {
                final JsonMember memberDetails = declaredField.getAnnotation(JsonMember.class);
                if (fields.containsKey(memberDetails.identifier())) {
                    throw new IllegalStateException("JsonMember identifier overlap.");
                } else {
                    fields.put(memberDetails.identifier(), declaredField);
                }
            }
        }
        return fields;
    }

    /**
     * We use Gson to save our data to the file.
     *
     * @return Gson object.
     */
    @NotNull
    default Gson obtainGson() {
        return new GsonBuilder().create();
    }

}
