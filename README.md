Elastos.ORG.Wallet.Service
==============

[Elastos.ORG.Wallet.Service documentation](https://walletservice.readthedocs.io)

This repo provide simple HTTP Restful API for developers to interact with elastos blockchain . you may need to construct your own local node to use some of these API , never provide private key to any third party . 


## Quick Start

Run with `Maven`：

```xml
<dependency>
    <groupId>org.elastos</groupId>
    <artifactId>base.api</artifactId>
    <version>0.0.6</version>
</dependency>
```

or `Gradle`:

```sh
compile 'org.elastos:base.api:0.0.6'
```

Add A Entry Class
```java
@SpringBootApplication
public class MainEntry {

    public static void main(String[] args) {
        SpringApplication.run(org.elastos.Application.class, args);
    }
}
```

Add application.properties in src/main/resources
```
## chain restful url
## change it to your own mainchain node port
node.prefix              = http://localhost:20334
node.connectionCount     = /api/v1/node/connectioncount
node.state               = /api/v1/node/state
node.blockTxByHeight     = /api/v1/block/transactions/height/
node.blockByHeight       = /api/v1/block/details/height/
node.blockByhash         = /api/v1/block/details/hash/
node.blockHeight         = /api/v1/block/height
node.blockHash           = /api/v1/block/hash/
node.transaction         = /api/v1/transaction/
node.asset               = /api/v1/asset/
node.balanceByAddr       = /api/v1/asset/balances/
node.balanceByAsset      = /api/v1/asset/balance/
node.utxoByAsset         = /api/v1/asset/utxo/
node.utxoByAddr          = /api/v1/asset/utxos/
node.sendRawTransaction  = /api/v1/transaction
node.transactionPool     = /api/v1/transactionpool
node.restart             = /api/v1/restart

## api return status code
retcode.SUCC             = 200
retcode.BAD_REQUEST      = 400
retcode.NOT_FOUND        = 404
retcode.INTERNAL_ERROR   = 500
retcode.PROCESS_ERROR    = 10001

## basic
basic.ONE_ELA            = 100000000
basic.FEE                = 0.000001
basic.CROSS_CHAIN_FEE    = 0.0001

## application
server.port              = 8090

## log
logging.level.root       =INFO
logging.level.org.elastos=DEBUG


## Eth related config
eth.gasLimit             =6600000
eth.network              =http://localhost:8545
eth.infuraNetwork        =https://rinkeby.infura.io
eth.infuraToken          =1bbe5789437548f190f73cfa72fd6a57
eth.infuraEnable         =true

## DID related api
did.mainChainAddress     =XKUh4GLhFJiqAMTF6HyWQrV9pK9HcGUdfJ
did.burnAddress          =0000000000000000000000000000000000
```
