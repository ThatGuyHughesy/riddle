# Configuration

## Server

The HTTP server proxying the requests.

* `host` - server IP/hostname e.g. `localhost` or `192.168.1.100`.
* `port` - server port e.g. `8080`.

For example:

```clojure
{:server {:host "localhost"
          :port 8080}}
```

## Rules

Instructions to apply to each request - following "when this" -> "then this" logic.

Each rule consists of a `when` and `then`.

### When

What to match

* `type` - One of `:equal?` `:not-equal?` `:greater-than?` `:less-than?` `:exist?` `:not-exist?`.
* `path` - Path to field in request e.g. `[:headers :authentication :username]`.
* `value` - Absolute value used in comparison e.g. `:get` or `true`.

### Then

What to do

* `type` - One of `:insert` `:remove` `:increment` `:decrement` `:allow` `:deny`.
* `path` (Optional) - Path to field in request e.g. `[:body :password]`.
* `value` (Optional) - Absolute value used by functions.

For example:

```clojure
{:rules [{:when {:type :not-equal?
                 :path [:uri]
                 :value "localhost:8080"}
          :then {:type :insert
                 :path [:uri]
                 :value "localhost:8080"}}
         {:when {:type :equal?
                 :path [:request-method]
                 :value :delete}
          :then {:type :deny}}]}
```