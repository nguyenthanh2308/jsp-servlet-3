package main.java.org.example.DAO;

import main.java.org.example.model.Province;
import main.java.org.example.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProvinceDAOImpl implements ProvinceDAO {
    private final DatabaseConnection databaseConnection = new DatabaseConnection();

    @Override
    public List<Province> getAllProvinces() throws SQLException {
        List<Province> provinces = new ArrayList<>();
        String sql = "SELECT * FROM provinces";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Province province = new Province();
                province.setIdProvince(resultSet.getInt("idProvince"));
                province.setNameProvince(resultSet.getString("nameProvince"));
                provinces.add(province);
                System.out.println("ProvinceDAOImpl: Loaded province - id: " + province.getIdProvince() + ", name: " + province.getNameProvince());
            }
        }
        return provinces;
    }
}