/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Component;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

/**
 *
 * @author Luming Wu
 */
public class Tile {

    private TileType _type;
    private int _number;

    private Label _node = null;

    public Tile() {
    }

    public Tile(TileType type) {
        _type = type;
    }

    public Tile(int number) {
        _number = number;
    }

    public Tile(TileType type, int number) {
        _type = type;
        _number = number;
    }

    public void setType(TileType type) {
        _type = type;
    }

    public void setNumber(int number) {
        _number = number;
    }

    public TileType getType() {
        return _type;
    }

    public int getNumber() {
        return _number;
    }

    public Label getNode() {
        if (_node == null) {
            _node = new Label();
            _node.setGraphic(new ImageView(new Image("file:img/" + _type + ".png")));
            _node.setStyle("-fx-text-fill:white;");
            _node.setFont(Font.font("Square721 BT", 69));
            _node.setContentDisplay(ContentDisplay.CENTER);
        }
        _node.setText("" + _number);
        return _node;
    }

    public Label getCopyNode() {
        Label node = new Label();
        node.setGraphic(new ImageView(new Image("file:img/" + _type + ".png")));
        node.setStyle("-fx-text-fill:white;");
        node.setText("" + _number);
        node.setFont(Font.font("Square721 BT", 69));
        node.setContentDisplay(ContentDisplay.CENTER);
        return node;
    }

    public void operation(Tile tile) {
        switch (tile.getType()) {
            case DIAMOND: {
                _number = _number + tile.getNumber();
                break;
            }
            case CLUB: {
                if(_number >= tile.getNumber()){
                    _number = _number - tile.getNumber();
                }
                else{
                    _number = tile.getNumber() - _number;
                }
                break;
            }
            case HEART: {
                _number = _number * tile.getNumber();
                break;
            }
            case SPADE: {
                _number = _number / tile.getNumber();
                break;
            }
        }
    }

    public enum TileType {

        DIAMOND, CLUB, HEART, SPADE
    }

    @Override
    public String toString() {
        return "" + _type + _number;
    }
}
