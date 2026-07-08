package com.nh.TravelPlanner;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.nh.service.CommentService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {
	"file:src/main/webapp/WEB-INF/spring/root-context.xml"
})


public class CommentServiceTest {
	
	@Autowired
	CommentService cSvc;
	
	// 댓글 개수 카운트
	@Test
	public void testCountComment() {
		// 1) Given
		int bno = 2;
		
		// 2) When
		int cnt = cSvc.CountComment(bno);
		
		// 3) Then : sql문이 오류가 안되면 성공
		System.out.println(bno + " 댓글수 : " + cnt);
	}
	
	// 댓글 목록 조회
	@Test
	public void testGetComment() {
		// 1) Given
		int bno = 1;
		int page = 1;
		
		// 2) When
		List<Map<String,Object>> list = cSvc.getComment(bno, page);
		
		// 3) Then : list가 null이면 안됨. 빈곳이라도 가르켜야됨.
		assertNotNull("list가 null이면 안됨.", list);
		System.out.println(list.size());
		for(int i=0; i<list.size(); i++) {
			System.out.println(list.get(i));
		}
	}
	
	// 게시글 댓글 삭제 
	@Test
	@Transactional
	public void testDeleteComment() {
		// 1) Given
		int cno = 100;
		int id = 4;
		
		// 2) When
		cSvc.deleteComment(cno, id);
		
		// 3) Then : id가 쓴 댓글이거나, 게시글 작성자여아지만 지워짐, 
	}
	
	// 게시글 댓글 추가
	@Test
	@Transactional
	public void testInsertComment() {
		// 1) Given
		int bno = 102;
		int writerId = 1;
		String content="테스트";
		
		// 2) When
		cSvc.insertComment(bno,writerId,content);
		
		// 3) Then : sql 오류가 안나야됨.
	}
	
	// 게시글 댓글 수정
	@Test
	@Transactional
	public void testModifyComment() {
		// 1) Given
		int cno = 120;
		String content="수정";
		
		// 2) When
		cSvc.modifyComment(cno,content);
		
		// 3) Then : sql 오류가 안나야됨.
	}
	
}
