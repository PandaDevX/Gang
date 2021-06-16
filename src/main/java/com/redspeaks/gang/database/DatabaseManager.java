package com.redspeaks.gang.database;

import com.redspeaks.gang.GangPlugin;
import com.redspeaks.gang.api.gangs.*;
import com.redspeaks.gang.objects.Database;
import com.redspeaks.gang.objects.Gang;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.HashMap;

public class DatabaseManager {

    private Connection connection = null;
    private final Database database = GangPlugin.getInstance().getDatabase();

    public Connection getConnection() {
        if(connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://" + database.host() + ":" + database.port() + "/" + database.database() + "?useSSL=false"
                        , database.user(), database.pass());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return connection;
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
        Bukkit.getScheduler().scheduleSyncDelayedTask(GangPlugin.getInstance(), () -> {
            try(PreparedStatement ps = preparedStatement("SELECT * FROM gangs WHERE uuid=?")) {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    ps.setString(1, player.getUniqueId().toString());
                }
                try(ResultSet resultSet = ps.executeQuery()) {
                    dataHandler.distributeData(resultSet);
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void loadManager(DataHandler dataHandler, Player player) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(GangPlugin.getInstance(), () -> {
            try(PreparedStatement ps = preparedStatement("SELECT * FROM gangs WHERE uuid=?")) {
                ps.setString(1, player.getUniqueId().toString());
                try(ResultSet resultSet = ps.executeQuery()) {
                    dataHandler.distributeData(resultSet);
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void saveData(HashMap<String, PlayerData> data) {
        try(PreparedStatement ps = preparedStatement("INSERT INTO gangs (uuid,level,exp,gang) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE level=?, exp=?, gang=?")) {
            for (String uuid : data.keySet()) {
                PlayerData playerData = data.get(uuid);
                ps.setString(1, playerData.getUniqueId());
                ps.setInt(2, playerData.level());
                ps.setDouble(3, playerData.exp());
                ps.setString(4, playerData.gang().getPrefix());
                ps.setInt(5, playerData.level());
                ps.setDouble(6, playerData.exp());
                ps.setString(7, playerData.gang().getPrefix());
                ps.executeUpdate();
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveData(HashMap<String, PlayerData> data, Player player) {
        GangPlayer gangPlayer = Gang.getPlayer(player);
        try(PreparedStatement ps = preparedStatement("INSERT INTO gangs (uuid,level,exp,gang) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE level=?, exp=?, gang=?")) {
            PlayerData playerData = gangPlayer.getPlayerData();
            ps.setString(1, playerData.getUniqueId());
            ps.setInt(2, playerData.level());
            ps.setDouble(3, playerData.exp());
            ps.setString(4, playerData.gang().getPrefix());
            ps.setInt(5, playerData.level());
            ps.setDouble(6, playerData.exp());
            ps.setString(7, playerData.gang().getPrefix());
            ps.executeUpdate();
            data.remove(player.getUniqueId().toString());
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadData() {
        createTableForGangs();
        loadManager(resultSet -> {
            try {
                while (resultSet.next()) {
                    Storage.playerDatabase.put(resultSet.getString("uuid"), new PlayerData(resultSet.getString("uuid"),
                            resultSet.getInt("level"),
                            resultSet.getDouble("exp"),
                            GangType.getGang(resultSet.getString("gang"))));
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void loadData(Player player) {
        createTableForGangs();
        loadManager(resultSet -> {
            try {
                if (resultSet.next()) {
                    Storage.playerDatabase.put(resultSet.getString("uuid"), new PlayerData(resultSet.getString("uuid"),
                            resultSet.getInt("level"),
                            resultSet.getDouble("exp"),
                            GangType.getGang(resultSet.getString("gang"))));
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }, player);
    }

    public PreparedStatement preparedStatement(String statement) throws SQLException {
        return getConnection().prepareStatement(statement);
    }
}
