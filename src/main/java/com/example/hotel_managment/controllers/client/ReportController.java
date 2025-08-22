package com.example.hotel_managment.controllers.client;

import com.example.hotel_managment.db.DBUtilsReport;
import com.example.hotel_managment.models.RoomIncome;
import com.example.hotel_managment.models.ServiceIncome;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class ReportController implements Initializable {
    public DatePicker date_first_data_picker, date_second_data_picker;
    public Button apply_btn, reload_btn1;
    public TableView<RoomIncome> rooms_money_tbl_view;
    public Label numbers_sum_txt_fld, service_sum_txt_fld, summary_sum_txt_fld;
    public TableView<ServiceIncome> services_money_tbl_view;
    public Label header_lbl, numbers_sum_lbl, service_sum_lbl, summary_sum_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Установка начальных дат (например, последние 2 месяца)
        date_second_data_picker.setValue(LocalDate.now());
        date_first_data_picker.setValue(LocalDate.now().minusMonths(2));

        initialLoadReportData();

        apply_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                loadReportData();
            }
        });

        reload_btn1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
               initialLoadReportData();
            }
        });
    }

    private void initialLoadReportData() {

        // Загрузка данных по номерам
        List<RoomIncome> roomData = DBUtilsReport.getRoomIncome();
        rooms_money_tbl_view.setItems(FXCollections.observableArrayList(roomData));

        // Загрузка данных по услугам
        List<ServiceIncome> serviceData = DBUtilsReport.getServiceIncome();
        services_money_tbl_view.setItems(FXCollections.observableArrayList(serviceData));

        // Обновление итогов
        updateTotals();
    }

    private void loadReportData() {
        LocalDate startDate = date_first_data_picker.getValue();
        LocalDate endDate = date_second_data_picker.getValue();

        if (startDate == null || endDate == null) {
            showAlert("Ошибка", "Пожалуйста, укажите период отчета");
            return;
        }

        if (startDate.isAfter(endDate)) {
            showAlert("Ошибка", "Дата начала не может быть позже даты окончания");
            return;
        }

        // Обновление заголовка
        header_lbl.setText(String.format("Прибыль отеля за период %s - %s",
                startDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                endDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));

        // Загрузка данных по номерам
        List<RoomIncome> roomData = DBUtilsReport.getRoomIncomeByPeriod(startDate, endDate);
        rooms_money_tbl_view.setItems(FXCollections.observableArrayList(roomData));

        // Загрузка данных по услугам
        List<ServiceIncome> serviceData = DBUtilsReport.getServiceIncomeByPeriod(startDate, endDate);
        services_money_tbl_view.setItems(FXCollections.observableArrayList(serviceData));

        // Обновление итогов
        updateTotals();
    }

    private void updateTotals() {
        double roomsTotal = rooms_money_tbl_view.getItems().stream()
                .mapToDouble(RoomIncome::getIncome)
                .sum();

        double servicesTotal = services_money_tbl_view.getItems().stream()
                .mapToDouble(ServiceIncome::getIncome)
                .sum();

        numbers_sum_lbl.setText(String.format("Итого по номерам: %,.2f ₽", roomsTotal));
        service_sum_lbl.setText(String.format("Итого по услугам: %,.2f ₽", servicesTotal));
        summary_sum_lbl.setText(String.format("Общий доход: %,.2f ₽", roomsTotal + servicesTotal));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private float sumArray(float[] array) {
        float sum = 0;
        for (float value : array) {
            sum += value;
        }
        return sum;
    }
}
