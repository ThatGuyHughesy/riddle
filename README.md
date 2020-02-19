# riddle

Rule based HTTP request proxy and filter written in Clojure.

[![Build Status](https://travis-ci.org/ThatGuyHughesy/riddle.svg?branch=master)](https://travis-ci.org/ThatGuyHughesy/riddle)

## Development

### Local

Start repl

```sh
$ lein repl
```

Start system

```sh
riddle.core=> (-main "resources/config.edn")
```

### Testing

Run tests

```sh
$ lein test
```

## Deployment

### Building

```sh
$ lein uberjar
```

### Running

```sh
$ java -jar riddle.jar config.edn
```

## Configuration

Checkout how to configure Riddle [here](https://github.com/ThatGuyHughesy/riddle/blob/master/doc/configuration.md).

## Performance

Checkout Riddle's performance [here](https://github.com/ThatGuyHughesy/riddle/blob/master/doc/performance.md).

## Contributing

Want to become a Riddle [contributor](https://github.com/ThatGuyHughesy/riddle/blob/master/CONTRIBUTORS.md)?  
Then checkout our [code of conduct](https://github.com/ThatGuyHughesy/riddle/blob/master/CODE_OF_CONDUCT.md) and [contributing guidelines](https://github.com/ThatGuyHughesy/riddle/blob/master/CONTRIBUTING.md).

## License

Copyright (c) 2020 Conor Hughes - Released under the MIT license.