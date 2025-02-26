package config;

import java.io.Serializable;
import java.util.ArrayList;

import config.Modes.Mode;
import models.Achievement;
import config.Modes.Mode;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    
    
    private String name;
    private int numberOfLevelsCompleted;
    private boolean isLevelTimer=false;
    private boolean isLevelLimited=false;
    private ArrayList<Achievement> achievementsList = new ArrayList<>(); 

    private boolean[] unlockedLevels = new boolean[45]; 
    private boolean[] completedLevels = new boolean[45];

    public Player(String name) {
        this.name = name.toLowerCase();
        this.numberOfLevelsCompleted = 0;
        for (int i = 0 ; i < 3 ; ++i) {
            unlockedLevels[i*15] = true;
        }
        initAchievements();
    }

    private void initAchievements(){
        achievementsList.add(new Achievement("BEGINNER", "Complete a level.", () -> numberOfLevelsCompleted >= 1));
        achievementsList.add(new Achievement("STARTER PLUMBER", "Complete all levels in easy mode.", () -> completedLevels[14]));
        achievementsList.add(new Achievement("INTERMEDIATE ENGINEER", "Complete all levels in medium mode.", () -> completedLevels[29]));
        achievementsList.add(new Achievement("HARDCORE PLUMBER", "Complete all levels in hard mode.", () -> completedLevels[44]));
        achievementsList.add(new Achievement("TOTAL MASTERY", "Complete all available levels.", () -> numberOfLevelsCompleted == LevelLoader.getNumberOfTotalLevels()));
        achievementsList.add(new Achievement("TIME TRIALIST", "Complete a level in timer mode.", () -> isLevelTimer));
        achievementsList.add(new Achievement("MOVEMENT MASTER", "Complete a level in limited move mode.", () -> isLevelLimited));
        achievementsList.add(new Achievement("MASTER PLUMBER", "Unlock all achievements.", () -> isAllUnlocked()));
        Achievement.resetIndex();

    }

    public boolean isAllUnlocked(){
        boolean b = true;
        for(Achievement a : achievementsList){
            if(a.getId()!=7){
                b=b&& a.isUnlocked();
            }
        }
        return b;
    }

    public void completeLevel() {
        if(!isCurrentLevelCompleted()){
            completeCurrentLevel();
        }
        unlockNextLevel();
        if(!isLevelTimer){
            isLevelTimer=(Modes.getActualMode()==Mode.TIMER);
        }
        if(!isLevelLimited){
            isLevelLimited=(Modes.getActualMode()==Mode.LIMITED_MOVE);
        }
        for (Achievement achievement : achievementsList) {
            achievement.updateUnlockStatus();
        }
    }

    private void completeCurrentLevel() {
        numberOfLevelsCompleted++;
        completedLevels[LevelLoader.getCurrentLevel() - 1] = true;
    }
    
    private boolean isCurrentLevelCompleted() {
        return completedLevels[LevelLoader.getCurrentLevel() - 1];
    }

    private void unlockNextLevel() {
        if (LevelLoader.hasNextLevel()) {
            unlockedLevels[LevelLoader.getCurrentLevel()] = true;
        }
    }

    //Getters

    public String getName() {
        return name;
    }

    public int getNumberOfLevelsCompleted() {
        return numberOfLevelsCompleted;
    }

    public ArrayList<Achievement> getAchievementsList() {
        return achievementsList;
    }

    public boolean[] getUnlockedLevels() {
        return unlockedLevels;
    }

    public boolean setName(String name) {
        if (name.length() > 0) {
            this.name = name;
            return true;
        }
        return false;
    }
}