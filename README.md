# Model-Rerank : Reranking a set of records containing model features based on a given model

__com.upwork.common.rerank__:__rerank__ is a library to rerank a set of instances containing features using a
pre-configured [Weka][weka] model. The library is designed to load and manage multiple [Weka][weka] models to enable comparative
evaluation of two or more models.

#### Where to use the library
This library can be used for reranking a set of records or data instances based on a machine learning model built using
[Weka][weka]. 

A typical use case is of reranking of search results returned by a search engine. For such an use case,
the input to the library is a list of data instances corresponding to each of the search results and a class label
whose score the results are reranked on. Each of the data instances contains a set of features which are required by the
 model. Output is an object composed of instance ids which are reranked and useful debugging information.

For the details around how to use the library, please look at "How to use" section below.

#### Overview

The library loads the model configuration files and the model binary files, optionally caches the model instances in the 
memory and can execute a model on a set of records to rerank the records. It also provide valuable debug info for a 
deeper dive into the performance of the model. 

The model binaries must have an extension _.model_ and the configuration for a model should be a _.json_ file 
containing the specification for the model and its features. The library provides an utility class
[JsonConfigGenerator][configgen] to convert the model specification from the weka standard _.ARFF_ format into the
custom _.json_ config this library takes. The library expects that all the files related to a model have a name which
 follows the convention `modelName.extension`. For example, if the model name is `modelZ` then the name of the the
 model binary must be `modelZ.model`, of the _.arff_ file must be `modelZ.arff` and of the generated _.json_ file is
  `modelZ.json`. The library doesn't support `date` or [`relation`][multiInstance] weka attribute [types][wekaTypes]
  at present.

The library chose custom format over the standard _.ARFF_ format so as to be sufficiently extensible for real time
use cases where each of the features may have additional configuration for their data sources or functions to compute
  them on the fly. That is how  we use it at [Upwork][upwork], however the current version of the library doesn't
  expose that extended functionality. We may decide to do so in future.

 You can read more about the [Weka][weka] and [ARFF][arff] by following the links.

#### Prerequisites
1. Java version 1.8 or greater
2. Maven version 3.2.1 or greater

#### How to use

Please follow the following steps to make use of the library in an application.

1. **Add Dependency** : Include the following maven dependency in your pom.xml

    ```
    <dependency>
        <groupId>com.upwork.common.rerank</groupId>
        <artifactId>rerank</artifactId>
        <version>{version}</version>
    </dependency>
    ```

2. **Configure** : Set the following properties in a file named __config.properties__ and make it available on the 
classpath of the application or set the properties as system properties through the program.

    ```
    ## Relative or absolute path of the repository where model binaries and their configs are stored.
    rerank.models.repo=../sample_models  
    
    ## Names of the supported models
    rerank.models.supported=rerank_model 
    ```

    The library uses [Archaius][archaius] for configuration management. The default __config.properties__ file is 
    available at [Default Config][defaultConfig]

3. **Convert ARFF to JSON** : [JsonConfigGenerator][configgen] can be used for this purpose. The usage is as follows.

    ```
    java com.upwork.rerank.apputils.JsonConfigGenerator <modelName>
    ```
    e.g. for a model name modelZ

    ```
    java com.upwork.rerank.apputils.JsonConfigGenerator modelZ
    ```

    The above command will expect a file `modelZ.arff` in the model store repo directory set by the property
    `rerank.models.repo` and generate a file named `modelZ.json` in the same directory. Open `modelZ.json` and make 
    sure the `name` and `shortName` of the models are as expected. They are exactly same as the name of the @rel in 
    the weka _.arff_ file, however they should be modified to appropriately reflect the name of the model. The _
    .json_ file can be optionally be formatted for easier readability.

4. **Create Instances and Rerank** : Create instances of features and use the library to rerank them. A typical 
usage is as follows.

    ```
    //This is the name of the model which is used for scoring each of the instances
    String modelName = "modelZ"; 
    
    //This is the class label on which the instances are scored
    String classLableToRerank = "1"; 
    
    //Convert the domain specific data instances to a list of TInstance
    List<TInstance> instances = getData(); 
    
    //Get an instance of the rerank lib
    RerankLib lib = RerankLibFactory.getInstance(2, 10).getRerankLib(modelName); 
    
    //Rerank using the lib
    RerankResultSet rerankResultSet = lib.rerank(instances, classLableToRerank); 
    ```

    The project includes a sample application [SampleRerankApplication][sampleApp] which uses a sample model named 
    _rerank_model_ to rerank a set of instances. While in an actual application the data i.e. List of TInstance
    would come from application at runtime, the sample application makes use of the data already available in the model 
    _.arff_ file to illustrate the usage. Otherwise, the _.arff_ is of no use after it is converted into _.json_ config 
    and can be done away with thereafter.

#### _.json_ config file format

The json format is illustrated below through the config for a sample model _rerank_model_ included in the
project. Every feature can optionally have another attribute name `customConfig` to include application specific 
custom configuration e.g. data source specifications.

    ```
    {
      "features": [
        {
          "name": "a",
          "dataType": "numeric"
        },
        {
          "name": "b",
          "dataType": "numeric"
        },
        {
          "name": "c",
          "dataType": "numeric"
        },
        {
          "name": "d",
          "dataType": "numeric"
        },
        {
          "name": "e",
          "dataType": "numeric"
        },
        {
          "name": "f",
          "dataType": "nominal",
          "values": [
            "0",
            "1"
          ],
          "class": true
        }
      ],
      "name": "rerank_model",
      "shortName": "rerank_model"
    }
    ```
    
#### Known Issues and Limitations
1. The RerankLib can load multiple models and caches them as per the arguments supplied while creating it. It uses an LRU cache internally.
2. If the class/target data type is a "numeric" then a classLabel is not required to be supplied to rerank call.

#### Point of Contact
Suggestions and comments are most welcome. Please ping me at [atpuga(reverse spelled) at upwork.com]

[configgen]:./rerank/src/main/java/com/upwork/rerank/apputils/JsonConfigGenerator.java
[defaultConfig]:./rerank/src/main/resources/config.properties
[sampleApp]:./rerank-example/src/main/java/com/upwork/rerankexample/SampleRerankApplication.java
[weka]:http://www.cs.waikato.ac.nz/ml/weka/
[arff]:http://weka.wikispaces.com/ARFF
[upwork]:http://upwork.com
[archaius]:https://github.com/Netflix/archaius
[multiInstance]:https://weka.wikispaces.com/Multi-instance+classifications
[wekaTypes]:http://www.cs.waikato.ac.nz/ml/weka/arff.html