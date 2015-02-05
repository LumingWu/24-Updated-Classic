/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphic;

import Component.Tile;
import Component.Tile.TileType;
import UI.UI;
import java.io.File;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;

/**
 *
 * @author Luming Wu
 */
public class FrontEffects extends StackPane {

    private UI _ui;

    private CardEffect _cardEffect;

    private ArrayList<String> _explodeeffects;

    private int _defaultSpeed;

    private Label _speedNode;
    private long _speedTimerCurrent;
    
    private AudioClip _stuckSound;
    private AudioClip _speedSound;

    public FrontEffects(UI ui) {
        _ui = ui;
        setPickOnBounds(false);
        _explodeeffects = new ArrayList<String>();
        for (int i = 1; i < 31; i++) {
            _explodeeffects.add("effect" + i);
        }
        _defaultSpeed = 1;
        _stuckSound = new AudioClip(new File(System.getProperty("user.dir") + "/mus/" + "stuck" + ".wav").toURI().toString());
        _speedSound = new AudioClip(new File(System.getProperty("user.dir") + "/mus/" + "increasespeed" + ".wav").toURI().toString());
        initialSpeed();
    }

    private void initialSpeed() {
        _speedNode = new Label();
        _speedNode.setVisible(false);
        _speedNode.setGraphic(loadImage("speed"));
        _speedNode.setContentDisplay(ContentDisplay.CENTER);
        _speedNode.setFont(Font.font("Square721 BT", 36));
        _speedNode.setStyle("-fx-text-fill:white;");
        _speedNode.setText("\n1");
        getChildren().add(_speedNode);
        _speedNode.setTranslateX(576);
        _speedNode.setTranslateY(-216);
    }

    public void showSpeed(boolean value) {
        _speedNode.setVisible(value);
    }

