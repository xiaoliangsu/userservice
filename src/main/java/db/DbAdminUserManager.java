package db;
import model.AdminUserBean.AdminUserBean;
import model.AdminUserInfoBean.AdminUserInfoBean;
import model.UserBean;
import model.UserInfoBean;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbAdminUserManager {
    private static DbAdminUserManager dbAdminUserManager;

    private Connection conn = null;
    //siteware 是数据库名，根据需要修改
    private String url = "jdbc:mysql://localhost:3306/siteware?useUnicode=true&characterEncoding=utf-8";
    public static DbAdminUserManager getInstance(){
        if(dbAdminUserManager==null){
            synchronized (DbAdminUserManager.class){
                if(dbAdminUserManager==null){
                    dbAdminUserManager=new DbAdminUserManager();
                }
            }
        }
        return dbAdminUserManager;
    }

    public void init(){
        try {
            Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动
            conn = DriverManager.getConnection(url,"root","1234");
            System.out.println("连接数据库成功");
            createTable();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    //创建数据表的操作
    public void createTable(){
        PreparedStatement pstmt;

        String sql = "create table IF NOT EXISTS adminUser(id integer primary key not null auto_increment,name varchar(20),password varchar(20),hashedPassword varchar(100)) ENGINE=InnoDB DEFAULT CHARSET=utf8";
        try {
            pstmt=conn.prepareStatement(sql);
            pstmt.execute();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //增加
    public void insert(String name,String password,String hashedPassword){
        String sql = "insert into adminUser(name,password,hashedPassword) values(?,?,?)";
        PreparedStatement pstmt;
        try {
            pstmt=conn.prepareStatement(sql);
            //1,2对应sql语句里面的问号顺序
            pstmt.setString(1,name);
            pstmt.setString(2,password);
            pstmt.setString(3,hashedPassword);

            pstmt.execute();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //修改
    public void update(String name,String password,String hashedPassword,int id){
        String sql = "UPDATE adminUser set name=?,password=?,hashedPassword=? WHERE  id=?";
        PreparedStatement pstmt;
        try {
            pstmt=conn.prepareStatement(sql);
            pstmt.setString(1,name);
            pstmt.setString(2,password);
            pstmt.setString(3,hashedPassword);
            pstmt.setInt(4,id);
            pstmt.execute();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //删除
    public void delete(String name){
        String sql = "DELETE FROM adminUser where name=?";
        PreparedStatement pstmt;
        try {
            pstmt=conn.prepareStatement(sql);
            pstmt.setString(1,name);
            pstmt.execute();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //查询
    public List<AdminUserInfoBean> select(String name){
        List<AdminUserInfoBean> adminUserBeans = new ArrayList<AdminUserInfoBean>();
        String sql = "SELECT * FROM adminUser WHERE name=?";    //要执行的SQL
        PreparedStatement pstmt;
        ResultSet rs=null;
        try {
            pstmt=conn.prepareStatement(sql);
            pstmt.setString(1,name);
            rs = pstmt.executeQuery();//创建数据对象
            while (rs.next()){
                AdminUserInfoBean adminUserInfoBean = new AdminUserInfoBean();
                adminUserInfoBean.setUsername(rs.getString(2));
                adminUserInfoBean.setPassword(rs.getString(3));
                adminUserInfoBean.setHashedPassword(rs.getString(4));

                adminUserBeans.add(adminUserInfoBean);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adminUserBeans;
    }



    public void close(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
