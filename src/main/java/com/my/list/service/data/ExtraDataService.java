package com.my.list.service.data;

import com.my.list.entity.ExtraData;
import com.my.list.exception.DataException;
import com.my.list.type.TypeDefinition;
import com.my.list.type.TypeManager;
import org.springframework.stereotype.Service;

@Service
public class ExtraDataService {

    private final TypeManager typeManager;

    ExtraDataService(TypeManager typeManager) {
        this.typeManager = typeManager;
    }

    public void add(ExtraData extraData) {
        if (extraData == null) throw new DataException("Input extraData is null.");
        if (extraData.getId() == null) throw new DataException("Id of input extraData is not set.");

        TypeDefinition typeDefinition = typeManager.getType(extraData);
        typeDefinition.getExtraDataMapper().insert(extraData);
    }
    public <T extends ExtraData> T get(Long extraDataId, Class<T> extraDataClass) {
        if (extraDataId == null) throw new DataException("Input extraDataId is null.");

        TypeDefinition typeDefinition = typeManager.getType(extraDataClass);
        ExtraData extraData = typeDefinition.getExtraDataMapper().selectByPrimaryKey(extraDataId);
        if (extraData == null) throw new DataException("Can't find extraData for node with id=" + extraDataId);

        @SuppressWarnings("unchecked") T t = (T) extraData;
        return t;
    }
    public void update(ExtraData extraData) {
        if (extraData == null) throw new DataException("Input extraData is null.");
        if (extraData.getId() == null) throw new DataException("Id of input extraData is not set.");
        TypeDefinition typeDefinition = typeManager.getType(extraData);
        
        ExtraData old = typeDefinition.getExtraDataMapper().selectByPrimaryKey(extraData.getId());
        if (old == null) throw new DataException("Can't find extra data with id=" + extraData.getId());
        extraData.setId(old.getId());
        
        typeDefinition.getExtraDataMapper().updateByPrimaryKey(extraData);
    }
    public void remove(Long extraDataId, Class<? extends ExtraData> extraDataClass) {
        if (extraDataId == null) throw new DataException("Input extraDataId id is null.");

        TypeDefinition typeDefinition = typeManager.getType(extraDataClass);
        typeDefinition.getExtraDataMapper().deleteByPrimaryKey(extraDataId);
    }
}
