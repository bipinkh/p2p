pragma solidity ^0.4.18;
import './ERC20Token.sol';

contract DecentralizedDB is ERC20Token {
    /**2 way mapping for user registration**/
    mapping(address=>uint) userRegistry;
    
    /**stores the whole file hash and its name **/
    struct FileDetail{
        bytes32 fileHash;
        bytes32 fileName;
    }
    
    /**stores the each chunk necessary data **/
    struct ChunkData{
        address nodeAddress;
        address clientAddress;
        bytes32 chunkHash;
        uint reqdTokens;
        bool clientVerify;
        bool nodeVerify;
        uint256 totalbandWidth;
        uint256 consumedBandwidth;
        uint256 timestamp;
    }
    /**stores the download info of data**/
    struct DownloadChunk{
        bool clientVerify;
        bool nodeVerify;
        uint256 timestamp;
    }
    /**maps client address to FileDetails uploaded**/
    mapping(address=>FileDetail[])clientFiles;
    /**maps each main file hash to its chunk data**/
    mapping(bytes32=>ChunkData[])mainFileToChunkData;
    /**maps chunk to mainfileHash**/
    mapping(bytes32=>bytes32) chunkHashToMainHash;
    /**maps chunkHash to its index**/
    mapping(bytes32=>uint)chunkHashToIndex;
     /**maps fileHash to its index**/
    mapping(bytes32=>uint)fileHashToIndex;
     /**maps client to DownloadChunk **/
    mapping(uint=>DownloadChunk)downLoadFiles;
    
   
     /**----------------------userRegistry------------------**/
    
    //called by msg.sender to set kademlia index
    function addUser(uint index) public{
        userRegistry[msg.sender]=index;
    }
    //called by msg.sender to get kademlia index
    function getUserId()view public returns(uint){
        return userRegistry[msg.sender];
    }
    
     /**----------------------clientFiles------------------**/
     
    /**connects FileDetail with msg.sender**/
    
    function setFileDetail(bytes32 fileHash,bytes32 fileName)public{
        FileDetail memory fileDetail=FileDetail(fileHash,fileName);
        clientFiles[msg.sender].push(fileDetail);
        uint currentFileDetailIndex=clientFiles[msg.sender].length-1;
        fileHashToIndex[fileHash]=currentFileDetailIndex;
    }
    
    function getFileDetail(bytes32 _fileHash) view public returns(bytes32 ,bytes32 ){
        //uint lengthOfClientFile=clientFiles[msg.sender].length;
        uint256 index=fileHashToIndex[_fileHash];
            FileDetail storage fileDetail=clientFiles[msg.sender][index];
            bytes32 fileHash=fileDetail.fileHash;
            bytes32 fileName=fileDetail.fileName;
           
        return(fileHash,fileName);
        
    }
    
    /**----------------------mainFileToChunkData------------------**/
    
    
    /**linking main file hash with each chunk data by client**/ 
    
    function setChunkDataOfMainFile(bytes32 mainFileHash,
                                    address nodeAddress,
                                    bytes32 chunkHash,
                                    uint reqdTokens,
                                    uint256 totalbandWidth)public{
        chunkHashToMainHash[chunkHash]=mainFileHash;
        mainFileToChunkData[mainFileHash].push(ChunkData(nodeAddress,
                                                        msg.sender,
                                                        chunkHash,
                                                        reqdTokens,
                                                        false,
                                                        false,
                                                        totalbandWidth,
                                                        0,
                                                        now));
        uint index=mainFileToChunkData[mainFileHash].length-1;
        chunkHashToIndex[chunkHash]=index;
       
    }
    
    /**get all the ChunkData associated with the mainFileHash for client**/
    
    function getChunckDataOfMainFile(bytes32 chunkHash)view public returns(address,
                                                                            bytes32,
                                                                            bool,
                                                                            bool,
                                                                            uint256){
            uint index=chunkHashToIndex[chunkHash];
            bytes32 mainFileHash=chunkHashToMainHash[chunkHash];
            ChunkData storage chunkData=mainFileToChunkData[mainFileHash][index];
           address nodeAddress=chunkData.nodeAddress;
           bytes32 chunkHashB=chunkData.chunkHash;
          bool  clientVerify=chunkData.clientVerify;
           bool nodeVerify=chunkData.nodeVerify;
           uint256 time=chunkData.timestamp;
        return(nodeAddress,chunkHashB,clientVerify,nodeVerify,time);
    }
    /**set verify by node to chunk**/
    
    function setChunkVerifyByNode(bytes32 chunkHash,bool verify)public{
        //gets mainFileHash from chunk hash
        bytes32  mainFileHash=chunkHashToMainHash[chunkHash];
        //gets index of chunkHash
        uint indexChunk=chunkHashToIndex[chunkHash];
        ChunkData storage chunkData=mainFileToChunkData[mainFileHash][indexChunk];
        //function caller should be node
        require(msg.sender==chunkData.nodeAddress);
        chunkData.nodeVerify=verify;
        }
        
        
    /**set verify by client to chunk**/
    function setChunkVerifyByClient(bytes32 fileHash,bytes32 chunkHash,bool verify)public{
        //gets index of chunkHash for ChunkData
        uint indexChunk=chunkHashToIndex[chunkHash];
        ChunkData storage chunkData=mainFileToChunkData[fileHash][indexChunk];
        uint indexFileDetail=fileHashToIndex[fileHash];
        require(fileHash==clientFiles[msg.sender][indexFileDetail].fileHash);
        require(msg.sender==chunkData.clientAddress);
        chunkData.clientVerify=verify;
        
        //consider lockBalanceOfBothParty is called after client verifies
        if(verify){
            lockBalanceOfBothParty(msg.sender,chunkData.nodeAddress,chunkData.reqdTokens);
        }
        
        }
    
    /**----------------------lockBalances------------------**/
        
    function lockBalanceOfBothParty(address from,address to,uint value)internal{
        //from=client to=node
        lockBalance(from,to,value);
        lockBalance(to,from,2*value);
        
    }
    //called when file is downloaded
    function payToNode(address from,address to,uint value)internal{
        lockPay(from,to,value);
        lockRelease(to,from,2*value);
    }
     //withdraw lock balances
    function lockBalanceWithdraw(address from,address to,uint value)internal{
        lockRelease(from,to,value);
        lockRelease(to,from,2*value);
    }    
    function lockBalancecompensateClient(address from,address to,uint value)internal{
        lockRelease(from,to,value);
        lockPay(to,from,2*value);
    }
     
    
     /**----------------------clientDownLoadFiles------------------**/
     
     function setClientDownLoadFile(bytes32 chunkHash,bool verify)public{
        //getting fileHash
        bytes32 fileHash=chunkHashToMainHash[chunkHash];
        //getting chunk index for ChunkData
        uint indexChunk=chunkHashToIndex[chunkHash];
        ChunkData memory chunkData=mainFileToChunkData[fileHash][indexChunk];
        //verifying client
        require(msg.sender==chunkData.clientAddress); 
        DownloadChunk memory downloadChunk=DownloadChunk(verify,false,now);
        downLoadFiles[indexChunk]=downloadChunk;
     }
     
     function setStatusOfDownLoadFileByNode(bytes32 chunkHash,bool status)public{
        //getting fileHash
        bytes32 fileHash=chunkHashToMainHash[chunkHash];
        uint indexChunk=chunkHashToIndex[chunkHash];
        DownloadChunk storage downloadChunk=downLoadFiles[indexChunk];
        ChunkData memory chunkData=mainFileToChunkData[fileHash][indexChunk];
        //verifying same node
        require(msg.sender==chunkData.nodeAddress);
        downloadChunk.nodeVerify=status;
     }
     
      function setStatusOfDownLoadFileByClient(bytes32 chunkHash,bool status)public{
          //getting fileHash
        bytes32 fileHash=chunkHashToMainHash[chunkHash];
        uint indexChunk=chunkHashToIndex[chunkHash];
        DownloadChunk storage downloadChunk=downLoadFiles[indexChunk];
        ChunkData memory chunkData=mainFileToChunkData[fileHash][indexChunk];
        //verifying client
        require(msg.sender==chunkData.clientAddress);
        downloadChunk.clientVerify=status;
        if(status){
            payToNode(chunkData.clientAddress,chunkData.nodeAddress,chunkData.reqdTokens);
        }
     }
     
     function triggerCompensation(address nodeAddress,bytes32 chunkHash)public returns(bool){
        //getting fileHash
        bytes32 fileHash=chunkHashToMainHash[chunkHash];
        //getting chunk index for ChunkData
        uint indexChunk=chunkHashToIndex[chunkHash];
        ChunkData memory chunkData=mainFileToChunkData[fileHash][indexChunk];
        //check timestamp
        DownloadChunk memory downloadChunk=downLoadFiles[indexChunk];
        uint prevoiusTimeStamp=downloadChunk.timestamp;
        require(now>prevoiusTimeStamp+2 minutes);
        //check status
        require(chunkData.nodeVerify==true);
        //checking nodeAddress contains that chunkData
        require(chunkData.nodeAddress==nodeAddress);
        //check bandwidth
        //totalbandWidth should be greater that consumedBandwidth to further download chunk
        require(chunkData.totalbandWidth>chunkData.consumedBandwidth);
        //check time
        
        
        //then transfer balance to client  
        lockBalancecompensateClient(chunkData.clientAddress,chunkData.nodeAddress,chunkData.reqdTokens);
        return true;
     }
}