package com.soriole.dfsnode.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author github.com/bipinkh
 * created on : 26 Jul 2018
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private Long clientId;

    private String clientPublicKey;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true )
    List<ClientData> clientDataList = new ArrayList<ClientData>();

    public void addClientData(ClientData data){
        this.clientDataList.add(data);
        data.setClient(this);
    }

}
