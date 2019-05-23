package com.parabox.protocol;

import com.parabox.protocol.core.methods.Parabox;
import com.parabox.protocol.core.methods.JsonRpc2_0Paraboxj;
import com.cryptape.cita.protocol.CITAj;
import com.cryptape.cita.protocol.CITAjService;

import java.util.concurrent.ScheduledExecutorService;

public interface Paraboxj extends CITAj, Parabox {

    /**
     * Construct a new Paraboxj instance.
     *
     * @param citajService citaj service instance - i.e. HTTP or IPC
     * @return new Paraboxj instance
     */
    static Paraboxj build(CITAjService citajService) {
        return new JsonRpc2_0Paraboxj(citajService);
    }

    static Paraboxj build(CITAjService citajService, long pollingInterval) {
        return new JsonRpc2_0Paraboxj(citajService, pollingInterval);
    }

    /**
     * Construct a new Paraboxj instance.
     *
     * @param citajService citaj service instance - i.e. HTTP or IPC
     * @param pollingInterval polling interval for responses from network nodes
     * @param scheduledExecutorService executor service to use for scheduled tasks.
     *                                 <strong>You are responsible for terminating this thread
     *                                 pool</strong>
     * @return new Paraboxj instance
     */
    static Paraboxj build(
            CITAjService citajService, long pollingInterval,
            ScheduledExecutorService scheduledExecutorService) {
        return new JsonRpc2_0Paraboxj(citajService, pollingInterval, scheduledExecutorService);
    }

}
