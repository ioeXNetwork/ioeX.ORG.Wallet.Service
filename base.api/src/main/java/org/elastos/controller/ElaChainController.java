/**
 * Copyright (c) 2017-2018 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.elastos.controller;

import org.elastos.service.ElaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * clark
 * <p>
 * 9/20/18
 */
@RestController
public class ElaChainController extends BaseController{

    @Autowired
    private ElaService service;

    @RequestMapping(value = "/currHeight",method = RequestMethod.GET)
    @ResponseBody
    public String getCurrentHeight(){

        return call(null,null,"getCurrentHeight",service);
    }

    @RequestMapping(value = "/txs/{height}",method = RequestMethod.GET)
    @ResponseBody
    public String getBlockTxsByHeight(@PathVariable("height") String height){

        return call( height ,String.class,"getBlockTxsByHeight",service);
    }

    @RequestMapping(value = "/block/height/{height}",method = RequestMethod.GET)
    @ResponseBody
    public String getBlockByHeight(@PathVariable("height") String height){

        return call( height ,String.class,"getBlockByHeight",service);
    }

    @RequestMapping(value = "/block/hash/{hash}",method = RequestMethod.GET)
    @ResponseBody
    public String getBlockByHash(@PathVariable("hash") String hash){

        return call( hash ,String.class,"getBlockByHash",service);
    }

    @RequestMapping(value = "/block/transaction/{hash}",method = RequestMethod.GET)
    @ResponseBody
    public String getTransactionByHash(@PathVariable("hash") String hash){

        return call( hash ,String.class,"getTransactionByHash",service);
    }

    @RequestMapping(value = "/balance/{address}",method = RequestMethod.GET)
    @ResponseBody
    public String getBalance(@PathVariable("address") String address){

        return call( address ,String.class,"getBalance",service);
    }

    @RequestMapping(value = "/utxos/{address}",method = RequestMethod.GET)
    @ResponseBody
    public String getUtxos(@PathVariable("address") String address){

        return call( address ,String.class,"getUtxos",service);
    }

}
