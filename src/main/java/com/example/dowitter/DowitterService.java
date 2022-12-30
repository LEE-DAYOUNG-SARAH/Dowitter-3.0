package com.example.dowitter;

import com.example.dowitter.Form.DocForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DowitterService {
    private final DowitterRepository dowitterRepository;

    public DowitterService(DowitterRepository dowitterRepository) {
        this.dowitterRepository = dowitterRepository;
    }

    public List<DocForm> getDocList() {
        return dowitterRepository.findDocList();
    }

}
