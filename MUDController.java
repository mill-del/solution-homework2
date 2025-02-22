package com.example.mud.controller;

import java.util.Scanner;
import com.example.mud.player.Player;
import com.example.mud.room.Room;
import com.example.mud.item.Item;


public class MUDController {
    private final Player player;
    private boolean running;
    private final Scanner scanner;

    public MUDController(Player player) {
        this.player = player;
        this.running = true;
        this.scanner = new Scanner(System.in);
    }


    public void runGameLoop() {
        System.out.println("Welcome to the MUD! Type 'help' for a list of commands.");
        while (running) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            handleInput(input);
        }
    }


    private void handleInput(String input) {
        if (input.isEmpty()) return;

        String[] parts = input.split(" ", 2);
        String command = parts[0].toLowerCase();
        String argument = (parts.length > 1) ? parts[1] : "";

        switch (command) {
            case "look":
                lookAround();
                break;
            case "move":
                move(argument);
                break;
            case "pick":
                if (argument.startsWith("up ")) pickUp(argument.substring(3));
                else System.out.println("Invalid command! Use 'pick up <item>'.");
                break;
            case "inventory":
                checkInventory();
                break;
            case "help":
                showHelp();
                break;
            case "quit":
            case "exit":
                running = false;
                System.out.println("Goodbye!");
                break;
            default:
                System.out.println("Unknown command. Type 'help' for available commands.");
        }
    }


    private void lookAround() {
        Room currentRoom = player.getCurrentRoom();
        System.out.println(currentRoom.getDescription());
        System.out.println("Items here: " + (currentRoom.getItems().isEmpty() ? "None" : currentRoom.getItems()));
    }


    private void move(String direction) {
        if (direction.isEmpty()) {
            System.out.println("Specify a direction: forward, back, left, or right.");
            return;
        }

        Room nextRoom = player.getCurrentRoom().getAdjacentRoom(direction);
        if (nextRoom == null) {
            System.out.println("You can't go that way!");
        } else {
            player.setCurrentRoom(nextRoom);
            System.out.println("You moved to a new room.");
            lookAround();
        }
    }


    private void pickUp(String itemName) {
        Room currentRoom = player.getCurrentRoom();
        Item item = currentRoom.getItem(itemName);
        if (item == null) {
            System.out.println("No item named " + itemName + " here!");
        } else {
            player.addItem(item);
            currentRoom.removeItem(item);
            System.out.println("You picked up " + itemName + ".");
        }
    }


    private void checkInventory() {
        if (player.getInventory().isEmpty()) {
            System.out.println("Your inventory is empty.");
        } else {
            System.out.println("You are carrying: " + player.getInventory());
        }
    }


    private void showHelp() {
        System.out.println("Available commands:");
        System.out.println("look - Describe the current room");
        System.out.println("move <forward|back|left|right> - Move in a direction");
        System.out.println("pick up <item> - Pick up an item");
        System.out.println("inventory - List items you are carrying");
        System.out.println("help - Show this help menu");
        System.out.println("quit or exit - End the game");
    }
}
