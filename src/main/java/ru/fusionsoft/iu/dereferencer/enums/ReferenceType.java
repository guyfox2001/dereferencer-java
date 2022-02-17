package ru.fusionsoft.iu.dereferencer.enums;

import ru.fusionsoft.iu.dereferencer.reference.internal.Reference;

import java.net.URI;

/*
* EXTERNAL_FRAGMENT - это кейс для того случая, если у нас $ref указывает на отдельный репозиторий/сервер с файлом, содержащим путь внутри схемы
* LOCAL_FRAGMENT -  это кейс для того случая, если у нас $ref указывает на локально расположенный файл, содержащий путь внутри схемы
* EXTRNAL_DOCUMENT - это кейс для того случая, если у нас $ref указывает на отдельный репозиторий/сервер с файлом.
* LOCAL_DOCUMENT - это кейс для того случая, если у нас $ref указывает на локально расположенный файл.
* INTERNAL_FRAGMENT - это кейс когда $ref внутри схемы.
* UNDEF_REF - неизвестный $ref/
* */
public enum ReferenceType {
//    EXTERNAL_DOCUMENT_ANCHOR,
//    EXTERNAL_DOCUMENT_PATH,
//    EXTERNAL_DOCUMENT,
//    LOCAL_DOCUMENT_ANCHOR,
//    LOCAL_DOCUMENT,
//    LOCAL_DOCUMENT_PATH,
//    INTERNAL_ANCHOR,
//    INTERNAL_PATH,
//    UNDEF_REF;

    GITHUB_REFERENCE,
    GITLAB_REFERENCE,
    LOCAL_REFERENCE,
    INTERNAL_ANCHOR_REFERENCE,
    INTERNAL_PATH_REFERENCE,
    UNDEFINED_REFERENCE;


//    public static ReferenceType checkFragmentType(URI uri){
//        if (uri.getScheme() != null && uri.getScheme().length() != 1) {
//            if (uri.getFragment() != null ) {
//                if (uri.getFragment().contains("/")) return ReferenceType.EXTERNAL_DOCUMENT_PATH;
//                return ReferenceType.EXTERNAL_DOCUMENT_ANCHOR;
//            }
//            return ReferenceType.EXTERNAL_DOCUMENT;
//        }
//        if (uri.getPath().equals("") && uri.getFragment() != null) {
//            if (uri.getFragment().contains("/")) return INTERNAL_PATH;
//            return ReferenceType.INTERNAL_ANCHOR;
//        }
//        if (uri.getFragment() != null){
//            if (uri.getFragment().contains("/")) return LOCAL_DOCUMENT_PATH;
//            return ReferenceType.LOCAL_DOCUMENT_ANCHOR;
//        }
//        return ReferenceType.LOCAL_DOCUMENT;
//    }

    public static ReferenceType checkFragmentType(URI uri){
        if (isGitHub(uri)) return GITHUB_REFERENCE;

        if(isGitLab(uri)) return GITLAB_REFERENCE;

        if(isLocal(uri)) return  LOCAL_REFERENCE;

        if(isInternalAnchor(uri)) return  INTERNAL_ANCHOR_REFERENCE;

        if(isInternalPath(uri)) return INTERNAL_PATH_REFERENCE;

        return UNDEFINED_REFERENCE;
    }

    public static boolean isLocal(URI expecting){
        if ((expecting.getScheme()== null || expecting.getScheme().length() == 1 ) && !expecting.getPath().equals("")) return true;
        return false;
    }



    public static boolean isExternal(URI expecting){
        if (expecting.getScheme()!= null && expecting.getScheme().length() != 1) return true;
        return false;
    }


    public static boolean isGitHub(URI expecting){
        if (expecting.getHost() == null || !expecting.getHost().contains("github")) return false;
        return true;
    }

    public static boolean isGitLab(URI expecting){
        if (expecting.getHost() == null || !expecting.getHost().contains("gitlab")) return false;
        return true;
    }


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
