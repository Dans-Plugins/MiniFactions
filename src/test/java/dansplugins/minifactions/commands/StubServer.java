package dansplugins.minifactions.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * The stub {@link Server} the command tests run against. It answers only the calls the commands
 * make, so the messages players receive are observed rather than read from the source, and no live
 * Bukkit server is needed.
 *
 * <p>Bukkit's server can be installed only once per JVM, so every command test class shares this
 * one and its online players. Each test class calls {@link #install()} before its tests and
 * {@link #reset()} after each one.
 */
final class StubServer {
    /** Online players by id, in the order they came online. */
    static final Map<UUID, Player> ONLINE = new LinkedHashMap<>();
    /** The messages each online player has received, by player id. */
    static final Map<UUID, List<String>> RECEIVED = new LinkedHashMap<>();

    private StubServer() {
    }

    static void install() {
        if (Bukkit.getServer() == null) {
            Bukkit.setServer(stub(Server.class, StubServer::answerServerCall));
        }
    }

    static void reset() {
        ONLINE.clear();
        RECEIVED.clear();
    }

    static Player online(UUID id, String name) {
        return online(id, name, null);
    }

    /**
     * Brings a player online.
     *
     * @param location Where the player stands, or null if no test asks.
     */
    static Player online(UUID id, String name, Location location) {
        List<String> received = new ArrayList<>();
        RECEIVED.put(id, received);
        Player player = stub(Player.class, (method, args) -> {
            switch (method.getName()) {
                case "getUniqueId":
                    return id;
                case "getName":
                    return name;
                case "isOnline":
                    return true;
                case "getLocation":
                    return location;
                case "sendMessage":
                    received.add((String) args[0]);
                    return null;
                default:
                    return null;
            }
        });
        ONLINE.put(id, player);
        return player;
    }

    /** A sender that is not a player, such as the console, whose messages go to {@code received}. */
    static CommandSender commandSender(List<String> received) {
        return stub(CommandSender.class, (method, args) -> {
            if (method.getName().equals("sendMessage")) {
                received.add((String) args[0]);
            }
            return null;
        });
    }

    private static Object answerServerCall(Method method, Object[] args) {
        switch (method.getName()) {
            case "getLogger":
                return Logger.getLogger(StubServer.class.getName());
            case "getName":
            case "getVersion":
            case "getBukkitVersion":
                return "test";
            case "getOnlinePlayers":
                return new ArrayList<>(ONLINE.values());
            case "getOfflinePlayers":
                return ONLINE.values().toArray(new Player[0]);
            case "getPlayer":
            case "getOfflinePlayer":
                return args[0] instanceof UUID ? ONLINE.get(args[0]) : byName((String) args[0]);
            default:
                return null;
        }
    }

    private static Player byName(String name) {
        return ONLINE.values().stream().filter(player -> player.getName().equals(name)).findFirst().orElse(null);
    }

    interface Answer {
        Object call(Method method, Object[] args);
    }

    /** A proxy of {@code type} whose calls {@code answer} handles; primitives default to false or 0. */
    @SuppressWarnings("unchecked")
    static <T> T stub(Class<T> type, Answer answer) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, (proxy, method, args) -> {
            Object result = answer.call(method, args);
            if (result != null || !method.getReturnType().isPrimitive() || method.getReturnType() == void.class) {
                return result;
            }
            return method.getReturnType() == boolean.class ? Boolean.FALSE : Integer.valueOf(0);
        });
    }
}
