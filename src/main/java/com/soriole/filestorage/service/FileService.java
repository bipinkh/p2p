package com.soriole.filestorage.service;

import com.filestorage.model.dto.FileUploadRequest;
import com.soriole.filestorage.repository.FileRepo;
import com.soriole.filestorage.repository.UserRepo;
import com.soriole.filestorage.model.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

/**
 * @author bipin khatiwada
 * github.com/bipinkh
 */
@Service
public class FileService {
    private static String BASE_FOLDER = "src//main//resources//files//";

    @Autowired
    FileRepo fileRepo;
    @Autowired
    UserRepo userRepo;

    public boolean saveFile(FileUploadRequest uploadRequest){
        User user;
        String filePath, folderPath, fileName;

        Optional<User> usr = userRepo.findByUserKey(uploadRequest.getUserKey());
        if (!usr.isPresent()){
            user = new User(null, uploadRequest.getUserKey(), 1, null, new ArrayList<>());
            userRepo.saveAndFlush(user);
        }else {
            user = usr.get();
        }
        // todo: remove this user declaration and throw exception of USER_NOT_FOUND
        // todo: handle duplicate file


        MultipartFile[] files = uploadRequest.getFiles();
        fileName = files[0].getOriginalFilename();
        folderPath = BASE_FOLDER.concat(user.getUserKey()).concat("//");
        filePath =folderPath.concat(fileName).concat("//");

        try{

            File directory = new File(String.valueOf(BASE_FOLDER+user.getUserKey()));
            if(!directory.exists())
                directory.mkdir();

            byte[] bytes = files[0].getBytes();
            Path path = Paths.get(filePath);
            Files.write(path, bytes);

        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
            return false;
        }

            //create file
            com.soriole.filestorage.model.db.File file = com.soriole.filestorage.model.db.File.fromFileUploadRequest(uploadRequest);
            fileRepo.save(file);
            //bidirectional mapping
            user.addFile(file);

        return true;

    }

}
