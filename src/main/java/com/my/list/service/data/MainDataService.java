package com.my.list.service.data;

import com.my.list.entity.MainData;
import com.my.list.exception.DataException;
import com.my.list.mapper.MainDataMapper;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class MainDataService {

    private final MainDataMapper mainDataMapper;
    
    MainDataService(MainDataMapper mainDataMapper) {
        this.mainDataMapper = mainDataMapper;
    }

    public void add(MainData mainData) {
        if (mainData == null) throw new DataException("Input mainData is null.");
        if (mainData.getId() != null) throw new DataException("Id of input mainData has already set.");

        if (mainData.getCtime() == null) {
            mainData.setCtime(new Timestamp(System.currentTimeMillis()));
        }
        if (mainData.getMtime() == null) {
            mainData.setMtime(new Timestamp(System.currentTimeMillis()));
        }
        
        mainDataMapper.insert(mainData);
    }
    public MainData get(Long mainDataId) {
        if (mainDataId == null) throw new DataException("Input mainDataId is null.");

        MainData mainData = mainDataMapper.select(mainDataId);
        if (mainData == null) throw new DataException("Can't find mainData with id=" + mainDataId);

        return mainData;
    }
    public void update(MainData mainData, boolean isSimple) {
        if (mainData == null) throw new DataException("Input mainData is null.");
        if (mainData.getId() == null) throw new DataException("Id of input mainData is not set.");

        mainData.setMtime(new Timestamp(System.currentTimeMillis()));
        
        mainDataMapper.update(MainData.copy(mainData), isSimple);
    }
    public void remove(Long mainDataId) {
        if (mainDataId == null) throw new DataException("Input mainDataId is null.");
        
        mainDataMapper.delete(mainDataId);
    }
}
