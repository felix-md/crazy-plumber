package models;

import java.io.Serializable;

@FunctionalInterface
public interface UnlockCondition extends Serializable{
    boolean test();
}
