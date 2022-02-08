# Оглавление
1. [Dereferencer](#dereferencer)
    1. [GitHub API](#для-github)
    2. [GitLab API](#для-gitlab)
2. [How to use](#описание-основных-объектов-взаимодействия)
    1. [Основоной класс](#основоной-класс-библиотеки)
    2. [Ссылки](#ссылки)
        1. [Reference](#reference)
        2. [LocalReference](#localreference)
        3. [GitReference](#gitreference)
        5. [GitHubReference](#githubreference)
        6. [GitLabReference](#gitlabreference)
    3. [Фабрика ссылок](#фабрика-ссылок)




# **Dereferencer**

Dereferencer - это библиотека, предназначенная для разыменования ссылок **JSON/YAML Schema**, отмеченных кейвордом **`$ref`** внутри документа:

```yaml
    someobj:
      $ref: "foo.yaml"
```
 
Ключевое слово **`$ref`** — это аппликатор, который используется для ссылки на статически идентифицированную схему. 
Процесс _**разыменования**_ - это получение объединённого **JSON/YAML Schema Document** из изначальной совокупности таких документов.
Значение свойства аппликатора **`$ref`** - это [RFC 2396: Uniform Resource Identifiers (URI): Generic Syntax](https://www.ietf.org/rfc/rfc2396.txt). Частный случай `URI` - это ссылки для 
сервисов GitHub и GitLab. Чтобы получить документ из этих сервисов, необходимо использовать их API. 

###### Для GitHub:
    https://raw.githubusercontent.com/[user]/[repos_name]/[branch]/[some_path_in_repos]
    
###### Для GitLab:
    https://gitlab.ru/api/v4/projects/[project_ID]/repository/files/[some_encoded_path_in_repos]/raw?ref=[branch]

Для работы с JSON/YAML файлами была использована библиотека [Jackson API](https://github.com/FasterXML/jackson.git)


## How to use:

### Основной класс библиотеки
Объявление: `public class Dereferencer`

Возвращаемая `JsonNode` - это результат слияния всех документов из пула схем в одну. 
Входной - это экземпляр класса `Reference` (ссылка).



|Return Value| Modificator |Method |
|:-----------------------:|:-----------------------:| ----------------------- |
|`JsonNode`| `public` | `static JsonNode getResolve(Reference ref)` | 

### Ссылки



#### Reference


~~Это клас родитель являющийся небольшим "расширением" класса [URI](https://docs.oracle.com/javase/8/docs/api/java/net/URI.html). 
Помимо основного синтаксиса `URI`: [RFC 2396: Uniform Resource Identifiers (URI): Generic Syntax](https://www.ietf.org/rfc/rfc2396.txt) (все ссылки должны быть составлены согласно этому стандарту), 
есть некоторые особенности ссылок, например: `AccessToken` - токен доступа к приватному репозиторию , `EncodedPath` - это часть `/:filepath` исрользуемая в [API GitLab](https://docs.gitlab.com/ee/api/repository_files.html#get-raw-file-from-repository) и т.д.
`Refefence` - содержит эти разобранные "свойства"**(отображения)** в себе~~  

Этот класс-обёртка для [`public final class URI extends Object implements Comparable<URI>, Serializable`](https://docs.oracle.com/javase/8/docs/api/java/net/URI.html). 
При создании **нового экземпляра**, из входного `URI` разбираются некоторые **особенности (отображения)** ссылки.
В большей степени применимо это для внешних ссылок: ветка репозитория, путь внутри репозитория и т.д.


Объявление: `public abstract class Reference implements Cloneable`

|Modificator| Type | Member|
|:------------:|:------------:| :----------|
|`protected`|`URI`   |`reference`|
|`protected`|`String`|`jsonPointerPath`|
|`protected`|`String`|`anchor`|
|`protected`|`String`|`fileName`|

|Modificator|Constructors|
|:------------:| :----------|
| `public` | `Reference(URI source)` |


|Modificator | Return Value|Method | Description|
|:-----------------------:|:-----------------------:| ----------------------- | :----------------------- |
|`protected`|  `void`      | `_parceUri(URI source)`           | Разбирает входной `URI`, извлекая необходимые значения|
|`public`   |  `URI`       | `getUri()`                        | Возвращает экземпляр класса `URI`
|`public`   |  `String`    | `getInternal()`                   | Возвращает `#uri-fragment` типа `String`
|`public`   |  `URI`       | `getInternalUri()`                | Возвращает `#uri-fragment` типа `URI`|
|`public`   |  `String`    | `getFileName()`                   | Возвращает имя файла, в котором находится **`JSON/YAML Schema Document`**|
|`public`   |  `Reference` | `getRel(String relativeUriString)`| Возвращает новый экземпляр класса `Reference`. Входной параметр: строка `relativeStringUri`, являющаяся относительной ссылкой на другой `JSON/YAML Schema Document`| 
|`public`   |  `Reference` | `getRel(Reference relativeRef)`   | Возвращает новый экземпляр класса `Reference`. Входной параметр: экземпляр класса `Reference` `relativeRef`, являющийся относительной ссылкой на другой `JSON/YAML Schema Document`| 

#### LocalReference

Наследник класса `Reference`, являющийся отображением ссылка на документы, расположенные локально (непсоредственно на физической машине).
Стоит так же отметить, что для указания ссылок на **`#fragment`** можно использовать ключевое слово **`$anchor`** или же использовать [JSON Pointer](https://datatracker.ietf.org/doc/html/rfc6901#section-6)

Объявление: `public class LocalReference extends Reference`

**Примеры**:
```
    W://some/Absolute/path/to/file.yaml
    /some/path/relative/from/root/to/file.yaml#/with/json/pointer
    ../relative/path/file.yaml#withAnchor    
```

|Modificator|Constructors|
|:------------:| :----------|
| `public` | `LocalReference(URI source)` |


|Overrided |Modificator | Return Value |Method | Description|
|:-----------------------:|:-----------------------:|:-----------------------:| ----------------------- | :----------------------- |
| + |`protected`|  `void`      | `_parceUri(URI source)`           | Разбирает входной `URI`, извлекая необходимые значения|
| - |`public`   |  `URI`       | `getUri()`                        | Возвращает экземпляр класса `URI`
| - |`public`   |  `String`    | `getInternal()`                   | Возвращает `#uri-fragment` типа `String`
| - |`public`   |  `URI`       | `getInternalUri()`                | Возвращает `#uri-fragment` типа `URI`|
| - |`public`   |  `String`    | `getFileName()`                   | Возвращает имя файла, в котором находится **`JSON/YAML Schema Document`**|
| + |`public`   |  `Reference` | `getRel(String relativeUriString)`| Возвращает новый экземпляр класса `Reference`. Входной параметр: строка `relativeStringUri`, являющаяся относительной ссылкой на другой `JSON/YAML Schema Document`| 
| + |`public`   |  `Reference` | `getRel(Reference relativeRef)`   | Возвращает новый экземпляр класса `Reference`. Входной параметр: экземпляр класса `Reference` `relativeRef`, являющаяся относительной ссылкой на другой `JSON/YAML Schema Document`| 

#### GitReference

Наследник класса `LocalReference`, являющийся отображением внешних ссылок для Git-сервисов.

Объявление: `public abstract class GitReference extends LocalReference`

|Modificator| Type | Member|
|:------------:|:------------:| :----------|
|`protected`|`String`|`accessTOKEN`|
|`protected`|`String`|`pathToFile`|
|`protected`|`String`|`hashFileName`|

**Note!**. Для получения доступа к приватному репозиторию необходимо использовать `Access Token`. 

|Modificator|Constructors|
|:------------:| :----------|
| `public` | `GitReference(URI source)` |
| `public` | `GitReference(URI source, String TOKEN)` |


|Overrided |Modificator | Return Value |Method | Description|
|:-----------------------:|:-----------------------:|:-----------------------:| ----------------------- | :----------------------- |
| - |`protected`|  `void`      | `_parceUri(URI source)`           | Разбирает входной `URI`, извлекая необходимые значения|
| - |`public`   |  `URI`       | `getUri()`                        | Возвращает экземпляр класса `URI`
| - |`public`   |  `String`    | `getInternal()`                   | Возвращает `#uri-fragment` типа `String`
| - |`public`   |  `URI`       | `getInternalUri()`                | Возвращает `#uri-fragment` типа `URI`|
| - |`public`   |  `String`    | `getFileName()`                   | Возвращает имя файла, в котором находится **`JSON/YAML Schema Document`**|
| - |`public`   |  `Reference` | `getRel(String relativeUriString)`| Возвращает новый экземпляр класса `Reference`. Входной параметр: строка `relativeStringUri`, являющаяся относительной ссылкой на другой `JSON/YAML Schema Document`| 
| - |`public`   |  `Reference` | `getRel(Reference relativeRef)`   | Возвращает новый экземпляр класса `Reference`. Входной параметр: экземпляр класса `Reference` `relativeRef`, являющаяся относительной ссылкой на другой `JSON/YAML Schema Document`| 
| - |`public`   |  `String`    | `getAccesTOKEN()`                 | Возвращает `accesTOKEN`|
| - |`public`   |  `String`    | `setAccesTOKEN(String Token)`     | Устанавливает `accesTOKEN = Token`|
| - |`public`   |  `String`    | `getPathToFile()`                 | Возвращает относительный путь до файла внутри репозитория. См.далее `GitLabReference` и `GitHubReference`|
| - |`public`   |  `String`    | `getHashFileName()`               | Возвращает имя файла, являющееся результатом хеширования ссылки методом `MD5`|

#### GitHubReference

Наследник `GitReference`. Это отображение формата ссылок, используемых в [GitHub API](#для-github).

|Modificator| Type | Member|
|:------------:|:------------:| :----------|
|`protected`|`String`|`user`|
|`protected`|`String`|`branch`|
|`protected`|`String`|`repo`|


|Modificator|Constructors|
|:------------:| :----------|
| `public` | `GitHubReference(URI source)` |
| `public` | `GitHubReference(URI source, String TOKEN)` |


|Overrided |Modificator | Return Value |Method | Description|
|:-----------------------:|:-----------------------:|:-----------------------:| ----------------------- | :----------------------- |
| + |`protected`|  `void`      | `_parceUri(URI source)`           | Разбирает входной `URI`, извлекая необходимые значения|
| - |`public`   |  `URI`       | `getUri()`                        | Возвращает экземпляр класса `URI`
| - |`public`   |  `String`    | `getInternal()`                   | Возвращает `#uri-fragment` типа `String`
| - |`public`   |  `URI`       | `getInternalUri()`                | Возвращает `#uri-fragment` типа `URI`|
| - |`public`   |  `String`    | `getFileName()`                   | Возвращает имя файла, в котором находится **`JSON/YAML Schema Document`**|
| + |`public`   |  `Reference` | `getRel(String relativeUriString)`| Возвращает новый экземпляр класса `Reference`. Входной параметр: строка `relativeStringUri`, являющаяся относительной ссылкой на другой `JSON/YAML Schema Document`| 
| + |`public`   |  `Reference` | `getRel(Reference relativeRef)`   | Возвращает новый экземпляр класса `Reference`. Входной параметр: экземпляр класса `Reference` `relativeRef`, являющаяся относительной ссылкой на другой `JSON/YAML Schema Document`| 
| - |`public`   |  `String`    | `getAccesTOKEN()`                 | Возвращает `accesTOKEN`|
| - |`public`   |  `String`    | `setAccesTOKEN(String Token)`     | Устанавливает `accesTOKEN = Token`|
| - |`public`   |  `String`    | `getPathToFile()`                 | Возвращает относительный путь до файла внутри репозитория. См.далее `GitLabReference` и `GitHubReference`|
| - |`public`   |  `String`    | `getHashFileName()`               | Возвращает имя файла, являющееся результатом хеширования ссылки методом `MD5`|
| - |`public`   |  `String`    | `getUser()`                       | Возвращает имя пользователя, которому принадлежит данный репозиторий|
| - |`public`   |  `String`    | `getRepo()`                       | Возвращает название репозитория|
| - |`public`   |  `String`    | `getBranch()`                     | Возвращает ветку, указанную в ссылке|

#### GitLabReference

Наследник `GitReference`. Это отображение формата ссылок, используемых в [GitLab API](#для-gitlab).

|Modificator| Type | Member|
|:------------:|:------------:| :----------|
|`protected`|`String` |`pathToFileEncoded`|
|`protected`|`String` |`branch`|
|`protected`|`Integer`|`projectID`|


|Modificator|Constructors|
|:------------:| :----------|
| `public` | `GitHubReference(URI source)` |
| `public` | `GitHubReference(URI source, String TOKEN)` |


|Overrided |Modificator | Return Value |Method | Description|
|:-----------------------:|:-----------------------:|:-----------------------:| ----------------------- | :----------------------- |
| + |`protected`|  `void`      | `_parceUri(URI source)`           | Разбирает входной `URI`, извлекая необходимые значения|
| - |`public`   |  `URI`       | `getUri()`                        | Возвращает экземпляр класса `URI`
| - |`public`   |  `String`    | `getInternal()`                   | Возвращает `#uri-fragment` типа `String`
| - |`public`   |  `URI`       | `getInternalUri()`                | Возвращает `#uri-fragment` типа `URI`|
| - |`public`   |  `String`    | `getFileName()`                   | Возвращает имя файла, в котором находится **`JSON/YAML Schema Document`**|
| + |`public`   |  `Reference` | `getRel(String relativeUriString)`| Возвращает новый экземпляр класса `Reference`. Входной параметр: строка `relativeStringUri`, являющаяся относительной ссылкой на другой `JSON/YAML Schema Document`| 
| + |`public`   |  `Reference` | `getRel(Reference relativeRef)`   | Возвращает новый экземпляр класса `Reference`. Входной параметр: экземпляр класса `Reference` `relativeRef`, являющаяся относительной ссылкой на другой `JSON/YAML Schema Document`| 
| - |`public`   |  `String`    | `getAccesTOKEN()`                 | Возвращает `accesTOKEN`|
| - |`public`   |  `String`    | `setAccesTOKEN(String Token)`     | Устанавливает `accesTOKEN = Token`|
| - |`public`   |  `String`    | `getPathToFile()`                 | Возвращает относительный путь до файла внутри репозитория. См.далее `GitLabReference` и `GitHubReference`|
| - |`public`   |  `String`    | `getHashFileName()`               | Возвращает имя файла, являющееся результатом хеширования ссылки методом `MD5`|
| - |`public`   |  `Integer`   | `getProjectId()`                  | Возвращает `ID` проекта (репзитория) |
| - |`public`   |  `String`    | `getBranch()`                     | Возвращает ветку, указанную в ссылке|

### Фабрика ссылок 

Это функциональный класс, содержащий в себе статические методы, которые создают экземпляр необходимой ссылки.

Объявление: `public class ReferenceFactory`

|Modificator | Return Value|Method | Description|
|:-----------------------:|:-----------------------:| ----------------------- | :----------------------- |
|`public`| `GitLabReference` | `makeGitLabReference(String host, Integer projectId, String branch, String path, String fragment, String TOKEN)`              | Создаёт экземпляр `GitLabReference`, `host` - это хост репозитория `GitLab` (может быть корпоративный); default: `gitlab.ru`, `projectID` - ID проекта , `branch` - целевая ветка, `path` - путь внутри репозитория, `fragment` - путь внутри документа, `TOKEN` - токен доступа|
|`public`| `GitHubReference` | `makeGitHubReference(String host, String user, String branch, String repository, String path, String fragment, String TOKEN)` | Создаёт экземпляр `GitHubReference`, `host` - это хост репозитория `GitHub` (может быть корпоративный); default: `raw.githubusercontent.com`, `repo` - репозиторий в котором находится документ, `user` - это владаелец репозитория, `branch` - целевая ветка, `path` - путь внутри репозитория, `fragment` - путь внутри документа, `TOKEN` - токен доступа|
|`public`| `Reference`       | `makeReference(String ref)`                                                                                                   | Создаёт экземпляр `Reference` не явно, если это ссылка на GitHub/Gitlab, то токен будет пустой строкой|
|`public`| `Reference`       | `makeReference(String ref, String TOKEN)`                                                                                     | Создаёт экземпляр `GitReference`.|
|`public`| `Reference`       | `makeReference(URI source)`                                                                                                   | Создаёт экземпляр `Reference` не явно, если это ссылка на GitHub/Gitlab, то токен будет пустой строкой|
|`public`| `Reference`       | `makeReference(URI source, String TOKEN)`                                                                                     | Создаёт экземпляр `GitReference`.|

## Примеры ссылок и их разыменование

### Локальные ссылки:

Имеем документ в локальном представлении(“foo.yaml”):
```yaml
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

### Внутренние ссылки

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

### Внешние ссылки

Чтобы получить файл со схемой из **GitHub** или **GitLab** необходимо использовать API этих сервисов:

Для GitHub: `https://raw.githubusercontent.com/[user]/[repos_name]/[branch]/[some_path]` 

Для GitLab: `https://gitlab.ru/api/v4/projects/[project_ID]/repository/files/[some_encoded_path]/raw?ref=[branch]`
  

