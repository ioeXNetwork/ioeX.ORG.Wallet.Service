/**
 * Copyright (c) 2017-2018 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.elastos.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * clark
 * <p>
 * 12/5/18
 */
@Entity
@Table(name = "ws_send_raw_tx_statistic")
public class SendRawTxStatistic {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(name = "user_agent")
    private String userAgent;

    private Date localSystemTime = new Date();

    private String address;

    public Date getLocalSystemTime() {
        return localSystemTime;
    }

    public void setLocalSystemTime(Date localSystemTime) {
        this.localSystemTime = localSystemTime;
    }

    @Column(columnDefinition="MEDIUMTEXT")
    private String raw;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }
}
