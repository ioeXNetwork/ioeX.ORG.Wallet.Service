/**
 * Copyright (c) 2017-2018 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.elastos.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

/**
 * clark
 * <p>
 * 9/10/18
 */
@Component
@ConfigurationProperties("eth")
public class EthConfiguration {
    private String infuraNetwork;
    private String infuraToken;
    private BigInteger gasLimit;

    public BigInteger getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(BigInteger gasLimit) {
        this.gasLimit = gasLimit;
    }

    public String getInfuraNetwork() {
        return infuraNetwork;
    }

    public void setInfuraNetwork(String infuraNetwork) {
        this.infuraNetwork = infuraNetwork;
    }

    public String getInfuraToken() {
        return infuraToken;
    }

    public void setInfuraToken(String infuraToken) {
        this.infuraToken = infuraToken;
    }

    public String getInfuraAccessUrl(){
        return this.infuraNetwork + "/" + this.infuraToken;
    };
}
