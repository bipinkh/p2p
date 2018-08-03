package com.soriole.dfsnode.service;

import com.soriole.dfsnode.model.db.ClientData;
import com.soriole.dfsnode.model.dto.ClientDataDto;
import com.soriole.dfsnode.model.dto.NodeDetails;
import com.soriole.dfsnode.model.dto.NodeTransactionDetails;
import com.soriole.dfsnode.repository.ClientDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author github.com/bipinkh
 * created on : 26 Jul 2018
 */
@Service
public class NodeService {

    @Autowired
    ClientDataRepository clientDataRepository;
    @Autowired
    TransactionService transactionService;

    public ResponseEntity<NodeDetails> getStats() {
    }

    public ResponseEntity<NodeTransactionDetails> getTxns() {

    }

    // get all file details stored
    public List<ClientDataDto> getFiles() {
        List<ClientDataDto> returnList = new ArrayList<>();

        try {
            List<ClientData> clientData = clientDataRepository.findAll();
            for (ClientData data : clientData) {
                returnList.add(ClientDataDto.fromClientData(data));
            }
        }catch (Exception e){
        }
        return returnList;
    }
}
