package config;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LevelLoader {

    public enum difficulty {
        EASY, MEDIUM, HARD;
    }

    private static List<List<String>> diff = new ArrayList<>();

    private static List<String> levels = new ArrayList<>();
    private static int selectedLevel = 1;
    private static difficulty actualDifficulty = difficulty.EASY;

    public static String loadLevel(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                contentBuilder.append(currentLine).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return contentBuilder.toString();
    }

    public static int getNumberOfFiles(String directoryPath) {
        try {
            try (var paths = Files.list(Paths.get(directoryPath))) {
                return (int) paths.filter(Files::isRegularFile).count();
            }
        } catch (IOException e) {

            e.printStackTrace();
            return -1;
        }
    }

    public static void loadLevels() {
        for (difficulty d : difficulty.values()) {
            levels = new ArrayList<>();
            for (int i = 1; i < getNumberOfFiles("./levels/" + d.name().toLowerCase()) + 1; i++) {
                levels.add(loadLevel("./levels/" + d.name() + "/level_" + i + ".txt"));
            }
            diff.add(levels);
        }
    }

    public static List<String> getLevels() {
        return diff.get(actualDifficulty.ordinal());
    }

    public static int getSelectedLevel() {
        return selectedLevel;
    }

    public static int getCurrentLevel() {
        return selectedLevel + levels.size() * getActualDifficultyIndex();
    }

    public static int getNextLevel() {
        if(hasNextLevel()) {
            selectedLevel++;
        }
        return selectedLevel;
    }

    public static boolean hasNextLevel() {
        return selectedLevel < levels.size();
    }

    public static void setSelectedLevel(int a) {

        LevelLoader.selectedLevel = a;
    }

    public static difficulty getActualDifficulty() {
        return actualDifficulty;
    }

    public static int getActualDifficultyIndex() {
        return actualDifficulty.ordinal();
    }

    public static void setActualDifficulty(difficulty actualDifficulty) {
        LevelLoader.actualDifficulty = actualDifficulty;
    }   

    public static int getNumberOfLevels() {
        return levels.size();
    }

    public static int getNumberOfTotalLevels() {
        return levels.size() * diff.size();
    }
}
