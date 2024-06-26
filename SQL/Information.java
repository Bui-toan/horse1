package SQL;

import java.sql.*;

public class Information {
	 public static final int MAX_COLUMN = 5;
	 public static String tableName = "horse2";
	 public static Connection connection = Connect.getConnection();
	 public static  void History(int color1, String time1) throws SQLException {
	        // Thêm dữ liệu vào bản
	       String insertSql = "INSERT INTO `horse`.`horse2` (`time`, `color`) VALUES ('" + time1 + "', " + color1 + ")";
	        try (Statement statement = connection.createStatement()) {
                 statement.executeUpdate(insertSql);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        // Kiểm tra số lượng cột hiện tại
	        int currentColumnCount = getRowCount(connection, tableName);

	        // Nếu số lượng cột vượt quá MAX_COLUMN, loại bỏ cột cũ nhất
	        if (currentColumnCount > MAX_COLUMN) {
	            deleteOldestRow(connection, tableName);
	        }
	    }
        public static void foundHistory(String time[], Integer[] color) {
        	int i=0;
        	try {
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM "+tableName);
				while(resultSet.next()) {
					time[i]=resultSet.getString("time");
					color[i]=resultSet.getInt("color");
					i++;
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	        
        }
	    public static int getColumnCount(Connection connection, String tableName) throws SQLException {
	        DatabaseMetaData metaData = connection.getMetaData();
	        ResultSet resultSet = metaData.getColumns(null, null, tableName, null);
	        int columnCount = 0;
	        while (resultSet.next()) {
	            columnCount++;
	        }
	        resultSet.close();
	        return columnCount;
	    }
	    public static int getRowCount(Connection connection, String tableName) throws SQLException {
	        String countQuery = "SELECT COUNT(*) FROM " + tableName;
	        try (Statement statement = connection.createStatement()) {
	            ResultSet resultSet = statement.executeQuery(countQuery);
	            resultSet.next();
	            return resultSet.getInt(1);
	        }
	    }
	    public static void deleteOldestRow(Connection connection, String tableName) throws SQLException {
	        String deleteQuery = "DELETE FROM " + tableName + " WHERE time = (SELECT MAX(time) FROM " + tableName + ")";
	        try (Statement statement = connection.createStatement()) {
	            statement.executeUpdate(deleteQuery);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
         public static void closeConnection() {
	    	  try {
	    		  if(connection!=null) {
	    			  connection.close();
	    		  }
	    	  }catch(Exception e){
	    		  e.printStackTrace();
	    	  }
	       }  
	}

