package co.aikar.timings;

public class FullServerTickHandler extends TimingHandler
{
    private static final TimingIdentifier IDENTITY = new TimingIdentifier("Minecraft", "Full Server Tick", null, false);
    final TimingData minuteData;
    double avgFreeMemory = -1.0;
    double avgUsedMemory = -1.0;

    FullServerTickHandler() {
        super(IDENTITY);
        minuteData = new TimingData(id);
        TimingsManager.TIMING_MAP.put(IDENTITY, this);
    }

    @Override
    public Timing startTiming() {
        if (TimingsManager.needsFullReset) {
            TimingsManager.resetTimings();
        }
        else if (TimingsManager.needsRecheckEnabled) {
            TimingsManager.recheckEnabled();
        }
        return super.startTiming();
    }

    @Override
    public void stopTiming() {
        super.stopTiming();
        if (!this.isEnabled()) {
            return;
        }
        if (TimingHistory.timedTicks % 20L == 0L) {
            final Runtime runtime = Runtime.getRuntime();
            final double usedMemory = runtime.totalMemory() - runtime.freeMemory();
            final double freeMemory = runtime.maxMemory() - usedMemory;
            if (this.avgFreeMemory == -1.0) {
                this.avgFreeMemory = freeMemory;
            }
            else {
                this.avgFreeMemory = this.avgFreeMemory * 0.9833333333333333 + freeMemory * 0.016666666666666666;
            }
            if (this.avgUsedMemory == -1.0) {
                this.avgUsedMemory = usedMemory;
            }
            else {
                this.avgUsedMemory = this.avgUsedMemory * 0.9833333333333333 + usedMemory * 0.016666666666666666;
            }
        }
        final long start = System.nanoTime();
        TimingsManager.tick();
        final long diff = System.nanoTime() - start;
        TimingsManager.CURRENT = TimingsManager.TIMINGS_TICK;
        TimingsManager.TIMINGS_TICK.addDiff(diff);
        this.record.setCurTickCount(this.record.getCurTickCount() - 1);
        this.minuteData.setCurTickTotal(this.record.getCurTickTotal());
        this.minuteData.setCurTickCount(1);
        final boolean violated = this.isViolated();
        this.minuteData.processTick(violated);
        TimingsManager.TIMINGS_TICK.processTick(violated);
        this.processTick(violated);
        if (TimingHistory.timedTicks % 1200L == 0L) {
            TimingsManager.MINUTE_REPORTS.add(new TimingHistory.MinuteReport());
            TimingHistory.resetTicks(false);
            this.minuteData.reset();
        }
        if (TimingHistory.timedTicks % Timings.getHistoryInterval() == 0L) {
            TimingsManager.HISTORY.add(new TimingHistory());
            TimingsManager.resetTimings();
        }
        TimingsExport.reportTimings();
    }

    boolean isViolated() {
        return this.record.getCurTickTotal() > 50000000L;
    }
}
