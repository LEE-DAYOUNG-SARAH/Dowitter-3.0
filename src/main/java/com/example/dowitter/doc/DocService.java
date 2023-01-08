package com.example.dowitter.doc;

import com.example.dowitter.form.DocForm;
import com.example.dowitter.form.ModifyDocForm;
import com.example.dowitter.form.WriteForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocService {
    private final DocRepository docRepository;

    /**
     * 문서 리스트 조회
     *
     * @return List<DocForm>
     */
    public List<DocForm> getDocList() {
        return docRepository.findDocList();
    }

    /**
     * 개인피드 리스트 조회
     *
     * @param uid
     * @return List<DocForm>
     */
    public List<DocForm> getFeedList(Long uid) {
        return getDocList().stream()
                .filter(docForm -> docForm.getMemberUid().equals(uid))
                .collect(Collectors.toList());
    }

    /**
     * 문서 생성
     *
     * @param writeForm
     */
    public void writeDoc(WriteForm writeForm) {
        int countOfInsertRow = docRepository.insertDoc(writeForm);
        if( countOfInsertRow != 1 ) {
            throw new RuntimeException();
        }
    }

    /**
     * 문서 수정
     *
     * @param modifyDocForm
     */
    public void modifyDoc(ModifyDocForm modifyDocForm) {
        int countOfUpdateRow = docRepository.updateDoc(modifyDocForm);
        if( countOfUpdateRow != 1 ) {
            throw new RuntimeException();
        }
    }

    /**
     * 문서 삭제
     *
     * @param uid
     */
    public void deleteDoc(Long uid) {
        int countOfDeleteRow = docRepository.deleteDoc(uid);
        if( countOfDeleteRow != 1 ) {
            throw new RuntimeException();
        }
    }

}
