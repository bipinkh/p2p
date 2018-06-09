package com.endtoendmessenging.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Subscription {

    @Id
    byte[] subscriber;
    Date subscriptionStart;
    Date subscriptionExpiry;

    public byte[] getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(byte[] subscriber) {
        this.subscriber = subscriber;
    }

    public Date getSubscriptionStart() {
        return subscriptionStart;
    }

    public void setSubscriptionStart(Date subscriptionStart) {
        this.subscriptionStart = subscriptionStart;
    }

    public Date getSubscriptionExpiry() {
        return subscriptionExpiry;
    }

    public void setSubscriptionExpiry(Date subscriptionExpiry) {
        this.subscriptionExpiry = subscriptionExpiry;
    }
}
