package ru.fusionsoft.iu.dereferencer.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import ru.fusionsoft.iu.dereferencer.enums.FileFormat;
import ru.fusionsoft.iu.dereferencer.reference.internal.Reference;

import java.io.File;
import java.io.IOException;

public class MapperUtil {
    private static ObjectMapper mapperInstance = null;
    private static YAMLMapper yamlMapperInstance = null;
    private static JsonMapper jsonMapperInstance = null;

    private static void initYaml(){
        yamlMapperInstance = new YAMLMapper();
    }
    private static void initJson(){
        jsonMapperInstance = new JsonMapper();
        jsonMapperInstance.enable(SerializationFeature.INDENT_OUTPUT);
    }
    private static void init() { mapperInstance = new ObjectMapper(); }

    public static ObjectMapper getMapperInstance(){
        if(mapperInstance == null) init();
        return mapperInstance;
    }

    public static ObjectMapper getMapperInstance(File file) throws IOException {
        switch (FileFormat.checkFileFormat(file)){
            case JSON:
                if (jsonMapperInstance == null) initJson();
                return jsonMapperInstance;
            case YAML:
                if (yamlMapperInstance == null) initYaml();
                return yamlMapperInstance;
            case UNDEF:
                throw new IOException("Invalid file format ");
            default:
                throw new NullPointerException();
        }

    }
    public static ObjectMapper getMapperInstance(Reference ref) throws IOException {
        switch (FileFormat.checkFileFormat(ref)){
            case JSON:
                if (jsonMapperInstance == null) initJson();
                return jsonMapperInstance;
            case YAML:
                if (yamlMapperInstance == null) initYaml();
                return yamlMapperInstance;
            case UNDEF:
                throw new IOException("Invalid file format ");
            default:
                throw new NullPointerException();
        }
    }
}
