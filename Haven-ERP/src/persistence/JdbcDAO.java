package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import java.sql.SQLException;
import javax.sql.DataSource;



import bean.Staff;
import util.DBUtils;

public class JdbcDAO implements StaffDAO{
	
	private static final String DATA_SOURCE = "jdbc:derby://localhost:1527/Haven;';";
    private Connection con;
    private ResultSet rs;
    private PreparedStatement pst;

	@Override
	public List<Staff> findAll() {
		// TODO Auto-generated method stub
		try {
			Class.forName("org.apache.derby.jdbc.ClientDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Staff> staffList = new ArrayList<>();

		execute(() -> {
            con = DriverManager.getConnection(DATA_SOURCE);
            pst = con.prepareStatement("SELECT * FROM STAFF");
            
            rs = pst.executeQuery();
            
            while (rs.next()) {
                Staff staff = new Staff();
                staff.setId(rs.getLong(1));
                staff.setFname(rs.getString(2));
                staff.setLname(rs.getString(3));
                staff.setSalary(rs.getInt(4));
                staffList.add(staff);
            }
        });

        return staffList;
	}
	
	private void execute(Executable executable) {

        try {
            executable.exec();
        } catch (NamingException | SQLException e) {

            Logger lgr = Logger.getLogger(JdbcDAO.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);

        } finally {
            
            DBUtils.closeResultSet(rs);
            DBUtils.closeStatement(pst);
            DBUtils.closeConnection(con);
        }
    }

}