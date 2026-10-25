import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class LibraryManagementSystem {
    private static final Color NAVY = new Color(13, 45, 83);
    private static final Color NAVY_LIGHT = new Color(20, 65, 115);
    private static final Color BLUE = new Color(35, 112, 225);
    private static final Color BG = new Color(244, 248, 253);
    private static final Color TEXT = new Color(30, 55, 88);
    private static final Color GREEN = new Color(49, 165, 112);
    private static final Color ORANGE = new Color(242, 157, 55);
    private static final Color PURPLE = new Color(124, 88, 220);
    private static final Color RED = new Color(225, 70, 78);

    private final List<Book> books = new ArrayList<>();
    private final List<Member> members = new ArrayList<>();
    private final List<Loan> loans = new ArrayList<>();
    private final List<Reservation> reservations = new ArrayList<>();

    private JFrame frame;
    private JPanel contentPanel;
    private JLabel pageTitle;
    private JLabel totalBooksValue, availableValue, membersValue, issuedValue, overdueValue;
    private DefaultTableModel booksModel, membersModel, loansModel, reservationsModel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibraryManagementSystem().start());
    }

    private void start() {
        seedDemoData();
        buildWindow();
        showDashboard();
    }

    private void seedDemoData() {
        books.add(new Book(1, "Java Programming", "James Gosling", "Programming", 5));
        books.add(new Book(2, "Data Structures", "Mark Allen Weiss", "Computer Science", 4));
        books.add(new Book(3, "Database Systems", "Raghu Ramakrishnan", "Database", 3));
        books.add(new Book(4, "Computer Networks", "Andrew Tanenbaum", "Computer Science", 4));
        books.add(new Book(5, "Operating Systems", "Abraham Silberschatz", "Computer Science", 2));
        books.add(new Book(6, "Artificial Intelligence", "Stuart Russell", "AI", 3));
        books.add(new Book(7, "Machine Learning", "Tom Mitchell", "AI", 4));
        books.add(new Book(8, "Deep Learning", "Ian Goodfellow", "AI", 2));
        books.add(new Book(9, "Computer Graphics", "Foley", "Graphics", 3));

        members.add(new Member(1, "Aarav Patil", "aarav@example.com", "9000000001"));
        members.add(new Member(2, "Ananya Sharma", "ananya@example.com", "9000000002"));
        members.add(new Member(2, "Rohan Gupta", "rohan@example.com", "9000000003"));
        members.add(new Member(2, "Rahul Sharma", "rahul@example.com", "9000000002"));
        members.add(new Member(2, "Gaytri Sarode", "gaytrisarode@example.com", "9000000002"));

        updateBookAvailability();
    }

    private void buildWindow() {
        frame = new JFrame("Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1120, 720));
        frame.setSize(1440, 900);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout(20, 0));
        header.setBackground(new Color(12, 66, 130));
        header.setBorder(new EmptyBorder(15, 24, 15, 24));
        JLabel brand = new JLabel("▣  LIBRARY MANAGEMENT SYSTEM");
        brand.setForeground(Color.WHITE);
        brand.setFont(new Font("SansSerif", Font.BOLD, 22));
        JLabel tagline = new JLabel("Read   |   Learn   |   Grow");
        tagline.setForeground(new Color(218, 231, 249));
        tagline.setFont(new Font("SansSerif", Font.PLAIN, 13));
        JPanel brandBox = new JPanel();
        brandBox.setOpaque(false);
        brandBox.setLayout(new BoxLayout(brandBox, BoxLayout.Y_AXIS));
        brandBox.add(brand);
        brandBox.add(Box.createVerticalStrut(4));
        brandBox.add(tagline);

        JTextField globalSearch = new JTextField(22);
        globalSearch.setFont(new Font("SansSerif", Font.PLAIN, 14));
        globalSearch.setToolTipText("Search books or members");
        JButton searchButton = new JButton("Search");
        styleButton(searchButton, BLUE);
        searchButton.addActionListener(e -> searchEverywhere(globalSearch.getText()));
        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchBox.setOpaque(false);
        searchBox.add(globalSearch);
        searchBox.add(searchButton);
        header.add(brandBox, BorderLayout.WEST);
        header.add(searchBox, BorderLayout.EAST);

        JPanel sidebar = new JPanel();
        sidebar.setBackground(NAVY);
        sidebar.setPreferredSize(new Dimension(235, 0));
        sidebar.setBorder(new EmptyBorder(22, 14, 18, 14));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        // Library brand section
        JLabel logo = new JLabel("  ♧  LIBRARY");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("SansSerif", Font.BOLD, 23));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logo);

        JLabel subtitle = new JLabel("      MANAGEMENT SYSTEM");
        subtitle.setForeground(new Color(170, 198, 232));
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 10));
        subtitle.setBorder(new EmptyBorder(6, 0, 18, 0));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(subtitle);

        // Divider
        JPanel line = new JPanel();
        line.setBackground(new Color(54, 83, 119));
        line.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        line.setPreferredSize(new Dimension(190, 1));
        sidebar.add(line);
        sidebar.add(Box.createVerticalStrut(18));

        // Navigation heading
        JLabel menuLabel = new JLabel("  MAIN MENU");
        menuLabel.setForeground(new Color(145, 174, 210));
        menuLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        menuLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(menuLabel);
        sidebar.add(Box.createVerticalStrut(12));

        String[][] navItems = {
                { "⌂", "Dashboard" },
                { "▣", "Book Management" },
                { "♙", "Member Registration" },
                { "↔", "Issue / Return" },
                { "☆", "Reservations" },
                { "!", "Overdue Alerts" },
                { "▤", "Reports" }
        };

        final JButton[] activeButton = { null };

        for (String[] item : navItems) {
            JButton button = new JButton(item[0] + "    " + item[1]);

            button.setHorizontalAlignment(SwingConstants.LEFT);
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
            button.setPreferredSize(new Dimension(205, 46));
            button.setFont(new Font("SansSerif", Font.PLAIN, 13));
            button.setForeground(Color.WHITE);
            button.setBackground(NAVY);
            button.setFocusPainted(false);
            button.setBorder(new EmptyBorder(0, 14, 0, 6));
            button.setCursor(Cursor.getPredefinedCursor(
                    Cursor.HAND_CURSOR));
            button.setAlignmentX(Component.LEFT_ALIGNMENT);

            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    button.setBackground(NAVY_LIGHT);
                }

                public void mouseExited(java.awt.event.MouseEvent e) {
                    button.setBackground(
                            activeButton[0] == button ? BLUE : NAVY);
                }
            });

            button.addActionListener(e -> {
                if (activeButton[0] != null) {
                    activeButton[0].setBackground(NAVY);
                }

                activeButton[0] = button;
                button.setBackground(BLUE);
                navigate(item[1]);
            });

            sidebar.add(button);
            sidebar.add(Box.createVerticalStrut(7));
        }

        // Push footer to bottom
        sidebar.add(Box.createVerticalGlue());

        JPanel footer = new JPanel();
        footer.setOpaque(false);
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));

        JLabel footerLine = new JLabel("________________________");
        footerLine.setForeground(new Color(54, 83, 119));

        JLabel quote = new JLabel(
                "<html><center>READ  •  LEARN  •  GROW<br><br>" +
                        "A library opens a world<br>of possibilities.</center></html>");
        quote.setForeground(new Color(190, 210, 237));
        quote.setFont(new Font("SansSerif", Font.ITALIC, 12));
        quote.setAlignmentX(Component.CENTER_ALIGNMENT);

        footer.add(footerLine);
        footer.add(Box.createVerticalStrut(15));
        footer.add(quote);
        sidebar.add(footer);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BG);
        contentPanel.setBorder(new EmptyBorder(22, 24, 22, 24));
        frame.add(header, BorderLayout.NORTH);
        frame.add(sidebar, BorderLayout.WEST);
        frame.add(contentPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void navigate(String page) {
        switch (page) {
            case "Dashboard" -> showDashboard();
            case "Book Management" -> showBooks();
            case "Member Registration" -> showMembers();
            case "Issue / Return" -> showLoans();
            case "Reservations" -> showReservations();
            case "Overdue Alerts" -> showOverdue();
            case "Reports" -> showReports();
        }
    }

    private void setPage(String title, JComponent component) {
        contentPanel.removeAll();
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        pageTitle = new JLabel(title);
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
        pageTitle.setForeground(TEXT);
        top.add(pageTitle, BorderLayout.WEST);
        top.setBorder(new EmptyBorder(0, 0, 18, 0));
        contentPanel.add(top, BorderLayout.NORTH);
        contentPanel.add(component, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showDashboard() {
        JPanel dashboard = new JPanel(new BorderLayout(0, 18));
        dashboard.setOpaque(false);

        JPanel stats = new JPanel(new GridLayout(1, 5, 14, 0));
        stats.setOpaque(false);
        totalBooksValue = new JLabel();
        availableValue = new JLabel();
        membersValue = new JLabel();
        issuedValue = new JLabel();
        overdueValue = new JLabel();
        stats.add(statCard("▣", "Total Books", totalBooksValue, BLUE, "Titles in collection"));
        stats.add(statCard("▤", "Available Copies", availableValue, GREEN, "Copies ready to issue"));
        stats.add(statCard("♙", "Registered Members", membersValue, PURPLE, "Library members"));
        stats.add(statCard("▧", "Issued Books", issuedValue, ORANGE, "Currently on loan"));
        stats.add(statCard("!", "Overdue Books", overdueValue, RED, "Past due date"));
        updateStats();

        JPanel lower = new JPanel(new GridLayout(2, 1, 0, 14));
        lower.setOpaque(false);
        lower.add(sectionPanel("Recent Issued Books", loanTablePanel(false)));
        lower.add(sectionPanel("Upcoming Due Dates", loanTablePanel(true)));

        JPanel right = new JPanel(new GridLayout(2, 1, 0, 14));
        right.setOpaque(false);
        right.add(sectionPanel("Book Categories", categoryPanel()));
        right.add(sectionPanel("Quick Actions", quickActions()));

        JPanel body = new JPanel(new BorderLayout(14, 0));
        body.setOpaque(false);
        body.add(lower, BorderLayout.CENTER);
        body.add(right, BorderLayout.EAST);
        right.setPreferredSize(new Dimension(360, 0));
        dashboard.add(stats, BorderLayout.NORTH);
        dashboard.add(body, BorderLayout.CENTER);
        setPage("⌂  Dashboard", dashboard);
    }

    private JPanel statCard(String icon, String title, JLabel value, Color accent, String subtitle) {
        JPanel card = new JPanel(new BorderLayout(10, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(new Color(220, 230, 242)), new EmptyBorder(15, 14, 15, 14)));
        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setOpaque(true);
        iconLabel.setBackground(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 28));
        iconLabel.setForeground(accent);
        iconLabel.setFont(new Font("SansSerif", Font.BOLD, 25));
        iconLabel.setPreferredSize(new Dimension(45, 45));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(TEXT);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        value.setForeground(accent);
        value.setFont(new Font("SansSerif", Font.BOLD, 28));
        JLabel sub = new JLabel(subtitle);
        sub.setForeground(new Color(105, 125, 151));
        sub.setFont(new Font("SansSerif", Font.PLAIN, 11));
        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(titleLabel);
        text.add(Box.createVerticalStrut(6));
        text.add(value);
        text.add(Box.createVerticalStrut(4));
        text.add(sub);
        card.add(iconLabel, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);
        return card;
    }

    private JPanel sectionPanel(String title, JComponent inner) {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(new LineBorder(new Color(220, 230, 242)), new EmptyBorder(15, 15, 15, 15)));
        JLabel label = new JLabel(title);
        label.setForeground(new Color(18, 65, 125));
        label.setFont(new Font("SansSerif", Font.BOLD, 17));
        panel.add(label, BorderLayout.NORTH);
        panel.add(inner, BorderLayout.CENTER);
        return panel;
    }
    
    private void applyTableStyle(JTable table) {

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(
                SwingConstants.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(centerRenderer);
        }

        // Center column headings also
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) table
                .getTableHeader()
                .getDefaultRenderer();

        headerRenderer.setHorizontalAlignment(
                SwingConstants.CENTER);
    }

    private JPanel loanTablePanel(boolean upcoming) {
        String[] columns = upcoming
                ? new String[] { "Book", "Member", "Due Date", "Days Left" }
                : new String[] { "Book", "Member", "Issue Date", "Due Date", "Status" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        for (Loan loan : loans) {
            if (loan.returned)
                continue;
            Book b = findBook(loan.bookId);
            Member m = findMember(loan.memberId);
            if (b == null || m == null)
                continue;
            long days = ChronoUnit.DAYS.between(LocalDate.now(), loan.dueDate);
            if (upcoming) {
                if (days >= 0)
                    model.addRow(new Object[] { b.title, m.name, loan.dueDate, days });
            } else {
                model.addRow(new Object[] { b.title, m.name, loan.issueDate, loan.dueDate,
                        days < 0 ? "OVERDUE" : "Issued" });
            }
        }
        JTable table = makeTable(model);
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.add(new JScrollPane(table), BorderLayout.CENTER);
        // DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

        // centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // for (int i = 0; i < table.getColumnCount(); i++) {
        //     table.getColumnModel()
        //             .getColumn(i)
        //             .setCellRenderer(centerRenderer);
        // }
        return wrap;
    }

    private JPanel categoryPanel() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        java.util.Map<String, Integer> counts = new java.util.LinkedHashMap<>();
        for (Book b : books)
            counts.put(b.category, counts.getOrDefault(b.category, 0) + 1);
        for (String category : counts.keySet()) {
            JPanel row = new JPanel(new BorderLayout(8, 0));
            row.setOpaque(false);
            JLabel dot = new JLabel("●");
            dot.setForeground(BLUE);
            JLabel name = new JLabel(category);
            name.setForeground(TEXT);
            JLabel count = new JLabel(String.valueOf(counts.get(category)));
            count.setForeground(TEXT);
            row.add(dot, BorderLayout.WEST);
            row.add(name, BorderLayout.CENTER);
            row.add(count, BorderLayout.EAST);
            row.setBorder(new EmptyBorder(9, 4, 9, 4));
            p.add(row);
        }
        return p;
    }

    private JPanel quickActions() {
        JPanel p = new JPanel(new GridLayout(2, 2, 8, 8));
        p.setOpaque(false);
        JButton addBook = actionTile("＋ Add Book", new Color(229, 240, 255), BLUE);
        JButton addMember = actionTile("♙ Register Member", new Color(226, 248, 244), GREEN);
        JButton issue = actionTile("↗ Issue Book", new Color(255, 243, 225), ORANGE);
        JButton report = actionTile("▤ Generate Report", new Color(239, 233, 255), PURPLE);
        addBook.addActionListener(e -> addBookDialog());
        addMember.addActionListener(e -> addMemberDialog());
        issue.addActionListener(e -> issueBookDialog());
        report.addActionListener(e -> showReports());
        p.add(addBook);
        p.add(addMember);
        p.add(issue);
        p.add(report);
        return p;
    }

    private JButton actionTile(String text, Color bg, Color fg) {
        JButton b = new JButton("<html><center>" + text + "</center></html>");
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorder(new LineBorder(new Color(220, 230, 242)));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void showBooks() {
        booksModel = new DefaultTableModel(
                new String[] { "ID", "Title", "Author", "Category", "Total Copies", "Available" }, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        refreshBooksModel();
        JTable table = makeTable(booksModel);
        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setOpaque(false);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.setOpaque(false);
        JTextField search = new JTextField(18);
        JButton find = new JButton("Search"), add = new JButton("Add Book"), edit = new JButton("Edit"),
                del = new JButton("Delete");
        styleButton(find, NAVY_LIGHT);
        styleButton(add, GREEN);
        styleButton(edit, ORANGE);
        styleButton(del, RED);
        find.addActionListener(e -> filterTable(booksModel, search.getText(), 1, 2, 3));
        add.addActionListener(e -> addBookDialog());
        edit.addActionListener(e -> editSelectedBook(table));
        del.addActionListener(e -> deleteSelectedBook(table));
        actions.add(new JLabel("Search:"));
        actions.add(search);
        actions.add(find);
        actions.add(add);
        actions.add(edit);
        actions.add(del);
        root.add(actions, BorderLayout.NORTH);
        root.add(new JScrollPane(table), BorderLayout.CENTER);
        setPage("▣  Book Management", root);

        // DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

        // centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // for (int i = 0; i < table.getColumnCount(); i++) {
        //     table.getColumnModel()
        //             .getColumn(i)
        //             .setCellRenderer(centerRenderer);
        // }
    }

    private void showMembers() {
        membersModel = new DefaultTableModel(new String[] { "ID", "Name", "Email", "Phone" }, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        refreshMembersModel();
        JTable table = makeTable(membersModel);
        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setOpaque(false);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.setOpaque(false);
        JTextField search = new JTextField(20);
        JButton find = new JButton("Search"), add = new JButton("Register Member"), del = new JButton("Delete");
        styleButton(find, NAVY_LIGHT);
        styleButton(add, GREEN);
        styleButton(del, RED);
        find.addActionListener(e -> filterTable(membersModel, search.getText(), 1, 2, 3));
        add.addActionListener(e -> addMemberDialog());
        del.addActionListener(e -> deleteSelectedMember(table));
        actions.add(new JLabel("Search:"));
        actions.add(search);
        actions.add(find);
        actions.add(add);
        actions.add(del);
        root.add(actions, BorderLayout.NORTH);
        root.add(new JScrollPane(table), BorderLayout.CENTER);
        setPage("♙  Member Registration", root);
        // DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

        // centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // for (int i = 0; i < table.getColumnCount(); i++) {
        //     table.getColumnModel()
        //             .getColumn(i)
        //             .setCellRenderer(centerRenderer);

    }

    private void showLoans() {
        loansModel = new DefaultTableModel(new String[] { "Loan ID", "Book", "Member", "Issue Date", "Due Date",
                "Return Date", "Fine (₹)", "Status" }, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        refreshLoansModel();
        JTable table = makeTable(loansModel);
        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setOpaque(false);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.setOpaque(false);
        JButton issue = new JButton("Issue Book"), ret = new JButton("Return Selected Book");
        styleButton(issue, GREEN);
        styleButton(ret, BLUE);
        issue.addActionListener(e -> issueBookDialog());
        ret.addActionListener(e -> returnSelectedLoan(table));
        actions.add(issue);
        actions.add(ret);
        root.add(actions, BorderLayout.NORTH);
        root.add(new JScrollPane(table), BorderLayout.CENTER);
        setPage("↔  Issue / Return", root);
        // DefaultTableCellRenderer centerRenderer =
        //         new DefaultTableCellRenderer();
        // centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        // for (int i = 0; i < table.getColumnCount(); i++) {
        //     table.getColumnModel()
        //             .getColumn(i)
        //             .setCellRenderer(centerRenderer);

    }

    private void showReservations() {
        reservationsModel = new DefaultTableModel(new String[] { "ID", "Book", "Member", "Date", "Status" }, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        refreshReservationsModel();
        JTable table = makeTable(reservationsModel);
        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setOpaque(false);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.setOpaque(false);
        JButton reserve = new JButton("Add Reservation"), cancel = new JButton("Cancel Selected");
        styleButton(reserve, GREEN);
        styleButton(cancel, RED);
        reserve.addActionListener(e -> addReservationDialog());
        cancel.addActionListener(e -> cancelSelectedReservation(table));
        actions.add(reserve);
        actions.add(cancel);
        root.add(actions, BorderLayout.NORTH);
        root.add(new JScrollPane(table), BorderLayout.CENTER);
        setPage("☆  Reservations", root);
        // DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        // centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // for (int i = 0; i < table.getColumnCount(); i++) {
        //     table.getColumnModel()
        //             .getColumn(i)
        //             .setCellRenderer(centerRenderer);

    }

    private void showOverdue() {
        DefaultTableModel model = new DefaultTableModel(
                new String[] { "Loan ID", "Book", "Member", "Due Date", "Days Late", "Fine (₹)" }, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        for (Loan l : loans) {
            if (!l.returned && l.dueDate.isBefore(LocalDate.now())) {
                Book b = findBook(l.bookId);
                Member m = findMember(l.memberId);
                if (b != null && m != null) {
                    long late = ChronoUnit.DAYS.between(l.dueDate, LocalDate.now());
                    model.addRow(new Object[] { l.id, b.title, m.name, l.dueDate, late, late * 5 });
                }
            }
        }
        JPanel root = new JPanel(new BorderLayout());
        root.setOpaque(false);
        root.add(new JScrollPane(makeTable(model)), BorderLayout.CENTER);
        setPage("!  Overdue Alerts", root);
    }

    private void showReports() {
        JPanel root = new JPanel(new BorderLayout(0, 14));
        root.setOpaque(false);
        JPanel summary = new JPanel(new GridLayout(1, 3, 12, 0));
        summary.setOpaque(false);
        summary.add(reportCard("Total book titles", String.valueOf(books.size())));
        summary.add(reportCard("Members", String.valueOf(members.size())));
        summary.add(reportCard("Total loan records", String.valueOf(loans.size())));
        JTextArea report = new JTextArea();
        report.setEditable(false);
        report.setFont(new Font("Monospaced", Font.PLAIN, 14));
        report.setText(buildReportText());
        root.add(summary, BorderLayout.NORTH);
        root.add(new JScrollPane(report), BorderLayout.CENTER);
        JButton refresh = new JButton("Refresh Report");
        styleButton(refresh, BLUE);
        refresh.addActionListener(e -> report.setText(buildReportText()));
        root.add(refresh, BorderLayout.SOUTH);
        setPage("▤  Reports", root);
    }

    private JPanel reportCard(String title, String value) {
        JPanel p = new JPanel(new GridLayout(2, 1, 0, 5));
        p.setBackground(Color.WHITE);
        p.setBorder(new CompoundBorder(new LineBorder(new Color(220, 230, 242)), new EmptyBorder(15, 15, 15, 15)));
        JLabel t = new JLabel(title);
        t.setForeground(TEXT);
        JLabel v = new JLabel(value);
        v.setForeground(BLUE);
        v.setFont(new Font("SansSerif", Font.BOLD, 25));
        p.add(t);
        p.add(v);
        return p;
    }

    private String buildReportText() {
        StringBuilder s = new StringBuilder("LIBRARY MANAGEMENT SYSTEM REPORT\n");
        s.append("Generated: ").append(LocalDate.now()).append("\n");
        s.append("------------------------------------------\n");
        s.append("Book titles: ").append(books.size()).append("\n");
        s.append("Total copies: ").append(totalCopies()).append("\n");
        s.append("Available copies: ").append(availableCopies()).append("\n");
        s.append("Registered members: ").append(members.size()).append("\n");
        s.append("Active loans: ").append(activeLoans()).append("\n");
        s.append("Overdue loans: ").append(overdueCount()).append("\n\n");
        s.append("            ================= BOOK INVENTORY=================\n");
        s.append("-----------------------------------------------------------------------\n");
        s.append(String.format("%-5s %-25s %-20s %-8s %-10s%n",
                "ID", "Book Title", "Category", "Total", "Available"));
        s.append("----------------------------------------------------------------------\n");

        for (Book b : books) {
            s.append(String.format("%-5d %-25s %-20s %-8d %-10d%n",
                    b.id,
                    b.title,
                    b.category,
                    b.totalCopies,
                    b.availableCopies));
        }
        return s.toString();
    }

    private void addBookDialog() {
        JTextField title = new JTextField(), author = new JTextField(), category = new JTextField("Programming"),
                copies = new JTextField("1");
        JPanel form = formPanel(new String[] { "Title", "Author", "Category", "Total copies" },
                new JComponent[] { title, author, category, copies });
        int result = JOptionPane.showConfirmDialog(frame, form, "Add Book", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION)
            return;
        try {
            if (title.getText().trim().isEmpty() || author.getText().trim().isEmpty())
                throw new IllegalArgumentException("Title and author are required.");
            int count = Integer.parseInt(copies.getText().trim());
            if (count < 1)
                throw new IllegalArgumentException("Copies must be at least 1.");
            books.add(new Book(nextBookId(), title.getText().trim(), author.getText().trim(), category.getText().trim(),
                    count));
            refreshAfterChange("Book added successfully.");
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage() == null ? "Enter a valid copy count." : ex.getMessage());
        }
    }

    private void editSelectedBook(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            showError("Select a book first.");
            return;
        }
        int id = Integer.parseInt(String.valueOf(table.getValueAt(row, 0)));
        Book b = findBook(id);
        if (b == null)
            return;
        JTextField title = new JTextField(b.title), author = new JTextField(b.author),
                category = new JTextField(b.category), copies = new JTextField(String.valueOf(b.totalCopies));
        JPanel form = formPanel(new String[] { "Title", "Author", "Category", "Total copies" },
                new JComponent[] { title, author, category, copies });
        if (JOptionPane.showConfirmDialog(frame, form, "Edit Book",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION)
            return;
        try {
            int count = Integer.parseInt(copies.getText().trim());
            int currentlyOut = b.totalCopies - b.availableCopies;
            if (count < currentlyOut || count < 1)
                throw new IllegalArgumentException("Total copies cannot be less than copies currently issued.");
            b.title = title.getText().trim();
            b.author = author.getText().trim();
            b.category = category.getText().trim();
            b.totalCopies = count;
            b.availableCopies = count - currentlyOut;
            refreshAfterChange("Book updated.");
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage() == null ? "Enter a valid copy count." : ex.getMessage());
        }
    }

    private void deleteSelectedBook(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            showError("Select a book first.");
            return;
        }
        int id = Integer.parseInt(String.valueOf(table.getValueAt(row, 0)));
        boolean active = loans.stream().anyMatch(l -> l.bookId == id && !l.returned);
        if (active) {
            showError("This book has an active issue. Return it before deleting.");
            return;
        }
        if (JOptionPane.showConfirmDialog(frame, "Delete selected book?", "Confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            books.removeIf(b -> b.id == id);
            reservations.removeIf(r -> r.bookId == id);
            refreshAfterChange("Book deleted.");
        }
    }

    private void addMemberDialog() {
        JTextField name = new JTextField(), email = new JTextField(), phone = new JTextField();
        JPanel form = formPanel(new String[] { "Full name", "Email", "Phone" },
                new JComponent[] { name, email, phone });
        if (JOptionPane.showConfirmDialog(frame, form, "Register Member",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION)
            return;
        if (name.getText().trim().isEmpty()) {
            showError("Member name is required.");
            return;
        }
        members.add(new Member(nextMemberId(), name.getText().trim(), email.getText().trim(), phone.getText().trim()));
        refreshAfterChange("Member registered successfully.");
    }

    private void deleteSelectedMember(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            showError("Select a member first.");
            return;
        }
        int id = Integer.parseInt(String.valueOf(table.getValueAt(row, 0)));
        if (loans.stream().anyMatch(l -> l.memberId == id && !l.returned)) {
            showError("This member has an active loan. Return the book first.");
            return;
        }
        if (JOptionPane.showConfirmDialog(frame, "Delete selected member?", "Confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            members.removeIf(m -> m.id == id);
            reservations.removeIf(r -> r.memberId == id);
            refreshAfterChange("Member deleted.");
        }
    }

    private void issueBookDialog() {
        if (books.isEmpty() || members.isEmpty()) {
            showError("Add at least one book and one member first.");
            return;
        }
        JComboBox<String> bookBox = new JComboBox<>();
        for (Book b : books)
            if (b.availableCopies > 0)
                bookBox.addItem(b.id + " - " + b.title + " (" + b.availableCopies + " available)");
        JComboBox<String> memberBox = new JComboBox<>();
        for (Member m : members)
            memberBox.addItem(m.id + " - " + m.name);
        if (bookBox.getItemCount() == 0) {
            showError("No copies are currently available.");
            return;
        }
        JPanel form = formPanel(new String[] { "Book", "Member" }, new JComponent[] { bookBox, memberBox });
        if (JOptionPane.showConfirmDialog(frame, form, "Issue Book",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION)
            return;
        int bookId = Integer.parseInt(bookBox.getSelectedItem().toString().split(" - ")[0]);
        int memberId = Integer.parseInt(memberBox.getSelectedItem().toString().split(" - ")[0]);
        boolean alreadyBorrowed = loans.stream()
                .anyMatch(l -> l.bookId == bookId && l.memberId == memberId && !l.returned);
        if (alreadyBorrowed) {
            showError("This member already has this book issued.");
            return;
        }
        Loan loan = new Loan(nextLoanId(), bookId, LocalDate.now(), LocalDate.now().plusDays(14));
        loan.memberId = memberId;
        loans.add(loan);
        updateBookAvailability();
        refreshAfterChange("Book issued. Due date: " + loan.dueDate);
    }

    private void returnSelectedLoan(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            showError("Select a loan first.");
            return;
        }
        int loanId = Integer.parseInt(String.valueOf(table.getValueAt(row, 0)));
        Loan l = loans.stream().filter(x -> x.id == loanId).findFirst().orElse(null);
        if (l == null || l.returned) {
            showError("This loan is already returned.");
            return;
        }
        l.returned = true;
        l.returnDate = LocalDate.now();
        long lateDays = Math.max(0, ChronoUnit.DAYS.between(l.dueDate, l.returnDate));
        l.fine = lateDays * 5;
        updateBookAvailability();
        refreshAfterChange("Book returned. Fine: ₹" + l.fine);
    }

    private void addReservationDialog() {
        if (books.isEmpty() || members.isEmpty()) {
            showError("Add a book and member first.");
            return;
        }
        JComboBox<String> bookBox = new JComboBox<>(), memberBox = new JComboBox<>();
        for (Book b : books)
            bookBox.addItem(b.id + " - " + b.title);
        for (Member m : members)
            memberBox.addItem(m.id + " - " + m.name);
        JPanel form = formPanel(new String[] { "Book", "Member" }, new JComponent[] { bookBox, memberBox });
        if (JOptionPane.showConfirmDialog(frame, form, "Add Reservation",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION)
            return;
        int bookId = Integer.parseInt(bookBox.getSelectedItem().toString().split(" - ")[0]);
        int memberId = Integer.parseInt(memberBox.getSelectedItem().toString().split(" - ")[0]);
        boolean duplicate = reservations.stream()
                .anyMatch(r -> r.bookId == bookId && r.memberId == memberId && r.status.equals("Waiting"));
        if (duplicate) {
            showError("This member already has a waiting reservation for this book.");
            return;
        }
        reservations.add(new Reservation(nextReservationId(), bookId, memberId, LocalDate.now(),
                findBook(bookId).availableCopies > 0 ? "Available" : "Waiting"));
        refreshAfterChange("Reservation added.");
    }

    private void cancelSelectedReservation(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            showError("Select a reservation first.");
            return;
        }
        int id = Integer.parseInt(String.valueOf(table.getValueAt(row, 0)));
        reservations.removeIf(r -> r.id == id);
        refreshAfterChange("Reservation cancelled.");
    }

    private void searchEverywhere(String query) {
        String q = query.trim().toLowerCase();
        if (q.isEmpty()) {
            showError("Type a book title or member name in the search box.");
            return;
        }
        StringBuilder result = new StringBuilder();
        for (Book b : books)
            if (b.title.toLowerCase().contains(q) || b.author.toLowerCase().contains(q))
                result.append("BOOK: ").append(b.title).append(" — ").append(b.author).append("\\n");
        for (Member m : members)
            if (m.name.toLowerCase().contains(q) || m.email.toLowerCase().contains(q))
                result.append("MEMBER: ").append(m.name).append(" — ").append(m.email).append("\\n");
        JOptionPane.showMessageDialog(frame,
                result.length() == 0 ? "No matching book or member found." : result.toString(), "Search Results",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshAfterChange(String message) {
        updateBookAvailability();
        updateStats();
        String current = pageTitle == null ? "Dashboard" : pageTitle.getText();
        if (current.contains("Book Management"))
            showBooks();
        else if (current.contains("Member Registration"))
            showMembers();
        else if (current.contains("Issue / Return"))
            showLoans();
        else if (current.contains("Reservations"))
            showReservations();
        else if (current.contains("Overdue Alerts"))
            showOverdue();
        else if (current.contains("Reports"))
            showReports();
        else
            showDashboard();
        JOptionPane.showMessageDialog(frame, message, "Library Management System", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateStats() {
        if (totalBooksValue == null)
            return;
        totalBooksValue.setText(String.valueOf(books.size()));
        availableValue.setText(String.valueOf(availableCopies()));
        membersValue.setText(String.valueOf(members.size()));
        issuedValue.setText(String.valueOf(activeLoans()));
        overdueValue.setText(String.valueOf(overdueCount()));
    }

    private void updateBookAvailability() {
        for (Book b : books) {
            long out = loans.stream().filter(l -> l.bookId == b.id && !l.returned).count();
            b.availableCopies = Math.max(0, b.totalCopies - (int) out);
        }
        for (Reservation r : reservations) {
            Book b = findBook(r.bookId);
            if (b != null && r.status.equals("Waiting") && b.availableCopies > 0)
                r.status = "Available";
        }
    }

    private void refreshBooksModel() {
        if (booksModel == null)
            return;
        booksModel.setRowCount(0);
        for (Book b : books)
            booksModel.addRow(new Object[] { b.id, b.title, b.author, b.category, b.totalCopies, b.availableCopies });
    }

    private void refreshMembersModel() {
        if (membersModel == null)
            return;
        membersModel.setRowCount(0);
        for (Member m : members)
            membersModel.addRow(new Object[] { m.id, m.name, m.email, m.phone });
    }

    private void refreshLoansModel() {
        if (loansModel == null)
            return;
        loansModel.setRowCount(0);
        for (Loan l : loans) {
            Book b = findBook(l.bookId);
            Member m = findMember(l.memberId);
            if (b == null || m == null)
                continue;
            long late = l.returned ? Math.max(0, ChronoUnit.DAYS.between(l.dueDate, l.returnDate))
                    : Math.max(0, ChronoUnit.DAYS.between(l.dueDate, LocalDate.now()));
            loansModel.addRow(new Object[] { l.id, b.title, m.name, l.issueDate, l.dueDate,
                    l.returned ? l.returnDate : "-", "₹" + (l.returned ? l.fine : late * 5),
                    l.returned ? "Returned" : (late > 0 ? "Overdue" : "Issued") });
        }
    }

    private void refreshReservationsModel() {
        if (reservationsModel == null)
            return;
        reservationsModel.setRowCount(0);
        for (Reservation r : reservations) {
            Book b = findBook(r.bookId);
            Member m = findMember(r.memberId);
            if (b != null && m != null)
                reservationsModel.addRow(new Object[] { r.id, b.title, m.name, r.date, r.status });
        }
    }

    private JPanel formPanel(String[] labels, JComponent[] fields) {
        JPanel p = new JPanel(new GridLayout(labels.length, 2, 10, 10));
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        for (int i = 0; i < labels.length; i++) {
            p.add(new JLabel(labels[i] + ":"));
            p.add(fields[i]);
        }
        return p;
    }

    private JTable makeTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setForeground(TEXT);
        table.setGridColor(new Color(224, 232, 242));
        table.setSelectionBackground(new Color(218, 233, 255));
        table.setSelectionForeground(TEXT);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(236, 243, 252));
        table.getTableHeader().setForeground(TEXT);
        table.setFillsViewportHeight(true);
        applyTableStyle(table);
        return table;
    }

    private void filterTable(DefaultTableModel model, String query, int... columns) {
        String q = query.trim().toLowerCase();
        if (q.isEmpty()) {
            refreshCurrentTableModel(model);
            return;
        }
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            boolean match = false;
            for (int c : columns)
                if (String.valueOf(model.getValueAt(i, c)).toLowerCase().contains(q))
                    match = true;
            if (!match)
                model.removeRow(i);
        }
    }

    private void refreshCurrentTableModel(DefaultTableModel model) {
        if (model == booksModel)
            refreshBooksModel();
        else if (model == membersModel)
            refreshMembersModel();
    }

    private void styleButton(JButton b, Color color) {
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setBorder(new EmptyBorder(8, 12, 8, 12));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Check Input", JOptionPane.WARNING_MESSAGE);
    }

    private int totalCopies() {
        return books.stream().mapToInt(b -> b.totalCopies).sum();
    }

    private int availableCopies() {
        return books.stream().mapToInt(b -> b.availableCopies).sum();
    }

    private long activeLoans() {
        return loans.stream().filter(l -> !l.returned).count();
    }

    private long overdueCount() {
        return loans.stream().filter(l -> !l.returned && l.dueDate.isBefore(LocalDate.now())).count();
    }

    private int nextBookId() {
        return books.stream().mapToInt(b -> b.id).max().orElse(0) + 1;
    }

    private int nextMemberId() {
        return members.stream().mapToInt(m -> m.id).max().orElse(0) + 1;
    }

    private int nextLoanId() {
        return loans.stream().mapToInt(l -> l.id).max().orElse(0) + 1;
    }

    private int nextReservationId() {
        return reservations.stream().mapToInt(r -> r.id).max().orElse(0) + 1;
    }

    private Book findBook(int id) {
        return books.stream().filter(b -> b.id == id).findFirst().orElse(null);
    }

    private Member findMember(int id) {
        return members.stream().filter(m -> m.id == id).findFirst().orElse(null);
    }

    private static class Book {
        int id;
        String title, author, category;
        int totalCopies, availableCopies;

        Book(int id, String title, String author, String category, int copies) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.category = category;
            this.totalCopies = copies;
            this.availableCopies = copies;
        }
    }

    private static class Member {
        int id;
        String name, email, phone;

        Member(int id, String name, String email, String phone) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.phone = phone;
        }
    }

    private static class Loan {
        int id, bookId, memberId;
        LocalDate issueDate, dueDate, returnDate;
        boolean returned = false;
        long fine = 0;

        Loan(int id, int bookId, LocalDate issueDate, LocalDate dueDate) {
            this.id = id;
            this.bookId = bookId;
            this.issueDate = issueDate;
            this.dueDate = dueDate;
        }
    }

    private static class Reservation {
        int id, bookId, memberId;
        LocalDate date;
        String status;

        Reservation(int id, int bookId, int memberId, LocalDate date, String status) {
            this.id = id;
            this.bookId = bookId;
            this.memberId = memberId;
            this.date = date;
            this.status = status;
        }
    }
}
