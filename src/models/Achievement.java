package models;

import java.io.Serializable;

public class Achievement implements Serializable {

    private static final long serialVersionUID = 1L;
    private static transient int index = 0;  

    private final int id;
    private final String name;
    private final String description;
    private boolean unlocked;
    private UnlockCondition unlockCondition;

    public Achievement(String name, String description, UnlockCondition unlockCondition) {
        this.id = index;
        index++;
        this.name = name;
        this.description = description;
        this.unlockCondition = unlockCondition;
        this.unlocked = false;
    }

    public void unlock() {
        this.unlocked = true;
    }

    public void updateUnlockStatus() {
        if (unlockCondition.test()) {
            unlock();
        }
    }

    public static void resetIndex() {
        index = 0;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

}