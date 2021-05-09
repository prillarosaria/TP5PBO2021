/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modulgame;
import com.mysql.jdbc.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Fauzan
 */
public class dbConnection {
    public static Connection con;
    public static Statement stm;
    
    public void connect(){//untuk membuka koneksi ke database
        try {
            String url ="jdbc:mysql://localhost/db_gamepbo";
            String user="root";
            String pass="";
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url,user,pass);
            stm = con.createStatement();
            System.out.println("koneksi berhasil;");
        } catch (Exception e) {
            System.err.println("koneksi gagal" +e.getMessage());
        }
    }
    
    public void scoreDB(String username, int score){
        try{
            connect();
            String uname = "SELECT username FROM highscore WHERE username = (?)";
            PreparedStatement stmt = (PreparedStatement) con.prepareStatement(uname);
            stmt.setString(1, username);
            ResultSet res = stmt.executeQuery(uname);
            
            if(res != null){
                String sql = "UPDATE highscore SET score = (?) WHERE username = (?)";
                PreparedStatement stm = (PreparedStatement) con.prepareStatement(sql);
                stm.setInt(1, score);
                stm.setString(2, username);
                stm.execute();
            }else{
                String sql = "INSERT INTO highscore (username, score) VALUES (?, ?)";
                PreparedStatement stm = (PreparedStatement) con.prepareStatement(sql);
                stm.setString(1, username);
                stm.setInt(2, score);
                stm.execute();
            }
            con.close();
        }catch(Exception e){
            System.err.println("tidak bisa update" +e.getMessage());
        }
    }
    
    public DefaultTableModel readTable(){
        
        DefaultTableModel dataTabel = null;
        try{
            Object[] column = {"No", "Username", "Score"};
            connect();
            dataTabel = new DefaultTableModel(null, column);
            String sql = "Select * from highscore order by Score Desc";
            ResultSet res = stm.executeQuery(sql);
            
            int no = 1;
            while(res.next()){
                Object[] hasil = new Object[3];
                hasil[0] = no;
                hasil[1] = res.getString("Username");
                hasil[2] = res.getString("Score");
                no++;
                dataTabel.addRow(hasil);
            }
        }catch(Exception e){
            System.err.println("Read gagal " +e.getMessage());
        }
        
        return dataTabel;
    }
}
