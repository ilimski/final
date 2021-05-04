import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Main extends JFrame {
    private RobotModel model = new RobotModel();
    private BufferedImage ground = ImageIO.read(new File("C:\\Users\\admin\\Desktop\\программирование\\images\\ground.png"));
    private BufferedImage robotD = ImageIO.read(new File("C:\\Users\\admin\\Desktop\\программирование\\images\\Robot.png"));
    private BufferedImage robotU = ImageIO.read(new File("C:\\Users\\admin\\Desktop\\программирование\\images\\RobotU.png"));
    private BufferedImage robotL = ImageIO.read(new File("C:\\Users\\admin\\Desktop\\программирование\\images\\RobotL.png"));
    private BufferedImage robotR = ImageIO.read(new File("C:\\Users\\admin\\Desktop\\программирование\\images\\RobotR.png"));
    private BufferedImage redBox = ImageIO.read(new File("C:\\Users\\admin\\Desktop\\программирование\\images\\BoxRed.png"));
    private BufferedImage blueBox = ImageIO.read(new File("C:\\Users\\admin\\Desktop\\программирование\\images\\BoxBlue.png"));
    private BufferedImage wall = ImageIO.read(new File("C:\\Users\\admin\\Desktop\\программирование\\images\\Wall.png"));
    private BufferedImage goal = ImageIO.read(new File("C:\\Users\\admin\\Desktop\\программирование\\images\\Goal.png"));
    JPanel controlPanel = new JPanel();
    DrawPanel panel = new DrawPanel();
    JButton undoMove = new JButton("Undo");
    JButton redoMove = new JButton("Redo");
    JButton reset = new JButton("Reset");
    JButton nextMap = new JButton("->>");
    JButton backMap = new JButton("<<-");
    JPanel keyAdvice = new JPanel();
    JPanel currentMap = new JPanel();
    JPanel moves = new JPanel();
    JPanel best = new JPanel();
    JPanel restart = new JPanel();
    JLabel restartLabel = new JLabel("Restart: Esc");
    JPanel undoKey = new JPanel();
    JLabel previousKeyLabel = new JLabel("Previous: Alt Left");
    JPanel previousKey = new JPanel();
    JLabel nextKeyLabel = new JLabel("Next: Alt Right");
    JPanel nextKey = new JPanel();
    JLabel undoKeyLabel = new JLabel("Undo: Ctrl Left");
    JPanel redoKey = new JPanel();
    JLabel redoKeyLabel = new JLabel("Redo: Ctrl Right");
    JLabel currentMapLabel = new JLabel((MapsOfLevels.index + 1) + " from " + MapsOfLevels.list.get(MapsOfLevels.levelIndex).size());
    JLabel movesLabel = new JLabel("Moves: " + model.moves);
    JLabel bestLabel = new JLabel("Best: " + (MapsOfLevels.listBests.get(MapsOfLevels.levelIndex).get(MapsOfLevels.index) == 0 ? " None" : MapsOfLevels.listBests.get(MapsOfLevels.levelIndex).get(MapsOfLevels.index)));
    JComboBox box = new JComboBox(MapsOfLevels.getItems());

    private Main() throws IOException {
        restart.setBackground(Color.CYAN);
        restart.add(restartLabel);
        restart.setFocusable(false);
        undoKey.setBackground(Color.CYAN);
        undoKey.add(undoKeyLabel);
        redoKey.setBackground(Color.CYAN);
        redoKey.add(redoKeyLabel);
        previousKey.setBackground(Color.CYAN);
        previousKey.add(previousKeyLabel);
        nextKey.setBackground(Color.CYAN);
        nextKey.add(nextKeyLabel);
        panel.setBackground(Color.GRAY);
        panel.setFocusable(true);
        panel.addKeyListener(new PanelKeyListener());
        currentMap.setBackground(Color.CYAN);
        currentMap.setPreferredSize(new Dimension(75, 30));
        currentMap.add(currentMapLabel, BorderLayout.CENTER);
        moves.setBackground(Color.CYAN);
        moves.setPreferredSize(new Dimension(75, 30));
        moves.add(movesLabel, BorderLayout.CENTER);
        best.setBackground(Color.CYAN);
        best.setPreferredSize(new Dimension(75, 30));
        best.add(bestLabel, BorderLayout.CENTER);
        box.setFocusable(false);
        box.setPreferredSize(new Dimension(20, 20));
        box.addActionListener(e -> {
            JComboBox item = (JComboBox) e.getSource();
            MapsOfLevels.levelIndex = item.getSelectedIndex();
            model = new RobotModel();
            displayCurrentMap();
            displayBest(MapsOfLevels.listBests.get(MapsOfLevels.levelIndex).get(MapsOfLevels.index));
            repaint();

        });
        reset.setFocusable(false);
        reset.addActionListener(new MyActionListener());
        reset.setPreferredSize(new Dimension(75, 30));
        undoMove.setPreferredSize(new Dimension(75, 30));
        undoMove.setFocusable(false);
        undoMove.addActionListener(e -> {
            undo();
        });
        redoMove.setFocusable(false);
        redoMove.setPreferredSize(new Dimension(75, 30));
        redoMove.addActionListener(e -> {
            redo();
        });
        backMap.setPreferredSize(new Dimension(75, 30));
        backMap.setFocusable(false);
        backMap.addActionListener(e -> {
            backMap();
        });
        nextMap.setPreferredSize(new Dimension(75, 30));
        nextMap.setFocusable(false);
        nextMap.addActionListener(e -> {
            nextMap();
        });
        controlPanel.setBackground(Color.GRAY);
        controlPanel.setPreferredSize(new Dimension(100, this.getHeight()));
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
        controlPanel.add(reset);
        controlPanel.add(nextMap);
        controlPanel.add(backMap);
        controlPanel.add(redoMove);
        controlPanel.add(undoMove);
        controlPanel.add(moves);
        controlPanel.add(best);
        controlPanel.add(currentMap);
        keyAdvice.setBackground(Color.GRAY);
        keyAdvice.setLayout(new FlowLayout(FlowLayout.CENTER));
        keyAdvice.add(restart);
        keyAdvice.add(undoKey);
        keyAdvice.add(redoKey);
        keyAdvice.add(nextKey);
        keyAdvice.add(previousKey);
        add(keyAdvice, BorderLayout.SOUTH);
        add(controlPanel, BorderLayout.EAST);
        add(box, BorderLayout.NORTH);
        add(panel);

    }

    private void nextMap() {
        if (MapsOfLevels.index < MapsOfLevels.list.get(MapsOfLevels.levelIndex).size() - 1) {
            MapsOfLevels.index++;
            model = new RobotModel();
            displayCurrentMap();
            displayBest(MapsOfLevels.listBests.get(MapsOfLevels.levelIndex).get(MapsOfLevels.index));
            displayMoves(model.moves);
            repaint();
        }
    }

    private void backMap() {
        if (MapsOfLevels.index > 0) {
            MapsOfLevels.index--;
            model = new RobotModel();
            displayCurrentMap();
            displayBest(MapsOfLevels.listBests.get(MapsOfLevels.levelIndex).get(MapsOfLevels.index));
            displayMoves(model.moves);
            repaint();
        }
    }

    private void redo() {
        model.redoMove();
        model.copyCurrentStageOfLevel();
        repaint();
    }

    private void undo() {
        model.undoMove();
        model.copyCurrentStageOfLevel();
        repaint();
    }

    private void displayCurrentMap() {
        currentMapLabel.setText((MapsOfLevels.index + 1) + " from " + MapsOfLevels.list.get(MapsOfLevels.levelIndex).size());
    }

    public static void main(String[] args) throws IOException {
        Main app = new Main();
        app.setTitle("Move Robot");
        app.setSize(700, 500);
        app.setLocationRelativeTo(null);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
    }

    public void displayMoves(int moves) {
        movesLabel.setText("Moves " + moves);
    }

    public void displayBest(int best) {
        bestLabel.setText(best == 0 ? "None" : best + "");
    }

    class DrawPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            int wRect = getWidth() / model.maxLength;
            int hRect = getHeight() / model.currentMap.length;
            g.drawImage(ground, 0, 0, getWidth(), getHeight(), null);
            for (int i = 0; i < model.currentMap.length; i++) {
                for (int j = 0; j < model.currentMap[i].length; j++) {
                    char c = model.currentMap[i][j];
                    switch (c) {
                        case ' ':
                            g.drawImage(ground, j * wRect, i * hRect, wRect, hRect, null);
                            break;
                        case '$':
                            g.drawImage(blueBox, j * wRect, i * hRect, wRect, hRect, null);
                            break;
                        case '+':
                            g.drawImage(redBox, j * wRect, i * hRect, wRect, hRect, null);
                        case '#':
                            g.drawImage(wall, j * wRect, i * hRect, wRect, hRect, null);
                            break;
                        case '.':
                            g.drawImage(goal, j * wRect + wRect / 4, i * hRect + hRect / 4, wRect / 2, hRect / 2, null);
                            break;
                    }
                }
            }
            BufferedImage currentRobotDir = null;
            switch (model.dir) {
                case 0:
                    currentRobotDir = robotL;
                    break;
                case 1:
                    currentRobotDir = robotU;
                    break;
                case 2:
                    currentRobotDir = robotR;
                    break;
                case 3:
                    currentRobotDir = robotD;
                    break;
            }
            g.drawImage(currentRobotDir, model.modelX * wRect, model.modelY * hRect, wRect, hRect, null);
        }
    }

    private class PanelKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT && e.isControlDown()) {
                undo();
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && e.isControlDown()) {
                redo();
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT && e.isAltDown()) {
                backMap();
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && e.isAltDown()) {
                nextMap();
            } else {
                model.copyCurrentStageOfLevel();
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        model.moveUp();
                        break;
                    case KeyEvent.VK_DOWN:
                        model.moveDown();
                        break;
                    case KeyEvent.VK_LEFT:
                        model.moveLeft();
                        break;
                    case KeyEvent.VK_RIGHT:
                        model.moveRight();
                        break;
                    case KeyEvent.VK_ESCAPE:
                        restart();
                        break;

                }
                displayMoves(model.moves);
                repaint();
                if (model.counter == 0) {
                    JOptionPane.showMessageDialog(null, String.format("You solved puzzle %d.\nMoves: %d", MapsOfLevels.index + 1, model.moves));
                    if (model.moves < MapsOfLevels.listBests.get(MapsOfLevels.levelIndex).get(MapsOfLevels.index) || MapsOfLevels.listBests.get(MapsOfLevels.levelIndex).get(MapsOfLevels.index) == 0) {
                        MapsOfLevels.listBests.get(MapsOfLevels.levelIndex).set(MapsOfLevels.index, model.moves);
                        try {
                            MapsOfLevels.writeBest(MapsOfLevels.folder);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    MapsOfLevels.index += 1;
                    model = new RobotModel();
                    repaint();
                }
            }
        }
    }

    private class MyActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            restart();
        }
    }

    private void restart() {
        model = new RobotModel();
        displayMoves(model.moves);
        repaint();
    }
}
