package ru.fusionsoft.iu.dereferencer.reference;

import ru.fusionsoft.iu.dereferencer.exceptions.InvalidReferenceException;

import java.net.URI;

public class LocalReference extends Reference{



    public LocalReference(URI source) throws InvalidReferenceException {
        super(source);
    }

    @Override
    protected void _parceUri(URI source) throws InvalidReferenceException {
        String pth = reference.getPath();
        fileName = pth.substring(pth.lastIndexOf("/")+1);
    }

    @Override
    public Reference getRel(String relUri) throws InvalidReferenceException {
        return new LocalReference(reference.resolve(URI.create(relUri)));
    }

    @Override
    public Reference getRel(Reference ref) throws InvalidReferenceException {
        return new LocalReference(reference.resolve(ref.get()));
    }

}
