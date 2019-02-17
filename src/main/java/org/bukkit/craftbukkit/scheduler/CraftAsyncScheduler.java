package org.bukkit.craftbukkit.scheduler;

import com.destroystokyo.paper.ServerSchedulerReportingWrapper;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

public class CraftAsyncScheduler extends CraftScheduler {

    private final ThreadPoolExecutor executor;
    private final Executor management;
    private final List<CraftTask> temp;

    CraftAsyncScheduler() {
        super(true);
        this.executor = new ThreadPoolExecutor(4, Integer.MAX_VALUE, 30L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), new ThreadFactoryBuilder().setNameFormat("Craft Scheduler Thread - %1$d").build());
        this.management = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("Craft Async Scheduler Management Thread").build());
        this.temp = new ArrayList<CraftTask>();
        this.executor.allowCoreThreadTimeOut(true);
        this.executor.prestartAllCoreThreads();
    }

    @Override
    public void cancelTask(int taskId) {
        this.management.execute(() -> this.removeTask(taskId));
    }

    private synchronized void removeTask(int taskId) {
        parsePending();
        this.pending.removeIf(task -> {
            if (task.getTaskId() == taskId) {
                task.cancel0();
                return true;
            }
            else {
                return false;
            }
        });
    }

    @Override
    public void mainThreadHeartbeat(int currentTick) {
        this.currentTick = currentTick;
        this.management.execute(() -> this.runTasks(currentTick));
    }

    private synchronized void runTasks(int currentTick) {
        this.parsePending();
        while (!this.pending.isEmpty() && this.pending.peek().getNextRun() <= currentTick) {
            CraftTask task = this.pending.remove();
            if (this.executeTask(task)) {
                final long period = task.getPeriod();
                if (period > 0L) {
                    task.setNextRun(currentTick + period);
                    this.temp.add(task);
                }
            }
            this.parsePending();
        }
        this.pending.addAll(this.temp);
        this.temp.clear();
    }

    private boolean executeTask(CraftTask task) {
        if (isValid(task)) {
            this.runners.put(task.getTaskId(), task);
            this.executor.execute(new ServerSchedulerReportingWrapper(task));
            return true;
        }
        return false;
    }

    @Override
    public synchronized void cancelTasks(Plugin plugin) {
        this.parsePending();
        Iterator<CraftTask> iterator = this.pending.iterator();
        while (iterator.hasNext()) {
            final CraftTask task = iterator.next();
            if (task.getTaskId() != -1 && (plugin == null || task.getOwner().equals(plugin))) {
                task.cancel0();
                iterator.remove();
            }
        }
    }

    @Override
    public synchronized void cancelAllTasks() {
        this.cancelTasks(null);
    }

    static boolean isValid(CraftTask runningTask) {
        return runningTask.getPeriod() >= -1L;
    }
}
