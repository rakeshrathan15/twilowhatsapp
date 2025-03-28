package com.neoteric.gupshup.service;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@Service
public class TwiloService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;


    @Value("${twilio.whatsapp.number}")
    private String twilioWhatsAppNumber;

    @PostConstruct
    public void initTwilio() {
        Twilio.init(accountSid, authToken);
        System.out.println("Twilio Initialized Successfully!");
    }

    public String sendMessage(String to, String message) {
        Message sentMessage = Message.creator(
                new PhoneNumber("whatsapp:" + to),  // Receiver's WhatsApp Number
                new PhoneNumber(twilioWhatsAppNumber),  // Twilio WhatsApp Number
                message
        )
                .create();

        return "Message Sent! ID: " + sentMessage.getSid();
    }

    public String sendMediaMessage(String to, String mediaUrl, String message) {
        Message sentMessage = Message.creator(
                new PhoneNumber("whatsapp:" + to),
                new PhoneNumber(twilioWhatsAppNumber),
                message
        ).setMediaUrl(Collections.singletonList(URI.create(mediaUrl))).create();

        return "Media message sent: " + sentMessage.getSid();
    }


}



