package com.neoteric.gupshup.service;

import com.neoteric.gupshup.model.BookingSession;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookingService {

    private final Map<String, BookingSession> userSessions = new HashMap<>();
    private final HotelService hotelService;

    public BookingService(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    public String startBookingSession(String from) {
        userSessions.put(from, new BookingSession());
        return "📍 Please select a city:\n" + hotelService.getLocationList();
    }

    public String handleUserResponse(String from, String body) {
        if (!userSessions.containsKey(from)) {
            return "Type 'book' to start hotel booking.";
        }

        BookingSession session = userSessions.get(from);

        if (session.getCity() == null) {
            return hotelService.processCitySelection(session, body);
        } else if (session.getHotel() == null) {
            return hotelService.selectHotel(session, body);
        } else if (session.getRoom() == null) {
            return hotelService.selectRoom(session, body);
        } else if (session.getCheckInDate() == null) {
            session.setCheckInDate(body);
            return "📅 Check-in date saved! Now enter your check-out date (YYYY-MM-DD):";
        } else if (session.getCheckOutDate() == null) {
            session.setCheckOutDate(body);
            userSessions.remove(from);  // Clear session after booking
            return "✅ Booking confirmed!\n\n📍 City: " + session.getCity() +
                    "\n🏨 Hotel: " + session.getHotel() +
                    "\n🛏 Room: " + session.getRoom() +
                    "\n📅 Check-in: " + session.getCheckInDate() +
                    "\n📅 Check-out: " + session.getCheckOutDate();
        } else {
            return "You have already booked. Send 'book' to start a new booking.";
        }
    }
}
