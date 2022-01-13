package ru.fusionsoft.iu.dereferencer.enums;

import java.net.URI;

/*
* EXTERNAL_FRAGMENT - это кейс для того случая, если у нас $ref указывает на отдельный репозиторий/сервер с файлом, содержащим путь внутри схемы
* LOCAL_FRAGMENT -  это кейс для того случая, если у нас $ref указывает на локально расположенный файл, содержащий путь внутри схемы
* EXTRNAL_DOCUMENT - это кейс для того случая, если у нас $ref указывает на отдельный репозиторий/сервер с файлом.
* LOCAL_DOCUMENT - это кейс для того случая, если у нас $ref указывает на локально расположенный файл.
* INTERNAL_FRAGMENT - это кейс когда $ref внутри схемы.
* UNDEF_REF - неизвестный $ref/
* */
public enum RefType {
    EXTERNAL_DOCUMENT_ANCHOR, EXTERNAL_DOCUMENT_PATH, EXTERNAL_DOCUMENT, LOCAL_DOCUMENT_ANCHOR, LOCAL_DOCUMENT, LOCAL_DOCUMENT_PATH, INTERNAL_ANCHOR, INTERNAL_PATH, UNDEF_REF;

    public static RefType checkFragmentType(URI uri){
        if (uri.getScheme() != null && uri.getScheme().length() != 1) {
            if (uri.getFragment() != null ) {
                if (uri.getFragment().contains("/")) return RefType.EXTERNAL_DOCUMENT_PATH;
                return RefType.EXTERNAL_DOCUMENT_ANCHOR;
            }
            return RefType.EXTERNAL_DOCUMENT;
        }
        if (uri.getPath().equals("") && uri.getFragment() != null) {
            if (uri.getFragment().contains("/")) return INTERNAL_PATH;
            return RefType.INTERNAL_ANCHOR;
        }
        if (uri.getFragment() != null){
            if (uri.getFragment().contains("/")) return LOCAL_DOCUMENT_PATH;
            return RefType.LOCAL_DOCUMENT_ANCHOR;
        }
        return RefType.LOCAL_DOCUMENT;
    }

    public static boolean isLocal(URI expecting){
        if ((expecting.getScheme()== null || expecting.getScheme().length() == 1 ) && !expecting.getPath().equals("")) return true;
        return false;
    }

    public static boolean isLocalPath(URI expecting){
        if (isLocal(expecting) && expecting.getFragment() != null && expecting.getFragment().contains("/")) return true;
        return false;
    }

    public static boolean isLocalAnchor(URI expecting){
        if (isLocal(expecting) && expecting.getFragment() != null && !expecting.getFragment().contains("/")) return true;
        return false;
    }

    public static boolean isExternal(URI expecting){
        if (expecting.getScheme()!= null && expecting.getScheme().length() != 1) return true;
        return false;
    }

    public static boolean isExternalPath(URI expecting){
        if (isExternal(expecting) && hasPath(expecting)) return true;
        return false;
    }

    public static boolean isGitHub(URI expecting){
        if (!expecting.getHost().equals("raw.githubusercontent.com")) return false;
        return true;
    }

    public static boolean isGitLab(URI expecting){
        if (!expecting.getHost().contains("gitlab")) return false;
        return true;
    }


    /*public static boolean isExternalAnchor(URI expecting)*/

    public static  boolean isInternalPath(URI expecting){
        if (isExternal(expecting) || isLocal(expecting) || !hasPath(expecting)) return false;
        return true;
    }

    public static boolean isInternalAnchor(URI expecting){
        if (isExternal(expecting) || isLocal(expecting) || !hasAnchor(expecting)) return false;
        return true;
    }

    public static boolean hasPath(URI expecting){
        if (expecting.getFragment() != null && expecting.getFragment().contains("/")) return true;
        return false;
    }
    public static boolean hasAnchor(URI expecting){
        if (expecting.getFragment() != null && !expecting.getFragment().contains("/")) return true;
        return false;
    }

}
