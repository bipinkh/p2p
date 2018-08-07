package com.soriole.dfsnode.blockchain.wrapper;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple5;
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
public class DecentralizedDB extends Contract {
    private static final String BINARY = "60806040908152620186a060008181556001805560028054600160a060020a03191633908117909155815260066020529190912055610f6d806100436000396000f3006080604052600436106100fb5763ffffffff7c0100000000000000000000000000000000000000000000000000000000600035041663095ea7b381146100fd57806318160ddd14610135578063200388161461015c57806323b872dd14610174578063285a92851461019e5780632ebcb653146101cb5780634a299305146101e05780634b94f50e146101fb57806370a08231146102105780637519d63d14610231578063893d20e8146102495780639bf4c0eb1461027a578063a482171914610297578063a9059cbb1461029f578063dd62ed3e146102c3578063e178bf8f146102ea578063e1d8173614610339578063efd63b6114610351575b005b34801561010957600080fd5b50610121600160a060020a0360043516602435610382565b604080519115158252519081900360200190f35b34801561014157600080fd5b5061014a6103af565b60408051918252519081900360200190f35b34801561016857600080fd5b506100fb6004356103b5565b34801561018057600080fd5b50610121600160a060020a03600435811690602435166044356103cb565b3480156101aa57600080fd5b506100fb600435600160a060020a03602435166044356064356084356104fd565b3480156101d757600080fd5b5061014a6106f4565b3480156101ec57600080fd5b506100fb600435602435610707565b34801561020757600080fd5b5061014a61076d565b34801561021c57600080fd5b5061014a600160a060020a0360043516610773565b34801561023d57600080fd5b506100fb60043561078e565b34801561025557600080fd5b5061025e6107a0565b60408051600160a060020a039092168252519081900360200190f35b34801561028657600080fd5b506100fb60043560243515156107af565b61014a610902565b3480156102ab57600080fd5b50610121600160a060020a036004351660243561092b565b3480156102cf57600080fd5b5061014a600160a060020a03600435811690602435166109c9565b3480156102f657600080fd5b506103026004356109f4565b60408051600160a060020a039096168652602086019490945291151584840152151560608401526080830152519081900360a00190f35b34801561034557600080fd5b506100fb600435610abc565b34801561035d57600080fd5b50610369600435610ace565b6040805192835260208301919091528051918290030190f35b336000908152600560209081526040808320600160a060020a039590951683529390529190912055600190565b60005490565b6103c76103c06107a0565b3383610b25565b5050565b600160a060020a038316600090815260056020908152604080832033845290915281205482118015906104165750600160a060020a0384166000908152600660205260409020548211155b80156104225750600082115b151561042d57600080fd5b600160a060020a038416600090815260066020526040902054610456908363ffffffff610bd916565b600160a060020a03808616600090815260066020526040808220939093559085168152205461048b908363ffffffff610beb16565b600160a060020a0380851660009081526006602090815260408083209490945591871681526005825282812033825290915220546104cf908363ffffffff610bd916565b600160a060020a03851660009081526005602090815260408083203384529091529020555060019392505050565b600085600a6000866000191660001916815260200190815260200160002081600019169055506009600087600019166000191681526020019081526020016000206101206040519081016040528087600160a060020a0316815260200133600160a060020a031681526020018660001916815260200185815260200160001515815260200160001515815260200184815260200160008152602001428152509080600181540180825580915050906001820390600052602060002090600802016000909192909190915060008201518160000160006101000a815481600160a060020a030219169083600160a060020a0316021790555060208201518160010160006101000a815481600160a060020a030219169083600160a060020a03160217905550604082015181600201906000191690556060820151816003015560808201518160040160006101000a81548160ff02191690831515021790555060a08201518160040160016101000a81548160ff02191690831515021790555060c0820151816005015560e08201518160060155610100820151816007015550505060016009600088600019166000191681526020019081526020016000208054905003905080600b60008660001916600019168152602001908152602001600020819055506106ec338685610c01565b505050505050565b3360009081526007602052604090205490565b61070f610ebe565b5060408051808201825283815260208082019384523360009081526008825283812080546001818101835582845284842095516002909202909501908155955195909301949094559054938352600c90529020600019919091019055565b60015490565b600160a060020a031660009081526006602052604090205490565b6103c73361079a6107a0565b83610b25565b600254600160a060020a031690565b6000806107ba610ed5565b6107c2610f21565b6000868152600a6020908152604080832054600b83528184205481855260099093529220805492965090945090849081106107f957fe5b6000918252602091829020604080516101208101825260089093029091018054600160a060020a03908116845260018201541693830184905260028101549183019190915260038101546060830152600481015460ff808216151560808501526101009182900416151560a0840152600582015460c0840152600682015460e0840152600790910154908201529250331461089357600080fd5b50604080516060818101835286151582526000602080840182815242858701908152888452600d83529590922084518154935115156101000261ff001991151560ff1990951694909417169290921782559351600191909101559183015183519284015191926106ec92610c1f565b60008061090d61076d565b3481151561091757fe5b0490506109256103c06107a0565b50919050565b33600090815260066020526040812054821180159061094a5750600082115b151561095557600080fd5b33600090815260066020526040902054610975908363ffffffff610bd916565b3360009081526006602052604080822092909255600160a060020a038516815220546109a7908363ffffffff610beb16565b600160a060020a03841660009081526006602052604090205550600192915050565b600160a060020a03918216600090815260056020908152604080832093909416825291909152205490565b6000818152600b6020908152604080832054600a83528184205480855260099093529083208054849384938493849391929184918291829182918291829189908110610a3c57fe5b906000526020600020906008020195508560000160009054906101000a9004600160a060020a03169450856002015493508560040160009054906101000a900460ff1692508560040160019054906101000a900460ff1691508560070154905084848484849c509c509c509c509c50505050505050505091939590929450565b33600090815260076020526040902055565b6000818152600c602090815260408083205433845260089092528220805483929183918291829185908110610aff57fe5b600091825260209091206002909102018054600190910154909890975095505050505050565b600160a060020a0383166000908152600660205260408120548211801590610b4d5750600082115b1515610b5857600080fd5b600160a060020a038416600090815260066020526040902054610b81908363ffffffff610bd916565b600160a060020a038086166000908152600660205260408082209390935590851681522054610bb6908363ffffffff610beb16565b600160a060020a0384166000908152600660205260409020555060019392505050565b600082821115610be557fe5b50900390565b600082820183811015610bfa57fe5b9392505050565b610c0c838383610c38565b610c1a828483600202610c38565b505050565b610c2a838383610d07565b610c1a828483600202610de1565b600160a060020a0383166000908152600660205260409020548111801590610c605750600081115b1515610c6b57600080fd5b600160a060020a038316600090815260066020526040902054610c94908263ffffffff610bd916565b600160a060020a03808516600090815260066020908152604080832094909455600481528382209286168252919091522054610cd6908263ffffffff610beb16565b600160a060020a03938416600090815260046020908152604080832095909616825293909352929091209190915550565b600160a060020a038084166000908152600460209081526040808320938616835292905220548111801590610d3c5750600081115b1515610d4757600080fd5b600160a060020a03808416600090815260046020908152604080832093861683529290522054610d7d908263ffffffff610bd916565b600160a060020a0380851660009081526004602090815260408083209387168352928152828220939093556006909252902054610dc0908263ffffffff610beb16565b600160a060020a039092166000908152600660205260409020919091555050565b600160a060020a038084166000908152600460209081526040808320938616835292905220548111801590610e165750600081115b1515610e2157600080fd5b600160a060020a03808416600090815260046020908152604080832093861683529290522054610e57908263ffffffff610bd916565b600160a060020a0380851660008181526004602090815260408083209488168352938152838220949094559081526006909252902054610e9d908263ffffffff610beb16565b600160a060020a039093166000908152600660205260409020929092555050565b604080518082019091526000808252602082015290565b6040805161012081018252600080825260208201819052918101829052606081018290526080810182905260a0810182905260c0810182905260e0810182905261010081019190915290565b6040805160608101825260008082526020820181905291810191909152905600a165627a7a7230582087638ac24ec2f905ef2d9f4165db2427ea6128dbfcde503c18406953529f03110029";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_INCREASETOKEN = "increaseToken";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_SETCHUNKDATAOFMAINFILE = "setChunkDataOfMainFile";

