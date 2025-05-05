package com.example.calorie_counter_bmi.controllers.client;

import com.example.calorie_counter_bmi.DBUtils;
import com.example.calorie_counter_bmi.controllers.RegisterController;
import com.example.calorie_counter_bmi.models.EatenProduct;
import com.example.calorie_counter_bmi.models.Product;
import com.example.calorie_counter_bmi.views.ViewFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

/** контроллер отвечающий за
 * приветствие пользователя
 * изменение веса пользователя
 * выход из аккаунта
 * выбор даты
 * отображение ежедневной нормы кбжу пользователя
 * кол-ва съеденных за день кбжу
 * списка съеденных за день продуктов
 * добавление продукта в рацион
 * отображение списка продуктов
 * добавление нового продукта
 * поиск продукта */

public class RightBoardController implements Initializable {

    /**
     * имя пользователя
     */
    public Label username_lbl;
    /**
     * вес пользователя
     */
    public TextField users_weigth_txt_fld;
    /**
     * кнопка изменить вес пользователя
     */
    public Button edit_users_weight_btn;
    /**
     * кнопка выйти
     */
    public Button logout_btn;
    /**
     * id пользователя, для обращения в таблицу хранящей то что съел пользователь
     */
    private Integer user_id;
    /**
     * id продукта, для обращения в таблицу хранящей то что съел пользователь
     */
    private Integer product_id;
    /**
     * календарь с выбором даты
     */
    public DatePicker calendar_date_picker;
    /**
     * поле в котором отображается текущее набранное кол-во калорий
     */
    public Label users_current_total_number_label;
    /**
     * поле в котором отображается текущее набранное кол-во белков
     */
    public Label users_current_proteins_number_label;
    /**
     * поле в котором отображается текущее набранное кол-во углеводов
     */
    public Label users_current_carbohydrates_number_label;
    /**
     * поле в котором отображается текущее набранное кол-во жиров
     */
    public Label users_current_fat_number_label;
    /**
     * поле в котором отображается текущее набранное кол-во клетчатки
     */
    public Label users_current_fiber_number_label;
    /**
     * поле в котором отображается норма суточного потребления калорий
     */
    public Label users_daily_total_label;
    /**
     * поле в котором отображается норма суточного потребления белков
     */
    public Label users_daily_proteins_label;
    /**
     * поле в котором отображается норма суточного потребления углеводов
     */
    public Label users_daily_carbohydrates_label;
    /**
     * поле в котором отображается норма суточного потребления клетчатки
     */
    public Label users_daily_fiber_label;
    /**
     * поле в котором отображается норма суточного потребления жиров
     */
    public Label users_daily_fat_label;
    /**
     * таблица отображающая съеденные за день продукты
     */
    public TableView<EatenProduct> users_daily_menu_table_view;
    /**
     * название съеденного продукта
     */
    public TableColumn<EatenProduct, String> name_ate_product_table_column;
    /**
     * кол-во съеденного продукта
     */
    public TableColumn<EatenProduct, Double> amount_ate_product_table_column;
    /**
     * калории полученные в результате съедания продукта
     */
    public TableColumn<EatenProduct, Double> calories_ate_product_table_column;
    /**
     * кнопка отвечающая за добавление съеденного продукта в рацион питания
     */
    public Button add_new_dish_btn;
    /**
     * поле в которое вводиться название съеденного продукта
     */
    public TextField add_new_dish_txt_fld;
    /**
     * поле в которое вводиться кол-во съеденного продукта
     */
    public Spinner<Double> add_new_dish_amount_spinner;
    /**
     * поле в которое вводиться продукт для добавления в рацион
     */
    public TextField search_product_txt_fld;
    /**
     * таблица отображающая список продуктов из бд
     */
    public TableView<Product> list_of_products_table_view;
    /**
     * столбец названия продукта
     */
    public TableColumn<Product, String> name_can_eat_product_table_column;
    /**
     * столбец калорийности выбираемого продукта в 100г/100мл
     */
    public TableColumn<Product, Double> calorie_dose_can_eat_product_table_column;
    /**
     * столбец кол-ва белка выбираемого продукта в 100г/100мл
     */
    public TableColumn<Product, Double> proteins_dose_can_eat_product_table_column;
    /**
     * столбец кол-ва жиров выбираемого продукта в 100г/100мл
     */
    public TableColumn<Product, Double> fat_dose_can_eat_product_table_column;
    /**
     * столбец кол-ва углеводов выбираемого продукта в 100г/100мл
     */
    public TableColumn<Product, Double> carbohydrates_dose_can_eat_product_table_column;
    /**
     * столбец кол-ва клетчатки добавляемого продукта в 100г/100мл
     */
    public TableColumn<Product, Double> fiber_dose_can_eat_product_table_column;
    /**
     * текстовое поле для названия продукта которое пользователь хочет добавить
     */
    public TextField add_new_product_txt_fld;
    /**
     * кнопка добавить новый продукт
     */
    public Button add_new_product_btn;
    /**
     * кол-во белков добавляемого продукта в 100г/100мл
     */
    public TextField new_product_proteins_dose_txt_fld;
    /**
     * кол-во жиров добавляемого продукта в 100г/100мл
     */
    public TextField new_product_fat_dose_txt_fld;
    /**
     * кол-во клетчатки добавляемого продукта в 100г/100мл
     */
    public TextField new_product_fiber_dose_txt_fld;
    /**
     * кол-во углеводов добавляемого продукта в 100г/100мл
     */
    public TextField new_product_carbohydrates_dose_txt_fld;
    /**
     * калорийность добавляемого продукта в 100г/100мл
     */
    public TextField new_product_calories_dose_txt_fld;
    /**
     * лист для хранения списка продуктов из бд и их динамического поиска
     */
    ObservableList<Product> productSearchObservableList = FXCollections.observableArrayList();
    /**
     * лист для хранения списка съеденных продуктов из бд
     */
    ObservableList<EatenProduct> eatenProductSearchObservableList = FXCollections.observableArrayList();
    /**
     * переменная для хранения id выбранного продукта из таблицы
     */
    Integer index;
    /**
     * переменная для хранения выбранной даты
     */
    String chosenDate;
    /**
     * переменная для хранения кол-ва съеденного продукта
     * добавляемого в меню
     */
    Double currentSpinnerValue;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /**
         * обработка date piker
         */
        calendar_date_picker.setValue(LocalDate.now());
        chosenDate = calendar_date_picker.getValue().toString();
        getEatenProductTableView();
        calendar_date_picker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                chosenDate = calendar_date_picker.getValue().toString();
                getEatenProductTableView();
                setEatenDoseLabelView();
            }
        });

        /**
         * обработка spinner в форме добавления съеденного продукта
         */
        add_new_dish_amount_spinner.valueProperty().addListener(new ChangeListener<Double>() {
            @Override
            public void changed(ObservableValue<? extends Double> observableValue, Double aDouble, Double t1) {
                currentSpinnerValue = add_new_dish_amount_spinner.getValue();
            }
        });
        /**
         * обработка формы добавления съеденного продукта
         */
        add_new_dish_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (index == null){
                    System.out.println("Please choose the product!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please choose the product!");
                    alert.show();
                }
                else if (currentSpinnerValue == null){
                    System.out.println("Please enter the amount!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please enter the amount!");
                    alert.show();
                }else {
                    String name = name_can_eat_product_table_column.getCellData(index).toString();
                    Double cal = calorie_dose_can_eat_product_table_column.getCellData(index);
                    Double protein = proteins_dose_can_eat_product_table_column.getCellData(index);
                    Double fat = fat_dose_can_eat_product_table_column.getCellData(index);
                    Double carbs = carbohydrates_dose_can_eat_product_table_column.getCellData(index);
                    Double fiber = fiber_dose_can_eat_product_table_column.getCellData(index);

                    Double[] nutritionValues = Product.calculateNutrition(currentSpinnerValue * 100, cal, protein, fat, carbs, fiber);
                    try {
                        boolean res = DBUtils.addNewEatenProduct(chosenDate, product_id, currentSpinnerValue * 100, name, nutritionValues[0], nutritionValues[1], nutritionValues[2], nutritionValues[3], nutritionValues[4]);
                        if (res){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setContentText("Eaten Product successfully added!");
                            alert.show();
                        }else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("An error has occurred!");
                            alert.show();
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    getEatenProductTableView();
                    setEatenDoseLabelView();
                }
            }
        });
        /**
         * обработка кнопки изменить вес
         */
        edit_users_weight_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!users_weigth_txt_fld.getText().trim().isEmpty() && users_weigth_txt_fld.getText().contains(".")){
                    try {
                        boolean res = DBUtils.updateUsersWeight(user_id, Double.valueOf(users_weigth_txt_fld.getText()));
                        if (res){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setContentText("Your weight successfully updated!");
                            alert.show();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("An error has occurred!");
                            alert.show();
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    setUserInformation(updated_user_id, updated_username_lbl, updated_users_weigth_txt_fld, updated_users_daily_total_label, updated_users_daily_proteins_label, updated_users_daily_carbohydrates_label, updated_users_daily_fat_label, updated_users_daily_fiber_label);
                }
                else if (!users_weigth_txt_fld.getText().contains(".")){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Use a dot!");
                    alert.show();
                }
                else if (!RegisterController.checkUsingIsDigitMethod(users_weigth_txt_fld.getText())){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Only numbers allowed!");
                    alert.show();
                }
                else {
                    System.out.println("Please fill in all information!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all information to sign up!");
                    alert.show();
                }
            }
        });
        /**
         * заполняем таблицу продуктов
         * и динамический поиск
         */
        getProductTableView();
        /**
         * обработка кнопки добавить новый продукт
         */
        add_new_product_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!add_new_product_txt_fld.getText().trim().isEmpty() && !new_product_calories_dose_txt_fld.getText().trim().isEmpty()
                        && !new_product_proteins_dose_txt_fld.getText().trim().isEmpty() && !new_product_fat_dose_txt_fld.getText().trim().isEmpty()
                        && !new_product_carbohydrates_dose_txt_fld.getText().trim().isEmpty() && !new_product_fiber_dose_txt_fld.getText().trim().isEmpty()){
                    try {
                        boolean res = DBUtils.addNewProduct(add_new_product_txt_fld.getText(), Double.valueOf(new_product_calories_dose_txt_fld.getText()), Double.valueOf(new_product_proteins_dose_txt_fld.getText()), Double.valueOf(new_product_fat_dose_txt_fld.getText()), Double.valueOf(new_product_carbohydrates_dose_txt_fld.getText()), Double.valueOf(new_product_fiber_dose_txt_fld.getText()));
                        if (res){
                            System.out.println("Product successfully added!");
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setContentText("Product successfully added!");
                            alert.show();
                        } else {
                            System.out.println("Product already exists!");
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("Product already exists!");
                            alert.show();
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    add_new_product_txt_fld.setText("");
                    new_product_calories_dose_txt_fld.setText("");
                    new_product_proteins_dose_txt_fld.setText("");
                    new_product_fat_dose_txt_fld.setText("");
                    new_product_carbohydrates_dose_txt_fld.setText("");
                    new_product_fiber_dose_txt_fld.setText("");
                   getProductTableView();
                }
                else if (!RegisterController.checkUsingIsDigitMethod(new_product_calories_dose_txt_fld.getText()) &&
                        !RegisterController.checkUsingIsDigitMethod(new_product_proteins_dose_txt_fld.getText()) &&
                        !RegisterController.checkUsingIsDigitMethod(new_product_fat_dose_txt_fld.getText()) &&
                        !RegisterController.checkUsingIsDigitMethod(new_product_carbohydrates_dose_txt_fld.getText()) &&
                        !RegisterController.checkUsingIsDigitMethod(new_product_fiber_dose_txt_fld.getText())){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Only numbers allowed!");
                    alert.show();
                }
                else if (!new_product_calories_dose_txt_fld.getText().contains(".") &&
                                !new_product_proteins_dose_txt_fld.getText().contains(".") &&
                                !new_product_fat_dose_txt_fld.getText().contains(".") &&
                                !new_product_carbohydrates_dose_txt_fld.getText().contains(".") &&
                                !new_product_fiber_dose_txt_fld.getText().contains(".")){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Use a dot!");
                    alert.show();
                } else {
                    System.out.println("Please fill in all information!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all information to sign up!");
                    alert.show();
                }
            }
        });
        /**
         * обработка кнопки выход из аккаунта
         */
        logout_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ViewFactory.changeSceneFromLogInToCenterBoard(actionEvent, "/fxml/login.fxml", "Log In", null,0, null, null, null, null, null, null);
            }
        });

        setEatenDoseLabelView();

    }
    /**
     * метод который вызывается в классе DBUtils в методе changeSceneFromLogInToCenterBoard
     * используется для того, чтобы при входе в аккаунт
     * данные пользователя отобразились на странице
     */
    public void setUserInformation(int id, String name, Double weight, Double calories, Double proteins, Double carbo, Double fat, Double fiber){
        user_id = id;
        users_daily_total_label.setText(calories.toString());
        users_daily_proteins_label.setText("" + proteins);
        users_daily_carbohydrates_label.setText("" + carbo);
        users_daily_fat_label.setText("" + fat);
        users_daily_fiber_label.setText("" + fiber);
        username_lbl.setText(name);
        users_weigth_txt_fld.setText("" + weight);
    }
    /**
     * переменные хранящие измененные данные о пользователе
     * (при изменении веса происходит пересчет и обновление)
     */
    static int updated_user_id;
    static Double updated_users_daily_total_label;
    static Double updated_users_daily_proteins_label;
    static Double updated_users_daily_carbohydrates_label;
    static Double updated_users_daily_fat_label;
    static Double updated_users_daily_fiber_label;
    static String updated_username_lbl;
    static Double updated_users_weigth_txt_fld;

    /**
     * метод который вызывается в классе DBUtils для того
     * чтобы передать изменненые данные о пользователе
     */
    public static void setUpdatedUserInformation(int id, String name, Double weight, Double calories, Double proteins, Double carbo, Double fat, Double fiber){
        updated_user_id = id;
        updated_users_daily_total_label = calories;
        updated_users_daily_proteins_label = proteins;
        updated_users_daily_carbohydrates_label = carbo;
        updated_users_daily_fat_label = fat;
        updated_users_daily_fiber_label = fiber;
        updated_username_lbl = name;
        updated_users_weigth_txt_fld = weight;
    }

    /**
     * метод который запоминает id выбранного продукта из таблицы
     * и добавляет его название в форму для добавления съеденного продукта
     */
    public void getItem(MouseEvent mouseEvent) {
        index = list_of_products_table_view.getSelectionModel().getSelectedIndex();

        if (index <= -1){
            return;
        }
        add_new_dish_txt_fld.setText(name_can_eat_product_table_column.getCellData(index).toString());

        product_id = DBUtils.getIdByProductName(add_new_dish_txt_fld.getText());
        System.out.println(product_id);
    }
    /**
     * заполняем таблицу продуктов
     * и динамический поиск
     */
    public void getProductTableView(){
        productSearchObservableList = DBUtils.getProductsFromDB();

        name_can_eat_product_table_column.setCellValueFactory(new PropertyValueFactory<>("productName"));
        calorie_dose_can_eat_product_table_column.setCellValueFactory(new PropertyValueFactory<>("product_calories_perg"));
        proteins_dose_can_eat_product_table_column.setCellValueFactory(new PropertyValueFactory<>("product_protein_perg"));
        fat_dose_can_eat_product_table_column.setCellValueFactory(new PropertyValueFactory<>("product_fat_perg"));
        carbohydrates_dose_can_eat_product_table_column.setCellValueFactory(new PropertyValueFactory<>("product_carbs_perg"));
        fiber_dose_can_eat_product_table_column.setCellValueFactory(new PropertyValueFactory<>("product_fiber_perg"));

        list_of_products_table_view.setItems(productSearchObservableList);

        FilteredList<Product> filteredList = new FilteredList<>(productSearchObservableList, b -> true);

        search_product_txt_fld.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(product -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();

                if(product.getProductName().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                } else {
                    return false;
                }
            });

        });

        SortedList<Product> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(list_of_products_table_view.comparatorProperty());
        list_of_products_table_view.setItems(sortedList);
    }

    /**
     * заполняем таблицу съеденных продуктов
     * за день и по id user
     */
    public void getEatenProductTableView(){
        eatenProductSearchObservableList = DBUtils.getMenuFromDB(chosenDate);

        name_ate_product_table_column.setCellValueFactory(new PropertyValueFactory<EatenProduct, String>("product_name"));
        amount_ate_product_table_column.setCellValueFactory(new PropertyValueFactory<EatenProduct, Double>("eaten_product_amount"));
        calories_ate_product_table_column.setCellValueFactory(new PropertyValueFactory<EatenProduct, Double>("eaten_product_calories"));

        users_daily_menu_table_view.setItems(eatenProductSearchObservableList.filtered(eatenProduct -> eatenProduct.getMenu_date().toString().equals(chosenDate)));
        System.out.println(eatenProductSearchObservableList.isEmpty());
    }
    /**
     * заполняем кол-во съеденных кбжу
     * за день и по id user
     * устанавливаем процентаж
     */
    public void setEatenDoseLabelView(){

        Double sumCal = eatenProductSearchObservableList.filtered(eatenProduct -> eatenProduct.getMenu_date().toString().equals(chosenDate)).stream()
                .mapToDouble(x -> x.getEaten_product_calories())
                .sum();
        sumCal = Math.round(sumCal * 100) / 100.0;
        users_current_total_number_label.setText(sumCal.toString());

        Double sumProteins = eatenProductSearchObservableList.filtered(eatenProduct -> eatenProduct.getMenu_date().toString().equals(chosenDate)).stream()
                .mapToDouble(x -> x.getEaten_product_protein())
                .sum();
        sumProteins = Math.round(sumProteins * 100) / 100.0;
        users_current_proteins_number_label.setText(sumProteins.toString());

        Double sumFat = eatenProductSearchObservableList.filtered(eatenProduct -> eatenProduct.getMenu_date().toString().equals(chosenDate)).stream()
                .mapToDouble(x -> x.getEaten_product_fat())
                .sum();
        sumFat = Math.round(sumFat * 100) / 100.0;
        users_current_fat_number_label.setText(sumFat.toString());

        Double sumCarbs = eatenProductSearchObservableList.filtered(eatenProduct -> eatenProduct.getMenu_date().toString().equals(chosenDate)).stream()
                .mapToDouble(x -> x.getEaten_product_carbs())
                .sum();
        sumCarbs = Math.round(sumCarbs * 100) / 100.0;
        users_current_carbohydrates_number_label.setText(sumCarbs.toString());

        Double sumFiber = eatenProductSearchObservableList.filtered(eatenProduct -> eatenProduct.getMenu_date().toString().equals(chosenDate)).stream()
                .mapToDouble(x -> x.getEaten_product_fiber())
                .sum();
        sumFiber = Math.round(sumFiber * 100) / 100.0;
        users_current_fiber_number_label.setText(sumFiber.toString());
    }
}
