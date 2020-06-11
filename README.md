# Demo project for Elasticsearch Rest High Level Client & Spring Data Elasticsearch studies

## Loading data

### Loading 1 million mock products
POST /products/load/full

### Loading few mock products
POST /products/load

## Searching

### Full-text search
GET /products/full-text-search?text=*

### Phrase search
GET /products/phrase-search

### Fuzzy search
GET /products/fuzzy-search

### Partial-matching search
GET /products/partial-matching-search

### Search-as-you-type
GET /products/search-as-you-type

### Common paged filtering search
GET /products
