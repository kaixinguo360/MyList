package com.my.list.entity;

import com.my.list.exception.DataException;
import lombok.Data;

import java.util.List;

@Data
public class NodeImpl implements Node {

    private final MainData mainData;
    private ExtraData extraData = null;
    private List<ListItem> extraList = null;

    // ---- Constructor ---- //
    public NodeImpl() {
        this.mainData = new MainData();
    }
    public NodeImpl(MainData mainData) {
        this.mainData = MainData.copy(mainData);
    }

    @Override
    public <T extends ExtraData> T getExtraData(Class<T> extraDataClass) {
        if (extraData == null) throw new DataException("ExtraData is null.");
        @SuppressWarnings("unchecked") T t = (T) extraData;
        return t;
    }

}