    public static final String FUNC_GETUSERID = "getUserId";

    public static final String FUNC_SETFILEDETAIL = "setFileDetail";

    public static final String FUNC_GETTOKENPRICE = "getTokenPrice";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_DECREASETOKEN = "decreaseToken";

    public static final String FUNC_GETOWNER = "getOwner";

    public static final String FUNC_SETCLIENTDOWNLOADFILE = "setClientDownLoadFile";

    public static final String FUNC_BUYTOKEN = "buyToken";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_ALLOWANCE = "allowance";

    public static final String FUNC_GETCHUNCKDATAOFMAINFILE = "getChunckDataOfMainFile";

    public static final String FUNC_ADDUSER = "addUser";

    public static final String FUNC_GETFILEDETAIL = "getFileDetail";


    protected DecentralizedDB(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected DecentralizedDB(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
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

    public RemoteCall<TransactionReceipt> increaseToken(BigInteger tokens) {
        final Function function = new Function(
                FUNC_INCREASETOKEN,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(tokens)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
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

    public RemoteCall<TransactionReceipt> setChunkDataOfMainFile(byte[] mainFileHash, String nodeAddress, byte[] chunkHash, BigInteger reqdTokens, BigInteger totalbandWidth) {
        final Function function = new Function(
                FUNC_SETCHUNKDATAOFMAINFILE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(mainFileHash),
                        new org.web3j.abi.datatypes.Address(nodeAddress),
                        new org.web3j.abi.datatypes.generated.Bytes32(chunkHash),
                        new org.web3j.abi.datatypes.generated.Uint256(reqdTokens),
                        new org.web3j.abi.datatypes.generated.Uint256(totalbandWidth)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> getUserId() {
        final Function function = new Function(FUNC_GETUSERID,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> setFileDetail(byte[] fileHash, byte[] fileName) {
        final Function function = new Function(
                FUNC_SETFILEDETAIL,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(fileHash),
                        new org.web3j.abi.datatypes.generated.Bytes32(fileName)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> getTokenPrice() {
        final Function function = new Function(FUNC_GETTOKENPRICE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> balanceOf(String _owner) {
        final Function function = new Function(FUNC_BALANCEOF,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> decreaseToken(BigInteger tokens) {
        final Function function = new Function(
                FUNC_DECREASETOKEN,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(tokens)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> getOwner() {
        final Function function = new Function(FUNC_GETOWNER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> setClientDownLoadFile(byte[] chunkHash, Boolean verify) {
        final Function function = new Function(
                FUNC_SETCLIENTDOWNLOADFILE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(chunkHash),
                        new org.web3j.abi.datatypes.Bool(verify)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> buyToken(BigInteger weiValue) {
        final Function function = new Function(
                FUNC_BUYTOKEN,
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

    public RemoteCall<BigInteger> allowance(String _owner, String _spender) {
        final Function function = new Function(FUNC_ALLOWANCE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner),
                        new org.web3j.abi.datatypes.Address(_spender)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Tuple5<String, byte[], Boolean, Boolean, BigInteger>> getChunckDataOfMainFile(byte[] chunkHash) {
        final Function function = new Function(FUNC_GETCHUNCKDATAOFMAINFILE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(chunkHash)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bool>() {}, new TypeReference<Bool>() {}, new TypeReference<Uint256>() {}));
        return new RemoteCall<Tuple5<String, byte[], Boolean, Boolean, BigInteger>>(
                new Callable<Tuple5<String, byte[], Boolean, Boolean, BigInteger>>() {
                    @Override
                    public Tuple5<String, byte[], Boolean, Boolean, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<String, byte[], Boolean, Boolean, BigInteger>(
                                (String) results.get(0).getValue(),
                                (byte[]) results.get(1).getValue(),
                                (Boolean) results.get(2).getValue(),
                                (Boolean) results.get(3).getValue(),
                                (BigInteger) results.get(4).getValue());
                    }
                });
    }

    public RemoteCall<TransactionReceipt> addUser(BigInteger index) {
        final Function function = new Function(
                FUNC_ADDUSER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(index)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Tuple2<byte[], byte[]>> getFileDetail(byte[] _fileHash) {
        final Function function = new Function(FUNC_GETFILEDETAIL,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_fileHash)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}));
        return new RemoteCall<Tuple2<byte[], byte[]>>(
                new Callable<Tuple2<byte[], byte[]>>() {
                    @Override
                    public Tuple2<byte[], byte[]> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<byte[], byte[]>(
                                (byte[]) results.get(0).getValue(),
                                (byte[]) results.get(1).getValue());
                    }
                });
    }



    public static RemoteCall<DecentralizedDB> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(DecentralizedDB.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<DecentralizedDB> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(DecentralizedDB.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static DecentralizedDB load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new DecentralizedDB(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static DecentralizedDB load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new DecentralizedDB(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class TransferEventResponse {
        public Log log;

        public String _from;

        public String _to;

        public BigInteger _value;
    }

    public static class ApprovalEventResponse {
        public Log log;

        public String _owner;

        public String _spender;

        public BigInteger _value;
    }
}