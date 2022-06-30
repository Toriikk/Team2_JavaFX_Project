package model;

import java.sql.*;




public class DB {
    public Connection conn; // SQL과 연결을 해줄 변수
    public Statement stmt; // SQL문을 실행할 변수
    public ResultSet rs;  // SQL문을 실행한 결과를 저장할 변수
    
    public void connect() {
        for (int i = 0; i < 3; i++) {
            try {
                Class.forName("oracle.jdbc.OracleDriver").newInstance(); // 드라이버 로드
                conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","bg","bg"); // DriverManager.getConnection() 으로 연결 얻기
                break;
            } catch (SQLException ex) {
                // handle any errors
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
   //책 재고 추가
    public void addQuantity(String number, String title) {
        try {
            connect();  // sql과 연결 시도
            stmt = conn.createStatement(); // sql문을 받아주는 stmt 생성
           
            stmt.executeUpdate("UPDATE books SET quantity = quantity +" + number +"WHERE title =" + "'" + title + "'" );
             // 책 제목을 입력받고 그것과 일치하는 책의 재고를 추가하는 SQL문       
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //책 재고 삭제
    public void deleteQuantity(String number , String title) {
        try {
            connect();  // sql과 연결 시도
            stmt = conn.createStatement(); // sql문을 받아주는 stmt 생성
           
            stmt.executeUpdate("UPDATE books SET quantity = quantity -" + number +"WHERE title =" + "'" + title + "'" );
             // 책 제목을 입력받고 그것과 일치하는 책의 재고를 삭제하는 SQL문 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void deleteBook(String title) {
        try {
            connect();  // sql과 연결 시도
            stmt = conn.createStatement(); // sql문을 받아주는 stmt 생성
           
            stmt.executeUpdate("DELETE FROM books WHERE title = " + "'" +title + "'" );
                    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
  
    public void addBook(String isbn, String title, String author, String price, String quantity) {
        try {
            connect();  // sql과 연결 시도
            stmt = conn.createStatement(); // sql문을 받아주는 stmt 생성
           
            stmt.executeUpdate("INSERT INTO books (isbn, title, author,regdate, price,quantity) VALUES (" +
                    "'" + isbn + "'," +
                    "'" + title + "'," +
                    "'" + author + "'," +
                    "TO_CHAR(SYSDATE,'YYYY-MM-DD'),"+  //현재 날짜를 DATE형식으로 
                    "'" +price + "'," 
                    + quantity + ")");  // sql문을 실행해줌. isbn, title, author, year,regdate,price 컬럼에 값(VALUES)을 넣는다
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //title은 업데이트할 '값', title1은 업데이트할 '대상'
    public void editBook(String isbn, String title, String author,String regdate, String price, 
    		String title1) {
    	try {
            connect();              
            stmt = conn.createStatement(); 
           
            stmt.executeUpdate("UPDATE books SET isbn =" + "'" + isbn + "'," + 
            "title = " + "'" + title + "'," +
            "author = " + "'" + author + "'," +
            "regdate = TO_DATE(" + "'" + regdate + "','YYYY-MM-DD')," +  // DATE형식으로 입력
            "price = " + "'" + price + "'" +
            "WHERE title = " + "'" + title1 + "'");
             
            		
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void freeUpResources(){ // 자원 반환
        if (rs != null) {		   // rs,stmt의 데이터와 JDBC의 자원이 닫힐때까지 기다리지 않고 해제
            try {				   // 불필요한 자원(메모리) 낭비 예방
                rs.close();
            } catch (SQLException sqlEx) {
            } // ignore
            rs = null;
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException sqlEx) {
            } // ignore

            stmt = null;
        }

    }
}
