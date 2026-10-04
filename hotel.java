//Develop a Java program that implements a simple hotel room booking system using twodimensional arrays. The system allows users to: View available and booked rooms, Book a
//room by selecting a floor and room number and exit the system when finished
//The hotel has 3 floors, each with 5 rooms.display status in table   

import java.util.Scanner;
public class hotel{
    public static void viewRooms(String[][] rooms) {
        System.out.println("Room Status:");

        System.out.println("\n==================== ROOM STATUS =========================");

            System.out.printf("%-10s %-8s %-12s %-12s %-12s %-12s\n","Floor","Room 1","Room 2","Room 3","Room 4","Room5");
            System.out.println("=============================================================");
     
        for (int i = 0; i < rooms.length; i++) {
            System.out.print((i + 1) + "\t");
            for (int j = 0; j < rooms[i].length; j++) {
                System.out.print(rooms[i][j] + "\t");
            }
            System.out.println();
        }
    }
    public static void bookRoom(String[][] rooms, int floor, int room) {
        if (rooms[floor - 1][room - 1].equals("Available")) {
            rooms[floor - 1][room - 1] = "Booked";
            System.out.println("Room " + room + " on Floor " + floor + " has been booked.");
        } else {
            System.out.println("Room " + room + " on Floor " + floor + " is already booked.");
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[][] rooms = new String[3][5];

        // Initialize all rooms as available
        for (int i = 0; i < rooms.length; i++) {
            for (int j = 0; j < rooms[i].length; j++) {
                rooms[i][j] = "Available";
            }
        }

        while (true) {
            System.out.println("\nHotel Room Booking System");
            System.out.println("1. View Rooms");
            System.out.println("2. Book a Room");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    viewRooms(rooms);
                    break;
                case 2:
                    System.out.print("Enter floor number (1-3): ");
                    int floor = scanner.nextInt();
                    System.out.print("Enter room number (1-5): ");
                    int room = scanner.nextInt();
                    if (floor >= 1 && floor <= 3 && room >= 1 && room <= 5) {
                        bookRoom(rooms, floor, room);
                    } else {
                        System.out.println("Invalid floor or room number.");
                    }
                    break;
                case 3:
                    System.out.println("Exiting the system. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }


}