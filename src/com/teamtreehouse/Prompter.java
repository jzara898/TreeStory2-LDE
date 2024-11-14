package com.teamtreehouse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Prompter {
    private BufferedReader mReader;
    private Set<String> mCensoredWords;
    private static final int MAX_RETRIES = 5;

    public Prompter() {
        mReader = new BufferedReader(new InputStreamReader(System.in));
        mCensoredWords = loadCensoredWords();
    }

    // Load censored words from file
    private Set<String> loadCensoredWords() {
        Set<String> censoredWords = new HashSet<>();
        Path file = Paths.get("resources", "censored_words.txt");

        try {
            List<String> lines = Files.readAllLines(file);
            censoredWords.addAll(lines.stream()
                    .map(String::trim)
                    .map(String::toLowerCase) // Normalize to lowercase for case-insensitive matching
                    .collect(Collectors.toSet()));
            System.out.println("DEBUG: Censored words set: " + censoredWords);
        } catch (IOException e) {
            System.out.println("Couldn't load censored words");
            e.printStackTrace();
        }
        return censoredWords;
    }

    // Prompt for input and check if it is valid or censored
    private String getInput(String placeholder) {
        int retries = 0;
        while (retries < MAX_RETRIES) {
            try {
                System.out.printf("Please enter a %s: ", placeholder);
                String input = mReader.readLine();

                // Check if input is null (user pressed Enter without typing anything)
                if (input == null) {
                    System.out.println("Input was empty. Please try again.");
                    retries++;
                    continue;
                }

                // Trim the input
                input = input.trim();

                // Check if input is literally "null" (not the null object, but the string "null")
                if ("null".equalsIgnoreCase(input)) {
                    System.out.println("The word 'null' is not allowed. Please try again.");
                    retries++;
                    continue;
                }

                // DEBUG: Show user input and cleaned input
                System.out.println("DEBUG: User entered: '" + input + "'");
                String cleaned = input.toLowerCase();  // Clean input (lowercase)
                System.out.println("DEBUG: Checking if censored word: '" + cleaned + "'");

                // Check if the cleaned input matches any censored word
                if (mCensoredWords.contains(cleaned)) {
                    System.out.println("That word is censored. Please try again.");
                    retries++;
                    continue;
                }

                return cleaned;  // Return valid input
            } catch (IOException e) {
                System.out.println("An error occurred while reading input. Please try again.");
                retries++;
            }
        }

        System.out.println("Too many invalid attempts. Exiting...");
        return null;
    }

    // Main logic for running the prompts and collecting valid inputs
    public List<String> run(Template tmpl) {
        List<String> results = new ArrayList<>();
        List<String> placeholders = tmpl.getPlaceHolders();
        System.out.println("DEBUG: All placeholders: " + placeholders);

        for (int i = 0; i < placeholders.size(); i++) {
            String placeholder = placeholders.get(i);
            String validInput = null;

            // Handle the last placeholder separately
            if (i == placeholders.size() - 1) {
                while (validInput == null) {
                    validInput = getInput(placeholder);
                }
            } else {
                int retries = 0;
                // Continue prompting for valid input or until retries are exhausted
                while (retries < MAX_RETRIES && validInput == null) {
                    validInput = getInput(placeholder);  // Get input for the placeholder
                    if (validInput == null) {
                        retries++;  // If input is invalid (null), increment retries
                    }
                }

                // If retries exceed the maximum, exit with a message
                if (retries >= MAX_RETRIES) {
                    System.out.println("Too many invalid attempts. Exiting...");
                    return null;
                }
            }

            results.add(validInput);  // Add valid input to results
        }

        return results;  // Return the collected results
    }

    public static void main(String[] args) {
        // Example Template (You can modify the template string as needed)
        String templateText = "Hello __name__, you are a __adjective__ __noun__!";
        Template tmpl = new Template(templateText);

        // Create the Prompter instance
        Prompter prompter = new Prompter();

        // Run the prompting process with the template
        List<String> results = prompter.run(tmpl);

        // Print the final results if not null (if too many invalid attempts, it would be null)
        if (results != null) {
            System.out.println("Final Results: " + tmpl.render(results));
        }
    }
}