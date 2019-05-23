package com.cryptape.cita.protocol.core.methods.request;

import java.util.Arrays;
import java.util.List;

import com.cryptape.cita.protocol.core.DefaultBlockParameter;

/**
 * Filter implementation as per
 * <a href="https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_newfilter">docs</a>.
 */
public class AppFilter extends Filter<AppFilter> {
    private DefaultBlockParameter fromBlock;  // optional, params - defaults to latest for both
    private DefaultBlockParameter toBlock;
    private List<String> address;  // spec. implies this can be single address as string or list

    public AppFilter() {
        super();
    }

    public AppFilter(DefaultBlockParameter fromBlock, DefaultBlockParameter toBlock,
                     List<String> address) {
        super();
        this.fromBlock = fromBlock;
        this.toBlock = toBlock;
        this.address = address;
    }

    public AppFilter(DefaultBlockParameter fromBlock, DefaultBlockParameter toBlock,
                     String address) {
        this(fromBlock, toBlock, Arrays.asList(address));
    }

    public DefaultBlockParameter getFromBlock() {
        return fromBlock;
    }

    public DefaultBlockParameter getToBlock() {
        return toBlock;
    }

    public List<String> getAddress() {
        return address;
    }

    @Override
    AppFilter getThis() {
        return this;
    }
}
