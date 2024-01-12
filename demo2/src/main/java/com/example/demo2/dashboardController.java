package com.example.demo2;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;

public class dashboardController implements Initializable {

    @FXML
    private TextField Expense_amount;

    @FXML
    private ComboBox<String> Expense_category;

    @FXML
    private TableColumn<ExpenseData, String> Expense_col_amount;

    @FXML
    private TableColumn<ExpenseData, String> Expense_col_category;

    @FXML
    private TableColumn<ExpenseData, String> Expense_col_date;

    @FXML
    private TableColumn<ExpenseData, String> Expense_col_description;

    @FXML
    private TableColumn<ExpenseData, String> Expense_col_purpose;

    @FXML
    private DatePicker Expense_date;

    @FXML
    private TextField Expense_description;

    @FXML
    private AnchorPane Expense_form;

    @FXML
    private TextField Expense_purpose;

    @FXML
    private TextField Expense_search;

    @FXML
    private TableView<ExpenseData> Expense_table;

    @FXML
    private Label Home_balance;

    @FXML
    private Label Home_bills;

    @FXML
    private BarChart<String, Number> Home_expense;

    @FXML
    private Label Home_fooddrink;

    @FXML
    private AnchorPane Home_form;

    @FXML
    private LineChart<String, Number> Home_income;

    @FXML
    private Label Home_others;

    @FXML
    private PieChart Home_piechart;

    @FXML
    private Label Home_shopping;

    @FXML
    private TextField Income_amount;

    @FXML
    private TextField Income_category;

    @FXML
    private TableColumn<IncomeData, String> Income_col_amount;

    @FXML
    private TableColumn<IncomeData, String> Income_col_category;

    @FXML
    private TableColumn<IncomeData, String> Income_col_date;

    @FXML
    private TableColumn<IncomeData, String> Income_col_source;

    @FXML
    private TableColumn<IncomeData, String> Income_col_no;

    @FXML
    private DatePicker Income_date;

    @FXML
    private AnchorPane Income_form;

    @FXML
    private TextField Income_search;

    @FXML
    private TextField Income_source;

    @FXML
    private TableView<IncomeData> Income_table;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button Expense_btn;

    @FXML
    private Button Home_btn;

    @FXML
    private Button Income_btn;

    @FXML
    private Button Indebt_btn;

    @FXML
    private AnchorPane Indebt_form;

    @FXML
    private Button setting_btn;

    @FXML
    private Button Logout_btn;

    @FXML
    private AnchorPane setting_form;

    @FXML
    private PasswordField password_field_password;

    @FXML
    private PasswordField password_field_reenter;

    @FXML
    private CheckBox SettingForm_PassWordHiddencheckbox;

    @FXML
    private TextField text_field_password;

    @FXML
    private TextField text_field_reenter;

    @FXML
    private ComboBox<String> Expense_Choose_Col;

    @FXML
    private ComboBox<String> Expense_sorttype;

    @FXML
    private ComboBox<String> Income_Choose_Col;

    @FXML
    private ComboBox<String> Income_sorttype;

    @FXML
    private ComboBox<String> Setting_currency;

    @FXML
    private HBox H_shopping;

    @FXML
    private HBox H_fooddrink;

    @FXML
    private HBox H_bills;

    @FXML
    private HBox H_others;

    @FXML
    private HBox H_balance;

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    public static CurrentUserData CurrentSessionUserData = FxmlController.userData;

    /****************
     * Indebt Form
     ****************/
    @FXML
    private CheckBox SavingCheckbox;

    @FXML
    private AnchorPane addAnchor;

    @FXML
    private AnchorPane IncreasingSizeAnchor;

    @FXML
    private CheckBox DebtCheckbox, ObjectiveCheckbox;

    //attribute
    private int counting = 0;

    //Temp value and obj
    private AnchorPane SelectedAnchorPane;
    private InDebtController SelectedInDebtController;
    private int IsTheSelectedAnchorMoved = 0;
    private boolean IsInitEvent = false;
    private boolean hasEmptyAnchor = false;

    public dashboardController() {
    }

    public void setDebtCheckboxAction(){
        SavingCheckbox.setSelected(false);
        ObjectiveCheckbox.setSelected(false);
    }

    public void setObjectiveCheckboxAction(){
        SavingCheckbox.setSelected(false);
        DebtCheckbox.setSelected(false);
    }

    public void setSavingCheckboxAction(){
        ObjectiveCheckbox.setSelected(false);
        DebtCheckbox.setSelected(false);
    }

