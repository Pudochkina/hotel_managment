package com.example.hotel_managment.controllers.client;

import com.example.hotel_managment.db.DBUtilsRoom;
import com.example.hotel_managment.db.DBUtilsService;
import com.example.hotel_managment.models.TypeOfRoom;
import com.example.hotel_managment.utils.FieldValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TypeOfRoomController implements Initializable {

    public TableColumn<TypeOfRoom, String> type_of_room_column, type_of_room_descr_column, type_of_room_id_column;
    public TableColumn<TypeOfRoom, Integer> room_capacity_column, type_of_room_cost_column;
    public TableView<TypeOfRoom> list_of_types_of_rooms_table_view;
    public TextField type_of_room_descr_txt_fld, type_of_room_cost_txt_fld, type_of_room_txt_fld1;
    public Button add_new_type_of_room_btn, update_type_of_room_btn, delete_type_of_room_btn;
    public Spinner<Integer> type_of_room_capacity_spinner;
    SpinnerValueFactory<Integer> spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,10);
    /**
     * переменная для хранения id выбранного гостя из таблицы
     */
    Integer index;
    /**
     * id гостя, для отображения данных о типе номеров в форме
     */
    private String type_of_room_id;
    /**
     * лист для хранения списка услуг из бд и их динамического поиска
     */
    ObservableList<TypeOfRoom> typeOfRoomSearchObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        spinnerValueFactory.setValue(2);
        type_of_room_capacity_spinner.setValueFactory(spinnerValueFactory);

        /**
         * заполняем таблицу услуги динамический поиск
         */
        getTypesTableView();

        /**
         * обработка кнопки добавить новоую услугу
         */
        add_new_type_of_room_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Сначала проверяем валидность каждого поля по отдельности
                if (!FieldValidator.isValidText(type_of_room_txt_fld1.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Введите название типа!");
                    alert.show();
                    return;
                }
                if (!FieldValidator.isValidText(type_of_room_descr_txt_fld.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Введите описание типа!");
                    alert.show();
                    return;
                }
                if (!FieldValidator.isValidNumber(type_of_room_cost_txt_fld.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Введите стоимость типа!");
                    alert.show();
                    return;
                }

                // Если все поля прошли валидацию, пробуем добавить гостя
                try {
                    boolean res = DBUtilsRoom.addNewTypeOfRoom(type_of_room_txt_fld1.getText(), type_of_room_capacity_spinner.getValue(),
                            Double.valueOf(type_of_room_cost_txt_fld.getText()), type_of_room_descr_txt_fld.getText());
                    if (res) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Тип номера успешно добавлен!");
                        alert.show();
                        // Очищаем поля
                        clearForm();
                        getTypesTableView();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Тип номера уже существует!");
                        alert.show();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        /**
         * обработка кнопки обновить информацию об услуге
         */
        update_type_of_room_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Сначала проверяем валидность каждого поля по отдельности
                if (!FieldValidator.isValidText(type_of_room_txt_fld1.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Введите название типа!");
                    alert.show();
                    return;
                }
                if (!FieldValidator.isValidText(type_of_room_descr_txt_fld.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Введите описание типа!");
                    alert.show();
                    return;
                }
                if (!FieldValidator.isValidNumber(type_of_room_cost_txt_fld.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Введите стоимость типа!");
                    alert.show();
                    return;
                }
                if (type_of_room_id != null){
                    // Если все поля прошли валидацию, пробуем добавить гостя
                    try {
                        boolean res = DBUtilsRoom.updateTypeOfRoom(type_of_room_id, type_of_room_capacity_spinner.getValue(),
                                Double.valueOf(type_of_room_cost_txt_fld.getText()), type_of_room_descr_txt_fld.getText());
                        if (res) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setContentText("Информация о типе номеров успешно обновлена!");
                            alert.show();
                            getTypesTableView();
                            // Очищаем поля
                            clearForm();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("Тип номера уже существует!");
                            alert.show();
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Выберите тип номера из списка!");
                    alert.show();
                }
            }
        });

        /**
         * обработка кнопки удалить услугу
         */
        delete_type_of_room_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Сначала проверяем валидность каждого поля по отдельности
                if (!FieldValidator.isValidText(type_of_room_txt_fld1.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Введите название типа!");
                    alert.show();
                    return;
                }
                if (!FieldValidator.isValidText(type_of_room_descr_txt_fld.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Введите описание типа!");
                    alert.show();
                    return;
                }
                if (!FieldValidator.isValidNumber(type_of_room_cost_txt_fld.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Введите стоимость типа!");
                    alert.show();
                    return;
                }

                // Если все поля прошли валидацию, пробуем добавить гостя
                try {
                    boolean res = DBUtilsRoom.deleteTypeOfRoom(type_of_room_id);
                    if (res) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Информация о типе номеров успешно удалена!");
                        alert.show();

                        // Очищаем поля
                        clearForm();
                        getTypesTableView();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Тип номера не существует!");
                        alert.show();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * заполняем таблицу услуги динамический поиск
     */
    public void getTypesTableView() {
        typeOfRoomSearchObservableList = DBUtilsRoom.getTypesOfRoomsFromDB();

        type_of_room_column.setCellValueFactory(new PropertyValueFactory<>("type_of_room"));
        room_capacity_column.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        type_of_room_cost_column.setCellValueFactory(new PropertyValueFactory<>("cost_per_night"));
        type_of_room_descr_column.setCellValueFactory(new PropertyValueFactory<>("description"));

        list_of_types_of_rooms_table_view.setItems(typeOfRoomSearchObservableList);
    }

    /**
     * метод который запоминает id выбранного гостя из таблицы
     * и добавляет его данные в форму для добавления гостя
     */
    public void getItem(MouseEvent mouseEvent) {
        index = list_of_types_of_rooms_table_view.getSelectionModel().getSelectedIndex();

        if (index <= -1) {
            return;
        }
        type_of_room_txt_fld1.setText(type_of_room_column.getCellData(index));
        type_of_room_descr_txt_fld.setText(type_of_room_descr_column.getCellData(index));
        type_of_room_cost_txt_fld.setText(type_of_room_cost_column.getCellData(index).toString());
        spinnerValueFactory.setValue(room_capacity_column.getCellData(index));
        type_of_room_capacity_spinner.setValueFactory(spinnerValueFactory);
        type_of_room_id = type_of_room_column.getCellData(index);
        System.out.println("type of room: " + type_of_room_id);
    }

    /**
     *  Метод для очистки формы
     */
    private void clearForm() {
        type_of_room_txt_fld1.setText("");
        type_of_room_descr_txt_fld.setText("");
        type_of_room_cost_txt_fld.setText("");
        type_of_room_id = null;
    }
}
