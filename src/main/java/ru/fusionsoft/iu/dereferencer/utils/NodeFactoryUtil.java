package ru.fusionsoft.iu.dereferencer.utils;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public class NodeFactoryUtil {
    public static JsonNodeFactory nodeFactory;

    public static JsonNodeFactory getNodeFactory(){
        if (nodeFactory == null)
            nodeFactory = new JsonNodeFactory(false);
        return nodeFactory;
    }
}
