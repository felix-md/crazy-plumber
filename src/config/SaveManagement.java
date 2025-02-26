package config;

import java.io.*;
import java.util.ArrayList;

public class SaveManagement {
    private static final String SAVE_FILE_DIRECTORY = "./saves/";


    public static boolean playerNameExists(String name) {
        File[] files = getSavesFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".dat")) {
                    String fileNameWithoutExtension = file.getName().substring(0, file.getName().lastIndexOf('.'));
                    if (fileNameWithoutExtension.equals(name.toLowerCase())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static File[] getSavesFiles() {
        File saveFolder = new File(SAVE_FILE_DIRECTORY);
        return saveFolder.listFiles();
    } 

    public static void createPlayer(Player player) {
        savePlayer(player);
    }

    public static void deletePlayer(String player){
        File playerFile = new File(SAVE_FILE_DIRECTORY + player + ".dat");
        try { 
            if (playerFile.exists() && playerFile.isFile()) {
                if (playerFile.delete()) {
                    System.out.println("Player file deleted: " + player);
                }
            } 
        }catch (Exception e){
                System.err.println("An error occurred while deleting the player file: " + e.getMessage());
                return;
            }
        for(Player p : getSaveList()){
            if(p.getName()==player){
                getSaveList().remove(p);
                return;
            }
        }
    }

    public static void savePlayer(Player player) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(SAVE_FILE_DIRECTORY + player.getName() + ".dat"))) {
            outputStream.writeObject(player);
            System.out.println("Save successful.");
        } catch (IOException e) {
            System.err.println("Error during save: " + e.getMessage());
        }
    }

    public static ArrayList<Player> getSaveList() {
        ArrayList<Player> playerList = new ArrayList<Player>();
        for(File file : getSavesFiles()){
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
                playerList.add((Player) inputStream.readObject());
                System.out.println("Load successful");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error during load: " + e.getMessage());
            }
        }
        return playerList;
    }

    public static String getLongestPlayerName() {
        ArrayList<Player> playerList = getSaveList();
        String longestName = "";
        for (Player p : playerList) {
            if (p.getName().length() > longestName.length()) {
                longestName = p.getName();
            }
        }
        return longestName;
    }

	
}