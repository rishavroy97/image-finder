# ImageFinder

ImageFinder is a Spring Boot web application that exposes a GraphQL endpoint. The application leverages a multi-threaded BFS crawl on a given website URL and retrieves a list of all images found on the site up to a specified depth level.

## Features

- **GraphQL API**: Easily query for images from a specified URL and depth.
- **Web Crawler**: Efficiently navigates web pages to gather image resources.
- **Customizable Depth**: Specify how deep the crawler should go when searching for images.
- **PostgreSQL Database**: SQL Database storage to save crawled website data.

## Getting Started

### Prerequisites

- **Java 21**
- **Maven** for building the project
- **Docker** for running the project along with a PostgreSQL Database

### Configuration

- **Application Properties**:
  - Copy the base properties from `src/main/resources/application.properties.example`
  - Configure the application in the `src/main/resources/application.properties` by updating username and password
  - Enter the db username and password in `docker-compose.yml`

### Installation

1. **Clone the repository**:

    ```bash
    git clone https://github.com/rishavroy97/imagefinder.git
    cd imagefinder
    ```

2. **Build the project** using Maven:

    ```bash
    mvn clean install
    ```

3. **Start** the Docker containers:

    ```bash
    docker-compose up --build
    ```

### GraphQL Endpoint

The application exposes a GraphQL endpoint at:

[http://localhost:8080/graphql](http://localhost:8080/graphql)


#### Example Query

To retrieve images from a website, you can use the following query structure:

```graphql
query {
    findImages(website: {
        url: "https://pytorch.org/"
        name: "PyTorch"
        levels: 4
    }) {
        id
        name
        url
        images {
            id
            url
        }
    }
}
```

## License
This project is licensed under the MIT License - see the [LICENSE](./LICENSE) file for details.
