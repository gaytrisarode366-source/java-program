import java.util.*;

class Library {

    private final List<Book> books = new ArrayList<>();
    private final List<Member> members = new ArrayList<>();
    private final List<IssueRecord> issueRecords = new ArrayList<>();
    private final List<IssueRecord> historyRecords = new ArrayList<>();

    private static final int BORROWING_DAYS = 14;
    private static final double FINE_PER_DAY = 1.0;

    // Add book
    public void addBook(Book book) {
        if (findBookByIsbn(book.getIsbn()) != null) {
            System.out.println("Book with this ISBN already exists.");
            return;
        }

        books.add(book);
        System.out.println("Book added successfully.");
    }

    // 1. Display all books
    public void listAllBooks() {
        System.out.println("\n======================================= ALL BOOKS ===========================================");

        System.out.printf("%-12s %-18s %-14s %-10s %-18s %-9s%n",
                "ISBN", "Book Title", "Author", "Edition",
                "Category", "Available");

        System.out.println("-".repeat(98));

        for (Book book : books) {
            System.out.printf("%-12s %-18s %-14s %-10s %-18s %-9s%n",
                    book.getIsbn(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getEdition(),
                    book.getCategory(),
                    book.getAvailableCopies());
        }

        System.out.println("-".repeat(98));
    }

    // 2. Display book details
    public void displayBookDetails(String isbn) {
        Book book = findBookByIsbn(isbn);

        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        System.out.println("\n======== BOOK DETAILS ==========");
        System.out.println("ISBN             : " + book.getIsbn());
        System.out.println("Title            : " + book.getTitle());
        System.out.println("Author           : " + book.getAuthor());
        System.out.println("Edition          : " + book.getEdition());
        System.out.println("Category         : " + book.getCategory());
        System.out.println("Available Copies : " + book.getAvailableCopies());
        System.out.println("====================================");
    }

    // 3. Issue book
    public void issueBook(String isbn, String memberId) {
        Book book = findBookByIsbn(isbn);
        Member member = findMemberById(memberId);

        if (book == null || member == null) {
            System.out.println("Invalid ISBN or member ID.");
            return;
        }

        if (book.getAvailableCopies() <= 0) {
            System.out.println("Book is not available.");
            return;
        }

        // Prevent duplicate issue of the same book to the same member
        if (findActiveRecord(book, member) != null) {
            System.out.println("This member already has this book.");
            return;
        }

        book.decreaseAvailableCopies();

        IssueRecord record = new IssueRecord(book, member, new Date());
        issueRecords.add(record);
        historyRecords.add(record);

        System.out.println("Book issued successfully.");
    }

    // 4. Calculate fine before returning a book
    public void calculateFine(String isbn, String memberId, Date returnDate) {
        IssueRecord record = findRecord(isbn, memberId);

        if (record == null) {
            System.out.println("No issue record found for this book and member.");
            return;
        }

        printFine(record, returnDate);
    }

    private void printFine(IssueRecord record, Date returnDate) {
        long difference = returnDate.getTime()
                - record.getIssueDate().getTime();

        long diffInDays = Math.max(0, difference / (1000L * 60 * 60 * 24));

        if (diffInDays > BORROWING_DAYS) {
            double fine = (diffInDays - BORROWING_DAYS) * FINE_PER_DAY;
            System.out.printf("Late return fine: $%.2f%n", fine);
        } else {
            System.out.println("No fine. Book returned on time.");
        }
    }

    // 5. Return book using current date
    public void returnBook(String isbn, String memberId) {
        returnBook(isbn, memberId, new Date());
    }

    // Return book using a specified date (also useful for testing)
    public void returnBook(String isbn, String memberId, Date returnDate) {
        IssueRecord record = findRecord(isbn, memberId);

        if (record == null) {
            System.out.println("Cannot return book. No active issue record found.");
            return;
        }

        Book book = record.getBook();

        // Calculate fine before removing the active issue
        printFine(record, returnDate);

        record.setReturnDate(returnDate);
        issueRecords.remove(record);
        book.increaseAvailableCopies();

        System.out.println("Book returned successfully.");

        // Give the returned copy to the next reserved member
        Member nextMember = book.getNextReservation();

        if (nextMember != null) {
            book.decreaseAvailableCopies();

            IssueRecord nextRecord =
                    new IssueRecord(book, nextMember, returnDate);

            issueRecords.add(nextRecord);
            historyRecords.add(nextRecord);

            System.out.println("Reserved book issued to: "
                    + nextMember.getName());
        }
    }

    // 6. Generate issue/return report
    public void generateReport(Date startDate, Date endDate) {
        if (startDate.after(endDate)) {
            System.out.println("Invalid date range.");
            return;
        }

        System.out.println("\n========== ISSUE / RETURN REPORT ==========");
        System.out.println("From: " + startDate);
        System.out.println("To  : " + endDate);

        boolean found = false;

        for (IssueRecord record : historyRecords) {
            Date issueDate = record.getIssueDate();
            Date returnDate = record.getReturnDate();

            boolean issueInRange =
                    !issueDate.before(startDate) && !issueDate.after(endDate);

            boolean returnInRange = returnDate != null
                    && !returnDate.before(startDate)
                    && !returnDate.after(endDate);

            if (issueInRange || returnInRange) {
                System.out.println(record);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No records found for this date range.");
        }
    }

    // 7. Register member
    public void registerMember(String id, String name) {
        if (id == null || id.trim().isEmpty()
                || name == null || name.trim().isEmpty()) {
            System.out.println("Member ID and name cannot be empty.");
            return;
        }

        if (findMemberById(id) != null) {
            System.out.println("Member ID already exists.");
            return;
        }

        members.add(new Member(id, name));
        System.out.println("Member registered successfully.");
    }

    // 8. Reserve a book
    public void reserveBook(String isbn, String memberId) {
        Book book = findBookByIsbn(isbn);
        Member member = findMemberById(memberId);

        if (book == null || member == null) {
            System.out.println("Invalid ISBN or member ID.");
            return;
        }

        if (findActiveRecord(book, member) != null) {
            System.out.println("Member already has this book.");
            return;
        }

        if (book.hasReservation(member)) {
            System.out.println("Member has already reserved this book.");
            return;
        }

        if (book.getAvailableCopies() > 0) {
            System.out.println("Book is available. You can issue it directly.");
            return;
        }

        book.addToReservationQueue(member);
        System.out.println("Book reserved successfully.");
    }

    // 9. Search books by category
    public void searchByCategory(String category) {
        System.out.println("=========== CATEGORY: " + category + " ==========");

        boolean found = false;

        for (Book book : books) {
            if (book.getCategory().equalsIgnoreCase(category)) {
                System.out.println(book);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No books found in this category.");
        }
    }

    // 10. Check overdue alerts
    public void checkOverdueAlerts() {
        Date currentDate = new Date();
        boolean found = false;

        System.out.println("========== OVERDUE ALERTS ==========");

        for (IssueRecord record : issueRecords) {
            long difference = currentDate.getTime()
                    - record.getIssueDate().getTime();

            long days = difference / (1000L * 60 * 60 * 24);

            if (days > BORROWING_DAYS) {
                System.out.println("OVERDUE: "
                        + record.getMember().getName()
                        + " | Book: " + record.getBook().getTitle()
                        + " | Days borrowed: " + days
                        + " | Late days: " + (days - BORROWING_DAYS));

                found = true;
            }
        }

        if (!found) {
            System.out.println("No overdue books found.");
        }
    }

    // 11. Display available copies
    public void displayAvailableCopies() {
        System.out.println("\n========== AVAILABLE COPIES ==========");

        for (Book book : books) {
            System.out.printf("%-12s %-12s Available: %d%n",
                    book.getIsbn(),
                    book.getTitle(),
                    book.getAvailableCopies());
        }
    }

    // 12. Find book by ISBN
    private Book findBookByIsbn(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        return null;
    }

    // 13. Find member by ID
    private Member findMemberById(String memberId) {
        for (Member member : members) {
            if (member.getId().equals(memberId)) {
                return member;
            }
        }
        return null;
    }

    // Find active issue record
    private IssueRecord findRecord(String isbn, String memberId) {
        for (IssueRecord record : issueRecords) {
            if (record.getBook().getIsbn().equals(isbn)
                    && record.getMember().getId().equals(memberId)) {
                return record;
            }
        }
        return null;
    }

    private IssueRecord findActiveRecord(Book book, Member member) {
        for (IssueRecord record : issueRecords) {
            if (record.getBook() == book && record.getMember() == member) {
                return record;
            }
        }
        return null;
    }

    // Main method
    public static void main(String[] args) {
        Library library = new Library();

        library.addBook(new Book(
                "1234567890", "Book 1", "Author A", "1st", 5, "Fiction"));

        library.addBook(new Book(
                "0987654321", "Book 2", "Author B", "2nd", 3, "Science"));

        library.addBook(new Book(
                "1122334455", "Book 3", "Author C", "1st", 2, "History"));

        library.addBook(new Book(
                "2233445566", "Book 4", "Author D", "3rd", 4, "Programming"));

        library.registerMember("M001", "John Doe");
        library.registerMember("M002", "Jane Smith");

        library.listAllBooks();

        library.displayBookDetails("1234567890");

        System.out.println("\n--- Issue Book ---");
        library.issueBook("1234567890", "M001");

        // Simulate return after 20 days to demonstrate the fine
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 20);
        Date simulatedReturnDate = calendar.getTime();

        System.out.println("\n--- Calculate Fine ---");
        library.calculateFine(
                "1234567890", "M001", simulatedReturnDate);

        System.out.println("\n--- Return Book ---");
        library.returnBook(
                "1234567890", "M001", simulatedReturnDate);

        System.out.println("\n--- Category Search ---");
        library.searchByCategory("Fiction");

        System.out.println("\n--- Available Copies ---");
        library.displayAvailableCopies();

        System.out.println("\n--- Overdue Alerts ---");
        library.checkOverdueAlerts();

        Calendar start = Calendar.getInstance();
        start.add(Calendar.DAY_OF_MONTH, -30);

        Calendar end = Calendar.getInstance();
        end.add(Calendar.DAY_OF_MONTH, 21);

        library.generateReport(start.getTime(), end.getTime());

        // Interactive reservation example
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("\nEnter ISBN to reserve: ");
            String isbn = scanner.nextLine().trim();

            System.out.print("Enter Member ID to reserve: ");
            String memberId = scanner.nextLine().trim();

            library.reserveBook(isbn, memberId);
        }
    }
}

// Book class
class Book {
    private final String isbn;
    private final String title;
    private final String author;
    private final String edition;
    private int availableCopies;
    private final String category;
    private final Queue<Member> reservationQueue = new LinkedList<>();

