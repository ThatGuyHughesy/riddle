# Performance

## Load Testing

Carried out using Apache JMeter.

1,000 request by 1,000 concurrent users = 1,000,0000 total requests.

Configuration used:

```clojure
{:server {:host "localhost"
          :port 8080}
 :rules [{:when {:type :equal?
                 :path [:request-method]
                 :value :delete}
          :then {:type :deny}}]}
```

## Results

### Without core.async

#### Throughput

335,752/minute

#### Latency

Average: 131ms
Median: 34ms

### With core.async

#### Throughput

614,049/minute

#### Latency

Average: 76ms
Median: 31ms