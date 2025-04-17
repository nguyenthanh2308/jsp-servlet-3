package main.java.org.example.DAO;

import main.java.org.example.model.Province;

import java.sql.SQLException;
import java.util.List;

public interface ProvinceDAO {
    List<Province> getAllProvinces() throws SQLException;
}