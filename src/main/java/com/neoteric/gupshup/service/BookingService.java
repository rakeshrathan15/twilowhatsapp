package com.neoteric.gupshup.service;

import com.neoteric.gupshup.model.BookingSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@Service
public class BookingService {

   private final Map<String, BookingSession> userSessions = new HashMap<>();
    private final HotelService hotelService;

    @Autowired
    private TwiloService twiloService;

    public BookingService(HotelService hotelService) {
        this.hotelService = hotelService;


    }


    public String startBookingSession(String from) {
        userSessions.put(from, new BookingSession());
        return "\uD83D\uDCCD Please select a city:\n" + hotelService.getLocationList();
    }

    public String handleUserResponse(String from, String body) {
        if (!userSessions.containsKey(from)) {
            return "🌟 Welcome to Avoota Hotel Booking! 🌟\n" + "\n" + "\n" +
                    "Type 'Book' to start hotel booking.";
        }


        BookingSession session = userSessions.get(from);


        if (session.getCity() == null ) {
            return hotelService.processCitySelection(session, body);
        } else if (session.getHotel() == null) {
            return hotelService.selectHotel(session, body);
        } else if (session.getRoom() == null) {
            return hotelService.selectRoom(session, body);
        } else if (session.getCheckInDate() == null) {
            if (!isValidDate(body)) {

                return "\u26A0\uFE0F Invalid date format! Please enter a valid check-in date (YYYY-MM-DD).";
            }
            LocalDate checkInDate = LocalDate.parse(body);
            if (checkInDate.isBefore(LocalDate.now())) {
                return "\u26A0\uFE0F Check-in date cannot be in the past! Please enter a valid date.";
            }
            session.setCheckInDate(body);
            return "\uD83D\uDCC5 Check-in date saved! Now enter your check-out date (YYYY-MM-DD):";
        } else if (session.getCheckOutDate() == null) {
            if (!isValidDate(body)) {

                return "\u26A0\uFE0F Invalid date format! Please enter a valid check-out date (YYYY-MM-DD).";
            }
            LocalDate checkOutDate = LocalDate.parse(body);
            LocalDate checkInDate = LocalDate.parse(session.getCheckInDate());
            if (checkOutDate.isBefore(checkInDate) || checkOutDate.isEqual(checkInDate)) {
                return "\u26A0\uFE0F Check-out date must be after check-in date! Please enter a valid date.";
            }
            session.setCheckOutDate(body);


            return sendReviewMessage(session);
        } else if (session.getReviewConfirmed() == null) {
            if (body.equalsIgnoreCase("Yes")  || body.equalsIgnoreCase("confirm_booking")) {
                session.setReviewConfirmed(true);
                userSessions.remove(from); // Clear session after booking
                return hotelService.confirmBooking(session);
            } else if (body.equalsIgnoreCase("No") || body.equalsIgnoreCase("edit_booking")) {
                session.setReviewConfirmed(false);
                session.setEditingField(true);
                return "✏️ Which detail would you like to edit? Reply with:\n1️⃣ City\n2️⃣ Hotel\n3️⃣ Room\n4️⃣ Check-in Date\n5️⃣ Check-out Date";
            } else {
                return "⚠️ Please reply with 'Yes' to confirm or 'No' to modify your booking.";
            }
        } else {
            return " You have already booked. Send 'Book' to start a new booking.";
        }
    }

    private boolean isValidDate(String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return true;
        } catch (DateTimeParseException e) {
            return false;


        }
    }

    private String sendReviewMessage(BookingSession session) {
        String reviewMessage = "📄 **Review Your Booking Details:**\n\n"
                + "📍 City: " + session.getCity() + "\n"
                + "🏨 Hotel: " + session.getHotel() + "\n"
                + "🛏 Room: " + session.getRoom() + "\n"
                + "📅 Check-in: " + session.getCheckInDate() + "\n"
                + "📅 Check-out: " + session.getCheckOutDate() + "\n\n"
                + "✅ Type *Yes* to confirm or *No* to modify your booking.";

        String interactiveJson = """
    {
        "messaging_product": "whatsapp",
        "recipient_type": "individual",
        "to": "+917013776567",
        "type": "interactive",
        "interactive": {
            "type": "button",
            "body": {
                "text": "%s"
            },
            "action": {
                "buttons": [
                    {
                        "type": "reply",
                        "reply": {
                            "id": "confirm_booking",
                            "title": "✅ Confirm Booking"
                        }
                    },
                    {
                        "type": "reply",
                        "reply": {
                            "id": "edit_booking",
                            "title": "✏️ Edit Booking"
                        }
                    }
                ]
            }
        }
    }
    """.formatted(reviewMessage);

        twiloService.sendMessage("+917013776567", reviewMessage);
        return reviewMessage;
    }









}
