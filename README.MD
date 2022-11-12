# Generator of QUIC requests

CLI tool to generate load profile of QUIC requests.
It allows start performance tests with different RPS for HTTP and QUIC server.
This util was build on:

* [flupke](https://bitbucket.org/pjtr/flupke.git) library with QUIC implementation
* [bucket4j](https://github.com/bucket4j/bucket4j) rate limiting library
* [picocli](https://github.com/remkop/picocli)

## Usage

```
Usage: quic-load-generator run [-k] [-c=REQUESTS_COUNT] [-o=<outFile>]
                               [-pt=<protocolType>] -r=RATE [-s=SECONDS]
                               [-st=<scenarioType>] <serverUri>
Start execution process
      <serverUri>
  -c, --count=REQUESTS_COUNT
                          Total requests count in experiment
  -k, --insecure          Disable certificate verification
  -o, --out=<outFile>     output for CSV results
      -pt, --protocol-type=<protocolType>
                          Protocol type
  -r, --rps=RATE          requests per second
  -s, --seconds=SECONDS   Experiment duration in seconds
      -st, --scenario-type=<scenarioType>
                          Scenario type
```

To run with limited request counts(30 requests with 10 RPS in 3 seconds):

    java -jar quic-load-generator.jar run -r=10 -c=30 https://localhost:6121


To run with limited execution time(30 total requests in 30 seconds with 1 RPS):

    java -jar quic-load-generator.jar run -r=1 -s=30 https://localhost:6121
    
