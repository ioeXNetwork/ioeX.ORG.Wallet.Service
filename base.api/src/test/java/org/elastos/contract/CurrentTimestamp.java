package org.elastos.contract;

import java.math.BigInteger;
import java.util.Arrays;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.5.0.
 */
public class CurrentTimestamp extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b50610168806100206000396000f30060806040526004361061004b5763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631468fe068114610050578063fb8f0f5914610077575b600080fd5b34801561005c57600080fd5b50610065610101565b60408051918252519081900360200190f35b34801561008357600080fd5b5061008c610105565b6040805160208082528351818301528351919283929083019185019080838360005b838110156100c65781810151838201526020016100ae565b50505050905090810190601f1680156100f35780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b4290565b60408051808201909152600481527f74657374000000000000000000000000000000000000000000000000000000006020820152905600a165627a7a72305820fd189d4c00e188930d45187b1ee876c7a6b7fb7e6cee8abc3841e75fc4e4c1560029";

    public static final String FUNC_GETCURRTIMESTAMP = "getCurrTimestamp";

    public static final String FUNC_TESTSTRING = "testString";

    protected CurrentTimestamp(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected CurrentTimestamp(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<BigInteger> getCurrTimestamp() {
        final Function function = new Function(FUNC_GETCURRTIMESTAMP, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> testString() {
        final Function function = new Function(FUNC_TESTSTRING, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public static RemoteCall<CurrentTimestamp> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(CurrentTimestamp.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<CurrentTimestamp> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(CurrentTimestamp.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static CurrentTimestamp load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new CurrentTimestamp(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static CurrentTimestamp load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new CurrentTimestamp(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
