package com.cryptape.cita.protocol.core.methods.response;

import java.math.BigInteger;

import com.cryptape.cita.abi.datatypes.Address;
import com.cryptape.cita.protocol.core.Response;
import com.cryptape.cita.utils.Numeric;


public class AppMetaData extends Response<AppMetaData.AppMetaDataResult> {

    public AppMetaDataResult getAppMetaDataResult() {
        return getResult();
    }

    public boolean isEmpty() {
        return getResult() == null;
    }

    public static class AppMetaDataResult {
        private String chainId;
        private String chainName;
        private String operator;
        private String website;
        private String genesisTimestamp;
        private int blockInterval;
        private String tokenName;
        private String tokenSymbol;
        private String tokenAvatar;
        private Address[] validators;
        private int version;
        private int economicalModel;
        private String chainIdV1;

        public void setChainId(String chainId) {
            this.chainId = chainId;
        }

        public BigInteger getChainId() {
            if (this.version == 0) {
                return Numeric.toBigInt(chainId);
            } else if (this.version == 1 || this.version == 2) {
                return Numeric.toBigInt(this.chainIdV1);
            } else {
                throw new IllegalArgumentException("version number can only be 0 , 1 or 2");
            }
        }

        public void setChainName(String chainName) {
            this.chainName = chainName;
        }

        public String getChainName() {
            return this.chainName;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public String getOperator() {
            return this.operator;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getWebsite() {
            return this.website;
        }

        public void setGenesisTimestamp(String genesisTimestamp) {
            this.genesisTimestamp = genesisTimestamp;
        }

        public String getGenesisTimestamp() {
            return this.genesisTimestamp;
        }

        public void setBlockInterval(int blockInterval) {
            this.blockInterval = blockInterval;
        }

        public int getBlockInterval() {
            return this.blockInterval;
        }

        public void setTokenName(String tokenName) {
            this.tokenName = tokenName;
        }

        public String getTokenName() {
            return this.tokenName;
        }

        public void setTokenSymbol(String tokenSymbol) {
            this.tokenSymbol = tokenSymbol;
        }

        public String getTokenSymbol() {
            return this.tokenSymbol;
        }

        public void setTokenAvatar(String tokenAvatar) {
            this.tokenAvatar = tokenAvatar;
        }

        public String getTokenAvatar() {
            return this.tokenAvatar;
        }

        public void setValidators(Address[] validators) {
            this.validators = validators;
        }

        public Address[] getValidators() {
            return this.validators;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public int getVersion() {
            return this.version;
        }

        public void setEconomicalModel(int economicalModel) {
            this.economicalModel = economicalModel;
        }

        public int getEconomicalModel() {
            return this.economicalModel;
        }

        public void setChainIdV1(String chainIdV1) {
            this.chainIdV1 = chainIdV1;
        }

        public String getChainIdV1() {
            return this.chainIdV1;
        }
    }
}
