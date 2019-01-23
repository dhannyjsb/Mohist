package co.aikar.timings;

import co.aikar.util.JSONUtil;
import com.google.common.base.Function;

import java.util.List;

class TimingHistoryEntry {

    final TimingData data;
    private final TimingData[] children;

    TimingHistoryEntry(final TimingHandler handler) {
        this.data = handler.record.clone();
        this.children = handler.cloneChildren();
    }

    List<Object> export() {
        final List<Object> result = this.data.export();
        if (this.children.length > 0) {
            result.add(JSONUtil.toArrayMapper(this.children, new Function<TimingData, Object>() {
                @Override
                public Object apply(final TimingData child) {
                    return child.export();
                }
            }));
        }
        return result;
    }
}
