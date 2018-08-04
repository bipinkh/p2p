//package com.soriole.dfsnode.service;
//
//import org.springframework.stereotype.Service;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;
//
//
///**
// * @author github.com/bipinkh
// * created on : 03 Aug 2018
// */
//@Service
//public class NotificationService {
//
//    // The SimpMessagingTemplate is used to send Stomp over WebSocket messages.
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//
//    /**
//     * Send notification to users subscribed on channel "/user/queue/notify".
//     *
//     * The message will be sent only to the user with the given username.
//     *
//     * @param notification The notification message.
//     * @param username The username for the user to send notification.
//     */
//    public void notify(String notification, String username) {
//        messagingTemplate.convertAndSendToUser(
//                username,
//                "/queue/notify",
//                notification
//        );
//        return;
//    }
//
//}