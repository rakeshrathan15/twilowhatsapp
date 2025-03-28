package com.neoteric.gupshup.service;

import com.neoteric.gupshup.model.BookingSession;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class HotelService {
    private static final List<String> locations = Arrays.asList(
            "Hyderabad", "Chennai", "Bangalore", "Mumbai", "Delhi"
    );

    private static final Map<String, List<String>> cityHotels = Map.of(
            "Hyderabad", Arrays.asList("Taj Krishna", "The Park", "Marriott", "Novotel", "Radisson Blu"),
            "Chennai", Arrays.asList("ITC Grand", "Hyatt Regency", "Taj Club House", "The Leela", "Hilton"),
            "Bangalore", Arrays.asList("The Oberoi", "Taj West End", "The Leela Palace", "Sheraton", "JW Marriott"),
            "Mumbai", Arrays.asList("Taj Mahal Palace", "The Oberoi", "Trident", "ITC Maratha", "Sofitel"),
            "Delhi", Arrays.asList("Leela Palace", "ITC Maurya", "The Oberoi", "Shangri-La", "Taj Diplomatic Enclave")
    );

    private static final Map<String, List<String>> hotelRooms = Map.of(
            "Taj Krishna", Arrays.asList("Deluxe Room - ₹5000", "Suite - ₹10000"),
            "The Park", Arrays.asList("Standard - ₹4000", "Luxury Suite - ₹9000"),
            "Marriott", Arrays.asList("Executive - ₹6000", "Presidential Suite - ₹12000")
    );

    public String getLocationList() {
        StringBuilder list = new StringBuilder();
        for (int i = 0; i < locations.size(); i++) {
            list.append(i + 1).append("️⃣ ").append(locations.get(i)).append("\n");
        }
        return list.append("\nReply with the number (1-").append(locations.size()).append(").").toString();
    }

    public String processCitySelection(BookingSession session, String body) {
        try {
            int choice = Integer.parseInt(body);
            if (choice >= 1 && choice <= locations.size()) {
                String city = locations.get(choice - 1);
                session.setCity(city);
                return "🏨 Select a hotel in " + city + ":\n" + getHotelList(city);
            } else {
                return "⚠️ Invalid choice! Reply with a number from the list.";
            }
        } catch (NumberFormatException e) {
            return "⚠️ Invalid input! Please enter a number.";
        }
    }

    private String getHotelList(String city) {
        List<String> hotels = cityHotels.getOrDefault(city, Collections.emptyList());
        StringBuilder list = new StringBuilder();
        for (int i = 0; i < hotels.size(); i++) {
            list.append(i + 1).append("️⃣ ").append(hotels.get(i)).append("\n");
        }
        return list.append("\nReply with the number (1-").append(hotels.size()).append(").").toString();
    }

    public String selectHotel(BookingSession session, String body) {
        try {
            int choice = Integer.parseInt(body);
            List<String> hotels = cityHotels.get(session.getCity());
            if (choice >= 1 && choice <= hotels.size()) {
                String hotel = hotels.get(choice - 1);
                session.setHotel(hotel);
                return "🛏 Select a room type in " + hotel + ":\n" + getRoomList(hotel);
            } else {
                return "⚠️ Invalid choice! Reply with a number from the list.";
            }
        } catch (NumberFormatException e) {
            return "⚠️ Invalid input! Please enter a number.";


        }
    }

    private String getRoomList(String hotel) {
        List<String> rooms = hotelRooms.getOrDefault(hotel, Collections.emptyList());
        StringBuilder list = new StringBuilder();
        for (int i = 0; i < rooms.size(); i++) {
            list.append(i + 1).append("️⃣ ").append(rooms.get(i)).append("\n");
        }
        return list.append("\nReply with the number (1-").append(rooms.size()).append(").").toString();
    }

    public String selectRoom(BookingSession session, String body) {
        try {
            int choice = Integer.parseInt(body);
            List<String> rooms = hotelRooms.get(session.getHotel());
            if (choice >= 1 && choice <= rooms.size()) {
                session.setRoom(rooms.get(choice - 1));
                return "📅 Please enter your check-in date (YYYY-MM-DD):";
            } else {
                return "⚠️ Invalid choice! Reply with a number from the list.";
            }
        } catch (NumberFormatException e) {
            return "⚠️ Invalid input! Please enter a number.";
        }
    }

    public String confirmBooking(BookingSession session){
        return "✅ Booking confirmed!\n\n📍 City: " + session.getCity() +
                "\n🏨 Hotel: " + session.getHotel() +
                "\n🛏 Room: " + session.getRoom() +
                "\n📅 Check-in: " + session.getCheckInDate() +
                "\n📅 Check-out: " + session.getCheckOutDate();
    }





}
