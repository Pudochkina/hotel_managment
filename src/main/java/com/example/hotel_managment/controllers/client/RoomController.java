package com.example.hotel_managment.controllers.client;

import com.example.hotel_managment.db.DBUtilsRoom;
import com.example.hotel_managment.models.Room;
import com.example.hotel_managment.models.TypeOfRoom;
import com.example.hotel_managment.utils.FieldValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class RoomController implements Initializable {
    public ComboBox<String> type_of_room_combo_box;
    public TableView<Room> list_of_rooms_table_view;
    public TableColumn<Room, Integer> room_number_column, room_id_column, type_of_room_id_column;
    public TableColumn<Room, String> type_of_room_descr_column, room_status_column;
    public TextField add_number_of_room_txt_fld;
    public Button add_new_room_btn, delete_room_btn, update_room_btn;
    public ComboBox<String> type_of_room_combo_box1, room_status_combo_box;
    /**
     * id гостя, для отображения данных о госте в форме
     */
    private Integer room_id;
    /**
     * переменная для хранения id выбранной комнаты из таблицы
     */
    Integer index;
    /**
     * лист для хранения списка комнат из бд
     */
    ObservableList<Room> roomSearchObservableList = FXCollections.observableArrayList();
    /**
     * лист для хранения списка типов номеров из бд для комбо бокса
     */
    ObservableList<TypeOfRoom> typeOfRoomSearchObservableList = FXCollections.observableArrayList();
    /**
     * переменная для хранения выбранного типа номеров из комбо-бокса
     */
    String chosenTypeOfRoom;
    /**
     * переменная для хранения выбранной услуги из комбо-бокса
     */
    String chosenStatus;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        getRoomsTableView();
        room_status_combo_box.getItems().addAll("Свободен", "Занят", "На ремонте", "На уборке");

        /**
         * обработка combo box type of room и сортировка номеров по типам
         */
        type_of_room_combo_box.valueProperty().addListener((observable, oldValue, newValue) -> {
            filterRoomsByType(newValue);
        });

        /**
         * обработка combo box type of room
         */
        typeOfRoomSearchObservableList = DBUtilsRoom.getTypesOfRoomsFromDB();
        ObservableList<String> type_of_rooms = typeOfRoomSearchObservableList.stream()
                .map(TypeOfRoom::getType_of_room)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        type_of_room_combo_box1.setItems(type_of_rooms);
        type_of_room_combo_box.setItems(type_of_rooms);
        type_of_room_combo_box1.setValue(null);
        type_of_room_combo_box1.setPromptText("Выберите тип номера");

        /**
         * обработка combo box type of room
         */
        type_of_room_combo_box1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                chosenTypeOfRoom = type_of_room_combo_box1.getValue();
            }
        });

        /**
         * обработка combo box room status
         */
        room_status_combo_box.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                chosenStatus = room_status_combo_box.getValue();
            }
        });

        /**
         * обработка кнопки добавить новую комнату
         */
        add_new_room_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Сначала проверяем валидность каждого поля по отдельности
                if (!FieldValidator.isValidNumber(add_number_of_room_txt_fld.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Номер комнаты не должен начинаться с нуля!");
                    alert.show();
                    return;
                }
                if (chosenStatus == null || chosenTypeOfRoom == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Ошибка!");
                    alert.show();
                    return;
                }
                // Если все поля прошли валидацию, пробуем добавить гостя
                try {
                    boolean res = DBUtilsRoom.addNewRoom(Integer.valueOf(add_number_of_room_txt_fld.getText()),
                                    chosenTypeOfRoom, chosenStatus);
                    if (res) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Номер успешно добавлен!");
                        alert.show();
                        // Очищаем поля
                        clearForm();
                        getRoomsTableView();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Комната уже существует!");
                        alert.show();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        /**
         * обработка кнопки обновить информацию о номере
         */
        update_room_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Сначала проверяем валидность каждого поля по отдельности
                if (!FieldValidator.isValidNumber(add_number_of_room_txt_fld.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Номер комнаты не должен начинаться с нуля!");
                    alert.show();
                    return;
                }
                if (chosenStatus == null || chosenTypeOfRoom == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Ошибка!");
                    alert.show();
                    return;
                }

                // Если все поля прошли валидацию, пробуем добавить гостя
                try {
                    boolean res = DBUtilsRoom.updateRoom(room_id, Integer.valueOf(add_number_of_room_txt_fld.getText()),
                            chosenTypeOfRoom, chosenStatus);
                    if (res) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Информация о комнате успешно обновлена!");
                        alert.show();
                        getRoomsTableView();
                        room_id = null;
                        // Очищаем поля
                        clearForm();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Комната уже существует!");
                        alert.show();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        /**
         * обработка кнопки удалить комнату
         */
        delete_room_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Сначала проверяем валидность каждого поля по отдельности
                if (room_id == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Выберите комнату!");
                    alert.show();
                    return;
                }
                // Если все поля прошли валидацию, пробуем удалить гостя
                try {
                    boolean res = DBUtilsRoom.deleteRoom(room_id);
                    if (res) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Информация о номере успешно удалена!");
                        alert.show();
                        room_id = null;
                        // Очищаем поля
                        clearForm();
                        getRoomsTableView();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Комната не существует!");
                        alert.show();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * заполняем таблицу комнаты
     */
    public void getRoomsTableView() {
        roomSearchObservableList = DBUtilsRoom.getRoomFromDB();

        room_number_column.setCellValueFactory(new PropertyValueFactory<>("room_number"));
        type_of_room_descr_column.setCellValueFactory(new PropertyValueFactory<>("type_of_room"));
        room_status_column.setCellValueFactory(new PropertyValueFactory<>("room_status"));
        room_id_column.setCellValueFactory(new PropertyValueFactory<>("id"));

        list_of_rooms_table_view.setItems(roomSearchObservableList);

        filterRoomsByType(type_of_room_combo_box.getValue());
    }

    /**
     * метод который запоминает id выбранной комнаты  из таблицы
     * и добавляет его данные в форму для добавления комнаты
     */
    public void getItem(MouseEvent mouseEvent) {
        index = list_of_rooms_table_view.getSelectionModel().getSelectedIndex();

        if (index <= -1) {
            return;
        }
        add_number_of_room_txt_fld.setText(room_number_column.getCellData(index).toString());
        room_status_combo_box.setValue(room_status_column.getCellData(index));
        type_of_room_combo_box1.setValue(type_of_room_descr_column.getCellData(index));
        room_id = room_id_column.getCellData(index);
        System.out.println("id комнаты: " + room_id);
    }

    /**
     *  Метод для фильтрации комнат по типу
     */
    private void filterRoomsByType(String roomType) {
        if (roomType == null || "Все".equals(roomType)) {
            // Если выбрано "Все" или значение null - показываем все комнаты
            list_of_rooms_table_view.setItems(roomSearchObservableList);
        } else {
            // Создаем отфильтрованный список
            FilteredList<Room> filteredData = new FilteredList<>(roomSearchObservableList);

            // Устанавливаем предикат для фильтрации
            filteredData.setPredicate(room -> {
                // Сравниваем тип комнаты с выбранным значением
                return roomType.equals(room.getType_of_room());
            });

            // Устанавливаем отфильтрованный список в таблицу
            list_of_rooms_table_view.setItems(filteredData);
        }
    }


    /**
     *  Метод для очистки формы
     */
    private void clearForm() {
        add_number_of_room_txt_fld.setText("");
        type_of_room_combo_box1.setValue(null);
        type_of_room_combo_box1.setPromptText("Выберите тип номера");
        room_status_combo_box.setValue(null);
        room_status_combo_box.setPromptText("Выберите статус номера");
    }
}
