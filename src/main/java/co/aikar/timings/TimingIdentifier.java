package co.aikar.timings;

import co.aikar.util.LoadingMap;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>Used as a basis for fast HashMap key comparisons for the Timing Map.</p>
 *
 * This class uses interned strings giving us the ability to do an identity check instead of equals() on the strings
 */
final class TimingIdentifier {
    /**
     * Holds all groups. Autoloads on request for a group by name.
     */
    static final Map<String, TimingGroup> GROUP_MAP = LoadingMap.of(new ConcurrentHashMap<>(64, .5F), TimingGroup::new);
    private static final TimingGroup DEFAULT_GROUP = getGroup("Minecraft");
    final String group;
    final String name;
    final TimingHandler groupHandler;
    final boolean protect;
    private final int hashCode;

    TimingIdentifier(String group, String name, Timing groupHandler, boolean protect) {
        this.group = group != null ? group: DEFAULT_GROUP.name;
        this.name = name;
        this.groupHandler = groupHandler != null ? groupHandler.getTimingHandler() : null;
        this.protect = protect;
        this.hashCode = (31 * this.group.hashCode()) + this.name.hashCode();
    }

    static TimingGroup getGroup(String groupName) {
        if (groupName == null) {
                return DEFAULT_GROUP;
            }
        return GROUP_MAP.get(groupName);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
                return false;
            }

                TimingIdentifier that = (TimingIdentifier) o;
        return group == that.group && name == that.name;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    static class TimingGroup {

        private static AtomicInteger idPool = new AtomicInteger(1);
        final int id = idPool.getAndIncrement();

        final String name;
        ArrayDeque<TimingHandler> handlers = new ArrayDeque<TimingHandler>(64);

        private TimingGroup(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TimingGroup that = (TimingGroup) o;
            return id == that.id;
        }

        @Override
        public int hashCode() {
            return id;
        }
    }
}
