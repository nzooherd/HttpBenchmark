# HttpBenchmark
Wrk is a well-known HTTP benchmarking tool. However, when I attempted to use it for stress testing 
my s3 object store service. I discovered that its functionality was rather limited.

S3 is a Data/IO-intensive service. And it can have various performance bottleneck, 
including cpu, disk, and network-related issues.
Difference factors can result in varied HTTP response times.

For S3 "put object" request, if there is a significant delay from the connection establishment 
to the initiation of transferring HTTP headers, it may indicate a queuing delay.
increasing the handler thread size on the server can be beneficial.
Additionally, if the upload time of http body is longer than presupposition.
The server bandwidth may be exhausted.

While Wrk is a high performance tool, but it offers only a limited of metrics, 
which may not be sufficient for in-depth server performance analysis.
So I develop the HttpBenchmark project, which offers the following key features:

* Record the time taken for all http lifecycles.
* Achieving a million concurrent request with the assistance of coroutines.
* Automatically generate stress test HTML report.
* Providing a high degree of customization, such as sending requests to a cluster and adding custom QPS strategy.

## HTTP Request
We all know HTTP request includes HTTP request line, header and body, so does http response.
Each part of http transport time is necessary to analyze server bottleneck. 
HttpBenchmark will track timestamp of HTTP lifecycle.
```Kotlin
enum class HttpLifeCycle {
    CALL_START,
    REQUEST_HEADER_START,
    REQUEST_HEADER_END,
    REQUEST_BODY_START,
    REQUEST_BODY_END,
    RESPONSE_HEADER_START,
    RESPONSE_HEADER_END,
    RESPONSE_BODY_START,
    RESPONSE_BODY_END,
    REQUEST_FAILED,
}
```
## Performance

## Basic Usage

## Advance Usage
### Strategy
### Analyzer
### Cluster

## Architecture
