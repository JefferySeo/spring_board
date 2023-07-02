package com.jeffery.spbbs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.jeffery.spbbs.dto.SpDTO;

// DB 접근 클래스
public class SpDAO {
	
	DataSource dataSource;
	// 생성자에서 db 접속
	public SpDAO() {
		try {
			
			Context context = new InitialContext();
			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/spbbs");
						
			
		}catch(NamingException e) {
			e.printStackTrace();
		}
	}
	
	// 글쓰기
	public void write(String uname, String upass, String title, String content) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = dataSource.getConnection();
			String sql = "insert into spboard (uname, upass, title, content) values (?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, uname);
			pstmt.setString(2, upass);
			pstmt.setString(3, title);
			pstmt.setString(4, content);
			
			pstmt.executeUpdate();
			
			rs = pstmt.getGeneratedKeys(); // 쿼리 실행 후 생성된 키값 반환
			
			pstmt.clearParameters(); // pstmt 재활용 위해서 클리어 해줌
			
			if(rs.next()) {
				int s_group = rs.getInt(1);
				try {
					String query = "update spboard set s_group = ? where num = ?";
					pstmt = conn.prepareStatement(query);
					pstmt.setInt(1,  s_group);
					pstmt.setInt(2, s_group);
					pstmt.executeUpdate();
					
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(conn != null) conn.close();
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			}catch(SQLException eee) {
				eee.printStackTrace();
			}
			
		}
	}
	
	// 답글 쓰기
	public SpDTO reply(String cNum) {
		int iNum = Integer.parseInt(cNum);
		SpDTO dto = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = dataSource.getConnection();
			String sql = "select * from spboard where num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, iNum);
			rs = pstmt.executeQuery();
			
			// dto에 담기
			if(rs.next()) {
				dto = new SpDTO();
				int num = rs.getInt("num");
				int s_group = rs.getInt("s_group");
				int s_step = rs.getInt("s_step");
				int s_indent = rs.getInt("s_indent");
				
				dto.setNum(num);
				dto.setS_group(s_group);
				dto.setS_step(s_step);
				dto.setS_indent(s_indent);
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(conn != null) conn.close();
				if(pstmt != null) pstmt.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		return dto;
	}
	
	// 답글 등록
	public void replyok(int s_group, int s_step, int s_indent, String uname, String upass, String title, String content) {
		replyUpdate(s_group, s_step);
		String sql = "insert into spboard(s_group, s_step, s_indent, uname, upass, title, content) values(?, ?, ?, ?, ?, ?, ?)";
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, s_group);
			pstmt.setInt(2, s_step);
			pstmt.setInt(3, s_indent);
			pstmt.setString(4, uname);
			pstmt.setString(5, upass);
			pstmt.setString(6, title);
			pstmt.setString(7, content);
			
			pstmt.executeUpdate();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(conn != null) conn.close();
				if(pstmt != null) pstmt.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void updateok(String num, String uname, String upass, String title, String content) {
		int inum = Integer.parseInt(num);
		String sql = "update spboard set uname=?, upass=?, title=?, content=? where num = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		
			
	}
	
	private void replyUpdate(int s_group, int s_step) {
		String sql = "update spboard set s_step = s_step+1 where s_group = ? and s_step >= ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, s_group);
			pstmt.setInt(2, s_step);
			pstmt.executeUpdate();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(conn != null) conn.close();
				if(pstmt != null) pstmt.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	// 본문 보기
	public SpDTO detail(String cNum) {
		int iNum = Integer.parseInt(cNum);
		hitAdd(iNum);
		SpDTO dto = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = dataSource.getConnection();
			String sql = "select * from spboard where num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, iNum);
			rs = pstmt.executeQuery();
			
			// dto에 담기
			if(rs.next()) {
				int num = rs.getInt("num");
				int s_group = rs.getInt("s_group");
				int s_step = rs.getInt("s_step");
				int s_indent = rs.getInt("s_indent");
				String uname = rs.getString("uname");
				String upass = rs.getString("upass");
				String title = rs.getString("title");
				String content = rs.getString("content");
				int ct = rs.getInt("ct");
				int hit = rs.getInt("hit");
				Timestamp wdate = rs.getTimestamp("wdate");
				
				dto = new SpDTO(num, s_group, s_step, s_indent, uname, upass, title, content, ct, hit, wdate);
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(conn != null) conn.close();
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			}catch(SQLException eee) {
				eee.printStackTrace();
			}
			
		}
		
		return dto;
	}
	
	
	// 데이터를 받아서 SpDTO에 담음
	public ArrayList<SpDTO> list(){
		
		ArrayList<SpDTO> dtos = new ArrayList<SpDTO>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = dataSource.getConnection();
			String sql = "select * from spboard order by s_group desc, s_step asc";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int num = rs.getInt("num");
				int s_group = rs.getInt("s_group");
				int s_step = rs.getInt("s_step");
				int s_indent = rs.getInt("s_indent");
				String uname = rs.getString("uname");
				String upass = rs.getString("upass");
				String title = rs.getString("title");
				String content = rs.getString("content");
				int ct = rs.getInt("ct");
				int hit = rs.getInt("hit");
				Timestamp wdate = rs.getTimestamp("wdate");
				
				SpDTO dto = new SpDTO(num, s_group, s_step, s_indent, uname, upass, title, content, ct, hit, wdate);
				
				dtos.add(dto);
			}
			
		}catch(Exception ee) {
			ee.printStackTrace();
		}finally {
			try {
				if(conn != null) conn.close();
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			}catch(SQLException eee) {
				eee.printStackTrace();
			}
			
		}
		
		return dtos;
	}// list() 끝
	
	// 조회수 증가
	private void hitAdd(int num) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = dataSource.getConnection();
			String sql = "update spboard set hit = hit + 1 where num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			int r = pstmt.executeUpdate();
			System.out.println("hit 업데이트 : " + r);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(conn != null) conn.close();
				if(pstmt != null) pstmt.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
}










