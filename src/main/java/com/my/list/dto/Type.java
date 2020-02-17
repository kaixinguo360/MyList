package com.my.list.dto;

import com.my.list.domain.ExtraData;
import com.my.list.domain.ExtraDataMapper;

public class Type {
    
    public Type(String name) {
        this.typeName = name;
    }

    private String typeName;
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    private Class<? extends ExtraData> extraDataClass = null;
    public Class<? extends ExtraData> getExtraDataClass() {
        return extraDataClass;
    }
    public void setExtraDataClass(Class<? extends ExtraData> extraDataClass) {
        this.extraDataClass = extraDataClass;
    }

    private ExtraDataMapper extraDataMapper = null;
    public ExtraDataMapper getExtraDataMapper() {
        return extraDataMapper;
    }
    public void setExtraDataMapper(ExtraDataMapper extraDataMapper) {
        this.extraDataMapper = extraDataMapper;
    }

    private boolean hasExtraData;
    public Boolean getHasExtraData() {
        return hasExtraData;
    }
    public void setHasExtraData(Boolean hasExtraData) {
        this.hasExtraData = hasExtraData;
    }

    private boolean hasExtraList;
    public Boolean getHasExtraList() {
        return hasExtraList;
    }
    public void setHasExtraList(Boolean hasExtraList) {
        this.hasExtraList = hasExtraList;
    }

    private boolean extraListUnique;
    public Boolean getExtraListUnique() {
        return extraListUnique;
    }
    public void setExtraListUnique(Boolean extraListUnique) {
        this.extraListUnique = extraListUnique;
    }

    private boolean extraListRequired;
    public Boolean getExtraListRequired() {
        return extraListRequired;
    }
    public void setExtraListRequired(Boolean extraListRequired) {
        this.extraListRequired = extraListRequired;
    }

    private NodeNormalizer nodeNormalizer = null;
    public NodeNormalizer getNodeNormalizer() {
        return nodeNormalizer;
    }
    public void setNodeNormalizer(NodeNormalizer nodeNormalizer) {
        this.nodeNormalizer = nodeNormalizer;
    }
    public void normalize(Node node) {
        if (this.nodeNormalizer != null) {
            this.nodeNormalizer.normalize(node);
        }
    }

    private ExcerptGenerator excerptGenerator = null;
    public ExcerptGenerator getExcerptGenerator() {
        return excerptGenerator;
    }
    public void setExcerptGenerator(ExcerptGenerator excerptGenerator) {
        this.excerptGenerator = excerptGenerator;
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
