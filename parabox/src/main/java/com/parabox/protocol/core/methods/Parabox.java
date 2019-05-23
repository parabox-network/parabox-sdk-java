package com.parabox.protocol.core.methods;

import com.parabox.protocol.core.methods.response.*;
import com.cryptape.cita.protocol.core.Request;

/**
 * Core Parabox JSON-RPC API.
 */
public interface Parabox {

    Request<?, ParaboxEstimateGas> estimateGas(String from, String to, String value, String data, String blockNumber);

    Request<?, ParaboxGetGasPrice> getGasPrice();

    Request<?, ParaboxSyncing> syncing();

    Request<?, ParaboxGetTransactionReceiptEx> getTransactionReceiptEx(String transactionHash);

}
