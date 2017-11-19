package com.muic.game.candy;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Game game = new Game();
        game.setPreferredSize(new Dimension(700,600));
        game.setMaximumSize(new Dimension(700,600));
        game.setMinimumSize(new Dimension(700,600));

        JFrame frame = new JFrame();
        frame.setTitle("Candy Crush");
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.start();
    }

}
