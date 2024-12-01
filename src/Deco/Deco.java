package Deco;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Random;
import GameManager.CoinManager;

import GameManager.FontManager;

public class Deco extends JDialog {
    public Boolean gameResult = null;
    private int clickCount = 0;
    public JLayeredPane layeredPane;
    private final int[] selectedItems = new int[3];
    final int[] currentList = new int[3];
    private final String[] films = { "사랑하는 마음이 담긴", "내 스타를 위한", "하늘에 수놓은 비단같은" };
    private final String[] colors = { "벚꽃", "푸른", "레몬" };
    private final String[] themes = { "공주", "바다", "진주" };
    private TopLoader topLoader;


    public Deco(JFrame parentFrame) {
        super(parentFrame, "탑로더를 꾸며보아요!", true);
        Arrays.fill(currentList, 3);
        setUndecorated(true);

        setTitle("탑로더를 꾸며보아요!");
        setSize(800, 600);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        ImageIcon originalIcon = new ImageIcon("assets/img/decoItem/Frame2.png");
        Image backgroundImage = originalIcon.getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.add(backgroundPanel, Integer.valueOf(0));

        backgroundPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {

                if (clickCount < 3) {
                    evt.consume();
                }
            }
        });

        GameInfo gameInfo = new GameInfo(this);
        gameInfo.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.add(gameInfo, Integer.valueOf(1));

        setVisible(true);
    }


    public void incrementClickCount() {
        clickCount++;
        if (clickCount == 3) {

            for (Component comp : layeredPane.getComponents()) {
                if (comp instanceof GameInfo) {
                    layeredPane.remove(comp);
                    break;
                }
            }

            topLoader = new TopLoader(this); // TopLoader 초기화
            topLoader.setBounds(0, 0, 600, 600);
            layeredPane.add(topLoader, Integer.valueOf(1));

            ColorLayer colorLayer = new ColorLayer(this);
            colorLayer.setBounds(100, 50, 250, 250);

            PartsLayer partsLayer = new PartsLayer(this);
            partsLayer.setBounds(500, 50, 130, 500);


            FilmLayer FilmLayer = new FilmLayer(this);
            FilmLayer.setBounds(50, 50, 200, 400);

            decoItem();

            layeredPane.revalidate();
            layeredPane.repaint();
        }
    }

    String randomDecoItem() {
        Random random = new Random();

        selectedItems[0] = random.nextInt(films.length);
        selectedItems[1] = random.nextInt(colors.length);
        selectedItems[2] = random.nextInt(themes.length);

        return films[selectedItems[0]] + " " + colors[selectedItems[1]] + " " + themes[selectedItems[2]];
    }
    public void onCurrentListChanged(int index, int value) {
        if (currentList.length > index) {
            currentList[index] = value;
            if (topLoader != null) {
                topLoader.updateLayers();
            }
        }
    }
    public int getClickCount() {
        return clickCount;
    }
    private void decoItem() {
        JPanel decoJPanel = new JPanel(null);
        decoJPanel.setSize(600, 50);
        decoJPanel.setLocation(0, 0);
        decoJPanel.setOpaque(false);

        JLabel textLabel = new JLabel(
                "\"" + films[selectedItems[0]] + "\" \"" +
                        colors[selectedItems[1]] + "\" \"" +
                        themes[selectedItems[2]] + "\"",
                SwingConstants.CENTER);
        textLabel.setOpaque(false);
        textLabel.setSize(600, 50);
        textLabel.setFont(FontManager.loadFont(24f));
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);

        decoJPanel.add(textLabel);
        layeredPane.add(decoJPanel, Integer.valueOf(1));
    }

    public Boolean gameEnd() {
        if (currentList.length != selectedItems.length) {
            return false;
        }

        for (int i = 0; i < currentList.length; i++) {
            if (currentList[i] != selectedItems[i]) {
                return false;
            }
        }

        return true;
    }
    public void ending() {
        ToploderEnd endPanel = new ToploderEnd(gameResult, selectedItems, this);

        endPanel.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.add(endPanel, Integer.valueOf(5));
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (gameResult != null) {
            System.out.println(gameResult ? "맞았습니다" : "틀렸습니다");
        }
        if(gameResult){
            CoinManager.updateCoinAmount(10);
        }
    }

    public static void main(String[] args) {

        JFrame parent = new JFrame();
        parent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        parent.setSize(800, 600);
        parent.setVisible(false);

        new Deco(parent);
    }


}
