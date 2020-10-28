package com.example.bullseye_android.games.turn_based;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
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
import com.example.bullseye_android.database.User;
import com.example.bullseye_android.database.UserViewModel;
import com.example.bullseye_android.games.Game;
import com.example.bullseye_android.games.GamePauseFragment;
import com.example.bullseye_android.games.sorting.SortingInstructionsActivity;
import com.example.bullseye_android.games.turn_based.units.EasyPatroller;
import com.example.bullseye_android.games.turn_based.units.Unit;
import com.example.bullseye_android.music.MusicActivity;
import com.example.bullseye_android.util.SfxManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import nl.dionsegijn.konfetti.KonfettiView;

public class TurnBasedActivity extends AppCompatActivity implements Game, MusicActivity {

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
    private int mapSizeX = 5;
    private int mapSizeY = 7;
    private Tile[][] board;
    private Node[][] graph;
    private ImageButton[][] buttons = new ImageButton[mapSizeX][mapSizeY];
    private Timer updateTimer;
    private ArrayList<Unit> playerUnits = new ArrayList<Unit>();
    private ArrayList<Unit> computerUnits = new ArrayList<Unit>();
    KonfettiView konfetti;
    private MediaPlayer enemyCaptured;
    private MediaPlayer playerCaptured;

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
                        Log.i("tbdubug",path.toString());
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

        grid.setVisibility(View.VISIBLE);
        finishedLayout.setVisibility(View.INVISIBLE);
        diff.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.INVISIBLE);
        endTurn.setVisibility(View.INVISIBLE);

        Button dashBtn = findViewById(R.id.dashBtn);
        dashBtn.setOnClickListener(view -> finish());

        playButton.setOnClickListener(view -> {
            diff.setVisibility(View.INVISIBLE);
            start();
        });
    }

    private void start(){
        float vol = (float) user.getGameVolume() / User.MAX_VOLUME;

        enemyCaptured = SfxManager.createSfx(this, R.raw.enemy_captured, vol);
        playerCaptured = SfxManager.createSfx(this, R.raw.unit_captured, vol);


        pauseButton.setVisibility(View.VISIBLE);
        endTurn.setVisibility(View.VISIBLE);
        endTurn.setOnClickListener(view -> {

            endTurn();
        });

        setBoard();

        setState(0);

        updateTimer = new Timer();
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    updateBoard();
                });
            }
        }, 100,100);

    }

    private void endTurn(){
        for(Unit playerUnit : playerUnits){
            if(playerUnit.getCurrentPath() != null && playerUnit.getCurrentPath().size() > 0){
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
    }

    /**
     * Sets board variable, sets node graph, and shows tiles on screen
     * Runs once at beginning
     */
    private void setBoard(){
        board = new Tile[mapSizeX][mapSizeY];

        for(int x=0;x<board.length;x++){
            for(int y=0;y<board[x].length;y++){
                board[x][y] = new Tile("grass", x, y,null,"ic_grean",1,true,null);
            }
        }

        for(int x=0;x<grid.getChildCount();x++){
            for(int y=0;y<((LinearLayout)grid.getChildAt(x)).getChildCount();y++){
                buttons[x][y] = (ImageButton) ((LinearLayout)grid.getChildAt(x)).getChildAt(y);

                String img = board[x][y].getIcon();
                int res = getResources().getIdentifier(img, "drawable", "com.example.bullseye_android");
                buttons[x][y].setOnClickListener(tileListener);
            }
        }

        graph = Pathfinder.generatePathfindingGraph(graph, mapSizeX, mapSizeY);

        for(int i=1;i<=3;i++){
            Unit playerUnit = new Unit("example", i, 6, null, "ic_strat_img_caracal", 1, Owners.PLAYER, board);
            playerUnits.add(playerUnit);
        }

        EasyPatroller easyPatroller1 = new EasyPatroller("patroller", 0,0,null,"ic_strat_img_duck",1,new ArrayList<>(Arrays.asList(new Pair<>(0,3))),graph, board);
        EasyPatroller easyPatroller2 = new EasyPatroller("patroller", 1,3,null,"ic_strat_img_duck",1,new ArrayList<>(Arrays.asList(new Pair<>(1,0))),graph, board);
        EasyPatroller easyPatroller3 = new EasyPatroller("patroller", 2,0,null,"ic_strat_img_duck",1,new ArrayList<>(Arrays.asList(new Pair<>(2,3))),graph, board);
        EasyPatroller easyPatroller4 = new EasyPatroller("patroller", 3,3,null,"ic_strat_img_duck",1,new ArrayList<>(Arrays.asList(new Pair<>(3,0))),graph, board);
        EasyPatroller easyPatroller5 = new EasyPatroller("patroller", 4,0,null,"ic_strat_img_duck",1,new ArrayList<>(Arrays.asList(new Pair<>(4,3))),graph, board);
        computerUnits.add(easyPatroller1);
        computerUnits.add(easyPatroller2);
        computerUnits.add(easyPatroller3);
        computerUnits.add(easyPatroller4);
        computerUnits.add(easyPatroller5);
        startingAmount = computerUnits.size();

    }

    private void updateBoard(){
        int totalUnits = 0;
        for(int x=0;x<buttons.length;x++){
            for(int y=0;y<buttons[x].length;y++){
                ImageButton button = buttons[x][y];
                Tile tile = board[x][y];
                if(tile.getUnit()!=null) {
                    totalUnits++;
                    button.setImageResource(getResources().getIdentifier(tile.getUnit().getIcon(), "drawable", "com.example.bullseye_android"));
                }else {
                    button.setImageResource(0);
                }
            }
        }
        for(Unit playerUnit : playerUnits){
            if(playerUnit.isJustDied()){
                playerCaptured.start();
                playerUnit.setJustDied(false);
            }
            if(playerUnit.isDead()){
                playerUnits.remove(playerUnit);
                break;
            }
        }
        for(Unit computerUnit : computerUnits){
            if(computerUnit.isJustDied()){
                enemyCaptured.start();
                computerUnit.setJustDied(false);
            }
            if(computerUnit.isDead()){
                computerUnits.remove(computerUnit);
                break;
            }
        }
        checkWin();
    }

    private void checkWin(){
        String winner = "";
        String text = "";
        if(computerUnits.isEmpty()){
            winner = "player";
        }else if(playerUnits.isEmpty()){
            winner = "computer";
        }else{
            return;
        }
        switch(winner){
            case "player":
                //player wins
                text = "You Win!";
                confetti(konfetti, this.getApplicationContext());
                endText.setText(text);
                break;
            case "computer":
                //player loses
                text = "You Lost . . .";
                endText.setText(text);
                break;
            default:
                break;
        }
        updateTimer.cancel();
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

                    playAgain.setOnClickListener(view -> pregame());
                    backBtn.setOnClickListener(view -> finish());
                });
            }
        }, 600);

    }

    private void setState(int state){
        String text = "";
        switch(state){
            case 0:
                text = "click on one of your units";
                break;
            case 1:
                text = "click on a tile to move there";
                break;
            case 2:
                text = "click on an enemy to move there";
        }
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
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
                runOnUiThread(() -> {
                    updateBoard();
                });
            }
        }, 100,100);
        runOnUiThread(()-> {
            for (ImageButton[] buttonList : buttons) {
                for(ImageButton button : buttonList) {
                    button.setEnabled(true);
                }
            }
        });

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
    public int getMusicId() {
        return R.raw.strategysong;
    }
}