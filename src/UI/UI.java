package UI;

import Component.Tile;
import FX.SmartButton;
import Graphic.BackEffects;
import Graphic.FrontEffects;
import Manager.Square;
import Manager.Square.GameMode;
import java.io.File;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Collections;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * A creative 24 game.
 *
 * @author Luming Wu
 */
public class UI {

    private final Stage _mainstage;
    private final BorderPane _mainPane;
    private Square _square;
    private final BackEffects _backeffects;
    private final FrontEffects _fronteffects;

    private StackPane _coverPane;
    private long _titlecurrent;
    private long _titlecounter;
    private double _titlescaleamount;
    private double _titlescaleadder;

    private BorderPane _selectionPane;
    private SmartButton _thatButton;
    private AnimationTimer _selectTimer;
    private long _selectcurrent;
    private long _selectcounter;
    private double _selectscaleamount;
    private double _selectscaleadder;
    private double _selectrotateamount;
    private double _selectrotateadder;

    private BorderPane _helpPane;
    private int _pageNumber;
    private Label _pageOne;
    private Label _pageTwo;
    private Label _pageThree;

    private BorderPane _gamePane;
    private Label _scoreBoard;
    private AnimationTimer _gameTimer;
    private long _gameCurrent;
    private int _gameCounter;

    private BorderPane _scorePane;
    private Label _stats;

    private ArrayList<MediaPlayer> _musicPlayer;

    private AudioClip _buttonSound;
    private AudioClip _24Sound;

