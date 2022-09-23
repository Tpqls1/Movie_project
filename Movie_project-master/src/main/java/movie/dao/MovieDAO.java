package movie.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.util.ArrayList;
//import java.util.List;

import movie.dto.MovieVO;
import util.DBManager;

public class MovieDAO {
	private MovieDAO() {}
	private static MovieDAO instance = new MovieDAO();
	public static MovieDAO getInstance() {return instance;}
	
	public MovieVO selectMvByNum(int mv_num){
		String sql = "select * from movie where mv_num = ?";
		ResultSet rs = null;
		MovieVO mVo = null;
		try (Connection conn = DBManager.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);){
			pstmt.setInt(1, mv_num);
			rs = pstmt.executeQuery();
			if(rs.next()) {
			mVo = new MovieVO();
			mVo.setMv_num(rs.getInt("mv_num"));
			mVo.setMv_name(rs.getString("mv_name"));
			mVo.setMv_price(rs.getInt("mv_price"));
			mVo.setMv_actor(rs.getString("mv_actor"));
			mVo.setMv_director(rs.getString("mv_director"));
			mVo.setMv_director(rs.getString("summary"));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBManager.close(rs);
		}
		return mVo;
	}
}
