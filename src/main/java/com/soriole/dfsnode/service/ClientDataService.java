package com.soriole.dfsnode.service;

import com.soriole.dfsnode.model.db.ClientData;
import com.soriole.dfsnode.model.dto.UploadRequest;
import com.soriole.dfsnode.repository.ClientDataRepository;
import com.soriole.dfsnode.repository.ClientRepository;
import com.soriole.dfsnode.model.db.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
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

    @Autowired
    ClientService clientService;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ClientDataRepository clientDataRepository;



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
            client = optClient.get();
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

        return true;
    }


    //todo: implement this
    private String getFileHash(MultipartFile file) {
        return "dummyFileHash";
    }

}
