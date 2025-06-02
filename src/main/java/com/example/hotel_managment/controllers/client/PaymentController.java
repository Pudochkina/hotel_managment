package com.example.hotel_managment.controllers.client;

import com.example.hotel_managment.db.DBUtilsGuest;
import com.example.hotel_managment.db.DBUtilsReservation;
import com.example.hotel_managment.db.DBUtilsRoom;
import com.example.hotel_managment.db.DBUtilsService;
import com.example.hotel_managment.models.*;
import com.example.hotel_managment.views.ViewFactory;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PaymentController implements Initializable {

    public TextField guest_fio_txt_fld;
    public DatePicker date_out_picker;
    public DatePicker date_in_picker;
    public Button find_available_rooms_btn;
    public TableView<Room> list_of_rooms_tbl_view;
    public TableColumn<Room, Integer> number_of_room_tbl_clmn;
    public ComboBox<String> type_of_room_combo_box;
    public ComboBox<String> service_combo_box;
    public TableView<TypeOfRoom> list_of_type_rooms_tbl_view;
    public TableColumn<TypeOfRoom, Integer> capacity_of_room_tbl_clmn;
    public TableColumn<TypeOfRoom, Integer> cost_of_room_tbl_clmn;
    public TextField search_reservation_txt_fld;
    public TableView<ReservationUS> list_of_reservations_table_view;
    public TableColumn<ReservationUS, String> guest_fio_column;
    public TableColumn<ReservationUS, String> service_name_column;
    public TableColumn<ReservationUS, Integer> room_number_column;
    public TableColumn<ReservationUS, String> type_of_room_column11;
    public TableColumn<ReservationUS, Date> date_in_column;
    public TableColumn<ReservationUS, Date> date_out_column;
    public TableColumn<ReservationUS, Integer> sum_column1;
    public TableColumn<ReservationUS, Integer> reservation_id_column;
    public TableColumn<ReservationUS, Integer> guest_id_column;
    public Button update_reservation_btn;
    public TextField sum_txt_fld;
    /**
     * id выбранной брони для ее обновления
     */
    private Integer res_id;
    /**
     * id выбранного гостя
     */
    private Integer guest_id;
    /**
     * id комнаты, которую добавляют в бронь
     */
    private Integer room_id;
    /**
     * id услги, которую добавляют в бронь
     */
    private Integer service_id;
    /**
     * переменная для хранения id выбранного гостя из таблицы
     */
    Integer index;
    /**
     * переменная для хранения id выбранной комнаты из таблицы
     */
    Integer roomIndex;
    /**
     * переменная для хранения текущей даты заезда
     */
    java.sql.Date DateIn;
    /**
     * переменная для хранения текущей даты выезда
     */
    java.sql.Date DateOut;
    /**
     * переменная для хранения выбранной даты заезда
     */
    java.sql.Date chosenDateIn;
    /**
     * переменная для хранения выбранной даты выезда
     */
    java.sql.Date chosenDateOut;
    /**
     * лист для хранения списка гостей из бд и их динамического поиска
     */
    ObservableList<ReservationUS> reservationUSSearchObservableList = FXCollections.observableArrayList();
    /**
     * лист для хранения списка типов номеров из бд для комбо бокса
     */
    ObservableList<TypeOfRoom> typeOfRoomSearchObservableList = FXCollections.observableArrayList();
    /**
     * лист для хранения списка услуг из бд
     */
    ObservableList<Service> serviceSearchObservableList = FXCollections.observableArrayList();
    /**
     * лист для хранения списка услуг из бд
     */
    ObservableList<Room> roomNumbersObservableList = FXCollections.observableArrayList();
    /**
     * лист для хранения списка типов номеров с описанием из бд для вставки в таблицу
     */
    ObservableList<TypeOfRoom> typeOfRoomsNumbersObservableList = FXCollections.observableArrayList();
    /**
     * переменная для хранения выбранного типа номеров из комбо-бокса
     */
    String chosenTypeOfRoom;
    /**
     * переменная для хранения выбранной услуги из комбо-бокса
     */
    String chosenService;
    /**
     * переменная для хранения выбранного номера комнаты из таблицы по фильтру
     */
    Integer chosenRoomNumber;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /**
         * заполняем таблицу броней
         * и динамический поиск
         */
        getReservationsTableView();

        /**
         * обработка date piker
         */
        date_in_picker.setValue(LocalDate.now());
        date_out_picker.setValue(LocalDate.now());

        date_in_picker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Date date = Date.from(date_in_picker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                chosenDateIn = new java.sql.Date(date.getTime());
            }
        });

        date_out_picker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Date date = Date.from(date_out_picker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                chosenDateOut = new java.sql.Date(date.getTime());

            }
        });

        /**
         * обработка combo box type of room
         */
        typeOfRoomSearchObservableList = DBUtilsRoom.getTypesOfRoomsFromDB();
        ObservableList<String> type_of_rooms = typeOfRoomSearchObservableList.stream()
                .map(TypeOfRoom::getType_of_room)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        type_of_room_combo_box.setItems(type_of_rooms);
        type_of_room_combo_box.setValue(null);
        type_of_room_combo_box.setPromptText("Выберите тип номера");

        type_of_room_combo_box.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                chosenTypeOfRoom = type_of_room_combo_box.getValue();
                System.out.println(chosenTypeOfRoom);
            }
        });

        /**
         * обработка combo box service
         */
        serviceSearchObservableList = DBUtilsService.getServiceFromDB();
        ObservableList<String> services = serviceSearchObservableList.stream()
                .map(Service::getService_name)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        service_combo_box.setItems(services);
        service_combo_box.setValue(null);
        service_combo_box.setPromptText("Выберите услугу");

        service_combo_box.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                chosenService = service_combo_box.getValue();
                //поиск id выбранной услуги
                service_id = DBUtilsService.getServiceIdByServiceName(chosenService);
                System.out.println(chosenService + "(id: " + service_id + ")");
            }
        });

        /**
         * обработка кнопки "найти номера"
         */
        find_available_rooms_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (chosenDateOut != null && chosenDateIn != null && chosenDateOut.after(chosenDateIn) && chosenTypeOfRoom != null){
                    roomNumbersObservableList = DBUtilsRoom.getFilteredNumberOfRoomFromDB(chosenDateIn, chosenDateOut, chosenTypeOfRoom);
                    typeOfRoomSearchObservableList = DBUtilsRoom.getFilteredTypeOfRoomFromDB(chosenDateIn, chosenDateOut, chosenTypeOfRoom);
                    number_of_room_tbl_clmn.setCellValueFactory(new PropertyValueFactory<>("room_number"));
                    capacity_of_room_tbl_clmn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
                    cost_of_room_tbl_clmn.setCellValueFactory(new PropertyValueFactory<>("cost_per_night"));

                    list_of_rooms_tbl_view.setItems(roomNumbersObservableList);
                    list_of_type_rooms_tbl_view.setItems(typeOfRoomSearchObservableList);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Заполните поля корректными данными!");
                    alert.show();
                }
            }
        });

        // Слушатель для даты заезда
        date_in_picker.valueProperty().addListener((obs, oldVal, newVal) -> updateTotalSum());

        // Слушатель для даты выезда
        date_out_picker.valueProperty().addListener((obs, oldVal, newVal) -> updateTotalSum());

        // Слушатель для услуги
        service_combo_box.valueProperty().addListener((obs, oldVal, newVal) -> updateTotalSum());

        // Слушатель для типа номера
        type_of_room_combo_box.valueProperty().addListener((obs, oldVal, newVal) -> updateTotalSum());

        /**
         * обработка кнопки "обновить бронь"
         */
        update_reservation_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (guest_fio_txt_fld != null
                        && chosenDateOut != null && chosenDateIn != null
                        && chosenDateOut.after(chosenDateIn)
                        && chosenTypeOfRoom != null && chosenRoomNumber != null
                        && chosenService != null) {
                    // Если все поля прошли валидацию, пробуем изменить бронь
                    try {
                        boolean res = DBUtilsReservation.updateReservation(res_id, guest_id, service_id, room_id, chosenDateIn, chosenDateOut);
                        if (res) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setContentText("Бронь успешно изменена!");
                            alert.show();
                            getReservationsTableView();

                            // Очищаем форму
                            clearForm();
                            // Очищаем таблицу доступных номеров
                            clearAvailableRoomsTable();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("Ошибка!");
                            alert.show();
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Заполните поля корректными данными!");
                    alert.show();
                }
            }
        });

    }

    /**
     * заполняем таблицу, содержащую список броней
     * и динамический поиск
     */
    public void getReservationsTableView() {
        reservationUSSearchObservableList = DBUtilsReservation.getReservationsFromDB();

        guest_fio_column.setCellValueFactory(new PropertyValueFactory<>("guest_fio"));
        service_name_column.setCellValueFactory(new PropertyValueFactory<>("service_name"));
        room_number_column.setCellValueFactory(new PropertyValueFactory<>("room_number"));
        type_of_room_column11.setCellValueFactory(new PropertyValueFactory<>("type_of_room"));
        date_in_column.setCellValueFactory(new PropertyValueFactory<>("date_in"));
        date_out_column.setCellValueFactory(new PropertyValueFactory<>("date_out"));
        reservation_id_column.setCellValueFactory(new PropertyValueFactory<>("reservation_id"));
        guest_id_column.setCellValueFactory(new PropertyValueFactory<>("guest_id"));
        sum_column1.setCellValueFactory(new PropertyValueFactory<>("total_sum"));

        list_of_reservations_table_view.setItems(reservationUSSearchObservableList);

        FilteredList<ReservationUS> filteredList = new FilteredList<>(reservationUSSearchObservableList, b -> true);

        search_reservation_txt_fld.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(product -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();

                if (product.getGuest_fio().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else {
                    return false;
                }
            });

        });

        SortedList<ReservationUS> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(list_of_reservations_table_view.comparatorProperty());
        list_of_reservations_table_view.setItems(sortedList);
    }

    /**
     * метод который запоминает id выбранного гостя из таблицы
     * и добавляет его данные в форму для добавления гостя
     */
    public void getItem(MouseEvent mouseEvent) {
        index = list_of_reservations_table_view.getSelectionModel().getSelectedIndex();

        if (index <= -1) {
            return;
        }
        guest_fio_txt_fld.setText(guest_fio_column.getCellData(index));
        date_in_picker.setValue(LocalDate.parse(date_in_column.getCellData(index).toString()));
        date_out_picker.setValue(LocalDate.parse(date_out_column.getCellData(index).toString()));
        type_of_room_combo_box.setValue(type_of_room_column11.getCellData(index));
        service_combo_box.setValue(service_name_column.getCellData(index));

        sum_txt_fld.setText(sum_column1.getCellData(index).toString() + ",00 руб.");

        res_id = reservation_id_column.getCellData(index);
        guest_id = guest_id_column.getCellData(index);
        service_id = DBUtilsService.getServiceIdByServiceName(service_name_column.getCellData(index));
        DateIn = java.sql.Date.valueOf(date_in_column.getCellData(index).toString());
        DateOut = java.sql.Date.valueOf(date_out_column.getCellData(index).toString());
        System.out.println(DateIn + "-" + DateOut + ", " + res_id);
    }

    /**
     * метод который запоминает id выбранного номера комнаты из таблицы
     * и возвращает номер комнаты для добавления брони в таблицу
     */
    public void getRoomNumber(MouseEvent mouseEvent) {
        roomIndex = list_of_rooms_tbl_view.getSelectionModel().getSelectedIndex();

        if (roomIndex <= -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Выберите номер комнаты!");
            alert.show();
        }
        chosenRoomNumber = Integer.parseInt(number_of_room_tbl_clmn.getCellData(roomIndex).toString());

        //String phone_txt_fld = (guest_phone_column.getCellData(index).toString());
        room_id = DBUtilsRoom.getRoomIdByRoomNumber(chosenRoomNumber);
        System.out.println("id выбранной комнаты: " + room_id);
    }

    private void updateTotalSum() {
        if (date_in_picker.getValue() == null || date_out_picker.getValue() == null
                || type_of_room_combo_box.getValue() == null) {
            return;  // Не все данные выбраны
        }

        try {
            // Получаем выбранные даты
            LocalDate checkInDate = date_in_picker.getValue();
            LocalDate checkOutDate = date_out_picker.getValue();

            // Проверка, что дата выезда после даты заезда
            if (checkOutDate.isBefore(checkInDate) || checkOutDate.isEqual(checkInDate)) {
                sum_txt_fld.setText("Ошибка в датах");
                return;
            }

            // Получаем количество дней
            long days = ChronoUnit.DAYS.between(checkInDate, checkOutDate);

            // Получаем цену номера (из БД или локального кэша)
            double roomPrice = DBUtilsRoom.getRoomPriceByType(type_of_room_combo_box.getValue());

            // Получаем цену услуги (если выбрана)
            double servicePrice = 0;
            if (service_combo_box.getValue() != null) {
                servicePrice = DBUtilsService.getServicePriceByName(service_combo_box.getValue());
            }

            // Рассчитываем итоговую сумму
            double totalSum = days * (roomPrice + servicePrice);

            // Обновляем поле суммы
            sum_txt_fld.setText(String.format("%.2f руб.", totalSum));

        } catch (Exception e) {
            sum_txt_fld.setText("Ошибка расчёта");
        }
    }

    // Метод для очистки формы
    private void clearForm() {
        guest_fio_txt_fld.setText("");
        service_combo_box.setValue(null);
        service_combo_box.setPromptText("Выберите услугу");
        type_of_room_combo_box.setValue(null);
        type_of_room_combo_box.setPromptText("Выберите тип номера");
        date_out_picker.setValue(LocalDate.now());
        date_in_picker.setValue(LocalDate.now());
        sum_txt_fld.setText("");
        number_of_room_tbl_clmn.setText("");
    }

    // Метод для очистки таблицы доступных номеров
    private void clearAvailableRoomsTable() {
        number_of_room_tbl_clmn.setCellValueFactory(null);
        capacity_of_room_tbl_clmn.setCellValueFactory(null);
        cost_of_room_tbl_clmn.setCellValueFactory(null);

        // Если у вас есть ObservableList для этой таблицы
        if (roomNumbersObservableList != null) {
            roomNumbersObservableList.clear();
        }

        // Или если вы используете TableView напрямую
        if (typeOfRoomSearchObservableList != null) {
            typeOfRoomSearchObservableList.clear();
        }
    }
}
