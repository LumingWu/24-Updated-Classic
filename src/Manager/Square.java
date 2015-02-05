/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Manager;

import Component.Tile;
import Component.Tile.TileType;
import UI.UI;
import UI.UI.ScreenState;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;

/**
 *
 * @author Luming Wu
 */
public class Square {

    private UI _ui;
    private final GameMode _mode;
    private final Tile[][] _square;

    private final int x;
    private final int y;

    private final ArrayList<Integer> _empty;

    private boolean _gamelost;

    public Square(UI ui, GameMode mode) {
        _ui = ui;
        _mode = mode;
        _square = new Tile[5][5];
        _empty = new ArrayList<Integer>();
        x = 5;
        y = 5;
        _gamelost = false;
        // Quick test to end game.
        /*
         for (int i = 0; i < x; i++) {
         for (int j = 1; j < y; j++) {
         Tile random = randomTile();
         random.setNumber(100);
         _square[i][j] = random;
         _ui.showNonEmptySpace(random.getNode(), i, j);
         }
         }
         */
        insert();
    }

    public void down() {
        if (!_gamelost) {
            _ui.getFrontEffects().getMovingCard().speedUp();
        }
    }

    public void left() {
        if (!_gamelost) {
            _ui.getFrontEffects().getMovingCard().left();
        }
    }

    public void right() {
        if (!_gamelost) {
            _ui.getFrontEffects().getMovingCard().right();
        }
    }

    public void normal() {
        if (!_gamelost) {
            _ui.getFrontEffects().getMovingCard().speedNormal();
        }
    }

    public void insert() {
        if (!_gamelost) {
            _gamelost = true;
            _empty.clear();
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    if (_square[i][j] == null) {
                        if (j == 0) {
                            _empty.add(i);
                        }
                        _gamelost = false;
                    }
                }
            }
        }
        if (!_gamelost) {
            Collections.shuffle(_empty);
            _ui.getFrontEffects().playCard(randomTile(), _empty.get(0), -2);
        } else {
            _ui.switchScreen(ScreenState.SCORE_STATE);
        }
    }

    private Tile randomTile() {
        int random = (int) (1 + Math.random() * 100);
        Tile that = new Tile();
        switch (_mode) {
            case NORMAL: {
                if (random <= 40) {
                    that.setType(TileType.DIAMOND);
                } else {
                    if (random <= 80) {
                        that.setType(TileType.CLUB);
                    } else {
                        if (random <= 90) {
                            that.setType(TileType.HEART);
                        } else {
                            that.setType(TileType.SPADE);
                        }
                    }
                }
                break;
            }
            case HARD: {
                if (random <= 35) {
                    that.setType(TileType.DIAMOND);
                } else {
                    if (random <= 70) {
                        that.setType(TileType.CLUB);
                    } else {
                        if (random <= 80) {
                            that.setType(TileType.HEART);
                        } else {
                            that.setType(TileType.SPADE);
                        }
                    }
                }
                break;
            }
            case INSANE: {
                if (random <= 25) {
                    that.setType(TileType.DIAMOND);
                } else {
                    if (random <= 50) {
                        that.setType(TileType.CLUB);
                    } else {
                        if (random <= 75) {
                            that.setType(TileType.HEART);
                        } else {
                            that.setType(TileType.SPADE);
                        }
                    }
                }
                break;
            }
        }
        if (that.getType() == TileType.DIAMOND || that.getType() == TileType.CLUB) {
            that.setNumber((int) (1 + Math.random() * 10));
        } else {
            if (that.getType() == TileType.SPADE) {
                ArrayList<Integer> list = new ArrayList<Integer>();
                for (int i = 1; i < 11; i++) {
                    boolean add = false;
                    for (int j = 0; j < x; j++) {
                        int k = 0;
                        while (k < y) {
                            if (_square[j][k] != null) {
                                if (_square[j][k].getNumber() % i == 0) {
                                    add = true;
                                }
                                break;
                            }
                            k = k + 1;
                            if (k == y && !add) {
                                add = true;
                            }
                        }
                    }
                    if (add) {
                        list.add(i);
                    }
                }
                if (!list.isEmpty()) {
                    Collections.shuffle(list);
                    that.setNumber(list.get(0));
                } else {
                    that.setNumber((int) (1 + Math.random() * 10));
                }
            } else {
                ArrayList<Integer> list = new ArrayList<Integer>();
                for (int i = 1; i < 11; i++) {
                    boolean add = false;
                    for (int j = 0; j < x; j++) {
                        int k = 0;
                        while (k < y) {
                            if (_square[j][k] != null) {
                                if (_square[j][k].getNumber() * i <= 48) {
                                    add = true;
                                }
                                break;
                            }
                            k = k + 1;
                            if (k == y && !add) {
                                add = true;
                            }
                        }
                    }
                    if (add) {
                        list.add(i);
                    }
                }
                if (!list.isEmpty()) {
                    Collections.shuffle(list);
                    that.setNumber(list.get(0));
                } else {
                    that.setNumber((int) (1 + Math.random() * 10));
                }
            }
        }
        return that;
    }

    public void gameLost() {
        _gamelost = true;
    }

    public Tile[][] getSquare() {
        return _square;
    }

    public enum GameMode {

        NORMAL, HARD, INSANE
    }

    public GameMode getGameMode() {
        return _mode;
    }

    @Override
    public String toString() {
        return Arrays.deepToString(_square);
    }
}
