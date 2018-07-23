package com.soriole.filestorage.service;

import com.soriole.filestorage.model.dto.FileUploadRequest;
import com.soriole.filestorage.repository.FileRepo;
import com.soriole.filestorage.repository.UserRepo;
import com.soriole.filestorage.model.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author bipin khatiwada
 * github.com/bipinkh
 */
@Service
public class FileService {
    private static String BASE_FOLDER = "files//";

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
            // todo: remove this user declaration and throw exception of USER_NOT_FOUND
            user = usr.get();
        }

        //todo: check subscription of user.

        // check for duplicate file
        Optional<com.soriole.filestorage.model.db.File> preExistingFile = fileRepo.findByFileHashAndUser(
                uploadRequest.getFiles()[0].getName(),user);
        if (preExistingFile.isPresent()){
            //todo: throw exception FILE_ALREADY_EXISTS
            System.out.println("File Already Exists, so cannot be stored again !");
        }


        MultipartFile[] files = uploadRequest.getFiles();
        fileName = files[0].getOriginalFilename();
        folderPath = BASE_FOLDER.concat(user.getUserKey()).concat("//");
        filePath =folderPath.concat(fileName).concat("//");

        try{

            File directory = new File(String.valueOf(BASE_FOLDER+user.getUserKey()));
            if(!directory.exists()){
                System.out.println("folder not found creating new folder for user");
                directory.mkdir();
            }
            System.out.println("writing file");
            byte[] bytes = files[0].getBytes();
            Path path = Paths.get(filePath);
            Files.write(path, bytes);
            System.out.println("file saved");

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

    // return all files of an user for given userId
    public List<com.soriole.filestorage.model.db.File> getAllFilesOfUser(Long userId){
        return userRepo.getOne(userId).getFile();
    }
    // return all files of user for given userKey
    public List<com.soriole.filestorage.model.db.File> getAllFilesOfUser(String userKey){
        Optional<User> user = userRepo.findByUserKey(userKey);
        if (user.isPresent()){
            return getAllFilesOfUser(user.get().getId());
        }
        return null;
    }

}
