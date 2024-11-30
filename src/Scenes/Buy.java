package Scenes;

import GameManager.CoinManager;
import GameManager.FontManager;
import GameManager.ProgressPaneManager;
import GameManager.StartManager;
import GameManager.ItemManager;
import Goods.Goods;
import Items.ItemPanel;
import GameManager.CoinManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Buy extends JFrame {
    private String[] itemImgPath = {
            "assets/img/item/slogan.png", "assets/img/item/tshirts.png",
            "assets/img/item/stick.png", "assets/img/item/doll.png",
            "assets/img/item/bag.png", "assets/img/item/album.png"
    };

    private boolean nextButtonClicked = false; // 버튼 클릭 상태
    private Runnable onDisposeAction; // dispose 시 실행할 동작
    CoinManager coinManager = new CoinManager();
    private ItemManager itemManager; // ItemManager 인스턴스 변수 추가
    private Goods goodsPanel;

    private boolean[] itemPurchased = new boolean[6]; // 각 아이템의 구매 여부를 추적

    private int[] itemPrices = {50, 200, 300, 400, 500, 600}; // 각 아이템의 가격

    public Buy() {
        showPopup();
    }

    public void setOnDisposeAction(Runnable onDisposeAction) {
        this.onDisposeAction = onDisposeAction;
    }

    public boolean isNextButtonClicked() {
        return nextButtonClicked; // 버튼 클릭 상태 반환
    }

    private void showPopup() {
        // JDialog 설정
        JDialog buyPopup = new JDialog(this, "아이템을 구매해서 돈을 더 빨리 모으자!", true);
        buyPopup.setSize(950, 650);
        buyPopup.setLocationRelativeTo(null); // 화면 중앙에 배치
        buyPopup.setResizable(false);
        buyPopup.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        buyPopup.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        // 배경 설정
        buyPopup.getContentPane().setBackground(Color.decode("#D5F2FF"));
        JLabel backgroundLabel = new JLabel(new ImageIcon("assets/img/panel/buy.png"));
        backgroundLabel.setLayout(null);

        // 아이템 패널
        JPanel itemPanel = new JPanel(new GridLayout(2, 3, 0, 0));
        itemPanel.setOpaque(false); // 배경 투명화
        itemPanel.setBounds(100, 150, 520, 340);

        // 추가
        itemManager = ItemManager.getInstance(); // 동일 인스턴스 사용
        goodsPanel = new Goods(itemManager); // Goods 패널 생성

        int[] selectedItemIndex = {-1}; // 선택된 아이템 인덱스를 저장 (-1: 선택 없음)

        // 아이템 이미지
        for (int i = 0; i < 6; i++) {
            // 아이템 박스
            JButton itemBox = new JButton(new ImageIcon("assets/img/panel/box.png"));
            itemBox.setLayout(new BorderLayout());
            itemBox.setBorderPainted(false);
            itemBox.setContentAreaFilled(false);

            Font font = FontManager.loadFont(20);
            JLabel itemLabel = new JLabel(itemPrices[i] + "만원", new ImageIcon(itemImgPath[i]), SwingConstants.CENTER);
            itemLabel.setHorizontalTextPosition(SwingConstants.CENTER);
            itemLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
            itemLabel.setFont(font);

            // 버튼에 아이템 인덱스 정보 추가
            itemBox.putClientProperty("itemIndex", i);

            // 아이템이 이미 구매되었으면 비활성화
            if (itemPurchased[i]) {
                itemBox.setEnabled(false); // 비활성화하여 클릭할 수 없도록
            }

            // 클릭 리스너 추가
            itemBox.addActionListener(e -> {
                int selectedIndex = (int) itemBox.getClientProperty("itemIndex");
                selectedItemIndex[0] = selectedIndex; // 선택된 아이템 인덱스 저장
                System.out.println("선택한 아이템 인덱스: " + selectedIndex);
            });

            itemBox.add(itemLabel, BorderLayout.CENTER);
            itemPanel.add(itemBox);
        }

        JPanel rightPanel = new JPanel(new GridLayout(4, 1, 0, 0));
        rightPanel.setOpaque(false);
        rightPanel.setBounds(590, 130, 300, 400);

        // 코인 타이틀 패널
        JPanel coinPanel = new JPanel(new BorderLayout());
        coinPanel.setOpaque(false);
        JLabel coinLabel = new JLabel(new ImageIcon("assets/img/coin.png"));
        coinPanel.add(coinLabel);

        // 소지 금액 패널
        JPanel haveCoinPanel = new JPanel(new FlowLayout());
        haveCoinPanel.setOpaque(false);

        JLabel coinImgLabel = new JLabel(new ImageIcon("assets/img/coinImage.png"));

        Font font = FontManager.loadFont(40);
        JLabel coinTxt = new JLabel(coinManager.getCoinAmount() + "만원");
        coinTxt.setFont(font);

        haveCoinPanel.add(coinImgLabel);
        haveCoinPanel.add(coinTxt);

        // 버튼 이미지
        JButton buyButton = new JButton(new ImageIcon("assets/img/button/buyButton.png"));
        buyButton.setBorderPainted(false);
        buyButton.setContentAreaFilled(false);

        JButton nextButton = new JButton(new ImageIcon("assets/img/button/nextButton.png"));
        nextButton.setBorderPainted(false);
        nextButton.setContentAreaFilled(false);

//        // 기존 Buy 버튼 클릭 리스너
//        buyButton.addActionListener(e -> {
//            if (selectedItemIndex[0] == -1) {
//                JOptionPane.showMessageDialog(this, "구매할 아이템을 선택하세요.", "알림", JOptionPane.WARNING_MESSAGE);
//            } else {
//                int selectedIndex = selectedItemIndex[0];
//                if (selectedIndex == 3 || selectedIndex == 4 || selectedIndex == 5) {
//                    itemManager.setVisibleItem(selectedIndex, true);
//                    System.out.println("아이템 인덱스 " + selectedIndex + "이(가) 화면에 보이도록 설정되었습니다.");
//                } else {
//                    // ItemPanel.itemArray 업데이트
//                    ItemPanel.itemArray[selectedIndex + 1] = true;
//
//                    // ItemPanel UI 업데이트 호출
//                    SwingUtilities.invokeLater(() -> {
//                        if (ItemPanel.instance != null) {
//                            ItemPanel.instance.refreshItems();
//                        } else {
//                            System.err.println("ItemPanel 인스턴스가 존재하지 않습니다.");
//                        }
//                    });
//                    System.out.println("아이템 인덱스 " + selectedIndex + "은(는) itemArray에 추가되었습니다.");
//                }
//            }
//        });

        // 수정된 Buy 버튼 클릭 리스너
        buyButton.addActionListener(e -> {
            if (selectedItemIndex[0] == -1) {
                JOptionPane.showMessageDialog(this, "구매할 아이템을 선택하세요.", "알림", JOptionPane.WARNING_MESSAGE);
            } else {
                int selectedIndex = selectedItemIndex[0];
                if (itemPurchased[selectedIndex]) {
                    JOptionPane.showMessageDialog(this, "이미 구매한 아이템입니다.", "알림", JOptionPane.WARNING_MESSAGE);
                } else {
                    if (coinManager.getCoinAmount() >= itemPrices[selectedIndex]) {
                        // 아이템 구매 처리
                        itemPurchased[selectedIndex] = true; // 해당 아이템을 구매했다고 표시

                        if (selectedIndex == 3 || selectedIndex == 4 || selectedIndex == 5) {
                            itemManager.setVisibleItem(selectedIndex, true);
                            System.out.println("아이템 인덱스 " + selectedIndex + "이(가) 화면에 보이도록 설정되었습니다.");
                        } else {
                            // ItemPanel.itemArray 업데이트
                            ItemPanel.itemArray[selectedIndex + 1] = true;

                            // ItemPanel UI 업데이트 호출
                            SwingUtilities.invokeLater(() -> {
                                if (ItemPanel.instance != null) {
                                    ItemPanel.instance.refreshItems();
                                } else {
                                    System.err.println("ItemPanel 인스턴스가 존재하지 않습니다.");
                                }
                            });
                            System.out.println("아이템 인덱스 " + selectedIndex + "은(는) itemArray에 추가되었습니다.");
                        }
                        // 아이템 박스를 비활성화하여 더 이상 클릭되지 않도록
                        Component[] components = itemPanel.getComponents();
                        for (Component component : components) {
                            if (component instanceof JButton) {
                                JButton button = (JButton) component;
                                int index = (int) button.getClientProperty("itemIndex");
                                if (index == selectedIndex) {
                                    button.setEnabled(false); // 이미 구매된 아이템은 클릭할 수 없게 함
                                }
                            }
                        }
                        coinManager.updateCoinAmount(-itemPrices[selectedIndex]);
                        coinTxt.setText(coinManager.getCoinAmount() + "만원");  // 새로운 코인 금액 표시
                    }
                    else{
                        JOptionPane.showMessageDialog(this, "코인이 부족합니다.", "알림", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });


        // 다음으로 버튼을 눌렀을 때 현재 buy 창이 꺼지기
        nextButton.addActionListener(e -> {
            nextButtonClicked = true;
            dispose();
        });

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                if (onDisposeAction != null) {
                    onDisposeAction.run();
                }
            }
        });

        // 오른쪽 담당 패널에 코인과 버튼 추가
        rightPanel.add(coinPanel);
        rightPanel.add(haveCoinPanel);
        rightPanel.add(buyButton);
        rightPanel.add(nextButton);

        // 배경에 아이템과 버튼 추가
        backgroundLabel.add(itemPanel);
        backgroundLabel.add(rightPanel);

        buyPopup.getContentPane().add(backgroundLabel, BorderLayout.CENTER);

        buyPopup.setVisible(true);
    }

    public static void main(String[] args) {
        new Buy();
    }
}