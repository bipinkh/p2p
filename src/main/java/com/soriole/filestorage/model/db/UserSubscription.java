package com.soriole.filestorage.model.db;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.soriole.filestorage.model.dto.SubscribeRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

/**
 * @author bipin khatiwada
 * github.com/bipinkh
 */

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class UserSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JsonBackReference
    User user;

    @ManyToOne
    @JsonManagedReference
    Subscription subscriptionPackage;

    private boolean activeStatus;

    private Timestamp startedDate;

    private Timestamp endingDate;

    @OneToMany
    @JsonManagedReference
    private List<Renewals> renewals;

    private double sizeConsumed;

    private double bandwidthConsumed;

    public static UserSubscription fromSubscribeRequest(SubscribeRequest request){

        UserSubscription subscription = new UserSubscription();

        Calendar calendar = Calendar.getInstance();
        Timestamp currentTimeStamp = new Timestamp(calendar.getTime().getTime());
        calendar.setTime(currentTimeStamp);
        calendar.add(Calendar.MONTH, request.getMonth());
        Timestamp endingTimestamp = new Timestamp(calendar.getTime().getTime());

        subscription.setStartedDate(currentTimeStamp);
        subscription.setEndingDate(endingTimestamp);
        subscription.setActiveStatus(false);            // subscription is active only after renewing.
        subscription.setSizeConsumed(0);
        subscription.setBandwidthConsumed(0);

        return subscription;

    }


    public void addRenewal(Renewals renewals){
        this.renewals.add(renewals);
        renewals.setUserSubscription(this);
    }

    @Override
    public String toString() {
        return "UserSubscription{" +
                "id=" + id +
                ", subscriptionPackage=" + subscriptionPackage +
                ", activeStatus=" + activeStatus +
                ", startedDate=" + startedDate +
                ", endingDate=" + endingDate +
                ", renewals=" + renewals +
                ", sizeConsumed=" + sizeConsumed +
                ", bandwidthConsumed=" + bandwidthConsumed +
                '}';
    }



}
