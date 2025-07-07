/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;


import View.DangNhapView;
import View.DangKyView;
import controller.LoginController;
import javax.swing.SwingUtilities;
import view.LoginView;

/**
 *
 * @author DUNG LE
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            new LoginController(loginView);
            loginView.setVisible(true);
        });
    }

}
