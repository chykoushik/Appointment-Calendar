package sample;


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





public class userProfileController implements Initializable {

    @FXML
    private TextField idField;

    @FXML
    private TextField souField;

    @FXML
    private TextField destField;

    @FXML
    private DatePicker datetField;

    @FXML
    private TextField detailsField;

    @FXML
    private TextField apprv;

    @FXML
    private Button insertButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button refreshButton;

    @FXML
    private Button Invitation;



    @FXML
    private TextField searchButton;

    ObservableList<Books> dataList;

    @FXML
    private Button logoutButton;


    @FXML
    private TextField idField1;

    //@FXML
    //private TextField apprv1;

    @FXML
    private ComboBox apprv1;

    @FXML
    private TableView<Books> TableView;

    @FXML
    private TableColumn<Books, Integer> idColumn;

    @FXML
    private TableColumn<Books, String> souColumn;

    @FXML
    private TableColumn<Books, String> destColumn;

    @FXML
    private TableColumn<Books, String> datetColumn;

    @FXML
    private TableColumn<Books, String> detailsColumn;

    @FXML
    private TableColumn<Books, String> apprvColumn;


    //for sending termin
    @FXML
    private void insertButton() {
        //String query = "insert into termininfo values("+idField.getText()+",'"+souField.getText()+"','"+destField.getText()+"','"+datetField.getValue()+"','"+detailsField.getText()+"')";
        String notify = "sent";
        //String query = "insert into termininfo1 values("+idField.getText()+",'"+readUserInfo()+"','"+destField.getText()+"','"+datetField.getValue()+"','"+detailsField.getText()+"', '"+apprv.getText()+"')";
        String query = "insert into termininfo1 values("+idField.getText()+",'"+readUserInfo()+"','"+destField.getText()+"','"+datetField.getValue()+"','"+detailsField.getText()+"', '"+notify+"')";

        executeQuery(query);
        showBooks();
    }

    //edit termin
    @FXML
    private void updateButton() throws RemoteException, Exception{
        //String query = "UPDATE termininfo1 SET Sou='"+souField.getText()+"',Dest='"+destField.getText()+"',Datet='"+datetField.getValue()+"',Details='"+detailsField.getText()+"', Apprv='"+apprv.getText()+"' WHERE ID="+idField.getText()+"";
        String query = "UPDATE termininfo1 SET Dest='"+destField.getText()+"',Datet='"+datetField.getValue()+"',Details='"+detailsField.getText()+"' WHERE ID="+idField.getText()+"";

        executeQuery(query);
        showBooks();
    }

    //remove termin
    @FXML
    private void deleteButton() throws RemoteException, Exception{
        String query = "DELETE FROM termininfo1 WHERE ID="+idField.getText()+"";
        executeQuery(query);
        showBooks();
    }

    //reject or Accept Invitation
    @FXML
    private void Invitation() throws RemoteException, Exception{
        String query = "UPDATE termininfo1 SET Apprv='"+apprv1.getValue()+"' WHERE ID="+idField1.getText()+"";
        executeQuery(query);
        showBooks();
        /*String query = "DELETE FROM termininfo1 WHERE ID="+idField1.getText()+"";
        executeQuery(query);
        showBooks();

         */

    }

    //refresh termin
    @FXML
    private void refreshButton() throws RemoteException, Exception{
        idField.clear();
        //souField.clear();
        destField.clear();
        //datetField.clear();
        detailsField.clear();
    }

    //Search Box
    @FXML
    private void searchButton() throws RemoteException, Exception{

        datetColumn.setCellValueFactory(new PropertyValueFactory<Books, String>("datet"));

        TableView.setItems(dataList);
        FilteredList<Books> filteredData = new FilteredList<>(dataList, b -> true);
        searchButton.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (String.valueOf(person.getDatet()).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else
                    return false;
            });

        });

        SortedList<Books> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(TableView.comparatorProperty());
        TableView.setItems(sortedData);



    }

    //logout
    @FXML
    private void logoutButton() throws RemoteException, Exception{
        logoutButton.getScene().getWindow().hide();
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


        apprv1.getItems().addAll("accept", "reject");

        showBooks();
    }

    //DB connection
    public Connection getConnection(){
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

    //file reading for specific user. It will help to show info of a specific logged in user.
    private String readUserInfo() {
        BufferedReader reader;
        String file = "userinfo.txt";
        String user = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line =reader.readLine();
            if (line != null){
                user = line.strip();
            }
            else {user = "admin";}
        }catch (IOException e){
            e.printStackTrace();
        }
        return user;
    }

    //data for tableview
    public ObservableList<Books> getBooksList(){
        ObservableList<Books> booksList = FXCollections.observableArrayList();
        Connection connection = getConnection();

        String userName = readUserInfo();
        String all;
        String query = "SELECT * FROM termininfo1 where sou = '" +userName + "' or dest = '" +userName + "' or dest = 'all'";
        //System.out.println(query);
        //System.out.println(userName);
        //String query = "SELECT termininfo.id, termininfo.sou, termininfo.dest, termininfo.datet, termininfo.details FROM termininfo INNER JOIN users ON termininfo.sou =" + userName;




        Statement st;
        ResultSet rs;

        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            Books books;
            while(rs.next()) {
                books = new Books(rs.getInt("Id"),rs.getString("Sou"),rs.getString("Dest"),rs.getString("Datet"),rs.getString("Details"),rs.getString("Apprv"));
                booksList.add(books);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return booksList;
    }


    public void showBooks() {
        ObservableList<Books> list = getBooksList();

        idColumn.setCellValueFactory(new PropertyValueFactory<Books,Integer>("id"));
        souColumn.setCellValueFactory(new PropertyValueFactory<Books,String>("sou"));
        destColumn.setCellValueFactory(new PropertyValueFactory<Books,String>("dest"));
        datetColumn.setCellValueFactory(new PropertyValueFactory<Books, String>("datet"));
        detailsColumn.setCellValueFactory(new PropertyValueFactory<Books, String>("details"));
        apprvColumn.setCellValueFactory(new PropertyValueFactory<Books, String>("apprv"));


        TableView.setItems(list);
    }



}
