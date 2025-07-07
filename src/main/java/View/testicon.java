/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package  view;

/**
 *
 * @author DUNG LE
 */
import javax.swing.*;

public class testicon {
    public static void main(String[] args) {
        // Tạo cửa sổ JFrame
        JFrame frame = new JFrame("Test GIF");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200, 200);
        frame.setLocationRelativeTo(null);

        // Load ảnh GIF từ file (cùng thư mục với file .java hoặc từ đường dẫn tuyệt đối)
       ImageIcon icon = new ImageIcon(testicon.class.getResource("/images/image_dialog/error.gif")); // hoặc "./error.gif" nếu nằm cạnh file .class

        // Gắn icon vào JLabel
        JLabel label = new JLabel(icon);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);

        frame.add(label);
        frame.setVisible(true);
    }
}