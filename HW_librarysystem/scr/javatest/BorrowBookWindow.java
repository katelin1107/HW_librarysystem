import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BorrowBookWindow {

    private ArrayList<Book> books; // 書籍列表
    private String currentUser; // 當前用戶的帳號

    public BorrowBookWindow(ArrayList<Book> books, String username) {
        this.books = books; // 傳入書籍列表
        this.currentUser = username; // 獲取當前用戶的帳號
        createBorrowBookWindow();
    }

    // 創建借書視窗
    private void createBorrowBookWindow() {
        JFrame borrowFrame = new JFrame("Borrow Book");
        borrowFrame.setSize(800, 500);
        borrowFrame.setLayout(new BorderLayout());

        // 創建書籍列表面板
        JPanel bookPanel = new JPanel();
        bookPanel.setLayout(new GridLayout(0, 1)); // 每行顯示一個書籍

        // 顯示所有書籍
        for (Book book : books) {
            JPanel bookInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel bookLabel = new JLabel("書名: " + book.getTitle() + ", 作者: " + book.getAuthor() +
                    ", ISBN: " + book.getIsbn() + ", 借閱裝態: " + (book.isAvailable() ? "可借閱" : "出借中" ));
            bookInfoPanel.add(bookLabel);

            // 如果書籍可借出，顯示借出按鈕
            if (book.isAvailable()) {
                JButton borrowButton = new JButton("借閱");
                borrowButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        book.setAvailable(false); // 更新书籍状态为已借出
                        String borrowTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        book.setBorrowInfo(currentUser, borrowTime); // 记录借阅者的账号及借书时间
                        JOptionPane.showMessageDialog(borrowFrame, "您成功借閱了 「" + book.getTitle() + " 」在 " + borrowTime);
                        borrowFrame.dispose(); // 可选：关闭借书窗口或刷新列表
                        new BorrowBookWindow(books, currentUser); // 重新显示更新后的书籍列表
                    }
                });
                bookInfoPanel.add(borrowButton);
            } else if (book.getBorrowedBy().equals(currentUser)) {
                // 如果是當前用戶借出的書籍，顯示還書按鈕
                JButton returnButton = new JButton("還書");
                returnButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        book.returnBook(); // 還書
                        JOptionPane.showMessageDialog(borrowFrame, "您成功還書，書名為「" + book.getTitle() + "」");
                        borrowFrame.dispose(); // 可選：關閉借書視窗或刷新列表
                        new BorrowBookWindow(books, currentUser); // 重新顯示更新後的書籍列表
                    }
                });
                bookInfoPanel.add(returnButton);
            }

            bookPanel.add(bookInfoPanel);
        }

        JScrollPane scrollPane = new JScrollPane(bookPanel);
        borrowFrame.add(scrollPane, BorderLayout.CENTER);

        // 創建返回按鈕
        JButton backButton = new JButton("Back to Login");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                borrowFrame.dispose(); // 關閉借書視窗
            }
        });
        borrowFrame.add(backButton, BorderLayout.SOUTH);

        borrowFrame.setVisible(true);
    }
}
