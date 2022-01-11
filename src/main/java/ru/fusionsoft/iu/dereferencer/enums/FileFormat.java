package ru.fusionsoft.iu.dereferencer.enums;

import java.io.File;

public enum FileFormat {
    YAML, JSON, NULL, UNDEF;

    public static FileFormat chekFileFormat(File checkingFile){
        if (checkingFile == null) return FileFormat.NULL;
        switch (checkingFile.getName().substring(checkingFile.getName().lastIndexOf(".") + 1) ){
            case "yaml":{
                return FileFormat.YAML;
            }
            case "json":{
                return FileFormat.JSON;
            }
            default:{
                return FileFormat.UNDEF;
            }
        }
    }
}