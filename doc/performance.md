# Performance

## Load Testing

Carried out using Apache JMeter.

1,000 request by 1,000 concurrent users = 1,000,0000 total requests.

Configuration used:

```clojure
{:server {:host "localhost"
          :port 8080}
 :rules [{:when {:type :equals
                 :path [:request-method]
                 :value :delete}
          :then {:type :deny}}]}
```

## Results

### Without core.async

#### Throughput

290,376/minute

#### Latency

162ms

### With core.async

#### Throughput

595,787/minute

#### Latency

82ms