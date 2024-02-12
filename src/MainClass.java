import java.io.*;
import java.nio.Buffer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class MainClass {
    private String url = "jdbc:mysql://localhost:3306/proiectMES";
    private String timezone = "?serverTimezone=UTC";
    private	String uid = "root";
    private	String pw = "TheHateUGive13$";
    private static Connection con;
    public File file;
    public List<String> notes;

    private FileWriter writer;
    SerialPort activePort;
    private boolean available;
    SerialPort[] ports = SerialPort.getCommPorts();

    public void showAllPort() {
        int i = 0;
        for(SerialPort port : ports) {
            System.out.print(i + ". " + port.getDescriptivePortName() + " ");
            System.out.println(port.getPortDescription());
            i++;
        }
    }

    public void setPort(int portIndex) {
        activePort = ports[portIndex];
        activePort.setBaudRate(115200);

        if (activePort.openPort()) {
            System.out.println(activePort.getPortDescription() + " port opened.");
        }

        activePort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

        activePort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent serialPortEvent) {
                int size = serialPortEvent.getSerialPort().bytesAvailable();
                byte[] buffer = new byte[size];
                serialPortEvent.getSerialPort().readBytes(buffer, size);
                for (byte b : buffer) {
                    System.out.print((char) b);
                    try {
                        if (available == true) {
                            Character c = (char) b;
                            if (c.equals('*')) {
                                System.out.println();
                                System.out.println();

                                try {
                                    available = false;
                                    writer.flush();
                                    writer.close();

                                    Scanner reader = new Scanner(new File("Note.txt"));
                                    Integer note = 0;
                                    Float frequency = Float.valueOf(0);
                                    while (reader.hasNextLine()) {
                                        try {
                                            frequency = reader.nextFloat();
                                            note = reader.nextInt();

                                            String statement = "INSERT INTO note(frecventa, notatie_muz) VALUES (" + frequency +
                                                    ", '" + notes.get(note) + "');";
                                            try {
                                                PreparedStatement stmt = con.prepareStatement(statement);
                                                stmt.execute();

                                            } catch(SQLException ex) {
                                                System.out.println("Error at inserting data into database: " + ex);
                                            }

                                            System.out.println(frequency + " " + notes.get(note));
                                        } catch (Exception e) {
                                            System.out.println("Some values aren't correct");
                                            if(reader.hasNextLine()) {
                                                reader.nextLine();
                                            }
                                        }
                                    }
                                    reader.close();
                                    System.exit(0);

                                } catch (Exception e) {
                                    System.out.println("An error occurred.");
                                    e.printStackTrace();
                                }
                            } else {
                                writer.write((char) b);
                            }
                        }
                    } catch(Exception e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                    }


                }
            }
        });
    }

    public void start() {
        showAllPort();
        Scanner reader = new Scanner(System.in);
        System.out.print("Port? ");
        int p = reader.nextInt();
        setPort(p);
        reader.close();
    }

    public static void main(String[] args) {
        MainClass mainClass = new MainClass();

        mainClass.notes = new ArrayList<>();
        mainClass.notes.add("E2");
        mainClass.notes.add("F2");
        mainClass.notes.add("FS2");
        mainClass.notes.add("G2");
        mainClass.notes.add("GS2");
        mainClass.notes.add("A2");
        mainClass.notes.add("AS2");
        mainClass.notes.add("B2");
        mainClass.notes.add("C3");
        mainClass.notes.add("CS3");
        mainClass.notes.add("D3");
        mainClass.notes.add("DS3");
        mainClass.notes.add("E3");
        mainClass.notes.add("F3");
        mainClass.notes.add("FS3");
        mainClass.notes.add("G3");
        mainClass.notes.add("GS3");
        mainClass.notes.add("A3");
        mainClass.notes.add("AS3");
        mainClass.notes.add("B3");
        mainClass.notes.add("C4");
        mainClass.notes.add("CS4");
        mainClass.notes.add("D4");
        mainClass.notes.add("DS4");
        mainClass.notes.add("E4");
        mainClass.notes.add("F4");
        mainClass.notes.add("FS4");
        mainClass.notes.add("G4");
        mainClass.notes.add("GS4");
        mainClass.notes.add("A4");
        mainClass.notes.add("AS4");
        mainClass.notes.add("B4");
        mainClass.notes.add("C5");
        mainClass.notes.add("CS5");
        mainClass.notes.add("D5");

        con = null;
        try {
            con = DriverManager.getConnection(mainClass.url + mainClass.timezone, mainClass.uid, mainClass.pw);
        }
        catch (SQLException ex) {
            System.err.println("SQLException: " + ex);
            System.exit(1);
        }

        try {
            mainClass.writer = new FileWriter("Note.txt");
        } catch (Exception e) {
            System.out.println("Error");
        }

        mainClass.available = true;

        mainClass.start();
    }
}
