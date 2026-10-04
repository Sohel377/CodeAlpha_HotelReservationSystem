package Java_Project;

import java.io.*;
import java.util.*;

class Room{
    private int roomNumber;
    private String category;
    private double price;
    private boolean available;

    public Room(int roomNumber, String category, double price){
        this.roomNumber = roomNumber;
        this.category = category;
        this.price = price;
        this.available = true;
    }

    public int getRoomNumber(){
        return roomNumber;
    }
    public String getCategory(){
        return category;
    }
    public double getPrice(){
        return price;
    }
    public boolean isAvailable(){
        return available;
    }
    public void setAvailable(boolean available){
        this.available = available;
    }

    public void displayRoom(){
        System.out.printf("%-10d %-15s %-10.2f %-10s\n", roomNumber,category,price,available ? "Available" : "Booked");
    }

}

class Booking{
    private String bookingId;
    private String customerName;
    private String phone;
    private int roomNumber;
    private String category;
    private double amount;

    public Booking(String bookingId, String customerName, String phone, int roomNumber, String category, double amount){
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.phone = phone;
        this.roomNumber = roomNumber;
        this.category = category;
        this.amount = amount;
    }

    public String getBookingId(){
        return bookingId;
    }
    public int getRoomNumber(){
        return roomNumber;
    }
    public String toFileString(){
        return bookingId+","+customerName+","+phone+","+roomNumber+","+category+","+amount;
    }
    public void displayBooking(){
        System.out.println("===== BOOKING DETAILS =====");
        System.out.println("Booking ID : "+ bookingId);
        System.out.println("Customer Name : "+ customerName);
        System.out.println("Phone : "+ phone);
        System.out.println("Room Number : "+ roomNumber);
        System.out.println("Category : "+ category);
        System.out.println("Amount : "+ amount);
    }

}

class Hotel{
    ArrayList<Room> rooms = new ArrayList<>();
    ArrayList<Booking> bookings = new ArrayList<>();
    final String FILE_NAME = "bookings.txt";
    int bookingCounter = 1001;

    public Hotel(){
        initializeRooms();
        loadBookings();
    }

    private void initializeRooms(){
        rooms.add(new Room(101,"Standard",1000));
        rooms.add(new Room(102,"Standard", 1000));
        rooms.add(new Room(201,"Deluxe", 2000));
        rooms.add(new Room(202,"Deluxe", 2000));
        rooms.add(new Room(301,"Suite", 3000));
        rooms.add(new Room(302,"Suite", 3000));
    }
    public void viewRooms(){
        System.out.println("\n================== ROOM LIST ==================");
        System.out.println("-----------------------------------------------");
        System.out.printf("%-10s %-15s %-10s %-10s\n", "Room No", "Category", "Price", "Status");
        System.out.println("-----------------------------------------------");
        for(Room room : rooms) room.displayRoom();
    }

    public void searchRoom(String category){
        boolean found = false;
        System.out.println("\nAvailable "+ category +" Rooms : ");
        System.out.println("-----------------------------------------------");
        System.out.printf("%-10s %-15s %-10s %-10s\n", "Room No", "Category", "Price", "Status");
        System.out.println("-----------------------------------------------");
        for(Room room : rooms){
            if(room.getCategory().equalsIgnoreCase(category) && room.isAvailable()){
                room.displayRoom();
                found = true;
            }
        }
        if(!found){
            System.out.println("No rooms available");
        }
    }

