package ru.fusionsoft.iu.dereferencer.builders.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.jetbrains.annotations.NotNull;
import ru.fusionsoft.iu.dereferencer.builders.Builder;
import ru.fusionsoft.iu.dereferencer.enums.RefType;
import ru.fusionsoft.iu.dereferencer.exceptions.InvalidReferenceException;
import ru.fusionsoft.iu.dereferencer.factories.ReferenceFactory;
import ru.fusionsoft.iu.dereferencer.managers.impl.ManagerImpl;
import ru.fusionsoft.iu.dereferencer.reference.internal.Reference;
import ru.fusionsoft.iu.dereferencer.utils.MapperUtil;
import ru.fusionsoft.iu.dereferencer.utils.NodeFactoryUtil;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class FragmentBuilder implements Builder {

    private final Map<Reference, JsonNode> sourceFragments;
    private final Map<JsonNode, JsonNode> resolveFragments;
    private final JsonNode finalResult;
    private final Reference refOnMainFragment;
    private final ManagerImpl fileManager;

    public FragmentBuilder(Reference ref) throws URISyntaxException, IOException, InvalidReferenceException {
        fileManager = new ManagerImpl();
        refOnMainFragment = ref;
        sourceFragments = new HashMap<>();
        resolveFragments = new HashMap<>();
        finalResult = NodeFactoryUtil.getNodeFactory().objectNode();
        sourceFragments.put(refOnMainFragment, executeDocument(refOnMainFragment));
    }

    public JsonNode nestFragment(Reference ref) throws URISyntaxException, IOException, InvalidReferenceException {
        JsonNode key = sourceFragments.get(ref), result;
        if (key == null) { key = executeDocument(ref); sourceFragments.put(ref, key); traceDefs(key, key); }
        result = resolveFragments.get(key);
        return result;
    }

    public JsonNode merge(ObjectNode source, @NotNull ObjectNode target, Reference instance)
            throws URISyntaxException,
            IOException,
            InvalidReferenceException, CloneNotSupportedException {

        Map.Entry<String, JsonNode> ptr;
        Reference ref;
        for (Iterator <Map.Entry<String, JsonNode>> it = target.fields(); it.hasNext();){
            ptr = it.next();
            JsonNode nestedFragment, tValue = ptr.getValue(), sValue = source.path(ptr.getKey());
            if (ptr.getKey().equals("$defs"))continue;
            if (ptr.getKey().equals("$ref")){
                ref = ReferenceFactory.makeReference(URI.create(ptr.getValue().asText()));

                if (!RefType.isInternalPath(ref.getUri()) && !RefType.isInternalAnchor(ref.getUri())) {
                    ref = instance.getRel(ref);
                    instance = (Reference) ref.clone();
                }

                nestedFragment= nestFragment(ref);
                if(nestedFragment != null) {
                    source.setAll((ObjectNode) nestedFragment.deepCopy()); continue;
                }
                merge(source, (ObjectNode) _referenceResolve(ref, instance), instance);
                continue;
            }
            if (ptr.getKey().equals("allOf")){
                nestedFragment = resolveFragments.get(ptr.getValue());
                if (nestedFragment != null) {
                    source.setAll((ObjectNode) nestedFragment);
                    continue;
                }
                merge(source, (ObjectNode) _allOfResolve((ArrayNode) ptr.getValue(), instance), instance);
                continue;
            }
            if (sValue.isObject() && tValue.isObject()){
                source.set(ptr.getKey(), merge((ObjectNode) sValue, (ObjectNode) tValue, instance));
                continue;
            }
            if (tValue.isObject()) {
                source.set(ptr.getKey(),
                        merge(NodeFactoryUtil.getNodeFactory().objectNode(), (ObjectNode) tValue, instance));
                continue;
            }
            source.set(ptr.getKey(), tValue);
        }
        return source;
    }
// изменяет внутренние ссылки(ссылки внутри документа): #props to #props_Nomenclature
    public void traceDefs(@NotNull JsonNode fragment, @NotNull JsonNode document) throws URISyntaxException, InvalidReferenceException {
        if (!document.has("$defs")) return;
        ObjectNode defs = ((ObjectNode)document.path("$defs")),searched;
        Map.Entry<String, JsonNode> ptr;
        for (Iterator<Map.Entry<String, JsonNode>>it = fragment.fields(); it.hasNext();){
            ptr = it.next();
            if (ptr.getKey().equals("$ref")){
                String old = ptr.getValue().asText();
                String ref = old + "_" + fileManager.getLastReference().getFileName().replace(".yaml", "").replace(".json", "");
                switch (RefType.checkFragmentType(new URI(ref))){
                    case INTERNAL_PATH : { searched = (ObjectNode)search(defs, old.replace("#/", "").replace("$defs/", "").split("/"));break; }
                    case INTERNAL_ANCHOR : { searched = (ObjectNode)anchorSearch(defs, old.replace("#",""));break; }
                    default : {continue;}
                }
                ((ObjectNode)fragment).replace("$ref", new TextNode(ref));
                if (searched == null) throw new URISyntaxException("The value found by reference " + old +" is null, " +
                        "check if correct link in the " + fileManager.getLastUsedFile().getPath(), "Fragment search error");
                sourceFragments.put(ReferenceFactory.makeReference(ref), searched.without("$anchor"));
            }
            if (ptr.getValue().isObject()) traceDefs(ptr.getValue(), document);
            if (ptr.getValue().isArray()) {
                for (JsonNode elem : ptr.getValue()){ traceDefs(elem, document);}
            }
        }
    }

    @Override
    public JsonNode build() throws URISyntaxException, IOException, InvalidReferenceException, CloneNotSupportedException {
        JsonNode nonMerged = sourceFragments.get(refOnMainFragment);
        traceDefs(nonMerged, nonMerged);
        merge((ObjectNode) finalResult, (ObjectNode) nonMerged, refOnMainFragment);
        writeToFile(refOnMainFragment.getFileName());
        return finalResult;
    }

    public void writeToFile(String filename){
        filename = "derefernced" + filename;
        File path = new File( fileManager.getStartDirectory().toString() + "/dereferenced-fragments/");
        if(!path.exists()){path.mkdir();}
        File file = new File(path + "/" +filename);
        try {
            MapperUtil.getMapperInstance(file).writeValue(file, finalResult);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // todo:add support Download Manager
    ///////////////////////////////////////////////////////////////////////////


    public JsonNode executeDocument (Reference ref) throws IOException, InvalidReferenceException {
        URI internal;
        JsonNode searched = fileManager.getDocument(ref);

        if(searched == null) throw new InvalidReferenceException("Invalid relative! Unable to get a file for this reference: " + ref.toString());

        if ((internal = ref.getInternalUri()) != null){

        }
        System.out.println("Referenced file: " + ref.getFileName() +". From: " + ref.toString());
        return searched;
    }

    public JsonNode anchorSearch(JsonNode sNode, String anchor){
        if (sNode.get(anchor)!= null) return sNode.get(anchor);
        for (JsonNode it : sNode){
            if (it.path("$anchor").asText().equals(anchor)) {
                return it;
            }
        }
        return null;
    }

    public JsonNode search(JsonNode sNode, String[] path){
        for (String it : path){
            sNode = sNode.path(it);
            if (sNode.isMissingNode()) return null;
        }
        return sNode;
    }

    private JsonNode _allOfResolve(@NotNull ArrayNode allOf, Reference instance)
            throws URISyntaxException,
            IOException,
            InvalidReferenceException,
            CloneNotSupportedException {

        ObjectNode res = NodeFactoryUtil.getNodeFactory().objectNode();
        for (JsonNode elem : allOf){
            merge(res, (ObjectNode) elem , instance);
        }
        resolveFragments.put(allOf, res);
        return res;
    }
    private JsonNode _referenceResolve(Reference ref, Reference instance)
            throws URISyntaxException,
            IOException,
            InvalidReferenceException,
            CloneNotSupportedException {

        JsonNode res = merge(NodeFactoryUtil.getNodeFactory().objectNode(), (ObjectNode) sourceFragments.get(ref), instance);
        resolveFragments.put(sourceFragments.get(ref), res);
        return res;
    }
}
