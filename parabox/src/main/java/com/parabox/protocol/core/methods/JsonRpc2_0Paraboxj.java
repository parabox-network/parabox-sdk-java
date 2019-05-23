package com.parabox.protocol.core.methods;

import com.parabox.protocol.Paraboxj;
import com.parabox.protocol.core.methods.response.*;

import com.alibaba.fastjson.JSONObject;
import com.cryptape.cita.protocol.CITAjService;
import com.cryptape.cita.protocol.core.JsonRpc2_0CITAj;
import com.cryptape.cita.protocol.core.Request;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ScheduledExecutorService;

public class JsonRpc2_0Paraboxj extends JsonRpc2_0CITAj implements Paraboxj {

    public JsonRpc2_0Paraboxj(CITAjService CITAjService) {
        super(CITAjService);
    }

    public JsonRpc2_0Paraboxj(com.cryptape.cita.protocol.CITAjService CITAjService, long pollingInterval) {
        super(CITAjService, pollingInterval);
    }

    public JsonRpc2_0Paraboxj(CITAjService CITAjService, long pollingInterval, ScheduledExecutorService scheduledExecutorService) {
        super(CITAjService, pollingInterval, scheduledExecutorService);
    }

    @Override
    public Request<?, ParaboxEstimateGas> estimateGas(String from, String to, String value, String data, String blockNumber) {

        JSONObject jsonObject = new JSONObject();

        if (from != null) {

            jsonObject.put("from", from);

        }

        if (to != null) {

            jsonObject.put("to", to);

        }

        if (value != null) {

            jsonObject.put("value", value);

        }

        if (data != null) {

            jsonObject.put("data", data);

        }

        return new Request<>(
                "estimateGas",
                (blockNumber == null)
                        ? Collections.singletonList(jsonObject)
                        : Arrays.asList(jsonObject, blockNumber),
                CITAjService,
                ParaboxEstimateGas.class);
    }

    @Override
    public Request<?, ParaboxGetGasPrice> getGasPrice() {
        return new Request<>(
                "getGasPrice",
                Collections.<String>emptyList(),
                CITAjService,
                ParaboxGetGasPrice.class);
    }

    @Override
    public Request<?, ParaboxSyncing> syncing() {
        return new Request<>(
                "syncing",
                Collections.<String>emptyList(),
                CITAjService,
                ParaboxSyncing.class);
    }

    @Override
    public Request<?, ParaboxGetTransactionReceiptEx> getTransactionReceiptEx(String transactionHash) {
        return new Request<>(
                "getTransactionReceiptEx",
                Collections.singletonList(transactionHash),
                CITAjService,
                ParaboxGetTransactionReceiptEx.class);
    }
}