    public void bookRoom(Scanner sc){
        System.out.print("Enter Customer Name : ");
        String name = sc.nextLine();

        System.out.print("Enter Phone Number : ");
        String phone = sc.nextLine();

        System.out.print("Enter Room Number : ");
        int roomNo = Integer.parseInt(sc.nextLine());

        Room selectedRoom = null;
        for(Room room : rooms){
            if(room.getRoomNumber() == roomNo){
                selectedRoom = room;
                break;
            }
        }

        if(selectedRoom == null){
            System.out.println("Room not found");
            return;
        }
        if(!selectedRoom.isAvailable()){
            System.out.println("Room already booked");
            return;
        }

        System.out.println("\nPayment Methods");
        System.out.println("1. UPI");
        System.out.println("2. Card");
        System.out.println("3. Cash");

        System.out.print("Choose Payment Method : ");
        int paymentChoice = Integer.parseInt(sc.nextLine());

        System.out.println("Processing payment...");
        System.out.println("Payment Successful!");

        String bookingId = "B"+bookingCounter++;

        Booking booking = new Booking(bookingId, name, phone,
                selectedRoom.getRoomNumber(),
                selectedRoom.getCategory(),
                selectedRoom.getPrice());

        bookings.add(booking);
        selectedRoom.setAvailable(false);

        System.out.println("\nBooking Successfull!");

        booking.displayBooking();
    }

    public void viewBooking(String bookingId){
        for(Booking booking : bookings){
            if(booking.getBookingId().equalsIgnoreCase(bookingId)){
                booking.displayBooking();
                return;
            }
        }
        System.out.println("Booking not found");
    }

    public void cancelBooking(String bookingId){
        Iterator<Booking> iterator = bookings.iterator();
        while(iterator.hasNext()){
            Booking booking = iterator.next();
            if(booking.getBookingId().equalsIgnoreCase(bookingId)){
                for(Room room : rooms){
                    if(room.getRoomNumber() == booking.getRoomNumber()){
                        room.setAvailable(true);
                        break;
                    }
                }
                iterator.remove();
                System.out.println("Reservation Cancelled Successfully");
                return;
            }
        }
        System.out.println("Booking Not Found");
    }

    public void saveBookings(){
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(FILE_NAME))){
            for(Booking booking : bookings){
                bw.write(booking.toFileString());
                bw.newLine();
            }
            System.out.println("Data Saved Successfuly");
        } catch (IOException e){
            System.out.println("Error Saving Data");
        }
    }

    public void loadBookings(){
        File file = new File(FILE_NAME);
        if(!file.exists()){
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))){
            String line;
            while((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Booking booking = new Booking(
                        data[0], data[1], data[2], Integer.parseInt(data[3]), data[4], Double.parseDouble(data[5])
                );

                bookings.add(booking);
                for(Room room : rooms){
                    if(room.getRoomNumber() == Integer.parseInt(data[3])){
                        room.setAvailable(false);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error Loading Data");
        }
    }

}
public class Hotel_Reservation_System {
    static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Hotel hotel = new Hotel();

        System.out.println("\n==============================");
        System.out.println("  HOTEL RESERVATION SYSTEM");
        System.out.println("===============================");
        System.out.println("1. View Rooms");
        System.out.println("2. Search Rooms");
        System.out.println("3. Book Rooms");
        System.out.println("4. Cancel Reservation");
        System.out.println("5. View Booking Details");
        System.out.println("6. Save Data");
        System.out.println("7. Exit");

        int choice;
        do{
            System.out.print("\nEnter choice : ");
            choice = Integer.parseInt(sc.nextLine());

            switch(choice){
                case 1:
                    hotel.viewRooms();
                    break;
                case 2:
                    System.out.print("Enter Category (Standard/Deluxe/Suite) : ");
                    String category = sc.nextLine();
                    hotel.searchRoom(category);
                    break;
                case 3:
                    hotel.bookRoom(sc);
                    break;
                case 4:
                    System.out.print("Enter Booking ID : ");
                    String cancelId = sc.nextLine();
                    hotel.cancelBooking(cancelId);
                    break;
                case 5:
                    System.out.print("Enter Booking ID : ");
                    String viewId = sc.nextLine();
                    hotel.viewBooking(viewId);
                    break;
                case 6:
                    hotel.saveBookings();
                    break;
                case 7:
                    hotel.saveBookings();
                    System.out.println("Thank You!");
                    break;
                default:
                    System.out.println("Invalid Choice!");
            }
        } while (choice != 7);
        sc.close();
    }
}
