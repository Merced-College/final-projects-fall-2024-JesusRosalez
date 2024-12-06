// Jesus Rosalez
// 11 - 20 - 24
// TableTop RPG Loot Generator Helper

import java.io.*;
import java.util.*;

// Helper class for main LootGame
public class LootHelper {
    // Random number generator
    private static Random random = new Random();

    // Load loot data from file and store it in Map
    public static Map<String, Map<String, List<String>>> loadLootData(String filename) {
        Map<String, Map<String, List<String>>> locationLoot = new HashMap<String, Map<String, List<String>>>();

        BufferedReader reader = null;
        try {

            //Open CSV file
            reader = new BufferedReader(new FileReader(filename));
            String line;

            // Read each line of the file, splitting at: ","
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                // Ensures four parts for each line or else it's invalid.
                if (parts.length!= 4) {
                    System.out.println("Invalid line format: " + line);
                    continue;
                }

                // First Part: location name
                String location = parts[0].trim();
                locationLoot.putIfAbsent(location, new HashMap<String, List <String>>());

                // Separate loot for each rarity level
                locationLoot.get(location).put("Common", Arrays.asList(parts[1].trim().split(";")));
                locationLoot.get(location).put("Rare", Arrays.asList(parts[2].trim().split(";")));
                locationLoot.get(location).put("Legendary", Arrays.asList(parts[3].trim().split(";")));
            }
        // Handle case where file is not found
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
            e.printStackTrace();

        // Handle other IO exceptions
        } catch (IOException e) {
            System.out.println("Error reading file:   " + e.getMessage());
            e.printStackTrace();
        }

        // Return loaded data structure
        return locationLoot;
    }

    // Display menu method
    public static void displayMenu() {
        System.out.println("\n--- Loot Game Menu ---");
        System.out.println("1. Search a Location");
        System.out.println("2. View Backpack");
        System.out.println("3. Exit Game");
        System.out.print("Choose an option: ");
    }

    // Determine loot tier method
    public static int getLootTier(int noticeCheck) {
        if (noticeCheck <= 3) return 1;  // Tier 1
        if (noticeCheck <= 6) return 2;  // Tier 2
        if (noticeCheck <= 8) return 3;  // Tier 3
        return 4;                        // Tier 4
    }

    // Determine loot rarity - changes ever tier
    public static String getLootRarity(int tier, int noticeCheck) {
        int roll = noticeCheck + random.nextInt(101);

        switch (tier) {
            case 1: // Tier 1 - low chance for legendary, higher chances for common
                if (roll <= 70) return "Common";     // 70% chance for Common
                if (roll <= 90) return "Rare";       // 20% chance for Rare
                return "Legendary";                  // 10% chance for Legendary
            case 2: // Tier 2 - higher chance for legendary and rare, common still most common
                if (roll <= 45) return "Common";     // 45% chance for Common
                if (roll <= 80) return "Rare";       // 35% chance for Rare
                return "Legendary";                  // 20% chance for Legendary
            case 3: // Tier 3 - 1/3 chance for legendary, equal chances for common and rare
                if (roll <= 35) return "Common";     // 35% chance for Common
                if (roll <= 70) return "Rare";       // 35% chance for Rare
                return "Legendary";                  // 30% chance for Legendary
            case 4: // Tier 4 - highest chance for legendary, more common rare, and less common common
                if (roll <= 25) return "Common";     // 25% chance for Common
                if (roll <= 60) return "Rare";       // 35% chance for Rare
                return "Legendary";                  // 40% chance for Legendary
            default:
                return "Common";                     // Default case, though it shouldn't happen
        }
    }

    // Generate loot for a given location and tier
    public static List<String> generateLoot(Map<String, Map<String, List<String>>> locationLoot, String location, int tier, int noticeCheck){

        List<String> loot = new ArrayList<String>();

        // Determine number of loot items
        int amount = tier + random.nextInt(tier + 1) + 1;

        for (int i = 0; i < amount; i++) {

            // Determine rarity of loot and get the list of items for the location and rarity
            String rarity = getLootRarity(tier, noticeCheck);
            List<String> items = locationLoot.get(location).get(rarity);

            // Random selection from loot pool (location specfic)
            if (items != null && !items.isEmpty()) {
                String item = items.get(random.nextInt(items.size())); // Pick a random item

                // Random durability percentage (based on tier)
                int durability = random.nextInt(26) + (tier * 15); // Durability is between 0 and 100%

                // Random number of items (based on tier)
                int itemCount = random.nextInt(3) + tier/2;

                loot.add(item + " (" + itemCount + " items, " + durability + "% durability, " + rarity + ")");
            }
        }

        // Return generated loot list
        return loot;
    }

    // Add items to backpack
    public static void addToBackpack(List<String> backpack, List<String> loot) {
        for (String item : loot) {

            // Keep track of items in backpack so long it fits
            if (backpack.size() < 30) {
                backpack.add(item);
            } else {

                // Stop the player when the backpack is full
                System.out.println("Backpack is full! Cannot add more loot.");
                break;
            }
        }
    }

    // Display contents of backpack method
    public static void displayBackpack(List<String> backpack) {
        if (backpack.isEmpty()) {
            System.out.println("Your backpack is empty.");
        } else {
            System.out.println("Backpack Contents:");
            for (String item : backpack) {
                System.out.println("- " + item);
            }
        }
    }
}
