package com.parabox.protocol.core.methods.response;

import com.cryptape.cita.protocol.core.Response;


public class ParaboxSyncing extends Response<Boolean> {
    public boolean getGasPrice() {
        return getResult();
    }
}
