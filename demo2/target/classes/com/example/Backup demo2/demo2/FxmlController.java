package com.example.demo2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.Optional;

public class FxmlController {

    @FXML
    private Button Close;
    public void Close(){
        System.exit(0);
    }

    @FXML
    private Button loginBtn;

    @FXML
    private Button Login_signupBtn;

    @FXML
    private TextField Signup_username;

    @FXML
    private PasswordField Signup_password;

    @FXML
    private PasswordField Signup_reenter;

    @FXML
    private Button Login_returnBtn;

    @FXML
    private AnchorPane main_form1;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    private Button SignupBtn;

    @FXML
    private AnchorPane right_form1;

    @FXML
    private AnchorPane right_form2;

    //Prepare Connection to connect database
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    //Drag and drop
    private double x;
    private double y;

    public static CurrentUserData userData;

    public void loginAdmin()
    {
        username.setText("phongnhvp");
        password.setText("P333");
        Alert alert;
        String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";
        connect = Database.connectDB();
        try{
            assert connect != null;
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, username.getText());
            prepare.setString(2, password.getText());

            result = prepare.executeQuery();

            if (username.getText().isEmpty() || password.getText().isEmpty())
            {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText(null);
                alert.setContentText("Please fill in the blank fields");
                alert.showAndWait();
            } else{
                if (result.next()){
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message!");
                    alert.setHeaderText(null);
                    alert.setContentText("Login Successfully");

                    userData = new CurrentUserData(result.getString(1),new Currency(result.getString(3)));
                    System.out.println(userData.getUsername() + "\n" + userData.getCurrency().getType());

                    loginBtn.getScene().getWindow().hide();

                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("dashboard.fxml")));

                    Stage stage = new Stage();
                    Scene scene = new Scene(root);

                    root.setOnMousePressed((MouseEvent event) ->{
                        x = event.getSceneX();
                        y = event.getSceneY();
                    });

                    scene.setOnMouseDragged((MouseEvent event) ->{
                        stage.setX(event.getScreenX() - x);
                        stage.setY(event.getScreenY() - y);
                    });

                    stage.initStyle(StageStyle.TRANSPARENT);

                    stage.setScene(scene);
                    stage.show();

                }
                else{
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong Username or Password");
                    alert.showAndWait();
                }
            }
        } catch(Exception e){e.printStackTrace(System.out);
        }
    }

    public void MoveToSignUp()
    {
        right_form1.setVisible(false);
        right_form2.setVisible(true);
    }

    public void MoveToLogin(){
        right_form2.setVisible(false);
        right_form1.setVisible(true);
    }

    public void SignUpUser()
    {
        Alert alert;
        String sql = "INSERT INTO admin (username,password) VALUES(?,?)";
        connect = Database.connectDB();
        //System.out.println(Signup_username.getText() + Signup_password.getText());
        try{
            if (Signup_password.getText().isEmpty()
              ||Signup_username.getText().isEmpty()
              ||Signup_reenter.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message!");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }
            else if (Signup_reenter.getText().equals(Signup_password.getText()))
            {
                String sqlcheck = "SELECT username FROM admin WHERE username = ?";
                PreparedStatement preparecheck;
                preparecheck = connect.prepareStatement(sqlcheck);
                preparecheck.setString(1,Signup_username.getText());

                result = preparecheck.executeQuery();
                if (result.next())
                {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message!");
                    alert.setHeaderText(null);
                    alert.setContentText("This username has already been used");
                    alert.showAndWait();
                }
                else {
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, Signup_username.getText());
                    prepare.setString(2, Signup_password.getText());

                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("INFORMATION MESSAGE");
                    alert.setHeaderText(null);
                    alert.setContentText("You have successfully sign up \nProceed to Login?");
                    Optional<ButtonType> option =  alert.showAndWait();
                    if (option.get().equals(ButtonType.OK)){
                        MoveToLogin();
                    }
                }
            }
            else{
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR MESSAGE");
                alert.setHeaderText(null);
                alert.setContentText("Your re-enter password doesn't match the previous one");
                alert.showAndWait();
            }
        }catch(Exception e) {
            e.printStackTrace(System.out);
        }
    }
}
