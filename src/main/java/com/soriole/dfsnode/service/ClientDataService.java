package com.soriole.dfsnode.service;

import com.google.common.hash.Hashing;
import com.soriole.dfsnode.exceptions.CustomException;
import com.soriole.dfsnode.model.db.ClientData;
import com.soriole.dfsnode.model.dto.DownloadRequest;
import com.soriole.dfsnode.model.dto.ClientDataDto;
import com.soriole.dfsnode.model.dto.RenewRequest;
import com.soriole.dfsnode.model.dto.UploadRequest;
import com.soriole.dfsnode.repository.ClientDataRepository;
import com.soriole.dfsnode.repository.ClientRepository;
import com.soriole.dfsnode.model.db.Client;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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


    /**
     * function to handle the file upload request by the client
     * @param request UploadRequest object that contains file, filehash and userKey
     * @return true if the file upload request is processed
     * */

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
        MultipartFile file = request.getFile();
        String fileName = file.getOriginalFilename();
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
            byte[] bytes = file.getBytes();
            Path path = Paths.get(savedFilePath);
            Files.write(path, bytes);
            System.out.println("file saved");

        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
            return false;
        }

        //agree on generated file hash with the user sent file hash
        String fileHash = null;
        try {
            fileHash = getFileHash(file);
            if (fileHash ==null)
                throw new CustomException("cannot calculate hash of file");
            if (! fileHash.equals(request.getFileHash())){
                throw new CustomException("hash doesn't match with the client sent hash !");
            }else{
                System.out.println("verified file hash !");
            }
        } catch (IOException e) {
            return false;
        }

        // create new client data
        ClientData clientData = new ClientData();
        clientData.setFileHash(fileHash);
        clientData.setRenewedDate(currentTimeStamp);
        clientData.setEndingDate(endingTimestamp);
        clientData.setFileDataPath(savedFilePath);
        clientData = clientDataRepository.getOne( clientDataRepository.save(clientData).getId() );
        client.addClientData(clientData);   // bidirectional mapping

        // add transaction
        transactionService.txnFileUpload(client.getClientPublicKey(), clientData.getFileHash());

        //todo CONTRACT: verify the file storage request on contract

        return true;
    }


    /**
     * function to send back the file when user requests it.
     * @param request DownloadRequest object that contains the userKey and filehash
     * @return requested file
     * */

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

        // return file
        return new File(clientData.getFileDataPath());
    }



    /**
     * function to renew the file storage subscription
     * @param request RenewRequest object that contains userKey and fileHash
     * @return true is the renew process is successful
     * */

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

        // add transaction
        transactionService.txnSubsRenew(optClient.get().getClientPublicKey(), clientData.getFileHash());

        // todo CONTRACT: agree to the update request in contract
        return true;
    }


    /**
     * function to return the details of the file
     * @param fileHash hash of the file whose detail is to be searched
     * @return ClientDataDto object that contains the file details
     * */

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

    /**
     * function to return the details of all the files of any user
     * @param userKey user key of the user whose all file details is to be returned
     * @return list of ClientDataDto object that contains the file details of each one
     * */

    public List<ClientDataDto> listAllFiles(String userKey) {
        // check if user exists
        Optional<Client> optClient = clientRepository.findByClientPublicKey(userKey);
        if (!optClient.isPresent()){
            System.out.println("user do not have file");
            throw new CustomException("user do not have any files currently in this node");
        }

        List<ClientData> allclientdata = clientDataRepository.findAllByClient(optClient.get());
        List<ClientDataDto> returns = new ArrayList<>();
        for (ClientData d : allclientdata)
            returns.add(ClientDataDto.fromClientData(d));
        return returns;
    }


    /**
     * function to calculate <></>he hash of file
     * @param file MultipartFile that is sent by the user to store in node
     * @return hash of file
     * */

    private static String getFileHash(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        if (convFile.createNewFile() ){
            try (FileOutputStream fos = new FileOutputStream(convFile)) {
                fos.write(file.getBytes());
                return com.google.common.io.Files.hash(convFile,Hashing.sha256()).toString();
            }
        }
        return null;
    }

    private static String getFileHash(File file) throws IOException {
        return com.google.common.io.Files.hash(file,Hashing.sha256()).toString();
    }

}