    public void setSpeed(int value) {
        _speedSound.play();
        _speedNode.setText("\n" + (_defaultSpeed - 1) + " + " + 1);
        _speedTimerCurrent = 0;
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (_speedTimerCurrent == 0) {
                    _speedTimerCurrent = now;
                }
                if ((now - _speedTimerCurrent) >= 1000000000) {
                    _speedNode.setText("\n" + value);
                    stop();
                }
            }
        }.start();
    }

    public void playCard(Tile tile, int x, int y) {
        _cardEffect = new CardEffect(_ui, tile, x, y);
        getChildren().add(_cardEffect);
        _cardEffect.start();
    }

    public CardEffect getMovingCard() {
        return _cardEffect;
    }

    public void changeDefaultSpeed(int newspeed) {
        _defaultSpeed = newspeed;
    }

    public class CardEffect extends Label {

        private AnimationTimer _timer;

        private long _current = 0;
        private long _speed = 1;

        private int xx;
        private int yy;

        private int _futureX;

        private CardEffect _me = this;

        public CardEffect(UI ui, Tile tile, int x, int y) {
            setOpacity(0);
            setGraphic(tile.getCopyNode());
            speedNormal();
            _futureX = (x - 2) * 144;
            _timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if (_current == 0) {
                        _current = now;
                        _cardEffect.setTranslateX((x - 2) * 144);
                        _cardEffect.setTranslateY((y - 1.5) * 144);
                        setOpacity(1);
                    }
                    if ((now - _current) >= 1000000000 / 60) {
                        _current = now;
                        _cardEffect.setTranslateY(_cardEffect.getTranslateY() + _speed);
                        xx = (int) (_cardEffect.getTranslateX() / 144 + 2);
                        yy = (int) (_cardEffect.getTranslateY() / 144 + 1.5);
                        if (yy + 1 > 4) {
                            _ui.getSquare()[xx][4] = tile;
                            _ui.showNonEmptySpace(tile.getNode(), xx, 4);
                            _stuckSound.play();
                            _ui.getSQUARE().insert();
                            _ui.getFrontEffects().getChildren().remove(_me);
                            stop();
                        } else {
                            if (_cardEffect.getTranslateY() / 144 + 1.5 < 0) {
                                yy = yy - 1;
                            }
                            if (yy >= -1) {
                                if (_ui.getSquare()[xx][yy + 1] != null) {
                                    if ((_ui.getSquare()[xx][yy + 1].getNumber() <= 48 || tile.getType() == TileType.CLUB) && tile.getType() != TileType.SPADE) {
                                        _ui.getSquare()[xx][yy + 1].operation(tile);
                                        _ui.showNonEmptySpace(_ui.getSquare()[xx][yy + 1].getNode(), xx, yy + 1);
                                        if (_ui.getSquare()[xx][yy + 1].getNumber() == 24) {
                                            _ui.getSquare()[xx][yy + 1] = null;
                                            _ui.showEmptySpace(xx, yy + 1);
                                            _ui.updateBoard();
                                            playExplosion(xx, yy + 1);
                                        }
                                    } else {
                                        if (tile.getType() == TileType.SPADE) {
                                            if (_ui.getSquare()[xx][yy + 1].getNumber() % tile.getNumber() == 0) {
                                                _ui.getSquare()[xx][yy + 1].operation(tile);
                                                _ui.showNonEmptySpace(_ui.getSquare()[xx][yy + 1].getNode(), xx, yy + 1);
                                                if (_ui.getSquare()[xx][yy + 1].getNumber() == 24) {
                                                    _ui.getSquare()[xx][yy + 1] = null;
                                                    _ui.showEmptySpace(xx, yy + 1);
                                                    _ui.updateBoard();
                                                    playExplosion(xx, yy + 1);
                                                }
                                            } else {
                                                if (yy >= 0) {
                                                    _ui.getSquare()[xx][yy] = tile;
                                                    _ui.showNonEmptySpace(tile.getNode(), xx, yy);
                                                    _stuckSound.play();
                                                }
                                                else{
                                                    _ui.getSQUARE().gameLost();
                                                }
                                            }
                                        } else {
                                            if (yy >= 0) {
                                                _ui.getSquare()[xx][yy] = tile;
                                                _ui.showNonEmptySpace(tile.getNode(), xx, yy);
                                                _stuckSound.play();
                                            }
                                            else{
                                                _ui.getSQUARE().gameLost();
                                            }
                                        }
                                    }
                                    _ui.getFrontEffects().getChildren().remove(_me);
                                    _ui.getSQUARE().insert();
                                    stop();
                                }
                            }
                        }
                    }
                }
            };
            _timer.start();
        }

        public void start() {
            _timer.start();
        }

        public void left() {
            if (_futureX <= 288 && _futureX >= -144) {
                _futureX = _futureX - 144;
                _cardEffect.setTranslateX(_futureX);
            }
        }

        public void right() {
            if (_futureX >= -288 && _futureX <= 144) {
                _futureX = _futureX + 144;
                _cardEffect.setTranslateX(_futureX);
            }
        }

        public void speedUp() {
            if (_defaultSpeed * +2 <= 10) {
                _speed = _defaultSpeed + 2;
            }
        }

        public void speedNormal() {
            _speed = _defaultSpeed;
        }
    }

    public void playExplosion(int x, int y) {
        SquareEffect node = new SquareEffect(this);
        node.setTranslateX((x - 2) * 144);
        node.setTranslateY((y - 1.5) * 144);
        getChildren().add(node);
    }

    private class SquareEffect extends ImageView {

        private long _current;
        private AnimationTimer _timer;
        private int _counter;
        private SquareEffect _me;

        private SquareEffect(FrontEffects front) {
            _me = this;
            _current = 0;
            _counter = 1;
            setImage(new Image("file:img/" + _explodeeffects.get((int) (Math.random() * 30)) + ".png"));
            _timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if ((now - _current) / 1000000 >= 500 / 30) {
                        setImage(new Image("file:img/" + _explodeeffects.get((int) (Math.random() * 30)) + ".png"));
                        _counter = _counter + 1;
                        if (_counter == 30) {
                            front.getChildren().remove(_me);
                            _timer.stop();
                        }
                    }
                }
            };
            _timer.start();
        }
    }

    private ImageView loadImage(String name) {
        return new ImageView(new Image("file:img/" + name + ".png"));
    }
}
