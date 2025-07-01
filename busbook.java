import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.io.File;

public class busbook extends Frame implements ActionListener {
    public Label seatLabel, nameLabel, ageLabel, contactLabel, sourceLabel, destinationLabel, busTypeLabel;
    private Label welcomeLabel;
    private Choice seatChoice, sourceChoice, destinationChoice, busTypeChoice;
    private TextField nameField, ageField, contactField;
    private Button bookButton, resetButton;
    private  Dialog d;
   
   // private 
    public String ticketDetails; 
    private Map<String, boolean[]> bookedSeats = new HashMap<>();
    private Random random = new Random();

    public busbook() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setTitle("Bus Booking System");
        setSize(600, 600);
        setLayout(null);
        setVisible(true);
        setBackground(new Color(107, 223, 205));

        welcomeLabel = new Label("Welcome to Yatra Bus Services", Label.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setBounds(100, 40, 400, 30);
        add(welcomeLabel);

        seatLabel = new Label("Select a Seat:");
        seatLabel.setBounds(50, 70, 100, 30);
        seatChoice = new Choice();
        for (int i = 1; i <= 10; i++) {
            seatChoice.add("Seat " + i);
        }
        seatChoice.setBounds(160, 70, 100, 30);

        sourceLabel = new Label("Source:");
        sourceLabel.setBounds(50, 120, 100, 30);
        sourceChoice = new Choice();
        String[] gujaratCities = {
                "Ahmedabad", "Surat", "Vadodara", "Rajkot", "Bhavnagar", "Jamnagar",
                "Junagadh", "Gandhinagar", "Anand", "Nadiad", "Porbandar", "Surendranagar",
                "Bharuch", "Patan", "Morbi", "Veraval", "Vapi", "Navsari", "Mehsana", "Bhuj"
        };
        for (String city : gujaratCities) {
            sourceChoice.add(city);
        }
        sourceChoice.setBounds(160, 120, 100, 30);

        destinationLabel = new Label("Destination:");
        destinationLabel.setBounds(50, 170, 100, 30);
        destinationChoice = new Choice();
        for (String city : gujaratCities) {
            destinationChoice.add(city);
        }
        destinationChoice.setBounds(160, 170, 100, 30);

        busTypeLabel = new Label("Bus Type:");
        busTypeLabel.setBounds(50, 220, 100, 30);
        busTypeChoice = new Choice();
        busTypeChoice.add("Gurjarnagari");
        busTypeChoice.add("Express");
        busTypeChoice.add("Sleeper");
        busTypeChoice.add("Volvo");
        busTypeChoice.setBounds(160, 220, 100, 30);



        
        nameLabel = new Label("Name:");
        nameLabel.setBounds(50, 270, 100, 30);
        nameField = new TextField(20);
        nameField.setBounds(160, 270, 200, 30);

        ageLabel = new Label("Age:");
        ageLabel.setBounds(50, 320, 100, 30);
        ageField = new TextField(2);
        ageField.setBounds(160, 320, 50, 30);

        contactLabel = new Label("Contact:");
        contactLabel.setBounds(50, 370, 100, 30);
        contactField = new TextField(10);
        contactField.setBounds(160, 370, 200, 30);

        bookButton = new Button("Book Seat");
        bookButton.setBounds(50, 420, 100, 30);
        bookButton.setBackground(new Color(0, 120, 215));
        bookButton.setForeground(Color.WHITE);

        resetButton = new Button("Reset");
        resetButton.setBounds(160, 420, 100, 30);
        resetButton.setBackground(new Color(255, 69, 58));
        resetButton.setForeground(Color.WHITE);

        add(seatLabel);
        add(seatChoice);
        add(sourceLabel);
        add(sourceChoice);
        add(destinationLabel);
        add(destinationChoice);
        add(busTypeLabel);
        add(busTypeChoice);
        add(nameLabel);
        add(nameField);
        add(ageLabel);
        add(ageField);
        add(contactLabel);
        add(contactField);
        add(bookButton);
        add(resetButton);

        bookButton.addActionListener(this);
        resetButton.addActionListener(this);
    }

