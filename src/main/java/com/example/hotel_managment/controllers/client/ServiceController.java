package com.example.hotel_managment.controllers.client;

import com.example.hotel_managment.models.Service;
import com.example.hotel_managment.views.ViewFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ServiceController implements Initializable {
    public TextField search_service_txt_fld, add_new_service_txt_fld, new_service_type_txt_fld, new_service_cost_txt_fld;
    public TableView<Service> list_of_services_table_view;
    public TableColumn<Service, String> service_name_column, service_type_column;
    public TableColumn<Service, Integer> service_cost_column, service_id_column;
    public Button add_new_service_btn, update_service_btn, delete_service_btn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void getItem(MouseEvent mouseEvent) {
    }


}
