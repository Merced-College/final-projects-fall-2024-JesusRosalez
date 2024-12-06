// Jesus Rosalez
// 11 - 20 - 24
// TableTop RPG Loot Generator

import java.util.*;
import java.io.*;

// Main
public class LootGame { public static void main(String[] args) {

    // Load data from file
    String filename = "/Users/jesusrosalez/IdeaProjects/Final/src/loot_data.csv";

    // Map to store loot data,
    Map<String, Map<String, List<String>>> locationLoot = LootHelper.loadLootData(filename);

    // Initalize empty list to create the player's backpack / inventory
    List<String> backpack = new ArrayList<String>();

    // Sanner Object to read user input; Location & Roll
    Scanner scanner = new Scanner(System.in);

    // While true: Game continues running
    // While false: Game ends
    boolean isRunning = true;

    // Main Game Loop
    while (isRunning) {

        // Helper method to display menu
        LootHelper.displayMenu();

        // Read users choice
        int choice = scanner.nextInt();

        // Handles multiple choices
        switch (choice) {

            // Get location from user
            case 1:
                System.out.print("Enter a location to search: ");
                scanner.nextLine(); // Consume newline
                String location = scanner.nextLine().trim();

                // Check if entered location exists in the loot data
                if (!locationLoot.containsKey(location)) {
                    System.out.println("Invalid location. Try again.");
                } else {

                    // If location is valid:
                    System.out.print("Enter your Notice Check roll (1-10): ");
                    // Get user's Notice Check roll (Dice Roll)
                    int noticeCheck = scanner.nextInt();

                    // Ensures the roll is within the range 1-10
                    if (noticeCheck < 1 || noticeCheck > 10) {
                        System.out.println("Invalid roll. It must be between 1 and 10.");
                    } else {

                        // If roll is valid, call loot tier function based on Notice Check Roll
                        int tier = LootHelper.getLootTier(noticeCheck);

                        // Call Loot Generator function
                        List<String> loot = LootHelper.generateLoot(locationLoot, location, tier, noticeCheck);

                        // Print out each item user found
                        System.out.println("You found:");
                        for (String item : loot) {
                            System.out.println("- " + item);
                        }

                        // Add found items to backpack
                        LootHelper.addToBackpack(backpack, loot);
                    }
                }
                break;

            // Display Backpack contents
            case 2:
                LootHelper.displayBackpack(backpack);
                break;

            // Exit Game - set to false
            case 3:
                isRunning = false;
                System.out.println("Thank you for playing!");
                break;

            // Handle invalid menu options
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    // Scanner close when game ends
    scanner.close();
}
}

