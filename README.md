# Microservice Name
### TMF-641 Compliance

## Description
In business terms, **Order Management** (OM) refers to the set of activities that occur between the time a company receives a customer's **order** until it is delivered and the customer has been satisfied. The **orders** might contain products, **services** or any mix of them.


## Specification
This microservice is exposed on two of API technologies. Rest API for operations existing in TMF-641 Service Ordering Management, and aditional API Graphql for queries purpose

This microservice is based on TMF-641 specification in version [version] available on. [TMF Forum - TMF641](https://www.tmforum.org/resources/specification/tmf641-service-ordering-api-user-guide-v-4-0-0/) 

View our [OpenAPI or Swagger ](http://host:port/swager-ui.html) details
View our [Graphql](http://host:port/graphql) details

### Operations

- **listServiceOrder** List all the ServiceOrders
- **retrieveServiceOrder** Retrieve a ServiceOrder by its ID
- **createServiceOrder** Create a new ServiceOrder
- **patchServiceOrder** Update an existing ServiceOrder
- **deleteServiceOrder** Update a service order to make it appear as deleted

## restrictions*
- Characteristics entity is restricted to one instance per Service, this is array with many characteristics* 

## requirements

### Develop

- Java JDK 11
- Maven
- IDE or Text Editor 


### Runtime
- Docker
- Neo4j instance URL
- Jaeger instance URL

## Architecture Diagram

All your files and folders are presented as a tree in the file explorer. You can switch from one to another by clicking a file in the tree.

![Microservice Diagram C4](https://steps.everis.com/git/fibercoassurancemodule/orchestration-activation/business-implementations/microservices/service-example/-/raw/master/images/c4_Model_MVP_v1.4-c3-gral-ms.png)

### Archetype & stack 
For the creation of microservices, a maven archetype has been created that allows to have a multi-layered java application with the following technologies:

- Java JDK 11
- Spring Boot 2.4.2
- Junit
- Mockito
- OpenAPI
***for more details check the archetype documentation** [here](https://steps.everis.com/git/fibercoassurancemodule/orchestration-activation/business-implementations/archetype/maven-archetype)


## Configuration

  
For the operation of the microservice, the following configuration parameters must be taken into account

- **com.fiberco.ordering.datasource.host**	IP or Host name of Neo4j database
- **com.fiberco.ordering.datasource.user** username for Neo4j Connection
- **com.fiberco.ordering.datasource.pass** pass for Neo4j Connection

## Testing
  
The unit tests of the microservice were carried out with the Mockito framework and the microservice is considered finished if it meets the following quality attributes:

- **coverage** it must be equal to or greater than 80%
- **bugs**
- **Code Smells**

### Integration  & Functional Test
This section should include the necessary needs to execute the tests, location, input data or authentication if necessary
- [Postman Collection](/src/test/resources)
 
## Model & Persistence
  
Define which are the main entities of the microservice and their main relationships

![Neo4j Model](https://steps.everis.com/git/fibercoassurancemodule/orchestration-activation/business-implementations/microservices/service-example/-/raw/1d69cddec2933a75cc3e26b070f45c68b04de5d0/images/TMF641-EXT%20%285%29.png)