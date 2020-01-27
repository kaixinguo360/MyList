package com.my.list.service.data;

import com.my.list.domain.ExtraData;
import com.my.list.dto.Type;
import com.my.list.dto.TypeConfig;
import com.my.list.service.DataException;
import org.springframework.stereotype.Service;

@Service
class ExtraDataService {

    private final TypeConfig typeConfig;

    ExtraDataService(TypeConfig typeConfig) {
        this.typeConfig = typeConfig;
    }

    void add(ExtraData extraData) {
        if (extraData == null) throw new DataException("Input extraData is null.");
        if (extraData.getExtraId() == null) throw new DataException("Id of input extraData is not set.");

        Type type = typeConfig.getType(extraData);
        type.getExtraDataMapper().insert(extraData);
    }
    <T extends ExtraData> T get(Long extraDataId, Class<T> extraDataClass) {
        if (extraDataId == null) throw new DataException("Input extraDataId is null.");

        Type type = typeConfig.getType(extraDataClass);
        ExtraData extraData = type.getExtraDataMapper().selectByPrimaryKey(extraDataId);
        if (extraData == null) throw new DataException("Can't find extraData for node with id=" + extraDataId);

        @SuppressWarnings("unchecked") T t = (T) extraData;
        return t;
    }
    void update(ExtraData extraData) {
        if (extraData == null) throw new DataException("Input extraData is null.");
        if (extraData.getExtraId() == null) throw new DataException("Id of input extraData is not set.");
        Type type = typeConfig.getType(extraData);
        
        ExtraData old = type.getExtraDataMapper().selectByPrimaryKey(extraData.getExtraId());
        if (old == null) throw new DataException("Can't find extra data with id=" + extraData.getExtraId());
        extraData.setExtraId(old.getExtraId());
        
        type.getExtraDataMapper().updateByPrimaryKey(extraData);
    }
    void remove(Long extraDataId, Class<? extends ExtraData> extraDataClass) {
        if (extraDataId == null) throw new DataException("Input extraDataId id is null.");

        Type type = typeConfig.getType(extraDataClass);
        type.getExtraDataMapper().deleteByPrimaryKey(extraDataId);
    }
}
