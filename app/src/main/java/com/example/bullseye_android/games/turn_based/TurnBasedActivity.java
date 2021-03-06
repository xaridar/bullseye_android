package com.example.bullseye_android.games.turn_based;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.user.User;
import com.example.bullseye_android.database.user.UserViewModel;
import com.example.bullseye_android.games.Game;
import com.example.bullseye_android.games.GamePauseFragment;
import com.example.bullseye_android.games.turn_based.units.Follower;
import com.example.bullseye_android.games.turn_based.units.Patroller;
import com.example.bullseye_android.games.turn_based.units.PatrollerEasy;
import com.example.bullseye_android.games.turn_based.units.PlayerUnit;
import com.example.bullseye_android.games.turn_based.units.Wanderer;
import com.example.bullseye_android.games.turn_based.units.Unit;
import com.example.bullseye_android.games.turn_based.units.WandererEasy;
import com.example.bullseye_android.games.turn_based.units.WandererHard;
import com.example.bullseye_android.music.MusicActivity;
import com.example.bullseye_android.music.MusicManager;
import com.example.bullseye_android.util.SfxManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import nl.dionsegijn.konfetti.KonfettiView;

public class TurnBasedActivity extends AppCompatActivity implements Game, MusicActivity {

    private int backCount;
    private Toast toast;

    private View diff;
    private Button playButton;
    private ImageButton pauseButton;
    private LinearLayout grid;
    private Button endTurn;
    private ConstraintLayout finishedLayout;
    private TextView endText;
    private TextView pointsText;
    private Button playAgain;
    private Button backBtn;

    private UserViewModel userViewModel;
    private SharedPreferences prefs;
    private User user;

    private int points;
    private int startingAmount;
    private int endingAmount;
    private int diffInt;
    private String difficulty;
    private RadioGroup diffGroup;
    private RadioButton buttonEasy;
    private RadioButton buttonHard;
    private int mapSizeX = 5;
    private int mapSizeY = 7;
    private Tile[][] board;
    private Node[][] graph;
    private ImageButton[][] buttons = new ImageButton[mapSizeX][mapSizeY];
    private Timer updateTimer;
    private ArrayList<Unit> playerUnits;
    private ArrayList<Unit> computerUnits;
    private ArrayList<Unit> capturedUnits;
    KonfettiView konfetti;
    private MediaPlayer enemyCaptured;
    private MediaPlayer playerCaptured;
    private MediaPlayer winSound;
    private MediaPlayer loseSound;
    private long time;
    private Timer secondTimer;
    private int moves;
    private int gameNum;
    private boolean won;

