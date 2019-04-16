/**
 * Copyright (c) 2017-2018 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.ioexnetwork.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties("basic")
public class BasicConfiguration {
    private long ONE_IOEX;
    private double FEE;
    private double CROSS_CHAIN_FEE;
    private boolean CROSS_DOMAIN;

    public boolean isCROSS_DOMAIN() {
        return CROSS_DOMAIN;
    }

    public void setCROSS_DOMAIN(boolean CROSS_DOMAIN) {
        this.CROSS_DOMAIN = CROSS_DOMAIN;
    }
    public double CROSS_CHAIN_FEE(){
        return CROSS_CHAIN_FEE;
    }

    public void setCrossChain_FEE(double CROSS_CHAIN_FEE) {
        this.CROSS_CHAIN_FEE = CROSS_CHAIN_FEE;
    }

    public long ONE_IOEX() {
        return ONE_IOEX;

    }

    public void setONE_IOEX(long ONE_IOEX) {
        this.ONE_IOEX = ONE_IOEX;
    }

    public double FEE() {
        return FEE;
    }

    public void setFEE(double FEE) {
        this.FEE = FEE;
    }
}
