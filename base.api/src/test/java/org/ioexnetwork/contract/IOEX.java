package org.ioexnetwork.contract;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.5.0.
 */
public class IOEX extends Contract {
    private static final String BINARY = "60606040526003805460ff19166012179055341561001c57600080fd5b604051610e7b380380610e7b833981016040528080519190602001805182019190602001805160008054600160a060020a033316600160a060020a03199091168117825560035460ff16600a0a870260048190559082526005602052604090912055909101905082828260018280516100999291602001906100b9565b5060028180516100ad9291602001906100b9565b50505050505050610154565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100fa57805160ff1916838001178555610127565b82800160010185558215610127579182015b8281111561012757825182559160200191906001019061010c565b50610133929150610137565b5090565b61015191905b80821115610133576000815560010161013d565b90565b610d18806101636000396000f3006060604052600436106101275763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166305fefda7811461012c57806306fdde0314610147578063095ea7b3146101d157806318160ddd1461020757806323b872dd1461022c578063313ce5671461025457806342966c681461027d5780634b7503341461029357806370a08231146102a657806379c65068146102c557806379cc6790146102e75780638620410b146103095780638da5cb5b1461031c57806395d89b411461034b578063a6f2ae3a1461035e578063a9059cbb14610366578063b414d4b614610388578063cae9ca51146103a7578063dd62ed3e1461040c578063e4849b3214610431578063e724529c14610447578063f2fde38b1461046b575b600080fd5b341561013757600080fd5b61014560043560243561048a565b005b341561015257600080fd5b61015a6104b0565b60405160208082528190810183818151815260200191508051906020019080838360005b8381101561019657808201518382015260200161017e565b50505050905090810190601f1680156101c35780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34156101dc57600080fd5b6101f3600160a060020a036004351660243561054e565b604051901515815260200160405180910390f35b341561021257600080fd5b61021a6105ba565b60405190815260200160405180910390f35b341561023757600080fd5b6101f3600160a060020a03600435811690602435166044356105c0565b341561025f57600080fd5b610267610637565b60405160ff909116815260200160405180910390f35b341561028857600080fd5b6101f3600435610640565b341561029e57600080fd5b61021a6106cb565b34156102b157600080fd5b61021a600160a060020a03600435166106d1565b34156102d057600080fd5b610145600160a060020a03600435166024356106e3565b34156102f257600080fd5b6101f3600160a060020a03600435166024356107a9565b341561031457600080fd5b61021a610885565b341561032757600080fd5b61032f61088b565b604051600160a060020a03909116815260200160405180910390f35b341561035657600080fd5b61015a61089a565b610145610905565b341561037157600080fd5b6101f3600160a060020a0360043516602435610925565b341561039357600080fd5b6101f3600160a060020a036004351661093b565b34156103b257600080fd5b6101f360048035600160a060020a03169060248035919060649060443590810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284375094965061095095505050505050565b341561041757600080fd5b61021a600160a060020a0360043581169060243516610a7e565b341561043c57600080fd5b610145600435610a9b565b341561045257600080fd5b610145600160a060020a03600435166024351515610afe565b341561047657600080fd5b610145600160a060020a0360043516610b8a565b60005433600160a060020a039081169116146104a557600080fd5b600791909155600855565b60018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105465780601f1061051b57610100808354040283529160200191610546565b820191906000526020600020905b81548152906001019060200180831161052957829003601f168201915b505050505081565b600160a060020a03338116600081815260066020908152604080832094871680845294909152808220859055909291907f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b9259085905190815260200160405180910390a350600192915050565b60045481565b600160a060020a038084166000908152600660209081526040808320339094168352929052908120548211156105f557600080fd5b600160a060020a038085166000908152600660209081526040808320339094168352929052208054839003905561062d848484610bd4565b5060019392505050565b60035460ff1681565b600160a060020a0333166000908152600560205260408120548290101561066657600080fd5b600160a060020a03331660008181526005602052604090819020805485900390556004805485900390557fcc16f5dbb4873280815c1ee09dbd06736cffcc184412cf7a71a0fdb75d397ca59084905190815260200160405180910390a2506001919050565b60075481565b60056020526000908152604090205481565b60005433600160a060020a039081169116146106fe57600080fd5b600160a060020a03808316600090815260056020526040808220805485019055600480548501905530909216917fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9084905190815260200160405180910390a381600160a060020a031630600160a060020a03167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef8360405190815260200160405180910390a35050565b600160a060020a038216600090815260056020526040812054829010156107cf57600080fd5b600160a060020a038084166000908152600660209081526040808320339094168352929052205482111561080257600080fd5b600160a060020a038084166000818152600560209081526040808320805488900390556006825280832033909516835293905282902080548590039055600480548590039055907fcc16f5dbb4873280815c1ee09dbd06736cffcc184412cf7a71a0fdb75d397ca59084905190815260200160405180910390a250600192915050565b60085481565b600054600160a060020a031681565b60028054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105465780601f1061051b57610100808354040283529160200191610546565b60006008543481151561091457fe5b049050610922303383610bd4565b50565b6000610932338484610bd4565b50600192915050565b60096020526000908152604090205460ff1681565b60008361095d818561054e565b15610a765780600160a060020a0316638f4ffcb1338630876040518563ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018085600160a060020a0316600160a060020a0316815260200184815260200183600160a060020a0316600160a060020a0316815260200180602001828103825283818151815260200191508051906020019080838360005b83811015610a135780820151838201526020016109fb565b50505050905090810190601f168015610a405780820380516001836020036101000a031916815260200191505b5095505050505050600060405180830381600087803b1515610a6157600080fd5b5af11515610a6e57600080fd5b505050600191505b509392505050565b600660209081526000928352604080842090915290825290205481565b60075430908202600160a060020a038216311015610ab857600080fd5b610ac3333084610bd4565b33600160a060020a03166108fc60075484029081150290604051600060405180830381858888f193505050501515610afa57600080fd5b5050565b60005433600160a060020a03908116911614610b1957600080fd5b600160a060020a03821660009081526009602052604090819020805460ff19168315151790557f48335238b4855f35377ed80f164e8c6f3c366e54ac00b96a6402d4a9814a03a5908390839051600160a060020a039092168252151560208201526040908101905180910390a15050565b60005433600160a060020a03908116911614610ba557600080fd5b6000805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0392909216919091179055565b600160a060020a0382161515610be957600080fd5b600160a060020a03831660009081526005602052604090205481901015610c0f57600080fd5b600160a060020a0382166000908152600560205260409020548181011015610c3657600080fd5b600160a060020a03831660009081526009602052604090205460ff1615610c5c57600080fd5b600160a060020a03821660009081526009602052604090205460ff1615610c8257600080fd5b600160a060020a038084166000818152600560205260408082208054869003905592851680825290839020805485019055917fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9084905190815260200160405180910390a35050505600a165627a7a723058201179cf464945f48e75272d8506a90e726021267029a07120a487f69b49d5fa420029";

