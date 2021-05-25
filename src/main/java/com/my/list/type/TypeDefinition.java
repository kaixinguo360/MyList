package com.my.list.type;

import com.my.list.entity.ExtraData;
import com.my.list.entity.Node;
import com.my.list.mapper.common.ExtraDataMapper;
import lombok.Data;

@Data
public class TypeDefinition {

    private final String typeName;
    private Class<? extends ExtraData> extraDataClass = null;
    private ExtraDataMapper extraDataMapper = null;
    private Boolean hasExtraData = false;
    private Boolean hasExtraList = false;
    private Boolean extraListUnique = false;
    private Boolean extraListRequired = false;
    private NodeNormalizer nodeNormalizer = null;
    private ExcerptGenerator excerptGenerator = null;

    public TypeDefinition(String name) {
        this.typeName = name;
    }

    public void normalize(Node node) {
        if (this.nodeNormalizer != null) {
            this.nodeNormalizer.normalize(node);
        }
    }

    public String generateExcerpt(Node node) {
        return this.excerptGenerator != null ? this.excerptGenerator.generate(node) : null;
    }

    public interface NodeNormalizer {
        void normalize(Node node);
    }

    public interface ExcerptGenerator {
        String generate(Node node);
    }
}
