import java.io.*;
import java.util.*;
import java.util.regex.*;

 class SpellChecker {
    private Set<String> dictionary;
    private String dictionaryFilePath;


    public SpellChecker(String dictionaryFilePath) {
        this.dictionaryFilePath = dictionaryFilePath;
        dictionary = new HashSet<>();
        loadDictionary();
    }


    private void loadDictionary() {
        File file = new File(dictionaryFilePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating dictionary file: " + e.getMessage());
            }
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim().toLowerCase();
                if (!line.isEmpty()) {
                    dictionary.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading dictionary file: " + e.getMessage());
        }
    }


    private void saveDictionary() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dictionaryFilePath))) {
            for (String word : dictionary) {
                writer.write(word);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing dictionary file: " + e.getMessage());
        }
    }


    public void addWord(String word) {
        String w = word.toLowerCase();
        if (dictionary.add(w)) {
            saveDictionary();
            System.out.println("Added word: " + w);
        } else {
            System.out.println("Word already in dictionary: " + w);
        }
    }


    public void removeWord(String word) {
        String w = word.toLowerCase();
        if (dictionary.remove(w)) {
            saveDictionary();
            System.out.println("Removed word: " + w);
        } else {
            System.out.println("Word not found in dictionary: " + w);
        }
    }

     int getLevenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= b.length(); j++) dp[0][j] = j;
        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                int cost = (a.charAt(i-1) == b.charAt(j-1)) ? 0 : 1;
                dp[i][j] = Math.min(
                        Math.min(dp[i-1][j] + 1, dp[i][j-1] + 1),
                        dp[i-1][j-1] + cost
                );
            }
        }
        return dp[a.length()][b.length()];
    }


    public List<String> suggestWords(String input) {
        List<String> suggestions = new ArrayList<>();
        String in = input.toLowerCase();
        for (String word : dictionary) {
            if (getLevenshteinDistance(in, word) <= 1) {
                suggestions.add(word);
            }
        }
        return suggestions;
    }


    public void processFile(String inputPath, String outputPath) {
        Pattern tokenPattern = Pattern.compile("(\\w+)|(\\W+)");
        List<String> outputTokens = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath))) {
            String line;
            Scanner console = new Scanner(System.in);
            while ((line = reader.readLine()) != null) {
                Matcher matcher = tokenPattern.matcher(line);
                while (matcher.find()) {
                    String token = matcher.group();
                    if (token.matches("\\w+")) {
                        String lower = token.toLowerCase();
                        if (!dictionary.contains(lower)) {
                            System.out.println("Misspelled: '" + token + "'");
                            List<String> sugg = suggestWords(lower);
                            if (!sugg.isEmpty()) {
                                System.out.println("Suggestions: " + sugg);
                                System.out.print("Use '" + sugg.get(0) + "'? (y/n/c): ");
                                String choice = console.nextLine().trim().toLowerCase();
                                if (choice.equals("y")) {
                                    token = matchCase(token, sugg.get(0));
                                } else if (choice.equals("c")) {
                                    System.out.print("Enter replacement: ");
                                    String custom = console.nextLine();
                                    token = custom;
                                }
                            }
                        }
                    }
                    outputTokens.add(token);
                }
                outputTokens.add("\n");
            }
            console.close();
            // Write output
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
                for (String tok : outputTokens) {
                    writer.write(tok);
                }
            }
            System.out.println("Processed file written to " + outputPath);
        } catch (IOException e) {
            System.err.println("Error processing file: " + e.getMessage());
        }
    }


    private String matchCase(String original, String suggestion) {
        if (Character.isUpperCase(original.charAt(0))) {
            return suggestion.substring(0,1).toUpperCase() + suggestion.substring(1);
        }
        return suggestion;
    }
}



class CensorModule {
    private Set<String> badWords;
    private String expletiveDictPath;

    public CensorModule(String expletiveDictPath) {
        this.expletiveDictPath = expletiveDictPath;
        badWords = new HashSet<>();
        loadExpletives();
    }

    private void loadExpletives() {
        File file = new File(expletiveDictPath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating expletive file: " + e.getMessage());
            }
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim().toLowerCase();
                if (!line.isEmpty()) badWords.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading expletive file: " + e.getMessage());
        }
    }

    private void saveExpletiveDictionary() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(expletiveDictPath))) {
            for (String word : badWords) {
                writer.write(word);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing expletive file: " + e.getMessage());
        }
    }

    public void addExpletive(String word) {
        String w = word.toLowerCase();
        if (badWords.add(w)) {
            saveExpletiveDictionary();
            System.out.println("Added expletive: " + w);
        } else {
            System.out.println("Expletive already exists: " + w);
        }
    }

    public void removeExpletive(String word) {
        String w = word.toLowerCase();
        if (badWords.remove(w)) {
            saveExpletiveDictionary();
            System.out.println("Removed expletive: " + w);
        } else {
            System.out.println("Expletive not found: " + w);
        }
    }


    public void censorFile(String inputPath, String outputPath) {
        Pattern tokenPattern = Pattern.compile("(\\w+)|(\\W+)");
        List<String> outputTokens = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = tokenPattern.matcher(line);
                while (matcher.find()) {
                    String token = matcher.group();
                    if (token.matches("\\w+")) {
                        if (badWords.contains(token.toLowerCase())) {
                            token = "[CENSORED]";
                        }
                    }
                    outputTokens.add(token);
                }
                outputTokens.add("\n");
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
                for (String tok : outputTokens) {
                    writer.write(tok);
                }
            }
            System.out.println("Censored file written to " + outputPath);
        } catch (IOException e) {
            System.err.println("Error censoring file: " + e.getMessage());
        }
    }
}

 class SpellCheckerTestApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SpellChecker sp = new SpellChecker("dictionary.txt");
        CensorModule cm = new CensorModule("expletives.txt");
        while (true) {
            System.out.println("\n--- SpellChecker & CensorModule Menu ---");
            System.out.println("1. Add word to dictionary");
            System.out.println("2. Remove word from dictionary");
            System.out.println("3. Suggest words");
            System.out.println("4. Process file for spelling");
            System.out.println("5. Add expletive");
            System.out.println("6. Remove expletive");
            System.out.println("7. Censor file");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    System.out.print("Enter word to add: ");
                    sp.addWord(scanner.nextLine());
                    break;
                case "2":
                    System.out.print("Enter word to remove: ");
                    sp.removeWord(scanner.nextLine());
                    break;
                case "3":
                    System.out.print("Enter word to suggest: ");
                    System.out.println("Suggestions: " + sp.suggestWords(scanner.nextLine()));
                    break;
                case "4":
                    System.out.print("Input file path: ");
                    String inFile = scanner.nextLine();
                    System.out.print("Output file path: ");
                    String outFile = scanner.nextLine();
                    sp.processFile(inFile, outFile);
                    break;
                case "5":
                    System.out.print("Enter expletive to add: ");
                    cm.addExpletive(scanner.nextLine());
                    break;
                case "6":
                    System.out.print("Enter expletive to remove: ");
                    cm.removeExpletive(scanner.nextLine());
                    break;
                case "7":
                    System.out.print("Input file path: ");
                    inFile = scanner.nextLine();
                    System.out.print("Output file path: ");
                    outFile = scanner.nextLine();
                    cm.censorFile(inFile, outFile);
                    break;
                case "8":
                    System.out.println("Exiting.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
