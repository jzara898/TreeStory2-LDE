package com.teamtreehouse;
import java.util.List;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        // Step 1: Use Scanner to read user input for the story template
        Scanner scanner = new Scanner(System.in);
        // Ask the user to enter a template story with placeholders
        System.out.println("Please enter your story template (use placeholders like name, adjective, etc.):");
        String story = scanner.nextLine();  // User enters a new story template
        // Step 2: Create Template object from the story
        Template tmpl = new Template(story);
        // Step 3: Create Prompter object to prompt for words
        Prompter prompter = new Prompter();
        // Step 4: Run the prompter to collect the values for placeholders (returns List<String>)
        List<String> results = prompter.run(tmpl);  // Prompts user for words
        // Step 5: Render the story using the user's input (replace placeholders with their inputs)
        String renderedStory = tmpl.render(results);
        // Step 6: Display the final "TreeStory" to the user
        System.out.println("\nYour TreeStory:\n");
        System.out.println(renderedStory);  // This is where the TreeStory is presented to the user
    }
}