    public static final String FUNC_SETPRICES = "setPrices";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_DECIMALS = "decimals";

    public static final String FUNC_BURN = "burn";

    public static final String FUNC_SELLPRICE = "sellPrice";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_MINTTOKEN = "mintToken";

    public static final String FUNC_BURNFROM = "burnFrom";

    public static final String FUNC_BUYPRICE = "buyPrice";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_BUY = "buy";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_FROZENACCOUNT = "frozenAccount";

    public static final String FUNC_APPROVEANDCALL = "approveAndCall";

    public static final String FUNC_ALLOWANCE = "allowance";

    public static final String FUNC_SELL = "sell";

    public static final String FUNC_FREEZEACCOUNT = "freezeAccount";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final Event FROZENFUNDS_EVENT = new Event("FrozenFunds", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Bool>() {}));
    ;

    public static final Event TRANSFER_EVENT = new Event("Transfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event APPROVAL_EVENT = new Event("Approval", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event BURN_EVENT = new Event("Burn", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    protected IOEX(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected IOEX(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<TransactionReceipt> setPrices(BigInteger newSellPrice, BigInteger newBuyPrice) {
        final Function function = new Function(
                FUNC_SETPRICES, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(newSellPrice), 
                new org.web3j.abi.datatypes.generated.Uint256(newBuyPrice)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> name() {
        final Function function = new Function(FUNC_NAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> approve(String _spender, BigInteger _value) {
        final Function function = new Function(
                FUNC_APPROVE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_spender), 
                new org.web3j.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> totalSupply() {
        final Function function = new Function(FUNC_TOTALSUPPLY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> transferFrom(String _from, String _to, BigInteger _value) {
        final Function function = new Function(
                FUNC_TRANSFERFROM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_from), 
                new org.web3j.abi.datatypes.Address(_to), 
                new org.web3j.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> decimals() {
        final Function function = new Function(FUNC_DECIMALS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> burn(BigInteger _value) {
        final Function function = new Function(
                FUNC_BURN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> sellPrice() {
        final Function function = new Function(FUNC_SELLPRICE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> balanceOf(String param0) {
        final Function function = new Function(FUNC_BALANCEOF, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> mintToken(String target, BigInteger mintedAmount) {
        final Function function = new Function(
                FUNC_MINTTOKEN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(target), 
                new org.web3j.abi.datatypes.generated.Uint256(mintedAmount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> burnFrom(String _from, BigInteger _value) {
        final Function function = new Function(
                FUNC_BURNFROM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_from), 
                new org.web3j.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> buyPrice() {
        final Function function = new Function(FUNC_BUYPRICE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> symbol() {
        final Function function = new Function(FUNC_SYMBOL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> buy(BigInteger weiValue) {
        final Function function = new Function(
                FUNC_BUY, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<TransactionReceipt> transfer(String _to, BigInteger _value) {
        final Function function = new Function(
                FUNC_TRANSFER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_to), 
                new org.web3j.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> frozenAccount(String param0) {
        final Function function = new Function(FUNC_FROZENACCOUNT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> approveAndCall(String _spender, BigInteger _value, byte[] _extraData) {
        final Function function = new Function(
                FUNC_APPROVEANDCALL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_spender), 
                new org.web3j.abi.datatypes.generated.Uint256(_value), 
                new org.web3j.abi.datatypes.DynamicBytes(_extraData)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> allowance(String param0, String param1) {
        final Function function = new Function(FUNC_ALLOWANCE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0), 
                new org.web3j.abi.datatypes.Address(param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> sell(BigInteger amount) {
        final Function function = new Function(
                FUNC_SELL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> freezeAccount(String target, Boolean freeze) {
        final Function function = new Function(
                FUNC_FREEZEACCOUNT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(target), 
                new org.web3j.abi.datatypes.Bool(freeze)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> transferOwnership(String newOwner) {
        final Function function = new Function(
                FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(newOwner)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<IOEX> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialSupply, String tokenName, String tokenSymbol) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(initialSupply), 
                new org.web3j.abi.datatypes.Utf8String(tokenName), 
                new org.web3j.abi.datatypes.Utf8String(tokenSymbol)));
        return deployRemoteCall(IOEX.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<IOEX> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialSupply, String tokenName, String tokenSymbol) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(initialSupply), 
                new org.web3j.abi.datatypes.Utf8String(tokenName), 
                new org.web3j.abi.datatypes.Utf8String(tokenSymbol)));
        return deployRemoteCall(IOEX.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public List<FrozenFundsEventResponse> getFrozenFundsEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(FROZENFUNDS_EVENT, transactionReceipt);
        ArrayList<FrozenFundsEventResponse> responses = new ArrayList<FrozenFundsEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            FrozenFundsEventResponse typedResponse = new FrozenFundsEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.target = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.frozen = (Boolean) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<FrozenFundsEventResponse> frozenFundsEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, FrozenFundsEventResponse>() {
            @Override
            public FrozenFundsEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(FROZENFUNDS_EVENT, log);
                FrozenFundsEventResponse typedResponse = new FrozenFundsEventResponse();
                typedResponse.log = log;
                typedResponse.target = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.frozen = (Boolean) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<FrozenFundsEventResponse> frozenFundsEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(FROZENFUNDS_EVENT));
        return frozenFundsEventObservable(filter);
    }

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TransferEventResponse> transferEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFER_EVENT, log);
                TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse.log = log;
                typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<TransferEventResponse> transferEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return transferEventObservable(filter);
    }

    public List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt);
        ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._spender = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ApprovalEventResponse> approvalEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, ApprovalEventResponse>() {
            @Override
            public ApprovalEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(APPROVAL_EVENT, log);
                ApprovalEventResponse typedResponse = new ApprovalEventResponse();
                typedResponse.log = log;
                typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._spender = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<ApprovalEventResponse> approvalEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT));
        return approvalEventObservable(filter);
    }

    public List<BurnEventResponse> getBurnEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(BURN_EVENT, transactionReceipt);
        ArrayList<BurnEventResponse> responses = new ArrayList<BurnEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            BurnEventResponse typedResponse = new BurnEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<BurnEventResponse> burnEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, BurnEventResponse>() {
            @Override
            public BurnEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(BURN_EVENT, log);
                BurnEventResponse typedResponse = new BurnEventResponse();
                typedResponse.log = log;
                typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<BurnEventResponse> burnEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(BURN_EVENT));
        return burnEventObservable(filter);
    }

    public static IOEX load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new IOEX(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static IOEX load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new IOEX(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class FrozenFundsEventResponse {
        public Log log;

        public String target;

        public Boolean frozen;
    }

    public static class TransferEventResponse {
        public Log log;

        public String from;

        public String to;

        public BigInteger value;
    }

    public static class ApprovalEventResponse {
        public Log log;

        public String _owner;

        public String _spender;

        public BigInteger _value;
    }

    public static class BurnEventResponse {
        public Log log;

        public String from;

        public BigInteger value;
    }
}
