package database;

public class DatabaseConn {
    public static String buildURL(String dbType, String host, int port, String dataBase){
        if(dbType.equalsIgnoreCase("mysql")){
            return "jdbc:mysql://"+host+":"+port+"/"+dataBase;
        }
        throw new RuntimeException("Nepodrzana baza");
    }
}
