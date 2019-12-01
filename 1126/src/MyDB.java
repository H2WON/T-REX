import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyDB {
	public static Connection getCon(){
		Connection con=null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");	// JDBC Driver Load(Driver 객체 생성)
			String url = "jdbc:mysql://localhost:3306/trexdb?characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false";
			//useSSL : 보안 설정(true - 속도가 느려짐)
			String username = "root";
			String pwd = "yuhan1234";
			con = DriverManager.getConnection(url, username, pwd);
		} catch (ClassNotFoundException e) {
			System.out.println("JDBC Driver 클래스를 찾을 수 없습니다.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return con;
	}
	
	public static void main(String[] args) {
		Connection con = getCon();
		System.out.println("정상적으로 연결이 되었습니다.");
		
		//문장객체 생성
		String sql = "SELECT no, score From ranking;";
		/*
		String sql = "SELECT isbn, name, writer, category_name, publisher_name, price" + 
				"	FROM book b, category c, publisher p" + 
				"    where b.category_id=c.category_id and b.publisher_id= p.publisher_id;";
		*/
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			//SQL문 실행(select문)
			ResultSet rs = pstmt.executeQuery();
			//pstmt.executeQuery() : ResultSet을 반환해줌
			while (rs.next()) {
				System.out.println(rs.getInt(1) + " ");
				System.out.println(rs.getString(2) + " ");
				System.out.println();
				/*
				System.out.print(rs.getInt(1)+"	");
				System.out.print(rs.getString(2)+"	");
				System.out.print(rs.getString(3)+"	");
				System.out.print(rs.getString(4)+"	");
				System.out.print(rs.getString(5)+"	");
				System.out.print(rs.getInt(6));
				System.out.println();
				*/
				//DB는 index번호 1부터 시작
			}
			if(con != null)
			{
				con.close(); //연결객체 닫아주기
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
