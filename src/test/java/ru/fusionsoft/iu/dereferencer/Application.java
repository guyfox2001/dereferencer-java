package ru.fusionsoft.iu.dereferencer;
import ru.fusionsoft.iu.dereferencer.exceptions.InvalidReferenceException;
import ru.fusionsoft.iu.dereferencer.factories.ReferenceFactory;
import ru.fusionsoft.iu.dereferencer.reference.external.GitHubReference;
import ru.fusionsoft.iu.dereferencer.reference.external.GitLabReference;
import ru.fusionsoft.iu.dereferencer.utils.GitTokensUtill;
import ru.fusionsoft.iu.dereferencer.utils.MapperUtil;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Application {
    public static void main(String[] args) throws InvalidReferenceException, IOException, URISyntaxException, CloneNotSupportedException {


        GitTokensUtill.getGitTokensInstance().put("guyfox2001", "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");
        GitTokensUtill.getGitTokensInstance().put("27", "MJ8hcszC654epjgXs_4i");
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
//        Dereferencer.getResolve(ReferenceFactory.makeReference(URI.create("W://dereferencer/iuData/Sample-univ/Nomenclature.yaml")));
        Dereferencer.getResolve(new GitHubReference(
                URI.create("https://raw.githubusercontent.com/guyfox2001/dataHolder/main/fusionsoft-data/Nomenclature.yaml"),
                "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"));
//        Dereferencer.getResolve(new GitHubReference(
//                URI.create("https://raw.githubusercontent.com/guyfox2001/dataHolder/main/absolute-links/Nomenclature.yaml"),
//                null));
//        Dereferencer.getResolve(new GitLabReference(
//                URI.create("https://gitlab.fusionsoft.ru/api/v4/projects/27/repository/files/iuData%2fSample-univ%2fNomenclature%2eyaml/raw?ref=master"),
//                "MJ8hcszC654epjgXs_4i"));
//        Dereferencer.getResolve(new GitLabReference(
//                URI.create("https://gitlab.fusionsoft.ru/api/v4/projects/27/repository/files/iuData%2fSample-univ%2fAggregate%2eyaml/raw?ref=master"),
//                "MJ8hcszC654epjgXs_4i"));
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
