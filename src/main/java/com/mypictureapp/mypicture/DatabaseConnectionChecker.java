package com.mypictureapp.mypicture;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class DatabaseConnectionChecker implements CommandLineRunner {

    private final DataSource dataSource;

    public DatabaseConnectionChecker(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) {
        System.out.println("=================================================");
        System.out.println("⏳ ĐANG KIỂM TRA KẾT NỐI ĐẾN TIDB CLOUD...");
        try (Connection connection = dataSource.getConnection()) {
            System.out.println(" KẾT NỐI DATABASE THÀNH CÔNG RỒI NHA!");
            System.out.println("👉 Database Product Name : " + connection.getMetaData().getDatabaseProductName());
            System.out.println("👉 Database Version      : " + connection.getMetaData().getDatabaseProductVersion());
            System.out.println("👉 Schema hiện tại       : " + connection.getCatalog());
        } catch (Exception e) {
            System.err.println("❌ KẾT NỐI THẤT BẠI! LỖI CHI TIẾT:");
            e.printStackTrace();
        }
        System.out.println("=================================================");
    }
}
