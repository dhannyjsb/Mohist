package co.aikar.timings;

import org.bukkit.Bukkit;

class UnsafeTimingHandler extends TimingHandler
{
    UnsafeTimingHandler(final TimingIdentifier id) {
        super(id);
    }

    private static void checkThread() {
        if (!Bukkit.isPrimaryThread()) {
            throw new IllegalStateException("Calling Timings from Async Operation");
        }
    }

    @Override
    public Timing startTiming() {
        checkThread();
        return super.startTiming();
    }

    @Override
    public void stopTiming() {
        checkThread();
        super.stopTiming();
    }
}
