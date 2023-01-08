package com.example.dowitter.doc;

import com.example.dowitter.form.DocForm;
import com.example.dowitter.form.ModifyDocForm;
import com.example.dowitter.form.WriteForm;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DocRepository {

    /**
     * 문서리스트 조회
     *
     * @return List<DocForm>
     */
    List<DocForm> findDocList();

    /**
     * 문서 추가
     *
     * @param writeForm
     * @return int
     */
    int insertDoc(WriteForm writeForm);

    /**
     * 뮨서 수정
     *
     * @param modifyDocForm
     * @return int
     */
    int updateDoc(ModifyDocForm modifyDocForm);

    /**
     * 문서 삭제
     *
     * @param uid
     * @return int
     */
    int deleteDoc(Long uid);
}
