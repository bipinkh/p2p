package com.soriole.blockchain.wrapper;

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
    private static final String BINARY = "60806040908152620186a060008181556001805560028054600160a060020a031916339081179091558152600660205291909120556114d9806100436000396000f30060806040526004361061011c5763ffffffff7c0100000000000000000000000000000000000000000000000000000000600035041663095ea7b3811461011e57806318160ddd1461015657806323b872dd1461017d578063285a9285146101a75780632ebcb653146101d45780634a299305146101e95780634a5b5f96146102045780634b94f50e146102215780635aee312e1461023657806370a08231146102565780638495269214610277578063893d20e8146102945780639bf4c0eb146102c5578063a4821719146102e2578063a9059cbb146102ea578063cb51a8d31461030e578063dd62ed3e14610332578063e178bf8f14610359578063e1d81736146103a8578063ecdb12c9146103c0578063efd63b61146103dd575b005b34801561012a57600080fd5b50610142600160a060020a036004351660243561040e565b604080519115158252519081900360200190f35b34801561016257600080fd5b5061016b61043b565b60408051918252519081900360200190f35b34801561018957600080fd5b50610142600160a060020a0360043581169060243516604435610441565b3480156101b357600080fd5b5061011c600435600160a060020a0360243516604435606435608435610573565b3480156101e057600080fd5b5061016b61075f565b3480156101f557600080fd5b5061011c600435602435610772565b34801561021057600080fd5b5061011c60043560243515156107d8565b34801561022d57600080fd5b5061016b6108f8565b34801561024257600080fd5b5061011c60043560243560443515156108fe565b34801561026257600080fd5b5061016b600160a060020a03600435166109d0565b34801561028357600080fd5b5061011c60043560243515156109eb565b3480156102a057600080fd5b506102a9610a67565b60408051600160a060020a039092168252519081900360200190f35b3480156102d157600080fd5b5061011c6004356024351515610a76565b61016b610bb7565b3480156102f657600080fd5b50610142600160a060020a0360043516602435610be7565b34801561031a57600080fd5b50610142600160a060020a0360043516602435610c85565b34801561033e57600080fd5b5061016b600160a060020a0360043581169060243516610e12565b34801561036557600080fd5b50610371600435610e3d565b60408051600160a060020a039096168652602086019490945291151584840152151560608401526080830152519081900360a00190f35b3480156103b457600080fd5b5061011c600435610f05565b3480156103cc57600080fd5b5061011c6004356024351515610f17565b3480156103e957600080fd5b506103f5600435611025565b6040805192835260208301919091528051918290030190f35b336000908152600560209081526040808320600160a060020a039590951683529390529190912055600190565b60005490565b600160a060020a0383166000908152600560209081526040808320338452909152812054821180159061048c5750600160a060020a0384166000908152600660205260409020548211155b80156104985750600082115b15156104a357600080fd5b600160a060020a0384166000908152600660205260409020546104cc908363ffffffff61107c16565b600160a060020a038086166000908152600660205260408082209390935590851681522054610501908363ffffffff61108e16565b600160a060020a038085166000908152600660209081526040808320949094559187168152600582528281203382529091522054610545908363ffffffff61107c16565b600160a060020a03851660009081526005602090815260408083203384529091529020555060019392505050565b600085600a6000866000191660001916815260200190815260200160002081600019169055506009600087600019166000191681526020019081526020016000206101206040519081016040528087600160a060020a0316815260200133600160a060020a031681526020018660001916815260200185815260200160001515815260200160001515815260200184815260200160008152602001428152509080600181540180825580915050906001820390600052602060002090600802016000909192909190915060008201518160000160006101000a815481600160a060020a030219169083600160a060020a0316021790555060208201518160010160006101000a815481600160a060020a030219169083600160a060020a03160217905550604082015181600201906000191690556060820151816003015560808201518160040160006101000a81548160ff02191690831515021790555060a08201518160040160016101000a81548160ff02191690831515021790555060c0820151816005015560e08201518160060155610100820151816007015550505060016009600088600019166000191681526020019081526020016000208054905003905080600b6000866000191660001916815260200190815260200160002081905550505050505050565b3360009081526007602052604090205490565b61077a61142a565b5060408051808201825283815260208082019384523360009081526008825283812080546001818101835582845284842095516002909202909501908155955195909301949094559054938352600c90529020600019919091019055565b60008060006107e5611441565b6000868152600a6020908152604080832054600b835281842054808552600d84528285208286526009909452919093208054939750909550909350908490811061082b57fe5b6000918252602091829020604080516101208101825260089093029091018054600160a060020a03908116845260018201541693830184905260028101549183019190915260038101546060830152600481015460ff808216151560808501526101009182900416151560a0840152600582015460c0840152600682015460e084015260079091015490820152915033146108c557600080fd5b815460ff1916851580159190911783556108f0576108f08160200151826000015183606001516110a4565b505050505050565b60015490565b6000828152600b60209081526040808320548684526009909252822080549192918291908490811061092c57fe5b60009182526020808320898452600c825260408085205433865260089384905294208054939092020194509192508290811061096457fe5b6000918252602090912060029091020154861461098057600080fd5b6001820154600160a060020a0316331461099957600080fd5b60048201805460ff191685158015919091179091556108f057815460038301546108f0913391600160a060020a03909116906110c2565b600160a060020a031660009081526006602052604090205490565b6000828152600a6020908152604080832054600b8352818420548185526009909352908320805491939183908110610a1f57fe5b600091825260209091206008909102018054909150600160a060020a03163314610a4857600080fd5b60040180549315156101000261ff001990941693909317909255505050565b600254600160a060020a031690565b600080610a81611441565b610a8961148d565b6000868152600a6020908152604080832054600b8352818420548185526009909352922080549296509094509084908110610ac057fe5b6000918252602091829020604080516101208101825260089093029091018054600160a060020a03908116845260018201541693830184905260028101549183019190915260038101546060830152600481015460ff808216151560808501526101009182900416151560a0840152600582015460c0840152600682015460e08401526007909101549082015292503314610b5a57600080fd5b50506040805160608101825293151584526000602080860182815242878501908152948352600d90915291902093518454915115156101000261ff001991151560ff19909316929092171617835551600192909201919091555050565b600080610bc26108f8565b34811515610bcc57fe5b049050610be1610bda610a67565b33836110db565b50919050565b336000908152600660205260408120548211801590610c065750600082115b1515610c1157600080fd5b33600090815260066020526040902054610c31908363ffffffff61107c16565b3360009081526006602052604080822092909255600160a060020a03851681522054610c63908363ffffffff61108e16565b600160a060020a03841660009081526006602052604090205550600192915050565b6000806000610c92611441565b610c9a61148d565b6000868152600a6020908152604080832054600b8352818420548185526009909352908320805491975091955085908110610cd157fe5b60009182526020808320604080516101208101825260089094029091018054600160a060020a039081168552600180830154909116858501526002820154858401526003820154606080870191909152600483015460ff80821615156080890152610100918290048116151560a0890152600585015460c0890152600685015460e0890152600790940154818801528b8852600d865296849020845191820185528054808516151583529790970490921615159382019390935293909101549083018190529094509092509050607881014211610dad57600080fd5b60a08301511515600114610dc057600080fd5b8251600160a060020a03898116911614610dd957600080fd5b60e083015160c084015111610ded57600080fd5b610e0483602001518460000151856060015161118f565b506001979650505050505050565b600160a060020a03918216600090815260056020908152604080832093909416825291909152205490565b6000818152600b6020908152604080832054600a83528184205480855260099093529083208054849384938493849391929184918291829182918291829189908110610e8557fe5b906000526020600020906008020195508560000160009054906101000a9004600160a060020a03169450856002015493508560040160009054906101000a900460ff1692508560040160019054906101000a900460ff1691508560070154905084848484849c509c509c509c509c50505050505050505091939590929450565b33600090815260076020526040902055565b6000806000610f24611441565b6000868152600a6020908152604080832054600b835281842054808552600d845282852082865260099094529190932080549397509095509093509084908110610f6a57fe5b6000918252602091829020604080516101208101825260089093029091018054600160a060020a0390811680855260018301549091169484019490945260028101549183019190915260038101546060830152600481015460ff808216151560808501526101009182900416151560a0840152600582015460c0840152600682015460e0840152600790910154908201529150331461100857600080fd5b5080549315156101000261ff001990941693909317909255505050565b6000818152600c60209081526040808320543384526008909252822080548392918391829182918590811061105657fe5b600091825260209091206002909102018054600190910154909890975095505050505050565b60008282111561108857fe5b50900390565b60008282018381101561109d57fe5b9392505050565b6110af8383836111a4565b6110bd82848360020261127e565b505050565b6110cd83838361135b565b6110bd82848360020261135b565b600160a060020a03831660009081526006602052604081205482118015906111035750600082115b151561110e57600080fd5b600160a060020a038416600090815260066020526040902054611137908363ffffffff61107c16565b600160a060020a03808616600090815260066020526040808220939093559085168152205461116c908363ffffffff61108e16565b600160a060020a0384166000908152600660205260409020555060019392505050565b61119a83838361127e565b6110bd8284836002025b600160a060020a0380841660009081526004602090815260408083209386168352929052205481118015906111d95750600081115b15156111e457600080fd5b600160a060020a0380841660009081526004602090815260408083209386168352929052205461121a908263ffffffff61107c16565b600160a060020a038085166000908152600460209081526040808320938716835292815282822093909355600690925290205461125d908263ffffffff61108e16565b600160a060020a039092166000908152600660205260409020919091555050565b600160a060020a0380841660009081526004602090815260408083209386168352929052205481118015906112b35750600081115b15156112be57600080fd5b600160a060020a038084166000908152600460209081526040808320938616835292905220546112f4908263ffffffff61107c16565b600160a060020a038085166000818152600460209081526040808320948816835293815283822094909455908152600690925290205461133a908263ffffffff61108e16565b600160a060020a039093166000908152600660205260409020929092555050565b600160a060020a03831660009081526006602052604090205481118015906113835750600081115b151561138e57600080fd5b600160a060020a0383166000908152600660205260409020546113b7908263ffffffff61107c16565b600160a060020a038085166000908152600660209081526040808320949094556004815283822092861682529190915220546113f9908263ffffffff61108e16565b600160a060020a03938416600090815260046020908152604080832095909616825293909352929091209190915550565b604080518082019091526000808252602082015290565b6040805161012081018252600080825260208201819052918101829052606081018290526080810182905260a0810182905260c0810182905260e0810182905261010081019190915290565b6040805160608101825260008082526020820181905291810191909152905600a165627a7a723058203fbe43249bf48ad07d855bc661fa44fef679e0484314d1e6b9b228f38a65245e0029";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_SETCHUNKDATAOFMAINFILE = "setChunkDataOfMainFile";

    public static final String FUNC_GETUSERID = "getUserId";

    public static final String FUNC_SETFILEDETAIL = "setFileDetail";

    public static final String FUNC_SETSTATUSOFDOWNLOADFILEBYCLIENT = "setStatusOfDownLoadFileByClient";

    public static final String FUNC_GETTOKENPRICE = "getTokenPrice";

    public static final String FUNC_SETCHUNKVERIFYBYCLIENT = "setChunkVerifyByClient";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_SETCHUNKVERIFYBYNODE = "setChunkVerifyByNode";

    public static final String FUNC_GETOWNER = "getOwner";

    public static final String FUNC_SETCLIENTDOWNLOADFILE = "setClientDownLoadFile";

    public static final String FUNC_BUYTOKEN = "buyToken";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_TRIGGERCOMPENSATION = "triggerCompensation";

    public static final String FUNC_ALLOWANCE = "allowance";

    public static final String FUNC_GETCHUNCKDATAOFMAINFILE = "getChunckDataOfMainFile";

    public static final String FUNC_ADDUSER = "addUser";

    public static final String FUNC_SETSTATUSOFDOWNLOADFILEBYNODE = "setStatusOfDownLoadFileByNode";

    public static final String FUNC_GETFILEDETAIL = "getFileDetail";

    public static final Event TRANSFER_EVENT = new Event("Transfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event APPROVAL_EVENT = new Event("Approval", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

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

    public RemoteCall<TransactionReceipt> setStatusOfDownLoadFileByClient(byte[] chunkHash, Boolean status) {
        final Function function = new Function(
                FUNC_SETSTATUSOFDOWNLOADFILEBYCLIENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(chunkHash), 
                new org.web3j.abi.datatypes.Bool(status)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> getTokenPrice() {
        final Function function = new Function(FUNC_GETTOKENPRICE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> setChunkVerifyByClient(byte[] fileHash, byte[] chunkHash, Boolean verify) {
        final Function function = new Function(
                FUNC_SETCHUNKVERIFYBYCLIENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(fileHash), 
                new org.web3j.abi.datatypes.generated.Bytes32(chunkHash), 
                new org.web3j.abi.datatypes.Bool(verify)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> balanceOf(String _owner) {
        final Function function = new Function(FUNC_BALANCEOF, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> setChunkVerifyByNode(byte[] chunkHash, Boolean verify) {
        final Function function = new Function(
                FUNC_SETCHUNKVERIFYBYNODE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(chunkHash), 
                new org.web3j.abi.datatypes.Bool(verify)), 
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

    public RemoteCall<TransactionReceipt> triggerCompensation(String nodeAddress, byte[] chunkHash) {
        final Function function = new Function(
                FUNC_TRIGGERCOMPENSATION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(nodeAddress), 
                new org.web3j.abi.datatypes.generated.Bytes32(chunkHash)), 
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

    public RemoteCall<TransactionReceipt> setStatusOfDownLoadFileByNode(byte[] chunkHash, Boolean status) {
        final Function function = new Function(
                FUNC_SETSTATUSOFDOWNLOADFILEBYNODE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(chunkHash), 
                new org.web3j.abi.datatypes.Bool(status)), 
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

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
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
                typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
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
