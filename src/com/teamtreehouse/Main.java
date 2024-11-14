package com.teamtreehouse;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Story template with placeholders for user input
        String story = "Thanks __name__ for helping me out. You are really a __adjective__ __noun__ and I owe you a __noun__.";
        Template tmpl = new Template(story);  // Create a new template
        Prompter prompter = new Prompter();   // Create a new prompter

        // Run the prompter to collect user inputs based on the template
        List<String> results = prompter.run(tmpl);  // Prompt user for words

        // If the results are null, it means the user failed too many times
        if (results == null) {
            System.out.println("User input invalid, exiting...");
            return;  // Exit gracefully if user input is invalid (too many retries)
        }

        // Check if the number of inputs matches the number of placeholders in the template
        if (results.size() != tmpl.getPlaceHolders().size()) {
            System.out.println("Invalid number of inputs. Exiting...");
            return;  // Exit if the number of inputs doesn't match the placeholders
        }

        // If everything is valid, render and print the story
        try {
            String renderedStory = tmpl.render(results);
            System.out.printf("Your TreeStory:%n%n%s", renderedStory);
        } catch (Exception e) {
            System.out.println("An error occurred while rendering the story: " + e.getMessage());
        }
    }
}