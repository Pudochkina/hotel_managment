package com.example.hotel_managment.controllers.client;

import com.example.hotel_managment.models.TypeOfRoom;
import com.example.hotel_managment.views.ViewFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class TypeOfRoomController implements Initializable {

    public TableColumn<TypeOfRoom, String> type_of_room_column, type_of_room_descr_column;
    public TableColumn<TypeOfRoom, Integer> type_of_room_id_column, room_capacity_column, type_of_room_cost_column;
    public TableView<TypeOfRoom> list_of_types_of_rooms_table_view;
    public TextField type_of_room_descr_txt_fld, type_of_room_cost_txt_fld, type_of_room_txt_fld1;
    public Button add_new_type_of_room_btn, update_type_of_room_btn, delete_type_of_room_btn;
    public Spinner<Integer> type_of_room_capacity_spinner;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void getItem(MouseEvent mouseEvent) {
    }
}
