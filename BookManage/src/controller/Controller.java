package sample;

import backend.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Controller {
    private static DB dbSQL = new DB();
    private static Stage searchByNameStage;
    private static Stage searchByISBNStage;
    private static Stage addBookStage;

    public void searchByNameWindow(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("searchByName.fxml"));
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("searchByISBN.fxml"));
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addBook.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            this.addBookStage = new Stage();
            addBookStage.setScene(new Scene(root1));
            addBookStage.setTitle("책 추가");
            addBookStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void searchByNameButton(ActionEvent event) {
        Label printCommand = (Label) searchByNameStage.getScene().lookup("#command");
        printCommand.setVisible(false);
        TextField name = (TextField) searchByNameStage.getScene().lookup("#name");
        if (name.getText().isEmpty()) {
            printCommand.setVisible(true);
            printCommand.setText("빈칸 입니다!");
        } else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TableView.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Stage tableViewStage = new Stage();
                tableViewStage.setScene(new Scene(root1));
                tableViewStage.setTitle("쿼리 결과");
                TableView tableview = (TableView) tableViewStage.getScene().lookup("#tableview");

                ObservableList<ObservableList> data = FXCollections.observableArrayList();

                dbSQL.connect();
                dbSQL.stmt = dbSQL.conn.createStatement();
                // Wyciagamy wszystkie wiersze danego autora
                dbSQL.rs = dbSQL.stmt.executeQuery("SELECT * FROM books WHERE author LIKE '%" + name.getText() + "%'");

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
                
                dbSQL.freeUpResources();
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
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TableView.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Stage tableViewStage = new Stage();
                tableViewStage.setScene(new Scene(root1));
                tableViewStage.setTitle("쿼리 결과");
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
        
        if (isbn.getText().isEmpty() || title.getText().isEmpty() || author.getText().isEmpty() || regdate.getText().isEmpty() || price.getText().isEmpty()) {
            printCommand.setVisible(true);
            printCommand.setText("어떤 필드도 비울 수 없습니다!");
        } else {
            dbSQL.addBook(isbn.getText(), title.getText(),author.getText(), regdate.getText(), price.getText() );

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Dodano! :D", ButtonType.OK);
            alert.setTitle("추가되었습니다!");
            alert.setHeaderText(null);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                addBookStage.close();
            }
            dbSQL.freeUpResources();
        }
    }
}
