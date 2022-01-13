package ru.fusionsoft.iu.dereferencer.reference.internal;

import ru.fusionsoft.iu.dereferencer.enums.RefType;
import ru.fusionsoft.iu.dereferencer.exceptions.InvalidReferenceException;

import java.net.URI;
import java.util.Objects;

public abstract class Reference implements Cloneable {

    protected URI reference;
    protected String jsonRelPointerPath = "";
    protected String anchor = "";
    protected String fileName = "";

    public Reference(URI source) throws InvalidReferenceException {
        this.reference = source;
        _parceUri(source);
        if(RefType.hasPath(source))
            jsonRelPointerPath = source.getFragment();
        if(RefType.hasAnchor(source))
            anchor = source.getFragment();
    }

    protected void _parceUri(URI source) throws InvalidReferenceException {

    }

    public URI get(){
        return reference;
    }

    public String getInternal(){
        if(!jsonRelPointerPath.equals("")) return jsonRelPointerPath;
        if(!anchor.equals("")) return anchor;
        return null;
    }

    public URI getInternalUri(){
        if(!jsonRelPointerPath.equals("")) return URI.create("#" + jsonRelPointerPath);
        if(!anchor.equals("")) return URI.create("#" + anchor);
        return null;
    }

    public Reference getRel(String URI) throws InvalidReferenceException {
        return null;
    }

    public Reference getRel(Reference ref) throws InvalidReferenceException { return null;}

    public String getFileName(){ return fileName; }

    @Override
    public String toString() {
        return reference.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reference)) return false;
        Reference reference1 = (Reference) o;
        return reference.equals(reference1.reference) &&
                Objects.equals(jsonRelPointerPath, reference1.jsonRelPointerPath) &&
                Objects.equals(anchor, reference1.anchor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reference, jsonRelPointerPath, anchor);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
