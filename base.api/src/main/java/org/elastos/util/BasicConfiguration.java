/**
 * Copyright (c) 2017-2018 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.elastos.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * clark
 *
 * 9/3/18
 *
 */
@Component
@ConfigurationProperties("basic")
public class BasicConfiguration {
    private long ONE_ELA;
    private double FEE;

    public long ONE_ELA() {
        return ONE_ELA;
    }

    public void setONE_ELA(long ONE_ELA) {
        this.ONE_ELA = ONE_ELA;
    }

    public double FEE() {
        return FEE;
    }

    public void setFEE(double FEE) {
        this.FEE = FEE;
    }
}
