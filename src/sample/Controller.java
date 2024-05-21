package sample;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.rmi.Remote;
import java.rmi.RemoteException;

public class Controller implements Initializable {
    @FXML
    private AnchorPane pane_login;

    @FXML
    private TextField txt_username;

    @FXML
    private PasswordField txt_password;

    @FXML
    private ComboBox type;

    @FXML
    private Button btn_login;

    @FXML
    private AnchorPane pane_signup;

    @FXML
    private TextField txt_username_up;

    @FXML
    private TextField txt_password_up;

    @FXML
    private TextField txt_username1;

    @FXML
    private PasswordField txt_password1;

    @FXML
    private Button btn_login1;


    @FXML
    private TextField email_up;

    @FXML
    private ComboBox type_up;

    @FXML
    private Button btn_signup;

    @FXML
    private TextField admin_username;

    @FXML
    private PasswordField admin_password;

    @FXML
    private ComboBox admin_type;

    @FXML
    private Button admin_login;

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;


    public void LoginpaneShow() throws RemoteException, Exception{
        pane_login.setVisible(true);
        pane_signup.setVisible(false);
    }

    public void SignuppaneShow () throws RemoteException, Exception{
        pane_login.setVisible(false);
        pane_signup.setVisible(true);
    }


    //User login
    private boolean storeUser(String userName) throws RemoteException, Exception{

        String file = "userinfo.txt";
        try {
            File myObj = new File(file);
            if (myObj.createNewFile()){
                System.out.println("FIle Created");
                FileWriter myWriter = new FileWriter(file);
                myWriter.write(userName);
                myWriter.close();
            }else {
                FileWriter myWriter = new FileWriter(file);
                myWriter.write(userName);
                myWriter.close();
            }
        }catch (IOException e){
                System.out.println(" File error occured");
        }
        return false;
    }
    //User Login: check username and password from users table from database
    @FXML
    private void Login (ActionEvent event) throws RemoteException, Exception{
        conn = mysqlconnect.ConnectDb();
        //String sql = "Select * from users where username = ? and password = ? and type = ?";
        String sql = "Select * from users where username = ? and password = ?";
        try{
            storeUser(txt_username.getText());
            //System.out.println(txt_username.getText());
            pst =conn.prepareStatement(sql);
            pst.setString(1, txt_username.getText());
            pst.setString(2, txt_password.getText());
           //pst.setString(3, type.getValue().toString());

            rs = pst.executeQuery();
            if(rs.next()){

                JOptionPane.showMessageDialog(null, "ok");

                btn_login.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("userProfile.fxml"));
                Stage mainStage = new Stage();
                Scene scene = new Scene(root);
                mainStage.setScene(scene);
                mainStage.show();
            }else
                JOptionPane.showMessageDialog(null, "Invalid! Try Again or User is Deleted");
            }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }

    }

    //Admin Login
    @FXML

    private void AdminLogin (ActionEvent event) throws RemoteException, Exception{
        conn = mysqlconnect.ConnectDb();

        //String sql = "Select * from users where username = ? and password = ? and type = ?";
        String sql = "Select * from admin where username = ? and password = ?";
        try{
            //storeUser(txt_username1.getText());
            //System.out.println(txt_username.getText());
            pst =conn.prepareStatement(sql);
            pst.setString(1, txt_username1.getText());
            pst.setString(2, txt_password1.getText());
            //pst.setString(3, type.getValue().toString());

            rs = pst.executeQuery();
            if(rs.next()){
                JOptionPane.showMessageDialog(null, "ok");

                btn_login.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("admin.fxml"));
                Stage mainStage = new Stage();
                Scene scene = new Scene(root);
                mainStage.setScene(scene);
                mainStage.show();
            }else
                JOptionPane.showMessageDialog(null, "Invalid! Try Again");
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }

    }



    //registration
    public void add_users(ActionEvent event) throws RemoteException, Exception{
        conn = mysqlconnect.ConnectDb();
        String sql = "insert into users (username, password, type, email) values (?,?,?,?)";

        try{
            pst =conn.prepareStatement(sql);
            pst.setString(1, txt_username_up.getText());
            pst.setString(2, txt_password_up.getText());
            pst.setString(3, type_up.getValue().toString());
            pst.setString(4, email_up.getText());
            pst.execute();
            txt_username_up.clear();
            txt_password_up.clear();
            email_up.clear();

            JOptionPane.showMessageDialog(null, "User is Registered");
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }

    }

    @Override
    public void initialize (URL url, ResourceBundle rb){
            type_up.getItems().addAll("user", "user1");
            type.getItems().addAll("user");
            //admin_type.getItems().addAll("admin");

    }

}
