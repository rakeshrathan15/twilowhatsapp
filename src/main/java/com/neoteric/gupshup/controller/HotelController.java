package com.neoteric.gupshup.controller;
import com.neoteric.gupshup.model.SendMessageRequest;
import com.neoteric.gupshup.service.BookingService;
import com.neoteric.gupshup.service.TwiloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/hotel")
public class HotelController {


    @Autowired
    private final TwiloService twiloService;

    @Autowired
    private final BookingService bookingService;

    public HotelController(TwiloService twiloService, BookingService bookingService) {
        this.twiloService = twiloService;
        this.bookingService = bookingService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody SendMessageRequest request) {
        if (request.getTo() == null || request.getMessage() == null) {
            return ResponseEntity.badRequest().body("❌ 'to' and 'message' fields are required.");
        }

        String response = twiloService.sendMessage(request.getTo(), request.getMessage());
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/webhook", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<String> receiveMessage(@RequestParam Map<String, String> payload) {
        String from = payload.get("From");
        String body = payload.get("Body").trim().toLowerCase();

        if (from == null || body == null) {
            return ResponseEntity.badRequest().body("❌ Invalid request payload");
        }

        String responseMessage = body.equals("book")
                ? bookingService.startBookingSession(from)
                : bookingService.handleUserResponse(from, body);

        twiloService.sendMessage(from.replace("whatsapp:", ""), responseMessage);
        return ResponseEntity.ok("✅ Message received and responded!");
    }
}
