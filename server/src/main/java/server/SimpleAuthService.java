package server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleAuthService implements AuthService {
    private static Connection connection;
    private static Statement stmt;

    private static String logPassNick;
    private static String token[];
    private static PreparedStatement psInsert;
    private class UserData {
        String login;
        String password;
        String nickname;

        public UserData(String login, String password, String nickname) {
            this.login = login;
            this.password = password;
            this.nickname = nickname;
        }
    }

    List<UserData> users;

    public SimpleAuthService() {
        try {
            connectDb ();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        users = new ArrayList<>();
        token = logPassNick.split("///");
        users.add(new UserData("qwe" , "qwe" , "qwe" ));
        users.add(new UserData("asd" , "asd" , "asd" ));
        users.add(new UserData("zxc" , "zxc" , "zxc" ));
        users.add(new UserData(token[0] , token[1] , token[2] ));

    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        for (UserData user : users) {
            if (user.login.equals(login) && user.password.equals(password)) {
                return user.nickname;
            }
        }
        return null;
    }

    @Override
    public boolean registration(String login, String password, String nickname) {

        for (UserData user : users) {
            if (user.login.equals(login) || user.nickname.equals(nickname)) {
                return false;
            }
        }
        users.add(new UserData(login , password, nickname ));
        try {

            System.out.println(login + nickname + password);
            psInsert = connection.prepareStatement("INSERT INTO users (name, nick, password) VALUES (?, ?, ?);");
            connection.setAutoCommit(false);
                psInsert.setString(1, login);
                psInsert.setString(3, password);
                psInsert.setString(2, nickname);
                psInsert.executeUpdate();
            connection.setAutoCommit(true);


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }
    public static void connectDb () throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:baseChat.db");
        stmt = connection.createStatement();
        System.out.println("connect ok");
        ResultSet rs = stmt.executeQuery("SELECT name, password, nick FROM users WHERE name = \"garik\" ");
        //      stmt.executeUpdate("INSERT INTO students (name,score) VALUES (\"Garik\", 80),(\"Aleksei\", 50);");
        while (rs.next()) {
            logPassNick =  rs.getString("name") + "///" + rs.getString("password")  + "///" + rs.getString("nick");

        }
    }
    public static void disconnect() {

        try {
            connection.close();
            System.out.println("Отключение от Базы данных");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
