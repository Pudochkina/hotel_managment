package com.example.hotel_managment.controllers.client;

import com.example.hotel_managment.db.DBUtilsGuest;
import com.example.hotel_managment.db.DBUtilsService;
import com.example.hotel_managment.models.Guest;
import com.example.hotel_managment.models.Service;
import com.example.hotel_managment.utils.FieldValidator;
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

public class ServiceController implements Initializable {
    public TextField search_service_txt_fld, add_new_service_txt_fld, new_service_type_txt_fld, new_service_cost_txt_fld;
    public TableView<Service> list_of_services_table_view;
    public TableColumn<Service, String> service_name_column, service_type_column;
    public TableColumn<Service, Integer> service_cost_column, service_id_column;
    public Button add_new_service_btn, update_service_btn, delete_service_btn;
    /**
     * переменная для хранения id выбранного гостя из таблицы
     */
    Integer index;
    /**
     * id гостя, для отображения данных о госте в форме
     */
    private Integer service_id;
    /**
     * лист для хранения списка услуг из бд и их динамического поиска
     */
    ObservableList<Service> serviceSearchObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /**
         * заполняем таблицу услуги динамический поиск
         */
        getServicesTableView();

        /**
         * обработка кнопки добавить новоую услугу
         */
        add_new_service_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Сначала проверяем валидность каждого поля по отдельности
                if (!FieldValidator.isValidText(add_new_service_txt_fld.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Введите название услуги!");
                    alert.show();
                    return;
                }
                if (!FieldValidator.isValidText(new_service_type_txt_fld.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Введите тип услуги!");
                    alert.show();
                    return;
                }
                if (!FieldValidator.isValidNumber(new_service_cost_txt_fld.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Введите стоимость услуги!");
                    alert.show();
                    return;
                }

                // Если все поля прошли валидацию, пробуем добавить гостя
               try {
                   boolean res = DBUtilsService.addNewService(add_new_service_txt_fld.getText(),
                           Double.valueOf(new_service_cost_txt_fld.getText()), new_service_type_txt_fld.getText());

                    if (res) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Услуга успешно добавлен!");
                        alert.show();

                        // Очищаем поля
                        clearForm();
                        getServicesTableView();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Услуга уже существует!");
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
        update_service_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Сначала проверяем валидность каждого поля по отдельности
                if (!FieldValidator.isValidText(add_new_service_txt_fld.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Введите название услуги!");
                    alert.show();
                    return;
                }
                if (!FieldValidator.isValidText(new_service_type_txt_fld.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Введите тип услуги!");
                    alert.show();
                    return;
                }
                if (!FieldValidator.isValidNumber(new_service_cost_txt_fld.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Введите стоимость услуги!");
                    alert.show();
                    return;
                }
                if (service_id != null){
                // Если все поля прошли валидацию, пробуем добавить гостя
                try {
                    boolean res = DBUtilsService.updateService(service_id, add_new_service_txt_fld.getText(),
                            Double.valueOf(new_service_cost_txt_fld.getText()), new_service_type_txt_fld.getText());

                    if (res) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Информация об услуге успешно обновлена!");
                        alert.show();
                        getServicesTableView();
                        // Очищаем поля
                        clearForm();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Услуга уже существует!");
                        alert.show();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Выберите услугу из списка!");
                    alert.show();
                }
            }
        });

        /**
         * обработка кнопки удалить услугу
         */
        delete_service_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Сначала проверяем валидность каждого поля по отдельности
                if (!FieldValidator.isValidText(add_new_service_txt_fld.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Введите название услуги!");
                    alert.show();
                    return;
                }
                if (!FieldValidator.isValidText(new_service_type_txt_fld.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Введите тип услуги!");
                    alert.show();
                    return;
                }
                if (!FieldValidator.isValidNumber(new_service_cost_txt_fld.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Введите стоимость услуги!");
                    alert.show();
                    return;
                }

                // Если все поля прошли валидацию, пробуем добавить гостя
               try {
                    boolean res = DBUtilsService.deleteService(service_id);
                    if (res) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Информация об услуге успешно удалена!");
                        alert.show();

                        // Очищаем поля
                        clearForm();
                        getServicesTableView();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Услуга не существует!");
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
    public void getServicesTableView() {
        serviceSearchObservableList = DBUtilsService.getServiceFromDB();

        service_id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        service_name_column.setCellValueFactory(new PropertyValueFactory<>("service_name"));
        service_cost_column.setCellValueFactory(new PropertyValueFactory<>("service_cost"));
        service_type_column.setCellValueFactory(new PropertyValueFactory<>("service_type"));

        list_of_services_table_view.setItems(serviceSearchObservableList);

        FilteredList<Service> filteredList = new FilteredList<>(serviceSearchObservableList, b -> true);

        search_service_txt_fld.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(product -> {
                if (newValue.isEmpty() || newValue.isBlank()) {
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();

                if (product.getService_name().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else {
                    return false;
                }
            });

        });

        SortedList<Service> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(list_of_services_table_view.comparatorProperty());
        list_of_services_table_view.setItems(sortedList);
    }

    /**
     * метод который запоминает id выбранного гостя из таблицы
     * и добавляет его данные в форму для добавления гостя
     */
    public void getItem(MouseEvent mouseEvent) {
        index = list_of_services_table_view.getSelectionModel().getSelectedIndex();

        if (index <= -1) {
            return;
        }
        add_new_service_txt_fld.setText(service_name_column.getCellData(index));
        new_service_type_txt_fld.setText(service_type_column.getCellData(index));
        new_service_cost_txt_fld.setText(service_cost_column.getCellData(index).toString());

        service_id = service_id_column.getCellData(index);
        System.out.println("service id: " + service_id);
    }

    /**
     *  Метод для очистки формы
     */
    private void clearForm() {
        add_new_service_txt_fld.setText("");
        new_service_type_txt_fld.setText("");
        new_service_cost_txt_fld.setText("");
        service_id = null;
    }
}
