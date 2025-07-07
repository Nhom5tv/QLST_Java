package View;

import com.formdev.flatlaf.FlatLightLaf;
import  view.testicon;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ThongBaodialog {

    public static void showErrorDialog(Component parent, String title, String message) {
       // Set giao diện FlatLaf dark mode
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Giao diện lỗi
       ImageIcon icon = new ImageIcon(testicon.class.getResource("/images/image_dialog/error.gif"));
       JLabel iconLabel = new JLabel(icon);
        iconLabel.setPreferredSize(new Dimension(40, 116));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        titleLabel.setForeground(Color.RED);

       JLabel messageLabel = new JLabel("<html><center>" + message + "</center></html>", SwingConstants.CENTER);
        messageLabel.setForeground(Color.BLACK);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(UIManager.getColor("Panel.background"));
        panel.add(iconLabel, BorderLayout.NORTH);
        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(messageLabel, BorderLayout.SOUTH);

        // Tùy chỉnh nút
        UIManager.put("OptionPane.yesButtonText", "Yes");
        UIManager.put("OptionPane.noButtonText", "No");

        int result = JOptionPane.showOptionDialog(
    null,
    panel, // panel của bạn có chứa icon + nội dung
    "Error",
    JOptionPane.YES_NO_OPTION,
    JOptionPane.PLAIN_MESSAGE, // ❗ Không dùng ERROR_MESSAGE nữa
    null, // ❗ Icon = null để không hiển thị icon mặc định bên trái
    new String[]{"Yes", "No"},
    "Yes"
);

    }
    public static void showWarningDialog(Component parent, String title, String message) {
        // Set giao diện FlatLaf
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        // Giao diện cảnh báo
       ImageIcon icon = new ImageIcon("C:\\Users\\DUNG LE\\OneDrive\\Desktop\\Java_Big_HomeWork\\src\\main\\resources\\images\\image_dialog\\warning.gif");
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setPreferredSize(new Dimension(64, 64)); // Scale qua JLabel
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        titleLabel.setForeground(Color.ORANGE);
        JLabel messageLabel = new JLabel("<html><center>" + message + "</center></html>", SwingConstants.CENTER);
        messageLabel.setForeground(Color.BLACK);
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(UIManager.getColor("Panel.background"));
        panel.add(iconLabel, BorderLayout.NORTH);
        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(messageLabel, BorderLayout.SOUTH);
        JOptionPane.showMessageDialog(parent, panel, "Warning", JOptionPane.PLAIN_MESSAGE);
    }
    public static boolean showConfirmDialog(Component parent, String title, String message) {
        // Set giao diện FlatLaf
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Giao diện xác nhận
        JLabel iconLabel = new JLabel(UIManager.getIcon("OptionPane.questionIcon"));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        titleLabel.setForeground(Color.RED);

        JLabel messageLabel = new JLabel("<html><center>" + message + "</center></html>", SwingConstants.CENTER);
        messageLabel.setForeground(Color.BLACK);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(UIManager.getColor("Panel.background"));
        panel.add(iconLabel, BorderLayout.NORTH);
        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(messageLabel, BorderLayout.SOUTH);
        int result = JOptionPane.showOptionDialog(
            parent,
            panel,
            "Confirm",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            new String[]{"Yes", "No"},
            "Yes"
        );

        return result == JOptionPane.YES_OPTION;
    }
    public static void showInfoDialog(Component parent, String title, String message) {
    // Set giao diện FlatLaf
    try {
        UIManager.setLookAndFeel(new FlatLightLaf());
    } catch (UnsupportedLookAndFeelException e) {
        e.printStackTrace();
    }

    // Giao diện thông báo
    JLabel iconLabel = new JLabel(UIManager.getIcon("OptionPane.informationIcon"));
    iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

    JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
    titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
    titleLabel.setForeground(new Color(0, 102, 204)); // Màu xanh dương

    JLabel messageLabel = new JLabel("<html><center>" + message + "</center></html>", SwingConstants.CENTER);
    messageLabel.setForeground(Color.BLACK);

    JPanel panel = new JPanel(new BorderLayout(10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    panel.setBackground(UIManager.getColor("Panel.background"));
    panel.add(iconLabel, BorderLayout.NORTH);
    panel.add(titleLabel, BorderLayout.CENTER);
    panel.add(messageLabel, BorderLayout.SOUTH);

    JOptionPane.showMessageDialog(parent, panel, title, JOptionPane.PLAIN_MESSAGE); // Đặt icon = null để không hiển thị icon mặc định
}
    public static void showsuccessDialog(Component parent, String title, String message) {
    // Set giao diện FlatLaf
    try {
        UIManager.setLookAndFeel(new FlatLightLaf());
    } catch (UnsupportedLookAndFeelException e) {
        e.printStackTrace();
    }

    // Giao diện thông báo
       ImageIcon icon = new ImageIcon("C:\\Users\\DUNG LE\\OneDrive\\Desktop\\Java_Big_HomeWork\\src\\main\\resources\\images\\image_dialog\\success.gif");
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setPreferredSize(new Dimension(64, 64)); // Scale qua JLabel


    JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
    titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
    titleLabel.setForeground(new Color(0, 102, 204)); // Màu xanh dương

    JLabel messageLabel = new JLabel("<html><center>" + message + "</center></html>", SwingConstants.CENTER);
    messageLabel.setForeground(Color.BLACK);

    JPanel panel = new JPanel(new BorderLayout(10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    panel.setBackground(UIManager.getColor("Panel.background"));
    panel.add(iconLabel, BorderLayout.NORTH);
    panel.add(titleLabel, BorderLayout.CENTER);
    panel.add(messageLabel, BorderLayout.SOUTH);

    JOptionPane.showMessageDialog(parent, panel, title, JOptionPane.PLAIN_MESSAGE); // Đặt icon = null để không hiển thị icon mặc định
}
    public static ImageIcon resizeGif(String path, int width, int height) {
    // Tạo ImageIcon từ ảnh gốc (có animation)
    ImageIcon originalIcon = new ImageIcon(path);
    
    // Lấy Image từ icon và scale
    Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
    
    // Trả về ImageIcon mới (mất animation nếu dùng getScaledInstance — KHÔNG NÊN resize ảnh động bằng cách này nếu cần giữ animation)
    return new ImageIcon(scaledImage);
}


}


 

