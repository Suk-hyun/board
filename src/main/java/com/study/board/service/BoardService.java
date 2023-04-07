package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired //field injection not recommended
    private BoardRepository boardRepository;

    //글 작성 처리
    public void write(Board board, MultipartFile file) throws Exception {
        // 매개변수로 받는 변수의 이름인 file 과 boardwrite.html 에 있는 input 태그의 name="" 값이 일치해야 된당
        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files";

        UUID uuid = UUID.randomUUID();

        String fileName;

        fileName = uuid + "_" + file.getOriginalFilename();

        File saveFile = new File(projectPath, fileName);

        file.transferTo(saveFile);

        board.setFilename(fileName);
        board.setFilepath("/files/" + fileName);

        boardRepository.save(board);
    }

    //게시글 리스트 불러오는 기능
    public Page<Board> boardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable) {

        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }

    //특정 게시글 불러오기
    public Board boardView(Integer id) {

        return boardRepository.findById(id).get(); // null check 없이 get 씀.
    }

    //특정 게시글 삭제
    public void boardDelete(Integer id) {

        boardRepository.deleteById(id);
    }
}
