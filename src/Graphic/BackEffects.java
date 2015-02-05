/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphic;

import UI.UI;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Screen;

/**
 *
 * @author Luming Wu
 */
public class BackEffects extends StackPane {

    private UI _ui;

    public BackEffects(UI ui) {
        setStyle("-fx-background-color:#AD6B6B");
        _ui = ui;
        setAlignment(Pos.CENTER);
        setPickOnBounds(false);
        double x = Screen.getPrimary().getVisualBounds().getWidth();
        double y = Screen.getPrimary().getVisualBounds().getHeight();
        new AnimationTimer() {
            private long current = 0;
            private int specialrequirement = 100;

            @Override
            public void handle(long now) {
                if (current == 0) {
                    current = now;
                }
                if ((now - current) / 1000000000 >= ((int)(Math.random() * 4) + 1)) {
                    current = now;
                    getChildren().add(new flyingObject(x, y));
                    if (getChildren().size() == specialrequirement) {
                        specialrequirement = 200;
                        new AnimationTimer() {
                            private long current2 = 0;

                            @Override
                            public void handle(long now) {
                                if (current2 == 0) {
                                    current2 = now;
                                }
                                if ((now - current2) >= 1000000000) {
                                    current2 = now;
                                    ((flyingObject) getChildren().get((int) (Math.random() * getChildren().size()))).disappear();
                                    if (getChildren().size() < 50) {
                                        specialrequirement = 100;
                                        stop();
                                    }
                                }
                            }
                        }.start();
                    }
                }
            }
        }.start();

    }

    private class flyingObject extends Label {

        private flyingObject _me;

        private AnimationTimer _timer;

        private double _x;
        private double _y;

        private int xchange;
        private int ychange;

        public flyingObject(double width, double height) {
            setVisible(false);
            _me = this;
            setText("" + (int) (Math.random() * 10));
            setStyle("-fx-text-fill: rgb(" + (int) (Math.random() * 256) + "," + (int) (Math.random() * 256) + "," + (int) (Math.random() * 256) + ")");
            setFont(Font.font("Square721 BT", 12 + Math.random() * 25));

            switch ((int) (Math.random() * 2)) {
                case 0: {
                    //_x = -1 * width / 2 / (Math.random() * 10);
                    xchange = (int) (Math.random() * 10 + 1);
                    switch ((int) (Math.random() * 2)) {
                        case 0: {
                            //_y = -1 * height / 2 / (Math.random() * 10);
                            ychange = (int) (Math.random() * 10 + 1);
                            break;
                        }
                        case 1: {
                            //_y = height / 2 / (Math.random() * 10);
                            ychange = -(int) (Math.random() * 10 + 1);
                            break;
                        }
                    }
                    break;
                }
                case 1: {
                    //_x = width / 2 / (Math.random() * 10);
                    xchange = -(int) (Math.random() * 10 + 1);
                    switch ((int) (Math.random() * 2)) {
                        case 0: {
                            //_y = -1 * height / 2 / (Math.random() * 10);
                            ychange = (int) (Math.random() * 10 + 1);
                            break;
                        }
                        case 1: {
                            //_y = height / 2 / (Math.random() * 10);
                            ychange = -(int) (Math.random() * 10 + 1);
                            break;
                        }
                    }
                    break;
                }
            }
            //_me.setTranslateX(_x);
            //_me.setTranslateY(_y);
            _timer = new AnimationTimer() {
                private long objectcurrent = 0;

                @Override
                public void handle(long now) {
                    if (objectcurrent == 0) {
                        objectcurrent = now;
                    }
                    if ((now - objectcurrent) >= 1000000000) {
                        objectcurrent = now;
                        _me.setTranslateX(_me.getTranslateX() + xchange);
                        _me.setTranslateY(_me.getTranslateY() + ychange);
                        if (_me.getTranslateX() >= width / 2 || _me.getTranslateX() <= -width / 2) {
                            xchange = xchange * -1;
                        }
                        if (_me.getTranslateY() >= height / 2 || _me.getTranslateY() <= -height / 2) {
                            ychange = ychange * -1;
                        }
                    }
                }
            };
            _timer.start();
            setVisible(true);
        }

        public synchronized void disappear() {
            new AnimationTimer() {
                private long disappearcurrent = 0;

                @Override
                public void handle(long now) {
                    if (disappearcurrent == 0) {
                        disappearcurrent = now;
                    }
                    if ((now - disappearcurrent) >= 1) {
                        disappearcurrent = now;
                        _me.setOpacity(_me.getOpacity() - 0.2);
                        if (_me.getOpacity() == 0) {
                            _ui.getBackEffects().getChildren().remove(_me);
                            stop();
                            _timer.stop();
                        }
                    }
                }
            }.start();
        }
    }
}
