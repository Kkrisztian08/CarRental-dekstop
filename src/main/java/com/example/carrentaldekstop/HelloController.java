package com.example.carrentaldekstop;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.Optional;

public class HelloController {

    @FXML
    private TableView<Car> cars;
    @FXML
    private TableColumn<Car,Integer> cost;
    @FXML
    private TableColumn<Car,String> model;
    @FXML
    private TableColumn<Car,String> brand;
    @FXML
    private TableColumn<Car,String> numberPlate;
    CarDB db;

    public void initialize(){
        numberPlate.setCellValueFactory(new PropertyValueFactory<>("license_plate_number"));
        brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        model.setCellValueFactory(new PropertyValueFactory<>("model"));
        cost.setCellValueFactory(new PropertyValueFactory<>("daily_cost"));

        try {
            db=new CarDB();
            listaz();
        } catch (SQLException e) {
            Platform.runLater(()->{
                alert("Hiba lépett fel kapcsolódás során. A program leáll");
                Platform.exit();
            });
        }
    }



    private void listaz() throws SQLException {
        cars.getItems().clear();
        cars.getItems().addAll(db.getCars());

    }

    @FXML
    public void onDeleteCar(ActionEvent actionEvent) {
        if (cars.getSelectionModel().getSelectedIndex()<0){
            informacio("Elöbb válasszon egy elemet a táblázatból");
            return;
        }
        Car torlendo=cars.getSelectionModel().getSelectedItem();
        if (!confirm("Biztos szeretné törölni a kiválaszott elemet?")){
            return;
        }
        try {
            if (db.deleteCar(torlendo)) {
                System.out.println("Sikeres törlés");
            }else {
                System.out.println("Sikertelen törlés");
            }
            listaz();
        } catch (SQLException e) {
            hibakiir(e);
        }


    }

    private void hibakiir(SQLException e) {
        Alert alert=new Alert(Alert.AlertType.ERROR);
        alert.setTitle(e.getClass().toString());
        alert.setHeaderText(e.getMessage());
        alert.setContentText(e.getLocalizedMessage());
        alert.showAndWait();

    }

    private boolean confirm(String s) {
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Biztos?");
        alert.setHeaderText(s);
        Optional<ButtonType> result=alert.showAndWait();
        return result.isPresent() && result.get().equals(ButtonType.OK);
    }

    private void alert(String s) {
        Alert alert=new Alert(Alert.AlertType.NONE);
        alert.setHeaderText(s);
        alert.getButtonTypes().add(ButtonType.CLOSE);
        alert.showAndWait();
    }

    private void informacio(String s) {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Figyelem");
        alert.setHeaderText(s);
        alert.showAndWait();
    }

}