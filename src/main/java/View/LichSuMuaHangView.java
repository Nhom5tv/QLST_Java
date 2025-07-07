/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 *
 * @author Admin
 */
public class LichSuMuaHangView extends JPanel {

    public JTextField searchField;
    public JPanel tabPanel, orderListPanel;

    public LichSuMuaHangView() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        initUI();
    }

    private void initUI() {
        // Tabs
        tabPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        tabPanel.setBackground(Color.WHITE);

        // Search
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(new Color(245, 245, 245));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel icon = new JLabel("Tìm kiếm: ");
        icon.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(null);

        searchPanel.add(icon, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        // Gộp tabs + search vào 1 JPanel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(tabPanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // Order list
        orderListPanel = new JPanel();
        orderListPanel.setLayout(new BoxLayout(orderListPanel, BoxLayout.Y_AXIS));
        orderListPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(orderListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(scrollPane, BorderLayout.CENTER);
    }

}
