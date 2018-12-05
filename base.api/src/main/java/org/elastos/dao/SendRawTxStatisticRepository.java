/**
 * Copyright (c) 2017-2018 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.elastos.dao;

import org.elastos.entity.SendRawTxStatistic;
import org.springframework.data.repository.CrudRepository;

/**
 * clark
 * <p>
 * 12/5/18
 */
public interface SendRawTxStatisticRepository extends CrudRepository<SendRawTxStatistic, Integer> {

}
