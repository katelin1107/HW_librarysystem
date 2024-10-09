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
    private static ArrayList<Book> books = new ArrayList<>(); // 存儲書籍列表

    public static void main(String[] args) {
        loadUserData();
        loadBookData(); // 載入書籍資料

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

         loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passwordText.getPassword());

                if (validateLogin(username, password)) {
                    updateLastLoginTime(username);
                    
                    new BorrowBookWindow(books, username); // 登入成功後開啟借書視窗
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
                
            }
        });

        frame.setVisible(true);
    }

    public static void loadUserData() {
        File userFile = new File("HW_librarysystem/users.txt");

         try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] user = line.split(",");
            users.add(user);
        }
        } catch (IOException e) {
        System.out.println("No user data found. Starting with an empty user list.");
        }
    }
    
    public static void loadBookData() {
        // 在這裡可以從文件或其他來源加載書籍資料
        books.add(new Book("摺紙新玩法開店遊戲超有趣!", "石川真理子/楊鴻儒", "9789866180514", true));
        books.add(new Book("神奇收納小撇步", "永井一夫", "9575265998", true));
        books.add(new Book("美味早午餐!", "張炳賢", "9789867502254", true));
        books.add(new Book("親子運動，提升學習力!", "柳澤秋孝/羅婷婷", "9789867167781", true));
        books.add(new Book("氣球造型變變變!", "李起泰", "9867171055", true));
        books.add(new Book("家事生活智慧通", "藤岡真澄", "9575265955", true));
        books.add(new Book("木偶奇遇記", "科洛迪/嶺月", "9576320763", true));
        books.add(new Book("家庭托嬰高手", "施以恩", "9576592542", true));
        books.add(new Book("會說話的骨笛", "李顯鳳", "9577752071", true));
        books.add(new Book("夢幻大飛行", "威斯納", "9573211963", true));


        
        // 添加更多書籍...
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
