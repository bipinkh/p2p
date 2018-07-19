package com.soriole.filestorage.model.db;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bipin khatiwada
 * github.com/bipinkh
 */

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String userKey;

    private int userStatus;     //active, deactive, blocked

    @OneToOne
    @JsonBackReference
    UserSubscription userSubscription;

    @OneToMany
    @JsonManagedReference
    List<File> file = new ArrayList<File>();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userKey='" + userKey + '\'' +
                ", userStatus=" + userStatus +
                ", userSubscription=" + userSubscription +
                '}';
    }


    public void addUserSubscription(UserSubscription subscription){
        this.setUserSubscription(subscription);
        subscription.setUser(this);
    }

    public void addFile(File file){
        this.file.add(file);
        file.setUser(this);
    }


}
