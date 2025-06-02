package com.example.hotel_managment.controllers.client;

import com.example.hotel_managment.models.Room;
import com.example.hotel_managment.views.ViewFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class RoomController implements Initializable {
    public ComboBox<String> type_of_room_combo_box;
    public TableView<Room> list_of_rooms_table_view;
    public TableColumn<Room, Integer> room_number_column, room_id_column, type_of_room_id_column;
    public TableColumn<Room, String> type_of_room_descr_column, room_status_column;
    public TextField type_of_room_txt_fld, room_status_txt_fld, add_number_of_room_txt_fld;
    public Button add_new_room_btn, delete_room_btn, update_room_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }

    public void getItem(MouseEvent mouseEvent) {
    }
}
