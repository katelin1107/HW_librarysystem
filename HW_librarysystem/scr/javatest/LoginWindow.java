import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LoginWindow {

    private static ArrayList<String[]> users = new ArrayList<>();

    public static void main(String[] args) {
        // 從文件加載用戶數據
        loadUserData();

        JFrame frame = new JFrame("Login Window");
        frame.setSize(400, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 2));

        JLabel userLabel = new JLabel("Phone Number:");
        JTextField userText = new JTextField();
        frame.add(userLabel);
        frame.add(userText);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordText = new JPasswordField();
        frame.add(passwordLabel);
        frame.add(passwordText);

        JButton loginButton = new JButton("Login");
        frame.add(loginButton);

        JButton registerButton = new JButton("Register");
        frame.add(registerButton);

        // 登入按鈕的事件處理
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passwordText.getPassword());

                if (validateLogin(username, password)) {
                    updateLastLoginTime(username);
                    JOptionPane.showMessageDialog(frame, "Login Successful!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid username or password");
                }
            }
        });

        // 註冊按鈕的事件處理
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 開啟註冊視窗
                new RegisterWindow(users);
                
                // 註冊後立即檢查用戶列表
                System.out.println("Current users:");
                for (String[] user : users) {
                    System.out.println("Phone: " + user[0] + ", Username: " + user[1]);
                }
            }
        });

        frame.setVisible(true);
    }

    public static void loadUserData() {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] user = line.split(",");
                users.add(user);
            }
        } catch (IOException e) {
            System.out.println("No user data found. Starting with an empty user list.");
        }
    }

    public static boolean validateLogin(String username, String password) {
        String hashedPassword = hashPassword(password);
        for (String[] user : users) {
            if (user[0].equals(username) && user[2].equals(hashedPassword)) {
                return true;
            }
        }
        return false;
    }

    public static void updateLastLoginTime(String username) {
        String lastLoginTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        for (String[] user : users) {
            if (user[0].equals(username)) {
                user[4] = lastLoginTime;
                break;
            }
        }
        saveUserDataToFile();
    }

    public static void saveUserDataToFile() {
        try (FileWriter fw = new FileWriter("users.txt", false);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            for (String[] user : users) {
                out.println(user[0] + "," + user[1] + "," + user[2] + "," + user[3] + "," + user[4]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String hashPassword(String password) {
        String salt = "mySaltValue"; 
        password = password + salt; 
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
