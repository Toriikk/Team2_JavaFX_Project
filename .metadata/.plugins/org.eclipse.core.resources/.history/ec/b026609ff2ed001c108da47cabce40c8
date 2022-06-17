package model;

import java.sql.*;

public class DB {
    public Connection conn;
    public Statement stmt; //DB 쿼리 명령 클래스
    public ResultSet rs; //명령에 대한 반환값,테이블로 반환

    public void connect() {
        for (int i = 0; i < 3; i++) {
            try { //예외가 일어날 가능성
                Class.forName("oracle.jdbc.OracleDriver").newInstance(); // JDBC DB연동을 위한 기본제공 함수 // oracle.jdbc.OracleDriver 연동한 DB URL
                conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","bg","bg"); //DB연결을 위한 Connection 객체
                break;
            } catch (SQLException ex) {  // 오류가 발생했을떄 메세지
                // handle any errors
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            } catch (Exception e) {
                e.printStackTrace(); //에러를 단계별로 출력
            }
        }
    }
//---------DB에 삽입할 데이터 메소드-------
    public void addBook(String isbn, String title, String author, String regdate, String price) {
        try {
            connect();
            stmt = conn.createStatement(); 
            stmt.executeUpdate("INSERT INTO books (isbn, title, author, regdate) VALUES (" +     //DB에 INSERT 명령
                    "'" + isbn + "'," +
                    "'" + title + "'," +
                    "'" + author + "'," +
                   regdate + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void freeUpResources(){
        if (rs != null) {
            try {
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



    public void searchByName(String name) {
        try {
            connect();
            stmt = conn.createStatement(); 

            rs = stmt.executeQuery("SELECT * FROM books WHERE author LIKE '%" + name + "%'"); //executeQuery메소드를 통해  조회하고 반환

            ResultSetMetaData rsmd = rs.getMetaData();  //Result클래스의 getMetaDate()메소드 호출 
            int columnCount = rsmd.getColumnCount();  //result의 총 필드 수 반환
            for (int i = 1; i <= columnCount; i++) {//필드의 목록
                String colName = rsmd.getColumnName(i); //필드 이름
                System.out.print(colName + ":" + getSpacesToPrint(colName + ":"));
            }
            System.out.println();
            while (rs.next()) {//다음행선택,행이 있으면 true 없으면 false
                for (int i = 1; i <= columnCount; i++) {
                    String str = rs.getString(i); //변수 str에 행마다 이름 저장
                    System.out.print(str + getSpacesToPrint(str));
                }
                System.out.println();
            }
        } catch (SQLException ex) {
            // handle any errors
            ex.printStackTrace();
        } finally {
            this.freeUpResources();
        }
    }

    public void searchByISBN(String isbnNumber) {
        try {
            connect();
            stmt = conn.createStatement();
    

            rs = stmt.executeQuery("SELECT * FROM books WHERE ISBN= " + isbnNumber);

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String colName = rsmd.getColumnName(i);
                System.out.print(colName + ":" + getSpacesToPrint(colName + ":"));
            }
            System.out.println();
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String str = rs.getString(i);
                    System.out.print(str + getSpacesToPrint(str));
                }
                System.out.println();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();

        } finally {
           
            this.freeUpResources();
        }
    }
    public void listNames() {
        try {
            connect();
            stmt = conn.createStatement();

            
            rs = stmt.executeQuery("SELECT author FROM books");

            while (rs.next()) {
                String name = rs.getString(1);
                System.out.println("사용자: " + name);
            }
        } catch (SQLException ex) {
            // handle any errors

        } finally {
            
            if (rs != null) {
                try {
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
    private String getSpacesToPrint(String wordToPrint) {
        Integer i = 40;
        String str = new String("          " + "          " + "          " + "          "); 
        if (wordToPrint.length() > i) {
            throw new RuntimeException("String to print is too long");
        }
        return str.substring(0, i - wordToPrint.length());
    }
}
