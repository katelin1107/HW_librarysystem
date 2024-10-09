import java.text.SimpleDateFormat;
import java.util.Date;

public class Book {
    private String title;
    private String author;
    private String isbn;
    private boolean available; // true = 在庫, false = 出借中
    private String borrowedBy; // 借閱者的帳號
    private String borrowTime; // 借書時間

    public Book(String title, String author, String isbn, boolean available) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.available = available;
        this.borrowedBy = null;
        this.borrowTime = null;
    }

    // 獲取書名
    public String getTitle() {
        return title;
    }

    // 獲取作者
    public String getAuthor() {
        return author;
    }

    // 獲取ISBN碼
    public String getIsbn() {
        return isbn;
    }

    // 獲取書籍狀態
    public boolean isAvailable() {
        return available;
    }

    // 設置書籍狀態
    public void setAvailable(boolean available) {
        this.available = available;
    }

    // 設置借閱者和借書時間
    public void setBorrowInfo(String username, String time) {
        this.borrowedBy = username;
        this.borrowTime = time;
    }

    // 獲取借閱者
    public String getBorrowedBy() {
        return borrowedBy;
    }

    // 獲取借書時間
    public String getBorrowTime() {
        return borrowTime;
    }

    // 還書
    public void returnBook() {
        this.available = true;
        this.borrowedBy = null;
        this.borrowTime = null;
    }
}