    /**
     *  0 - Can click on their own units, selects them and lets them move
     *  1 - Can click on a tile, moves unit after turn ends
     *  2 - Can click on an enemy unit if in range
     */
    private int state;
    private Unit selectedUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn_based);
        konfetti = findViewById(R.id.viewKonfetti2);
        prefs = getSharedPreferences("userID", MODE_PRIVATE);
        long id = (prefs.getLong("id", 0));

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        toast = Toast.makeText(this, "Press the back button twice at any time to go back to the dashboard.", Toast.LENGTH_SHORT);
        toast.show();

        LiveData<User> mUser = userViewModel.getUser(id);
        mUser.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                TurnBasedActivity.this.user = user;
                mUser.removeObserver(this);
                pregame();
            }
        });
    }


    private View.OnClickListener tileListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Pair<Integer, Integer> location = null;
            for(int x=0;x<buttons.length;x++){
                for(int y=0;y<buttons[x].length;y++){
                    if(v==buttons[x][y]){
                        location = new Pair<>(x, y);
                    }
                }
            }
            Log.i("TB", Arrays.toString(new Tile[]{board[location.first][location.second]}));
            Log.i("TB", Arrays.toString(new Node[]{graph[location.first][location.second]}));
            Log.i("TB", board[location.first][location.second].getUnit() == null ? "  > Unit is null" : "  > " + board[location.first][location.second].getUnit().getName());
            Log.i("TB", board[location.first][location.second].getUnit() != null ? board[location.first][location.second].getUnit().getCurrentPath() != null ? "    > " + board[location.first][location.second].getUnit().getCurrentPath().toString() : "" : "" );
            Log.i("TB", board[location.first][location.second].getUnit() != null ?  "    > " + board[location.first][location.second].getUnit().x + ", " + board[location.first][location.second].getUnit().y : "" );

            Log.i("TB", "  > " + board[location.first][location.second].x + ", " + board[location.first][location.second].y);
            switch(state){
                case 0:

                    if(location==null){
                        return;
                    }
                    if((board[location.first][location.second].getUnit()!=null) && (board[location.first][location.second].getUnit().getOwner() == Owners.PLAYER) && (!board[location.first][location.second].getUnit().isMoved())){
                        selectedUnit = board[location.first][location.second].getUnit();
                        setState(1);
                    }

                    break;

                case 1:

                    if(location==null){
                        return;
                    }
                    if(selectedUnit!=null) {
                        ArrayList<Node> path = Pathfinder.generatePathTo(selectedUnit.x, selectedUnit.y, location.first, location.second, graph, board, selectedUnit);
                        if(path.size() > 0) {
                            selectedUnit.setMoved(true);
                            selectedUnit.setCurrentPath(path);
                        }
                    }
                    selectedUnit = null;

                    boolean allUnitsMoved = false;
                    for(Unit unit: playerUnits){
                        if(!unit.isMoved()){
                            allUnitsMoved = false;
                            break;
                        }
                        allUnitsMoved = true;
                    }

                    if(allUnitsMoved){
                        endTurn();
                    }else{
                        setState(0);
                    }

                    break;

                case 2:
                    break;
            }
        }
    };

    private void pregame(){
        grid = findViewById(R.id.grid);
        diff = findViewById(R.id.settingsLayout);
        playButton = findViewById(R.id.playBtn);
        pauseButton = findViewById(R.id.pauseButton);
        endTurn = findViewById(R.id.endTurnButton);
        finishedLayout = findViewById(R.id.finishedLayout);
        endText = findViewById(R.id.endText);
        pointsText = findViewById(R.id.pointCount);
        playAgain = findViewById(R.id.playAgain);
        backBtn = findViewById(R.id.backToDashboard);
        diffGroup = findViewById(R.id.diffGroup);

        grid.setVisibility(View.VISIBLE);
        finishedLayout.setVisibility(View.INVISIBLE);
        diff.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.INVISIBLE);
        endTurn.setVisibility(View.INVISIBLE);
        konfetti.setVisibility(View.INVISIBLE);

        Button dashBtn = findViewById(R.id.dashBtn);
        dashBtn.setOnClickListener(view -> finish());

        playButton.setOnClickListener(view -> {
            diff.setVisibility(View.INVISIBLE);
            if(diffGroup.getCheckedRadioButtonId() == R.id.buttonEasy){
                diffInt = User.GAME_STRATEGY_EASY;
                difficulty = "Easy";
            }else{
                diffInt = User.GAME_STRATEGY_HARD;
                difficulty = "Hard";
            }
            start();
            gameNum = User.GAME_STRATEGY_EASY;
        });
    }

    private void start(){
        moves = 0;
        secondTimer = new Timer();
        secondTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                time++;
            }
        }, 1000, 1000);
        float vol = (float) user.getGameVolume() / User.MAX_VOLUME;

        playerUnits = new ArrayList<Unit>();
        computerUnits = new ArrayList<Unit>();
        capturedUnits = new ArrayList<Unit>();

        enemyCaptured = SfxManager.createSfx(this, R.raw.enemy_captured, vol);
        playerCaptured = SfxManager.createSfx(this, R.raw.unit_captured, vol);
        winSound = SfxManager.createSfx(this, R.raw.win_default, vol);
        loseSound = SfxManager.createSfx(this, R.raw.lose_sound, vol);


        pauseButton.setVisibility(View.VISIBLE);
        endTurn.setVisibility(View.VISIBLE);
        endTurn.setOnClickListener(view -> endTurn());

        setBoard();

        setState(0);

        updateTimer = new Timer();
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> updateBoard());
            }
        }, 100,100);

    }

    private void endTurn(){
        for(Unit playerUnit : playerUnits){
            if((playerUnit.getCurrentPath() != null) && (playerUnit.getCurrentPath().size() > 0) && (!playerUnit.isDead())){
                playerUnit.movement(board, graph);
            }

        }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                for(Unit computerUnit : computerUnits){
                    if(!computerUnit.isDead()) {
                        computerUnit.movement(board, graph);
                    }
                }
            }
        }, 600);
        setState(0);
        for(Unit unit : playerUnits){
            unit.setMoved(false);
        }
        moves++;
    }

    /**
     * Sets board variable, sets node graph, and shows tiles on screen
     * Runs once at beginning
     */
    private void setBoard(){
        board = new Tile[mapSizeX][mapSizeY];

        for(int x=0;x<board.length;x++){
            for(int y=0;y<board[x].length;y++){
                board[x][y] = new Tile("grass", x, y,"",1,true,null);

            }
        }

        for(int x=0;x<grid.getChildCount();x++){
            for(int y=0;y<((LinearLayout)grid.getChildAt(x)).getChildCount();y++){
                buttons[x][y] = (ImageButton) ((LinearLayout)grid.getChildAt(x)).getChildAt(y);

                String img = board[x][y].getIcon();
                int res = getResources().getIdentifier(img, "drawable", "com.example.bullseye_android");
                buttons[x][y].setOnClickListener(tileListener);

                board[x][y].setPadding(buttons[x][y].getPaddingLeft(), buttons[x][y].getPaddingTop(), buttons[x][y].getPaddingRight(), buttons[x][y].getPaddingBottom());
                board[x][y].setOriginalPadding(board[x][y].getPadding());
            }
        }

        graph = Pathfinder.generatePathfindingGraph(mapSizeX, mapSizeY);

        int playerUnitNum = 3;
        for(int i = 1; i< playerUnitNum+1; i++){
            Unit playerUnit = new Unit("example" + (i), i, 6,"ic_strat_img_bulldog", 1, Owners.PLAYER, board, graph, this, this);
            playerUnits.add(playerUnit);
        }

        LevelGen levelGen = difficulty.equals("Easy") ? new EasyLevelGen() : new HardLevelGen();
        computerUnits = levelGen.genLevel(board, graph, this);

        startingAmount = computerUnits.size();

        won = false;
    }

    private void updateBoard(){
        int totalUnits = 0;
        for(int x=0;x<buttons.length;x++){
            for(int y=0;y<buttons[x].length;y++) {
                ImageButton button = buttons[x][y];
                Tile tile = board[x][y];
                if (tile.getUnit() != null && !tile.getUnit().isDead()) {
                    totalUnits++;
                    button.setImageResource(getResources().getIdentifier(tile.getUnit().getIcon(), "drawable", "com.example.bullseye_android"));
                }else if(!tile.getIcon().equals("")){
                    button.setImageResource(getResources().getIdentifier(tile.getIcon(), "drawable", "com.example.bullseye_android"));
                }else {
                    button.setImageResource(0);
                }
                button.setPadding(tile.getPadding()[0],tile.getPadding()[1],tile.getPadding()[2],tile.getPadding()[3]);
            }
        }
        for(Unit playerUnit : playerUnits) {
            if (playerUnit.isJustDied()) {
                playerCaptured.start();
                playerUnit.setJustDied(false);
                playerUnit.setDead(true);
            }
            if (playerUnit.isDead()) {
                playerUnits.remove(playerUnit);
                break;
            } else {
                playerUnit.update();
            }
        }
        for(Unit computerUnit : computerUnits){
            if(computerUnit.isJustDied()){
                enemyCaptured.start();
                computerUnit.setJustDied(false);
                computerUnit.setDead(true);
            }
            if(computerUnit.isDead()){
                computerUnits.remove(computerUnit);
                break;
            }else{
                computerUnit.update();
            }
        }
        checkWin();
    }

    private void checkWin(){
        if (!won) {
            String winner;
            String text;
            if (computerUnits.isEmpty()) {
                winner = "player";
            } else if (playerUnits.isEmpty()) {
                winner = "computer";
            } else {
                return;
            }
            MusicManager.getInstance().stop();
            switch (winner) {
                case "player":
                    //player wins
                    text = "You Win!";
                    confetti(konfetti, this.getApplicationContext());
                    endText.setText(text);
                    winSound.start();
                    break;
                case "computer":
                    //player loses
                    text = "You Lost . . .";
                    endText.setText(text);
                    loseSound.start();
                    break;
                default:
                    break;
            }
            int remaining = playerUnits.size();
            int pointsToAdd = calcPoints(moves, remaining);
            user.addStratGame(gameNum, pointsToAdd, moves, remaining, time);
            updateTimer.cancel();
            secondTimer.cancel();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> {
                        endingAmount = computerUnits.size();
                        points = startingAmount - endingAmount;
                        pointsText.setText(getString(R.string.tb_points, points));
                        finishedLayout.setVisibility(View.VISIBLE);
                        endTurn.setVisibility(View.INVISIBLE);
                        pauseButton.setVisibility(View.INVISIBLE);

                        playAgain.setOnClickListener(view -> {
                            MusicManager.getInstance().make(TurnBasedActivity.this, getMusicId()).start();
                            pregame();
                        });
                        backBtn.setOnClickListener(view -> finish());
                    });
                }
            }, 600);
            won = true;

        }
    }

    public Tile[][] getBoard(){ return board; }

    public Node[][] getGraph(){ return graph; }

    private int calcPoints(int moves, int remaining) {
        // determine a good points algorithm
        float points = 100 * ((float) remaining / moves) * (startingAmount * 3);
        return (int) points;
    }

    private void setState(int state){
        String text = "";
        switch(state){
            case 0:
                text = "Click on one of your units";
                break;
            case 1:
                text = "Click on a tile to move there";
                break;
            case 2:
                text = "Click on an enemy to move there";
        }
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
        this.state = state;
    }

    /**
     * Brings user to tutorial screen
     */
    public void howToPlay(View view){
        startActivity(new Intent(this, TurnBasedInstructions.class));
    }

    /**
     * Brings user to dashboard screen
     */
    public void back(View view){

    }

    /**
     * Stops all timers and brings user to pause fragment
     */
    public void pause(View view){
//        pauseButton.setVisibility(View.INVISIBLE);
        endTurn.setVisibility(View.INVISIBLE);
        updateTimer.cancel();
        secondTimer.cancel();
        for(ImageButton[] buttonList : buttons){
            for(ImageButton button : buttonList){
                button.setEnabled(false);
            }
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.turn_based_game, GamePauseFragment.newInstance(user)).commit();
//        MusicManager.getInstance().setVolume((float) user.getMusicVolume() / 2);
    }

    /**
     * Continues all timers and brings user back to game
     * (Accessed and used by pause fragment)
     */
    public void unpause(){
//        MusicManager.getInstance().setVolume(user.getMusicVolume());
        runOnUiThread(() -> {
            pauseButton.setVisibility(View.VISIBLE);
            endTurn.setVisibility(View.VISIBLE);
        });
        updateTimer = new Timer();
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> updateBoard());
            }
        }, 100,100);
        runOnUiThread(()-> {
            for (ImageButton[] buttonList : buttons) {
                for(ImageButton button : buttonList) {
                    button.setEnabled(true);
                }
            }
        });
        secondTimer = new Timer();
        secondTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                time++;
            }
        }, 100, 100);

    }

    /**
     * Returns current game
     * (Used by pause fragment to get colors, set text, and to send after un-pausing)
     */
    public String getGame(){ return "strategy"; }

    @Override
    protected void onResume() {
        super.onResume();
        prefs = getSharedPreferences("userID", MODE_PRIVATE);
        long id = (prefs.getLong("id", 0));

        LiveData<User> mUser = userViewModel.getUser(id);
        mUser.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                TurnBasedActivity.this.user = user;
                mUser.removeObserver(this);
            }
        });
    }

    @Override
    public void finish() {
        userViewModel.update(user);
        super.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        userViewModel.update(user);
    }

    @Override
    public int getMusicId() {
        return R.raw.strategysong;
    }

    @Override
    public void onBackPressed() {
        backCount++;
        if (backCount >= 2) {
            super.onBackPressed();
            toast.cancel();
            return;
        }
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, "Press back again to go to the dashboard", Toast.LENGTH_SHORT);
        toast.show();
        new Handler().postDelayed(() -> backCount = 0, 2000);
    }
}