    public void actionPerformed(ActionEvent ae) {
        String command = ae.getActionCommand();

        if (command.equals("Book Seat")) {
            int seatNumber = seatChoice.getSelectedIndex();
            String source = sourceChoice.getSelectedItem();
            String destination = destinationChoice.getSelectedItem();
            String busType = busTypeChoice.getSelectedItem();
            String name = nameField.getText();
            String ageText = ageField.getText();
            String contact = contactField.getText();

            if (name.isEmpty() || ageText.isEmpty() || contact.isEmpty()) {
                showErrorDialog("Please fill in all passenger details.");
                return;
            }
            if (name.length() < 2) {
                showErrorDialog("Name must be more than 2 letters.");
                return;
            }
            if (name.matches(".*\\d.*")) {
                showErrorDialog("Please enter a valid name!");
                return;
            }

            int age;
            try {
                age = Integer.parseInt(ageText);
                if (age < 0 || age > 100) {
                    showErrorDialog("Age must be between 0 and 100.");
                    return;
                }
            } catch (NumberFormatException e) {
                showErrorDialog("Please enter a valid number for age.");
                return;
            }

            if (!contact.matches("\\d{10}")) {
                showErrorDialog("Contact number must be exactly 10 digits and contain only numbers.");
                return;
            }

            if (source.equals(destination)) {
                showErrorDialog("Source and Destination cannot be the same.");
                return;
            }

            String routeKey = source + " to " + destination + " - " + busType;

            if (!bookedSeats.containsKey(routeKey)) {
                bookedSeats.put(routeKey, new boolean[10]);
            }

            boolean[] seats = bookedSeats.get(routeKey);

            if (seats[seatNumber]) {
                showErrorDialog("Seat " + (seatNumber + 1) + " is already booked. Please choose another seat.");
            } else {
                seats[seatNumber] = true;
                int farePrice = getFarePrice(busType);
                int pnr = random.nextInt(900000) + 1000000;

                ticketDetails = "Ticket Booked Successfully!\n\n" +
                        "Booking Details:\n" +
                        "--------------------\n" +
                        "PNR Number: G191" + pnr + "\n" +
                        "Seat Number: " + (seatNumber + 1) + "\n" +
                        "Name: " + name + "\n" +
                        "Age: " + age + "\n" +
                        "Contact: " + contact + "\n" +
                        "Source: " + source + "\n" +
                        "Destination: " + destination + "\n" +
                        "Bus Type: " + busType + "\n" +
                        "Fare Price: " + farePrice;

                        showDialogWithDownloadOption("Booking Confirmation", ticketDetails);
            }
        } else if (command.equals("Reset")) {
            bookedSeats.clear();
            nameField.setText("");
            ageField.setText("");
            contactField.setText("");
            showDialogWithDownloadOption("Reset", "All seats and passenger details have been reset.");
        }
    }

    private int getFarePrice(String busType) {
        int minFare = 0;
        int maxFare = 0;

        switch (busType) {
            case "Volvo":
                minFare = 2000;
                maxFare = 2700;
                break;
            case "Sleeper":
                minFare = 1200;
                maxFare = 1800;
                break;
            case "Express":
                minFare = 800;
                maxFare = 1200;
                break;
            case "Gurjarnagari":
                minFare = 400;
                maxFare = 800;
                break;
        }

        return minFare + random.nextInt(maxFare - minFare + 1);
    }

    private void showDialogWithDownloadOption(String title, String message) {
        Dialog dialog = new Dialog(this, title, true);
        d =  new Dialog(dialog,"Info", false);
        dialog.setLayout(new BorderLayout());

        TextArea messageArea = new TextArea(message, 10, 40);
        messageArea.setEditable(false);

        Panel buttonPanel = new Panel();
        Button okButton = new Button("OK");
        Button downloadButton = new Button("Download Ticket");

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        });

        downloadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                d.setSize(350,150);
                d.setLayout(new  BorderLayout());
                Panel p = new Panel();
                p.setLayout(new GridLayout(1,1));
                p.add(new Label("        Ticket has been saved in D://TICKET_DOWNLOAD"));
                d.add(p);
                d.setVisible(true);
                downloadTicket();
                
            }
        });
        d.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                d.dispose();
            }
            
        });

        buttonPanel.add(okButton);
        buttonPanel.add(downloadButton);
        dialog.add(messageArea, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setSize(500, 400);
        dialog.setVisible(true);
    }

    public void downloadTicket() {
        String source = sourceChoice.getSelectedItem();
        String destination = destinationChoice.getSelectedItem();
        int seatNumber = seatChoice.getSelectedIndex() + 1; 

        String uniqueFileName = source + "_to_" + destination + "_Seat_" + seatNumber + "_" + System.currentTimeMillis()
                + ".txt";
        String directoryPath = "D:\\TICKET_DOWNLOAD\\"; 

        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs(); 
        }

        String fullPath = directoryPath + uniqueFileName;

        try (FileWriter writer = new FileWriter(fullPath)) {
            writer.write(ticketDetails); 
        } catch (IOException e) {
            showErrorDialog("Error saving ticket: " + e.getMessage());
        }
    }

    private void showErrorDialog(String message) {
        Dialog dialog = new Dialog(this, "Error", true);
        dialog.setLayout(new FlowLayout());
        Label msgLabel = new Label(message);
        Button okButton = new Button("OK");

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        });

        dialog.add(msgLabel);
        dialog.add(okButton);
        dialog.setSize(400, 200);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        new busbook();
    }
}
