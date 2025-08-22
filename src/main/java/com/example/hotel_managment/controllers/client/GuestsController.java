package com.example.hotel_managment.controllers.client;

import com.example.hotel_managment.db.DBUtilsGuest;
import com.example.hotel_managment.db.DBUtilsSecurity;
import com.example.hotel_managment.utils.FieldValidator;
import com.example.hotel_managment.models.Guest;
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
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class GuestsController implements Initializable {
    public TextField search_guest_txt_fld, add_new_fio_txt_fld, new_phone_txt_fld, new_email_txt_fld, new_passport_txt_fld;
    public TableView<Guest> list_of_guests_table_view;
    public TableColumn<Guest, String> guest_fio_column, guest_phone_column, guest_email_column, guest_passport_column;
    public TableColumn<Guest, java.sql.Date> guest_date_birth_column;
    public TableColumn<Guest, Integer> guest_id_column;
    public Button add_new_guest_btn, update_guest_btn, delete_guest_btn;
    public DatePicker calendar_date_picker;

    /**
     * id гостя, для отображения данных о госте в форме
     */
    private Integer guest_id;

    /**
     * переменная для хранения id выбранного гостя из таблицы
     */
    Integer index;

    /**
     * переменная для хранения выбранной даты
     */
    java.sql.Date chosenDate;

    /**
     * лист для хранения списка гостей из бд и их динамического поиска
     */
    ObservableList<Guest> guestSearchObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (DBUtilsSecurity.retrRole.equals("MANAGER")){
            delete_guest_btn.setVisible(false);
        }
        /**
         * заполняем таблицу гостей
         * и динамический поиск
         */
        getGuestsTableView();

        /**
         * обработка date piker
         */
        calendar_date_picker.setValue(LocalDate.now());

        calendar_date_picker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Date date = Date.from(calendar_date_picker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                chosenDate = new java.sql.Date(date.getTime());
            }
        });

        /**
         * обработка кнопки добавить нового гостя
         */
        add_new_guest_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Сначала проверяем валидность каждого поля по отдельности
                if (!FieldValidator.isValidFio(add_new_fio_txt_fld.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("ФИО должно содержать 2-3 слова с заглавной буквы!");
                    alert.show();
                    return;
                }

                if (!FieldValidator.isValidPhone(new_phone_txt_fld.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Телефон должен быть в формате +7XXXXXXXXXX!");
                    alert.show();
                    return;
                }

                if (!FieldValidator.isValidEmail(new_email_txt_fld.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Email должен быть в формате example@domain.com!");
                    alert.show();
                    return;
                }

                if (!FieldValidator.isValidPassport(new_passport_txt_fld.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Паспорт должен быть в формате 1234 567890!");
                    alert.show();
                    return;
                }

                // Если все поля прошли валидацию, пробуем добавить гостя
                try {
                    boolean res = DBUtilsGuest.addNewGuest(
                            add_new_fio_txt_fld.getText(),
                            new_phone_txt_fld.getText(),
                            new_email_txt_fld.getText(),
                            chosenDate,
                            new_passport_txt_fld.getText()
                    );

                    if (res) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Гость успешно добавлен!");
                        alert.show();

                        // Очищаем поля
                        add_new_fio_txt_fld.setText("");
                        new_phone_txt_fld.setText("");
                        new_email_txt_fld.setText("");
                        new_passport_txt_fld.setText("");
                        calendar_date_picker.setValue(LocalDate.now());

                        getGuestsTableView();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Гость уже существует!");
                        alert.show();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        /**
         * обработка кнопки обновить информацию о госте
         */
        update_guest_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Сначала проверяем валидность каждого поля по отдельности
                if (!FieldValidator.isValidFio(add_new_fio_txt_fld.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("ФИО должно содержать 2-3 слова с заглавной буквы!");
                    alert.show();
                    return;
                }

                if (!FieldValidator.isValidPhone(new_phone_txt_fld.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Телефон должен быть в формате +7XXXXXXXXXX!");
                    alert.show();
                    return;
                }

                if (!FieldValidator.isValidEmail(new_email_txt_fld.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Email должен быть в формате example@domain.com!");
                    alert.show();
                    return;
                }

                if (!FieldValidator.isValidPassport(new_passport_txt_fld.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Паспорт должен быть в формате 1234 567890!");
                    alert.show();
                    return;
                }

                // Если все поля прошли валидацию, пробуем добавить гостя
                try {
                    boolean res = DBUtilsGuest.updateGuest(guest_id, add_new_fio_txt_fld.getText(),
                            new_phone_txt_fld.getText(), new_email_txt_fld.getText(), chosenDate, new_passport_txt_fld.getText()
                    );
                    System.out.println(new_phone_txt_fld.getText());
                    if (res) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Информация о госте успешно обновлена!");
                        alert.show();
                        getGuestsTableView();
                        // Очищаем поля
                        add_new_fio_txt_fld.setText("");
                        new_phone_txt_fld.setText("");
                        new_email_txt_fld.setText("");
                        new_passport_txt_fld.setText("");
                        calendar_date_picker.setValue(LocalDate.now());
                        guest_id = null;
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Гость уже существует!");
                        alert.show();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        /**
         * обработка кнопки удалить гостя
         */
        delete_guest_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Сначала проверяем валидность каждого поля по отдельности
                if (!FieldValidator.isValidFio(add_new_fio_txt_fld.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("ФИО должно содержать 2-3 слова с заглавной буквы!");
                    alert.show();
                    return;
                }

                if (!FieldValidator.isValidPhone(new_phone_txt_fld.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Телефон должен быть в формате +7XXXXXXXXXX!");
                    alert.show();
                    return;
                }

                if (!FieldValidator.isValidEmail(new_email_txt_fld.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Email должен быть в формате example@domain.com!");
                    alert.show();
                    return;
                }

                if (!FieldValidator.isValidPassport(new_passport_txt_fld.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Паспорт должен быть в формате 1234 567890!");
                    alert.show();
                    return;
                }

                // Если все поля прошли валидацию, пробуем добавить гостя
                try {
                    boolean res = DBUtilsGuest.deleteGuest(guest_id);
                    if (res) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Информация о госте успешно удалена!");
                        alert.show();

                        // Очищаем поля
                        add_new_fio_txt_fld.setText("");
                        new_phone_txt_fld.setText("");
                        new_email_txt_fld.setText("");
                        new_passport_txt_fld.setText("");
                        calendar_date_picker.setValue(LocalDate.now());
                        guest_id = null;
                        getGuestsTableView();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Гость не существует!");
                        alert.show();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * заполняем таблицу гостей
     * и динамический поиск
     */
    public void getGuestsTableView() {
        guestSearchObservableList = DBUtilsGuest.getGuestsFromDB();

        guest_fio_column.setCellValueFactory(new PropertyValueFactory<>("guestFIO"));
        guest_phone_column.setCellValueFactory(new PropertyValueFactory<>("guest_phone"));
        guest_email_column.setCellValueFactory(new PropertyValueFactory<>("guest_email"));
        guest_date_birth_column.setCellValueFactory(new PropertyValueFactory<>("guest_date_birth"));
        guest_passport_column.setCellValueFactory(new PropertyValueFactory<>("guest_passport"));

        list_of_guests_table_view.setItems(guestSearchObservableList);

        FilteredList<Guest> filteredList = new FilteredList<>(guestSearchObservableList, b -> true);

        search_guest_txt_fld.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(product -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();

                if (product.getGuestFIO().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else {
                    return false;
                }
            });

        });

        SortedList<Guest> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(list_of_guests_table_view.comparatorProperty());
        list_of_guests_table_view.setItems(sortedList);
    }

    /**
     * метод который запоминает id выбранного гостя из таблицы
     * и добавляет его данные в форму для добавления гостя
     */
    public void getItem(MouseEvent mouseEvent) {
        index = list_of_guests_table_view.getSelectionModel().getSelectedIndex();

        if (index <= -1) {
            return;
        }
        add_new_fio_txt_fld.setText(guest_fio_column.getCellData(index));
        new_phone_txt_fld.setText(guest_phone_column.getCellData(index));
        new_email_txt_fld.setText(guest_email_column.getCellData(index));
        new_passport_txt_fld.setText(guest_passport_column.getCellData(index));

        java.sql.Date utilDate = guest_date_birth_column.getCellData(index);
        LocalDate date = utilDate.toLocalDate();
        calendar_date_picker.setValue(date);

        guest_id = DBUtilsGuest.getIdByGuestPhone(new_phone_txt_fld.getText());
        System.out.println(guest_id);
    }
}
