package ru.fusionsoft.iu.dereferencer.enums;

import ru.fusionsoft.iu.dereferencer.reference.internal.Reference;

import java.io.File;

public enum FileFormat {
    YAML, JSON, NULL, UNDEF;

    public static FileFormat checkFileFormat(File checkingFile){
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

    public static FileFormat checkFileFormat(Reference ref){
        return checkFileFormat(new File(ref.getFileName()));
    }
}