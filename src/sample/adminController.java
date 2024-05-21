package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ComboBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Books;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class adminController implements Initializable {

    @FXML
    private TableView<users> aTableView;

    @FXML
    private TableColumn<users, Integer> idColumna;

    @FXML
    private TableColumn<users, String> ausername;

    @FXML
    private TableColumn<users, String> apassword;

    @FXML
    private TableColumn<users, String> aemail;

    @FXML
    private TableColumn<users, String> atype;

    @FXML
    private Button deleteButtona;

    @FXML
    private TextField idFielda;

    @FXML
    private Button logoutButtona;
    ObservableList<users> dataList;

    //delete user with id
    @FXML
    private void deleteButtona() {
        String query = "DELETE FROM users WHERE user_id="+idFielda.getText()+"";
        executeQuery(query);
        showUsers();
    }

    //logout option for admin
    @FXML
    private void logoutButtona() {
        logoutButtona.getScene().getWindow().hide();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            Stage mainStage = new Stage();
            Scene scene = new Scene(root);
            mainStage.setScene(scene);
            mainStage.show();
        } catch (Exception e) {
            //np

        }
    }

    //SQL execution
    public void executeQuery(String query) {
        Connection conn = getConnection();
        Statement st;
        try {
            st = conn.createStatement();
            st.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showUsers();
    }

    //DB connection
    public Connection getConnection() {
        Connection conn;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/termin","root","");
            return conn;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    //Table View from DB
    public ObservableList<users> getusersList(){
        ObservableList<users> usersList = FXCollections.observableArrayList();
        Connection connection = getConnection();

        String query = "SELECT * FROM users";
        Statement st;
        ResultSet rs;

        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            users users;
            while(rs.next()) {
                users = new users(rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("type"));
                usersList.add(users);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usersList;
    }



    public void showUsers() {
        ObservableList<users> list = getusersList();

        idColumna.setCellValueFactory(new PropertyValueFactory<users,Integer>("id"));
        ausername.setCellValueFactory(new PropertyValueFactory<users,String>("username"));
        apassword.setCellValueFactory(new PropertyValueFactory<users,String>("password"));
        aemail.setCellValueFactory(new PropertyValueFactory<users, String>("email"));
        atype.setCellValueFactory(new PropertyValueFactory<users, String>("type"));



        aTableView.setItems(list);
    }

}