    public UI(Stage mainstage) {
        _mainstage = mainstage;
        _mainPane = new BorderPane();
        _backeffects = new BackEffects(this);
        initialUI();
        refreshScreen();
        _fronteffects = new FrontEffects(this);
        _musicPlayer = new ArrayList<MediaPlayer>();
        _musicPlayer.add(new MediaPlayer(new Media(new File(System.getProperty("user.dir") + "/mus/" + "Five Armies" + ".mp3").toURI().toString())));
        _musicPlayer.get(0).setVolume(0.2);
        _musicPlayer.add(new MediaPlayer(new Media(new File(System.getProperty("user.dir") + "/mus/" + "Welcome to the Show" + ".mp3").toURI().toString())));
        _musicPlayer.get(1).setVolume(0.5);
        _musicPlayer.add(new MediaPlayer(new Media(new File(System.getProperty("user.dir") + "/mus/" + "Rollin at 5" + ".mp3").toURI().toString())));
        _musicPlayer.get(2).setVolume(0.4);
        Collections.shuffle(_musicPlayer);
        for (int i = 0; i < 3; i++) {
            _musicPlayer.get(i).setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 3; j++) {
                        _musicPlayer.get(j).stop();
                    }
                    Collections.shuffle(_musicPlayer);
                    _musicPlayer.get(0).play();
                }
            });
        }
        _musicPlayer.get(0).play();
        _buttonSound = new AudioClip(new File(System.getProperty("user.dir") + "/mus/" + "button" + ".mp3").toURI().toString());
        _24Sound = new AudioClip(new File(System.getProperty("user.dir") + "/mus/" + "24" + ".wav").toURI().toString());
    }

    private void initialUI() {
        initialCoverScreen();
        initialSelectionScreen();
        initialHelpScreen();
        initialGameScreen();
        initialScoreScreen();
        switchScreen(ScreenState.COVER_STATE);
    }

    private void initialCoverScreen() {
        _coverPane = new StackPane();
        _coverPane.setAlignment(Pos.CENTER);

        BorderPane background = new BorderPane();

        BorderPane option = new BorderPane();
        VBox options = new VBox();
        options.setAlignment(Pos.CENTER);
        options.setSpacing(10);
        option.setCenter(options);

        Label title = new Label();
        _titlecurrent = 0;
        _titlecounter = 1;
        _titlescaleamount = 1.0;
        // Change Speed
        _titlescaleadder = 0.001;
        AnimationTimer titletimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (_titlecurrent == 0) {
                    _titlecurrent = now;
                }
                // Change Time Per Second
                if ((now - _titlecurrent) / 1000000000 * 60 >= _titlecounter) {
                    _titlescaleamount = _titlescaleamount + _titlescaleadder;
                    title.setScaleX(_titlescaleamount);
                    title.setScaleY(_titlescaleamount);
                    // Total Change times
                    if (_titlecounter == 500) {
                        _titlescaleadder = _titlescaleadder * -1;
                        _titlecurrent = now;
                        _titlecounter = 0;
                    }
                    _titlecounter = _titlecounter + 1;
                }
            }
        };
        title.setGraphic(loadImage("title"));
        title.setOnMouseEntered((MouseEvent event) -> {
            titletimer.start();
        });
        title.setOnMouseExited((MouseEvent event) -> {
            titletimer.stop();
            title.setScaleX(1.0);
            title.setScaleY(1.0);
            title.setRotate(0.0);
            _titlecurrent = 0;
            _titlecounter = 1;
            _titlescaleamount = 1.0;
            _titlescaleadder = 0.001;
        });
        options.getChildren().add(title);

        SmartButton start = new SmartButton();
        start.setPressedNode(loadImage("startactive"));
        start.setReleasedNode(loadImage("startstatic"));
        start.setOnMousePressed((MouseEvent event) -> {
            _buttonSound.play();
            start.flip();
        });
        start.setOnMouseReleased((MouseEvent event) -> {
            if (event.getX() <= start.getWidth() && event.getX() >= -start.getWidth() && event.getY() <= start.getHeight() && event.getY() >= -start.getHeight()) {
                switchScreen(ScreenState.SELECTION_STATE);
            }
            start.flip();
        });
        options.getChildren().add(start);

        SmartButton help = new SmartButton();
        help.setPressedNode(loadImage("helpactive"));
        help.setReleasedNode(loadImage("helpstatic"));
        help.setOnMousePressed((MouseEvent event) -> {
            help.flip();
            _buttonSound.play();
        });
        help.setOnMouseReleased((MouseEvent event) -> {
            if (event.getX() <= help.getWidth() && event.getX() >= -help.getWidth() && event.getY() <= help.getHeight() && event.getY() >= -help.getHeight()) {
                switchScreen(ScreenState.HELP_STATE);
            }
            help.flip();
        });
        options.getChildren().add(help);

        SmartButton exit = new SmartButton();
        exit.setPressedNode(loadImage("exitactive"));
        exit.setReleasedNode(loadImage("exitstatic"));
        exit.setOnMousePressed((MouseEvent event) -> {
            exit.flip();
            _buttonSound.play();
        });
        exit.setOnMouseReleased((MouseEvent event) -> {
            if (event.getX() <= exit.getWidth() && event.getX() >= -exit.getWidth() && event.getY() <= exit.getHeight() && event.getY() >= -exit.getHeight()) {
                _mainstage.close();
            }
            exit.flip();
        });
        options.getChildren().add(exit);

        _coverPane.getChildren().addAll(background, option);
    }

    private void initialSelectionScreen() {
        _selectionPane = new BorderPane();

        HBox center = new HBox();
        center.setAlignment(Pos.CENTER);
        center.setSpacing(Screen.getPrimary().getVisualBounds().getWidth() / 5);

        SmartButton normal = new SmartButton();
        normal.setPressedNode(loadImage("normalactive"));
        normal.setReleasedNode(loadImage("normalstatic"));
        normal.setOnMousePressed((MouseEvent event) -> {
            normal.flip();
            _buttonSound.play();
        });
        normal.setOnMouseReleased((MouseEvent event) -> {
            if (event.getX() <= normal.getWidth() && event.getX() >= -normal.getWidth() && event.getY() <= normal.getHeight() && event.getY() >= -normal.getHeight()) {
                _square = new Square(this, GameMode.NORMAL);
                switchScreen(ScreenState.GAME_STATE);
            }
            normal.flip();
        });
        normal.setOnMouseEntered((MouseEvent event) -> {
            _thatButton = normal;
            _selectTimer.start();
        });
        normal.setOnMouseExited((MouseEvent event) -> {
            _selectTimer.stop();
            resetOrSetEffect();
        });
        center.getChildren().add(normal);

        SmartButton hard = new SmartButton();
        hard.setPressedNode(loadImage("hardactive"));
        hard.setReleasedNode(loadImage("hardstatic"));
        hard.setOnMousePressed((MouseEvent event) -> {
            hard.flip();
            _buttonSound.play();
        });
        hard.setOnMouseReleased((MouseEvent event) -> {
            if (event.getX() <= hard.getWidth() && event.getX() >= -hard.getWidth() && event.getY() <= hard.getHeight() && event.getY() >= -hard.getHeight()) {
                _square = new Square(this, GameMode.HARD);
                switchScreen(ScreenState.GAME_STATE);
            }
            hard.flip();
        });
        hard.setOnMouseEntered((MouseEvent event) -> {
            _thatButton = hard;
            _selectTimer.start();
        });
        hard.setOnMouseExited((MouseEvent event) -> {
            _selectTimer.stop();
            resetOrSetEffect();
        });
        center.getChildren().add(hard);

        SmartButton insane = new SmartButton();
        insane.setPressedNode(loadImage("insaneactive"));
        insane.setReleasedNode(loadImage("insanestatic"));
        insane.setOnMousePressed((MouseEvent event) -> {
            insane.flip();
            _buttonSound.play();
        });
        insane.setOnMouseReleased((MouseEvent event) -> {
            if (event.getX() <= insane.getWidth() && event.getX() >= -insane.getWidth() && event.getY() <= insane.getHeight() && event.getY() >= -insane.getHeight()) {
                _square = new Square(this, GameMode.INSANE);
                switchScreen(ScreenState.GAME_STATE);
            }
            insane.flip();
        });
        insane.setOnMouseEntered((MouseEvent event) -> {
            _thatButton = insane;
            _selectTimer.start();
        });
        insane.setOnMouseExited((MouseEvent event) -> {
            _selectTimer.stop();
            resetOrSetEffect();
        });
        center.getChildren().add(insane);

        resetOrSetEffect();
        _selectTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (_selectcurrent == 0) {
                    _selectcurrent = now;
                }
                if ((now - _selectcurrent) / 1000000000 * 60 >= _selectcounter) {
                    if (_selectcounter <= 20) {
                        _selectscaleamount = _selectscaleamount + _selectscaleadder;
                        _selectrotateamount = _selectrotateamount + _selectrotateadder;
                        _thatButton.setScaleX(_selectscaleamount);
                        _thatButton.setScaleY(_selectscaleamount);
                        _thatButton.setRotate(_selectrotateamount);
                    }
                    if (_selectcounter == 10) {
                        _selectscaleadder = _selectscaleadder * -1;
                        _selectrotateadder = _selectrotateadder * -1;
                    }
                    if (_selectcounter == 20) {
                        resetOrSetEffect();
                        _selectcounter = 0;
                    }
                    _selectcounter = _selectcounter + 1;
                }
            }
        };

        HBox bottom = new HBox();
        bottom.setAlignment(Pos.CENTER);
        bottom.setSpacing(Screen.getPrimary().getVisualBounds().getWidth() / 1.5);
        bottom.setPadding(new Insets(Screen.getPrimary().getVisualBounds().getHeight() / 15, Screen.getPrimary().getVisualBounds().getWidth() / 15,
                Screen.getPrimary().getVisualBounds().getHeight() / 15, Screen.getPrimary().getVisualBounds().getWidth() / 15));

        SmartButton back = new SmartButton();
        back.setPressedNode(loadImage("backactive"));
        back.setReleasedNode(loadImage("backstatic"));
        back.setOnMousePressed((MouseEvent event) -> {
            back.flip();
            _buttonSound.play();
        });
        back.setOnMouseReleased((MouseEvent event) -> {
            if (event.getX() <= back.getWidth() && event.getX() >= -back.getWidth() && event.getY() <= back.getHeight() && event.getY() >= -back.getHeight()) {
                switchScreen(ScreenState.COVER_STATE);
            }
            back.flip();
        });
        bottom.getChildren().add(back);

        Label empty = new Label();
        empty.setGraphic(loadImage("empty"));
        bottom.getChildren().add(empty);

        _selectionPane.setCenter(center);
        _selectionPane.setBottom(bottom);
    }

    private void resetOrSetEffect() {
        _selectcurrent = 0;
        _selectcounter = 1;
        _selectscaleamount = 1;
        _selectrotateamount = 0;
        switch ((int) (Math.random() * 4)) {
            case 0: {
                _selectrotateadder = 0.01;
                break;
            }
            case 1: {
                _selectrotateadder = -0.01;
                break;
            }
            case 2: {
                _selectrotateadder = 0.02;
                break;
            }
            case 3: {
                _selectrotateadder = -0.02;
                break;
            }
        }
        switch ((int) (Math.random() * 4)) {
            case 0: {
                _selectscaleadder = 0.01;
                break;
            }
            case 1: {
                _selectscaleadder = 0.02;
                break;
            }
            case 2: {
                _selectscaleadder = 0.03;
                break;
            }
            case 3: {
                _selectscaleadder = 0.04;
                break;
            }
        }
        if (_thatButton != null) {
            _thatButton.setScaleX(_selectscaleamount);
            _thatButton.setScaleY(_selectscaleamount);
            _thatButton.setRotate(_selectrotateamount);
        }
    }

    private void initialHelpScreen() {
        _helpPane = new BorderPane();
        _pageNumber = 1;

        _pageOne = new Label();
        _pageOne.setGraphic(loadImage("pageone"));
        _helpPane.setCenter(_pageOne);

        _pageTwo = new Label();
        _pageTwo.setGraphic(loadImage("pagetwo"));

        _pageThree = new Label();
        _pageThree.setGraphic(loadImage("pagethree"));

        BorderPane bottom = new BorderPane();

        HBox options = new HBox();
        options.setAlignment(Pos.CENTER);
        options.setSpacing(Screen.getPrimary().getVisualBounds().getWidth() / 1.5);
        options.setPadding(new Insets(Screen.getPrimary().getVisualBounds().getHeight() / 15, Screen.getPrimary().getVisualBounds().getWidth() / 15,
                Screen.getPrimary().getVisualBounds().getHeight() / 15, Screen.getPrimary().getVisualBounds().getWidth() / 15));
        bottom.setCenter(options);

        SmartButton back = new SmartButton();
        back.setPressedNode(loadImage("backactive"));
        back.setReleasedNode(loadImage("backstatic"));
        back.setOnMousePressed((MouseEvent event) -> {
            back.flip();
            _buttonSound.play();
        });
        back.setOnMouseReleased((MouseEvent event) -> {
            if (event.getX() <= back.getWidth() && event.getX() >= -back.getWidth() && event.getY() <= back.getHeight() && event.getY() >= -back.getHeight()) {
                helpPageAction(PageAction.BACK);
            }
            back.flip();
        });
        options.getChildren().add(back);

        SmartButton next = new SmartButton();
        next.setPressedNode(loadImage("nextactive"));
        next.setReleasedNode(loadImage("nextstatic"));
        next.setOnMousePressed((MouseEvent event) -> {
            next.flip();
            _buttonSound.play();
        });
        next.setOnMouseReleased((MouseEvent event) -> {
            if (event.getX() <= next.getWidth() && event.getX() >= -next.getWidth() && event.getY() <= next.getHeight() && event.getY() >= -next.getHeight()) {
                helpPageAction(PageAction.NEXT);
            }
            next.flip();
        });
        options.getChildren().add(next);

        _helpPane.setBottom(bottom);
    }

    private void helpPageAction(PageAction action) {
        switch (action) {
            case NEXT: {
                if (_pageNumber < 3) {
                    _pageNumber = _pageNumber + 1;
                }
                break;
            }
            case BACK: {
                if (_pageNumber > 1) {
                    _pageNumber = _pageNumber - 1;
                } else {
                    switchScreen(ScreenState.COVER_STATE);
                }
                break;
            }
        }
        switch (_pageNumber) {
            case 1: {
                _helpPane.setCenter(_pageOne);
                break;
            }
            case 2: {
                _helpPane.setCenter(_pageTwo);
                break;
            }
            case 3: {
                _helpPane.setCenter(_pageThree);
                break;
            }
        }
    }

    private enum PageAction {

        NEXT, BACK
    }

    private void initialGameScreen() {
        _gamePane = new BorderPane();

        Font font = Font.font("Square721 BT", 36);

        HBox top = new HBox();
        top.setAlignment(Pos.CENTER);
        top.setSpacing(Screen.getPrimary().getVisualBounds().getWidth() / 2);

        _scoreBoard = new Label("\n0");
        _scoreBoard.setGraphic(loadImage("score"));
        _scoreBoard.setContentDisplay(ContentDisplay.CENTER);
        _scoreBoard.setTextAlignment(TextAlignment.CENTER);
        _scoreBoard.setFont(font);
        top.getChildren().add(_scoreBoard);

        Label timeBoard = new Label("\n00:00:00");
        timeBoard.setGraphic(loadImage("time"));
        timeBoard.setContentDisplay(ContentDisplay.CENTER);
        timeBoard.setTextAlignment(TextAlignment.CENTER);
        timeBoard.setFont(font);
        top.getChildren().add(timeBoard);

        _gameCurrent = 0;
        _gameCounter = 0;
        _gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (_gameCurrent == 0) {
                    _gameCurrent = now;
                }
                if ((now - _gameCurrent) / 1000000000 >= 1) {
                    _gameCurrent = now;
                    _gameCounter = _gameCounter + 1;
                    String text = "\n";
                    int hour = _gameCounter / 3600;
                    if (hour >= 10) {
                        text = text + hour + ":";
                    } else {
                        text = text + "0" + hour + ":";
                    }
                    int minute = (_gameCounter - 3600 * hour) / 60;
                    if (minute >= 10) {
                        text = text + minute + ":";
                    } else {
                        text = text + "0" + minute + ":";
                    }
                    int second = _gameCounter - 3600 * hour - 60 * minute;
                    if (second >= 10) {
                        text = text + second;
                    } else {
                        text = text + "0" + second;
                    }
                    timeBoard.setText(text);
                    switch (_gameCounter) {
                        case 60: {
                            _fronteffects.changeDefaultSpeed(2);
                            _fronteffects.setSpeed(2);
                            break;
                        }
                        case 90: {
                            _fronteffects.changeDefaultSpeed(3);
                            _fronteffects.setSpeed(3);
                            break;
                        }
                        case 180: {
                            _fronteffects.changeDefaultSpeed(4);
                            _fronteffects.setSpeed(4);
                            break;
                        }
                        case 240: {
                            _fronteffects.changeDefaultSpeed(5);
                            _fronteffects.setSpeed(5);
                            break;
                        }
                        case 400: {
                            _fronteffects.changeDefaultSpeed(6);
                            _fronteffects.setSpeed(6);
                            break;
                        }
                        case 600: {
                            _fronteffects.changeDefaultSpeed(7);
                            _fronteffects.setSpeed(7);
                            break;
                        }
                        case 800: {
                            _fronteffects.changeDefaultSpeed(8);
                            _fronteffects.setSpeed(8);
                            break;
                        }
                        case 1000: {
                            _fronteffects.changeDefaultSpeed(9);
                            _fronteffects.setSpeed(9);
                            break;
                        }
                        case 1200: {
                            _fronteffects.changeDefaultSpeed(10);
                            _fronteffects.setSpeed(10);
                            break;
                        }
                        case 1400: {
                            _fronteffects.changeDefaultSpeed(11);
                            _fronteffects.setSpeed(11);
                            break;
                        }
                        case 1600: {
                            _fronteffects.changeDefaultSpeed(12);
                            _fronteffects.setSpeed(12);
                            break;
                        }
                        case 1800: {
                            _fronteffects.changeDefaultSpeed(13);
                            _fronteffects.setSpeed(13);
                            break;
                        }
                        case 2000: {
                            _fronteffects.changeDefaultSpeed(14);
                            _fronteffects.setSpeed(14);
                            break;
                        }
                        case 2200: {
                            _fronteffects.changeDefaultSpeed(15);
                            _fronteffects.setSpeed(15);
                            break;
                        }
                        case 2400: {
                            _fronteffects.changeDefaultSpeed(16);
                            _fronteffects.setSpeed(16);
                            break;
                        }
                        case 2600: {
                            _fronteffects.changeDefaultSpeed(17);
                            _fronteffects.setSpeed(17);
                            break;
                        }
                        case 2800: {
                            _fronteffects.changeDefaultSpeed(18);
                            _fronteffects.setSpeed(18);
                            break;
                        }
                        case 3000: {
                            _fronteffects.changeDefaultSpeed(19);
                            _fronteffects.setSpeed(19);
                            break;
                        }
                        case 3200: {
                            _fronteffects.changeDefaultSpeed(20);
                            _fronteffects.setSpeed(20);
                            break;
                        }
                    }
                }
            }
        };

        HBox center = new HBox();
        center.setAlignment(Pos.CENTER);
        for (int i = 0; i < 5; i++) {
            VBox column = new VBox();
            column.setAlignment(Pos.CENTER);
            for (int j = 0; j < 5; j++) {
                column.getChildren().add(new Label());
            }
            center.getChildren().add(column);
        }

        _gamePane.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.NUMPAD5 || event.getCode() == KeyCode.S) {
                _square.down();
            } else {
                if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.NUMPAD4 || event.getCode() == KeyCode.A) {
                    _square.left();
                } else {
                    if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.NUMPAD6 || event.getCode() == KeyCode.D) {
                        _square.right();
                    }
                }
            }
        });

        _gamePane.setOnKeyReleased((KeyEvent event) -> {
            _square.normal();
        });

        _gamePane.setTop(top);
        _gamePane.setCenter(center);
    }

    public void showEmptySpace(int i, int j) {
        HBox square = (HBox) _gamePane.getCenter();
        ((Label) ((VBox) square.getChildren().get(i)).getChildren().get(j)).setGraphic(loadImage("space"));
    }

    public void showNonEmptySpace(Label image, int i, int j) {
        HBox square = (HBox) _gamePane.getCenter();
        ((VBox) square.getChildren().get(i)).getChildren().set(j, image);
    }

    public void refreshScreen() {
        HBox square = (HBox) _gamePane.getCenter();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                ((Label) ((VBox) square.getChildren().get(i)).getChildren().get(j)).setGraphic(loadImage("space"));
            }
        }

    }

    private void initialScoreScreen() {
        _scorePane = new BorderPane();

        VBox options = new VBox();
        options.setAlignment(Pos.CENTER);
        options.setSpacing(10);
        options.setMaxWidth(600);
        _scorePane.setCenter(options);
        
        _stats = new Label("Stats");
        _stats.setTextAlignment(TextAlignment.CENTER);
        _stats.setWrapText(true);
        _stats.setStyle("-fx-font-fill:black");
        _stats.setFont(Font.font("Square721 BT", 36));
        options.getChildren().add(_stats);

        SmartButton exit = new SmartButton();
        exit.setPressedNode(loadImage("exitactive"));
        exit.setReleasedNode(loadImage("exitstatic"));
        exit.setOnMousePressed((MouseEvent event) -> {
            exit.flip();
            _buttonSound.play();
        });
        exit.setOnMouseReleased((MouseEvent event) -> {
            if (event.getX() <= exit.getWidth() && event.getX() >= -exit.getWidth() && event.getY() <= exit.getHeight() && event.getY() >= -exit.getHeight()) {
                _mainstage.close();
            }
            exit.flip();
        });
        options.getChildren().add(exit);
    }

    public void switchScreen(ScreenState state) {
        switch (state) {
            case COVER_STATE: {
                _mainPane.setCenter(_coverPane);
                break;
            }
            case SELECTION_STATE: {
                _mainPane.setCenter(_selectionPane);
                break;
            }
            case HELP_STATE: {
                _mainPane.setCenter(_helpPane);
                break;
            }
            case GAME_STATE: {
                _fronteffects.showSpeed(true);
                _mainPane.setCenter(_gamePane);
                _gamePane.requestFocus();
                _gameTimer.start();
                break;
            }
            case SCORE_STATE: {
                String text = "";
                int hour = _gameCounter / 3600;
                if (hour >= 10) {
                    text = text + hour + ":";
                } else {
                    text = text + "0" + hour + ":";
                }
                int minute = (_gameCounter - 3600 * hour) / 60;
                if (minute >= 10) {
                    text = text + minute + ":";
                } else {
                    text = text + "0" + minute + ":";
                }
                int second = _gameCounter - 3600 * hour - 60 * minute;
                if (second >= 10) {
                    text = text + second;
                } else {
                    text = text + "0" + second;
                }
                int subindex = 0;
                while (_scoreBoard.getText().substring(subindex, subindex + 1).equals(" ")) {
                    subindex = subindex + 1;
                }
                _stats.setText(_stats.getText() + "\nScore: " + _scoreBoard.getText().trim() + "\nTime: " + text + "\nSpeed: " + ((Label) _fronteffects.getChildren().remove(0)).getText().trim() + "\nMode: " + _square.getGameMode());

                _gameTimer.stop();
                _fronteffects.setOpacity(0);
                _fronteffects.setStyle("-fx-background-color:white");
                new AnimationTimer() {
                    private long scoreboardcurrent = 0;

                    @Override
                    public void handle(long now) {
                        if (scoreboardcurrent == 0) {
                            scoreboardcurrent = now;
                        }
                        if ((now - scoreboardcurrent) >= 1000000000 / 2) {
                            scoreboardcurrent = now;
                            if (_fronteffects.getOpacity() < 1) {
                                _fronteffects.setOpacity(_fronteffects.getOpacity() + 0.1);
                                _scorePane.setOpacity(_fronteffects.getOpacity());
                            } else {
                                _backeffects.setStyle("-fx-background-color:white");
                                _fronteffects.setStyle(null);
                                _fronteffects.setPickOnBounds(false);
                                _mainPane.setCenter(_scorePane);
                                stop();
                            }
                        }
                    }

                }.start();
                break;

            }
        }
    }

    public enum ScreenState {

        COVER_STATE, SELECTION_STATE, HELP_STATE, GAME_STATE, SCORE_STATE
    }

    public BorderPane getRoot() {
        return _mainPane;
    }

    public BorderPane getGamePane() {
        return _gamePane;
    }

    public Tile[][] getSquare() {
        return _square.getSquare();
    }

    public BackEffects getBackEffects() {
        return _backeffects;
    }

    public FrontEffects getFrontEffects() {
        return _fronteffects;
    }

    public Square getSQUARE() {
        return _square;
    }

    public void updateBoard() {
        _24Sound.play();
        int score = Integer.parseInt(_scoreBoard.getText().substring("\n".length()));
        _scoreBoard.setText("\n" + score + " + " + 24);
        new AnimationTimer() {
            private long boardcurrent = 0;

            @Override
            public void handle(long now) {
                if (boardcurrent == 0) {
                    boardcurrent = now;
                }
                if ((now - boardcurrent) >= 1000000000) {
                    _scoreBoard.setText("\n" + (score + 24));
                    stop();
                }
            }

        }.start();
    }

    private ImageView loadImage(String name) {
        return new ImageView(new Image("file:img/" + name + ".png"));
    }
}
