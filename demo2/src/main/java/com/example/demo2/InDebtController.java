package com.example.demo2;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class InDebtController {

    //General Attribute
    private PreparedStatement prepare;
    private Connection connect;
    private ResultSet result;
    private Statement statement;
    private int TypeOfDebtForm;
    private boolean isSubmit = false;
    private final CurrentUserData userData = FxmlController.userData;
    private int Key = -1;

    //Type = 1 == Debt
    //Type = 2 == Obj
    //Type = 3 == Saving

    public boolean getIsSubmit(){
        return isSubmit;
    }
    public void setTypeOfDebtForm(int i) {
        TypeOfDebtForm = i;
    }

    public int getTypeOfDebtForm() {return TypeOfDebtForm;
    }
    /******
     *  Saving Controller
     * ******/

    @FXML
    private DatePicker LoanDate1;

    @FXML
    private TextField SavingAmount;

    private double SavingPrincipal;
    private String SavingTitles;
    private double SavingInterest;
    private double CurrentSavingAmount;
    private java.util.Date SavingStartDate;
    private java.util.Date SavingEndDate;

    public boolean DeleteSavingFromDatabase(){
        String deleteData = "DELETE FROM saving WHERE No = "
                + Key + " AND username = '"+ userData.getUsername()+"' ";

        connect = Database.connectDB();

        try {
            Alert alert;

            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to DELETE this data ?");

            Optional<ButtonType> option = alert.showAndWait();

            assert option.orElse(null) != null;
            if (option.orElse(null).equals(ButtonType.OK)) {

                statement = connect.createStatement();
                statement.executeUpdate(deleteData);

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Deleted!");
                alert.showAndWait();

                String sql = "INSERT INTO income (Source, Amount, Category, Date, username) VALUES (?,?,?,?,?)";

                prepare = connect.prepareStatement(sql);

                prepare.setString(1, "Saving Maturity Value from Saving " + SavingTitles);
                prepare.setDouble(2, CurrentSavingAmount);
                prepare.setString(3, "Saving Income");
                prepare.setString(4, String.valueOf(LocalDate.now()));
                prepare.setString(5, userData.getUsername());

                prepare.executeUpdate();
            } else return false;
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return true;


    }

    private void setSavingAppearance(){
        myTextField.setText(String.valueOf(SavingPrincipal));
        LoanInterest.setText(String.valueOf(SavingInterest) + '%');
        LoanTitles.setText(SavingTitles);
        LoanDate.setValue(LocalDate.parse(String.valueOf(SavingStartDate)));
        LoanDate1.setValue(LocalDate.parse(String.valueOf(SavingEndDate)));
        SavingAmount.setText(String.valueOf(CurrentSavingAmount));
        LoanRatio.setText("'" + LocalDate.now() + "' / '" + LoanDate1.getValue() + "'");

        MyDebtSubmit.setVisible(false);

        long dayDifference1 = ChronoUnit.DAYS.between( LocalDate.parse(String.valueOf(SavingStartDate)), LocalDate.now());
        long dayDifference2 = ChronoUnit.DAYS.between( LocalDate.parse(String.valueOf(SavingStartDate)), LocalDate.parse(String.valueOf(SavingEndDate)));

        LoanProgress.setProgress((double) (dayDifference1) / (double)(dayDifference2));

        LoanDate1.setEditable(false);
        SavingAmount.setEditable(false);
        LoanInterest.setEditable(false);
        myTextField.setEditable(false);
        LoanTitles.setEditable(false);
        LoanDate.setEditable(false);

        isSubmit = true;
    }

    private void UpdateSavingData(){
        LocalDate today = LocalDate.now();

        LocalDate start = LocalDate.parse(String.valueOf(SavingStartDate));
        LocalDate end = LocalDate.parse(String.valueOf(SavingEndDate));

        if (today.isBefore(end)){
            end = today;
        }

        double CurrentSavingAmount = SavingPrincipal;

        while (start.isBefore(end))
        {
            start = start.plusMonths(1);
            if (start.isAfter(end)) break;
            CurrentSavingAmount = (CurrentSavingAmount) * (1.0+ SavingInterest/100.0);
        }

        if (this.CurrentSavingAmount != CurrentSavingAmount) {
            this.CurrentSavingAmount = CurrentSavingAmount;

            String sql = "UPDATE saving SET Amount = " + this.CurrentSavingAmount
                    + " WHERE NO = " + Key;

            connect = Database.connectDB();

            try{
                assert connect != null;
                prepare = connect.prepareStatement(sql);

                prepare.executeUpdate();
            }catch (Exception e){
                e.printStackTrace(System.out);
            }
        }
    }

    private boolean CheckSavingPeriod(){
        return !LocalDate.now().isAfter(LocalDate.parse(String.valueOf(SavingEndDate)));
    }

    public void setSavingControllerAttribute(int Key,double SavingPrincipal,
                                             String savingTitles,
                                             double SavingInterest,
                                             double CurrentSavingAmount,
                                             java.util.Date SavingStartDate,
                                             java.util.Date SavingEndDate){
        this.Key = Key;
        this.SavingPrincipal = SavingPrincipal;
        this.SavingTitles = savingTitles;
        this.SavingInterest = SavingInterest;
        this.CurrentSavingAmount = CurrentSavingAmount;
        this.SavingStartDate = SavingStartDate;
        this.SavingEndDate = SavingEndDate;

        if (CheckSavingPeriod()) UpdateSavingData();

        setSavingAppearance();
    }

    public void SubmitSaving(){
        String sql = "INSERT INTO saving (username, name, principal, interest, startDate, endDate, Amount)" +
                " VALUES (?,?,?,?,?,?,?)";

        connect = Database.connectDB();

        try{
            Alert alert;
            if (myTextField.getText().isEmpty()
                    || LoanInterest.getText().isEmpty()
                    || !myTextField.getText().matches("\\d+(\\.\\d+)?")
                    || Double.parseDouble(LoanInterest.getText()) >= 100.0
                    || Double.parseDouble(LoanInterest.getText()) <= 0.0
                    || LoanDate.getValue() == null
                    || LoanTitles.getText().isEmpty()
                    || LoanDate1.getValue() == null){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message!");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields with proper input");
                alert.showAndWait();
            } else {
                prepare = connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                prepare.setString(1, userData.getUsername());
                prepare.setString(2, LoanTitles.getText());
                prepare.setString(3, myTextField.getText());
                prepare.setDouble(4, Double.parseDouble(LoanInterest.getText()));
                prepare.setString(5, String.valueOf(LoanDate.getValue()));
                prepare.setString(6, String.valueOf(LoanDate1.getValue()));
                prepare.setDouble(7, Double.parseDouble(myTextField.getText()));

                int rowAffected = prepare.executeUpdate();
                if (rowAffected > 0){
                    result = prepare.getGeneratedKeys();
                    if (result.next()) {
                        Key = result.getInt(1);

                        LoanRatio.setText("'" + LocalDate.now() + "' / '" + LoanDate1.getValue() + "'");

                        //init attribute
                        SavingPrincipal = Double.parseDouble(myTextField.getText());
                        SavingInterest = Double.parseDouble(LoanInterest.getText());
                        SavingTitles = LoanTitles.getText();
                        SavingStartDate = Date.valueOf(LoanDate.getValue());
                        SavingEndDate = Date.valueOf(LoanDate1.getValue());
                        CurrentSavingAmount = SavingPrincipal;

                        myTextField.setText(String.valueOf(Double.parseDouble(myTextField.getText())));
                        LoanInterest.setText(LoanInterest.getText() + '%');
                        SavingAmount.setText(String.valueOf(SavingPrincipal));
                        MyDebtSubmit.setVisible(false);

                        long dayDifference1 = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(String.valueOf(SavingStartDate)));
                        long dayDifference2 = ChronoUnit.DAYS.between(LocalDate.parse(String.valueOf(SavingEndDate)), LocalDate.parse(String.valueOf(SavingStartDate)));

                        LoanProgress.setProgress((double) (dayDifference1) / (double)(dayDifference2));

                        LoanDate1.setEditable(false);
                        SavingAmount.setEditable(false);
                        LoanInterest.setEditable(false);
                        myTextField.setEditable(false);
                        LoanTitles.setEditable(false);
                        LoanDate.setEditable(false);

                        isSubmit = true;

                        String Sql = "INSERT INTO expense (Purpose, Category, Amount, Date, Description, username) VALUES (?,?,?,?,?,?)";

                        connect = Database.connectDB();

                        assert connect != null;
                        prepare = connect.prepareStatement(Sql);

                        prepare.setString(1,"Add bank saving");
                        prepare.setString(2,"Accumulated Expense +");
                        prepare.setDouble(3, SavingPrincipal);
                        prepare.setString(4, String.valueOf(SavingStartDate));
                        prepare.setString(5, "Add to Saving account " + SavingTitles);
                        prepare.setString(6, userData.getUsername());

                        prepare.executeUpdate();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
    }

    /********************
     *   Objective Controller
     ********************/

    private double goal;
    private double saved;
    private String ObjectiveName;
    private Date ObjectiveDate;

    public void setObjFormAppearance(){
        myTextField.setText(ObjectiveName);
        LoanTitles.setText(String.valueOf(goal));
        LoanDate.setValue(LocalDate.parse(String.valueOf(ObjectiveDate)));

        myTextField.setEditable(false);
        LoanTitles.setEditable(false);
        LoanDate.setEditable(false);

        MyDebtSubmit.setDisable(true);
        LoanRatio.setText(saved + "/" + goal);

        LoanProgress.setProgress(saved/goal);
    }

    public void CompleteObjective()
    {
        if ((goal - saved) < 0.00001)
        {
            MyDebtButton.setText("Complete");
            MyDebtButton.setDisable(true);
        }
    }

    public void addToObjective(){
        if (PaidAmount.getText().isEmpty()) return;
        if (!PaidAmount.getText().matches("^\\d+$|^\\d+\\.\\d+$"))
        {
            PaidAmount.setText("");
            return;
        }
        //change the data on the Debt table
        double AmountToPaid = Double.parseDouble(PaidAmount.getText());
        PaidAmount.setText("");
        if (saved + AmountToPaid > goal) AmountToPaid = goal - saved;

        String updateData = "UPDATE objective SET saved = ? WHERE No = ? AND username = ?";

        connect = Database.connectDB();

        try{
            assert connect != null;
            prepare = connect.prepareStatement(updateData);

            saved = saved + AmountToPaid;

            prepare.setDouble(1, saved);
            prepare.setInt(2,Key);
            prepare.setString(3, userData.getUsername());

            prepare.executeUpdate();

            LoanRatio.setText(saved + "/" + goal);
            LoanProgress.setProgress(saved/goal);

        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        CompleteObjective();

        // add new expense data to the expense
        String Sql = "INSERT INTO expense (Purpose, Category, Amount, Date, Description, username) VALUES (?,?,?,?,?,?)";

        connect = Database.connectDB();
        try{
            assert connect != null;
            prepare = connect.prepareStatement(Sql);

            prepare.setString(1,"Add to Objective");
            prepare.setString(2,"Accumulated Expense +");
            prepare.setDouble(3, AmountToPaid);
            prepare.setString(4, String.valueOf(LocalDate.now()));
            prepare.setString(5, "Add to Objective named " + ObjectiveName);
            prepare.setString(6, userData.getUsername());

            prepare.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace(System.out);
        }
    }

    public boolean DeleteObjectiveFromDatabase(){
        String deleteData = "DELETE FROM objective WHERE No = "
                + Key + " AND username = '"+ userData.getUsername()+"' ";

        connect = Database.connectDB();

        try {
            Alert alert;

            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to DELETE this data ?");

            Optional<ButtonType> option = alert.showAndWait();

            assert option.orElse(null) != null;
            if (option.orElse(null).equals(ButtonType.OK)) {

                statement = connect.createStatement();
                statement.executeUpdate(deleteData);

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Deleted!");
                alert.showAndWait();
            } else return false;
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return true;
    }

    public void setObjControllerAttribute (int Key, double goal,
                                           double saved, String ObjectiveName,
                                           Date ObjectiveDate){
        this.Key = Key;
        this.goal = goal;
        this.saved = saved;
        this.ObjectiveName = ObjectiveName;
        this.ObjectiveDate = ObjectiveDate;

        MyDebtButton.setVisible(true);
        MyDebtSubmit.setVisible(false);

        isSubmit = true;
        CompleteObjective();
        setObjFormAppearance();
    }

    public void SubmitObjective(){
        String sql = "INSERT INTO objective (username, Name, goal, date, saved)" +
                " VALUES (?,?,?,?,0)";

        connect = Database.connectDB();

        try{
            Alert alert;
            if (myTextField.getText().isEmpty()
                    || !LoanTitles.getText().matches("\\d+(\\.\\d+)?")
                    || LoanDate.getValue() == null
                    || LoanTitles.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message!");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields with proper input");
                alert.showAndWait();
            } else {
                prepare = connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                prepare.setString(1, userData.getUsername());
                prepare.setString(2, myTextField.getText());
                prepare.setString(3, LoanTitles.getText());
                prepare.setString(4, String.valueOf(LoanDate.getValue()));

                int rowAffected = prepare.executeUpdate();
                if (rowAffected > 0){
                    result = prepare.getGeneratedKeys();
                    if (result.next()) {
                        Key = result.getInt(1);

                        LoanRatio.setText("0.0/" + Double.parseDouble(LoanTitles.getText()));

                        //init attribute
                        goal = Double.parseDouble(LoanTitles.getText());
                        ObjectiveName = myTextField.getText();
                        ObjectiveDate = Date.valueOf(LoanDate.getValue());
                        saved = 0;

                        LoanTitles.setText(String.valueOf(Double.parseDouble(LoanTitles.getText())));
                        MyDebtSubmit.setVisible(false);
                        myTextField.setEditable(false);
                        LoanTitles.setEditable(false);
                        LoanDate.setEditable(false);

                        MyDebtSubmit.setVisible(false);
                        MyDebtButton.setVisible(true);
                        isSubmit = true;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
    }

    /******************
     *  Debt Controller
    ******************/
    //Debt Element
    @FXML
    private Button MyDebtButton, MyDebtSubmit;

    @FXML
    private TextField myTextField;

    @FXML
    private TextField LoanInterest;

    @FXML
    private TextField PaidAmount;

    @FXML
    private ProgressBar LoanProgress;

    @FXML
    private Label LoanRatio;

    @FXML
    private DatePicker LoanDate;

    @FXML
    private TextField LoanTitles;

    private double principalAmount;
    private double currentDebt;
    private double loanInterest;
    private double paidAmount;
    private Date latestupdate;
    private Date firstIncludeDate;
    private String name;

    public void UpdateDebtData(){
        LocalDate today = LocalDate.now();

        LocalDate latestUpdateDate = latestupdate.toLocalDate();

        double escalatedDebtAmount = currentDebt - paidAmount;

        while (latestUpdateDate.isBefore(today))
        {
            latestUpdateDate = latestUpdateDate.plusMonths(1);
            if (latestUpdateDate.isAfter(today)) break;
            escalatedDebtAmount = (escalatedDebtAmount) * (1.0 + loanInterest/100.0);
        }
        if (latestUpdateDate.isAfter(today)) latestUpdateDate = latestUpdateDate.minusMonths(1);
        escalatedDebtAmount += paidAmount;


        if (currentDebt != escalatedDebtAmount) {
            currentDebt = escalatedDebtAmount;
            latestupdate = Date.valueOf(latestUpdateDate);

            String sql = "UPDATE debt SET currentdebt = " + currentDebt
                    +" , latestupdate = '" +latestupdate + "' " +
                    "WHERE NO = " + Key;

            connect = Database.connectDB();

            try{
                assert connect != null;
                prepare = connect.prepareStatement(sql);

                prepare.executeUpdate();
            }catch (Exception e){
                e.printStackTrace(System.out);
            }
        }
    }

    public void setDebtFormAppearance(){

        //set value of each panel
        myTextField.setText(String.valueOf(principalAmount));
        LoanInterest.setText(String.valueOf(loanInterest));
        LoanTitles.setText(name);
        LoanDate.setValue(firstIncludeDate.toLocalDate());

        //set appearance when the data is already in database
        myTextField.setEditable(false);
        LoanInterest.setEditable(false);
        MyDebtSubmit.setVisible(false);
        LoanTitles.setEditable(false);
        LoanDate.setEditable(false);

        //Show current progress
        LoanRatio.setText(paidAmount + "/" + currentDebt);
        LoanProgress.setProgress(paidAmount/currentDebt);
    }

    public void setControllerAttribute (int Key,
                                        double principalAmount,
                                        double currentDebt,
                                        double loanInterest,
                                        double paidAmount,
                                        java.util.Date latestupdate,
                                        Date firstIncludeDate,
                                        String name){
        //Construct and set Controller Attribute
        this.Key = Key;
        this.principalAmount = principalAmount;
        this.currentDebt = currentDebt;
        this.loanInterest = loanInterest;
        this.paidAmount = paidAmount;
        this.latestupdate = (Date) latestupdate;
        this.firstIncludeDate = firstIncludeDate;
        this.name = name;

        //If the Debt is not completely paid
        isSubmit = true;
        MyDebtButton.setVisible(true);
        if(!CompletePaidDebt()) UpdateDebtData();
        setDebtFormAppearance();
    }

    public boolean CompletePaidDebt()
    {
        if ((currentDebt - paidAmount) < 0.00001)
        {
            MyDebtButton.setText("Complete");
            MyDebtButton.setDisable(true);
            return true;
        }
        return false;
    }

    public void Paid(){
        if (PaidAmount.getText().isEmpty()) return;
        if (!PaidAmount.getText().matches("^\\d+$|^\\d+\\.\\d+$"))
        {
            PaidAmount.setText("");
            return;
        }
        //change the data on the Debt table
        double AmountToPaid = Double.parseDouble(PaidAmount.getText());
        PaidAmount.setText("");
        if (paidAmount + AmountToPaid > currentDebt) AmountToPaid = currentDebt - paidAmount;

        String updateData = "UPDATE debt SET paid = ? WHERE No = ? AND username = ?";

        connect = Database.connectDB();

        try{
            assert connect != null;
            prepare = connect.prepareStatement(updateData);

            prepare.setDouble(1, paidAmount + AmountToPaid);
            prepare.setInt(2,Key);
            prepare.setString(3, userData.getUsername());

            prepare.executeUpdate();
            paidAmount = paidAmount + AmountToPaid;

            LoanRatio.setText(paidAmount + "/" + currentDebt);
            LoanProgress.setProgress(paidAmount/currentDebt);
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        CompletePaidDebt();

        String Sql = "INSERT INTO expense (Purpose, Category, Amount, Date, Description, username) VALUES (?,?,?,?,?,?)";

        connect = Database.connectDB();
        try{
            assert connect != null;
            prepare = connect.prepareStatement(Sql);

            prepare.setString(1,"Paid Debt");
            prepare.setString(2,"Accumulated Expense +");
            prepare.setDouble(3, AmountToPaid);
            prepare.setString(4, String.valueOf(LocalDate.now()));
            prepare.setString(5, "Paid to Debt named " + LoanTitles.getText());
            prepare.setString(6, userData.getUsername());

            prepare.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace(System.out);
        }
    }

    public boolean DeleteDebtFromDatabase() {
        String deleteData = "DELETE FROM debt WHERE No = "
                + Key + " AND username = '"+ userData.getUsername()+"' ";

        connect = Database.connectDB();

        try {
            Alert alert;

            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to DELETE this data ?");

            Optional<ButtonType> option = alert.showAndWait();

            assert option.orElse(null) != null;
            if (option.orElse(null).equals(ButtonType.OK)) {

                statement = connect.createStatement();
                statement.executeUpdate(deleteData);

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Deleted!");
                alert.showAndWait();
            } else return false;
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return true;
    }

    public void Submit()
    {
        String sql = "INSERT INTO debt (username,principal, interest, latestupdate, currentdebt, insertdate, name)" +
                " VALUES (?,?,?,?,?,?,?)";

        connect = Database.connectDB();

        try{
            Alert alert;
            if (myTextField.getText().isEmpty()
             || LoanInterest.getText().isEmpty()
             || !myTextField.getText().matches("\\d+(\\.\\d+)?")
             || !LoanInterest.getText().matches("\\d+(\\.\\d+)?")
             || Double.parseDouble(LoanInterest.getText()) >= 100.0
             || LoanDate.getValue() == null
             || LoanTitles.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message!");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields with proper input");
                alert.showAndWait();
            } else {
                prepare = connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                prepare.setString(1, userData.getUsername());
                prepare.setString(2, myTextField.getText());
                prepare.setString(3, LoanInterest.getText());
                prepare.setString(4, String.valueOf(LoanDate.getValue()));
                prepare.setString(5, myTextField.getText());
                prepare.setString(6, String.valueOf(LoanDate.getValue()));
                prepare.setString(7, LoanTitles.getText());

                int rowAffected = prepare.executeUpdate();
                if (rowAffected > 0){
                    result = prepare.getGeneratedKeys();
                    if (result.next()) {
                        Key = result.getInt(1);

                        LoanRatio.setText("0.0/" + Double.parseDouble(myTextField.getText()));

                        //init attribute
                        principalAmount = Double.parseDouble(myTextField.getText());
                        currentDebt = principalAmount;
                        loanInterest = Double.parseDouble(LoanInterest.getText());
                        paidAmount = 0;

                        myTextField.setText(myTextField.getText()+ ".0");
                        LoanInterest.setText(LoanInterest.getText()+ ".0%");
                        MyDebtSubmit.setVisible(false);
                        LoanInterest.setEditable(false);
                        myTextField.setEditable(false);
                        LoanTitles.setEditable(false);
                        LoanDate.setEditable(false);

                        isSubmit = true;
                        MyDebtButton.setVisible(true);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
    }
}
