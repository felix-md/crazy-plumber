package config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;

import config.LevelLoader.difficulty;
import models.HintModel;

public class Hints {
    
    private static ArrayList<ArrayList<HintModel>> hintsList;
    private static int nbHintsLeft=-1;
    private static int nbHintsMax=-1;
    private static int difficultyGap;
    private static String path;
    
    public static int getNbHintsUsed() {
        return nbHintsMax- nbHintsLeft;
    }

    public static int getNbHintsLeft() {
        return nbHintsLeft;
    }

    public static void initHints(){
        hintsList=new ArrayList<ArrayList<HintModel>>();

        for(difficulty d : LevelLoader.difficulty.values()){
            path = "./Hints/"+d.name().toLowerCase()+"_hints.txt";
            initHint(path,d.ordinal());
        }
        resetNb();
    }

    public static void initHint(String filePath, int diff){
        try{
            int levelIndice=0+(diff*15);
            int countParametre=0;// compteur de parametre qu'on a lu (max 3)
            Hints hints = new Hints();
            ArrayList<Integer> arg = new ArrayList<>();// liste des parametres pour faire un indice
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String readline;
            while((readline = reader.readLine())!=null){
                hintsList.add(new ArrayList<>());
                for(int i=0; i<readline.length();i++){
                    //On verifie si le char est un nombre
                    if(Character.isDigit(readline.charAt(i))){
                        arg.add(Character.getNumericValue(readline.charAt(i)));
                        countParametre++;
                    }
                    //On ajoute l'indice et on renitialise
                    if(countParametre==3){
                        hintsList.get(levelIndice).add(makeHint(arg,hints));
                        countParametre=0;
                        arg.clear();
                    }
                }
                Collections.shuffle(hintsList.get(levelIndice));//melanger les indices
                levelIndice++;
            }
            reader.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static HintModel makeHint(ArrayList<Integer> arg, Hints hints){
        return new HintModel(PipeMap.getPipe(arg.get(1), arg.get(0)),arg.get(2));
    }

    public static void activateHint(){
        if(nbHintsMax==-1){
            resetNb();
        }
        if(nbHintsLeft>0){
            int indiceHint = nbHintsMax-nbHintsLeft;
            HintModel hint = hintsList.get((LevelLoader.getSelectedLevel()-1)+difficultyGap).get(indiceHint);
            hint.activateHint();
            nbHintsLeft--;
        }
    }

    public static boolean isZeroHint(){
        return nbHintsLeft==0;
    }

    public static void resetNb(){
        difficultyGap= 15*LevelLoader.getActualDifficulty().ordinal();
        nbHintsMax=hintsList.get(LevelLoader.getSelectedLevel()-1+difficultyGap).size();
        nbHintsLeft=nbHintsMax;
    }

    public static void resetHints(){
        nbHintsMax=-1;
        nbHintsLeft=-1;
    }

    public static void resetBooleanRotate(){
        for(HintModel hint : hintsList.get(LevelLoader.getSelectedLevel()-1+difficultyGap)){
            hint.getPipe().setCanBeRotated(true);
        }
    }
}
