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
@ConfigurationProperties("did")
public class DidConfiguration {
    private String address;
    private String fee;
    private String mainChainAddress;
    private String burnAddress;

    public String getMainChainAddress() {
        return mainChainAddress;
    }

    public void setMainChainAddress(String mainChainAddress) {
        this.mainChainAddress = mainChainAddress;
    }

    public String getBurnAddress() {
        return burnAddress;
    }

    public void setBurnAddress(String burnAddress) {
        this.burnAddress = burnAddress;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