    public void addSavingOnInit(){
        IsInitEvent = true;

        String sql = "SELECT * FROM saving WHERE username = '" + CurrentSessionUserData.getUsername() + "'";

        connect = Database.connectDB();

        try {
            assert connect != null;
            prepare = connect.prepareStatement(sql);

            result = prepare.executeQuery();

            SavingCheckbox.setSelected(true);
            ObjectiveCheckbox.setSelected(false);
            DebtCheckbox.setSelected(false);

            while (result.next()){
                addEventManually();

                int No = result.getInt(1);
                String name = result.getString(2);
                double principal = result.getDouble(7);
                Date startDate = result.getDate(4);
                Date endDate = result.getDate(5);
                double amount =  result.getDouble(8);
                double interest = result.getDouble(3);

                SelectedInDebtController.setSavingControllerAttribute(No,principal,name, interest, amount, startDate, endDate);
            }

            SavingCheckbox.setSelected(false);
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
    }

    public void addObjectiveOnInit(){
        IsInitEvent = true;

        String sql = "SELECT * FROM objective WHERE username = '" + CurrentSessionUserData.getUsername() + "'";

        connect = Database.connectDB();

        try {
            assert connect != null;
            prepare = connect.prepareStatement(sql);

            result = prepare.executeQuery();

            ObjectiveCheckbox.setSelected(true);

            while (result.next()){
                addEventManually();

                int No = result.getInt(1);
                String name = result.getString(2);
                double goal = result.getDouble(3);
                java.sql.Date date = result.getDate(4);
                double saved = result.getDouble(5);

                SelectedInDebtController.setObjControllerAttribute(No,goal,saved, name, date);
            }
            ObjectiveCheckbox.setSelected(false);
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
    }

    public void addEventOnInit() {
        IsInitEvent = true;

        String sql = "SELECT NO, principal, interest, paid, latestupdate, currentdebt, insertdate, name" +
                " FROM debt WHERE username = '" + CurrentSessionUserData.getUsername() + "'";

        connect = Database.connectDB();

        try{
            assert connect != null;
            prepare = connect.prepareStatement(sql);

            result = prepare.executeQuery();

            DebtCheckbox.setSelected(true);
            while (result.next()){

                addEventManually();
                int No = result.getInt(1);
                double principal = result.getDouble(2);
                double interest = result.getDouble(3);
                double paid = result.getDouble(4);
                Date LatestUpdate = result.getDate(5);
                double currentDebt = result.getDouble(6);
                java.sql.Date insertDate = result.getDate(7);
                String name = result.getString(8);

                //create new panel depend on saved data
                SelectedInDebtController.setControllerAttribute(No, principal, currentDebt ,interest, paid, LatestUpdate,insertDate,name);
            }
            DebtCheckbox.setSelected(false);
        }catch (Exception e){
            e.printStackTrace(System.out);
        }

        IsInitEvent = false;
        SelectedInDebtController = null;
        SelectedAnchorPane = null;
    }

    public void addEventManually() throws IOException {
        if (hasEmptyAnchor)
            return;
        if (!ObjectiveCheckbox.isSelected() && !DebtCheckbox.isSelected() && !SavingCheckbox.isSelected()) return;

        //add new components
        FXMLLoader fxmlLoader;
        AnchorPane addNewDebtPane = new AnchorPane();
        AnchorPane finalN;
        InDebtController CurrentIndebtController;
        int Type;

        //Create new Pane and load layout
        if (DebtCheckbox.isSelected()) {
            fxmlLoader = new FXMLLoader(getClass().getResource("addIndept.fxml"));
            Type = 1;
        } else if (ObjectiveCheckbox.isSelected()){
            fxmlLoader = new FXMLLoader(getClass().getResource("addObjective.fxml"));
            Type = 2;
        }else {
            fxmlLoader = new FXMLLoader(getClass().getResource("addSaving.fxml"));
            Type = 3;
        }

        fxmlLoader.setRoot(addNewDebtPane);
        addNewDebtPane = fxmlLoader.load();
        finalN = addNewDebtPane; //create tmp anchor
        CurrentIndebtController = fxmlLoader.getController();//load controller
        CurrentIndebtController.setTypeOfDebtForm(Type);

        //Locate the new created anchor in the grand scheme
        addNewDebtPane.setPrefWidth(736);
        AnchorPane.setTopAnchor(addNewDebtPane,25.0+ counting*153.0);
        AnchorPane.setLeftAnchor(addNewDebtPane, 36.0);

        //Set Current Choosing Pane
        addNewDebtPane.setOnMouseClicked(mouseEvent -> {

            //If SelectedPane is selected
            if (SelectedAnchorPane != null)
            {
                //Turn off highlighted anchor
                SelectedAnchorPane.setStyle("-fx-border-color: transparent");
                if (SelectedAnchorPane != finalN) {
                    AnchorPane.setTopAnchor(SelectedAnchorPane, SelectedAnchorPane.getLayoutY() + 10);
                    AnchorPane.setLeftAnchor(SelectedAnchorPane, SelectedAnchorPane.getLayoutX() + 5);
                }
                IsTheSelectedAnchorMoved = 0;
                SelectedAnchorPane.setPrefWidth(736);
                SelectedAnchorPane.setPrefHeight(128);
            }

            //If the SelectedPane is currently select
            if (SelectedAnchorPane == finalN){
                SelectedAnchorPane.setStyle("-fx-border-color:linear-gradient(to bottom right, #2d658c, #2ca772)");
                SelectedAnchorPane.setPrefWidth(756);
                SelectedAnchorPane.setPrefHeight(138);
                return;
            }

            //Select the clicked pane
            SelectedAnchorPane = finalN;
            SelectedInDebtController = CurrentIndebtController;
            SelectedAnchorPane.setStyle("-fx-border-color:linear-gradient(to bottom right, #2d658c, #2ca772)");
            if (IsTheSelectedAnchorMoved == 0) {
                AnchorPane.setTopAnchor(SelectedAnchorPane, SelectedAnchorPane.getLayoutY() - 10);
                AnchorPane.setLeftAnchor(SelectedAnchorPane, SelectedAnchorPane.getLayoutX() - 5);
                IsTheSelectedAnchorMoved = 1;
            }
            SelectedAnchorPane.setPrefWidth(756);
            SelectedAnchorPane.setPrefHeight(138);
        });

        //adding some hover effect from css
        addNewDebtPane.setOnMouseEntered(MouseEvent -> finalN.setStyle("-fx-border-color:linear-gradient(to bottom right, #2d658c, #2ca772)"));
        addNewDebtPane.setOnMouseExited(MouseEvent ->{
            if (SelectedAnchorPane == finalN) return;
            finalN.setStyle("-fx-border-color:transparent");
        });

        //Add the new Pane to the ScrollingPane
        IncreasingSizeAnchor.getChildren().add(addNewDebtPane);
        AnchorPane.setTopAnchor(addAnchor, 25.0 + (counting + 1) * 153);

        if (IsInitEvent)
        {
            SelectedAnchorPane = addAnchor;
            SelectedInDebtController = CurrentIndebtController;
        }

        //resizing grand anchor
        counting ++;
        IncreasingSizeAnchor.setPrefHeight(counting * 153 + 203);
    }

    public void DeleteAllAnchor(){
        ObservableList<javafx.scene.Node> children = IncreasingSizeAnchor.getChildren();

        int size = children.size() - 1;

        children.removeIf(node -> node != addAnchor);
        counting -= size;

        AnchorPane.setTopAnchor(addAnchor, 25.0);

    }
    
    public void deleteExistedAnchor(){
        //init selected Anchor Attribute
        if (SelectedInDebtController.getIsSubmit()) {
            if (SelectedInDebtController.getTypeOfDebtForm() == 1) {
                if (!SelectedInDebtController.DeleteDebtFromDatabase()) return;
            }else if (SelectedInDebtController.getTypeOfDebtForm() == 2){
                if (!SelectedInDebtController.DeleteObjectiveFromDatabase()) return;
            }else {
                if (!SelectedInDebtController.DeleteSavingFromDatabase()) return;
            }
        }

        int currentindex = IncreasingSizeAnchor.getChildren().indexOf(SelectedAnchorPane) + 1;
        int AnchorsSize = IncreasingSizeAnchor.getChildren().size() ;

        //if no Anchor is Selected or there is no Anchor, return;
        if (SelectedAnchorPane == null) return;
        if (AnchorsSize < 2) return;

        //Relocated the anchor after the current anchor
        while (currentindex < AnchorsSize){
            AnchorPane CurrentAnchor = (AnchorPane) IncreasingSizeAnchor.getChildren().get(currentindex);
            AnchorPane.setTopAnchor(CurrentAnchor, CurrentAnchor.getLayoutY() - 153);
            currentindex++;
        }

        //Relocated the Add Anchor
        AnchorPane.setTopAnchor(addAnchor, addAnchor.getLayoutY() - 153);

        //remove selected anchor and clear the data
        IncreasingSizeAnchor.getChildren().remove(SelectedAnchorPane);

        SelectedAnchorPane = null;
        SelectedInDebtController = null;
        IsTheSelectedAnchorMoved = 0;

        //Resizing Grand Anchor
        counting--;
        IncreasingSizeAnchor.setPrefHeight(counting * 153 + 203);
    }

    /******************
     * Home FORM
     *******************/

    @FXML
    private Label Home_expensePeriod;

    @FXML
    private ComboBox<String> Home_barChart_ChooseGroup;

    @FXML
    private ComboBox<String> Home_barChart_baseOn;

    @FXML
    private ComboBox<String> Home_barChart_sorttype;

    @FXML
    private DatePicker Home_pieChartStartDatePicker, Home_pieChartEndDatePicker;

    @FXML
    private DatePicker Home_pieChartDatePicker;

    @FXML
    private ComboBox<String> Home_pieChartMYPicker;

    @FXML
    private ComboBox<String> Home_PieChart_TimeRange;

    public void ChooseTimeRangePicker(){
        if (Home_pieChartEndDatePicker.getValue() != null &&
           Home_pieChartEndDatePicker.getValue() != null) {

            Home_piechart.getData().clear();

            String sql = "SELECT sum(Amount), Category FROM expense WHERE Date >= '" +
                    Home_pieChartStartDatePicker.getValue() + "' AND Date <= '" +
                    Home_pieChartEndDatePicker.getValue() + "' GROUP BY Category";

            connect = Database.connectDB();

            if (Home_pieChartStartDatePicker.getValue().isAfter(Home_pieChartEndDatePicker.getValue())){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please choose the start date before the end date");
                alert.showAndWait();

                Home_pieChartStartDatePicker.setValue(null);
                Home_pieChartEndDatePicker.setValue(null);
            }
            else {
                try {
                    assert connect != null;
                    prepare = connect.prepareStatement(sql);
                    result = prepare.executeQuery();

                    List<PieChart.Data> arrayL = new ArrayList<>();

                    while (result.next()) {
                        arrayL.add(new PieChart.Data(result.getString(2), result.getDouble(1)));
                    }

                    ObservableList<PieChart.Data> piechartData = FXCollections.observableArrayList(arrayL);

                    Home_piechart.setData(piechartData);

                } catch (SQLException e) {
                    e.printStackTrace(System.out);
                }
            }
        }
    }

    public void addHomeTimeRangeCategoryList()
    {
        List<String> arrayL = new ArrayList<>();
        arrayL.add("Day");
        arrayL.add("Month");
        arrayL.add("Year");
        arrayL.add("From date to date");
        ObservableList<String> ObList = FXCollections.observableArrayList(arrayL);
        Home_PieChart_TimeRange.setItems(ObList);
        HomeComboSetOnAction();
    }

    public void HomeComboSetOnAction()
    {
        Home_PieChart_TimeRange.setOnAction(e -> handleHomeChooseTimeRangeOption(Home_PieChart_TimeRange.getValue()));
    }

    public void ChooseMYPicker(String selectedOption)
    {
        String sql;
        if (selectedOption.equals("Month")){
            sql = "SELECT DISTINCT DATE_FORMAT(Date, '%Y-%m') FROM expense";
        }else {
            sql = "SELECT DISTINCT DATE_FORMAT(Date, '%Y') FROM expense";
        }
        connect = Database.connectDB();
        try{
            assert connect != null;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            List<String> arrayL = new ArrayList<>();
            while(result.next()){
                String now = result.getString(1);
                arrayL.add(now);
            }

            ObservableList<String> ObList = FXCollections.observableArrayList(arrayL);
            Home_pieChartMYPicker.setItems(ObList);
        }catch(Exception e){
            e.printStackTrace(System.out);
        }
    }

    public void handleHomeChooseTimeRangeOption (String selectedOption){
        if (selectedOption.equals("Day"))
        {
            Home_pieChartMYPicker.setVisible(false);
            Home_pieChartDatePicker.setVisible(true);
            Home_pieChartEndDatePicker.setVisible(false);
            Home_pieChartStartDatePicker.setVisible(false);

            Home_pieChartDatePicker.setValue(null);
        } else if (selectedOption.equals("From date to date")){
            Home_pieChartMYPicker.setVisible(false);
            Home_pieChartDatePicker.setVisible(false);
            Home_pieChartEndDatePicker.setVisible(true);
            Home_pieChartStartDatePicker.setVisible(true);

            Home_pieChartEndDatePicker.setValue(null);
            Home_pieChartStartDatePicker.setValue(null);
        }
        else {
            Home_pieChartMYPicker.setVisible(true);
            Home_pieChartDatePicker.setVisible(false);
            Home_pieChartEndDatePicker.setVisible(false);
            Home_pieChartStartDatePicker.setVisible(false);
            ChooseMYPicker(selectedOption);
        }
    }

    public void HomeDisplayTotalExpensePieChart(){
        Home_piechart.getData().clear();

        String selectedOption = Home_PieChart_TimeRange.getValue();

        String sql;
        String replaceSql = "", replaceParameter = "";
        switch (selectedOption){
            case "Day" -> {
                replaceParameter = String.valueOf(Home_pieChartDatePicker.getValue());
                replaceSql = "Date";
            }
            case "Month" -> {
                replaceParameter = String.valueOf(Home_pieChartMYPicker.getValue());
                replaceSql = "DATE_FORMAT(Date, '%Y-%m')";
            }
            case "Year" -> {
                replaceParameter = String.valueOf(Home_pieChartMYPicker.getValue());
                replaceSql = "DATE_FORMAT(Date, '%Y')";
            }
        }
        connect = Database.connectDB();
        sql = "SELECT sum(Amount), Category FROM expense WHERE "
                + replaceSql + " = '"
                + replaceParameter + "' GROUP BY Category";
        try{
            assert connect != null;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            List<PieChart.Data> arrayL = new ArrayList<>();

            float count = 0;

            while(result.next())
            {
                arrayL.add(new PieChart.Data(result.getString(2),result.getDouble(1)));
                count += (float)result.getDouble(1);
            }

            ObservableList<PieChart.Data> piechartData = FXCollections.observableArrayList(arrayL);

            Home_piechart.setData(piechartData);
            Home_expensePeriod.setText(count + " " + CurrentSessionUserData.getCurrency().getType());
        }catch(Exception e){
            e.printStackTrace(System.out);
        }
    }

    public void setHighlightedCellDatePicker()
    {
        Home_piechartAnchor.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        List<LocalDate> DateList = new ArrayList<>();
        LocalDate date;
        Calendar calendar = Calendar.getInstance();
        int year,month,day;
        for (ExpenseData e: addExpenseListD)
        {
            calendar.setTime(e.getDate());
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-based
            day = calendar.get(Calendar.DAY_OF_MONTH);
            date = LocalDate.of(year, month, day);
            DateList.add(date);
        }

        Home_pieChartDatePicker.setDayCellFactory(new Callback<>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        // Highlight the desired cell
                        if (item != null && DateList.contains(item)) {
                            getStyleClass().add("highlighted-cell");
                        }else getStyleClass().add("others-cell");
                    }
                };
            }
        });

        Home_pieChartStartDatePicker.setDayCellFactory(new Callback<>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        // Highlight the desired cell
                        if (item != null && DateList.contains(item)) {
                            getStyleClass().add("highlighted-cell");
                        } else getStyleClass().add("others-cell");
                    }
                };
            }
        });

        Home_pieChartEndDatePicker.setDayCellFactory(new Callback<>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        // Highlight the desired cell
                        if (item != null && DateList.contains(item)) {
                            getStyleClass().add("highlighted-cell");
                        }else getStyleClass().add("others-cell");
                    }
                };
            }
        });
    }

    @FXML
    private AnchorPane Home_piechartAnchor, Home_barChartAnchor;

    public void testTranslation()
    {
        if (Home_barChartAnchor.isVisible()) {
            Home_piechartAnchor.setVisible(true);
            Home_barChartAnchor.setVisible(false);
            TranslateTransition slideIn = new TranslateTransition(Duration.seconds(1), Home_piechartAnchor);
            slideIn.setFromX(836);
            slideIn.setToX(0);
            slideIn.play();
        } else{
            Home_barChartAnchor.setVisible(true);
            Home_piechartAnchor.setVisible(false);
            TranslateTransition slideIn = new TranslateTransition(Duration.seconds(1), Home_barChartAnchor);
            slideIn.setFromX(836);
            slideIn.setToX(0);
            slideIn.play();
        }
    }

    public void HomeChooseSortType(String s){
        List<String> arrayL = new ArrayList<>();

        if (s == null){
            Home_barChart_sorttype.getItems().clear();
        }else if (s.equals("Amount Spent")) {
            arrayL.add("Big to small");
            arrayL.add("Small to big");
        } else {
            arrayL.add("Latest to furthest");
            arrayL.add("Furthest to latest");
        }

        ObservableList<String> ObList = FXCollections.observableArrayList(arrayL);
        Home_barChart_sorttype.setItems(ObList);
    }

    public void ChooseBaseOn(String i){
        List<String> arrayL = new ArrayList<>();
        Home_barChart_baseOn.getItems().clear();

        arrayL.add("Amount Spent");
        arrayL.add(i);

        ObservableList<String> ObList = FXCollections.observableArrayList(arrayL);
        Home_barChart_baseOn.setItems(ObList);
    }

    public void ChooseGroupByOption()
    {
        List<String> arrayL = new ArrayList<>();

        arrayL.add("Day");
        arrayL.add("Month");
        arrayL.add("Year");

        ObservableList <String> ObList = FXCollections.observableArrayList(arrayL);
        Home_barChart_ChooseGroup.setItems(ObList);
        BarChartOptionSetOnAction();
    }

    public void BarChartOptionSetOnAction()
    {
        Home_barChart_ChooseGroup.setOnAction(e -> handleHomeChooseGroupSelection(Home_barChart_ChooseGroup.getValue()));
        Home_barChart_baseOn.setOnAction(e -> handleHomeChooseBaseOnSelection(Home_barChart_baseOn.getValue()));
        Home_barChart_sorttype.setOnAction(e -> handleHomeChooseSortTypeSelection(Home_barChart_sorttype.getValue()));
    }

    public void handleHomeChooseBaseOnSelection(String selectedOption)
    {
        HomeChooseSortType(selectedOption);
    }

    private void handleHomeChooseGroupSelection(String selectedOption) {
        ChooseBaseOn(selectedOption);}

    private void handleHomeChooseSortTypeSelection(String selectedOption){
        HomeDisplayTotalExpenseChart(Home_barChart_ChooseGroup.getValue(), Home_barChart_baseOn.getValue(),selectedOption);
        HomeDisplayTotalIncomeChart(Home_barChart_ChooseGroup.getValue(), Home_barChart_baseOn.getValue(),selectedOption);
    }

    private int Data_located_page = 1, maxExpenseData = 0;
    private int Data_located_pagei = 1, maxIncomeData = 0;

    @FXML
    Button ChartLeft, ChartRight;

    @FXML
    private Label Home_FilterLabel;

    public void EnableHomeFilterCheck() {
        Home_Filter.setVisible(Home_EnableFilter.isSelected());
        Home_FilterLabel.setVisible(Home_EnableFilter.isSelected());
    }

    public void NavigateLeft ()
    {
        if (Data_located_page > 1)
        {
            Data_located_page --;
            HomeDisplayTotalExpenseChart(Home_barChart_ChooseGroup.getValue(), Home_barChart_baseOn.getValue(),Home_barChart_sorttype.getValue());
        }
        if (Data_located_pagei > 1) {
            Data_located_pagei --;
            HomeDisplayTotalIncomeChart(Home_barChart_ChooseGroup.getValue(), Home_barChart_baseOn.getValue(), Home_barChart_sorttype.getValue());
        }
    }

    public void NavigateRight ()
    {
        if (Data_located_page < maxExpenseData)
        {
            Data_located_page ++;
            HomeDisplayTotalExpenseChart(Home_barChart_ChooseGroup.getValue(), Home_barChart_baseOn.getValue(),Home_barChart_sorttype.getValue());
        }
        if (Data_located_pagei < maxIncomeData) {
            Data_located_pagei ++;
            HomeDisplayTotalIncomeChart(Home_barChart_ChooseGroup.getValue(), Home_barChart_baseOn.getValue(), Home_barChart_sorttype.getValue());
        }
    }

    public void DatePickerOption()
    {
        if (Home_barChart_ChooseGroup == null || Home_barChart_baseOn == null || Home_barChart_sorttype == null)
            return;
        HomeDisplayTotalExpenseChart(Home_barChart_ChooseGroup.getValue(), Home_barChart_baseOn.getValue(),Home_barChart_sorttype.getValue());
        HomeDisplayTotalIncomeChart(Home_barChart_ChooseGroup.getValue(), Home_barChart_baseOn.getValue(),Home_barChart_sorttype.getValue());
    }

    @FXML
    private CheckBox Home_EnableFilter;

    @FXML
    private DatePicker Home_Filter;

    public void HomeDisplayTotalExpenseChart(String GroupBy, String BaseOn, String SortType){

        maxExpenseData = 0;
        Home_expense.getData().clear();

        Home_expense.setAnimated(false);

        String sql;

        connect = Database.connectDB();

        try{

            XYChart.Series<String,Number> chart = new XYChart.Series<>();

            String filter = "";
            String loc1 = "", loc2 = "", loc3 = "";

            switch (GroupBy){
                case "Day" -> loc1 = "Date";
                case "Month" -> loc1 = "DATE_FORMAT(Date, '%Y-%m')";
                case "Year" -> loc1 = "DATE_FORMAT(Date, '%Y')";
            }

            switch (BaseOn){
                case "Day" -> loc2 = "Date";
                case "Month" -> loc2 = "DATE_FORMAT(Date, '%Y-%m')";
                case "Year" -> loc2 = "DATE_FORMAT(Date, '%Y')";
                case "Amount Spent" -> loc2 = "sum(Amount)";
            }

            switch (SortType){
                case "Big to small", "Latest to furthest" -> loc3 = "DESC";
                case "Small to big", "Furthest to Latest" -> loc3 = "ASC";
            }
            if (Home_EnableFilter.isSelected())
            {
                if(Home_Filter.getValue() != null)
                {
                    filter = "AND Date > '" + Home_Filter.getValue() + "' ";
                }
            }
            sql = "SELECT " + loc1 + ", sum(Amount) "
                    + "From expense WHERE username = '"+ CurrentSessionUserData.getUsername() +"' " + filter
                    + "Group by " + loc1
                    + " Order by "+ loc2 + " " + loc3;

            prepare = connect.prepareStatement(sql);
            
            result = prepare.executeQuery();

            int counter = 0;
            while(result.next()){
                if (counter % 5 == 0) maxExpenseData++;
                if (counter >= (Data_located_page-1) * 5 && counter < Data_located_page*5)
                chart.getData().add(new XYChart.Data<>(result.getString(1), result.getInt(2)));
                else counter+=0;
                counter++;
            }

            Home_expense.getData().add(chart);

        } catch (Exception ignore){
        }
    }

    public void HomeDisplayTotalIncomeChart(String GroupBy, String BaseOn, String SortType){
        maxIncomeData = 0;

        Home_income.getData().clear();

        Home_income.setAnimated(false);

        String sql;

        connect = Database.connectDB();

        try{
            XYChart.Series<String,Number> chart = new XYChart.Series<>();

            String filter = "";
            String loc1 = "", loc2 = "", loc3 = "";

            switch (GroupBy){
                case "Day" -> loc1 = "Date";
                case "Month" -> loc1 = "DATE_FORMAT(Date, '%Y-%m')";
                case "Year" -> loc1 = "DATE_FORMAT(Date, '%Y')";
            }

            switch (BaseOn){
                case "Day" -> loc2 = "Date";
                case "Month" -> loc2 = "DATE_FORMAT(Date, '%Y-%m')";
                case "Year" -> loc2 = "DATE_FORMAT(Date, '%Y')";
                case "Amount Spent" -> loc2 = "sum(Amount)";
            }

            switch (SortType){
                case "Big to small", "Latest to furthest" -> loc3 = "DESC";
                case "Small to big", "Furthest to Latest" -> loc3 = "ASC";
            }

            if (Home_EnableFilter.isSelected())
            {
                if(Home_Filter.getValue() != null)
                {
                    filter = "AND Date > '" + Home_Filter.getValue() + "' ";
                }
            }
            sql = "SELECT " + loc1 + ", sum(Amount) "
                    + "From income WHERE username = '"+ CurrentSessionUserData.getUsername() +"' " + filter
                    + "Group by " + loc1
                    + " Order by "+ loc2 + " " + loc3;

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            int counter = 0;
            while(result.next()){
                if (counter % 5 == 0) maxExpenseData++;
                if (counter >= (Data_located_pagei-1) * 5 && counter < Data_located_pagei * 5)
                    chart.getData().add(new XYChart.Data<>(result.getString(1), result.getInt(2)));
                else counter+=0;
                counter++;
            }

            Home_income.getData().add(chart);

        } catch (Exception e ){
            e.printStackTrace(System.out);
        }
    }

    public void HomeDisplayShoppingExpense(){
        String sql = "SELECT sum(Amount) FROM expense WHERE username = '" + CurrentSessionUserData.getUsername() + "'"
                + "AND Category = 'Living Expense'";

        connect = Database.connectDB();

        double count = 0;

        try{
            assert connect != null;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()){
                count = result.getDouble("sum(Amount)");
            }
            Home_shopping.setText(String.valueOf(Math.round( count)));
            if (Math.abs(count) > 10000){
                Home_shopping.setFont(Font.font("Tahoma", FontWeight.BOLD,17));
            } else {
                Home_shopping.setFont(Font.font("Tahoma", FontWeight.BOLD,23));
            }
        }catch (Exception e){
            e.printStackTrace(System.out);
        }

    }

    public void HomeDisplayEntertainmentExpense(){
        String sql ="SELECT sum(Amount) FROM expense WHERE username = '"
                + CurrentSessionUserData.getUsername() + "'"
                + "AND Category = 'Entertainment Expense'";

        connect = Database.connectDB();

        double num = 0;

        try {
            assert connect != null;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next())
            {
                num = result.getDouble("sum(Amount)");
            }
            Home_fooddrink.setText(String.valueOf(Math.round( num)));
            if (Math.abs(num) > 10000){
                Home_fooddrink.setFont(Font.font("Tahoma", FontWeight.BOLD,17));
            } else {
                Home_fooddrink.setFont(Font.font("Tahoma", FontWeight.BOLD,23));
            }
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
    }

    public void HomeDisplayBills(){
        String sql ="SELECT sum(Amount) FROM expense WHERE username = '" + CurrentSessionUserData.getUsername() + "'"
                + "AND Category = 'Capital Expenditure'";

        connect = Database.connectDB();

        double num = 0;

        try {
            assert connect != null;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next())
            {
                num = result.getDouble("sum(Amount)");
            }
            Home_bills.setText(String.valueOf(Math.round( num)));
            if (Math.abs(num) > 10000){
                Home_bills.setFont(Font.font("Tahoma", FontWeight.BOLD,17));
            } else {
                Home_bills.setFont(Font.font("Tahoma", FontWeight.BOLD,23));
            }
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
    }

    public void HomeDisplayOthers(){
        String sql ="SELECT sum(Amount) FROM expense WHERE username = '" + CurrentSessionUserData.getUsername() + "'"
                + "AND (Category = 'Accumulated Expense' OR Category = 'Accumulated Expense +')";

        connect = Database.connectDB();

        double num = 0;

        try {
            assert connect != null;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next())
            {
                num = result.getDouble("sum(Amount)");
            }
            Home_others.setText(String.valueOf(Math.round( num)));
            if (Math.abs(num) > 10000){
                Home_others.setFont(Font.font("Tahoma", FontWeight.BOLD,17));
            } else {
                Home_others.setFont(Font.font("Tahoma", FontWeight.BOLD,23));
            }
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
    }

    public void HomeDisplayMyBalance(){
        String sql = "SELECT sum(Amount) FROM income WHERE username = '" + CurrentSessionUserData.getUsername() + "'";

        connect = Database.connectDB();

        double countIncome = 0;

        try{
            assert connect != null;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()){
                countIncome = result.getDouble("sum(Amount)");
            }
        }catch (Exception e){
            e.printStackTrace(System.out);
        }

        sql = "SELECT SUM(Amount) FROM expense WHERE username = '"+ CurrentSessionUserData.getUsername() + "'";

        double countExpense = 0;
        try{
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()){
                countExpense = result.getDouble("SUM(Amount)");
            }
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        Home_balance.setText(String.valueOf(Math.round( countIncome - countExpense)));
        if (Math.abs(countIncome-countExpense) > 10000){
            Home_balance.setFont(Font.font("Tahoma", FontWeight.BOLD,17));
        } else {
            Home_balance.setFont(Font.font("Tahoma", FontWeight.BOLD,23));
        }
    }

    /******************
    * SETTING FORM
    *******************/

    private final String[] currencyList = {"USD", "Euro" , "VND", "Yen", "Pound"};

    public void SettingCurrencyList(){
        ObservableList<String> Curl = FXCollections.observableArrayList();

        Collections.addAll(Curl, currencyList);

        Setting_currency.setItems(Curl);
    }

    public void CheckBoxHiddenPassword()
    {
        if(SettingForm_PassWordHiddencheckbox.isSelected()){
            text_field_password.setText(password_field_password.getText());
            text_field_reenter.setText(password_field_reenter.getText());
            password_field_password.setVisible(false);
            password_field_reenter.setVisible(false);
            text_field_reenter.setVisible(true);
            text_field_password.setVisible(true);
        } else{
            password_field_reenter.setText(text_field_reenter.getText());
            password_field_password.setText(text_field_password.getText());
            password_field_password.setVisible(true);
            password_field_reenter.setVisible(true);
            text_field_reenter.setVisible(false);
            text_field_password.setVisible(false);
        }
    }

    @FXML
    private Button changepassBtn;

    @FXML
    private TextField Setting_username;

    public void settingUsernameChange(){
        String sql = "UPDATE admin SET username = '" + Setting_username.getText() + "' " +
                "WHERE username = '" + CurrentSessionUserData.getUsername() + "' ";

        String CheckSql = "SELECT * FROM admin WHERE username = '" + Setting_username.getText() + "'";

        connect = Database.connectDB();

        try{
            assert connect != null;
            prepare = connect.prepareStatement(CheckSql);

            result = prepare.executeQuery();

            if (result.next()){
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setHeaderText(null);
               alert.setTitle("Error Message!");
               alert.setContentText("This username is already in used, please try another username");
               alert.showAndWait();
            }
            else{
                prepare = connect.prepareStatement(sql);

                prepare.executeUpdate();

                ShowUsername();
            }
        }catch (SQLException e){
            e.printStackTrace(System.out);
        }
    }

    @FXML
    private Label username;

    public void ShowUsername(){
        username.setText(CurrentSessionUserData.getUsername());
    }

    public void settingPasswordChange()
    {
        String ChangePasswordSql, TmpGetPassword;
        if (SettingForm_PassWordHiddencheckbox.isSelected()) {
            TmpGetPassword = text_field_password.getText();
        } else {
            TmpGetPassword = password_field_password.getText();
        }
        ChangePasswordSql = "UPDATE admin SET"
                + " password = '" + TmpGetPassword
                + "' WHERE username ='" + CurrentSessionUserData.getUsername() + "'";
        Alert alert;
        connect = Database.connectDB();

            try{
                if (((text_field_password.getText().isEmpty()
                   ||text_field_reenter.getText().isEmpty()) && SettingForm_PassWordHiddencheckbox.isSelected())
                   ||((password_field_password.getText().isEmpty()
                   ||password_field_reenter.getText().isEmpty()) && !SettingForm_PassWordHiddencheckbox.isSelected())){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message!");
                    alert.setHeaderText(null);
                    alert.setContentText("Please enter/re-enter your new password blank fields");
                    alert.showAndWait();
                } else if ((text_field_reenter.getText().equals(text_field_password.getText()) && SettingForm_PassWordHiddencheckbox.isSelected())
                         ||(password_field_password.getText().equals(password_field_reenter.getText()) && !SettingForm_PassWordHiddencheckbox.isSelected()))
                {
                    String sqlcheck = "SELECT password FROM admin WHERE username = ?";
                    PreparedStatement preparecheck;
                    preparecheck = connect.prepareStatement(sqlcheck);
                    preparecheck.setString(1, CurrentSessionUserData.getUsername());

                    result = preparecheck.executeQuery();
                    result.next();
                    if (result.getString("password").equals(TmpGetPassword))
                    {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Message!");
                        alert.setHeaderText(null);
                        alert.setContentText("Please enter a new password!");
                        alert.showAndWait();
                    }
                    else {
                        prepare = connect.prepareStatement(ChangePasswordSql);
                        prepare.executeUpdate();

                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("INFORMATION MESSAGE");
                        alert.setHeaderText(null);
                        alert.setContentText("You have successfully changed your password");
                        alert.showAndWait();
                    }
                }
                else{
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR MESSAGE");
                    alert.setHeaderText(null);
                    alert.setContentText("Your re-enter password doesn't match the previous one");
                    alert.showAndWait();
                }

                text_field_password.setText("");
                text_field_reenter.setText("");
                password_field_password.setText("");
                password_field_reenter.setText("");

            }catch(Exception e) {
                e.printStackTrace(System.out);}
    }

    public void SettingSaveChanges(){
        if (!text_field_password.getText().isEmpty() || !password_field_password.getText().isEmpty()){
            settingPasswordChange();
        }
        if (!Setting_username.getText().isEmpty()){
            settingUsernameChange();
        }
        if (Setting_currency.getSelectionModel().getSelectedItem() != null){
            SettingCurrencyChange();
        }
    }

    public void SettingCurrencyChange(){
        if (!Setting_currency.getSelectionModel().getSelectedItem().equals(CurrentSessionUserData.getCurrency().getType()))
        {
            double oc = Currency.USDRatio(CurrentSessionUserData.getCurrency().getType());
            double nc = Currency.USDRatio(Setting_currency.getSelectionModel().getSelectedItem());
            String rep = "/" + oc + "*" + nc;
            String user = CurrentSessionUserData.getUsername();

           connect = Database.connectDB();

           try{
               String sql = "UPDATE Expense SET Amount = Amount" + rep +" WHERE username = '" + user + "'";

               assert connect != null;
               prepare = connect.prepareStatement(sql);
               prepare.executeUpdate();


               sql = "UPDATE Income SET Amount = Amount"+ rep +" WHERE username = '" + user + "'";

               prepare = connect.prepareStatement(sql);
               prepare.executeUpdate();

               sql = "UPDATE debt SET principal = principal"+ rep +", " +
                       "paid = paid" + rep + ", " +
                       "currentdebt = currentdebt" + rep +
                       " WHERE username = '" + user + "'";

               prepare = connect.prepareStatement(sql);
               prepare.executeUpdate();

               sql = "UPDATE objective SET goal = goal"+ rep +", " +
                       "saved = saved" + rep +
                       " WHERE username = '" + user + "'";
               prepare = connect.prepareStatement(sql);
               prepare.executeUpdate();

               sql = "UPDATE saving SET principal = principal"+ rep +", " +
                       "Amount = Amount" + rep +
                       " WHERE username = '" + user + "'";
               prepare = connect.prepareStatement(sql);
               prepare.executeUpdate();

               sql = "UPDATE admin SET currency = '" + Setting_currency.getSelectionModel().getSelectedItem() + "' " +
                       "WHERE username = '" + user + "'";
               prepare = connect.prepareStatement(sql);
               prepare.executeUpdate();

               CurrentSessionUserData.getCurrency().setType(Setting_currency.getSelectionModel().getSelectedItem());

               setHomeCurrencyType();

               Setting_currency.getSelectionModel().clearSelection();
           } catch (Exception e){
               e.printStackTrace(System.out);
           }

        }
    }

    @FXML
    private Label Home_CurrencyType;

    public void setHomeCurrencyType(){
        Home_CurrencyType.setText(CurrentSessionUserData.getCurrency().getType());
    }

    /************************************
     *  INCOME MAIN FORM
     ************************************/
    //Return Income List
    public ObservableList<IncomeData> addIncomeListData(){
        ObservableList<IncomeData> listIncome = FXCollections.observableArrayList();

        String sql = "SELECT * FROM income WHERE username = ?";

        connect = Database.connectDB();

        try{
            IncomeData IncomeD;
            assert connect != null;
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, CurrentSessionUserData.getUsername());
            result = prepare.executeQuery();
            while (result.next()){
                IncomeD = new IncomeData(result.getInt("No"),
                        result.getString("Source"),
                        result.getDouble("Amount"),
                        result.getString("Category"),
                        result.getDate("Date"));
                listIncome.add(IncomeD);
            }

        }catch(Exception e){e.printStackTrace(System.out);}

        return listIncome;
    }

    public void IncomeShowListData(){
        //Show List
        ObservableList<IncomeData> addIncomeListD = addIncomeListData();

        Income_col_no.setCellValueFactory(new PropertyValueFactory<>("No"));
        Income_col_source.setCellValueFactory(new PropertyValueFactory<>("Source"));
        Income_col_amount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
        Income_col_category.setCellValueFactory(new PropertyValueFactory<>("Category"));
        Income_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        Income_table.setItems(addIncomeListD);
    }

    //Show Selected Income Row
    private int Number;
    public void IncomeSelect(){
        IncomeData expenseD = Income_table.getSelectionModel().getSelectedItem();
        int num = Income_table.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) return;

        Income_source.setText(expenseD.getSource());
        Income_amount.setText(String.valueOf(expenseD.getAmount()));
        Income_category.setText(expenseD.getCategory());
        Income_date.setValue(LocalDate.parse(String.valueOf(expenseD.getDate())));
        Number = expenseD.getNo();
    }

    public void IncomeAdd(){
        String insertData = "INSERT INTO income (Source,Amount,Category,Date,username) VALUES (?,?,?,?,?)";

        connect = Database.connectDB();

        try{
            Alert alert;
            if (Income_source.getText().isEmpty()
            || Income_amount.getText().isEmpty()
            || Income_category.getText().isEmpty()
            || Income_date.getValue() == null){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message!");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else if (!Income_amount.getText().matches("\\d+(\\.\\d+)?")){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message!");
                alert.setHeaderText(null);
                alert.setContentText("Please input a valid value for amount");
                alert.showAndWait();
            }
            else {
                String UN = CurrentSessionUserData.getUsername();
                prepare = connect.prepareStatement(insertData);
                prepare.setString(1,Income_source.getText());
                prepare.setString(2,Income_amount.getText());
                prepare.setString(3,Income_category.getText());
                prepare.setString(4,String.valueOf(Income_date.getValue()));
                prepare.setString(5,UN);

                prepare.executeUpdate();

                IncomeShowListData();
                IncomeClear();
            }
        }catch(Exception e){
            e.printStackTrace(System.out);
        }
    }

    public void IncomeClear(){
        Income_source.setText("");
        Income_amount.setText("");
        Income_category.setText("");
        Income_date.setValue(null);
        Number = -1;
    }

    public void IncomeUpdate(){
        String NumNow = String.valueOf(Number);
        String updateData = "UPDATE income SET "
                + "Source = '" + Income_source.getText()
                + "', Amount = '" + Income_amount.getText()
                + "', Category = '" + Income_category.getText()
                + "', Date = '" + Income_date.getValue()
                + "' WHERE No = "
                + NumNow;

        connect = Database.connectDB();

        try {
            Alert alert;
            if (Income_source.getText().isEmpty()
                    || Income_amount.getText().isEmpty()
                    || Income_category.getText().isEmpty()
                    || Income_date.getValue() == null
                    || Number == -1) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select a data");
                alert.showAndWait();
            }else if (!Income_amount.getText().matches("\\d+(\\.\\d+)?")){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message!");
                alert.setHeaderText(null);
                alert.setContentText("Please input a valid value for amount");
                alert.showAndWait();
            }
            else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE Income No " + NumNow + "?");
                Optional<ButtonType> option = alert.showAndWait();

                assert option.orElse(null) != null;
                if (option.orElse(null).equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(updateData);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();

                    // TO UPDATE THE TABLEVIEW
                    IncomeShowListData();
                    // TO CLEAR THE FIELDS
                    IncomeClear();

                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void IncomeDelete(){
        String NumNow = String.valueOf(Number);
        String deleteData = "DELETE FROM income WHERE No = '"
                + NumNow + "'AND username = \""+ CurrentSessionUserData.getUsername()+"\"";

        connect = Database.connectDB();

        try {
            Alert alert;
            if (Income_source.getText().isEmpty()
                    || Income_amount.getText().isEmpty()
                    || Income_category.getText().isEmpty()
                    || Income_date.getValue() == null
                    || Number == -1) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE this data (#" + NumNow + ")?");

                Optional<ButtonType> option = alert.showAndWait();

                assert option.orElse(null) != null;
                if (option.orElse(null).equals(ButtonType.OK)) {

                    statement = connect.createStatement();
                    statement.executeUpdate(deleteData);

                    String checkData = "SELECT No FROM income "
                            + "WHERE No = '" + NumNow + "' AND username = \""+ CurrentSessionUserData.getUsername()+"\"";

                    prepare = connect.prepareStatement(checkData);
                    result = prepare.executeQuery();

                    // IF THE STUDENT NUMBER IS EXIST THEN PROCEED TO DELETE
                    if (result.next()) {
                        String deleteGrade = "DELETE FROM income WHERE "
                                + "No = '" + NumNow + "' AND username = \""+ CurrentSessionUserData.getUsername()+"\"";

                        statement = connect.createStatement();
                        statement.executeUpdate(deleteGrade);

                    }// IF NOT THEN NVM

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();

                    // TO UPDATE THE TABLEVIEW
                    IncomeShowListData();
                    // TO CLEAR THE FIELDS
                    IncomeClear();

                }

            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    private final String[] IScategory = {"(default)","Source", "Amount", "Category", "Date"};

    public void IncomeSortList()
    {
        List<String> arrayL = new ArrayList<>();

        Collections.addAll(arrayL,IScategory);

        ObservableList <String> ObList = FXCollections.observableArrayList(arrayL);
        Income_Choose_Col.setItems(ObList);
    }

    public void IncomeSorttypeList()
    {
        List<String> arrL = new ArrayList<>();

        arrL.add("ASC");
        arrL.add("DESC");

        ObservableList<String> ObList = FXCollections.observableArrayList(arrL);
        Income_sorttype.setItems(ObList);
    }

    public void IncomeSearch(){
        ObservableList <IncomeData> addExpenseListC = Income_table.getItems();
        FilteredList<IncomeData> filter = new FilteredList<>(addExpenseListC,e->true);
        Income_search.textProperty().addListener((Observable, oldValue, newValue) ->{

            filter.setPredicate(predicateExpenseData ->{

                if (newValue == null || newValue.isEmpty()){
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if(predicateExpenseData.getSource().toLowerCase().contains(searchKey)){
                    return true;
                }else if (predicateExpenseData.getCategory().toLowerCase().contains(searchKey)){
                    return true;
                }else if (String.valueOf(predicateExpenseData.getAmount()).contains(searchKey)){
                    return true;
                }else return predicateExpenseData.getDate().toString().contains(searchKey);

            });
            SortedList <IncomeData> sortList = new SortedList<>(filter);
            sortList.comparatorProperty().bind(Income_table.comparatorProperty());
            Income_table.setItems(sortList);
        });
    }

    public void IncomeSort()
    {
        ObservableList <IncomeData> addExpenseListC = Income_table.getItems();
        SortedList<IncomeData> sortL = new SortedList<>(addExpenseListC);
        String scol = Income_Choose_Col.getSelectionModel().getSelectedItem();
        String stype = Income_sorttype.getSelectionModel().getSelectedItem();
        if (Expense_Choose_Col.getSelectionModel().getSelectedItem() == null
                || Expense_sorttype.getSelectionModel().getSelectedItem() == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error Message");
            alert.setContentText("Please select column/type to sort");
            alert.showAndWait();
        } else {
        sortL.setComparator((item1,item2) ->{
            switch (scol) {
                case "(default)" -> {
                    int a1 = item1.getNo();
                    int a2 = item2.getNo();
                    if (stype.equals("ASC")) return Integer.compare(a1, a2);
                    else return Integer.compare(a2, a1);
                }
                case "Purpose" -> {
                    String a1 = item1.getSource();
                    String a2 = item2.getSource();
                    if (stype.equals("ASC")) return a1.compareToIgnoreCase(a2);
                    else return a2.compareToIgnoreCase(a1);
                }
                case "Category" -> {
                    String a1 = item1.getCategory();
                    String a2 = item2.getCategory();
                    if (stype.equals("ASC")) return a1.compareToIgnoreCase(a2);
                    else return a2.compareToIgnoreCase(a1);
                }
                case "Amount" -> {
                    double a1 = item1.getAmount();
                    double a2 = item2.getAmount();
                    if (stype.equals("ASC")) return Double.compare(a1, a2);
                    else return Double.compare(a2, a1);
                }
                default -> {
                    Date a1 = item1.getDate();
                    Date a2 = item2.getDate();
                    if (stype.equals("ASC")) return a1.compareTo(a2);
                    else return a2.compareTo(a1);
                }
            }
        });
        Income_table.setItems(sortL);}
    }

    /************************************
     * EXPENSE MAIN FORM
     ************************************/

    //Show Expense Data in the table
    public ObservableList<ExpenseData> addExpenseListData(){

        ObservableList<ExpenseData> listExpense = FXCollections.observableArrayList();

        String sql = "SELECT * FROM expense WHERE username = \""+ CurrentSessionUserData.getUsername()+"\"";

        connect = Database.connectDB();

        try{
            ExpenseData expenseD;
            assert connect != null;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()){
                expenseD = new ExpenseData(result.getInt("No"),
                                           result.getString("Purpose"),
                                           result.getString("Category"),
                                           result.getDouble("Amount"),
                                           result.getDate("Date"),
                                           result.getString("Description"));
                listExpense.add(expenseD);
            }

        }catch(Exception e){e.printStackTrace(System.out);}

        return listExpense;
    }
    private ObservableList<ExpenseData> addExpenseListD;

    public void IncomeSortingTable()
    {
        ObservableList <ExpenseData> getExpenseSortList = Expense_table.getItems();
        SortedList<ExpenseData> setExpenseSortList = new SortedList<>(getExpenseSortList);
        String selectedSortColumn = Expense_Choose_Col.getSelectionModel().getSelectedItem();
        String selectedSortType = Expense_sorttype.getSelectionModel().getSelectedItem();
        if (Expense_Choose_Col.getSelectionModel().getSelectedItem() == null
        || Expense_sorttype.getSelectionModel().getSelectedItem() == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error Message");
            alert.setContentText("Please select column/type to sort");
            alert.showAndWait();
        } else {
        setExpenseSortList.setComparator((item1,item2) ->{
            switch (selectedSortColumn) {
                case "(default)" -> {
                    int a1 = item1.getNo();
                    int a2 = item2.getNo();
                    if (selectedSortType.equals("ASC")) return Integer.compare(a1, a2);
                    else return Integer.compare(a2, a1);
                }
                case "Purpose" -> {
                    String a1 = item1.getPurpose();
                    String a2 = item2.getPurpose();
                    if (selectedSortType.equals("ASC")) return a1.compareToIgnoreCase(a2);
                    else return a2.compareToIgnoreCase(a1);
                }
                case "Category" -> {
                    String a1 = item1.getCategory();
                    String a2 = item2.getCategory();
                    if (selectedSortType.equals("ASC")) return a1.compareToIgnoreCase(a2);
                    else return a2.compareToIgnoreCase(a1);
                }
                case "Amount" -> {
                    double a1 = item1.getAmount();
                    double a2 = item2.getAmount();
                    if (selectedSortType.equals("ASC")) return Double.compare(a1, a2);
                    else return Double.compare(a2, a1);
                }
                case "Date" -> {
                    Date a1 = item1.getDate();
                    Date a2 = item2.getDate();
                    if (selectedSortType.equals("ASC")) return a1.compareTo(a2);
                    else return a2.compareTo(a1);
                }
                default -> {
                    String a1 = item1.getDescription();
                    String a2 = item2.getDescription();
                    if (selectedSortType.equals("ASC")) return a1.compareToIgnoreCase(a2);
                    else return a2.compareToIgnoreCase(a1);
                }
            }
        });
        Expense_table.setItems(setExpenseSortList);}
    }

    public void ExpenseSearch(){
        FilteredList<ExpenseData> filter = new FilteredList<>(addExpenseListD,e->true);

        Expense_search.textProperty().addListener((Observable, oldValue, newValue) ->{

            filter.setPredicate(predicateExpenseData ->{

                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                String searchKey = newValue.toLowerCase();

                if(predicateExpenseData.getPurpose().toLowerCase().contains(searchKey)){
                    return true;
                }else if (predicateExpenseData.getCategory().toLowerCase().contains(searchKey)){
                    return true;
                }else if (String.valueOf(predicateExpenseData.getAmount()).contains(searchKey)){
                    return true;
                }else if (predicateExpenseData.getDate().toString().contains(searchKey)){
                    return true;
                }else return predicateExpenseData.getDescription().toLowerCase().contains(searchKey);

            });
            SortedList <ExpenseData> sortList = new SortedList<>(filter);
            sortList.comparatorProperty().bind(Expense_table.comparatorProperty());
            Expense_table.setItems(sortList);
        });
    }

    public void addExpenseShowListData(){
        addExpenseListD = addExpenseListData();

        Expense_col_purpose.setCellValueFactory(new PropertyValueFactory<>("purpose"));
        Expense_col_category.setCellValueFactory(new PropertyValueFactory<>("Category"));
        Expense_col_amount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
        Expense_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        Expense_col_description.setCellValueFactory(new PropertyValueFactory<>("description"));

        Expense_table.setItems(addExpenseListD);
    }

    private final String[] ExpenseFormCategoryList = {"Living Expense", "Capital Expenditure", "Entertainment Expense", "Accumulated Expenses"};
    private final String[] ExpenseFormSortCategory = {"Purpose", "Category", "Amount", "Date", "Description", "(default)"};
    public void addExpenseSortList()
    {
        List<String> genderL = new ArrayList<>();

        Collections.addAll(genderL, ExpenseFormSortCategory);

        ObservableList<String> ObList = FXCollections.observableArrayList(genderL);
        Expense_Choose_Col.setItems(ObList);
    }

    public void addExpenseSortTypeList()
    {
        List<String> genderL = new ArrayList<>();

        genderL.add("ASC");
        genderL.add("DESC");

        ObservableList<String> ObList = FXCollections.observableArrayList(genderL);
        Expense_sorttype.setItems(ObList);
    }

    private final List<String> CategoryExpenseSelectionList = new ArrayList<>();

    public void addExpenseCategoryList(){
        CategoryExpenseSelectionList.clear();

        CategoryExpenseSelectionList.addAll(Arrays.asList(ExpenseFormCategoryList));

        ObservableList<String> ObserveList = FXCollections.observableArrayList(CategoryExpenseSelectionList);

        Expense_category.setItems(ObserveList);
    }

    public void addExpenseSelect(){
        ExpenseData expenseD = Expense_table.getSelectionModel().getSelectedItem();
        int num = Expense_table.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) return;

        Expense_purpose.setText(expenseD.getPurpose());
        Expense_amount.setText(String.valueOf(expenseD.getAmount()));
        Expense_description.setText(expenseD.getDescription());
        Expense_date.setValue(LocalDate.parse(String.valueOf(expenseD.getDate())));
        Number = expenseD.getNo();
    }

    public void ExpenseAdd() {

        String insertData = "INSERT INTO expense (Purpose,Category,Amount,Date, Description, username) VALUES (?,?,?,?,?,?)";

        connect = Database.connectDB();
        try{
            Alert alert;
            if (Expense_purpose.getText().isEmpty()
                    || Expense_category.getSelectionModel().getSelectedItem() == null
                    || Expense_amount.getText().isEmpty()
                    || Expense_date.getValue() == null){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message!");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }
            else if (Number != -1)
            {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message!");
                alert.setHeaderText(null);
                alert.setContentText("Please use update function instead");
                alert.showAndWait();
            }else if (!Expense_amount.getText().matches("\\d+(\\.\\d+)?")){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message!");
                alert.setHeaderText(null);
                alert.setContentText("Please input a valid value for amount");
                alert.showAndWait();
            } else
            {
                String UN = CurrentSessionUserData.getUsername();
                String des;
                if (Expense_description.getText().isEmpty()) des = "No Description";
                else des = Expense_description.getText();
                prepare = connect.prepareStatement(insertData);
                prepare.setString(1,Expense_purpose.getText());
                prepare.setString(2,(Expense_category.getSelectionModel().getSelectedItem()));
                prepare.setString(3,Expense_amount.getText());
                prepare.setString(4,String.valueOf(Expense_date.getValue()));
                prepare.setString(5,des);
                prepare.setString(6,UN);

                prepare.executeUpdate();

                addExpenseShowListData();
                ExpenseClear();
            }
        }catch(Exception e){
            e.printStackTrace(System.out);
        }
    }

    public void ExpenseClear(){
        Expense_purpose.setText("");
        Expense_category.getSelectionModel().clearSelection();
        Expense_amount.setText("");
        Expense_description.setText("");
        Expense_date.setValue(null);
        Number = -1;
    }

    public void ExpenseUpdate(){
        String NumNow = String.valueOf(Number);
        String des = Expense_description.getText();
        if (Expense_description.getText().isEmpty()) des = "No Description";
        String updateData = "UPDATE expense SET "
                + "Purpose = '" + Expense_purpose.getText()
                + "', Category = '" + Expense_category.getSelectionModel().getSelectedItem()
                + "', Amount = '" + Expense_amount.getText()
                + "', Date = '" + Expense_date.getValue()
                + "', Description = '" + des
                + "' WHERE No = "
                + NumNow;

        connect = Database.connectDB();

        try {
            Alert alert;
            if (Expense_purpose.getText().isEmpty()
                    || Expense_amount.getText().isEmpty()
                    || Expense_category.getSelectionModel().getSelectedItem() == null
                    || Expense_date.getValue() == null
                    || Number == -1) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select a data");
                alert.showAndWait();
            } else if (!Expense_amount.getText().matches("\\d+(\\.\\d+)?")) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message!");
                alert.setHeaderText(null);
                alert.setContentText("Please input a valid value for amount");
                alert.showAndWait();
            }
            else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE Expense  " + NumNow + "?");
                Optional<ButtonType> option = alert.showAndWait();

                assert option.orElse(null) != null;
                if (option.orElse(null).equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(updateData);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();

                    // TO UPDATE THE TABLEVIEW
                    addExpenseShowListData();
                    // TO CLEAR THE FIELDS
                    ExpenseClear();

                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void ExpenseDelete(){
        String NumNow = String.valueOf(Number);
        String deleteData = "DELETE FROM expense WHERE No = '"
                + NumNow + "'AND username = \""+ CurrentSessionUserData.getUsername()+"\"";

        connect = Database.connectDB();

        try {
            Alert alert;
            if (Expense_purpose.getText().isEmpty()
                    || Expense_amount.getText().isEmpty()
                    || Expense_description.getText().isEmpty()
                    || Expense_date.getValue() == null
                    || Number == -1) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE this data (#" + NumNow + ")?");

                Optional<ButtonType> option = alert.showAndWait();

                assert option.orElse(null) != null;
                if (option.orElse(null).equals(ButtonType.OK)) {

                    statement = connect.createStatement();
                    statement.executeUpdate(deleteData);

                    String checkData = "SELECT No FROM expense "
                            + "WHERE No = '" + NumNow + "' AND username = \""+ CurrentSessionUserData.getUsername()+"\"";

                    prepare = connect.prepareStatement(checkData);
                    result = prepare.executeQuery();

                    // IF THE STUDENT NUMBER IS EXIST THEN PROCEED TO DELETE
                    if (result.next()) {
                        String deleteGrade = "DELETE FROM expense WHERE "
                                + "No = '" + NumNow + "' AND username = \""+ CurrentSessionUserData.getUsername()+"\"";

                        statement = connect.createStatement();
                        statement.executeUpdate(deleteGrade);

                    }// IF NOT THEN NVM

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();

                    // TO UPDATE THE TABLEVIEW
                    addExpenseShowListData();
                    // TO CLEAR THE FIELDS
                    ExpenseClear();

                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    /************************************
     *NAVIGATE FORM
     ************************************/
    //Drag and drop UI
    private double x;
    private double y;
    public void logOut(){
        try {
            Alert alert = new Alert (Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");

            Optional<ButtonType> option = alert.showAndWait();

            assert option.orElse(null) != null;
            if (option.orElse(null).equals(ButtonType.OK))
            {
                Logout_btn.getScene().getWindow().hide();

                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                scene.setOnMousePressed((MouseEvent event) -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });

                scene.setOnMouseDragged((MouseEvent event) ->{
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);
                    stage.setOpacity(.8);
                });

                scene.setOnMouseReleased((MouseEvent event) -> stage.setOpacity(1));


                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {e.printStackTrace(System.out);}
    }

    private int CurrentNavigator = 1;
    public void switchForm(ActionEvent event){
        if (event.getSource() == Home_btn)
        {
            Home_form.setVisible(true);
            Expense_form.setVisible(false);
            Income_form.setVisible(false);
            Indebt_form.setVisible(false);
            setting_form.setVisible(false);

            Home_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3f82ae, #26bf7d)");
            Expense_btn.setStyle("-fx-background-color: transparent");
            Income_btn.setStyle("-fx-background-color: transparent");
            Indebt_btn.setStyle("-fx-background-color: transparent");
            setting_btn.setStyle("-fx-background-color: transparent");

            CurrentNavigator = 1;

            setHighlightedCellDatePicker();
            HomeDisplayTotalExpenseChart(Home_barChart_ChooseGroup.getValue(),Home_barChart_baseOn.getValue(), Home_barChart_sorttype.getValue());
            HomeDisplayMyBalance();
            HomeDisplayShoppingExpense();
            HomeDisplayBills();
            HomeDisplayEntertainmentExpense();
            HomeDisplayOthers();
        } else if (event.getSource() == Expense_btn)
        {
            Home_form.setVisible(false);
            Expense_form.setVisible(true);
            Income_form.setVisible(false);
            Indebt_form.setVisible(false);
            setting_form.setVisible(false);

            Home_btn.setStyle("-fx-background-color: transparent");
            Expense_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3f82ae, #26bf7d)");
            Income_btn.setStyle("-fx-background-color: transparent");
            Indebt_btn.setStyle("-fx-background-color: transparent");
            setting_btn.setStyle("-fx-background-color: transparent");

            CurrentNavigator = 2;

            Number = -1;
            addExpenseShowListData();
            addExpenseSortList();
            addExpenseSortTypeList();
        } else if (event.getSource() == Income_btn)
        {
            Home_form.setVisible(false);
            Expense_form.setVisible(false);
            Income_form.setVisible(true);
            Indebt_form.setVisible(false);
            setting_form.setVisible(false);

            Home_btn.setStyle("-fx-background-color: transparent");
            Expense_btn.setStyle("-fx-background-color: transparent");
            Income_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3f82ae, #26bf7d)");
            Indebt_btn.setStyle("-fx-background-color: transparent");
            setting_btn.setStyle("-fx-background-color: transparent");

            Number = -1;
            CurrentNavigator = 3;

            IncomeShowListData();
        } else if (event.getSource() == Indebt_btn)
        {
            Home_form.setVisible(false);
            Expense_form.setVisible(false);
            Income_form.setVisible(false);
            Indebt_form.setVisible(true);
            setting_form.setVisible(false);

            Home_btn.setStyle("-fx-background-color: transparent");
            Expense_btn.setStyle("-fx-background-color: transparent");
            Income_btn.setStyle("-fx-background-color: transparent");
            Indebt_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3f82ae, #26bf7d)");
            setting_btn.setStyle("-fx-background-color: transparent");


            DeleteAllAnchor();
            addEventOnInit();
            addObjectiveOnInit();
            addSavingOnInit();

            CurrentNavigator = 4;
        } else if (event.getSource() == setting_btn)
        {
            Home_form.setVisible(false);
            Expense_form.setVisible(false);
            Income_form.setVisible(false);
            Indebt_form.setVisible(false);
            setting_form.setVisible(true);

            Home_btn.setStyle("-fx-background-color: transparent");
            Expense_btn.setStyle("-fx-background-color: transparent");
            Income_btn.setStyle("-fx-background-color: transparent");
            Indebt_btn.setStyle("-fx-background-color: transparent");
            setting_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3f82ae, #26bf7d)");

            CurrentNavigator = 5;

            text_field_password.setVisible(false);
            text_field_reenter.setVisible(false);
            SettingForm_PassWordHiddencheckbox.setSelected(false);
        }
    }
    public void Dclose()
    {
        System.exit(0);
    }

    public void Minimize()
    {
        Stage stage = (Stage)main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    public void defaultNav(){
        //Navigate Home Form First
        Home_form.setVisible(true);
        Expense_form.setVisible(false);
        Income_form.setVisible(false);
        Indebt_form.setVisible(false);
        setting_form.setVisible(false);

        Home_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3f82ae, #26bf7d)");
        Expense_btn.setStyle("-fx-background-color: transparent");
        Income_btn.setStyle("-fx-background-color: transparent");
        Indebt_btn.setStyle("-fx-background-color: transparent");
        setting_btn.setStyle("-fx-background-color: transparent");

        Home_Filter.setVisible(false);
        Home_FilterLabel.setVisible(false);
        //Display Home Form Attribute
        HomeDisplayMyBalance();
        HomeDisplayBills();
        HomeDisplayEntertainmentExpense();
        HomeDisplayShoppingExpense();
        HomeDisplayOthers();

        H_balance.setAlignment(Pos.CENTER_RIGHT);
        H_shopping.setAlignment(Pos.CENTER);
        H_fooddrink.setAlignment(Pos.CENTER);
        H_bills.setAlignment(Pos.CENTER);
        H_others.setAlignment(Pos.CENTER);

        //Re-create hover css on navigate buttons
        Home_btn.setOnMouseEntered(MouseEvent ->{
            if (CurrentNavigator == 1) return;
            Home_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #2d658c, #2ca772)");
        });
        Home_btn.setOnMouseExited(MouseEvent ->{
            if (CurrentNavigator == 1) return;
            Home_btn.setStyle("-fx-background-color: transparent");
        });

        Expense_btn.setOnMouseEntered(MouseEvent ->{
            if (CurrentNavigator == 2) return;
            Expense_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #2d658c, #2ca772)");
        });
        Expense_btn.setOnMouseExited(MouseEvent ->{
            if (CurrentNavigator == 2) return;
            Expense_btn.setStyle("-fx-background-color: transparent");
        });

        Income_btn.setOnMouseEntered(MouseEvent ->{
            if (CurrentNavigator == 3) return;
            Income_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #2d658c, #2ca772)");
        });
        Income_btn.setOnMouseExited(MouseEvent ->{
            if (CurrentNavigator == 3) return;
            Income_btn.setStyle("-fx-background-color: transparent");
        });

        Indebt_btn.setOnMouseEntered(MouseEvent ->{
            if (CurrentNavigator == 4) return;
            Indebt_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #2d658c, #2ca772)");
        });
        Indebt_btn.setOnMouseExited(MouseEvent ->{
            if (CurrentNavigator == 4) return;
            Indebt_btn.setStyle("-fx-background-color: transparent");
        });

        setting_btn.setOnMouseEntered(MouseEvent ->{
            if (CurrentNavigator == 5) return;
            setting_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #2d658c, #2ca772)");
        });
        setting_btn.setOnMouseExited(MouseEvent ->{
            if (CurrentNavigator == 5) return;
            setting_btn.setStyle("-fx-background-color: transparent");
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ChooseGroupByOption();

        ShowUsername();

        setHomeCurrencyType();

        defaultNav();
        addHomeTimeRangeCategoryList();

        SettingCurrencyList();

        addExpenseShowListData();
        addExpenseCategoryList();
        addExpenseSortList();
        addExpenseSortTypeList();

        IncomeShowListData();
        IncomeSortList();
        IncomeSorttypeList();

        setHighlightedCellDatePicker();
    }
}
