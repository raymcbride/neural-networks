# neural-networks

This project focuses on the ability of the Multilayer Perceptron, the Time Delay Neural Network and the Recurrent Neural Network at forecasting end of day closing price of the FTSE 100 Index.

[![Build Status](https://travis-ci.org/raymcbride/neural-networks.svg?branch=master)](https://travis-ci.org/raymcbride/neural-networks)

## Requirements

- Java
- Java API for XML Processing (JAXP)
- Maven

## Quickstart

Install the project:

    mvn clean install

Run the project:

    mvn exec:java

The source code for this project is available here:

- BiasNeuron.java
- ContextNeuron.java
- ContextSynapse.java
- DataProcessor.java
- HiddenNeuron.java
- InputNeuron.java
- MLP.java
- Network.java
- Neuron.java
- OutputFile.java
- OutputNeuron.java
- RNN.java
- Synapse.java
- TDNN.java
- Test.java
- XMLParser.java

## Documentation

There are several other additional files also included. These are:

- The Javadoc
- The data files Train500.xml, Test100.xml, Validate100.xml and Forecast5.xml
- The project schema and XSL stylesheet
- An ant build.xml
- A maven pom.xml

## Further Reading

[Project Report](https://raymcbride.com/downloads/RM_MSc_Project_Report_2004.pdf)