    public Book(String isbn, String title, String author,
                String edition, int availableCopies, String category) {

        if (isbn == null || isbn.trim().isEmpty()
                || title == null || title.trim().isEmpty()
                || author == null || edition == null || category == null
                || availableCopies < 0) {
            throw new IllegalArgumentException("Invalid book details.");
        }

        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.edition = edition;
        this.availableCopies = availableCopies;
        this.category = category;
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getEdition() { return edition; }
    public int getAvailableCopies() { return availableCopies; }
    public String getCategory() { return category; }

    public void decreaseAvailableCopies() {
        if (availableCopies <= 0) {
            throw new IllegalStateException("No copies available.");
        }
        availableCopies--;
    }

    public void increaseAvailableCopies() {
        availableCopies++;
    }

    public void addToReservationQueue(Member member) {
        reservationQueue.add(member);
    }

    public boolean hasReservation(Member member) {
        return reservationQueue.contains(member);
    }

    public Member getNextReservation() {
        return reservationQueue.poll();
    }

    @Override
    public String toString() {
        return "ISBN: " + isbn
                + " | Title: " + title
                + " | Author: " + author
                + " | Edition: " + edition
                + " | Category: " + category
                + " | Available: " + availableCopies;
    }
}

// Member class
class Member {
    private final String id;
    private final String name;

    public Member(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return id + " - " + name;
    }
}

// IssueRecord class
class IssueRecord {
    private final Book book;
    private final Member member;
    private final Date issueDate;
    private Date returnDate;

    public IssueRecord(Book book, Member member, Date issueDate) {
        this.book = book;
        this.member = member;
        this.issueDate = new Date(issueDate.getTime());
    }

    public Book getBook() { return book; }
    public Member getMember() { return member; }

    public Date getIssueDate() {
        return new Date(issueDate.getTime());
    }

    public Date getReturnDate() {
        return returnDate == null ? null : new Date(returnDate.getTime());
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = new Date(returnDate.getTime());
    }

    @Override
    public String toString() {
        return "Book: " + book.getTitle()
                + " | Member: " + member.getName()
                + " | Issue Date: " + issueDate
                + " | Return Date: "
                + (returnDate == null ? "Not returned" : returnDate);
    }
}

    
   
