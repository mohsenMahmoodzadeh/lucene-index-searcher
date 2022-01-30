
# Lucene Index Searcher

This project is developed with Apache Lucene, a Java full-text search engine software library, for building a simple indexer and index searcher.

## Environment

- Java: 15.0.2
- Lucene: 7.4.0


## Setup Guide

Clone the repository:

```
https://github.com/mohsenMahmoodzadeh/Lucene-Index-Searcher.git
```

Download java suitable for your machine from [here](https://www.oracle.com/java/technologies/javase/jdk15-archive-downloads.html). After installation, you can use the following command to check that java is installed correctly: 

```
java --version
# you should see something like these outputs on your command line interface:

# java 15.0.2 2021-01-19
# Java(TM) SE Runtime Environment (build 15.0.2+7-27)
# Java HotSpot(TM) 64-Bit Server VM (build 15.0.2+7-27, mixed mode, sharing)
```

Download lucene 7.4.0 from [here](https://archive.apache.org/dist/lucene/java/7.4.0/), unzip it and add these four **.jar** files to your project:

- `lucene-core-7.4.0.jar`: Lucene core library(located at *lucene-7.4.0/core*)
- `lucene-demo-7.4.0.jar`: Simple example code(located at *lucene-7.4.0/demo*) 
- `lucene-queryparser-7.4.0.jar`: Query parsers and parsing framework(located at *lucene-7.4.0/queryparser*)
- `lucene-analyzers-common-7.4.0.jar`: Analyzers for indexing content in different languages and domains(located at *lucene-7.4.0/analysis/common*)


## Usage Guide

Run main() method of Main.java, and enter the directories of data and index(the path you want to save).


## Future Works

- Rewrite the project with **PyLucene**, a Python extension for accessing Java Lucene™.

## Contributing

Fixes and improvements are more than welcome, so raise an issue or send a PR!
