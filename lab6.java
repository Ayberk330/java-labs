// JournalEntry.java
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

class JournalEntry implements Serializable {
    private String title;
    private String content;
    private transient LocalDateTime timestamp;

    public JournalEntry(String title, String content) {
        this.title = title;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Title: " + title + "\n" +
                "Timestamp: " + timestamp + "\n" +
                "Content: " + content + "\n";
    }
}

class JournalManager {
    private List<JournalEntry> entries;

    public JournalManager() {
        entries = new ArrayList<>();
    }

    public void addEntry(JournalEntry entry) {
        entries.add(entry);
    }

    public void viewEntries() {
        for (JournalEntry entry : entries) {
            System.out.println(entry);
        }
    }

    public void saveEntries(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(entries);
            System.out.println("Entries saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadEntries(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            List<JournalEntry> loaded = (List<JournalEntry>) ois.readObject();
            entries.addAll(loaded);
            System.out.println("Entries loaded from " + filename);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void exportToBinary(String filename) {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(filename))) {
            for (JournalEntry entry : entries) {
                dos.writeUTF(entry.getTitle());
                dos.writeUTF(entry.getContent());
            }
            System.out.println("Entries exported to binary file: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importFromBinary(String filename) {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(filename))) {
            while (dis.available() > 0) {
                String title = dis.readUTF();
                String content = dis.readUTF();
                entries.add(new JournalEntry(title, content));
            }
            System.out.println("Entries imported from binary file: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void flushToDiskUsingBuffer(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (JournalEntry entry : entries) {
                writer.write("Title: " + entry.getTitle());
                writer.newLine();
                writer.write("Content: " + entry.getContent());
                writer.newLine();
                writer.write("---");
                writer.newLine();
            }
            System.out.println("Buffered flush to disk complete: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        JournalManager manager = new JournalManager();
        int choice;

        do {
            System.out.println("\n--- Personal Journal System ---");
            System.out.println("1. Add Entry");
            System.out.println("2. View Entries");
            System.out.println("3. Save Entries to File");
            System.out.println("4. Load Entries from File");
            System.out.println("5. Export to Binary");
            System.out.println("6. Import from Binary");
            System.out.println("7. Flush to Disk with Buffer");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter content: ");
                    String content = scanner.nextLine();
                    manager.addEntry(new JournalEntry(title, content));
                    break;
                case 2:
                    manager.viewEntries();
                    break;
                case 3:
                    System.out.print("Enter filename: ");
                    String saveFile = scanner.nextLine();
                    manager.saveEntries(saveFile);
                    break;
                case 4:
                    System.out.print("Enter filename: ");
                    String loadFile = scanner.nextLine();
                    manager.loadEntries(loadFile);
                    break;
                case 5:
                    System.out.print("Enter binary filename: ");
                    String binOut = scanner.nextLine();
                    manager.exportToBinary(binOut);
                    break;
                case 6:
                    System.out.print("Enter binary filename: ");
                    String binIn = scanner.nextLine();
                    manager.importFromBinary(binIn);
                    break;
                case 7:
                    System.out.print("Enter text filename: ");
                    String bufferFile = scanner.nextLine();
                    manager.flushToDiskUsingBuffer(bufferFile);
                    break;
                case 0:
                    System.out.println("Exiting... Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 0);

        scanner.close();
    }
}
