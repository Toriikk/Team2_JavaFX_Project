package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import backend.*;


//public class Main {
//    public static void main(String[] args){
//        DB database = new DB();
//        database.searchByName("Hemingway");
//        database.searchByISBN("1234567891234");
//        database.addBook("9876543219876", "Moja ksiazka" , "Michal P" , "2019");
//    }
//}


    public class Main extends Application {
        private static Stage primaryStage;
        static  private void setPrimaryStage(Stage stage){
            Main.primaryStage=stage;
        }
        static public Stage getPrimaryStage(){
            return Main.primaryStage;
        }

    @Override
    public void start(Stage primaryStage) throws Exception{
        setPrimaryStage(primaryStage);
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("SQL BOOKS");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setResizable(false);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
