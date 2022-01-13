package ru.fusionsoft.iu.dereferencer;
import ru.fusionsoft.iu.dereferencer.exceptions.InvalidReferenceException;
import ru.fusionsoft.iu.dereferencer.reference.external.GitLabReference;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Application {
    public static void main(String[] args) throws InvalidReferenceException, IOException, URISyntaxException, CloneNotSupportedException {
//        try {
//            Dereferencer.getResolve("https://raw.githubusercontent.com/guyfox2001/dataHolder/main/Nomenclature.yaml");
//        }
//        catch (URISyntaxException e) {
//            System.err.println("\nInvalid path\n");
//        } catch (IOException e) {
//            System.err.println("\nInvalid filename\n");
//        } catch (InvalidReferenceException e) {
//            e.printStackTrace();
//        }
//        Dereferencer.getResolve("https://gitlab.fusionsoft.ru/fusionsoft-ru/iu/info-universe/-/raw/master/iuData/Sample-univ/Nomenclature.yaml");
//        Dereferencer.getResolve(ReferenceFactory.makeReference(URI.create("iuData/Sample-univ/Nomenclature.yaml")));
//        Dereferencer.getResolve(new GitHubReference(
//                URI.create("https://raw.githubusercontent.com/guyfox2001/dataHolder/main/Nomenclature.yaml"),
//                "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"));
        Dereferencer.getResolve(new GitLabReference(
                URI.create("https://gitlab.fusionsoft.ru/api/v4/projects/27/repository/files/iuData%2fSample-univ%2fNomenclature%2eyaml/raw?ref=master"),
                "MJ8hcszC654epjgXs_4i"));
//        Dereferencer.getResolve(new LocalReference(URI.create("iuData/Sample-univ/Nomenclature.yaml")));
//        GitService gitService = new GitHubService();
//        LocalReference ref = new LocalReference(URI.create("#anchor"));
//        System.out.println(ref.getInternalUri());
//        System.out.println(gitService.get(new GitHubReference(
//                URI.create("https://raw.githubusercontent.com/guyfox2001/dataHolder/main/Nomenclature.yaml"),
//                "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"))
//        );
//    }

    }
}
