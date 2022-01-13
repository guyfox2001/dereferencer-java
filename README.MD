# **Dereferencer**

#### Что и для чего :)

Данная библиотека библиотека предоставляет функционал, позволяющий разрешать ссылки в файлах формата **_JSON-Schema Document_**. 
Ключевое слово **"$ref"** — это аппликатор, который используется для ссылки на статически идентифицированную схему. Все референсы на другую схему составляются в соответствии с [RFC 2396: Uniform Resource Identifiers (URI): Generic Syntax](https://www.ietf.org/rfc/rfc2396.txt).

`Hierarchical URI  example:
    [scheme:][//authority][/path][?query][#fragment]
`

Стоит так же отметить, что для указания **"internal"** ссылок можно использовать ключевое слово **"$anchor"** или же прибегнуть к использованию [JSON Pointer](https://datatracker.ietf.org/doc/html/rfc6901#section-6)

#### Примеры ссылок и из разыменование

###### Локальные ссылки:

Имеем документ в локальном представлении(“foo.yaml”):
``` yaml
foo:
 $ref: "foo/bar.yaml"
bar:
 foo1: "value1"
 foo2: 
foo3: "value1"
$ref: "bar1.yaml"
 foo4: "value1"
```

В объекте foo существует ссылка `foo/bar.yaml`, которая указывает на документ, находящийся в папке foo.
Содержимое документа “`bar.yaml`” :

```yaml
bar-referenced:
 title: "text"
 obj-1:
   prop-1: 1
   prop-2: 2
   prop-3: 3
 obj-2:
   prop-1: 1
   prop-2: 2
   prop-3: 3
```

В данном документе простой фрагмент схемы.
	
Теперь рассмотрим файл “`bar1.yaml`”:

```yaml
allOf:
 - $ref: "first-array-item.yaml"
 - $ref: "second-array-item.yaml"
```

Это массив ссылок, который отмечен `allOf`.

Рассмотрим содержимое файлов `first-array-item.yaml` и `second-array-item.yaml` 
```yaml
title: "first-array-item.yaml"
 obj-array-item-1:
   prop-1: 1
   prop-2: "some-string"
 obj-array-item-2:
   prop-1: 1
   prop-2: 2
   prop-3: 3
   prop-4:
       title: "some object"
       prop-1: 1
       prop-2: 2
```

Содержимое  `first-array-item.yaml` 

```yaml
title: "second-array-item.yaml"
 obj-array-item-1:
   prop-1: "newValue"
   prop-2:
     prop-1: 1
     prop-2: 2
     prop-3: 3
 obj-array-item-2:
   prop-1: 4
   prop-2: 5
   prop-3: 6
	
```
Содержимое  `second-array-item.yaml`

Результатом разыменования будет следующая схема:

```yaml
foo:
 bar-referenced:
   title: "text"
   obj-1:
     prop-1: 1
     prop-2: 2
     prop-3: 3
   obj-2:
     prop-1: 1
     prop-2: 2
     prop-3: 3
bar:
 foo1: "value1"
 foo2:
   foo3: "value1"
   title: "second-array-item.yaml"
     obj-array-item-1:
       prop-1: "newValue"
       prop-2:
         prop-1: 1
         prop-2: 2
         prop-3: 3
     obj-array-item-2:
       prop-1: 4
       prop-2: 5
       prop-3: 6
       prop-4:
         title: "some object"
         prop-1: 1
         prop-2: 2
 foo4: "value1"
```

**Первая ссылка** заменена содержимым `bar.yaml`.
**Вторая ссылка** - это результат мёржа ссылок массива `allOf` из файла `bar1.yaml` `first-array-item.yaml` и `second-array-item.yaml`.

###### Внутренние ссылки

Допустим у нас есть некоторый документ `foo.yaml`

```yaml
foo:
    title: "example object"
    allOf:
      - $ref: "#anchorExample"
      - $ref: "#/json/pointer"
$defs: 
    anchorObject:
        title: "Acnhor reference example"
        $anchor: "anchorExample"
        prop-1: 1
        prop-2: 2
    json:
        pointer:
            title: "JSON pointer example"
            prop-1: 1
            prop-2: 2
```

В данном примере приведены примеры с использованием `$anchor` и [JSON Pointer](https://datatracker.ietf.org/doc/html/rfc6901#section-6)

Результатом разименования будет:

```yaml
foo:
    title: "example object"
    anchorObject:
        title: "Acnhor reference example"
        $anchor: "anchorExample"
        prop-1: 1
        prop-2: 2
    json:
        pointer:
            title: "JSON pointer example"
            prop-1: 1
            prop-2: 2
```

###### Внешние ссылки

Чтобы получить файл со схемой из **GitHub** или **GitLab** необходимо использовать API:

Для GitHub: `https://raw.githubusercontent.com/[user]/[repos_name]/[branch]/[some_path]` 

Для GitLab: `https://gitlab.ru/api/v4/projects/[project_ID]/repository/files/[some_encoded_path]?ref=[branch]` 

 #### Использование библиотеки
 
Одноимённый класс `Dereferencer` содержит метод `getResolve(Reference)` в который передаётся ссылка на **source fragment**

###### GitLab и GitHub взаимодействие

```java
public class Application {
    public static void main(String[] args) throws InvalidReferenceException, IOException, URISyntaxException, CloneNotSupportedException {
        Dereferencer.getResolve(new GitLabReference(
                URI.create("https://gitlab.fusionsoft.ru/api/v4/projects/27/repository/files/iuData%2fSample-univ%2fNomenclature%2eyaml/raw?ref=master"),
                "MJ8hcszC654epjgXs_4i"));
    }
}
```

```java
public class Application {
    public static void main(String[] args) throws InvalidReferenceException, IOException, URISyntaxException, CloneNotSupportedException {
        Dereferencer.getResolve(new GitHubReference(
                URI.create("https://raw.githubusercontent.com/guyfox2001/dataHolder/main/Nomenclature.yaml"),
                "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"));
    }
}
```

Для взаимодействия с этими сервисами необходимо использовать **токен** доступа только для **приватных репозиториев**.

###### Локальные файлы

Ссылки на локальные файлы могут быть, как относительными, так и абсолютными

```java
public class Application {
    public static void main(String[] args) throws InvalidReferenceException, IOException, URISyntaxException, CloneNotSupportedException {
        Dereferencer.getResolve(ReferenceFactory.makeReference(URI.create("iuData/Sample-univ/Nomenclature.yaml")));
    }
}
```

```java
public class Application {
    public static void main(String[] args) throws InvalidReferenceException, IOException, URISyntaxException, CloneNotSupportedException {
        Dereferencer.getResolve(ReferenceFactory.makeReference(URI.create("W://dereferencer/iuData/Sample-univ/Nomenclature.yaml")));
    }
}
```

#### Фабрика ссылок 

`ReferenceFactory` - это инструмент построения ссылок, в особенности внешних. В примере выше показано, как создаётся локальная ссылка. 

###### Построение ссылки на репозиторий GitHub

Можно напрямую вызвать метод, который пострит **`GitHubReference`** `makeGitHubReference(String host, String user, String branch, String repository, String path, String fragment, String TOKEN)`
, или использовать универсальный метод `makeReference(URI ref, String TOKEN)`.

```java
public class Application {
    public static void main(String[] args) throws InvalidReferenceException, IOException, URISyntaxException, CloneNotSupportedException {
        GitReference ref = ReferenceFactory.makeGitHubReference(
            "someHost",
            "someUser",
            "someBranch",
            "someRepos",
            "somePathToFileOnRepos",
            "some #fragment",
            "someTOKEN"
        );
    }
}
```

###### Построение ссылки на Git Lab

Аналогично с предыдущим пунктом можем вызвать два метода для построения **`GitLabReference`**. 
`makeGitLabReference(String host, Integer projectId, String branch, String path, Boolean raw, String fragment, String TOKEN)`

```java
    public class Application {
        public static void main(String[] args) throws InvalidReferenceException, IOException, URISyntaxException, CloneNotSupportedException {
            GitReference ref = ReferenceFactory.makeGitLabReference(
                "someHost",
                27, //projectID
                "someBranch",
                "somePathToFileOnRepos",
                true,
                "some #fragment",
                "someTOKEN"
            );
        }
    }
```