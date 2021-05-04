package com.redspeaks.gang.database;

import com.redspeaks.gang.GangPlugin;
import com.redspeaks.gang.api.gangs.DataHandler;
import com.redspeaks.gang.api.gangs.Storage;
import com.redspeaks.gang.objects.Database;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.HashMap;

public class DatabaseManager {

    private Connection connection = null;
    private Database database = GangPlugin.getInstance().getDatabase();

    public void setup() {
        if(connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://" + database.host() + ":" + database.port() + "/" + database.database() + "?useSSL=false"
                , database.user(), database.pass());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void close() {
        if(connection != null) {
            try{
                connection.close();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void createTableForGangs() {
        try(PreparedStatement ps = preparedStatement("CREATE TABLE IF NOT EXISTS gangs " +
                "(uuid VARCHAR(100), level INT(100), exp DOUBLE PRECISION, gang VARCHAR(100), PRIMARY KEY(uuid))")) {
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void loadManager(DataHandler dataHandler) {
        Bukkit.getScheduler().runTaskAsynchronously(GangPlugin.getInstance(), () -> {
            try(PreparedStatement ps = preparedStatement("SELECT * FROM gangs")) {
                try(ResultSet resultSet = ps.executeQuery()) {
                    dataHandler.distributeData(resultSet);
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void saveData(HashMap<String, String> data) {
        try(PreparedStatement ps = preparedStatement("INSERT INTO gangs (uuid,level,exp,gang) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE level=?, exp=?, gang=?")) {
            for (String uuid : data.keySet()) {
                String[] readData = data.get(uuid).split("::");
                ps.setString(1, uuid);
                ps.setInt(2, Integer.parseInt(readData[0]));
                ps.setDouble(3, Double.parseDouble(readData[1]));
                ps.setString(4, readData[2]);
                ps.setInt(5, Integer.parseInt(readData[0]));
                ps.setDouble(6, Double.parseDouble(readData[1]));
                ps.setString(7, readData[2]);
                ps.executeUpdate();
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadData() {
        createTableForGangs();
        loadManager(resultSet -> {
            try {
                while (resultSet.next()) {
                    String data[] = {resultSet.getString("uuid"), resultSet.getInt("level") + "", resultSet.getDouble("exp") + "", resultSet.getString("gang")};
                    Storage.playerDatabase.put(data[0], data[1]);
                    Storage.playerDatabase.put(data[0], data[2]);
                    Storage.playerDatabase.put(data[0], data[3]);
                    data = null;
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public PreparedStatement preparedStatement(String statement) throws SQLException {
        return connection.prepareStatement(statement);
    }
}
