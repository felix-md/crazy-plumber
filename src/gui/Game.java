package gui;

import javax.swing.JFrame;

import config.Constant;
import config.Hints;
import config.LevelLoader;
import config.Modes;
import config.PipeMap;
import config.LevelLoader.difficulty;
import config.Modes.Mode;
import renders.GameView;
import renders.LevelView;
import renders.LoseView;
import renders.MenuView;
import renders.ModeView;
import renders.PipeView;
import renders.RulesView;
import renders.AchievementView;
import renders.DifficultyView;
import renders.View;
import renders.WinView;
import renders.PlayerView;

import config.Player;
import config.SaveManagement;
import config.SoundLoader;
import controllers.SoundController;

public class Game extends JFrame implements Runnable {

    private GameView gameView;
    private MenuView menuView;
    private LevelView levelView;
    private WinView winView;
    private LoseView loseView;
    private RulesView rulesView;
    private ModeView modeView;
    private PlayerView playerView;
    private DifficultyView difficultyView;
    private AchievementView achievementView;

    Player player;

    private final double FPS_SET = 120.0;
    private final double UPS_SET = 60.0;

    private Thread gameThread;

    public Game() {
        LevelLoader.loadLevels();

        SoundLoader.loadSound("background", "./sounds/game_sound.wav");
        SoundLoader.loadSound("rotation", "./sounds/rotation_sound.wav");
        SoundLoader.loadSound("button", "./sounds/click_button.wav");

        SoundController soundController = new SoundController(SoundLoader.getSoundClips());
        soundController.setVolume("background", -20.0f);

        player = new Player("Guest");
        initClasses();
        

        setSize(Constant.GAME_WIDTH, Constant.GAME_HEIGHT); // (1024, 768)
        setTitle("Crazy Plumber");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(playerView);

        pack();

        setVisible(true);

        soundController.loopSound("background");
      

    }

    private void initClasses() {
        gameView = new GameView(this);
        menuView = new MenuView(this);
        achievementView= new AchievementView(this);
        levelView = new LevelView(this);
        winView = new WinView(this);
        loseView= new LoseView(this);
        rulesView= new RulesView(this);
        modeView= new ModeView(this);
        difficultyView = new DifficultyView(this);
        achievementView= new AchievementView(this);
        playerView = new PlayerView(this);
    }

    /**
     * Change la fenêtre actuelle du jeu.
     * 
     * @param newView la nouvelle fenêtre à afficher
     */
    private void switchView(View newView) {
        setContentPane(newView);
        this.setCursor(newView.getCursors().get(0));
        validate();
        repaint();
    }

    public void switchGameView() {
        Modes.restart();
        Modes.resetMode();
        Modes.startTimer();
        switchView(gameView);
    }
    
    public void switchMenuView() {
        this.resetAll();
        switchView(menuView);
    }

    public void switchLevelView(Mode mode,difficulty diff) {
        LevelLoader.setActualDifficulty(diff);
        this.levelView=new LevelView(this);
        this.resetAll();
        Modes.setActualMode(mode);
        levelView.updateLevelButtons();
        switchView(levelView);
    }

    public void switchLevelView(int level) {
        this.resetAll();
        LevelLoader.setSelectedLevel(level);
        switchGameView();
    }

    public void switchLoseView(){
        this.resetAll();
        loseView= new LoseView(this);
        switchView(loseView);
    }

    public void switchWinView() {
        PipeMap.resetAnimationAttributes();
        GameView.setAnimating(false);
        PipeView.resetAll();
        player.completeLevel();
        System.out.println("Niveaux completes : " + player.getNumberOfLevelsCompleted());
        SaveManagement.savePlayer(player);
        winView.updateNextButton();
        winView= new WinView(this);
        
        switchView(winView);
    }
     
    public void updatePlayer(Player player){
        achievementView.setPlayer(player);
    }
    
    public void switchRulesView() {
        switchView(rulesView);
    }

    public void switchModeView() {
        switchView(modeView);
    }

    public void switchDifficultyView(){
        switchView(difficultyView);
    }

    public void switchPlayerView(){
        switchView(playerView);
    }

    public void switchAchievementView() {
        achievementView.updateAchievements();
        switchView(achievementView);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
        achievementView.setPlayer(player);
    }

    private void updateGame() {
        // Update game logic
        Modes.setScore();
        gameView.update();
    }

    private void resetAll(){
        PipeMap.resetAnimationAttributes();
        GameView.setAnimating(false);
        PipeView.resetAll();
        Modes.resetMode();
        Hints.resetHints();
    }

    
    public void start() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        // Game loop
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long lastFrame = System.nanoTime();
        long lastUpdate = System.nanoTime();
        long lastTimeCheck = System.currentTimeMillis();

        int frames = 0;
        int updates = 0;
        long now;

        while (true) {

            now = System.nanoTime();
            // Render
            if (now - lastFrame >= timePerFrame) {
                repaint();
                lastFrame = now;
                frames++;
            }

            // Update
            if (now - lastUpdate >= timePerUpdate) {
                updateGame();
                lastUpdate = now;
                updates++;
            }

            if (System.currentTimeMillis() - lastTimeCheck >= 1000) {
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
                lastTimeCheck = System.currentTimeMillis();
            }

        }

    }

    
    

    
}

