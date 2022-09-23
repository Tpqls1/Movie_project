package member.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import member.dto.MemberVO;
//import theater_date.dto.Theater_DateVO;
import util.DBManager;

// DAO : Database Access Object (데이터베이스 조작을 한다.) -> SingleTon으로 만듬
public class MemberDAO {
	private MemberDAO() { }
	private static MemberDAO instance = new MemberDAO();
	public static MemberDAO getInstance() {	return instance;	}
	public Connection getConnection() throws Exception {	// DBCP
		Connection conn = null;
		Context initContext = new InitialContext();
		Context envContext = (Context) initContext.lookup("java:/comp/env");
		DataSource ds = (DataSource) envContext.lookup("jdbc/movie_reserve");
		conn = ds.getConnection();
		return conn;
	}

	public int userCheck(String mem_id, String mem_pw) {	// 사용자 인증시 사용하는 메소드
		int result = -1;
		String sql = "select mem_pw from member where mem_id=?";
		ResultSet rs = null;
		try (Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql); ) {
			pstmt.setString(1, mem_id);
			rs = pstmt.executeQuery();
			if (rs.next()) {	// 사용자가 존재
				if (rs.getString("mem_pw") != null	&& rs.getString("mem_pw").equals(mem_pw))
					result = 1;	// 인증 성공
				else
					result = 0;	// 비밀번호 불일치
			} else
				result = -1;		// 존재하지 않는 아이디
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public MemberVO getMember(String mem_id) {		// 아이디로 회원 정보 가져오는 메소드
		MemberVO mVo = null;
		String sql = "select * from member where mem_id=?";
		ResultSet rs = null;
		try (Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql); ) {
			pstmt.setString(1, mem_id);
			rs = pstmt.executeQuery();
			if (rs.next()) {	// 존재하는 사용자
				mVo = new MemberVO();
				mVo.setMem_id(rs.getString("mem_id"));
				mVo.setMem_name(rs.getString("mem_name"));
				mVo.setMem_pw(rs.getString("mem_pw"));
				mVo.setMem_email(rs.getString("mem_email"));
				mVo.setMem_phone(rs.getString("mem_phone"));
				mVo.setGrade(rs.getInt("grade"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mVo;
	}

	public int confirmID(String mem_id) {		// 아이디 중복 검사
		int result = -1;
		String sql = "select mem_id from member where userid=?";
		ResultSet rs = null;
		try (Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql); ) {
			pstmt.setString(1, mem_id);
			rs = pstmt.executeQuery();
			if (rs.next())
				result = 1;		// 존재하는 아이디
			else
				result = -1;		// 존재하지 않는 아이디
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public int insertMember(MemberVO mVo) {	// 회원정보 추가
		int result = -1;
		String sql = "insert into member values(?, ?, ?, ?, ?, ?)";
		try (Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql); ) {
			pstmt.setString(1, mVo.getMem_id());
			pstmt.setString(3, mVo.getMem_pw());
			pstmt.setString(2, mVo.getMem_name());
			pstmt.setString(5, mVo.getMem_phone());
			pstmt.setString(4, mVo.getMem_email());
			pstmt.setInt(6, mVo.getGrade());
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int updateMember(MemberVO mVo) {		// 회원정보 변경
		int result = -1;
		String sql = "update member set mem_pw=?, mem_email=?,"	// 비밀번호, 이메일, 전화번호, 유형 변경
				+ "mem_phone=?, grade=? where mem_id=?";
		try (Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql); ) {
			pstmt.setString(1, mVo.getMem_pw());
			pstmt.setString(2, mVo.getMem_email());
			pstmt.setString(3, mVo.getMem_phone());
			pstmt.setInt(4, mVo.getGrade());
			pstmt.setString(5, mVo.getMem_id());
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public List<MemberVO> selectAllMember() {
		String sql = "select * from member";
		List<MemberVO>list = new ArrayList<MemberVO>();
		try(Connection conn = DBManager.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);	){
			while(rs.next()) {	//	모든 열에 대하여 반복 
				MemberVO mVo = getMemberVOFromResultSet(rs);
				list.add(mVo);
			}
		}catch(SQLException | NamingException e) {
			e.printStackTrace();
		}
		return list;
	}
	private MemberVO getMemberVOFromResultSet(ResultSet rs) throws SQLException {
		MemberVO mVo = new MemberVO();
		mVo.setMem_id(rs.getString("mem_id"));
		mVo.setMem_pw(rs.getString("mem_pw"));
		mVo.setMem_name(rs.getString("mem_name"));
		mVo.setMem_phone(rs.getString("mem_phone"));
		mVo.setMem_email(rs.getString("mem_email"));
		mVo.setGrade(rs.getInt("grade"));
		return mVo;
	}
}
