import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RegisterWindow {

    private ArrayList<String[]> users; // 用戶數據列表

    public RegisterWindow(ArrayList<String[]> users) {
        this.users = users; // 傳入用戶數據列表
        createRegisterWindow();
    }

    // 創建註冊視窗
    private void createRegisterWindow() {
        JFrame registerFrame = new JFrame("Register");
        registerFrame.setSize(450, 350);
        registerFrame.setLayout(new GridLayout(5, 2));

        JLabel usernameLabel = new JLabel("Username (max 20 characters):");
        JTextField usernameText = new JTextField();
        usernameText.setDocument(new javax.swing.text.PlainDocument() {
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (str == null) return;
                if (getLength() + str.length() > 20) {
                    str = str.substring(0, 20 - getLength());
                }
                super.insertString(offs, str, a);
            }
        });
        registerFrame.add(usernameLabel);
        registerFrame.add(usernameText);

        JLabel phoneLabel = new JLabel("Phone Number (10 digits):");
        JTextField phoneText = new JTextField();
        registerFrame.add(phoneLabel);
        registerFrame.add(phoneText);

        JLabel passwordLabel = new JLabel("Password (exactly 6 characters):");
        JPasswordField passwordText = new JPasswordField();
        registerFrame.add(passwordLabel);
        registerFrame.add(passwordText);

        JButton submitButton = new JButton("Submit");
        registerFrame.add(submitButton);

        // 註冊提交按鈕事件
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameText.getText();
                String phoneNumber = phoneText.getText();
                String password = new String(passwordText.getPassword());

                // 檢查手機號是否為10位數
                if (phoneNumber.length() != 10 || !phoneNumber.matches("\\d+")) {
                    JOptionPane.showMessageDialog(registerFrame, "Phone number must be 10 digits.");
                    return;
                }

                // 檢查密碼長度是否為6個字元
                if (password.length() != 6) {
                    JOptionPane.showMessageDialog(registerFrame, "Password must be exactly 6 characters.");
                    return;
                }

                // 檢查帳號是否唯一
                if (isPhoneNumberExists(phoneNumber)) {
                    JOptionPane.showMessageDialog(registerFrame, "Phone number already exists.");
                    return;
                }

                // 哈希處理密碼
                String hashedPassword = LoginWindow.hashPassword(password);

                // 獲取當前時間作為註冊日期
                String registrationDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                String lastLoginTime = "Never"; // 初始值

                // 保存帳號、哈希後的密碼、註冊者名稱、註冊日期、最後登入時間
                saveUser(username, phoneNumber, hashedPassword, registrationDate, lastLoginTime);
                JOptionPane.showMessageDialog(registerFrame, "Registration Successful!");
                registerFrame.dispose();  // 註冊成功後關閉註冊視窗
            }
        });

        registerFrame.setVisible(true);
    }

    // 檢查手機號是否已經存在
    private boolean isPhoneNumberExists(String phoneNumber) {
        for (String[] user : users) {
            if (user[0].equals(phoneNumber)) {
                return true;
            }
        }
        return false;
    }

    // 保存用戶信息到數據列表和文件
    private void saveUser(String username, String phoneNumber, String hashedPassword, String registrationDate, String lastLoginTime) {
        String[] user = {phoneNumber, username, hashedPassword, registrationDate, lastLoginTime};
        users.add(user);
        saveUserDataToFile();
    }

    // 將用戶數據保存到文件
    private void saveUserDataToFile() {
        try (FileWriter fw = new FileWriter("HW_librarysystem/javatest/users.txt", false);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            for (String[] user : users) {
                out.println(user[0] + "," + user[1] + "," + user[2] + "," + user[3] + "," + user[4]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
