package com.soriole.dfsnode.service;

import com.soriole.dfsnode.exceptions.CustomException;
import com.soriole.dfsnode.model.db.ClientData;
import com.soriole.dfsnode.model.dto.DownloadRequest;
import com.soriole.dfsnode.model.dto.ClientDataDto;
import com.soriole.dfsnode.model.dto.RenewRequest;
import com.soriole.dfsnode.model.dto.UploadRequest;
import com.soriole.dfsnode.repository.ClientDataRepository;
import com.soriole.dfsnode.repository.ClientRepository;
import com.soriole.dfsnode.model.db.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Optional;

/**
 * @author github.com/bipinkh
 * created on : 26 Jul 2018
 */

// services for file upload and download
    @Service
public class ClientDataService {

    @Value("${dfs.params.subscriptionlength}")
    private int SUBSCRIPTON_LENGTH_MONTH;
    @Value("${dfs.params.uploadFolder}")
    private String BASE_FOLDER;
    @Value("${dfs.params.totalDownloads}")
    private int TOTAL_DOWNLOAD_COUNT;

    @Autowired
    ClientService clientService;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ClientDataRepository clientDataRepository;
    @Autowired TransactionService transactionService;



    public boolean uploadFile(UploadRequest request){
        Client client;

        Calendar calendar = Calendar.getInstance();
        Timestamp currentTimeStamp = new Timestamp(calendar.getTime().getTime());
        calendar.add(Calendar.MONTH, SUBSCRIPTON_LENGTH_MONTH);
        Timestamp endingTimestamp = new Timestamp(calendar.getTime().getTime());

        // get or create new client
        Optional<Client> optClient = clientRepository.findByClientPublicKey(request.getUserKey());
        if (!optClient.isPresent()){
            client = new Client();
            client.setClientPublicKey(request.getUserKey());
            client = clientRepository.getOne(clientRepository.save(client).getId());   // get reference
        }else{
            client = clientRepository.getOne( optClient.get().getId() );
        }

        // create directory if not already created.
        MultipartFile[] files = request.getFiles();
        String fileName = files[0].getOriginalFilename();
        String folderPath = BASE_FOLDER.concat(client.getClientPublicKey().concat("//"));
        String savedFilePath = folderPath.concat(fileName).concat("//");
        try{
            File directory = new File(String.valueOf(BASE_FOLDER+client.getClientPublicKey()));
            if(!directory.exists()){
                System.out.println("folder not found creating new folder for user");
                directory.mkdir();
            }

            // save file
            System.out.println("writing file");
            byte[] bytes = files[0].getBytes();
            Path path = Paths.get(savedFilePath);
            Files.write(path, bytes);
            System.out.println("file saved");

        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
            return false;
        }

        // create new client data
        ClientData clientData = new ClientData();
        clientData.setFileHash(getFileHash(files[0]));
        clientData.setRenewedDate(currentTimeStamp);
        clientData.setEndingDate(endingTimestamp);
        clientData.setFileDataPath(savedFilePath);
        clientData = clientDataRepository.getOne( clientDataRepository.save(clientData).getId() );
        client.addClientData(clientData);   // bidirectional mapping

        // add transaction
        transactionService.txnFileUpload(client.getClientPublicKey(), clientData.getFileHash());

        //todo: agree storage in contract

        return true;
    }

    public File getFile(DownloadRequest request){
        ClientData clientData;

        // check if user exists
        Optional<Client> optClient = clientRepository.findByClientPublicKey(request.getUserKey());
        if (!optClient.isPresent()){
            System.out.println("user do not have file");
            throw new CustomException("user do not have any files currently in this node");
        }

        // check if file exists
        Optional<ClientData> optData = clientDataRepository.findByFileHash(request.getFilehash());
        if (!optData.isPresent()){
            System.out.println("user do not have the file with given hash");
            throw new CustomException("user do not have the file with given hash in this node");
        }else{
            clientData = clientDataRepository.getOne(optData.get().getId());
        }

        // check if the number of downloads is exceeded
        if (clientData.getCurrentDownloadCount() > TOTAL_DOWNLOAD_COUNT)
            throw  new CustomException("user has exceeded download number for this subscription");

        //check if time has expired
        Calendar calendar = Calendar.getInstance();
        Timestamp today = new Timestamp(calendar.getTime().getTime());
        if (today.after(clientData.getEndingDate()))
            throw new CustomException("user has already exceeded the timelimit. you might want to renew or delete file");

        // increase current download and total download count
        clientData.setCurrentDownloadCount(clientData.getCurrentDownloadCount() + 1);
        clientData.setTotalDownloadCount(clientData.getTotalDownloadCount() + 1 );
        clientDataRepository.save(clientData);

        // add transaction
        transactionService.txnFileDownload(optClient.get().getClientPublicKey(), clientData.getFileHash());

        return new File(clientData.getFileDataPath());
    }

    public boolean renewFile(RenewRequest request) {
        ClientData clientData;

        // check if user exists
        Optional<Client> optClient = clientRepository.findByClientPublicKey(request.getUserKey());
        if (!optClient.isPresent()){
            System.out.println("user do not have file");
            throw new CustomException("user do not have any files currently in this node");
        }

        // check if file exists
        Optional<ClientData> optData = clientDataRepository.findByFileHash(request.getFilehash());
        if (!optData.isPresent()){
            System.out.println("user do not have the file with given hash");
            throw new CustomException("user do not have the file with given hash in this node");
        }else{
            clientData = clientDataRepository.getOne(optData.get().getId());
        }

        // update renew date
        Calendar calendar = Calendar.getInstance();
        Timestamp currentTimeStamp = new Timestamp(calendar.getTime().getTime());
        calendar.add(Calendar.MONTH, SUBSCRIPTON_LENGTH_MONTH);
        Timestamp endingTimestamp = new Timestamp(calendar.getTime().getTime());
        System.out.println(currentTimeStamp.toString());
        clientData.setRenewedDate(currentTimeStamp);
        clientData.setEndingDate(endingTimestamp);
        clientData.setCurrentDownloadCount(0);
        clientDataRepository.save(clientData);

        // todo: agree in contract
        return true;
    }

    //todo: use this using dto response
    public ClientDataDto getStatusOfFile(String fileHash){
        // check if file exists
        Optional<ClientData> optData = clientDataRepository.findByFileHash(fileHash);
        if (!optData.isPresent()){
            System.out.println("user do not have the file with given hash");
            throw new CustomException("user do not have the file with given hash in this node");
        }else{
           return ClientDataDto.fromClientData(optData.get());
        }
    }


    //todo: implement this
    private String getFileHash(MultipartFile file) {
        return "dummyFileHash";
    }

}
