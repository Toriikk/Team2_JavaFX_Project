package controller;


import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.DB;






public class Controller {
    private static DB dbSQL = new DB();  // DB클래스를 참조하는 dbSQL 객체 생성. 이것으로 connect() 메소드 사용가능
    private static Stage searchByNameStage;
    private static Stage searchByISBNStage;
    private static Stage searchByTitleStage;
    private static Stage addBookStage;
    private static Stage deleteBookStage;
    private static Stage editBookStage;
    private static Stage editQuantityStage;
  
    
    
    // [추가된 부분]책 제목 검색창//
    public void searchByTitleWindow(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/searchByTitle.fxml")); // fxml파일이 같은 패키지가 아닌곳에 있을때 호출하는 방법
            Parent root1 = (Parent) fxmlLoader.load(); // fxml파일이 같은 패키지가 아닌곳에 있을때 호출하는 방법, parent는 루트 컨테이너 (HBOX,VBOX 같은 것들)
            this.searchByTitleStage = new Stage();  // 스테이지 생성
            searchByTitleStage.setScene(new Scene(root1)); // scene 생성 + stage를 통해 출력
            searchByTitleStage.setTitle("책 제목으로 검색");
            searchByTitleStage.show(); // 스테이지 표시
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void searchByNameWindow(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/searchByName.fxml")); 
            Parent root1 = (Parent) fxmlLoader.load(); 
            this.searchByNameStage = new Stage();  
            searchByNameStage.setScene(new Scene(root1)); 
            searchByNameStage.setTitle("저자 이름으로 검색");
            searchByNameStage.show(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void searchByISBNWindow(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/searchByISBN.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            this.searchByISBNStage = new Stage();
            searchByISBNStage.setScene(new Scene(root1));
            searchByISBNStage.setTitle("ISBN으로 검색");
            searchByISBNStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addBookWindow(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/addBook.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            this.addBookStage = new Stage();
            addBookStage.setScene(new Scene(root1));
            addBookStage.setTitle("책 추가");
            addBookStage.show(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void deleteBookWindow(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/deleteBook.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            this.deleteBookStage = new Stage();
            deleteBookStage.setScene(new Scene(root1));
            deleteBookStage.setTitle("책 삭제");
            deleteBookStage.show(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void editBookWindow(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/editBook.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            this.editBookStage = new Stage();
            editBookStage.setScene(new Scene(root1));
            editBookStage.setTitle("책 수정");
            editBookStage.show(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void editQuantityWindow(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/editQuantity.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            this.editQuantityStage = new Stage();
            editQuantityStage.setScene(new Scene(root1));
            editQuantityStage.setTitle("재고 관리");
            editQuantityStage.show(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //[추가된 부분] 책 제목으로 검색 버튼&이벤트
    public void searchByTitleButton(ActionEvent event) {
        Label printCommand = (Label) searchByTitleStage.getScene().lookup("#command");
        printCommand.setVisible(false);
        TextField title = (TextField) searchByTitleStage.getScene().lookup("#title");

        if (title.getText().isEmpty()) {
            printCommand.setVisible(true);
            printCommand.setText("title 필드는 비워둘 수 없습니다!");
        } else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/TableView.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Stage tableViewStage = new Stage();
                tableViewStage.setScene(new Scene(root1));
                tableViewStage.setTitle("제목으로 검색 결과");
                TableView tableview = (TableView) tableViewStage.getScene().lookup("#tableview");

                ObservableList<ObservableList> data = FXCollections.observableArrayList();

                dbSQL.connect();
                dbSQL.stmt = dbSQL.conn.createStatement();
                
                dbSQL.rs = dbSQL.stmt.executeQuery("SELECT * FROM books WHERE title LIKE '%" + title.getText() + "%'");

                for (int i = 0; i < dbSQL.rs.getMetaData().getColumnCount(); i++) {
                    //We are using non property style for making dynamic table
                    final int j = i;
                    TableColumn col = new TableColumn(dbSQL.rs.getMetaData().getColumnName(i + 1));
                    col.setCellValueFactory((Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString()));
                    tableview.getColumns().addAll(col);
                    System.out.println("Column [" + i + "] ");
                }

                /* Data added to ObservableList */
                while (dbSQL.rs.next()) {
                    //Iterate Row
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for (int i = 1; i <= dbSQL.rs.getMetaData().getColumnCount(); i++) {
                        
                        row.add(dbSQL.rs.getString(i));
                    }
                    System.out.println("Row [1] added " + row);
                    data.add(row);
                }
                tableview.setItems(data);
                tableViewStage.show();
                searchByTitleStage.close();

            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                
                dbSQL.freeUpResources();
            }
        }
    }
    //[추가된 부분] 책 전체 목록 조회
    public void viewAllBookButton(ActionEvent event) {
        
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/TableView.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Stage tableViewStage = new Stage();
                tableViewStage.setScene(new Scene(root1));
                tableViewStage.setTitle("책 전체 목록");
                TableView tableview = (TableView) tableViewStage.getScene().lookup("#tableview");

                ObservableList<ObservableList> data = FXCollections.observableArrayList();

                dbSQL.connect();
                dbSQL.stmt = dbSQL.conn.createStatement();
                
                dbSQL.rs = dbSQL.stmt.executeQuery("SELECT * FROM books");

                for (int i = 0; i < dbSQL.rs.getMetaData().getColumnCount(); i++) {
                    //We are using non property style for making dynamic table
                    final int j = i;
                    TableColumn col = new TableColumn(dbSQL.rs.getMetaData().getColumnName(i + 1));
                    col.setCellValueFactory((Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString()));
                    tableview.getColumns().addAll(col);
                    System.out.println("Column [" + i + "] ");
                }

                /* Data added to ObservableList */
                while (dbSQL.rs.next()) {
                    //Iterate Row
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for (int i = 1; i <= dbSQL.rs.getMetaData().getColumnCount(); i++) {
                        
                        row.add(dbSQL.rs.getString(i));
                    }
                    System.out.println("Row [1] added " + row);
                    data.add(row);
                }
                tableview.setItems(data);
                tableViewStage.show();
//               tableViewStage.close();

            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                
                dbSQL.freeUpResources();
            }
        }
    

    public void searchByNameButton(ActionEvent event) {
        Label printCommand = (Label) searchByNameStage.getScene().lookup("#command");  // id가 command인 컨트롤 불러오기
        printCommand.setVisible(false);												   // printCommand가 경고창 역할하는 라벨
        TextField name = (TextField) searchByNameStage.getScene().lookup("#name");
        if (name.getText().isEmpty()) {
            printCommand.setVisible(true);
            printCommand.setText("빈칸 입니다!");
        } else {
            try {  // name이라는 id를 가진 텍스트 필드에 빈칸이 없을때 수행
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/TableView.fxml"));  
                Parent root1 = (Parent) fxmlLoader.load(); // fxml 로드한후
                Stage tableViewStage = new Stage();	// stage 생성
                tableViewStage.setScene(new Scene(root1)); // scene 생성
                tableViewStage.setTitle("작가명으로 검색 결과"); 
                TableView tableview = (TableView) tableViewStage.getScene().lookup("#tableview");

                ObservableList<ObservableList> data = FXCollections.observableArrayList();  // ObservableList를 구현시키기 위한 객체 생성
                																			// ObservableList는 배열의 한 종류
				                                                                            // ListView 컨트롤을 위해 필요 
                																			// ListView는 항목들을 목록으로 보여주는 역할
                dbSQL.connect(); //sql과 연결 시도
                dbSQL.stmt = dbSQL.conn.createStatement(); // sql문을 받아주는 stmt 생성
                dbSQL.rs = dbSQL.stmt.executeQuery("SELECT * FROM books WHERE author LIKE '%" + name.getText() + "%'"); 
                // stmt를 통해 위 sql문을 실행하고 그 결과를 rs에 저장함( 검색한 결과 [name.getText()]  와 같은 저자를 가진 책 목록 출력하기) 
                
                //동적으로 테이블을 생성= 빈 화면인 tableview에서 테이블을 만듬 //
                for (int i = 0; i < dbSQL.rs.getMetaData().getColumnCount(); i++) {
                    // rs에 저장된 총 열 개수 만큼 for문을 돌림
                    final int j = i;
                    TableColumn col = new TableColumn(dbSQL.rs.getMetaData().getColumnName(i + 1));  // TableColumn : 해당 열의 내용을 편집하는 클래스
                                                         // sql문을 실행했던 결과를 저장한 rs의 정보를 가져온 후 
                    									 // 해당되는 열의 이름을 참조하는 객체인 col 생성
                    col.setCellValueFactory((Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString()));
                    // param 은 특정 cell에 대한 정보를 제공하는데 필요한 객체 
                    // setCellValueFactory는 대상의 cell안에 있는 값을 추출함
                    // Callback은 ObservableList안에 있는 특정 행의 데이터를 가져옴. 
                    tableview.getColumns().addAll(col);
                    System.out.println("Column [" + i + "] ");
                }

                //이제 실제로 테이블에 데이터가 저장//
                while (dbSQL.rs.next()) { // rs에 값이 존재하지 않을때까지 반복
                    //Iterate Row
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for (int i = 1; i <= dbSQL.rs.getMetaData().getColumnCount(); i++) {
                        //Iterate Column
                        row.add(dbSQL.rs.getString(i));
                    }
                    System.out.println("Row [1] added " + row);
                    data.add(row);
                }
                //FINALLY ADDED TO TableView
                tableview.setItems(data);
                tableViewStage.show();
                searchByNameStage.close();

            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                
                dbSQL.freeUpResources(); // DB클래스에 있음.자원반환, <=메모리 절약
            }
        }
    }

    public void searchByISBNButton(ActionEvent event) {
        Label printCommand = (Label) searchByISBNStage.getScene().lookup("#command");
        printCommand.setVisible(false);
        TextField isbn = (TextField) searchByISBNStage.getScene().lookup("#isbn");

        if (isbn.getText().isEmpty()) {
            printCommand.setVisible(true);
            printCommand.setText("ISBN 필드는 비워둘 수 없습니다!");
        } else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/TableView.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Stage tableViewStage = new Stage();
                tableViewStage.setScene(new Scene(root1));
                tableViewStage.setTitle("ISBN으로 검색 결과");
                TableView tableview = (TableView) tableViewStage.getScene().lookup("#tableview");

                ObservableList<ObservableList> data = FXCollections.observableArrayList();

                dbSQL.connect();
                dbSQL.stmt = dbSQL.conn.createStatement();
                
                dbSQL.rs = dbSQL.stmt.executeQuery("SELECT * FROM books WHERE ISBN= " + isbn.getText());

                for (int i = 0; i < dbSQL.rs.getMetaData().getColumnCount(); i++) {
                    //We are using non property style for making dynamic table
                    final int j = i;
                    TableColumn col = new TableColumn(dbSQL.rs.getMetaData().getColumnName(i + 1));
                    col.setCellValueFactory((Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString()));
                    tableview.getColumns().addAll(col);
                    System.out.println("Column [" + i + "] ");
                }

                /* Data added to ObservableList */
                while (dbSQL.rs.next()) {
                    //Iterate Row
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for (int i = 1; i <= dbSQL.rs.getMetaData().getColumnCount(); i++) {
                        
                        row.add(dbSQL.rs.getString(i));
                    }
                    System.out.println("Row [1] added " + row);
                    data.add(row);
                }
                
                tableview.setItems(data);
                tableViewStage.show();
                searchByISBNStage.close();

            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                
                dbSQL.freeUpResources();
            }
        }
    }

    public void addBookButton(ActionEvent event) {
        Label printCommand = (Label) addBookStage.getScene().lookup("#command");
        printCommand.setVisible(false);
        TextField isbn = (TextField) addBookStage.getScene().lookup("#isbn");
        TextField title = (TextField) addBookStage.getScene().lookup("#title");
        TextField author = (TextField) addBookStage.getScene().lookup("#author");
        TextField regdate = (TextField) addBookStage.getScene().lookup("#regdate");
        TextField price = (TextField) addBookStage.getScene().lookup("#price");
        TextField quantity = (TextField) addBookStage.getScene().lookup("#quantity");
        
        if (isbn.getText().isEmpty() || title.getText().isEmpty() || author.getText().isEmpty() || regdate.getText().isEmpty() || price.getText().isEmpty() || quantity.getText().isEmpty()) {
            printCommand.setVisible(true);
            printCommand.setText("어떤 필드도 비울 수 없습니다!");
        } else {
            dbSQL.addBook(isbn.getText(), title.getText(),author.getText(), regdate.getText(), price.getText(),quantity.getText() );

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "완료! :D", ButtonType.OK);
            alert.setTitle("추가되었습니다!");
            alert.setHeaderText(null);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                addBookStage.close();
            }
            dbSQL.freeUpResources();
        }
    }

    // --------------------------------테스트------------------------------------------//
    
//    public static class Book {
//        private final SimpleStringProperty isbn;
//        private final SimpleStringProperty title;
//        private final SimpleStringProperty author;
//        private final SimpleStringProperty regdate;
//        private final SimpleStringProperty price;
//    //    private final SimpleStringProperty Publisher ;  출판사
//    //    private final SimpleIntegerProperty Quantity ;  재고
//    //    private final SimpleBooleanProperty Availability ;  가능여부
//   
//    // get()으로 정보를 가져오기 위한 절차
//    public Book(String isbn, String title, String author,String regdate, String price) {
//        this.isbn = new SimpleStringProperty(isbn);  
//        this.title = new SimpleStringProperty(title);
//        this.author = new SimpleStringProperty(author);
//        this.regdate = new SimpleStringProperty(regdate);
//        this.price = new SimpleStringProperty(price);
//    //    this.Publisher =new SimpleStringProperty(Publisher);
//    //   this.Quantity = new SimpleIntegerProperty(Quantity);
//    //    this.Availability = new SimpleBooleanProperty(Availability);
//    }
//    // get()
//    public String getTitle() {
//            return title.get();
//       } 
//    }
   
 // [추가된 부분] 책 삭제
   	public void deleteBookButton(ActionEvent event) {
   		
   		
    TextField deleteBookTitle = (TextField) deleteBookStage.getScene().lookup("#deleteBookTitle"); // deleteBook.fxml에 있는 
    			                                                                                   // deleteBookTitle이름을 가진 텍스트 필드
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION); // 알람 발생
    alert.setTitle("도서 삭제");
    alert.setContentText("정말 삭제하시겠습니까? 책제목 : " + deleteBookTitle.getText()); // deleteBookTitle 텍스트 필드 내에 있는 
                                                                                   // 텍스트를 읽어옴
     
     Optional<ButtonType> answer = alert.showAndWait(); // answer라는 버튼 생성
     
    if(answer.get() == ButtonType.OK){ // 만약 확인 버튼을 누르면 
    	 dbSQL.deleteBook(deleteBookTitle.getText());  // DB클래스에 있는 deleteBook 메소드를 실행
     Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "완료! :D", ButtonType.OK);
     alert2.setTitle("삭제되었습니다!");
     alert2.setHeaderText(null);
     alert2.showAndWait();
     if (alert2.getResult() == ButtonType.OK) {
         deleteBookStage.close();  // 확인 버튼 누르면 창 종료
     }
   }
 }
   	// [추가된 부분] 책 수정
 	public void editBookButton(ActionEvent event) {
 		TextField editTargetTitle = (TextField) editBookStage.getScene().lookup("#editTargetTitle");
        
        TextField editIsbn = (TextField) editBookStage.getScene().lookup("#editIsbn");
        TextField editTitle = (TextField) editBookStage.getScene().lookup("#editTitle");
        TextField editAuthor = (TextField) editBookStage.getScene().lookup("#editAuthor");
        TextField editRegdate = (TextField) editBookStage.getScene().lookup("#editRegdate");
        TextField editPrice = (TextField) editBookStage.getScene().lookup("#editPrice");
        TextField editQuantity = (TextField) editBookStage.getScene().lookup("#editQuantity");
   		
 	    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
 	    alert.setTitle("도서 수정");
 	    alert.setContentText("정말 수정하시겠습니까? 책제목 :" + editTargetTitle.getText());
 	     
 	     Optional<ButtonType> answer = alert.showAndWait();
 	     
 	    if(answer.get() == ButtonType.OK){
 	    	 dbSQL.editBook( editIsbn.getText(), editTitle.getText(),editAuthor.getText(), editRegdate.getText(), editPrice.getText(), editQuantity.getText(),
 	    			editTargetTitle.getText());
 	    // isbn, 제목, 저자, 등록일, 가격을 입력하여 바꾼다.
 	     Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "완료! :D", ButtonType.OK);
 	     alert2.setTitle("수정되었습니다!");
 	     alert2.setHeaderText(null);
 	     alert2.showAndWait();
 	     if (alert2.getResult() == ButtonType.OK) {
 	         editBookStage.close();
 	     }
     }
  }
 	// [추가된 부분] 책 재고 추가 및 삭제
 	public void addQuantityButton(ActionEvent event) {
 		TextField editTargetTitle1 = (TextField) editQuantityStage.getScene().lookup("#editTargetTitle1");
 		TextField QuantityNumber = (TextField) editQuantityStage.getScene().lookup("#QuantityNumber");
 		 
 		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
 	 	    alert.setTitle("재고 추가");
 	 	    alert.setContentText("정말 추가하시겠습니까? 책제목 :" + editTargetTitle1.getText()
 	 	    +"추가할 수량 : " + QuantityNumber.getText() +"권");
 	 	  Optional<ButtonType> answer = alert.showAndWait();
 	 	  if(answer.get() == ButtonType.OK){
  	    	 dbSQL.addQuantity(QuantityNumber.getText(),editTargetTitle1.getText() );
  	     Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "완료! :D", ButtonType.OK);
  	     alert2.setTitle("추가되었습니다!");
  	     alert2.setHeaderText(null);
  	     alert2.showAndWait();
  	     if (alert2.getResult() == ButtonType.OK) {
  	         editQuantityStage.close();
  	     }
      }
 	}
 	
 	public void deleteQuantityButton(ActionEvent event) {
 		TextField editTargetTitle = (TextField) editQuantityStage.getScene().lookup("#editTargetTitle");
 		TextField QuantityNumber = (TextField) editQuantityStage.getScene().lookup("#QuantityNumber");
 		 
 		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
 	 	    alert.setTitle("재고 삭제");
 	 	    alert.setContentText("정말 삭제하시겠습니까? 책제목 :" + editTargetTitle.getText()
 	 	    +"삭제할 수량 : " + QuantityNumber.getText() +"권");
 	 	  Optional<ButtonType> answer = alert.showAndWait();
 	 	  if(answer.get() == ButtonType.OK){
  	    	 dbSQL.deleteQuantity(QuantityNumber.getText(),editTargetTitle.getText() );
  	     Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "완료! :D", ButtonType.OK);
  	     alert2.setTitle("삭제되었습니다!");
  	     alert2.setHeaderText(null);
  	     alert2.showAndWait();
  	     if (alert2.getResult() == ButtonType.OK) {
  	         editQuantityStage.close();
  	     }
      }
 	}
 	
 	
 	//[미구현] 책 정보 확인버튼
 	public void printInformation(ActionEvent event) {
 		TextField editTargetTitle = (TextField) editBookStage.getScene().lookup("#editTargetTitle");
 		Label printInformation = (Label) editBookStage.getScene().lookup("#printBookInformation");
 		dbSQL.searchByTitle(editTargetTitle.getText());
 		printInformation.setVisible(true);
 		printInformation.setText(editTargetTitle.getText());
 		
 		
 	}
 	
}


