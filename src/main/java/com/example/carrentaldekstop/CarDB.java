package com.example.carrentaldekstop;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDB {
    Connection conn;

    public CarDB() throws SQLException {
        String DB_CONNECTION="mysql";
        String DB_HOST="localhost";
        String DB_PORT="3306";
        String DB_DATABASE="car_vizsga";
        String DB_USERNAME="root";
        String DB_PASSWORD="";

        String url=String.format("jdbc:%s://%s:%s/%s",DB_CONNECTION,DB_HOST,DB_PORT,DB_DATABASE);
        conn= DriverManager.getConnection(url,DB_USERNAME,DB_PASSWORD);
    }

    public List<Car> getCars() throws SQLException {
        List<Car>cars=new ArrayList<>();
        String sql="SELECT * FROM cars";
        Statement statement= conn.createStatement();
        ResultSet resultSet= statement.executeQuery(sql);
        while (resultSet.next()){
            int id=resultSet.getInt("id");
            String license_plate_number=resultSet.getString("license_plate_number");
            String brand=resultSet.getString("brand");
            String model=resultSet.getString("model");
            int daily_cost=resultSet.getInt("daily_cost");
            Car car=new Car(id,license_plate_number,brand,model,daily_cost);
            cars.add(car);
        }
        return cars;
    }

    public boolean deleteCar(Car torlendo) throws SQLException {
        String sql="DELETE FROM cars WHERE id=?";
        PreparedStatement statement=conn.prepareStatement(sql);
        statement.setInt(1,torlendo.getId());
        return statement.executeUpdate()>0;

    }
}
