package co.aikar.timings;

public interface Timing extends AutoCloseable
{
    Timing startTiming();

    void stopTiming();

    Timing startTimingIfSync();

    void stopTimingIfSync();

    void abort();

    TimingHandler getTimingHandler();

    void close();
}
