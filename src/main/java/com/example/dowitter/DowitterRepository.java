package com.example.dowitter;

import com.example.dowitter.Form.DocForm;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DowitterRepository {
    List<DocForm> findDocList();
}
